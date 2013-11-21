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

    public String getValue() {
        return value;
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
