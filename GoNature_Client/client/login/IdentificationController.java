package login;

import java.io.IOException;

import com.google.gson.Gson;

import client.ChatClient;
import client.ClientUI;
import fxmlGeneralFunctions.FxmlOpenGuiPage;
import fxmlGeneralFunctions.IFxmlOpenGuiPage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import popup.PopUp;
import requestHandler.RequestHandler;
import requestHandler.controllerName;
import subscriber.Subscriber;

/**
 * This class inserts the user into the system as client
 * 
 * @author Yaniv Sokolov
 *
 */
public class IdentificationController {

	Gson gson = new Gson();
	// FIXME refactor the global variabels

	@FXML
	private TextField IDText;

	@FXML
	private Button ContinueBtn;

	@FXML
	private Text BackButton;

	IFxmlOpenGuiPage fxmlOpenGuiPage = new FxmlOpenGuiPage();

	public void setFxmlOpenGui(IFxmlOpenGuiPage fxmlOpenGuiPage) {
		this.fxmlOpenGuiPage = fxmlOpenGuiPage;
	}

	public void setId(String id) {
		IDText.setText(id);
	}

	public String getID() {
		return IDText.getText();
	}

	/**
	 * This method is called every time the BackButton is pressed
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void identificationBackButton(MouseEvent event) throws IOException {
		Stage primaryStage = new Stage();
		Stage stage = (Stage) BackButton.getScene().getWindow();
		stage.close();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(GoNatureLoginController.class.getResource("GoNatureLogin.fxml"));
		Pane root = null;

		try {
			root = loader.load();
		} catch (IOException e) {
			System.out.println("Load Faild");
		}

		Scene sc = new Scene(root);
		primaryStage.setTitle("Login");
		primaryStage.setScene(sc);
		primaryStage.show();
		primaryStage.setResizable(false);
	}

	/**
	 * This method is calld
	 * 
	 * @param event a certian event realted to button, like right klick
	 * @throws IOException
	 */
	@FXML
	void identificationContinueButton(ActionEvent event) throws IOException {

		StringBuilder popError = new StringBuilder();

		if (getID().isEmpty()) {
			popError.append("Must enter id\n");
		} else if (!(getID().matches("[0-9]+"))) {
			popError.append("Must enter numbers\n");
		}

		if (popError.length() > 0) {
			PopUp.popUpForCheck.display("Error", popError.toString());
		} else {
			sendLoginRequestToServer();
			analyzeAnswerFromServer();
		}
	}

	/**
	 * 
	 * @param selectedCombo The option selected from the combobox
	 */
	private void analyzeAnswerFromServer() {
		String VisitorName = null;
		String VisitorType = null;
		switch (ChatClient.serverMsg) {

		case "all ready connected":
			PopUp.popUpForCheck.display("Error", "all ready connected");
			break;

		case "update faild":
			PopUp.popUpForCheck.display("Error", "update faild");
			break;

		default:
			Subscriber subscriberFromServer = gson.fromJson(ChatClient.serverMsg, Subscriber.class);
			if (subscriberFromServer.getSubscriberid() == null) {
				setClientInfoAndType(String.class, "Guest");
				VisitorName = subscriberFromServer.getId();
				VisitorType = "guest";
				ChatClient.clientIdString = subscriberFromServer.getId();
			} else {
				ChatClient.clientIdString = subscriberFromServer.getId();
				setClientInfoAndType(Subscriber.class, subscriberFromServer.getSubscriberType());
				VisitorName = subscriberFromServer.getName() + " " + subscriberFromServer.getLastName();
				VisitorType = subscriberFromServer.getSubscriberType();
			}
			fxmlOpenGuiPage.openPageUsingFxmlName(VisitorName, VisitorType, BackButton);
			break;
		}

	}

	/**
	 * @param loginType The selected combobox Type for login
	 */
	private void sendLoginRequestToServer() {
		RequestHandler rh = new RequestHandler(controllerName.LoginController, "clientLogin", IDText.getText());
		ClientUI.chat.accept(gson.toJson(rh));
	}

	/**
	 * @param clientType
	 * @param clientTypeString
	 */
	private void setClientInfoAndType(Class clientType, String clientTypeString) {
		ChatClient.clientInfo = ChatClient.serverMsg;
		ChatClient.clientType = clientType;
		ChatClient.clientTypeString = clientTypeString;
	}

}
