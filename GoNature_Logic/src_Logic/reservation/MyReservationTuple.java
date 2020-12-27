package reservation;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MyReservationTuple {
	private String numofvisitors;
	private String reservationID;
	private String parkname;
	private String dateAndTimeString;
	private Button approve;
	private Button cancel;
	Font commonFontSize12 = Font.font("System", FontWeight.BOLD, 12);
	
	public MyReservationTuple(String numofvisitors, String reservationID, String parkname, String dateAndTimeString) {
		super();
		this.numofvisitors = numofvisitors;
		this.reservationID = reservationID;
		this.parkname = parkname;
		this.dateAndTimeString = dateAndTimeString;
		approve = new Button("Approve");
		approve.setPadding(new Insets(5));
		cancel = new Button("Cancel");
		cancel.setPadding(new Insets(5));
		
		approve.setFont(commonFontSize12);
		approve.setStyle("-fx-background-color: #2ECC71; ");
		approve.setTextFill(Color.WHITE);
		
		cancel.setFont(commonFontSize12);
		cancel.setStyle("-fx-background-color: #E74C3C; ");
		cancel.setTextFill(Color.WHITE);
	}

	public MyReservationTuple(Reservation reservation) {
		this.numofvisitors = reservation.getNumofvisitors();
		this.reservationID = reservation.getReservationID();
		this.parkname = reservation.getParkname();
		this.dateAndTimeString = reservation.getDateAndTime().toString();

		approve = new Button("Approve");
		approve.setPadding(new Insets(5));
		cancel = new Button("Cancel");
		cancel.setPadding(new Insets(5));
		
		approve.setFont(commonFontSize12);
		approve.setStyle("-fx-background-color: #2ECC71; ");
		approve.setTextFill(Color.WHITE);
		
		cancel.setFont(commonFontSize12);
		cancel.setStyle("-fx-background-color: #E74C3C; ");
		cancel.setTextFill(Color.WHITE);
	}

	public Button getCancel() {
		return cancel;
	}

	public void setCancel(Button cancel) {
		this.cancel = cancel;
	}

	public String getNumofvisitors() {
		return numofvisitors;
	}

	public void setNumofvisitors(String numofvisitors) {
		this.numofvisitors = numofvisitors;
	}

	public String getReservationID() {
		return reservationID;
	}

	public void setReservationID(String reservationID) {
		this.reservationID = reservationID;
	}

	public String getParkname() {
		return parkname;
	}

	public void setParkname(String parkname) {
		this.parkname = parkname;
	}

	public String getDateAndTimeString() {
		return dateAndTimeString;
	}

	public void setDateAndTimeString(String dateAndTimeString) {
		this.dateAndTimeString = dateAndTimeString;
	}

	public Button getApprove() {
		return approve;
	}

	public void setApprove(Button approve) {
		this.approve = approve;
	}
}
