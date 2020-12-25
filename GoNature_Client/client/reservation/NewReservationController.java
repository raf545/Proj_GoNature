package reservation;

import java.sql.Timestamp;

import com.google.gson.Gson;

import client.ChatClient;
import client.ClientUI;
import common.ChatIF;
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

public class NewReservationController {

	Gson gson = new Gson();
	@FXML
	private Button ContinueBtn;

	@FXML
	private ComboBox<String> ChooseParkComboBox;

	@FXML
	private DatePicker WantedDatePicker;

	@FXML
	private ComboBox<String> HourComboBox;

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

	public void setIdentFields() {
		ChooseParkComboBox.getItems().addAll("Niagara", "Banias", "Safari");
		HourComboBox.getItems().addAll("08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00");
		countVisitor = 0;
		numOfVisitorTxt.setText(String.valueOf(countVisitor));

	}

	@FXML
	void Back(MouseEvent event) {

		StaticPaneMainPageClient.clientMainPane.getChildren().clear();
	}

	@FXML
	void ClickHereLink(ActionEvent event) {

	}

	@SuppressWarnings("deprecation")
	@FXML
	void Continue(ActionEvent event) {

		Timestamp reservationDateAndTime;
		StringBuilder timeFromCombo = new StringBuilder();
		timeFromCombo.append(HourComboBox.getValue());
		timeFromCombo.delete(2, 5);
		int hour = Integer.parseInt(timeFromCombo.toString());

		int day = WantedDatePicker.getValue().getDayOfMonth();
		int month = WantedDatePicker.getValue().getMonthValue();
		int year = WantedDatePicker.getValue().getYear();

		reservationDateAndTime = new Timestamp(year - 1900, month - 1, day, hour, 00, 00, 00);
		System.out.println(reservationDateAndTime);

		Reservation reservation = new Reservation("", ChatClient.clientIdString, ChooseParkComboBox.getValue(),
				numOfVisitorTxt.getText(), ChatClient.clientTypeString, EmailTxt.getText(), reservationDateAndTime, 0,
				"valid");
		RequestHandler requestNewReservationId = new RequestHandler(controllerName.ReservationController,
				"createNewReservation", gson.toJson(reservation));
		ClientUI.chat.accept(gson.toJson(requestNewReservationId));

	}

	@FXML
	void minus(ActionEvent event) {
		if (countVisitor == 0)
			PopUp.display("Error", "Can not order less then zero");
		else {
			countVisitor--;
			numOfVisitorTxt.setText(String.valueOf(countVisitor));
		}
	}

	@FXML
	void plus(ActionEvent event) {
		System.out.println(ChatClient.clientTypeString);
		if (ChatClient.clientTypeString.equals("instructor") && countVisitor == 16) {
			PopUp.display("Error", "The maximum visitors for an instructor is 16");
		} else {
			countVisitor++;
			numOfVisitorTxt.setText(String.valueOf(countVisitor));
		}
	}

}
