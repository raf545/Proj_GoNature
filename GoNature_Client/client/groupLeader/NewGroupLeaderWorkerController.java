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
			}else {
				if (phone.length()!=10)
					popError.append("-Must enter 10 digit Phone number\n");
			}
		}
	
		if (email.isEmpty())
		{
			popError.append("-Must enter Email\n");
		}else {
			if (!(email.matches("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$")))
				popError.append("-Not a valid Email\n");
		}
			

			if (popError.length() > 0)
				PopUp.display("Error", popError.toString());
				else {

					subscriber = new Subscriber(id, subscriberid, name, lastName, phone, email, null,
					creditCardNumber, subscriberType);
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
		default:
			subscriber = gson.fromJson(answer, Subscriber.class);
			PopUp.display("sucess", "your subscriber id is:" + subscriber.getSubscriberid());
			break;
		}
	}

}
