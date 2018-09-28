package com.a6raywa1cher.eztojson;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author 6rayWa1cher
 * @version 1.0
 * @since 1.0.1
 */
class ClassDatabaseEntry {
	private Map<Class, ClassContainer> classDatabase;

	public ClassDatabaseEntry(Map<Class, ClassContainer> classDatabase) {
		this.classDatabase = classDatabase;
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
