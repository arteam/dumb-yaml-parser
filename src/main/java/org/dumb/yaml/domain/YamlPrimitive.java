package org.dumb.yaml.domain;

import org.dumb.yaml.annotation.DateConverter;
import org.dumb.yaml.annotation.EnumConverter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Date: 11/19/13
 * Time: 9:51 PM
 *
 * @author Artem Prigoda
 */
public class YamlPrimitive implements YamlObject {

    @NotNull
    private String value;

    public YamlPrimitive(@NotNull String value) {
        this.value = value;
    }

    /**
     * Cast object to extended types (Enum, Date...)
     */
    @NotNull
    public Object cast(@NotNull Class<?> type, @Nullable Annotation... annotations) {
        Object o = castToBaseType(type);
        if (o == null) {
            if (Enum.class.isAssignableFrom(type)) {
                return castToEnum(type, annotations);
            } else if (type == Date.class) {
                return castToDate(annotations);
            } else {
                throw new IllegalArgumentException("Unable represent " + type + " as plain value");
            }
        }
        return o;
    }

    /**
     * Cast to base type
     */
    @Nullable
    private Object castToBaseType(@NotNull Class<?> type) {
        try {
            if (type == String.class) {
                return value;
            } else if (type == int.class || type == Integer.class) {
                return Integer.valueOf(value);
            } else if (type == boolean.class || type == Boolean.class) {
                return Boolean.valueOf(value);
            } else if (type == float.class || type == Float.class) {
                return Float.valueOf(value);
            } else if (type == double.class || type == Double.class) {
                return Double.valueOf(value);
            } else if (type == long.class || type == Long.class) {
                return Long.valueOf(value);
            } else if (type == short.class || type == Short.class) {
                return Short.valueOf(value);
            } else if (type == char.class || type == Character.class) {
                return value.charAt(0);
            } else if (type == byte.class || type == Byte.class) {
                return Byte.valueOf(value);
            }
            return null;
        } catch (Exception e) {
            throw new IllegalArgumentException("type=" + type + " is not primitive");
        }
    }

    /**
     * Cast to enum based on valueOf method from annotation
     */
    @NotNull
    private Object castToEnum(@NotNull Class<?> type, @Nullable Annotation[] annotations) {
        EnumConverter enumConverter = getAnnotation(annotations, EnumConverter.class);
        String valueOfMethod = enumConverter != null ? enumConverter.value() : "valueOf";
        try {
            Method method = type.getMethod(valueOfMethod, String.class);
            method.setAccessible(true);
            Object enumObject = method.invoke(null, value);
            if (enumObject == null) {
                throw new IllegalArgumentException("No enum with value=" + value);
            }
            return enumObject;
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable invoke valueOf method of enum " + type, e);
        }
    }

    /**
     * Cast to date based on format from annotation
     */
    @NotNull
    private Object castToDate(@Nullable Annotation[] annotations) {
        DateConverter dateConverter = getAnnotation(annotations, DateConverter.class);
        String dateFormat = dateConverter != null ? dateConverter.value() : "yyyy-MM-dd HH:mm:ss";

        try {
            return new SimpleDateFormat(dateFormat).parse(value);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Unable format " + value + " by format: " + value, e);
        }
    }


    @Override
    public boolean equals(Object o) {
        if (o instanceof YamlPrimitive) {
            YamlPrimitive that = (YamlPrimitive) o;
            return value.equals(that.value);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "YamlPrimitive{value=" + value + "}";
    }

    @Nullable
    @SuppressWarnings("unchecked")
    private <T extends Annotation> T getAnnotation(@Nullable Annotation[] array, @NotNull Class<T> clazz) {
        if (array == null) return null;
        for (Annotation a : array) {
            if (a.annotationType().equals(clazz)) {
                return (T) a;
            }
        }
        return null;
    }
}
