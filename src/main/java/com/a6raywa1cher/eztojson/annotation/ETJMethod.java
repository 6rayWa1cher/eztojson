package com.a6raywa1cher.eztojson.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation marks method as compulsory for parsing into JSON.
 *
 * @author 6rayWa1cher
 * @version 1.0
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ETJMethod {
	/**
	 * This field provides the name of a key, which will have all values from
	 * the method, in JSON. By default, it's the name of the method, when this
	 * variable equals to an empty string.
	 *
	 * @return name of the key
	 */
	String name() default "";

}
