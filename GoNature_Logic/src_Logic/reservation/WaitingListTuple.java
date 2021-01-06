package reservation;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * This class is representing a tuple in the table of the waiting list window
 *
 */
public class WaitingListTuple {
	
// Class Variables ========================================================
	private String parkname;
	private String dateAndTimeString;
	private Button approve;
	private Button cancel;
	Font commonFontSize12 = Font.font("System", FontWeight.BOLD, 12);
	
// Class Constructors =====================================================
	public WaitingListTuple(String parkname, String dateAndTimeString) {
		super();
		this.parkname = parkname;
		this.dateAndTimeString = dateAndTimeString;
		approve = new Button("Approve");
		approve.setPadding(new Insets(5));
		cancel = new Button("Cancel");
		cancel.setPadding(new Insets(5));
		
		//set fonts and color of the approve and cancel button
		approve.setFont(commonFontSize12);
		approve.setStyle("-fx-background-color: #2ECC71; "); // green
		approve.setTextFill(Color.WHITE);

		cancel.setFont(commonFontSize12);
		cancel.setStyle("-fx-background-color: #E74C3C; "); // red
		cancel.setTextFill(Color.WHITE);
	}

	/**
	 * 
	 * Constracts a WaitingList tuple using a reservation 
	 * 
	 * @param witingList
	 */
	public WaitingListTuple(Reservation witingList) {
		this.parkname = witingList.getParkname();
		this.dateAndTimeString = witingList.getDateAndTime().toString();

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

	public Button getCancel() {
		return cancel;
	}

	public void setCancel(Button cancel) {
		this.cancel = cancel;
	}

	public Font getCommonFontSize12() {
		return commonFontSize12;
	}

	public void setCommonFontSize12(Font commonFontSize12) {
		this.commonFontSize12 = commonFontSize12;
	}

}
