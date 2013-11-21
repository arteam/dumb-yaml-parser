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

    public Object cast(Class<?> type) {
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
            } else {
               throw new IllegalArgumentException("type=" + type + " is not primitive");
            }
        } catch (Exception e) {
            throw new IllegalStateException("Unable assign value to type=" + type);
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
