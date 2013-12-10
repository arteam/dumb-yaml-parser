package test.data;

import org.dumb.yaml.annotation.Name;
import org.dumb.yaml.annotation.Names;

import java.util.Objects;

/**
 * Date: 11/21/13
 * Time: 11:04 PM
 *
 * @author Artem Prigoda
 */
@Names
public class StringPrimitivesEmptyNames {

    private final String key;
    private final String trash;

    public StringPrimitivesEmptyNames(String key, String trash) {
        this.key = key;
        this.trash = trash;
    }

    @Override
    public String toString() {
        return "StringPrimitives{key=" + key + ",trash=" + trash + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StringPrimitivesEmptyNames) {
            StringPrimitivesEmptyNames that = (StringPrimitivesEmptyNames) obj;
            return Objects.equals(key, that.key) &&
                    Objects.equals(trash, that.trash);
        }
        return false;
    }
}
