package org.dumb.yaml.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Date: 11/23/13
 * Time: 4:38 PM
 * Enum converter for property values
 *
 * @author Artem Prigoda
 */
@Retention(RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface EnumConverter {

    String value() default "valueOf";
}
