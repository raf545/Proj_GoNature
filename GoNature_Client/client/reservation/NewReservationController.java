package reservation;

import java.sql.Timestamp;
import com.google.gson.Gson;
import client.ChatClient;
import client.ClientUI;
import guiCommon.StaticPaneMainPageClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import popup.PopUp;
import requestHandler.RequestHandler;
import requestHandler.controllerName;
import subscriber.Subscriber;

public class NewReservationController {

	// Class variables *************************************************
	Gson gson = new Gson();

	Reservation reservationFromServer;

	@FXML
	private Button ContinueBtn;

	@FXML
	private ComboBox<String> chooseParkComboBox;

	@FXML
	private DatePicker wantedDatePicker;

	@FXML
	private ComboBox<String> hourComboBox;

	@FXML
	private TextField EmailTxt;

	@FXML
	private Button minusBtn;

	@FXML
	private Label numOfVisitorTxt;

	@FXML
	private Button plusBtn;

	@FXML
	private Hyperlink ClickHereLink;

	@FXML
	private Text BackBtn;

	private int countVisitor;

	// Instance methods ************************************************

	/**
	 *  Sets the relevant fields in the newReservation section
	 */
	public void setIdentFields() {
		chooseParkComboBox.getItems().addAll("Niagara", "Banias", "Safari");
		hourComboBox.getItems().addAll("08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00");
		countVisitor = 1;
		numOfVisitorTxt.setText(String.valueOf(countVisitor));
		if (!ChatClient.clientTypeString.equals("Guest")) {
			Subscriber currentSubscriber = gson.fromJson(ChatClient.clientInfo, Subscriber.class);
			EmailTxt.setText(currentSubscriber.getEmail());
		}

	}

	/**
	 * 
	 * This method is invoked by the back button
	 * 
	 * @param event
	 */
	@FXML
	void Back(MouseEvent event) {

		StaticPaneMainPageClient.clientMainPane.getChildren().clear();
	}

	@FXML
	void ClickHereLink(ActionEvent event) {

	}

	/**
	 * 
	 * This method is invoked by the continue button witch initiate the new
	 * reservation process
	 * 
	 * @param event
	 */
	@SuppressWarnings("deprecation")
	@FXML
	void Continue(ActionEvent event) {

		Timestamp reservationDateAndTime;
		StringBuilder timeFromCombo = new StringBuilder();
		StringBuilder errorMessage = new StringBuilder();
		String selectedCombo = hourComboBox.getSelectionModel().getSelectedItem();

		if (selectedCombo == null) {
			errorMessage.append("No hour selected\n");
		}
		if (EmailTxt.getText().isEmpty()) {
			errorMessage.append("No Email enterd\n");
		}
		selectedCombo = chooseParkComboBox.getSelectionModel().getSelectedItem();
		if (selectedCombo == null) {
			errorMessage.append("No park selected\n");
		}
		if (wantedDatePicker.getValue() == null) {
			errorMessage.append("No date selected\n");
		}

		if (errorMessage.length() == 0) {
			timeFromCombo.append(hourComboBox.getValue());
			timeFromCombo.delete(2, 5);
			int hour = Integer.parseInt(timeFromCombo.toString());

			int day = wantedDatePicker.getValue().getDayOfMonth();
			int month = wantedDatePicker.getValue().getMonthValue();
			int year = wantedDatePicker.getValue().getYear();

			reservationDateAndTime = new Timestamp(year - 1900, month - 1, day, hour, 00, 00, 00);
			System.out.println(reservationDateAndTime);

			Reservation reservation = new Reservation("", ChatClient.clientIdString, chooseParkComboBox.getValue(),
					numOfVisitorTxt.getText(), ChatClient.clientTypeString, EmailTxt.getText(), reservationDateAndTime,
					0, "valid");
			RequestHandler requestNewReservationId = new RequestHandler(controllerName.ReservationController,
					"createNewReservation", gson.toJson(reservation));
			ClientUI.chat.accept(gson.toJson(requestNewReservationId));
			analyzeAnswerFromServer();
		} else {
			PopUp.display("Error", errorMessage.toString());
		}

	}

	/**
	 * Increase the number of people in the counter
	 *
	 * @param event
	 */
	@FXML
	void minus(ActionEvent event) {
		if (countVisitor == 1)
			PopUp.display("Error", "Can not place an order for less then one");
		else {
			countVisitor--;
			numOfVisitorTxt.setText(String.valueOf(countVisitor));
		}
	}

	/**
	 * Decrease the number of people in the counter
	 *
	 * @param event
	 */
	@FXML
	void plus(ActionEvent event) {
		if (ChatClient.clientTypeString.equals("instructor") && countVisitor == 16) {
			PopUp.display("Error", "The maximum visitors for an instructor is 16");
		} else {
			countVisitor++;
			numOfVisitorTxt.setText(String.valueOf(countVisitor));
		}
	}

	/**
	 * Analyze the message returned from the server and display feedback using a
	 * PopUp window
	 */
	private void analyzeAnswerFromServer() {
		String answer = ChatClient.serverMsg;
		switch (answer) {
		case "There is no available space in the park\n for the given time":
			PopUp.display("Error", answer);
			break;
		case "fail update reservation ID":
			PopUp.display("Error", answer);
			break;
		case "fail insert reservation to DB":
			PopUp.display("Error", answer);
			break;
		default:
			reservationFromServer = gson.fromJson(answer, Reservation.class);
			PopUp.display("sucess", "Reservation was placed successfuly\n " + "your Reservation id is: "
					+ reservationFromServer.getReservationID());
			break;
		}
	}

}
