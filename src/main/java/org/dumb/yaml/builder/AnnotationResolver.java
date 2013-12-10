package org.dumb.yaml.builder;

import org.dumb.yaml.annotation.Name;
import org.dumb.yaml.annotation.Names;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Date: 11/21/13
 * Time: 11:17 PM
 * Arguments resolver from constructor
 *
 * @author Artem Prigoda
 */
public class AnnotationResolver {

    private Types typesUtil = new Types();

    /**
     * Parse annotations from constructor args and
     * build map with info about arguments position and their actual types
     */
    public Map<String, ParamInfo> lookupParameterNames(Constructor<?> constructor) {
        Class<?>[] types = constructor.getParameterTypes();
        Type[] genericTypes = constructor.getGenericParameterTypes();
        Annotation[][] anns = constructor.getParameterAnnotations();
        String[] namesParams = getNamesParams(constructor);

        if (types.length == 0) return Collections.emptyMap();

        Map<String, ParamInfo> paramTypes = new LinkedHashMap<String, ParamInfo>();
        for (int i = 0; i < types.length; i++) {
            String namedValue = null;
            // If @Names annotation present then use it
            if (namesParams != null && namesParams.length > i) {
                namedValue = namesParams[i];
            } else {
                Name name = getAnnotation(anns[i], Name.class);
                if (name != null) namedValue = name.value();
            }
            if (namedValue == null) {
                throw new IllegalArgumentException("Name not set for " + (i + 1) + " param in " + constructor);
            }
            if (paramTypes.containsKey(namedValue)) {
                throw new IllegalArgumentException("Constructor " + constructor +
                        " has 2 params with the same name name=" + namedValue);
            }
            Type[] actualTypes = typesUtil.getActualTypes(genericTypes[i]);
            paramTypes.put(namedValue, new ParamInfo(i, types[i], actualTypes, anns[i]));
        }
        return paramTypes;
    }

    /**
     * Parse parameters from @Names annotation on class or constructor
     */
    private String[] getNamesParams(Constructor<?> constructor) {
        Names constructorNames = getAnnotation(constructor.getDeclaredAnnotations(), Names.class);
        Names classNames = getAnnotation(constructor.getDeclaringClass().getDeclaredAnnotations(), Names.class);
        if (constructorNames != null && classNames != null) {
            throw new IllegalStateException("Annotation @Names can be present either on a class or on a constructor");
        }
        if (constructorNames == null && classNames == null) {
            return null;
        }
        return (classNames != null ? classNames : constructorNames).value();
    }

    @SuppressWarnings("unchecked")
    public static <T extends Annotation> T getAnnotation(Annotation[] array, Class<T> clazz) {
        if (array == null) return null;
        for (Annotation a : array) {
            if (a.annotationType().equals(clazz)) {
                return (T) a;
            }
        }
        return null;
    }
}
