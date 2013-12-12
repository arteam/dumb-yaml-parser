package test.data;

import org.dumb.yaml.annotation.Names;

import org.dumb.yaml.util.Objects;

/**
 * Date: 11/21/13
 * Time: 11:04 PM
 *
 * @author Artem Prigoda
 */
@Names({"key", "trash"})
public class StringPrimitivesNames {

    private String keyValue;

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
        if (obj instanceof StringPrimitivesNames) {
            StringPrimitivesNames that = (StringPrimitivesNames) obj;
            return Objects.equals(keyValue, that.keyValue) &&
                    Objects.equals(trashValue, that.trashValue);
        }
        return false;
    }
}
