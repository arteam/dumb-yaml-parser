import annotation.AnnotationResolver;
import annotation.Name;
import annotation.ParamInfo;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
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

        Constructor<T> constructor = getConstructor(clazz);
        if (constructor.getParameterTypes().length == 0) {
            return injectByFields(constructor, clazz, yamlMap);
        } else {
            return injectByConstructor(constructor, clazz, yamlMap);
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

    private <T> T injectByFields(Constructor<T> constructor, Class<T> clazz, YamlMap yamlMap) {
        Map<String, YamlObject> map = yamlMap.getMap();
        try {
            T instance = constructor.newInstance();
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field field : declaredFields) {
                Name annotationName = field.getAnnotation(Name.class);
                String fieldName = annotationName != null ? annotationName.value() : field.getName();
                YamlObject yamlObject = map.get(fieldName);
                if (yamlObject != null) {
                    Object typedValue = getTyped(yamlObject, field.getType());
                    field.setAccessible(true);
                    field.set(instance, typedValue);
                }
            }
            return instance;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T injectByConstructor(Constructor<T> constructor, Class<T> clazz, YamlMap yamlMap) {
        Map<String, ParamInfo> argNameTypes = resolver.lookupParameterNames(constructor);
        Object[] initArgs = getConstructorArgs(yamlMap, argNameTypes);
        constructor.setAccessible(true);
        try {
            return (T) constructor.newInstance(initArgs);
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
            Object injected = getTyped(value, paramInfo.getType());
            initArgs[paramInfo.getPos()] = injected;
        }
        return initArgs;
    }

    private Object getTyped(YamlObject yamlObject, Class<?> type) {
        if (yamlObject instanceof YamlPrimitive) {
            YamlPrimitive primitive = (YamlPrimitive) yamlObject;
            return primitive.cast(type);
        }
        return null;
    }
}
