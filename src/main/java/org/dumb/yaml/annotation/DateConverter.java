package org.dumb.yaml.annotation;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Date: 11/23/13
 * Time: 4:38 PM
 * Date converter for property values
 *
 * @author Artem Prigoda
 */
@Retention(RUNTIME)
public @interface DateConverter {

    String value() default "yyyy-MM-dd HH:mm:ss";
}
