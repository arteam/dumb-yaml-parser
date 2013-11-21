package annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
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

    public Map<String, ParamInfo> lookupParameterNames(Constructor<?> constructor) {
        Class<?>[] types = constructor.getParameterTypes();
        Annotation[][] anns = constructor.getParameterAnnotations();

        if (types.length == 0) {
            return Collections.emptyMap();
        }

        Map<String, ParamInfo> paramTypes = new LinkedHashMap<>();
        for (int i = 0; i < types.length; i++) {
            for (int j = 0; j < anns[i].length; j++) {
                Annotation ann = anns[i][j];
                if (!isNamed(ann)) continue;

                String namedValue = getNamedValue(ann);
                if (paramTypes.containsKey(namedValue)) {
                    throw new IllegalArgumentException("Constructor " + constructor +
                            " has 2 params with the same name name=" + namedValue);
                }
                paramTypes.put(namedValue, new ParamInfo(i, types[i]));
                break;
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
