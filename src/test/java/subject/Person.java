package subject;

import com.a6raywa1cher.eztojson.annotation.ETJCustSerialMethod;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;

@ETJCustSerialMethod(getter = "toJSONObject")
public class Person {
	private Integer age;
	private String name;
	private LocalDate recDate = LocalDate.of(2018, 10, 2);

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public JSONObject toJSONObject() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("customAge", this.age);
		jsonObject.put("customName", this.name);
		return jsonObject;
	}
}
