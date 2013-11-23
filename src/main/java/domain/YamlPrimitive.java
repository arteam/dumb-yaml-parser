package domain;

import annotation.EnumConverter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Date: 11/19/13
 * Time: 9:51 PM
 *
 * @author Artem Prigoda
 */
public class YamlPrimitive implements YamlObject {

    private String value;

    public YamlPrimitive(String value) {
        this.value = value;
    }

    /**
     * Cast object to extended types (Enum, Date...)
     */
    public Object cast(Class<?> type, Annotation... annotations) {
        Object o = castToBaseType(type);
        if (o == null) {
            if (Enum.class.isAssignableFrom(type)) {
                return castToEnum(type, annotations);
            }
        }
        return o;
    }

    /**
     * Cast object to base class (primitives + strings)
     */
    public Object cast(Class<?> type) {
        Object o = castToBaseType(type);
        if (o == null) {
            throw new IllegalArgumentException("type=" + type + " is not primitive");
        }
        return 0;
    }

    private Object castToBaseType(Class<?> type) {
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

    private Object castToEnum(Class<?> type, Annotation[] annotations) {
        String valueOfMethod = "valueOf";
        for (Annotation ann : annotations) {
            if (ann.annotationType() == EnumConverter.class) {
                EnumConverter enumConverter = (EnumConverter) ann;
                valueOfMethod = enumConverter.value();
                break;
            }
        }
        try {
            Method method = type.getMethod(valueOfMethod, String.class);
            method.setAccessible(true);
            Object enumObject = method.invoke(null, value);
            if (enumObject == null) {
                throw new IllegalArgumentException("No enum with value=" + value);
            }
            return enumObject;
        } catch (Exception e) {
            throw new IllegalStateException("Unable invoke valueOf method of enum " + type, e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof YamlPrimitive) {
            YamlPrimitive that = (YamlPrimitive) o;
            return Objects.equals(value, that.value);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return "YamlPrimitive{value=" + value + "}";
    }
}
