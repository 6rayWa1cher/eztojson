package com.a6raywa1cher.eztojson.adapter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Adapters provide additional information about fields in java-classes and
 * getter, necessary for ETJUtility during scanning and converting. You don't
 * have to check ETJField and ShortInfo annotations.
 *
 * @author 6rayWa1cher
 * @version 1.1
 * @since 1.0.0
 */
public interface Adapter {
	/**
	 * Transient field means that this field doesn't provide necessary
	 * information and it can be skipped in final JSON.
	 * It can be a local variable, using for in-session calculations,
	 * buffer, input-output stream, etc.
	 *
	 * @param f      field in scanning object
	 * @param getter getter for field f
	 * @return is this field serializing
	 */
	boolean isTransient(Field f, Method getter);

	/**
	 * This method provides the same information, as a ShortInfo annotation.
	 * Warning: if this method returns 'true' about two or more fields in
	 * this scanning class, only first will be considered. But, if ShortInfo
	 * annotation was set up, this method will not be called.
	 *
	 * @param f      field in scanning object
	 * @param getter getter for field f
	 * @return is this field using for unambiguous identification of this
	 * object
	 * @see com.a6raywa1cher.eztojson.annotation.ShortInfo
	 */
	boolean isIdentification(Field f, Method getter);

	/**
	 * Extraneous field doesn't hold information about their purpose. It can
	 * be service information
	 *
	 * @param f      field in scanning
	 * @param getter getter for field f
	 * @return is this field extraneous (true - extraneous)
	 */
	boolean isExtraneous(Field f, Method getter);

	/**
	 * This method tells if this object can be parsing only shortly. It can be
	 * Java classes, for example, LocalDate.
	 *
	 * @param clazz Class to be analyzed
	 * @return is this class can be parsing only shortly?
	 */
	boolean isShortOnly(Class clazz);
}
