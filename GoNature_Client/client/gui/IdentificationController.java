package gui;

import java.io.IOException;

import com.google.gson.Gson;

import client.ChatClient;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.Reservation;
import logic.Subscriber;
import popup.PopUp;
import requestHandler.RequestHandler;
import requestHandler.controllerName;

public class IdentificationController {

	Gson gson = new Gson();

	// FIXME refactor the global variabels

	@FXML
	private TextField IDText;

	@FXML
	private ComboBox<String> OptionCombo;

	@FXML
	private Button ContinueBtn;

	@FXML
	private Text BackButton;

	public void setIdentificationComboBox() {
		OptionCombo.getItems().addAll("Guest ID", "Subscriber", "Reservation ID");
	}

	@FXML
	void identificationBackButton(MouseEvent event) throws IOException {
		Stage primaryStage = new Stage();
		Stage stage = (Stage) BackButton.getScene().getWindow();
		// FIXME primaryStage.setOnCloseRequest(value);

		stage.close();

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("GoNatureLogin.fxml"));

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
	}

	@FXML
	void identificationContinueButton(ActionEvent event) throws IOException {

		String selectedCombo = OptionCombo.getSelectionModel().getSelectedItem();
		StringBuilder popError = new StringBuilder();
		if (selectedCombo == null) {
			popError.append("Must choose Login Type\n");
		}

		if (IDText.getText().isEmpty()) {
			popError.append("Must enter id\n");
		}

		if (popError.length() > 0) {
			PopUp.display("Error", popError.toString());
		} else {
			sendLoginRequestToServer(selectedCombo);
			analyzeAnswerFromServer(selectedCombo);
		}
	}

	/**
	 * 
	 */
	private void analyzeAnswerFromServer(String selectedCombo) {
		String VisitorName = null;
		String VisitorType = null;
		switch (ChatClient.serverMsg) {

		case "all ready connected":
			PopUp.display("Error", "all ready connected");
			break;

		case "update faild":
			PopUp.display("Error", "update faild");
			break;

		case "not subscriber":
			PopUp.display("Error", "not subscriber");
			break;

		case "no reservation":
			PopUp.display("Error", "no reservation");
			break;

		default:
			if (selectedCombo == "Guest ID") {
				setClientInfoAndType(String.class,"Guest");
				VisitorName = "Guest";
				VisitorType = "Guest";
			} else if (selectedCombo == "Subscriber") {
				
				Subscriber savedSubscriberName = gson.fromJson(ChatClient.serverMsg, Subscriber.class);
				setClientInfoAndType(Subscriber.class,savedSubscriberName.getSubscriberType());
				VisitorName = savedSubscriberName.getName() +" "+ savedSubscriberName.getLastName();
				VisitorType = "Subscriber";
			} else if (selectedCombo == "Reservation ID") {
				setClientInfoAndType(Reservation.class,"Reservation");
				Reservation saveRsesrvationId = gson.fromJson(ChatClient.serverMsg, Reservation.class);
				VisitorName = saveRsesrvationId.getPersonalID() + "Reservasion id: " + saveRsesrvationId.getReservationID();
				VisitorType = "Reservation";
			}
			openPageUsingFxmlName(VisitorName,VisitorType);
			break;
		}

	}

	/**
	 * loads a fxml file with a given name and setes the window titel
	 * 
	 * @throws IOException
	 */
	private void openPageUsingFxmlName(String VisitorHelloString, String VisitorType) {
		try {
			Stage primaryStage = new Stage();
			Stage stage = (Stage) BackButton.getScene().getWindow();
			// FIXME primaryStage.setOnCloseRequest(value);
			stage.close();

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(this.getClass().getResource("MainPageForClient.fxml"));
			Pane root = loader.load();
			
			MainPageForClientController mainPageForClientController = loader.getController();
			mainPageForClientController.setTitels("hello " + VisitorHelloString , VisitorType);
			StaticPaneMainPageClient.clientMainPane = mainPageForClientController.getPane();
			
			Scene sc = new Scene(root);
			primaryStage.setTitle("Main page");
			primaryStage.setScene(sc);
			primaryStage.show();
		} catch (IOException e) {
			System.out.println("Load Faild");
		}
	}

	/**
	 * @param loginType The selected combobox Type for login
	 */
	private void sendLoginRequestToServer(String loginType) {
		RequestHandler rh = new RequestHandler(controllerName.LoginController, loginType, IDText.getText());
		ClientUI.chat.accept(gson.toJson(rh));
	}

	private void setClientInfoAndType(Class clientType, String clientTypeString) {
		ChatClient.clientInfo = ChatClient.serverMsg;
		ChatClient.clientType = clientType;
		ChatClient.clientTypeString = clientTypeString;
	}
}
