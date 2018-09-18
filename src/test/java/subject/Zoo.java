package subject;

import com.a6raywa1cher.eztojson.annotation.ShortInfo;

import java.util.List;

@ShortInfo(getter = "getName")
public class Zoo {
	private String name;
	private String address;
	private List<Aviary> aviaries;
	private List<Employee> employees;

	public Zoo() {
	}

	public Zoo(String name, String address) {
		this.name = name;
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public List<Aviary> getAviaries() {
		return aviaries;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

	public void setAviaries(List<Aviary> aviaries) {
		this.aviaries = aviaries;
	}
}
