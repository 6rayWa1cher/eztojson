package com.a6raywa1cher.eztojson;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Set;

/**
 * @author 6rayWa1cher
 * @version 1.0
 * @since 1.0.0
 */
class ClassContainer {
    FieldContainer shortInfo;
    Set<FieldContainer> persistenceFields;
    Set<FieldContainer> otherFields;
    Class aClass;
    Method customSerializeMethod;

    ClassContainer(Class aClass) {
        this.shortInfo = null;
        this.persistenceFields = null;
        this.otherFields = null;
        this.aClass = aClass;
        this.customSerializeMethod = null;
    }

    ClassContainer(FieldContainer shortInfo, Set<FieldContainer> persistenceFields,
                   Set<FieldContainer> otherFields, Class aClass, Method customSerializeMethod) {
        this.shortInfo = shortInfo;
        this.persistenceFields = persistenceFields;
        this.otherFields = otherFields;
        this.aClass = aClass;
        this.customSerializeMethod = customSerializeMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClassContainer)) return false;
        ClassContainer that = (ClassContainer) o;
        return Objects.equals(shortInfo, that.shortInfo) &&
                Objects.equals(persistenceFields, that.persistenceFields) &&
                Objects.equals(otherFields, that.otherFields) &&
                Objects.equals(aClass, that.aClass) &&
                Objects.equals(customSerializeMethod, that.customSerializeMethod);
    }

    @Override
    public int hashCode() {

        return Objects.hash(shortInfo, persistenceFields, otherFields, aClass, customSerializeMethod);
    }
}
