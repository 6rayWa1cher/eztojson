package com.a6raywa1cher.eztojson.adapter;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Implementation of Adapter for objects with JPA annotations.
 * <p>
 * As transient marker, this class uses {@code Transient} annotation.
 * As identification marker, this class uses {@code Id} annotation.
 * Extraneous object haven't any javax.persistence annotation.
 *
 * @author 6rayWa1cher
 * @version 1.0
 * @see com.a6raywa1cher.eztojson.adapter.Adapter
 * @see javax.persistence.Transient
 * @see javax.persistence.Id
 * @since 1.0.0
 */
public class JPAAdapter implements Adapter {
	@Override
	public boolean isTransient(Field f, Method getter) {
		return f.isAnnotationPresent(Transient.class) || getter.isAnnotationPresent(Transient.class);
	}

	@Override
	public boolean isIdentification(Field f, Method getter) {
		return f.isAnnotationPresent(Id.class) || getter.isAnnotationPresent(Id.class);
	}

	@Override
	public boolean isExtraneous(Field f, Method getter) {
		for (Annotation annotation : f.getAnnotations()) {
			if (annotation.toString().startsWith("@javax.persistence")) return false;
		}
		for (Annotation annotation : getter.getAnnotations()) {
			if (annotation.toString().startsWith("@javax.persistence")) return false;
		}
		return true;
	}
}
