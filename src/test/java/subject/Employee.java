package subject;

import com.a6raywa1cher.eztojson.annotation.ShortInfo;

import java.math.BigDecimal;
import java.time.LocalDate;

@ShortInfo(getter = "getId")
public class Employee {
	private Integer id;
	private long passnum;
	private String firstName;
	private String lastName;
	private LocalDate birthday;
	private BigDecimal salary;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public long getPassnum() {
		return passnum;
	}

	public void setPassnum(long passnum) {
		this.passnum = passnum;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public LocalDate getBirthday() {
		return birthday;
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}

	public BigDecimal getSalary() {
		return salary;
	}

	public void setSalary(BigDecimal salary) {
		this.salary = salary;
	}
}
