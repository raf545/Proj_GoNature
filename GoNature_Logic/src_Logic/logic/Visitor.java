package logic;

/**
 * This class represents a General visitor
 *
 * @author Dan Gutchin
 * @author Yaniv Sokolov
 * @author Rafael elkoby
 * @version December 3 2020
 */

public class Visitor {
// Class variables *************************************************
	private String name, email, id, lastname, phone;

// Class Constructors *************************************************
	public Visitor() {
		this.name = null;
		this.email = null;
		this.id = null;
		this.lastname = null; 
		this.phone = null;
	}

	/**
	 * @param name     First name
	 * @param email    Email
	 * @param id       ID
	 * @param lastname Last name
	 * @param phone    Phone number
	 */
	public Visitor(String name, String email, String id, String lastname, String phone) {
		this.name = name;
		this.email = email;
		this.id = id;
		this.lastname = lastname;
		this.phone = phone;
	}
// Class Getters *************************************************

	public String getEmail() {
		return email;
	}

	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}

	public String getLastname() {
		return lastname;
	}

	public String getPhone() {
		return phone;
	}

// Class Setters *************************************************
	public void setName(String name) {
		this.name = name;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setEmail(String email) {
		this.email = email;
	}

// Overridden methods ************************************************
	@Override
	public String toString() {
		StringBuilder ts = new StringBuilder();
		if (id == null) {
			return "Error";
		}
		ts.append(id);
		ts.append(" ");
		ts.append(name);
		ts.append(" ");
		ts.append(lastname);
		ts.append(" ");
		ts.append(phone);
		ts.append(" ");
		ts.append(email);
		return ts.toString();

	}

}
