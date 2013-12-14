package org.dumb.yaml.builder;

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
    private Class<?> type;

    /**
     * Actual types (if type is collection: List, Map)
     */
    private Type[] actualTypes;

    private Annotation[] annotations;

    public ParamInfo(int pos, Class<?> type, Type[] actualTypes, Annotation[] annotations) {
        this.pos = pos;
        this.type = type;
        this.actualTypes = actualTypes;
        this.annotations = annotations;
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

    public Annotation[] getAnnotations() {
        return annotations;
    }
}
