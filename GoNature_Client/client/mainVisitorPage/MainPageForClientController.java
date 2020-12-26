package mainVisitorPage;

import java.io.IOException;

import com.google.gson.Gson;


import client.ClientUI;
import guiCommon.StaticPaneMainPageClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import login.IdentificationController;
import requestHandler.RequestHandler;
import requestHandler.controllerName;
import reservation.MyReservationsController;
import reservation.NewReservationController;
import waitingList.MyWaitingListController;

public class MainPageForClientController {

	Gson gson = new Gson();

	@FXML
	private Pane PaneChange;

	@FXML
	private Text mainPageTypeOfClient;

	@FXML
	private Label mainPageWelcomLabel;

	@FXML
	private Hyperlink logoutLink;

	@FXML
	private Button NewReservationBtn;

	@FXML
	private Button MyReservationBtn1;

	@FXML
	private Button WaitingListBtn;

	public void setTitels(String welcome, String typeOfClient) {
		mainPageTypeOfClient.setText(typeOfClient);
		mainPageWelcomLabel.setText(welcome);
	}

	public Pane getPane() {
		return PaneChange;
	}

	@FXML
	void MyReservationBtn(ActionEvent event) throws IOException {
		StaticPaneMainPageClient.clientMainPane.getChildren().clear();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MyReservationsController.class.getResource("MyReservations.fxml"));
		Pane root = loader.load();
		StaticPaneMainPageClient.clientMainPane.getChildren().add(root);

	}

	@FXML
	void NewReservation(ActionEvent event) throws IOException {
		StaticPaneMainPageClient.clientMainPane.getChildren().clear();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(NewReservationController.class.getResource("NewReservation.fxml"));
		Pane root = loader.load();
		NewReservationController newReservationController = loader.getController();
		newReservationController.setIdentFields();
		StaticPaneMainPageClient.clientMainPane.getChildren().add(root);

	}

	@FXML
	void WaitingListBtn(ActionEvent event) throws IOException {
		StaticPaneMainPageClient.clientMainPane.getChildren().clear();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MyWaitingListController.class.getResource("MyWaitingList.fxml"));
		Pane root = loader.load();
		StaticPaneMainPageClient.clientMainPane.getChildren().add(root);

	}

	@FXML
	void logout(MouseEvent event) throws IOException {

		RequestHandler rh = new RequestHandler(controllerName.LoginController, "logout", "");
		ClientUI.chat.accept(gson.toJson(rh));

		Stage primaryStage = new Stage();
		// get a handle to the stage
		Stage stage = (Stage) NewReservationBtn.getScene().getWindow();
		// do what you have to do
		stage.close();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(IdentificationController.class.getResource("Identification.fxml"));
		Pane root = loader.load();
		Scene sc = new Scene(root);
		primaryStage.setTitle("Identification");
		IdentificationController identificationController = loader.getController();
		identificationController.setIdentificationComboBox();
		primaryStage.setScene(sc);
		primaryStage.show();

	}

}
