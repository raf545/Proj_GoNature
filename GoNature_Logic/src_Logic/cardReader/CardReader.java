package cardReader;

import java.sql.Timestamp;

public class CardReader {

	private String reservationID;
	private String personalID;
	private String phoneNumber;
	private Timestamp entryTime;
	private String numberOfVisitors;
	private String parkName;
	private String typeOfVisitor;
	
	public CardReader(String reservationID, String personalID, String phoneNumber, Timestamp entryTime,
			String numberOfVisitors, String parkName, String typeOfVisitor) {
		super();
		this.reservationID = reservationID;
		this.personalID = personalID;
		this.phoneNumber = phoneNumber;
		this.entryTime = entryTime;
		this.numberOfVisitors = numberOfVisitors;
		this.parkName = parkName;
		this.typeOfVisitor = typeOfVisitor;
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
	
}
