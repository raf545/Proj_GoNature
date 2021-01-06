package instructorSubWorker;

import java.io.IOException;

import com.google.gson.Gson;

import client.ChatClient;
import client.ClientUI;
import employee.BlankEmployeeController;
import fxmlGeneralFunctions.FXMLFunctions;
import guiCommon.StaticPaneMainPageEmployee;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import popup.PopUp;
import requestHandler.RequestHandler;
import requestHandler.controllerName;
import subscriber.Subscriber;

/**
 * creates new instructor in the system only by employee.
 * 
 * @author Avri Zaguri
 *
 */
public class NewInstructorSubWorkerController {
	Gson gson = new Gson();
	Subscriber subscriber;
	@FXML
	private TextField firstNameTF;

	@FXML
	private TextField lastNameTF;

	@FXML
	private TextField idTF;

	@FXML
	private TextField phoneTF;

	@FXML
	private TextField EmailTF;

	@FXML
	private TextField creditCardTF;

	@FXML
	private Text backBtn;

	@FXML
	private Button savebtn;

	/**
	 * When pressed checks that all of the text fields are correct and well written
	 * and ask to add subscription from the server.
	 * 
	 * @param event
	 */
	@FXML
	void SaveInstructor(ActionEvent event) {

		StringBuilder popError = new StringBuilder();
		String id, subscriberid, name, lastName, phone, email, creditCardNumber, subscriberType;
		id = idTF.getText();
		subscriberid = null;
		name = firstNameTF.getText();
		lastName = lastNameTF.getText();
		phone = phoneTF.getText();
		email = EmailTF.getText();
		creditCardNumber = creditCardTF.getText();
		subscriberType = "instructor";

		if (name.isEmpty())
			popError.append("-Must enter first name\n");

		if (!name.matches("[a-zA-Z]+"))
			popError.append("-No Numbers allowed in names\n");

		if (lastName.isEmpty())
			popError.append("-Must enter last name\n");

		else if (!lastName.matches("[a-zA-Z]+"))
			popError.append("-No Numbers allowed in last names\n");

		if (id.isEmpty())
			popError.append("-Must enter ID\n");

		else if (!(id.matches("[0-9]+") && id.length() > 2))
			popError.append("-Please enter only numbers on ID\n");

		if (phone.isEmpty())
			popError.append("-Must enter Phone number\n");
		else {
			if (phone.matches("[a-zA-z]+")) {
				popError.append("-Must enter only numbers for Phone number\n");
			} else {
				if (phone.length() != 10)
					popError.append("-Must enter 10 digit Phone number\n");
			}
		}

		if (email.isEmpty()) {
			popError.append("-Must enter Email\n");
		} else {
			if (!(email.matches("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$")))
				popError.append("-Not a valid Email\n");
		}

		if (popError.length() > 0)
			PopUp.display("Error", popError.toString());
		else {

			subscriber = new Subscriber(id, subscriberid, name, lastName, phone, email, null, creditCardNumber,
					subscriberType);
			sendLoginRequestToServer("addInstructorSub", subscriber);
			analyzeAnswerFromServer();
			firstNameTF.setText("");
			lastNameTF.setText("");
			idTF.setText("");
			phoneTF.setText("");
			EmailTF.setText("");
			creditCardTF.setText("");

		}
	}

	/**
	 * sends the data and the needed function to the server.
	 * 
	 * @param loginType  sends to server the function it needs to do.
	 * @param subscriber subscriber send the data to the server.
	 */
	private void sendLoginRequestToServer(String loginType, Subscriber subscriber) {
		RequestHandler rh = new RequestHandler(controllerName.EmployeeSystemController, loginType,
				gson.toJson(subscriber));
		ClientUI.chat.accept(gson.toJson(rh));
	}

	/**
	 * Analyse the answer we got back from the server for the function we sent. and
	 * open popups if succeeded failed or already exist.
	 * 
	 */
	private void analyzeAnswerFromServer() {
		String answer = ChatClient.serverMsg;
		switch (answer) {
		case "Instructor already exist":
			PopUp.display("Error", answer);
			break;
		case "fail":
			PopUp.display("Error", answer);
		default:
			subscriber = gson.fromJson(answer, Subscriber.class);
			PopUp.display("sucess", "your subscriber id is:" + subscriber.getSubscriberid());
			break;
		}
	}

	/**
	 * go back to the main pane
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void backBottun(MouseEvent event) throws IOException {
		StaticPaneMainPageEmployee.employeeMainPane.getChildren().clear();
		 FXMLFunctions.loadSceneToMainPane(BlankEmployeeController.class,"BlankEmployee.fxml", StaticPaneMainPageEmployee.employeeMainPane);
	}

}
