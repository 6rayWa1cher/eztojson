package com.a6raywa1cher.eztojson;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author 6rayWa1cher
 */
class ClassDatabase {
	private static final ClassDatabase cd = new ClassDatabase();
	private Map<Class, ClassContainer> classDatabase = new HashMap<>();

	private ClassDatabase() {

	}

	static ClassDatabase getInstance() {
		return cd;
	}

	synchronized void updateDatabase(Set<ClassContainer> set) {
		for (ClassContainer cc : set) {
			classDatabase.put(cc.aClass, cc);
		}
	}

	synchronized void updateDatabase(ClassContainer cc) {
		classDatabase.put(cc.aClass, cc);
	}

	synchronized Map<Class, ClassContainer> getDatabaseCopy() {
		return new HashMap<>(classDatabase);
	}
}
