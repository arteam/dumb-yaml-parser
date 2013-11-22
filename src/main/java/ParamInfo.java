import java.lang.reflect.Type;

/**
 * Date: 11/21/13
 * Time: 11:38 PM
 *
 * @author Artem Prigoda
 */
public class ParamInfo {

    /**
     * Argument position in constructor
     */
    private int pos;

    /**
     * Type of argument
     */
    private Class<?> type;

    /**
     * Actual types (if type is collection: List, Map)
     */
    private Type[] actualTypes;

    public ParamInfo(int pos, Class<?> type, Type[] actualTypes) {
        this.pos = pos;
        this.type = type;
        this.actualTypes = actualTypes;
    }

    public int getPos() {
        return pos;
    }

    public Class<?> getType() {
        return type;
    }

    public Type[] getActualTypes() {
        return actualTypes;
    }
}
