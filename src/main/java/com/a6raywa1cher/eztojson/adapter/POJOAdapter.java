package com.a6raywa1cher.eztojson.adapter;

import com.a6raywa1cher.eztojson.annotation.ShortInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class POJOAdapter implements Adapter {
	@Override
	public boolean isTransient(Field f, Method getter) {
		return false;
	}

	@Override
	public boolean isIdentification(Field f, Method getter) {
		return false;
	}

	@Override
	public boolean isExtraneous(Field f, Method getter) {
//		return !f.getDeclaringClass().isAnnotationPresent(ShortInfo.class);
		return false;
	}
}
