package builder;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Date: 11/23/13
 * Time: 12:05 AM
 * Utils for work with types
 *
 * @author Artem Prigoda
 */
class Types {

    public Type[] getActualTypes(Type genericType) {
        return genericType instanceof ParameterizedType ?
                ((ParameterizedType) genericType).getActualTypeArguments() :
                new Type[]{genericType};
    }


    /**
     * Create new collection based on type
     */
    @SuppressWarnings("unchecked")
    public Collection<Object> newCollection(Class<?> type) {
        if (!Collection.class.isAssignableFrom(type)) {
            throw new IllegalArgumentException("Unable assign collection to " + type);
        }
        if (!type.isInterface()) {
            try {
                return (Collection<Object>) type.newInstance();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
        if (List.class.isAssignableFrom(type)) {
            return new ArrayList<>();
        } else if (Set.class.isAssignableFrom(type)) {
            return new HashSet<>();
        }
        throw new IllegalStateException("Unknown type " + type);
    }
}
