package reservation;

import java.io.IOException;

import com.google.gson.Gson;

import client.ChatClient;
import client.ClientUI;
import guiCommon.StaticPaneMainPageClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import requestHandler.RequestHandler;
import requestHandler.controllerName;

public class WaitingListQuestionController {

	@FXML
	private Button joinToWaitingListBtn;

	@FXML
	private Button showOtherOptionsBtn;

	Reservation reservation;
	Gson gson = new Gson();

	public void setReservation(Reservation reservation) {
		this.reservation = reservation;
	}

	@FXML
	void joinToWaitingList(ActionEvent event) {

	}

	@FXML
	void showOtherOptions(ActionEvent event) {
		try {
			Reservation[] myReservation;
			RequestHandler rh = new RequestHandler(controllerName.ReservationController, "showAvailableSpace",
					gson.toJson(reservation));
			ClientUI.chat.accept(gson.toJson(rh));
			myReservation = gson.fromJson(ChatClient.serverMsg, Reservation[].class);

			StaticPaneMainPageClient.clientMainPane.getChildren().clear();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(OptionsForReservationsController.class.getResource("OptionsForReservations.fxml"));
			Pane root = loader.load();
			OptionsForReservationsController optionsForReservationsController = loader.getController();
			optionsForReservationsController.loadOptionalReservationsToTable(myReservation);
			StaticPaneMainPageClient.clientMainPane.getChildren().add(root);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
