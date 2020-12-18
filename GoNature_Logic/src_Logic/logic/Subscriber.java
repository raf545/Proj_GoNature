package logic;

public class Subscriber {
	private String id;
	private String subscriberid;
	private String name;
	private String lastName;
	private String phone;
	private String email;
	private String numOfMembers;
	private String creditCardNumber;
	private String subscriberTypre;

	public Subscriber(String id, String subscriberid, String name, String lastName, String phone, String email,
			String numOfMembers, String creditCardNumber, String subscriberTypre) {
		this.id = id;
		this.subscriberid = subscriberid;
		this.name = name;
		this.lastName = lastName;
		this.phone = phone;
		this.email = email;
		this.numOfMembers = numOfMembers;
		this.creditCardNumber = creditCardNumber;
		this.subscriberTypre = subscriberTypre;
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

	public void setSubscriberid(String subscriberid) {
		this.subscriberid = subscriberid;
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

	public String getSubscriberTypre() {
		return subscriberTypre;
	}

	public void setSubscriberTypre(String subscriberTypre) {
		this.subscriberTypre = subscriberTypre;
	}

	@Override
	public String toString() {
		return id + ", " + subscriberid + ", " + name + ", " + lastName + ", " + phone + ", " + email + ", "
				+ numOfMembers + ", " + creditCardNumber + ", " + subscriberTypre;
	}

}