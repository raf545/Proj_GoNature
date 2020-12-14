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
import popup.PopUp;
import requestHandler.RequestHandler;
import requestHandler.controllerName;

public class IdentificationController {

	Gson gson = new Gson();

	@FXML
	private TextField IDText;

	@FXML
	private ComboBox<String> OptionCombo;

	@FXML
	private Button ContinueBtn;

	@FXML
	private Text BackButton;

	public void setCombo() {

		OptionCombo.getItems().addAll("Guest ID", "Subscriber", "Family subscriber", "Instructor", "Reservation ID");

	}

	@FXML
	void Back(MouseEvent event) throws IOException {
		openPageUsingFxmlName("GoNatureLogin.fxml", "Login");
	}

	@FXML
	void Continue(ActionEvent event) throws IOException {

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

			if (selectedCombo.equals("Guest ID")) {
				RequestHandler rh = new RequestHandler(controllerName.LoginController, "GuestID", IDText.getText());
				ClientUI.chat.accept(gson.toJson(rh));
				analyzeAnswer();
			}
			if (selectedCombo.equals("Subscriber")) {
				RequestHandler rh = new RequestHandler(controllerName.LoginController, "Subscriber", IDText.getText());
				ClientUI.chat.accept(gson.toJson(rh));
				analyzeAnswer();
			}
			if (selectedCombo.equals("Family subscriber")) {
				RequestHandler rh = new RequestHandler(controllerName.LoginController, "Family subscriber", IDText.getText());
				ClientUI.chat.accept(gson.toJson(rh));
				analyzeAnswer();
			}
			if (selectedCombo.equals("Instructor")) {
				RequestHandler rh = new RequestHandler(controllerName.LoginController, "Instructor", IDText.getText());
				ClientUI.chat.accept(gson.toJson(rh));
				analyzeAnswer();
			}
			if (selectedCombo.equals("Reservation ID")) {
				RequestHandler rh = new RequestHandler(controllerName.LoginController, "Reservation ID", IDText.getText());
				ClientUI.chat.accept(gson.toJson(rh));
				analyzeAnswer();
			}
		}

	}

	private void analyzeAnswer() {
		try {
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
			case "connected succsesfuly":
				openPageUsingFxmlName("MainPageForClient.fxml", "Main page");
				break;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void openPageUsingFxmlName(String name, String titel) throws IOException {
		Stage primaryStage = new Stage();
		// get a handle to the stage
		Stage stage = (Stage) BackButton.getScene().getWindow();
		// do what you have to do
		stage.close();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource(name));
		Pane root = loader.load();
		Scene sc = new Scene(root);
		primaryStage.setTitle(titel);
		primaryStage.setScene(sc);
		primaryStage.show();
	}

}
