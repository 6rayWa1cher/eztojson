package com.a6raywa1cher.eztojson;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 6rayWa1cher
 * @version 1.1
 * @since 1.0.0
 */
class ClassDatabase {
	private static final ClassDatabase cd = new ClassDatabase();
	private Map<Class, ClassDatabaseEntry> classDatabases = new HashMap<>();

	private ClassDatabase() {

	}

	static ClassDatabaseEntry getInstance(Class adapter) {
		ClassDatabaseEntry cde = cd.classDatabases.getOrDefault(adapter, null);
		if (cde == null) {
			cde = new ClassDatabaseEntry(new HashMap<>());
			cd.classDatabases.put(adapter, cde);
		}
		return cde;
	}
}
