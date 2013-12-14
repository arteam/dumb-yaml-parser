package org.dumb.yaml.builder;

import org.dumb.yaml.annotation.Name;
import org.dumb.yaml.annotation.Names;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.dumb.yaml.builder.AnnotationResolver.getAnnotation;

/**
 * Date: 11/23/13
 * Time: 12:01 AM
 * Utils for work with constructors
 *
 * @author Artem Prigoda
 */
class Constructors {

    /**
     * Get actual constructor from class. Default constructor has more high priority
     */
    @SuppressWarnings("unchecked")
    public <T> Constructor<T> getConstructor(Class<T> clazz) {
        Constructor<T>[] constructors = (Constructor<T>[]) clazz.getDeclaredConstructors();
        Constructor<T> constructor = null;
        for (Constructor<T> c : constructors) {
            // Default constructor
            if (c.getParameterTypes().length == 0) {
                constructor = c;
                break;
            }
            if (constructor != null) {
                throw new IllegalArgumentException("Too many constructors with parameters for " + clazz);
            }
            constructor = c;
        }
        return constructor;
    }

    public <T> T newInstance(Constructor<T> constructor, Object... args) {
        constructor.setAccessible(true);
        try {
            return constructor.newInstance(args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
