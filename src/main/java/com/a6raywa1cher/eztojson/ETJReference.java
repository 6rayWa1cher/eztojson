package com.a6raywa1cher.eztojson;

import com.a6raywa1cher.eztojson.adapter.Adapter;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * ETJReference provides interface to control and invoke JSON generator at
 * ETJUtility.
 * <p>
 * Quick howto:
 * 1) Receive object of ETJReference: ETJUtility.create()
 * 2) Set up necessary parameters: etjReference.configure(Properties, Object),
 * where Object - new value of parameter
 * 3) Using etjReference, parse Java class to JSON:
 * etjReference.process(Object, int),
 * where int is scanning depth. Zero means that only this class will be showed
 * fully in JSONObject, and increasing this value by one you'll get one more
 * layer of scanning.
 *
 * @author 6rayWa1cher
 * @version 1.0
 * @see com.a6raywa1cher.eztojson.ETJUtility
 * @see JSONObject
 */
public class ETJReference {
	Map<Class, ClassContainer> classDatabase;
	Adapter adapter;

	public enum Properties {
		INCLUDE_TRANSIENT_FIELDS, INCLUDE_TRANSIENT_FIELDS_IN, INCLUDE_NON_JPA_FIELDS, INCLUDE_NON_JPA_FIELDS_IN,
		PARSE_EMPTY_VALUES, PARSE_EMPTY_VALUES_IN, ALLOW_DUPLICATES, ALLOW_DUPLICATES_IN, ALLOW_DUPLICATES_OF,
		SCANNING_DEPTH, WHITE_SET_OF_CLASSES, SHORT_ONLY_SET, BLACK_SET_OF_CLASSES, WHITE_SET_OF_METHODS,
		BLACK_SET_OF_METHODS, ADDITIONAL_METHODS
	}

	boolean includeTransientFields = false;
	Set<Class> includeTransientFieldsIn = null;

	boolean includeNonJPAFields = false;
	Set<Class> includeNonJPAFieldsIn = null;

	boolean parseEmptyValues = false;
	Set<Class> parseEmptyValuesIn = null;

	boolean allowDuplicates = false;
	Set<Class> allowDuplicatesIn = null;
	Set<Class> allowDuplicatesOf = null;

	private int scanningDepth = 0;

	Set<Class> whiteSetOfClasses = null;
	Set<Class> shortOnlySet = null;
	Set<Class> blackSetOfClasses = null;

	Set<Method> whiteSetOfMethods = null;
	Set<Class> whiteSetOfMethodsIn = null;
	Set<Method> blackSetOfMethods = null;
	Set<Class> blackSetOfMethodsIn = null;

	Map<Class, Set<AdditionalMethodSetting>> additionalMethods = null;

	public JSONObject process(Object o) {
		return ETJUtility.process(this, o, scanningDepth);
	}

	public JSONObject process(Object o, int scanningDepth) {
		return ETJUtility.process(this, o, scanningDepth);
	}

	<T> boolean nullSafeContains(Set<T> set, T obj) {
		if (set == null) return false;
		return set.contains(obj);
	}

	<T> boolean nullSafeNotContains(Set<T> set, T obj) {
		if (set == null) return false;
		return !set.contains(obj);
	}

