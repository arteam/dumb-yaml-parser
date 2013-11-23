package test.data;

import org.dumb.yaml.annotation.Name;

import java.util.Objects;

/**
 * Date: 11/21/13
 * Time: 11:04 PM
 *
 * @author Artem Prigoda
 */
public class TooManyConstructors {

    private final String key;
    private final String trash;

    public TooManyConstructors(@Name("key") String key, @Name("trash") String trash) {
        this.key = key;
        this.trash = trash;
    }

    public TooManyConstructors(@Name("key") Integer key, @Name("trash") Integer trash) {
        this.key = key.toString();
        this.trash = trash.toString();
    }

    @Override
    public String toString() {
        return "StringPrimitives{key=" + key + ",trash=" + trash + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TooManyConstructors) {
            TooManyConstructors that = (TooManyConstructors) obj;
            return Objects.equals(key, that.key) &&
                    Objects.equals(trash, that.trash);
        }
        return false;
    }
}
