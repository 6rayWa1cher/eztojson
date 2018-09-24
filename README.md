# EzToJson
Java-classes to JSON converter for use during development.  

Main feature - scanning depth. Zero means that only this class will be showed
fully in JSONObject, and increasing this value by one you'll get one more
layer of scanning.

# Usage
1. Be sure to make getters for all necessary fields
2. Receive object of ETJReference: `ETJUtility.create()`
3. Set up necessary parameters: `etjReference.configure(Object, Properties)`,
where Object - new value of parameter
4. Using etjReference, parse Java class to JSON: `etjReference.process(Object, int)`,
where int is scanning depth. 

# Download
Maven dependency:
```
<dependency>
  <groupId>com.a6raywa1cher</groupId>
  <artifactId>eztojson</artifactId>
  <version>1.0.0</version>
</dependency>
```
Jar file also in releases tab. In this case, you need to download https://github.com/stleary/JSON-java too.
