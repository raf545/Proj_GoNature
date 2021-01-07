package reservation;

import java.io.IOException;

import com.google.gson.Gson;

import client.ChatClient;
import client.ClientUI;
import fxmlGeneralFunctions.FXMLFunctions;
import guiCommon.StaticPaneMainPageClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import mainVisitorPage.BlankVisitorController;
import popup.PopUp;
import requestHandler.RequestHandler;
import requestHandler.controllerName;

/**
 * ask the client if he wants to enter waiting list
 * 
 * @author Yaniv Sokolov
 *
 */
public class WaitingListQuestionController {

	@FXML
	private Button joinToWaitingListBtn;

	@FXML
	private Button showOtherOptionsBtn;

	@FXML
	private Text backBtn;

	Reservation reservation;
	Gson gson = new Gson();

	public void setReservation(Reservation reservation) {
		this.reservation = reservation;
	}

	/**
	 * enter to the waiting list
	 * 
	 * @param event
	 */
	@FXML
	void joinToWaitingList(ActionEvent event) {
		reservation.setReservetionStatus("waitingList");
		RequestHandler rh = new RequestHandler(controllerName.WaitingListController, "enterToWaitingList",
				gson.toJson(reservation));
		ClientUI.chat.accept(gson.toJson(rh));
		switch (ChatClient.serverMsg) {
		case "You're already in the waiting list in this park and at this time":
			PopUp.display("ERROR", ChatClient.serverMsg);
			break;
		case "You entered to waiting list":
			PopUp.display("Success", ChatClient.serverMsg);
			StaticPaneMainPageClient.clientMainPane.getChildren().clear();
			try {
				FXMLFunctions.loadSceneToMainPane(BlankVisitorController.class, "BlankVisitor.fxml",
						StaticPaneMainPageClient.clientMainPane);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			break;
		case "fail":
			PopUp.display("ERROR", ChatClient.serverMsg);
			break;

		}
	}

	/**
	 * open new window with the new options to the same park
	 * 
	 * @param event
	 */
	@FXML
	void showOtherOptions(ActionEvent event) {
		try {
			Reservation[] myOptionalReservation;
			RequestHandler rh = new RequestHandler(controllerName.ReservationController, "showAvailableSpace",
					gson.toJson(reservation));
			ClientUI.chat.accept(gson.toJson(rh));
			myOptionalReservation = gson.fromJson(ChatClient.serverMsg, Reservation[].class);

			StaticPaneMainPageClient.clientMainPane.getChildren().clear();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(OptionsForReservationsController.class.getResource("OptionsForReservations.fxml"));
			Pane root = loader.load();
			OptionsForReservationsController optionsForReservationsController = loader.getController();
			optionsForReservationsController.loadOptionalReservationsToTable(myOptionalReservation, reservation);
			StaticPaneMainPageClient.clientMainPane.getChildren().add(root);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * go back to the reservation window
	 * 
	 * @param event
	 */
	@FXML
	void back(MouseEvent event) {
		try {
			StaticPaneMainPageClient.clientMainPane.getChildren().clear();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(NewReservationController.class.getResource("NewReservation.fxml"));
			Pane root;
			root = loader.load();
			NewReservationController newReservationController = loader.getController();
			newReservationController.setIdentFields();
			StaticPaneMainPageClient.clientMainPane.getChildren().add(root);
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

}
