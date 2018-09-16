import com.a6raywa1cher.eztojson.ETJReference;
import com.a6raywa1cher.eztojson.ETJUtility;
import com.a6raywa1cher.eztojson.adapter.POJOAdapter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import subject.Aviary;
import subject.Employee;
import subject.Zoo;

import java.math.BigDecimal;
import java.time.LocalDate;

public class IntegrationTests {
	@Test
	public void simpleTest() {
		Zoo zoo = new Zoo();
		zoo.setName("New Zoo");
		zoo.setAddress("somewhere");
		JSONObject json = ETJUtility.create(new POJOAdapter())
				.process(zoo, 0);
		System.out.println(json.toString());
		Assert.assertEquals(zoo.getAddress(), json.optString("address"));
		Assert.assertEquals(zoo.getName(), json.optString("name"));
		Assert.assertNull(json.optJSONArray("aviaries"));
	}

	@Test
	public void parseEmptyValuesTest() {
		Zoo zoo = new Zoo();
		zoo.setName(null);
		JSONObject json = ETJUtility.create(new POJOAdapter())
				.configure(Zoo.class, ETJReference.Properties.PARSE_EMPTY_VALUES_IN)
				.process(zoo, 0);
		System.out.println(json.toString());
		Assert.assertNotNull(json.optJSONArray("aviaries"));
		Assert.assertTrue(json.has("name"));
		Assert.assertTrue(json.has("address"));
		json = ETJUtility.create(new POJOAdapter())
				.configure(true, ETJReference.Properties.PARSE_EMPTY_VALUES)
				.process(zoo, 0);
		System.out.println(json.toString());
		Assert.assertNotNull(json.optJSONArray("aviaries"));
		Assert.assertTrue(json.has("name"));
		Assert.assertTrue(json.has("address"));
		json = ETJUtility.create(new POJOAdapter())
				.process(zoo, 0);
		System.out.println(json.toString());
		Assert.assertEquals("{}", json.toString());
	}

	@Test
	public void variousTypes() {
		Employee employee = new Employee();
		employee.setId(42);
		employee.setBirthday(LocalDate.now());
		employee.setFirstName("Deep Thought");
		employee.setLastName(employee.getLastName());
		employee.setPassnum(1234567890L);
		employee.setSalary(new BigDecimal(99999999999L));
		JSONObject jsonObject = ETJUtility.create(new POJOAdapter())
				.process(employee);
		System.out.println(jsonObject.toString());
		Assert.assertEquals(employee.getBirthday(), jsonObject.opt("birthday"));
		Assert.assertEquals(employee.getPassnum(), jsonObject.optInt("passnum"));
		Assert.assertEquals(employee.getId(), jsonObject.opt("id"));
		Assert.assertEquals(employee.getLastName(), jsonObject.opt("last_name"));
		Assert.assertEquals(employee.getSalary().toString(), jsonObject.opt("salary").toString());
	}

	@Test
	public void arrayAndScanningTest() {
		Employee[] employees = new Employee[2];
		employees[0] = new Employee();
		employees[0].setId(154);
		employees[0].setLastName("secret");
		employees[0].setPassnum(10L);
		employees[1] = new Employee();
		employees[1].setId(145);
		employees[1].setLastName("secret_too");
		employees[1].setPassnum(14L);
		Aviary aviary = new Aviary();
		aviary.setEmployees(employees);
		aviary.setName("test_aviary");
		ETJReference etjReference = ETJUtility.create(new POJOAdapter());
		JSONObject scanning0 = etjReference.process(aviary),
				scanning1 = etjReference.process(aviary, 1);
		System.out.println(scanning0.toString());
		System.out.println(scanning1.toString());
		JSONObject emp0 = etjReference.process(employees[0]), emp1 = etjReference.process(employees[1]);
		Assert.assertTrue(scanning0.has("employeesIds"));
		Assert.assertTrue(scanning1.has("employees"));
		Assert.assertEquals(new JSONArray("[154,145]").toString(), scanning0.optJSONArray("employeesIds").toString());
		Assert.assertEquals(new JSONArray().put(emp0).put(emp1).toString(), scanning1.optJSONArray("employees").toString());
	}


}
