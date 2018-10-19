package com.a6raywa1cher.eztojson;

import com.a6raywa1cher.eztojson.adapter.Adapter;
import com.a6raywa1cher.eztojson.annotation.ETJCustSerialMethod;
import com.a6raywa1cher.eztojson.annotation.ETJField;
import com.a6raywa1cher.eztojson.annotation.ETJMethod;
import com.a6raywa1cher.eztojson.annotation.ShortInfo;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Java-classes to JSON converter class. How-to in {@link ETJReference}.
 * <p>
 * JSON includes two parts: short info and main info.
 * Short info - also identification info - briefly characterizes the object.
 * Example: primary keys in a database. Main info - all other fields in the
 * class. To get info about short/main/extraneous/transient type, ETJUtility
 * uses {@link Adapter}. Classification made to work primarily with databases
 * objects.
 *
 * @author 6rayWa1cher
 * @version 1.1
 * @see ETJReference
 * @since 1.0.0
 */
public class ETJUtility {

	private ETJUtility() {
	}

	/**
	 * Generates object of ETJReference. This is starting point.
	 *
	 * @param adapter Adapter, suitable for classes which this class will
	 *                convert after setting up the ETJReference object.
	 * @return ETJReference object
	 * @see ETJReference
	 * @see Adapter
	 */
	public static ETJReference create(Adapter adapter) {
		ETJReference j = new ETJReference();
		j.adapter = adapter;
		ClassDatabaseEntry classDatabaseEntry = ClassDatabase.getInstance(j.adapter.getClass());
		j.classDatabase = classDatabaseEntry.getDatabaseCopy();
		return j;
	}

	static JSONObject process(ETJReference j, Object o, int scanningDepth) {
		Queue<GeneratorRequest> processRequest = new ArrayDeque<>();
		Set<Object> callSet = j.allowDuplicates ? null : new HashSet<>();
		boolean first = true;
		JSONObject json = new JSONObject();
		classToStack(processRequest, j, o, json, scanningDepth);
		while (!processRequest.isEmpty()) {
			try {
				GeneratorRequest request = processRequest.remove();
				if (request.object == null) {
					parseNull(j, request);
					continue;
				}
				if (callSet != null) {
					if (callSet.contains(request.object) &&
							!j.nullSafeContains(j.allowDuplicatesOf, request.object.getClass()) &&
							!j.nullSafeContains(j.allowDuplicatesIn, request.caller.getClass())) {
						parseShortly(j, request, request.object);
						continue;
					}
					if (first) {
						callSet.add(o);
						first = false;
					}
					callSet.add(request.object);
				}
				if (j.nullSafeContains(j.shortOnlySet, request.object.getClass())) {
					parseShortly(j, request, request.object);
					continue;
				}

				FieldContainer.MethodType methodType = request.objectDescription != null ?
						request.objectDescription.methodType : getType(request.object.getClass());

				if (checkClass(j, request.object.getClass()) == null) {
					methodType = FieldContainer.MethodType.DATA; //toString, if we don't know this class
				}

				ClassContainer classContainer = j.classDatabase.get(request.object.getClass());
				if (classContainer == null) {
					classContainer = checkClass(j, request.object.getClass());
				}

				switch (methodType) {


					case DATA:
						pullTo(request.objectDescription != null ? request.objectDescription.fieldName : "", request.object, request);
						break;
					case CLASS:
						if (classContainer != null && classContainer.customSerializeMethod != null) {
							parseUsingCustomMethod(j, request, request.object);
						} else if (request.remainingScanningDepth > 0) {
							JSONObject jsonObject = new JSONObject();
							long entriesO = classToStack(processRequest, j, request.object, jsonObject, request.remainingScanningDepth - 1);
							if (entriesO != 0) {
								pullTo(request.objectDescription != null ? request.objectDescription.fieldName : "", jsonObject, request);
							}
						} else {
							parseShortly(j, request, request.object);
						}
						break;
					case COLLECTION:
						if (classContainer != null && classContainer.customSerializeMethod != null) {
							parseUsingCustomMethod(j, request, request.object);
						} else if (request.remainingScanningDepth > 0) {
							JSONArray jsonArray = new JSONArray();
							long entriesA = classToStack(processRequest, j, request.object, jsonArray, request.remainingScanningDepth);
							if (entriesA != 0) {
								pullTo(request.objectDescription != null ? request.objectDescription.fieldName : "", jsonArray, request);
							}
						} else {
							parseShortly(j, request, request.object);
						}
						break;
				}
			} catch (Exception ignored) {
//				ignored.printStackTrace();
			}
		}
		return json;
	}

