package reservation;

import java.sql.Timestamp;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * 
 * This class represent a tuple in the optional reservation table of optional
 * times in case of a occupied hour
 * 
 * @author rafael elkoby
 *
 */
public class OptionalReservationsTuple {

// Class Variables =======================================
	private String parkname;
	private Timestamp dateAndTimeString;
	private Button approve;
	Font commonFontSize12 = Font.font("System", FontWeight.BOLD, 12);
	
// Class constructors =======================================
	public OptionalReservationsTuple(Reservation reservation) {
		this.parkname = reservation.getParkname();
		this.dateAndTimeString = reservation.getDateAndTime();

		approve = new Button("Approve");
		approve.setPadding(new Insets(5));

		approve.setFont(commonFontSize12);
		approve.setStyle("-fx-background-color: #2ECC71; ");
		approve.setTextFill(Color.WHITE);
	}
	
// Class Getters and Setters =======================================
	public String getParkname() {
		return parkname;
	}

	public void setParkname(String parkname) {
		this.parkname = parkname;
	}

	public Timestamp getDateAndTimeString() {
		return dateAndTimeString;
	}

	public void setDateAndTimeString(Timestamp dateAndTimeString) {
		this.dateAndTimeString = dateAndTimeString;
	}

	public Button getApprove() {
		return approve;
	}

	public void setApprove(Button approve) {
		this.approve = approve;
	}

}
