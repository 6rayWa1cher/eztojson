package com.a6raywa1cher.eztojson.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation ShortInfo used as a pointer to getter, which can provide short
 * info about this object. It's using when current scanning depth equals 0, but
 * we have to provide some information about non-primitive object, stored in
 * the variable of the current scanning object
 *
 * @author 6rayWa1cher
 * @version 1.0
 * @see java.lang.annotation.Annotation
 * @since 1.0.0
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ShortInfo {
	/**
	 * This variable set up a name of a key in JSON object with an object from
	 * the getter. By default, it's the name of the field, when this variable
	 * equals to an empty string.
	 *
	 * @return name of key with object in JSON
	 */
	String name() default "";

	/**
	 * This field contains a name of the getter, used for getting short info
	 * about scanning object.
	 * Examples: "getId", "getName"
	 *
	 * @return name of getter for providing short info
	 */
	String getter();
}
