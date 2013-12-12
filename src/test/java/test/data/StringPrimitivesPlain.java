package test.data;

import org.dumb.yaml.util.Objects;

/**
 * Date: 11/21/13
 * Time: 11:04 PM
 *
 * @author Artem Prigoda
 */
public class StringPrimitivesPlain {

    private String key;
    private String trash;

    @Override
    public String toString() {
        return "StringPrimitives{key=" + key + ",trash=" + trash + "}";
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setTrash(String trash) {
        this.trash = trash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StringPrimitivesPlain) {
            StringPrimitivesPlain that = (StringPrimitivesPlain) obj;
            return Objects.equals(key, that.key) &&
                    Objects.equals(trash, that.trash);
        }
        return false;
    }
}
