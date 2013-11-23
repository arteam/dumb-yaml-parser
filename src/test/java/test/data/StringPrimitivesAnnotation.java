package test.data;

import org.dumb.yaml.annotation.Name;

import java.util.Objects;

/**
 * Date: 11/21/13
 * Time: 11:04 PM
 *
 * @author Artem Prigoda
 */
public class StringPrimitivesAnnotation {

    @Name("key")
    private String keyValue;

    @Name("trash")
    private String trashValue;

    @Override
    public String toString() {
        return "StringPrimitives{key=" + keyValue + ",trash=" + trashValue + "}";
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }

    public void setTrashValue(String trashValue) {
        this.trashValue = trashValue;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StringPrimitivesAnnotation) {
            StringPrimitivesAnnotation that = (StringPrimitivesAnnotation) obj;
            return Objects.equals(keyValue, that.keyValue) &&
                    Objects.equals(trashValue, that.trashValue);
        }
        return false;
    }
}
