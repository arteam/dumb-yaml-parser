import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Date: 11/23/13
 * Time: 12:05 AM
 *
 * @author Artem Prigoda
 */
public class Types {

    public Type[] getActualTypes(Type genericType) {
        return genericType instanceof ParameterizedType ?
                ((ParameterizedType) genericType).getActualTypeArguments() :
                new Type[]{genericType};
    }
}
