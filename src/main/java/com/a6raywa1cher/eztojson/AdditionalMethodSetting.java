package com.a6raywa1cher.eztojson;

import java.lang.reflect.Method;

import static com.a6raywa1cher.eztojson.ETJUtility.unGet;

/**
 * This class using as info to set up ADDITIONAL_METHODS.
 *
 * @author 6raywa1cher
 * @version 1.0
 * @see com.a6raywa1cher.eztojson.ETJReference.Properties
 * @since 1.0.0
 */
public class AdditionalMethodSetting {
	private Method method;
	private String name;

	/**
	 * Constructor specifies method to append with name of method without
	 * 'get' as key for all values from the method.
	 *
	 * @param method Method to be appended to final JSON
	 */
	public AdditionalMethodSetting(Method method) {
		this.method = method;
		this.name = unGet(method.getName());
	}

	/**
	 * Constructor specifies the method to append with provided name
	 * as key for all values from the method.
	 *
	 * @param method Method to be appended to final JSON
	 * @param name   Name of the key
	 */
	public AdditionalMethodSetting(Method method, String name) {
		this.method = method;
		this.name = name;
	}

	/**
	 * Constructor specifies method to append with name of method without
	 * 'get' as key for all values from the method.
	 *
	 * @param method Method name. Example: {@code "models.auth.User.getPassword"}
	 */
	public AdditionalMethodSetting(String method) {
		this.method = ETJReference.getMethod(method);
		this.name = unGet(this.method.getName());
	}

	/**
	 * Constructor specifies the method to append with provided name
	 * as key for all values from the method.
	 *
	 * @param method Method name. Example: {@code "models.auth.User.getPassword"}
	 * @param name   Name of the key
	 */
	public AdditionalMethodSetting(String method, String name) {
		this.method = ETJReference.getMethod(method);
		this.name = name;
	}

	public Method getMethod() {
		return method;
	}

	public String getName() {
		return name;
	}
}
