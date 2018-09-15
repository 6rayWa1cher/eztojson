package com.a6raywa1cher.eztojson;

import java.util.Objects;
import java.util.Set;

class ClassContainer {
	FieldContainer shortInfo;
	Set<FieldContainer> persistenceFields;
	Set<FieldContainer> otherFields;
	Class aClass;

	ClassContainer(Class aClass) {
		this.shortInfo = null;
		this.persistenceFields = null;
		this.otherFields = null;
		this.aClass = aClass;
	}

	ClassContainer(FieldContainer shortInfo, Set<FieldContainer> persistenceFields,
	               Set<FieldContainer> otherFields, Class aClass) {
		this.shortInfo = shortInfo;
		this.persistenceFields = persistenceFields;
		this.otherFields = otherFields;
		this.aClass = aClass;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ClassContainer)) return false;
		ClassContainer that = (ClassContainer) o;
		return Objects.equals(shortInfo, that.shortInfo) &&
				Objects.equals(persistenceFields, that.persistenceFields) &&
				Objects.equals(otherFields, that.otherFields) &&
				Objects.equals(aClass, that.aClass);
	}

	@Override
	public int hashCode() {

		return Objects.hash(shortInfo, persistenceFields, otherFields, aClass);
	}
}
