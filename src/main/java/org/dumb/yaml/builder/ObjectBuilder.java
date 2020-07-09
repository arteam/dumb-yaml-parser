package org.dumb.yaml.builder;

import org.dumb.yaml.annotation.Name;
import org.dumb.yaml.annotation.Names;
import org.dumb.yaml.domain.YamlList;
import org.dumb.yaml.domain.YamlMap;
import org.dumb.yaml.domain.YamlObject;
import org.dumb.yaml.domain.YamlPrimitive;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.dumb.yaml.builder.AnnotationResolver.getAnnotation;

/**
 * Date: 11/21/13
 * Time: 10:01 PM
 * Object builder from YAML map representations
 *
 * @author Artem Prigoda
 */
public class ObjectBuilder {

    private final AnnotationResolver resolver = new AnnotationResolver();
    private final Constructors constructors = new Constructors();
    private final Types types = new Types();

    @SuppressWarnings("unchecked")
    public <T> List<T> buildList(@NotNull YamlList yamlList, @NotNull Class<T> clazz) {
        Type[] actualTypes = new Type[]{clazz};
        return (List<T>) typedList(yamlList, List.class, actualTypes, new Annotation[]{});
    }

    @SuppressWarnings("unchecked")
    public <T> Map<String, T> buildMap(@NotNull YamlMap yamlMap, @NotNull Class<T> clazz) {
        Type[] actualTypes = new Type[]{String.class, clazz};
        return (Map<String, T>) typedMap(yamlMap, Map.class, actualTypes, new Annotation[]{});
    }

    /**
     * Build an object representation based on YAML map
     */
    @NotNull
    public <T> T build(@NotNull YamlMap yamlMap, @NotNull Class<T> clazz) {
        Constructor<T> constructor = constructors.getConstructor(clazz);
        if (constructor.getParameterTypes().length == 0) {
            if (getAnnotation(clazz.getDeclaredAnnotations(), Names.class) != null
                    || getAnnotation(constructor.getDeclaredAnnotations(), Names.class) != null) {
                throw new IllegalStateException("Annotation @Names can be present only on a class " +
                        "with a constructor with parameters");
            }
            return buildByFields(yamlMap, constructors.newInstance(constructor));
        } else {
            if (resolver.hasNameAnnotations(constructor)) {
                return buildByConstructor(yamlMap, constructor);
            } else {
                throw new IllegalStateException(clazz + " doesn't have default constructor or" +
                        " named constructor with params");

            }
        }
    }

    /**
     * Build an object by setting private fields
     */
    @NotNull
    private <T> T buildByFields(@NotNull YamlMap yamlMap, @NotNull T instance) {
        Map<String, YamlObject> map = yamlMap.getMap();
        try {
            Field[] declaredFields = instance.getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                Name annotationName = field.getAnnotation(Name.class);
                String fieldName = annotationName != null ? annotationName.value() : field.getName();
                YamlObject yamlObject = map.get(fieldName);
                if (yamlObject != null) {
                    Type[] actualTypes = types.getActualTypes(field.getGenericType());
                    Object typedValue = typedValue(yamlObject, field.getType(), actualTypes, field.getAnnotations());
                    field.setAccessible(true);
                    field.set(instance, typedValue);
                }
            }
            return instance;
        } catch (Exception e) {
            throw new IllegalStateException("Unable inject fields to " + instance.getClass(), e);
        }
    }

    /**
     * Build an object by invoking a constructor with parameters
     */
    @NotNull
    private <T> T buildByConstructor(@NotNull YamlMap yamlMap, @NotNull Constructor<T> constructor) {
        Map<String, ParamInfo> argNameTypes = resolver.lookupParameterNames(constructor);

        Object[] args = new Object[argNameTypes.size()];
        for (Map.Entry<String, YamlObject> entry : yamlMap.getMap().entrySet()) {
            // Remove for checking that all arguments have been set
            ParamInfo paramInfo = argNameTypes.remove(entry.getKey());
            if (paramInfo == null) continue;

            Object valueToInject = typedValue(entry.getValue(), paramInfo.type,
                    paramInfo.actualTypes, paramInfo.annotations);
            args[paramInfo.pos] = valueToInject;
        }
        if (!argNameTypes.isEmpty()) {
            throw new IllegalArgumentException(argNameTypes.keySet() + " are not set for " + constructor);
        }
        return constructors.newInstance(constructor, args);
    }

    /**
     * Convert yaml object to typed representation
     * If {@param type} is a collection then generic types passed to parameter {@param actualTypes}
     */
    @NotNull
    private Object typedValue(@NotNull YamlObject yamlObject, @NotNull Class<?> type,
                              @NotNull Type[] actualTypes, @NotNull Annotation[] annotations) {
        if (yamlObject instanceof YamlPrimitive) {
            return typedPrimitive((YamlPrimitive) yamlObject, type, annotations);
        } else if (yamlObject instanceof YamlMap) {
            return typedMap((YamlMap) yamlObject, type, actualTypes, annotations);
        } else if (yamlObject instanceof YamlList) {
            return typedList((YamlList) yamlObject, type, actualTypes, annotations);
        }
        throw new IllegalStateException("Unknown yaml object=" + yamlObject);
    }

    /**
     * Cast YAML primitive to actual type
     */
    @NotNull
    private Object typedPrimitive(@NotNull YamlPrimitive primitive, @NotNull Class<?> type,
                                  @NotNull Annotation[] annotations) {
        return primitive.cast(type, annotations);
    }

    /**
     * Convert YAML list to type-safe list
     */
    @NotNull
    private Object typedList(@NotNull YamlList yamlList, @NotNull Class<?> type,
                             @NotNull Type[] actualTypes, @NotNull Annotation[] annotations) {
        Type actualType = actualTypes[0];
        Collection<Object> collection = types.newCollection(type);
        for (YamlObject subObject : yamlList.getList()) {
            collection.add(typedValue(subObject, (Class<?>) actualType, types.getActualTypes(actualType), annotations));
        }
        return collection;
    }

    /**
     * Convert YAML map to type-safe map or composite object
     */
    @NotNull
    private Object typedMap(@NotNull YamlMap yamlMap, @NotNull Class<?> type,
                            @NotNull Type[] actualTypes, @NotNull Annotation[] annotations) {
        if (Map.class.isAssignableFrom(type)) {
            // If inner map
            Map<Object, Object> map = new LinkedHashMap<Object, Object>();
            for (Map.Entry<String, YamlObject> entry : yamlMap.getMap().entrySet()) {
                String key = entry.getKey();
                YamlObject value = entry.getValue();
                if (!actualTypes[0].equals(String.class)) {
                    throw new IllegalArgumentException("Maps can have only Strings as keys, not " + actualTypes[0]);
                }
                Type actualType = actualTypes[1];  // Map value
                map.put(key, typedValue(value, types.toClass(actualType), types.getActualTypes(actualType), annotations));
            }
            return map;
        } else {
            // If composite object
            if (type.isPrimitive() || type.isArray() || type.getName().startsWith("java.")) {
                throw new IllegalArgumentException("Class " + type + " cannot be represented as map");
            }
            return build(yamlMap, type);
        }
    }

}
