import annotation.Name;

/**
 * Date: 11/22/13
 * Time: 9:47 PM
 *
 * @author Artem Prigoda
 */
public class IdListWrongType {

    private String ids;

    public IdListWrongType(@Name("ids") String ids) {
        this.ids = ids;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IdListWrongType that = (IdListWrongType) o;

        if (ids != null ? !ids.equals(that.ids) : that.ids != null) return false;

        return true;
    }

    @Override
    public String toString() {
        return "IdList{" +
                "ids=" + ids +
                '}';
    }
}