	public ETJReference configure(Object o, Properties property) {
		switch (property) {
			case INCLUDE_TRANSIENT_FIELDS:
				includeTransientFields = (boolean) o;
				break;
			case INCLUDE_TRANSIENT_FIELDS_IN:
				if (includeTransientFieldsIn == null) {
					includeTransientFieldsIn = new HashSet<>();
				}
				includeTransientFieldsIn.add((Class) o);
				break;
			case INCLUDE_NON_JPA_FIELDS:
				includeNonJPAFields = (boolean) o;
				break;
			case INCLUDE_NON_JPA_FIELDS_IN:
				if (includeNonJPAFieldsIn == null) {
					includeNonJPAFieldsIn = new HashSet<>();
				}
				includeNonJPAFieldsIn.add((Class) o);
				break;
			case PARSE_EMPTY_VALUES:
				parseEmptyValues = (boolean) o;
				break;
			case PARSE_EMPTY_VALUES_IN:
				if (parseEmptyValuesIn == null) {
					parseEmptyValuesIn = new HashSet<>();
				}
				parseEmptyValuesIn.add((Class) o);
				break;
			case ALLOW_DUPLICATES:
				allowDuplicates = (boolean) o;
				break;
			case ALLOW_DUPLICATES_IN:
				if (allowDuplicatesIn == null) {
					allowDuplicatesIn = new HashSet<>();
				}
				allowDuplicatesIn.add((Class) o);
				break;
			case ALLOW_DUPLICATES_OF:
				if (allowDuplicatesOf == null) {
					allowDuplicatesOf = new HashSet<>();
				}
				allowDuplicatesOf.add((Class) o);
				break;
			case SCANNING_DEPTH:
				if (((int) o) < 0) throw new RuntimeException("property SCANNING_DEPTH always greater or equals 0");
				scanningDepth = (int) o;
				break;
			case WHITE_SET_OF_CLASSES:
				if (whiteSetOfClasses == null) {
					whiteSetOfClasses = new HashSet<>();
				}
				whiteSetOfClasses.add((Class) o);
				break;
			case SHORT_ONLY_SET:
				if (shortOnlySet == null) {
					shortOnlySet = new HashSet<>();
				}
				shortOnlySet.add((Class) o);
				break;
			case BLACK_SET_OF_CLASSES:
				if (blackSetOfClasses == null) {
					blackSetOfClasses = new HashSet<>();
				}
				blackSetOfClasses.add((Class) o);
				break;
			case WHITE_SET_OF_METHODS:
				if (whiteSetOfMethods == null) whiteSetOfMethods = new HashSet<>();
				if (whiteSetOfMethodsIn == null) whiteSetOfMethodsIn = new HashSet<>();
				Method method = getMethod((String) o);
				if (blackSetOfMethodsIn != null && blackSetOfMethodsIn.contains(method.getDeclaringClass())) {
					throw new RuntimeException("class" + method.getDeclaringClass() + "already figured in black list, cannot pull to white");
				}
				whiteSetOfMethods.add(method);
				whiteSetOfMethodsIn.add(method.getDeclaringClass());
				break;
			case BLACK_SET_OF_METHODS:
				if (blackSetOfMethods == null) blackSetOfMethods = new HashSet<>();
				if (blackSetOfMethodsIn == null) blackSetOfMethodsIn = new HashSet<>();
				Method method1 = getMethod((String) o);
				if (whiteSetOfMethodsIn != null && whiteSetOfMethodsIn.contains(method1.getDeclaringClass())) {
					throw new RuntimeException("class" + method1.getDeclaringClass() + "already figured in black list, cannot pull to white");
				}
				blackSetOfMethods.add(method1);
				blackSetOfMethodsIn.add(method1.getDeclaringClass());
				break;
			case ADDITIONAL_METHODS:
				if (additionalMethods == null) additionalMethods = new HashMap<>();
				AdditionalMethodSetting ams = (AdditionalMethodSetting) o;
				if (!additionalMethods.containsKey(ams.getMethod().getDeclaringClass())) {
					additionalMethods.put(ams.getMethod().getDeclaringClass(), new HashSet<>());
				}
				additionalMethods.get(ams.getMethod().getDeclaringClass()).add(ams);
				break;
		}
		return this;
	}

	@SuppressWarnings("unchecked")
	static Method getMethod(String str) { //example: models.auth.User.getPassword
		String className = null;
		String methodName = null;
		try {
			className = str.substring(0, str.lastIndexOf('.'));
			methodName = str.substring(str.lastIndexOf('.') + 1);
			Class clazz = Class.forName(className);
			return clazz.getMethod(methodName);
		} catch (ClassNotFoundException cnfe) {
			throw new RuntimeException("white rule incorrect: not found " + className);
		} catch (NoSuchMethodException nsme) {
			throw new RuntimeException("white rule incorrect: not found " + methodName);
		} catch (IndexOutOfBoundsException ioobe) {
			throw new RuntimeException("error with translating strings: " + str);
		}
	}

	ETJReference() {
	}
}
