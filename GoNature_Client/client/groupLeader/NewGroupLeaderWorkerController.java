package groupLeader;

import com.google.gson.Gson;

import client.ChatClient;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;
import popup.PopUp;
import requestHandler.RequestHandler;
import requestHandler.controllerName;
import subscriber.Subscriber;

public class NewGroupLeaderWorkerController {
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
    private Circle backBtn;

    @FXML
    private Button savebtn;

    @FXML
    void SaveInstructor(ActionEvent event) {

		StringBuilder popError = new StringBuilder();
		String id, subscriberid, name, lastName, phone, email, creditCardNumber, subscriberType;
		id = idTF.getText();
		subscriberid = "123489";
		name = firstNameTF.getText();
		lastName = lastNameTF.getText();
		phone = phoneTF.getText();
		email = EmailTF.getText();
		creditCardNumber = creditCardTF.getText();
		subscriberType = "instructor";

		if (firstNameTF.getText().isEmpty()) {
			popError.append("Must enter first name");

			if (lastNameTF.getText().isEmpty())
				popError.append("Must enter last name");

			if (idTF.getText().isEmpty())
				popError.append("Must enter ID");

			if (phoneTF.getText().isEmpty())
				popError.append("Must enter Phone number");

			if (EmailTF.getText().isEmpty())
				popError.append("Must enter Email");

		

			if (popError.length() > 0)
				PopUp.display("Error", popError.toString());
		} else {

			 subscriber = new Subscriber(id, subscriberid, name, lastName, phone, email, null,
					creditCardNumber, subscriberType);
			sendLoginRequestToServer("addInstructorSub", subscriber);
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
		case "Instructor already exist":
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
