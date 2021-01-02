package reservation;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import com.google.gson.Gson;
import client.ChatClient;
import client.ClientUI;
import employee.BlankEmployeeController;
import fxmlGeneralFunctions.FXMLFunctions;
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
import requestHandler.RequestHandler;
import requestHandler.controllerName;
import subscriber.Subscriber;

public class ReservationForOccasionalVisitorController {

	@FXML
	private Button ContinueBtn;

	@FXML
	private ComboBox<String> chooseParkComboBox;

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
		countVisitor = 1;
		numOfVisitorTxt.setText(String.valueOf(countVisitor));

	}

	@FXML
	void Back(MouseEvent event) throws IOException {
		StaticPaneMainPageEmployee.employeeMainPane.getChildren().clear();
		BlankEmployeeController controller = FXMLFunctions.loadSceneToMainPane(BlankEmployeeController.class,
				"BlankEmployee.fxml", StaticPaneMainPageEmployee.employeeMainPane).getController();
		controller.setBlank();
	}

	@FXML
	void Continue(ActionEvent event) {
		String selectedCombo = chooseParkComboBox.getSelectionModel().getSelectedItem();
		StringBuilder errorMessage = new StringBuilder();
		if (EmailTxt.getText().isEmpty()) {
			errorMessage.append("No Email enterd\n");
		}
		if (PhoneTxt.getText().isEmpty()) {
			errorMessage.append("No Phone number enterd\n");
		}
		if (IdTxt.getText().isEmpty()) {
			errorMessage.append("No id number enterd\n");
		}

		if (selectedCombo == null) {
			errorMessage.append("No Park selected\n");
		}
		if (errorMessage.length() == 0) {
			Reservation occasionalVisitor = new Reservation();
			occasionalVisitor.setReservationID(null);
			occasionalVisitor.setPersonalID(IdTxt.getText());
			occasionalVisitor.setParkname(selectedCombo);
			occasionalVisitor.setNumofvisitors(numOfVisitorTxt.getText());
			occasionalVisitor.setReservationtype(null);
			occasionalVisitor.setEmail(EmailTxt.getText());
			occasionalVisitor.setDateAndTime(new Timestamp(System.currentTimeMillis()));
			occasionalVisitor.setPrice(0);
			occasionalVisitor.setReservetionStatus("Approved");
			occasionalVisitor.setPhone(PhoneTxt.getText());
			RequestHandler requestToServer = new RequestHandler(controllerName.ReservationController,
					"occasionalVisitor", gson.toJson(occasionalVisitor));
			
			ClientUI.chat.accept(gson.toJson(requestToServer));
			analyzeMessegeFromServer();
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
		if (countVisitor == 16) {
			PopUp.display("Error", "The maximum visitors for an instructor is 16");
		} else {
			countVisitor++;
			numOfVisitorTxt.setText(String.valueOf(countVisitor));
		}
	}
	
	private void analyzeMessegeFromServer() {
		
		switch (ChatClient.serverMsg) {
		case "No available space at the park":
			PopUp.display("Error", ChatClient.serverMsg);
			break;
		case "faild to get park data":
			PopUp.display("Error", ChatClient.serverMsg);
			break;
		case "fail":
			PopUp.display("Error", ChatClient.serverMsg);
			break;			
		default:
			Reservation reservation = gson.fromJson(ChatClient.serverMsg, Reservation.class);
			PopUp.display("Succses", reservation.createReceipt() );
			break;
		}
		
	}
}