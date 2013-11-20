import java.util.List;
import java.util.Objects;

/**
 * Date: 11/19/13
 * Time: 9:51 PM
 *
 * @author Artem Prigoda
 */
public class YamlList implements YamlObject {

    private List<YamlObject> list;

    public YamlList(List<YamlObject> list) {
        this.list = list;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof YamlList) {
            YamlList that = (YamlList) o;
            return Objects.equals(list, that.list);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(list);
    }

    @Override
    public String toString() {
        return "YamlList{list=" + list + "}";
    }
}
