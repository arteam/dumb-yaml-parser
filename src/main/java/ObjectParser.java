import annotation.Name;
import annotation.ParamInfo;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Date: 11/21/13
 * Time: 10:01 PM
 *
 * @author Artem Prigoda
 */
public class ObjectParser {

    private YamlParser yamlParser = new YamlParser();
    private AnnotationResolver resolver = new AnnotationResolver();
    private Constructors constructors = new Constructors();
    private Types types = new Types();

    public <T> T parse(String source, Class<T> clazz) {
        YamlMap yamlMap = yamlParser.parse(source);
        return parse(yamlMap, clazz);
    }

    private <T> T parse(YamlMap yamlMap, Class<T> clazz) {
        Constructor<T> constructor = constructors.getConstructor(clazz);
        if (constructor.getParameterTypes().length == 0) {
            return injectByFields(yamlMap, constructor);
        } else {
            return injectByConstructor(yamlMap, constructor);
        }
    }

    private <T> T injectByFields(YamlMap yamlMap, Constructor<T> constructor) {
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

    private <T> T injectByConstructor(YamlMap yamlMap, Constructor<T> constructor) {
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


    @SuppressWarnings("all")
    private Object typedValue(YamlObject yamlObject, Class<?> type, Type[] actualTypes) {
        if (yamlObject instanceof YamlPrimitive) {
            YamlPrimitive primitive = (YamlPrimitive) yamlObject;
            return primitive.cast(type);
        } else if (yamlObject instanceof YamlMap) {
            return typedMap(yamlObject, type, actualTypes);
        } else if (yamlObject instanceof YamlList) {
            return typedList(yamlObject, type, actualTypes);
        }
        throw new IllegalStateException("Unknow yaml object=" + yamlObject);
    }

    private Object typedMap(YamlObject yamlObject, Class<?> type, Type[] actualTypes) {
        YamlMap yamlMap = (YamlMap) yamlObject;
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
            return parse(yamlMap, type);
        }
    }

    private Collection<Object> typedList(YamlObject yamlObject, Class<?> type, Type[] actualTypes) {
        YamlList yamlList = (YamlList) yamlObject;
        Collection<Object> collection = newCollection(type);
        for (YamlObject subObject : yamlList.getList()) {
            Type actualType = actualTypes[0];
            collection.add(typedValue(subObject, (Class<?>) actualType, types.getActualTypes(actualType)));
        }
        return collection;
    }

    @SuppressWarnings("unchecked")
    private Collection<Object> newCollection(Class<?> type) {
        if (!Collection.class.isAssignableFrom(type)) {
            throw new IllegalArgumentException(type + " is not collection");
        }
        if (!type.isInterface()) {
            try {
                return (Collection<Object>) type.newInstance();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
        if (List.class.isAssignableFrom(type)) {
            return new ArrayList<>();
        } else if (Set.class.isAssignableFrom(type)) {
            return new HashSet<>();
        }
        throw new IllegalStateException("Unknown type " + type);
    }
}
