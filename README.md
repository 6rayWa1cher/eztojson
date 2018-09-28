# EzToJson
Java-classes to JSON converter for use during development.  

Main feature - scanning depth. Zero means that only this class will be showed
fully in JSONObject, and increasing this value by one you'll get one more
layer of scanning.

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


# Usage
## Fast setup
1. Be sure to make getters for all necessary fields
2. Receive object of ETJReference: `ETJUtility.create(Adapter)`
   1. If you plan to parse classes with JPA annotations (e.g., `@Column`, `@Id`, `@Transient`), use JPAAdapter.
   2. In other cases, use POJOAdapter or implement Adapter. You probably would like to set @ShortInfo annotation.
3. Set up necessary parameters: `etjReference.configure(Object, Properties)`, where Object - new value of parameter
4. Using etjReference, parse Java class to JSON: `etjReference.process(Object, int)`, where int is scanning depth.
## Annotations
You can use annotations:
1. `@ETJMethod(name)` This annotation marks method as compulsory for parsing into JSON. 'name' - Key in JSON. 
By default, it's the name of the method.
2. `@ETJField` This annotation marks the field as compulsory for parsing, if this field is extraneous or has another 
blocking modification.
3. `@ShortInfo(name, getter)` Annotation ShortInfo used as a pointer to getter, which can provide short info about 
this object. It's using when current scanning depth equals 0, but we have to provide some information about 
non-primitive object, stored in the variable of the current scanning object. If this annotation isn't setted, parser 
will use value of toString().
   1. 'name' - Key in JSON, By default, it's the name of the method.
   2. 'getter' - Getter of short info, necessary. Examples: "getId", "getName"
## Properties of ETJReference
* `WHITELIST_OF_CLASSES`, `BLACKLIST_OF_CLASSES`
   *  This properties can be used, when you need to parse only several fields in class. Whitelist allows only several
   classes to be parsed, blacklist prevents some classes to be parsed. Do not use both of them simultaneously!
   
   Example:
   ```java
   JSONObject jsonObject = ETJUtility.create(new POJOAdapter())
   				.configure(Employee.class, ETJReference.Properties.BLACKLIST_OF_CLASSES)
   				.process(aviary, 2);
   ```
