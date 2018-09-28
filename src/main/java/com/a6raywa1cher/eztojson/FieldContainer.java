package com.a6raywa1cher.eztojson;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Set;

/**
 * @author 6rayWa1cher
 * @version 1.0
 * @since 1.0.0
 */
class FieldContainer {
	enum MethodType {
		DATA, CLASS, COLLECTION
	}

	String fieldName;
	Method getter;
	Class returnClass;
	MethodType methodType;
	Set<Annotation> annotations;
	boolean transientPersistence;
	boolean suitable;
	boolean jsonGenColumnAnnotated;

	FieldContainer(String fieldName, Method getter, Class returnClass, MethodType methodType, Set<Annotation> annotations, boolean transientPersistence, boolean suitable, boolean jsonGenColumnAnnotated) {
		this.fieldName = fieldName;
		this.getter = getter;
		this.returnClass = returnClass;
		this.methodType = methodType;
		this.annotations = annotations;
		this.transientPersistence = transientPersistence;
		this.suitable = suitable;
		this.jsonGenColumnAnnotated = jsonGenColumnAnnotated;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof FieldContainer)) return false;
		FieldContainer that = (FieldContainer) o;
		return transientPersistence == that.transientPersistence &&
				suitable == that.suitable &&
				jsonGenColumnAnnotated == that.jsonGenColumnAnnotated &&
				Objects.equals(fieldName, that.fieldName) &&
				Objects.equals(getter, that.getter) &&
				Objects.equals(returnClass, that.returnClass) &&
				methodType == that.methodType &&
				Objects.equals(annotations, that.annotations);
	}

	@Override
	public int hashCode() {

		return Objects.hash(fieldName, getter, returnClass, methodType, annotations, transientPersistence, suitable, jsonGenColumnAnnotated);
	}
}
