package test.data;

import org.dumb.yaml.annotation.Name;
import org.dumb.yaml.annotation.Names;

import java.util.List;

/**
 * Date: 11/22/13
 * Time: 9:47 PM
 *
 * @author Artem Prigoda
 */
@Names("ids")
public class IdListDoubleNames {

    private List<Integer> ids;

    @Names("ids")
    public IdListDoubleNames(List<Integer> ids) {
        this.ids = ids;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IdListDoubleNames idList = (IdListDoubleNames) o;

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
