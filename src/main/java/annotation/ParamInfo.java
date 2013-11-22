package annotation;

import java.lang.reflect.Type;

/**
 * Date: 11/21/13
 * Time: 11:38 PM
 *
 * @author Artem Prigoda
 */
public class ParamInfo {

    int pos;
    Class<?> type;
    Type[] actualTypes;

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
