package org.dumb.yaml.builder;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.*;

/**
 * Date: 11/23/13
 * Time: 12:05 AM
 * Utils for work with types
 *
 * @author Artem Prigoda
 */
class Types {

    /**
     * Get actual type for maps/lists. Otherwise return current type
     */
    @NotNull
    public Type[] getActualTypes(@NotNull Type genericType) {
        return genericType instanceof ParameterizedType ?
                ((ParameterizedType) genericType).getActualTypeArguments() :
                new Type[]{genericType};
    }

    /**
     * Create new collection based on type
     */
    @SuppressWarnings("unchecked")
    @NotNull
    Collection<Object> newCollection(@NotNull Class<?> type) {
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
            return new ArrayList<Object>();
        } else if (Set.class.isAssignableFrom(type)) {
            return new HashSet<Object>();
        }
        throw new IllegalStateException("Unknown type " + type);
    }

    @NotNull
    Class toClass(@NotNull Type type) {
        if (type instanceof Class) return (Class) type;
        if (type instanceof WildcardType) {
            Type[] upperBounds = ((WildcardType) type).getUpperBounds();
            return (Class) upperBounds[0];
        }
        throw new IllegalStateException("Unsupported type: " + type);
    }
}
