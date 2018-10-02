# EzToJson
[![Build Status](https://semaphoreci.com/api/v1/6raywa1cher/eztojson/branches/master/shields_badge.svg)](https://semaphoreci.com/6raywa1cher/eztojson)

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
  <version>1.0.1</version>
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
  
Example:
```
JSONObject json = ETJUtility.create(new POJOAdapter())
				.process(zoo, 0);
```
## Annotations
You can use annotations:
* `@ETJMethod(name)` This annotation marks method as compulsory for parsing into JSON. 'name' - Key in JSON. 
By default, it's the name of the method.
* `@ETJField` This annotation marks the field as compulsory for parsing, if this field is extraneous or has another 
blocking modification.
* `@ShortInfo(name, getter)` Annotation ShortInfo used as a pointer to getter, which can provide short info about 
this object. It's using when current scanning depth equals 0, but we have to provide some information about 
non-primitive object, stored in the variable of the current scanning object. If this annotation isn't setted, parser 
will use value of toString().
   1. 'name' - Key in JSON, By default, it's the name of the method.
   2. 'getter' - Getter of short info, necessary. Examples: "getId", "getName"
## Properties of ETJReference
These properties operate only within this object of ETJReference. 
* `WHITELIST_OF_CLASSES`, `BLACKLIST_OF_CLASSES`  
   This properties can be used, when you need to parse only several classes from request. Whitelist allows only several
   classes to be parsed, blacklist prevents some classes to be parsed. Do not use both of them simultaneously!
   
   Example:
   ```
   etjReference.configure(Employee.class, ETJReference.Properties.BLACKLIST_OF_CLASSES);
   ```
* `WHITELIST_OF_METHODS`, `BLACKLIST_OF_METHODS`  
    Sounds and works like previous properties, but for methods (getters too). This list is unique for every class, and
    when it doesn't set up for a specific class, parsing will be as always. Do not use both of them for same class 
    simultaneously!
    
    Example:
    ```
    etjReference.configure("subject.Aviary.getEmployees", ETJReference.Properties.BLACKLIST_OF_METHODS);
    ```
*  `SHORT_ONLY_SET`  
    Classes in this list will be parsed shortly, i.e. only short info will be included in final JSON. Short info - 
    also identification info - briefly characterizes the object. For example, it could be primary key in a database.
    
    Example:
    ```
    etjReference.configure(Employee.class, ETJReference.Properties.SHORT_ONLY_SET);
    ```
*  `ALLOW_DUPLICATES`, `ALLOW_DUPLICATES_IN`, `ALLOW_DUPLICATES_OF`  
    The utility can detect duplicates and prevent them to parse fully, leaving only short info. However, in some cases, 
    you probably want to parse these objects. You can do this in three ways:
    *   Set `ALLOW_DUPLICATES` to `true`. Duplication will be allowed everywhere (for this etjReference, of course).  
    Example:
    ```
    etjReference.configure(true, ETJReference.Properties.ALLOW_DUPLICATES);
    ```
    *   Add class to `ALLOW_DUPLICATES_IN`, if duplicates are needed only in this class.
    *   Add class to `ALLOW_DUPLICATES_OF`, if you need to allow duplicates for only specific classes.
*   `ADDITIONAL_METHODS`  
    The utility parses primarily pairs "field - getter of field". If you need to attach value from other function, you
    can use `@ETJMethod(name)`, or use `ADDITIONAL_METHODS` property. Value of the property is AdditionalMethodSetting
    object, check javadoc for constructors.
    
    Example: 
    ```
    JSONObject json = ETJUtility.create(new POJOAdapter())
    				.configure(new AdditionalMethodSetting("subject.Employee.getFullName", "trigger"), 
    						ETJReference.Properties.ADDITIONAL_METHODS)
    				.process(employee);
    ```
*   `PARSE_EMPTY_VALUES`, `PARSE_EMPTY_VALUES_IN`  
    When utility stumbles on value `null` or empty collections, they don't appear in JSON. However, if you need these 
    keys with null values in JSON, you can:
    *   Set `PARSE_EMPTY_VALUES` to `true`, and this will change behavior everywhere (for this etjReference, 
    of course).
    *   Add class to `PARSE_EMPTY_VALUES_IN`, and changes will affect only specific classes. 
    
    This properties will forward empty collections to JSON, in other cases leave `null`.
*   `SCANNING_DEPTH`
    This property replaces default value of `scanningDepth`. It affects only `process(Object)`.
    
    Example:
    ```
    ETJReference etjReference = ETJUtility.create(new POJOAdapter())
    				.configure(2, ETJReference.Properties.SCANNING_DEPTH);
    JSONObject json1 = etjReference.process(aviary1);
    JSONObject json2 = etjReference.process(aviary2);
    ```
*   `INCLUDE_TRANSIENT_FIELDS`, `INCLUDE_TRANSIENT_FIELDS_IN`  
    Properties activates the parsing of transient fields. Transient field means that this field doesn't provide 
    necessary information and it can be skipped in final JSON. It can be a local variable, using for in-session calculations, 
    buffer, input-output stream, etc. It depends on Adapter. For JPAAdapter, it is fields with `@Transient` annotation.
    If you need them, you can:
    *   Set `INCLUDE_TRANSIENT_FIELDS` to `true` and this will change behavior everywhere (for this etjReference, of 
    course).
    *   Add class to `INCLUDE_TRANSIENT_FIELDS_IN`, and changes will affect only specific classes. 
*   `INCLUDE_EXTRANEOUS_FIELDS`, `INCLUDE_EXTRANEOUS_FIELDS_IN`  
    Properties activates the parsing of extraneous fields.  Extraneous field doesn't hold information about their 
    purpose. It can be service information, java internal classes. It depends on Adapter. 
    If you need extraneous fields, you can:
    *   Set `INCLUDE_EXTRANEOUS_FIELDS` to `true` and this will change behavior everywhere (for this etjReference, of 
    course).
    *   Add class to `INCLUDE_EXTRANEOUS_FIELDS_IN`, and changes will affect only specific classes. 
