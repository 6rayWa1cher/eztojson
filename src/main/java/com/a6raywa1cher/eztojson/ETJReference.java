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
 * <ol>
 * <li>
 * Receive object of ETJReference: {@link ETJUtility#create(Adapter)}
 * </li>
 * <li>
 * Set up necessary parameters:
 * {@link ETJReference#configure(Object, Properties)}, where Object - new value
 * of parameter
 * </li>
 * <li>
 * Using etjReference, parse Java class to JSON:
 * {@link ETJReference#process(Object, int)},
 * where int is scanning depth. Zero means that only this class will be showed
 * fully in JSONObject, and increasing this value by one you'll get one more
 * layer of scanning.
 * </li>
 * </ol>
 *
 * @author 6rayWa1cher
 * @version 1.0
 * @see ETJUtility
 * @see JSONObject
 * @since 1.0.0
 */
public class ETJReference {
	Map<Class, ClassContainer> classDatabase;
	Adapter adapter;

	public enum Properties {
		/**
		 * Activates the parsing of transient fields in all classes.
		 * <p>
		 * Object - boolean.
		 */
		INCLUDE_TRANSIENT_FIELDS,
		/**
		 * Activates the parsing of transient fields in the specific class.
		 * <p>
		 * Object - Class.
		 */
		INCLUDE_TRANSIENT_FIELDS_IN,
		/**
		 * Activates the parsing of extraneous fields in all classes.
		 * <p>
		 * Object - boolean.
		 */
		INCLUDE_EXTRANEOUS_FIELDS,
		/**
		 * Activates the parsing of extraneous fields in the specific class.
		 * <p>
		 * Object - Class.
		 */
		INCLUDE_EXTRANEOUS_FIELDS_IN,
		/**
		 * Activates the parsing of fields with empty values in all classes.
		 * Empty values will have {@code JSONObject.NULL} as value in JSON.
		 * <p>
		 * Object - boolean
		 */
		PARSE_EMPTY_VALUES,
		/**
		 * Activates the parsing of fields with empty values in the specific
		 * class.
		 * The empty values will have {@code JSONObject.NULL} as value in JSON.
		 * <p>
		 * Object - Class.
		 */
		PARSE_EMPTY_VALUES_IN,
		/**
		 * Disables the detection of object duplicates in all classes.
		 * <p>
		 * Object - boolean.
		 */
		ALLOW_DUPLICATES,
		/**
		 * Disables the detection of object duplicates in the specific class.
		 * <p>
		 * Object - Class.
		 */
		ALLOW_DUPLICATES_IN,
		/**
		 * Disables the detection of object duplicates for all object of the
		 * specific class.
		 * <p>
		 * Object - Class.
		 */
		ALLOW_DUPLICATES_OF,
		/**
		 * Setting default scanning depth for {@link ETJReference#process(Object)}
		 * <p>
		 * Object - int, not less than zero.
		 */
		SCANNING_DEPTH,
		/**
		 * Enables the whitelist rule and appends the class to the list.
		 * <p>
		 * The whitelist of classes allows parsing only specific classes.
		 * All class not included in this list will not parse, also
		 * short info will not show.
		 * Not compatible with {@code BLACKLIST_OF_CLASSES}.
		 * <p>
		 * Object - Class.
		 */
		WHITELIST_OF_CLASSES,
		/**
		 * Appending class to the list of classes, which have only short info
		 * in JSON.
		 * Other fields of the class will not include.
		 * <p>
		 * Object - Class.
		 */
		SHORT_ONLY_SET,
		/**
		 * Enables the blacklist rule and appends the class to the list.
		 * <p>
		 * The blacklist of classes disallows parsing classes in the list.
		 * No fields of the classes will parse, also short info will not show.
		 * Not compatible with {@code WHITELIST_OF_CLASSES}.
		 * <p>
		 * Object - Class.
		 */
		BLACKLIST_OF_CLASSES,
		/**
		 * Enables the whitelist of methods and appends the method to the list.
		 * <p>
		 * The whitelist of methods allows parsing only specific methods, but
		 * only if the list specified for the parsing class.
		 * All methods not included in this list, if the list exists for the class,
		 * will not parse.
		 * Not compatible with {@code BLACKLIST_OF_METHODS}, if already specified
		 * for the class.
		 * <p>
		 * Object - String. Example: {@code "models.auth.User.getPassword"}
		 */
		WHITELIST_OF_METHODS,
		/**
		 * Enables the blacklist of methods and appends the method to the list.
		 * <p>
		 * The blacklist of methods disallows parsing of specific methods, but
		 * only if the list specified for the parsing class.
		 * All methods included in this list if the list exists for the class,
		 * will not parse.
		 * Not compatible with {@code WHITELIST_OF_METHODS}, if already specified
		 * for the class.
		 * <p>
		 * Object - String. Example: {@code "models.auth.User.getPassword"}
		 */
		BLACKLIST_OF_METHODS,
		/**
		 * Forcibly appends method to be parsed.
		 * <p>
		 * Object - {@link AdditionalMethodSetting}.
		 */
		ADDITIONAL_METHODS
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

	/**
	 * Asks ETJUtility to process object with defined in this class settings.
	 *
	 * @param o Object to convert to JSONObject
	 * @return JSONObject of object
	 */
	public JSONObject process(Object o) {
		return ETJUtility.process(this, o, scanningDepth);
	}

	/**
	 * Asks ETJUtility to process object with defined in this class settings.
	 *
	 * @param o             Object to convert to JSONObject
	 * @param scanningDepth Scanning depth
	 * @return JSONObject of object
	 */
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

	/**
	 * Configures property: replaces saved value or appends value to list.
	 *
	 * @param o        Replacement or value to append
	 * @param property Property to configure
	 * @return self
	 * @see ETJReference.Properties
	 */
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
			case INCLUDE_EXTRANEOUS_FIELDS:
				includeNonJPAFields = (boolean) o;
				break;
			case INCLUDE_EXTRANEOUS_FIELDS_IN:
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
			case WHITELIST_OF_CLASSES:
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
			case BLACKLIST_OF_CLASSES:
				if (blackSetOfClasses == null) {
					blackSetOfClasses = new HashSet<>();
				}
				blackSetOfClasses.add((Class) o);
				break;
			case WHITELIST_OF_METHODS:
				if (whiteSetOfMethods == null) whiteSetOfMethods = new HashSet<>();
				if (whiteSetOfMethodsIn == null) whiteSetOfMethodsIn = new HashSet<>();
				Method method = getMethod((String) o);
				if (blackSetOfMethodsIn != null && blackSetOfMethodsIn.contains(method.getDeclaringClass())) {
					throw new RuntimeException("class" + method.getDeclaringClass() + "already figured in black list, cannot pull to white");
				}
				whiteSetOfMethods.add(method);
				whiteSetOfMethodsIn.add(method.getDeclaringClass());
				break;
			case BLACKLIST_OF_METHODS:
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
