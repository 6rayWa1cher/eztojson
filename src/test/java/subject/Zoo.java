package subject;

import com.a6raywa1cher.eztojson.annotation.ShortInfo;

import java.util.List;

@ShortInfo(getter = "getName")
public class Zoo {
	private String name;
	private String address;
	private List<Aviary> aviaries;

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

	public void setAviaries(List<Aviary> aviaries) {
		this.aviaries = aviaries;
	}
}
