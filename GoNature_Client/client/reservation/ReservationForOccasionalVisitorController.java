package reservation;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import com.google.gson.Gson;
import client.ChatClient;
import client.ClientUI;
import employee.BlankEmployeeController;
import employee.Employee;
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
import popup.AlertBox;
import popup.PopUp;
import requestHandler.RequestHandler;
import requestHandler.controllerName;
import subscriber.Subscriber;

/**
 * reservation for occasional visitor
 * 
 * @author Refael Alkoby
 *
 */
public class ReservationForOccasionalVisitorController {

	@FXML
	private Button ContinueBtn;

	@FXML
	private TextField parkNameTxt;

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

	/**
	 * set the window
	 * 
	 * @param employee to set park name
	 */
	public void setIdentFields(Employee employee) {
		parkNameTxt.setText(employee.getParkName());
		countVisitor = 1;
		numOfVisitorTxt.setText(String.valueOf(countVisitor));

	}

	/**
	 * back to the employee window
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void Back(MouseEvent event) {
		StaticPaneMainPageEmployee.employeeMainPane.getChildren().clear();
		try {
			BlankEmployeeController controller = FXMLFunctions.loadSceneToMainPane(BlankEmployeeController.class,
					"BlankEmployee.fxml", StaticPaneMainPageEmployee.employeeMainPane).getController();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	/**
	 * check if the details are valid
	 * 
	 * @param event
	 */
	@FXML
	void Continue(ActionEvent event) {
		String parkName = parkNameTxt.getText();
		StringBuilder errorMessage = new StringBuilder();
		if (EmailTxt.getText().isEmpty()) {
			errorMessage.append("No Email enterd\n");
		} else if (!(EmailTxt.getText().matches("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$"))) {
			errorMessage.append("-Not a valid Email\n");
		}
		if (PhoneTxt.getText().isEmpty()) {
			errorMessage.append("No Phone number enterd\n");
		} else if (!(PhoneTxt.getText().matches("[0-9]+"))) {
			errorMessage.append("-Must enter only numbers for Phone number\n");
		} else if (PhoneTxt.getText().length() != 10) {
			errorMessage.append("Must enter 10 digit Phone number\n");
		}
		if (IdTxt.getText().isEmpty()) {
			errorMessage.append("No id number enterd\n");
		} else if (!(IdTxt.getText().matches("[0-9]+"))) {
			errorMessage.append("Must enter numbers\n");
		}

		if (errorMessage.length() == 0) {
			Reservation occasionalVisitor = new Reservation();
			occasionalVisitor.setReservationID(null);
			occasionalVisitor.setPersonalID(IdTxt.getText());
			occasionalVisitor.setParkname(parkName);
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
			analyzeMessegeFromServer(occasionalVisitor);
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

	/**
	 * check the answer from the server
	 */
	private void analyzeMessegeFromServer(Reservation reservation) {

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
		case "Instructor cant make a reservaion for more then 15 pepole":
			PopUp.display("Error", ChatClient.serverMsg);
			break;
		case "reservation exists":
			String titel = "Reservation exists";
			String header = "Add to exsisting reservation";
			String Content = "You already have a reservation do you\n want to add to the exsisting reservation ?";
			if (AlertBox.display(titel, header, Content)) {
				RequestHandler addToreservation = new RequestHandler(controllerName.ReservationController,
						"addOccasionalToExsistingReservation", gson.toJson(reservation));
				ClientUI.chat.accept(gson.toJson(addToreservation));
				PopUp.display("", ChatClient.serverMsg);
			}
			break;
		default:
			Reservation reservation1 = gson.fromJson(ChatClient.serverMsg, Reservation.class);
			PopUp.display("Succses", reservation1.createReceipt());
			StaticPaneMainPageEmployee.employeeMainPane.getChildren().clear();
			try {
				FXMLFunctions.loadSceneToMainPane(BlankEmployeeController.class, "BlankEmployee.fxml",
						StaticPaneMainPageEmployee.employeeMainPane).getController();
			} catch (IOException e) {

				e.printStackTrace();
			}
			break;
		}

	}
}