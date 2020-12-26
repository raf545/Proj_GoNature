package subscriber;

public class Subscriber {
	private String id=null;
	private  String subscriberid=null;
	private String name=null;
	private String lastName=null;
	private String phone=null;
	private String email=null;
	private String numOfMembers=null;
	private String creditCardNumber=null;
	private String subscriberType=null;

	public Subscriber(String id, String subscriberid, String name, String lastName, String phone, String email,
			String numOfMembers, String creditCardNumber, String subscriberType) {
		this.id = id;
		this.subscriberid=	subscriberid;
		this.name = name;
		this.lastName = lastName;
		this.phone = phone;
		this.email = email;
		this.numOfMembers = numOfMembers;
		this.creditCardNumber = creditCardNumber;
		this.subscriberType = subscriberType;
	}

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

	@Override
	public String toString() {
		return "(" + id + ", " + subscriberid + ", \"" + name + "\", \"" + lastName + "\", " + phone + ", \"" + email + "\", "
				+ numOfMembers + ", " + creditCardNumber + ", \"" + subscriberType + "\")";
	}
	public String toString2() {
		return "('" + id + "', '" + subscriberid + "','" + name + "','" + lastName + "','" + phone + "','" + email + "','"
				+ numOfMembers + "',' " + creditCardNumber + "','" + subscriberType + "')";
	}


}