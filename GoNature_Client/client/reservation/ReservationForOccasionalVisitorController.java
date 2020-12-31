package reservation;

import java.time.LocalDate;

import com.google.gson.Gson;

import client.ChatClient;
import guiCommon.StaticPaneMainPageEmployee;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.util.Callback;
import popup.PopUp;
import subscriber.Subscriber;

public class ReservationForOccasionalVisitorController {

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
	private TextField PhoneTxt;

	@FXML
	private TextField IdTxt;

	@FXML
	private Text BackBtn;

	private int countVisitor;

	Gson gson = new Gson();

	public void setIdentFields() {
		chooseParkComboBox.getItems().addAll("Niagara", "Banias", "Safari");
		hourComboBox.getItems().addAll("08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00");
		countVisitor = 1;
		numOfVisitorTxt.setText(String.valueOf(countVisitor));
		// disable not relevant data
		Callback<DatePicker, DateCell> callB = new Callback<DatePicker, DateCell>() {
			@Override
			public DateCell call(final DatePicker param) {
				return new DateCell() {
					@Override
					public void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty); // To change body of generated methods, choose Tools | Templates.
						LocalDate today = LocalDate.now();
						setDisable(empty || item.compareTo(today) < 0);
					}

				};
			}

		};
		wantedDatePicker.setDayCellFactory(callB);

	}

	@FXML
	void Back(MouseEvent event) {
		StaticPaneMainPageEmployee.employeeMainPane.getChildren().clear();
	}

	@FXML
	void Continue(ActionEvent event) {

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
		if (countVisitor == 16) {
			PopUp.display("Error", "The maximum visitors for an instructor is 16");
		} else {
			countVisitor++;
			numOfVisitorTxt.setText(String.valueOf(countVisitor));
		}
	}
}
