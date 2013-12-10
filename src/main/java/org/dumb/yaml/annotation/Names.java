package org.dumb.yaml.annotation;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Date: 11/21/13
 * Time: 11:04 PM
 * Property names in YAML file
 *
 * @author Artem Prigoda
 */
@Retention(RUNTIME)
public @interface Names {

    String[] value() default {};
}
