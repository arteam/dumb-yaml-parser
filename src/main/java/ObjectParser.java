import annotation.AnnotationResolver;
import annotation.Name;
import annotation.ParamInfo;
import com.thoughtworks.paranamer.CachingParanamer;
import com.thoughtworks.paranamer.Paranamer;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        Constructor<?> constructor = getConstructor(clazz);
        Map<String, ParamInfo> argNameTypes = resolver.lookupParameterNames(constructor);

        YamlMap yamlMap = yamlParser.parse(source);
        Object[] initArgs = processArgs(yamlMap, argNameTypes);

        return newInstance(constructor, initArgs);
    }

    private Object[] processArgs(YamlMap yamlMap, Map<String, ParamInfo> argNameTypes) {
        Object[] initArgs = new Object[argNameTypes.size()];
        for (Map.Entry<String, YamlObject> entry : yamlMap.getMap().entrySet()) {
            String key = entry.getKey();
            YamlObject value = entry.getValue();
            if (value instanceof YamlPrimitive) {
                YamlPrimitive primitive = (YamlPrimitive) value;

                ParamInfo paramInfo = argNameTypes.get(key);
                Class type = paramInfo.getType();
                Object injected = null;
                if (type.equals(String.class)) {
                    injected = primitive.getValue();
                }
                initArgs[paramInfo.getPos()] = injected;
            }
        }
        return initArgs;
    }

    private <T> Constructor<?> getConstructor(Class<T> clazz) {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        if (constructors.length == 0) {
            throw new RuntimeException("No constructors");
        }

        Constructor<?> constructor = null;
        for (Constructor<?> c : constructors) {
            if (c.getParameterTypes().length == 0) {
                constructor = c;
                break;
            }
            constructor = c;
        }
        return constructor;
    }

    @SuppressWarnings("unchecked")
    private <T> T newInstance(Constructor constructor, Object[] initArgs) {
        constructor.setAccessible(true);
        try {
            return (T) constructor.newInstance(initArgs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
