package subject;

import com.a6raywa1cher.eztojson.annotation.ShortInfo;

@ShortInfo(getter = "getNumber")
public class Aviary {
	private String name;
	private int number;
	private Employee[] employees;

	public String getName() {
		return name;
	}

	public int getNumber() {
		return number;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public Employee[] getEmployees() {
		return employees;
	}

	public void setEmployees(Employee[] employees) {
		this.employees = employees;
	}
}
