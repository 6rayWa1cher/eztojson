package com.a6raywa1cher.eztojson;

import java.lang.reflect.Method;

import static com.a6raywa1cher.eztojson.ETJUtility.unGet;

/**
 * @author 6raywa1cher
 * @version 1.0
 * @since 1.0.0
 */
public class AdditionalMethodSetting {
	private Method method;
	private String name;

	public AdditionalMethodSetting(Method method) {
		this.method = method;
		this.name = unGet(method.getName());
	}

	public AdditionalMethodSetting(Method method, String name) {
		this.method = method;
		this.name = name;
	}

	public AdditionalMethodSetting(String method) {
		this.method = ETJReference.getMethod(method);
		this.name = unGet(this.method.getName());
	}

	public AdditionalMethodSetting(String method, String name) {
		this.method = ETJReference.getMethod(method);
		this.name = name;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
