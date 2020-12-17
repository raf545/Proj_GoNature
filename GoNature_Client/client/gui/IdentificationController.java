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
		String guestId;
		Reservation reservation;
		Subscriber subscriber;
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
				guestId = ChatClient.serverMsg;
				System.out.println(guestId);
			} else if (selectedCombo == "Subscriber") {
				subscriber = gson.fromJson(ChatClient.serverMsg, Subscriber.class);
				System.out.println(subscriber);
			} else if (selectedCombo == "Reservation ID") {
				reservation = gson.fromJson(ChatClient.serverMsg, Reservation.class);
				System.out.println(reservation);
			}
			openPageUsingFxmlName("MainPageForClient.fxml", "Main page");
			break;
		}

	}

	/**
	 * loads a fxml file with a given name and setes the window titel
	 * 
	 * @param name  FXML file name
	 * @param titel Window titel (Javafx)
	 * @throws IOException
	 */
	private void openPageUsingFxmlName(String name, String titel) {
		Stage primaryStage = new Stage();
		Stage stage = (Stage) BackButton.getScene().getWindow();
		// FIXME primaryStage.setOnCloseRequest(value);

		stage.close();

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource(name));

		Pane root = null;
		try {
			root = loader.load();
		} catch (IOException e) {
			System.out.println("Load Faild");
		}

		Scene sc = new Scene(root);
		primaryStage.setTitle(titel);
		primaryStage.setScene(sc);
		primaryStage.show();
	}

	/**
	 * @param loginType The selected combobox Type for login
	 */
	private void sendLoginRequestToServer(String loginType) {
		RequestHandler rh = new RequestHandler(controllerName.LoginController, loginType, IDText.getText());
		ClientUI.chat.accept(gson.toJson(rh));
	}

}
