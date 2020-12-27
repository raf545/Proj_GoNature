package reservation;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class OptionalReservationsTuple {

	private String parkname;
	private String dateAndTimeString;
	private Button approve;
	Font commonFontSize12 = Font.font("System", FontWeight.BOLD, 12);

	public OptionalReservationsTuple(Reservation reservation) {
		this.parkname = reservation.getParkname();
		this.dateAndTimeString = reservation.getDateAndTime().toString();

		approve = new Button("Approve");
		approve.setPadding(new Insets(5));

		approve.setFont(commonFontSize12);
		approve.setStyle("-fx-background-color: #2ECC71; ");
		approve.setTextFill(Color.WHITE);
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
