package cardReader;

import java.sql.Timestamp;

/**
 * this class represent all the data that the card reader has.
 * its purpose is mainly to save the card data and upload it to the server later.
 * @author dan
 *
 */
public class CardReader {

	private String reservationID;
	private String personalID;
	private String phoneNumber;
	private Timestamp entryTime;
	private Timestamp exitTime;
	private String numberOfVisitors;
	private String parkName;
	private String typeOfVisitor;
	private Double price;

	public CardReader(String reservationID, String personalID, String phoneNumber, Timestamp entryTime,
			Timestamp exitTime, String numberOfVisitors, String parkName, String typeOfVisitor, Double price) {
		super();
		this.reservationID = reservationID;
		this.personalID = personalID;
		this.phoneNumber = phoneNumber;
		this.entryTime = entryTime;
		this.exitTime = exitTime;
		this.numberOfVisitors = numberOfVisitors;
		this.parkName = parkName;
		this.typeOfVisitor = typeOfVisitor;
		this.price = price;
	}

	public String getReservationID() {
		return reservationID;
	}

	public void setReservationID(String reservationID) {
		this.reservationID = reservationID;
	}

	public String getPersonalID() {
		return personalID;
	}

	public void setPersonalID(String personalID) {
		this.personalID = personalID;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Timestamp getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(Timestamp entryTime) {
		this.entryTime = entryTime;
	}

	public Timestamp getExitTime() {
		return exitTime;
	}

	public void setExitTime(Timestamp exitTime) {
		this.exitTime = exitTime;
	}

	public String getNumberOfVisitors() {
		return numberOfVisitors;
	}

	public void setNumberOfVisitors(String numberOfVisitors) {
		this.numberOfVisitors = numberOfVisitors;
	}

	public String getParkName() {
		return parkName;
	}

	public void setParkName(String parkName) {
		this.parkName = parkName;
	}

	public String getTypeOfVisitor() {
		return typeOfVisitor;
	}

	public void setTypeOfVisitor(String typeOfVisitor) {
		this.typeOfVisitor = typeOfVisitor;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
}
