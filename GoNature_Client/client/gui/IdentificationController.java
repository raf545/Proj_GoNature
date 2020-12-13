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
		Stage primaryStage = new Stage();
		// get a handle to the stage
		Stage stage = (Stage) BackButton.getScene().getWindow();
		// do what you have to do
		stage.close();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("GoNatureLogin.fxml"));
		Pane root = loader.load();
		Scene sc = new Scene(root);
		primaryStage.setTitle("Login");
		primaryStage.setScene(sc);
		primaryStage.show();

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
		}

		if (selectedCombo.equals("Guest ID")) {
			RequestHandler rh = new RequestHandler(controllerName.LoginController,"GuestID",IDText.getText());
			ClientUI.chat.accept(gson.toJson(rh));
			switch(ChatClient.serverMsg){
			case "all ready connected":
				PopUp.display("Error", "all ready connected");
				break;
			case "update faild":
				PopUp.display("Error", "update faild");
				break;
			case "connected succsesfuly":
				Stage primaryStage = new Stage();
				// get a handle to the stage
				Stage stage = (Stage) BackButton.getScene().getWindow();
				// do what you have to do
				stage.close();
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(this.getClass().getResource("MainPageForClient.fxml"));
				Pane root = loader.load();
				Scene sc = new Scene(root);
				primaryStage.setTitle("MainPage");
				primaryStage.setScene(sc);
				primaryStage.show();
				break;
			
			}
		}
		if (selectedCombo.equals("Subscriber")) {

		}
		if (selectedCombo.equals("Family subscriber")) {

		}
		if (selectedCombo.equals("Instructor")) {

		}
		if (selectedCombo.equals("Reservation ID")) {

		}


	}

	// OptionCombo.getSelectionModel().getSelectedItem();
	// value of combo box
}
