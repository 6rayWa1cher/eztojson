package subject;

import java.util.ArrayList;
import java.util.List;

public class Plane {
	List<Person> personList = new ArrayList<>();

	public List<Person> getPersonList() {
		return personList;
	}

	public void setPersonList(List<Person> personList) {
		this.personList = personList;
	}
}
