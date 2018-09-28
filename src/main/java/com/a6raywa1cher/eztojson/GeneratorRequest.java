package com.a6raywa1cher.eztojson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Objects;

/**
 * @author 6rayWa1cher
 * @version 1.0
 * @since 1.0.0
 */
class GeneratorRequest {
	Object caller;
	Object object;
	FieldContainer objectDescription;
	JSONObject associatedJO;
	JSONArray associatedJA;
	int remainingScanningDepth;

	GeneratorRequest(Object caller, Object object, FieldContainer objectDescription, JSONObject associatedJO, int remainingScanningDepth) {
		this.caller = caller;
		this.object = object;
		this.objectDescription = objectDescription;
		this.associatedJO = associatedJO;
		this.remainingScanningDepth = remainingScanningDepth;
	}

	GeneratorRequest(Object caller, Object object, FieldContainer objectDescription, JSONArray associatedJA, int remainingScanningDepth) {
		this.caller = caller;
		this.object = object;
		this.objectDescription = objectDescription;
		this.associatedJA = associatedJA;
		this.remainingScanningDepth = remainingScanningDepth;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof GeneratorRequest)) return false;
		GeneratorRequest that = (GeneratorRequest) o;
		return remainingScanningDepth == that.remainingScanningDepth &&
				Objects.equals(caller, that.caller) &&
				Objects.equals(object, that.object) &&
				Objects.equals(objectDescription, that.objectDescription) &&
				Objects.equals(associatedJO, that.associatedJO) &&
				Objects.equals(associatedJA, that.associatedJA);
	}

	@Override
	public int hashCode() {

		return Objects.hash(caller, object, objectDescription, associatedJO, associatedJA, remainingScanningDepth);
	}

	@Override
	public String toString() {
		if (associatedJA != null) {
			return "GeneratorRequest{" +
					"caller=" + caller +
					", object=" + object +
					", objectDescription=" + objectDescription.fieldName +
					", associatedJA=" + associatedJA +
					", remainingScanningDepth=" + remainingScanningDepth +
					'}';
		} else {
			return "GeneratorRequest{" +
					"caller=" + caller +
					", object=" + object +
					", objectDescription=" + objectDescription.fieldName +
					", associatedJO=" + associatedJO +
					", remainingScanningDepth=" + remainingScanningDepth +
					'}';
		}
	}
}
