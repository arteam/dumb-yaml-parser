import annotation.Name;
import annotation.ParamInfo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Date: 11/21/13
 * Time: 11:17 PM
 *
 * @author Artem Prigoda
 */
public class AnnotationResolver {

    private Types typesUtil = new Types();

    public Map<String, ParamInfo> lookupParameterNames(Constructor<?> constructor) {
        Class<?>[] types = constructor.getParameterTypes();
        Type[] genericTypes = constructor.getGenericParameterTypes();
        Annotation[][] anns = constructor.getParameterAnnotations();

        if (types.length == 0) return Collections.emptyMap();

        Map<String, ParamInfo> paramTypes = new LinkedHashMap<>();
        for (int i = 0; i < types.length; i++) {
            boolean hasNameAnnotation = false;
            for (int j = 0; j < anns[i].length; j++) {
                Annotation ann = anns[i][j];
                if (!isNamed(ann)) continue;

                String namedValue = getNamedValue(ann);
                if (paramTypes.containsKey(namedValue)) {
                    throw new IllegalArgumentException("Constructor " + constructor +
                            " has 2 params with the same name name=" + namedValue);
                }

                Type[] actualTypes = typesUtil.getActualTypes(genericTypes[i]);
                paramTypes.put(namedValue, new ParamInfo(i, types[i], actualTypes));
                hasNameAnnotation = true;
                break;
            }
            if (!hasNameAnnotation) {
                throw new IllegalArgumentException("Name not set for " + (i + 1) + " param in " + constructor);
            }
        }
        return paramTypes;
    }


    protected String getNamedValue(Annotation ann) {
        return ((Name) ann).value();
    }

    protected boolean isNamed(Annotation ann) {
        return ann.annotationType().equals(Name.class);
    }
}
