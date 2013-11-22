import annotation.AnnotationResolver;
import annotation.Name;
import annotation.ParamInfo;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Date: 11/21/13
 * Time: 10:01 PM
 *
 * @author Artem Prigoda
 */
public class ObjectParser {

    private YamlParser yamlParser = new YamlParser();
    private AnnotationResolver resolver = new AnnotationResolver();

    public <T> T parse(String source, Class<T> clazz) {
        YamlMap yamlMap = yamlParser.parse(source);
        return parse(yamlMap, clazz);
    }

    private <T> T parse(YamlMap yamlMap, Class<T> clazz) {
        Constructor<T> constructor = getConstructor(clazz);
        if (constructor.getParameterTypes().length == 0) {
            return injectByFields(yamlMap, constructor);
        } else {
            return injectByConstructor(yamlMap, constructor);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> Constructor<T> getConstructor(Class<T> clazz) {
        Constructor<T>[] constructors = (Constructor<T>[]) clazz.getDeclaredConstructors();
        Constructor<T> constructor = null;
        for (Constructor<T> c : constructors) {
            if (c.getParameterTypes().length == 0) {
                constructor = c;
                break;
            }
            constructor = c;
        }
        return constructor;
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
                    Type genericType = field.getGenericType();
                    Type[] actualTypes = genericType instanceof ParameterizedType ?
                            ((ParameterizedType) genericType).getActualTypeArguments() :
                            new Type[]{genericType};
                    Object typedValue = getTyped(yamlObject, field.getType(), actualTypes);
                    field.setAccessible(true);
                    field.set(instance, typedValue);
                }
            }
            return instance;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T injectByConstructor(YamlMap yamlMap, Constructor<T> constructor) {
        Map<String, ParamInfo> argNameTypes = resolver.lookupParameterNames(constructor);
        Object[] initArgs = getConstructorArgs(yamlMap, argNameTypes);
        constructor.setAccessible(true);
        try {
            return constructor.newInstance(initArgs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object[] getConstructorArgs(YamlMap yamlMap, Map<String, ParamInfo> argNameTypes) {
        Object[] initArgs = new Object[argNameTypes.size()];
        for (Map.Entry<String, YamlObject> entry : yamlMap.getMap().entrySet()) {
            String key = entry.getKey();
            YamlObject value = entry.getValue();

            ParamInfo paramInfo = argNameTypes.get(key);
            Object injected = getTyped(value, paramInfo.getType(), paramInfo.getActualTypes());
            initArgs[paramInfo.getPos()] = injected;
        }
        return initArgs;
    }

    private Object getTyped(YamlObject yamlObject, Class<?> type, Type[] actualTypes) {
        if (yamlObject instanceof YamlPrimitive) {
            YamlPrimitive primitive = (YamlPrimitive) yamlObject;
            return primitive.cast(type);
        } else if (yamlObject instanceof YamlMap) {
            YamlMap yamlMap = (YamlMap) yamlObject;
            return parse(yamlMap, type);
        } else if (yamlObject instanceof YamlList) {
            YamlList yamlList = (YamlList) yamlObject;
            Collection<Object> list = new ArrayList<>();
            for (YamlObject subObject : yamlList.getList()) {
                Type actualType;
                Type[] subTypes;
                if (actualTypes != null && actualTypes.length > 0) {
                    actualType = actualTypes[0];
                    subTypes = new Type[actualTypes.length - 1];
                    System.arraycopy(actualTypes, 1, subTypes, 0, actualTypes.length - 1);
                } else {
                    actualType = String.class;
                    subTypes = null;
                }
                list.add(getTyped(subObject, (Class) actualType, subTypes));
            }
            return list;
        }
        return null;
    }
}
