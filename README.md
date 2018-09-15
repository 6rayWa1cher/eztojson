# Easy-To-JSON
Java-classes to JSON converter for use during development

# Usage
1. Be sure to make getters for all fields
2. Receive object of ETJReference: `ETJUtility.create()`
3. Set up necessary parameters: `etjReference.configure(Properties, Object)`,
where Object - new value of parameter
4. Using etjReference, parse Java class to JSON: `etjReference.process(Object, int)`,
where int is scanning depth. Zero means that only this class will be showed
fully in JSONObject, and increasing this value by one you'll get one more
layer of scanning.