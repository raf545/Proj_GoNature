package reservation;

import java.sql.Timestamp;

public class Reservation {

	private String reservationID;
	private String personalID;
	private String parkname;
	private String numofvisitors;
	private String reservationtype;
	private String email;
	private Timestamp dateAndTime;
	private double price;
	private String reservetionStatus;
	private String phone;

	public Reservation(String reservationID, String personalID, String parkname, String numofvisitors,
			String reservationtype, String email, Timestamp dateAndTime, float price, String reservetionStatus,
			String phone) {
		this.reservationID = reservationID;
		this.personalID = personalID;
		this.parkname = parkname;
		this.numofvisitors = numofvisitors;
		this.reservationtype = reservationtype;
		this.email = email;
		this.dateAndTime = dateAndTime;
		this.price = price;
		this.reservetionStatus = reservetionStatus;
		this.phone = phone;
	}

	public Reservation() {
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

	public String getParkname() {
		return parkname;
	}

	public void setParkname(String parkname) {
		this.parkname = parkname;
	}

	public String getNumofvisitors() {
		return numofvisitors;
	}

	public void setNumofvisitors(String numofvisitors) {
		this.numofvisitors = numofvisitors;
	}

	public void setDateAndTime(Timestamp dateAndTime) {
		this.dateAndTime = dateAndTime;
	}

	public String getReservationtype() {
		return reservationtype;
	}

	public void setReservationtype(String reservationtype) {
		this.reservationtype = reservationtype;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Timestamp getDateAndTime() {
		return dateAndTime;
	}

	public void setDate(Timestamp date) {

		this.dateAndTime = date;

	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getReservetionStatus() {
		return reservetionStatus;
	}

	public void setReservetionStatus(String reservetionStatus) {
		this.reservetionStatus = reservetionStatus;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "Reservation [reservationID=" + reservationID + ", personalID=" + personalID + ", parkname=" + parkname
				+ ", numofvisitors=" + numofvisitors + ", reservationtype=" + reservationtype + ", email=" + email
				+ ", dateAndTime=" + dateAndTime + ", price=" + price + ", reservetionStatus=" + reservetionStatus
				+ ", phone=" + phone + "]";
	}

}
