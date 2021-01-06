package subscriber;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 
 * This class represents a subscriber
 * 
 * @author rafaelelkoby
 *
 */
public class Subscriber { 
	
//Class variables ===============================
	private String id = null;
	private String subscriberid = null;
	private String name = null;
	private String lastName = null;
	private String phone = null;
	private String email = null;
	private String numOfMembers = null;
	private String creditCardNumber = null;
	private String subscriberType = null;
// Class constructors =================================
	public Subscriber(String id, String subscriberid, String name, String lastName, String phone, String email,
			String numOfMembers, String creditCardNumber, String subscriberType) {
		this.id = id;
		this.subscriberid = subscriberid;
		this.name = name;
		this.lastName = lastName;
		this.phone = phone;
		this.email = email;
		this.numOfMembers = numOfMembers;
		this.creditCardNumber = creditCardNumber;
		this.subscriberType = subscriberType;
	}

	public Subscriber(ResultSet subscriber) {
		try {
			this.id = subscriber.getString("id");
			this.subscriberid = subscriber.getString("subscriberid");
			this.name = subscriber.getString("firstName");
			this.lastName = subscriber.getString("lastName");
			this.phone = subscriber.getString("phone");
			this.email = subscriber.getString("email");
			this.numOfMembers = subscriber.getString("numOfMembers");
			this.creditCardNumber = subscriber.getString("creditCardNumber");
			this.subscriberType = subscriber.getString("subscriberTypre");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
// Class getters and setters ======================
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSubscriberid() {
		return subscriberid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNumOfMembers() {
		return numOfMembers;
	}

	public void setNumOfMembers(String numOfMembers) {
		this.numOfMembers = numOfMembers;
	}

	public String getCreditCardNumber() {
		return creditCardNumber;
	}

	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}

	public String getSubscriberType() {
		return subscriberType;
	}

	public void setSubscriberTypre(String subscriberTypre) {
		this.subscriberType = subscriberTypre;
	}

	public void setSubscriberId(String subscriberid) {
		this.subscriberid = subscriberid;
	}
// class methods =============================
	@Override
	public String toString() {
		return "(" + id + ", " + subscriberid + ", \"" + name + "\", \"" + lastName + "\", " + phone + ", \"" + email
				+ "\", " + numOfMembers + ", " + creditCardNumber + ", \"" + subscriberType + "\")";
	}

	public String toString2() {
		return "('" + id + "', '" + subscriberid + "','" + name + "','" + lastName + "','" + phone + "','" + email
				+ "','" + numOfMembers + "',' " + creditCardNumber + "','" + subscriberType + "')";
	}

}