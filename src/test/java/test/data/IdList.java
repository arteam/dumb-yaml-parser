package test.data;

import org.dumb.yaml.annotation.Name;

import java.util.List;

/**
 * Date: 11/22/13
 * Time: 9:47 PM
 *
 * @author Artem Prigoda
 */
public class IdList {

    private List<Integer> ids;

    public IdList(@Name("ids") List<Integer> ids) {
        this.ids = ids;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IdList idList = (IdList) o;

        if (ids != null ? !ids.equals(idList.ids) : idList.ids != null) return false;

        return true;
    }

    @Override
    public String toString() {
        return "IdList{" +
                "ids=" + ids +
                '}';
    }
}
