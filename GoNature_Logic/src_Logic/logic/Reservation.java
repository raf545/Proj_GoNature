package logic;

public class Reservation {

	private String reservationID;
	private String personalID;
	private String parkname;
	private String visithour;
	private String numofvisitors;
	private String reservationtype;
	private String email;
	private String date;
	private float price;
	private String reservetionStatus;

	public Reservation(String reservationID, String personalID, String parkname, String visithour, String numofvisitors,
			String reservationtype, String email, String date, float price, String reservetionStatus) {
		this.reservationID = reservationID;
		this.personalID = personalID;
		this.parkname = parkname;
		this.visithour = visithour;
		this.numofvisitors = numofvisitors;
		this.reservationtype = reservationtype;
		this.email = email;
		this.date = date;
		this.price = price;
		this.reservetionStatus = reservetionStatus;
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

	public String getVisithour() {
		return visithour;
	}

	public void setVisithour(String visithour) {
		this.visithour = visithour;
	}

	public String getNumofvisitors() {
		return numofvisitors;
	}

	public void setNumofvisitors(String numofvisitors) {
		this.numofvisitors = numofvisitors;
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getReservetionStatus() {
		return reservetionStatus;
	}

	public void setReservetionStatus(String reservetionStatus) {
		this.reservetionStatus = reservetionStatus;
	}

	@Override
	public String toString() {
		return "Reservation [reservationID=" + reservationID + ", personalID=" + personalID + ", parkname=" + parkname
				+ ", visithour=" + visithour + ", numofvisitors=" + numofvisitors + ", reservationtype="
				+ reservationtype + ", email=" + email + ", date=" + date + ", price=" + price + ", reservetionStatus="
				+ reservetionStatus + "]";
	}

}
