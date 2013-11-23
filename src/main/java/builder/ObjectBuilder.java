package builder;

import annotation.Name;
import builder.util.Constructors;
import builder.util.Types;
import domain.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Date: 11/21/13
 * Time: 10:01 PM
 * Object builder from YAML map representations
 *
 * @author Artem Prigoda
 */
public class ObjectBuilder {

    private AnnotationResolver resolver = new AnnotationResolver();
    private Constructors constructors = new Constructors();
    private Types types = new Types();

    /**
     * Build an object representation based on YAML map
     */
    public <T> T build(YamlMap yamlMap, Class<T> clazz) {
        Constructor<T> constructor = constructors.getConstructor(clazz);
        if (constructor.getParameterTypes().length == 0) {
            return buildByFields(yamlMap, constructor);
        } else {
            return buildByConstructor(yamlMap, constructor);
        }
    }

    /**
     * Build an object by setting private fields
     */
    private <T> T buildByFields(YamlMap yamlMap, Constructor<T> constructor) {
        Map<String, YamlObject> map = yamlMap.getMap();
        try {
            T instance = constructor.newInstance();
            Field[] declaredFields = instance.getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                Name annotationName = field.getAnnotation(Name.class);
                String fieldName = annotationName != null ? annotationName.value() : field.getName();
                YamlObject yamlObject = map.get(fieldName);
                if (yamlObject != null) {
                    Type[] actualTypes = types.getActualTypes(field.getGenericType());
                    Object typedValue = typedValue(yamlObject, field.getType(), actualTypes);
                    field.setAccessible(true);
                    field.set(instance, typedValue);
                }
            }
            return instance;
        } catch (Exception e) {
            throw new IllegalStateException("Unable inject fields to " + constructor.getDeclaringClass(), e);
        }
    }

    /**
     * Build an object by invoking a constructor with parameters
     */
    private <T> T buildByConstructor(YamlMap yamlMap, Constructor<T> constructor) {
        Map<String, ParamInfo> argNameTypes = resolver.lookupParameterNames(constructor);

        Object[] args = new Object[argNameTypes.size()];
        for (Map.Entry<String, YamlObject> entry : yamlMap.getMap().entrySet()) {
            // Remove for checking that all arguments have been set
            ParamInfo paramInfo = argNameTypes.remove(entry.getKey());
            if (paramInfo == null) continue;

            Object valueToInject = typedValue(entry.getValue(), paramInfo.getType(), paramInfo.getActualTypes());
            args[paramInfo.getPos()] = valueToInject;
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
    private Object typedValue(YamlObject yamlObject, Class<?> type, Type[] actualTypes) {
        if (yamlObject instanceof YamlPrimitive) {
            return typedPrimitive((YamlPrimitive) yamlObject, type);
        } else if (yamlObject instanceof YamlMap) {
            return typedMap((YamlMap) yamlObject, type, actualTypes);
        } else if (yamlObject instanceof YamlList) {
            return typedList((YamlList) yamlObject, type, actualTypes);
        }
        throw new IllegalStateException("Unknown yaml object=" + yamlObject);
    }

    /**
     * Cast YAML primitive to actual type
     */
    private Object typedPrimitive(YamlPrimitive primitive, Class<?> type) {
        return primitive.cast(type);
    }

    /**
     * Convert YAML list to type-safe list
     */
    private Object typedList(YamlList yamlList, Class<?> type, Type[] actualTypes) {
        Type actualType = actualTypes[0];
        Collection<Object> collection = types.newCollection(type);
        for (YamlObject subObject : yamlList.getList()) {
            collection.add(typedValue(subObject, (Class<?>) actualType, types.getActualTypes(actualType)));
        }
        return collection;
    }

    /**
     * Convert YAML map to type-safe map or composite object
     */
    private Object typedMap(YamlMap yamlMap, Class<?> type, Type[] actualTypes) {
        if (Map.class.isAssignableFrom(type)) {
            // If inner map
            Map<Object, Object> map = new HashMap<>();
            for (Map.Entry<String, YamlObject> entry : yamlMap.getMap().entrySet()) {
                String key = entry.getKey();
                YamlObject value = entry.getValue();
                if (!actualTypes[0].equals(String.class)) {
                    throw new IllegalArgumentException("Maps can have only Strings as keys, not " + actualTypes[0]);
                }
                Type actualType = actualTypes[1];  // Map value
                map.put(key, typedValue(value, (Class<?>) actualType, types.getActualTypes(actualType)));
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