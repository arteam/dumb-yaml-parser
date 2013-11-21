package annotation;

/**
 * Date: 11/21/13
 * Time: 11:38 PM
 *
 * @author Artem Prigoda
 */
public class ParamInfo {

    int pos;
    Class<?> type;

    public ParamInfo(int pos, Class<?> type) {
        this.pos = pos;
        this.type = type;
    }

    public int getPos() {
        return pos;
    }

    public Class<?> getType() {
        return type;
    }
}
