package test.data;

import org.dumb.yaml.annotation.Name;

import util.Objects;

/**
 * Date: 11/21/13
 * Time: 11:04 PM
 *
 * @author Artem Prigoda
 */
public class StringPrimitives {

    private final String key;
    private final String trash;

    public StringPrimitives(@Name("key") String key, @Name("trash") String trash) {
        this.key = key;
        this.trash = trash;
    }

    @Override
    public String toString() {
        return "StringPrimitives{key=" + key + ",trash=" + trash + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StringPrimitives) {
            StringPrimitives that = (StringPrimitives) obj;
            return Objects.equals(key, that.key) &&
                    Objects.equals(trash, that.trash);
        }
        return false;
    }
}
