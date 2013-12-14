package org.dumb.yaml.builder;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Date: 11/21/13
 * Time: 11:38 PM
 *
 * @author Artem Prigoda
 */
class ParamInfo {

    /**
     * Argument position in constructor
     */
    private int pos;

    /**
     * Type of argument
     */
    @NotNull
    private Class<?> type;

    /**
     * Actual types (if type is collection: List, Map)
     */
    @NotNull
    private Type[] actualTypes;

    @NotNull
    private Annotation[] annotations;

    public ParamInfo(int pos, @NotNull Class<?> type, @NotNull Type[] actualTypes, @NotNull Annotation[] annotations) {
        this.pos = pos;
        this.type = type;
        this.actualTypes = actualTypes;
        this.annotations = annotations;
    }

    public int getPos() {
        return pos;
    }

    @NotNull
    public Class<?> getType() {
        return type;
    }

    @NotNull
    public Type[] getActualTypes() {
        return actualTypes;
    }

    @NotNull
    public Annotation[] getAnnotations() {
        return annotations;
    }
}
