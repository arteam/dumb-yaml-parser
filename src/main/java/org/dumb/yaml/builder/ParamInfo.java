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
    int pos;

    /**
     * Type of argument
     */
    @NotNull
    Class<?> type;

    /**
     * Actual types (if type is collection: List, Map)
     */
    @NotNull
    Type[] actualTypes;

    @NotNull
    Annotation[] annotations;

    ParamInfo(int pos, @NotNull Class<?> type, @NotNull Type[] actualTypes, @NotNull Annotation[] annotations) {
        this.pos = pos;
        this.type = type;
        this.actualTypes = actualTypes;
        this.annotations = annotations;
    }
}
