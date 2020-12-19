package gui;

import java.io.IOException;

import client.ChatClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.Subscriber;

public class MainPageForClientController {

	@FXML
	private Pane PaneChange;

	@FXML
	private Button NewReservationBtn;

	@FXML
	private Button MyReservationBtn;

	@FXML
	private Button WaitingListBtn;

	@FXML
	private Text QuitBtn;

	@FXML
	private Text mainPageTypeOfClient;

	@FXML
	private Label mainPageWelcomLabel;

	void setTitels(String welcome, String typeOfClient) {
		mainPageTypeOfClient.setText(typeOfClient);
		mainPageWelcomLabel.setText(welcome);
	}

	Pane getPane() {
		return PaneChange;
	}

	@FXML
	void MyReservationBtn(ActionEvent event) {

	}

	@FXML
	void NewReservation(ActionEvent event) throws IOException {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("NewReservation.fxml"));
		Pane root = loader.load();
		StaticPaneMainPageClient.clientMainPane.getChildren().add(root);

	}

	@FXML
	void Quit(MouseEvent event) {
		// get a handle to the stage
		Stage stage = (Stage) QuitBtn.getScene().getWindow();
		// do what you have to do
		stage.close();
		System.exit(0);

	}

	@FXML
	void WaitingListBtn(ActionEvent event) {

	}

}