	private static void parseShortly(ETJReference j, GeneratorRequest request, Object o) throws IllegalAccessException, InvocationTargetException {
		FieldContainer shortInfo;
		ClassContainer classContainer = j.classDatabase.get(o.getClass());
		if (classContainer == null) {
			classContainer = checkClass(j, o.getClass());
		}
		if (classContainer != null && classContainer.persistenceFields != null && classContainer.shortInfo != null) {
			shortInfo = classContainer.shortInfo;
			String simpleName = request.objectDescription != null ? unGet(request.objectDescription.getter.getName()) : o.getClass().getSimpleName();
			pullTo(
					shortInfo.fieldName.equals("id") ? Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1) + "Id" : shortInfo.fieldName,
					shortInfo.getter.invoke(o),
					request
			);
		} else {
			pullTo(
					request.objectDescription != null ? request.objectDescription.fieldName : "",
					request.object,
					request
			);
		}
	}

	private static void parseUsingCustomMethod(ETJReference j, GeneratorRequest request, Object o) throws IllegalAccessException, InvocationTargetException {
		Method customMethod;
		ClassContainer classContainer = j.classDatabase.get(o.getClass());
		if (classContainer == null) {
			classContainer = checkClass(j, o.getClass());
		}

		if (classContainer != null && classContainer.persistenceFields != null && classContainer.customSerializeMethod != null) {
			customMethod = classContainer.customSerializeMethod;
			String simpleName = request.objectDescription != null ? unGet(request.objectDescription.getter.getName()) : o.getClass().getSimpleName();
			pullTo(
					Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1),
					customMethod.invoke(o),
					request
			);
		} else {
			pullTo(
					request.objectDescription != null ? request.objectDescription.fieldName : "",
					request.object,
					request
			);
		}
	}

	private static void parseNull(ETJReference j, GeneratorRequest request) {
		if (j.parseEmptyValues || j.nullSafeContains(j.parseEmptyValuesIn, request.caller.getClass())) {
			pullTo(request.objectDescription.fieldName, JSONObject.NULL, request);
		}
	}

	private static void pullTo(String fieldName, Object o, GeneratorRequest request) {
		if (request.associatedJO != null) {
			request.associatedJO.put(fieldName, o);
		} else {
			request.associatedJA.put(o);
		}
	}

	@SuppressWarnings("unchecked")
	private static ClassContainer checkClass(ETJReference j, Class c) {
		ClassContainer classContainer;
		if ((classContainer = j.classDatabase.getOrDefault(c, null)) != null) {
			if (classContainer.persistenceFields == null) return null;
			else return classContainer;
		}
		if (j.adapter.isShortOnly(c)) {
			classContainer = new ClassContainer(c);
			ClassDatabase.getInstance(j.adapter.getClass()).updateDatabase(classContainer);
			j.classDatabase.put(c, classContainer);
			return null;
		}
		Field[] fields;
		do {
			fields = c.getDeclaredFields();
		} while (fields.length == 0);
		FieldContainer shortInfo = null;
		Method customSerializeMethod = null;

		try {
			ShortInfo anno = (ShortInfo) c.getAnnotation(ShortInfo.class);
			ETJCustSerialMethod etjCustSerialMethod = (ETJCustSerialMethod) c.getAnnotation(ETJCustSerialMethod.class);

			if (etjCustSerialMethod != null) {
				customSerializeMethod = c.getMethod(etjCustSerialMethod.getter());
			}

			Method shortInfoGetter = c.getMethod(anno.getter());
			Class returnClass = shortInfoGetter.getReturnType();
			shortInfo = new FieldContainer(anno.name().equals("") ? unGet(shortInfoGetter.getName()) : anno.name(),
					shortInfoGetter, returnClass, getType(returnClass), null, false, true, true);
		} catch (NullPointerException | NoSuchMethodException ignored) {

		}
		Set<FieldContainer> persistenceFields = new HashSet<>(fields.length);
		Set<FieldContainer> otherFields = new HashSet<>(fields.length);
		for (Field f : fields) {
			try {
				Method getter = c.getMethod(toGet(f.getName()));
				Set<Annotation> annotations = new HashSet<>();
				Collections.addAll(annotations, f.getAnnotations());
				Collections.addAll(annotations, getter.getAnnotations());
				boolean isSuitable = !j.adapter.isExtraneous(f, getter);
				boolean transientPersistence = isSuitable && j.adapter.isTransient(f, getter);
				boolean jsonGenColumnAnnotated = f.isAnnotationPresent(ETJField.class) || getter.isAnnotationPresent(ETJField.class);
				Class returnType = getter.getReturnType();
				FieldContainer.MethodType type = getType(returnType);
				if (shortInfo != null && shortInfo.getter.equals(getter)) {
					continue;
				}
				if (shortInfo == null && j.adapter.isIdentification(f, getter)) {
					shortInfo = new FieldContainer(f.getName(), getter, returnType, type, annotations,
							false, true, jsonGenColumnAnnotated);
				} else if (isSuitable) {
					persistenceFields.add(new FieldContainer(f.getName(), getter, returnType, type, annotations,
							transientPersistence, true, jsonGenColumnAnnotated));
				} else {
					otherFields.add(new FieldContainer(f.getName(), getter, returnType, type, annotations,
							false, false, jsonGenColumnAnnotated));
				}
			} catch (NoSuchMethodException ignored) {

			}
		}
		for (Method method : c.getMethods()) {
			if (method.isAnnotationPresent(ETJMethod.class)) {
				ETJMethod etjMethod = method.getAnnotation(ETJMethod.class);
				Set<Annotation> annotations = new HashSet<>();
				Collections.addAll(annotations, method.getAnnotations());
				persistenceFields.add(new FieldContainer(
						etjMethod.name().equals("") ? method.getName() : etjMethod.name(),
						method, method.getReturnType(), getType(method.getReturnType()), annotations,
						false, true, true));
			}
		}
		if (otherFields.size() == 0 && persistenceFields.size() == 0 && shortInfo == null && customSerializeMethod == null) {
			classContainer = new ClassContainer(c);
		} else {
			classContainer = new ClassContainer(shortInfo, persistenceFields, otherFields, c, customSerializeMethod);
		}
		ClassDatabase.getInstance(j.adapter.getClass()).updateDatabase(classContainer);
		j.classDatabase.put(c, classContainer);
		if (classContainer.persistenceFields != null) {
			return classContainer;
		} else {
			return null;
		}
	}

	static String unGet(String s) {
		if (s != null && s.startsWith("get")) {
			return Character.toLowerCase(s.charAt(3)) + s.substring(4);
		} else return s;
	}

	private static String toGet(String s) {
		if (s == null || s.startsWith("get")) return s;
		return "get" + Character.toUpperCase(s.charAt(0)) + s.substring(1);
	}

	private static FieldContainer.MethodType getType(Class returnClass) {
		if (returnClass.isPrimitive() || String.class.isAssignableFrom(returnClass) ||
				Number.class.isAssignableFrom(returnClass) || Boolean.class.isAssignableFrom(returnClass)
				|| Character.class.isAssignableFrom(returnClass) || Enum.class.isAssignableFrom(returnClass)) {
			return FieldContainer.MethodType.DATA;
		} else if (Collection.class.isAssignableFrom(returnClass) || returnClass.isArray()) {
			return FieldContainer.MethodType.COLLECTION;
		} else {
			return FieldContainer.MethodType.CLASS;
		}
	}

	private static long classToStack(Queue<GeneratorRequest> stack, ETJReference j, Object o, Object json,
									 int remainingScanningDepth) {
		Class cl = o.getClass();
		ClassContainer classContainer = checkClass(j, cl);
		if (classContainer == null) return 0;
		if (j.nullSafeNotContains(j.whiteSetOfClasses, cl)) return 0;
		if (j.nullSafeContains(j.blackSetOfClasses, cl)) return 0;
		if (remainingScanningDepth < 0) return 0;
		Set<FieldContainer> set = new HashSet<>(classContainer.persistenceFields);
		set.addAll(classContainer.otherFields);
		set.add(classContainer.shortInfo);
		if (j.additionalMethods != null && j.additionalMethods.containsKey(cl)) {
			for (AdditionalMethodSetting ams : j.additionalMethods.get(cl)) {
				Method method = ams.getMethod();
				String name = ams.getName();
				Set<Annotation> annotations = new HashSet<>();
				Collections.addAll(annotations, method.getAnnotations());
				set.add(new FieldContainer(
						name, method, method.getReturnType(), getType(method.getReturnType()), annotations,
						false, true, true));
			}
		}
		boolean whiteCheck = j.nullSafeContains(j.whiteSetOfMethodsIn, cl);
		boolean blackCheck = j.nullSafeContains(j.blackSetOfMethodsIn, cl);
		if (whiteCheck && blackCheck) throw new RuntimeException("white&black!");
		for (FieldContainer fieldContainer : set) {
			try {
				if (!fieldContainer.jsonGenColumnAnnotated) {
					if (fieldContainer.transientPersistence &&
							!(j.includeTransientFields || j.nullSafeContains(j.includeTransientFieldsIn, fieldContainer.getter.getDeclaringClass()))
							) {
						continue;
					}
					if (!fieldContainer.suitable && !(j.includeNonJPAFields || j.nullSafeContains(j.includeNonJPAFieldsIn, fieldContainer.getter.getDeclaringClass()))) {
						continue;
					}
					if (whiteCheck && j.nullSafeNotContains(j.whiteSetOfMethods, fieldContainer.getter)) continue;
					if (blackCheck && j.nullSafeContains(j.blackSetOfMethods, fieldContainer.getter)) continue;
				}
				fieldToStack(fieldContainer, j, o, json, remainingScanningDepth, stack);
			} catch (Exception ignored) {

			}
		}
		return stack.size();
	}

	private static void fieldToStack(FieldContainer fieldContainer, ETJReference j, Object o, Object json,
									 int remainingScanningDepth, Queue<GeneratorRequest> stack)
			throws IllegalAccessException, InvocationTargetException {
		Class cl = o.getClass();
		fieldContainer.getter.setAccessible(true); //local classes
		switch (fieldContainer.methodType) {
			case DATA:
			case CLASS:
				Object o1 = fieldContainer.getter.invoke(o);
				stack.add(createGeneratorRequest(o, o1, fieldContainer, json, remainingScanningDepth));
				break;
			case COLLECTION:
				Collection collection;
				if (fieldContainer.getter.getReturnType().isArray()) {
					collection = new ArrayList();
					Object[] arr = (Object[]) fieldContainer.getter.invoke(o);
					Collections.addAll(collection, arr);
				} else collection = (Collection) fieldContainer.getter.invoke(o);
				if ((collection == null || collection.size() == 0) && (j.parseEmptyValues || j.nullSafeContains(j.parseEmptyValuesIn, cl))) {
					if (json instanceof JSONArray) {
						((JSONArray) json).put(Collections.emptyList());
					} else {
						((JSONObject) json).put(fieldContainer.fieldName, Collections.emptyList());
					}
					break;
				} else if (collection.size() == 0) break;
				FieldContainer.MethodType methodType = null;
				JSONArray jsonArray = new JSONArray();
				for (Object objectOfCollection : collection) {
					if (methodType == null) methodType = getType(objectOfCollection.getClass());
					stack.add(createGeneratorRequest(o, objectOfCollection, null, jsonArray, remainingScanningDepth));
				}
				if (json instanceof JSONArray) {
					((JSONArray) json).put(jsonArray);
				} else {
					((JSONObject) json).put(remainingScanningDepth == 0 && methodType == FieldContainer.MethodType.CLASS ?
							fieldContainer.fieldName + "Ids" : fieldContainer.fieldName, jsonArray);
				}
				break;
		}
	}

	private static GeneratorRequest createGeneratorRequest(Object caller, Object object, FieldContainer objectDescription, Object json,
														   int remainingScanningDepth) {
		if (json instanceof JSONObject) {
			return new GeneratorRequest(caller, object, objectDescription, (JSONObject) json, remainingScanningDepth);
		} else {
			return new GeneratorRequest(caller, object, objectDescription, (JSONArray) json, remainingScanningDepth);
		}
	}
}
