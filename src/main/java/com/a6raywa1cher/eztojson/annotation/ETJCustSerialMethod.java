package com.a6raywa1cher.eztojson.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation marks method that returns JSONObject for this object.
 *
 * @author santus20111
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ETJCustSerialMethod {
    /**
     * This field contains a name of the getter, used for getting JSONObject
     * about scanning object.
     * Examples: "toJSONObject"
     *
     * @return name of getter for getting JSONObject
     */
    String getter() default "";
}
