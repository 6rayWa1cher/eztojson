package com.a6raywa1cher.eztojson.adapter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Implementation of Adapter for POJO.
 * <p>
 * POJOs have not any special markers for transient, id or extraneous fields.
 * Short info can be provided with {@code ShortInfo} annotation or from
 * {@code toString()}.
 * (Un)necessary fields can be set up with
 * {@link com.a6raywa1cher.eztojson.ETJReference.Properties#WHITELIST_OF_METHODS}
 * or
 * {@link com.a6raywa1cher.eztojson.ETJReference.Properties#BLACKLIST_OF_METHODS}.
 *
 * @author 6rayWa1cher
 * @version 1.0
 * @see com.a6raywa1cher.eztojson.adapter.Adapter
 * @see com.a6raywa1cher.eztojson.ETJReference
 * @since 1.0.0
 */
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
		return false;
	}
}
