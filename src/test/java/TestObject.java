import annotation.Name;

import java.util.Objects;

/**
 * Date: 11/21/13
 * Time: 11:04 PM
 *
 * @author Artem Prigoda
 */
public class TestObject {

    private final String key;
    private final String trash;

    public TestObject(@Name("key") String key, @Name("trash") String trash) {
        this.key = key;
        this.trash = trash;
    }

    public String getKey() {
        return key;
    }

    public String getTrash() {
        return trash;
    }

    @Override
    public String toString() {
        return "TestObject{key=" + key + ",trash=" + trash + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TestObject) {
            TestObject that = (TestObject) obj;
            return Objects.equals(key, that.key) &&
                    Objects.equals(trash, that.trash);
        }
        return false;
    }

    @Override
    public int hashCode() {
       return Objects.hash(key, trash);
    }
}
