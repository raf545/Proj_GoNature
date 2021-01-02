package employee;

/**class that define the data of an employee contains setters ,getters and to string
 * @author zivi9
 *
 */
public class Employee {
	private String employeeId;
	private String password;
	private String name;
	private String lasstName;
	private String email;
	private String typeOfEmployee;
	private String parkName;

	public Employee(String employeeId, String password) {
		this.employeeId = employeeId;
		this.password = password;
	}

	public Employee(String employeeId, String password, String name, String lasstName, String email,
			String typeOfEmployee, String parkName) {
		this.employeeId = employeeId;
		this.password = password;
		this.name = name;
		this.lasstName = lasstName;
		this.email = email;
		this.typeOfEmployee = typeOfEmployee;
		this.parkName = parkName;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLasstName() {
		return lasstName;
	}

	public void setLasstName(String lasstName) {
		this.lasstName = lasstName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTypeOfEmployee() {
		return typeOfEmployee;
	}

	public void setTypeOfEmployee(String typeOfEmployee) {
		this.typeOfEmployee = typeOfEmployee;
	}

	public String getParkName() {
		return parkName;
	}

	public void setParkName(String parkName) {
		this.parkName = parkName;
	}

	@Override
	public String toString() {
		return "Employee [employeeId=" + employeeId + ", password=" + password + ", name=" + name + ", lasstName="
				+ lasstName + ", email=" + email + ", typeOfEmployee=" + typeOfEmployee + ", parkName=" + parkName
				+ "]";
	}

}
