package org.dumb.yaml.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Date: 11/21/13
 * Time: 11:04 PM
 * Property names in YAML file
 *
 * @author Artem Prigoda
 */
@Retention(RUNTIME)
@Target({ElementType.CONSTRUCTOR, ElementType.TYPE})
public @interface Names {

    String[] value() default {};
}
