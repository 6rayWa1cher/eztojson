package com.a6raywa1cher.eztojson.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation marks the field as compulsory for parsing,
 * if this field is extraneous or has another blocking modification
 *
 * @author 6rayWa1cher
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ETJField {
}
