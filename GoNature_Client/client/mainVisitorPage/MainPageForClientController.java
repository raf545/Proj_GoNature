package mainVisitorPage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import client.ChatClient;
import client.ClientUI;
import fxmlGeneralFunctions.FXMLFunctions;
import guiCommon.StaticPaneMainPageClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import requestHandler.RequestHandler;
import requestHandler.controllerName;
import reservation.MyReservationsController;
import reservation.NewReservationController;
import reservation.Reservation;
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
		Reservation[] myReservation;

		RequestHandler getReservationsRequest = new RequestHandler(controllerName.ReservationController,
				"getReservations", ChatClient.clientIdString);
		ClientUI.chat.accept(gson.toJson(getReservationsRequest));
		myReservation = gson.fromJson(ChatClient.serverMsg, Reservation[].class);
		for (Reservation reservation : myReservation)
			System.out.println(reservation);
		StaticPaneMainPageClient.clientMainPane.getChildren().clear();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MyReservationsController.class.getResource("MyReservations.fxml"));
		Pane root = loader.load();
		MyReservationsController myReservationsController = loader.getController();
		myReservationsController.loadReservations(myReservation);
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
		Reservation[] myWaitinigList;
		RequestHandler getReservationsRequest = new RequestHandler(controllerName.WaitingListController,
				"getWaitingList", ChatClient.clientIdString);
		ClientUI.chat.accept(gson.toJson(getReservationsRequest));
		myWaitinigList = gson.fromJson(ChatClient.serverMsg, Reservation[].class);

		StaticPaneMainPageClient.clientMainPane.getChildren().clear();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MyWaitingListController.class.getResource("MyWaitingList.fxml"));
		Pane root = loader.load();
		MyWaitingListController myWaitingListController = loader.getController();
		myWaitingListController.loadReservations(myWaitinigList);
		StaticPaneMainPageClient.clientMainPane.getChildren().add(root);

	}

	@FXML
	void logout(MouseEvent event) {
		FXMLFunctions.logOutFromMainPage(NewReservationBtn.getScene());
	}

}
