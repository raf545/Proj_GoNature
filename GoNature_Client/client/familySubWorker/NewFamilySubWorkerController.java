package familySubWorker;

import com.google.gson.Gson;

import client.ChatClient;
import client.ClientUI;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;
import popup.PopUp;
import requestHandler.RequestHandler;
import requestHandler.controllerName;
import subscriber.Subscriber;

public class NewFamilySubWorkerController {
	Gson gson = new Gson();
	Subscriber subscriber;
	@FXML
	private TextField firstNameTF;

	@FXML
	private TextField LastNameTF;

	@FXML
	private TextField IdTF;

	@FXML
	private TextField PhoneTF;

	@FXML
	private TextField EmailTF;

	@FXML
	private TextField AmountTF;

	@FXML
	private TextField CreditCardTF;

	@FXML
	private Circle backBtn;

	@FXML
	private Button saveBtn;

	@FXML
	void saveFamily(ActionEvent event) {

		
		StringBuilder popError = new StringBuilder();
		String id, subscriberid, name, lastName, phone, email, numOfMembers, creditCardNumber, subscriberType;
		id = IdTF.getText();
		subscriberid = "1234";
		name = firstNameTF.getText();
		lastName = LastNameTF.getText();
		phone = PhoneTF.getText();
		email = EmailTF.getText();
		numOfMembers = AmountTF.getText();
		creditCardNumber = CreditCardTF.getText();
		subscriberType = "family";

		if (firstNameTF.getText().isEmpty()) {
			popError.append("Must enter first name");

			if (LastNameTF.getText().isEmpty())
				popError.append("Must enter last name");

			if (IdTF.getText().isEmpty())
				popError.append("Must enter ID");

			if (PhoneTF.getText().isEmpty())
				popError.append("Must enter Phone number");

			if (EmailTF.getText().isEmpty())
				popError.append("Must enter Email");

			if (AmountTF.getText().isEmpty())
				popError.append("Must enter Amount of pepole");

			if (popError.length() > 0)
				PopUp.display("Error", popError.toString());
		} else {

			 subscriber = new Subscriber(id, subscriberid, name, lastName, phone, email, numOfMembers,
					creditCardNumber, subscriberType);
			sendLoginRequestToServer("addFamilySub", subscriber);
			analyzeAnswerFromServer();
		}

	}

	private void sendLoginRequestToServer(String loginType, Subscriber subscriber) {
		RequestHandler rh = new RequestHandler(controllerName.EmployeeSystemController, loginType,
				gson.toJson(subscriber));
		ClientUI.chat.accept(gson.toJson(rh));
	}

	private void analyzeAnswerFromServer() {
		String answer = ChatClient.serverMsg;
		switch (answer) {
		case "subscriber already exist":
			PopUp.display("Error", answer);
			break;
		case "fail":
			PopUp.display("Error", answer);
		case "success":
			PopUp.display("sucess", "your subscriber id is:"+subscriber.getSubscriberid());
			break;
		}
	}

}
