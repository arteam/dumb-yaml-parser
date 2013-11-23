package test.data;

import annotation.Name;

import java.util.Objects;

/**
 * Date: 11/21/13
 * Time: 11:04 PM
 *
 * @author Artem Prigoda
 */
public class WrongStringPrimitives {

    private final String key;
    private final String trash;

    public WrongStringPrimitives(@Name("key1") String key, @Name("trash") String trash) {
        this.key = key;
        this.trash = trash;
    }

    @Override
    public String toString() {
        return "StringPrimitives{key=" + key + ",trash=" + trash + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof WrongStringPrimitives) {
            WrongStringPrimitives that = (WrongStringPrimitives) obj;
            return Objects.equals(key, that.key) &&
                    Objects.equals(trash, that.trash);
        }
        return false;
    }
}
