package waitingList;

import java.io.IOException;

import com.google.gson.Gson;

import client.ChatClient;
import client.ClientUI;
import employee.BlankEmployeeController;
import fxmlGeneralFunctions.FXMLFunctions;
import guiCommon.StaticPaneMainPageClient;
import guiCommon.StaticPaneMainPageEmployee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import mainVisitorPage.BlankVisitorController;
import popup.AlertBox;
import popup.PopUp;
import requestHandler.RequestHandler;
import requestHandler.controllerName;
import reservation.OptionalReservationsTuple;
import reservation.Reservation;
import reservation.WaitingListQuestionController;
import reservation.WaitingListTuple;

/**
 * show to the client his waiting list and give him option to cancel or approve
 * 
 * @author Yaniv Sokolov
 *
 */
public class MyWaitingListController {

	@FXML
	private Text backBtn;

	@FXML
	private TableView<WaitingListTuple> reservationTable;

	@FXML
	private TableColumn<WaitingListTuple, String> parkNameCol;

	@FXML
	private TableColumn<WaitingListTuple, String> timeCol;

	@FXML
	private TableColumn<WaitingListTuple, String> approveCol;

	@FXML
	private TableColumn<WaitingListTuple, String> cancellCol;

	ObservableList<WaitingListTuple> reservationList = FXCollections.observableArrayList();

	Reservation[] myWaitinigList;
	Gson gson = new Gson();

	/**
	 * show to the client the waiting list
	 * 
	 * @param myWaitinigList list of waiting list
	 */
	public void loadReservations(Reservation[] myWaitinigList) {
		this.myWaitinigList = myWaitinigList;
		for (Reservation tupleInWaitingList : myWaitinigList) {
			WaitingListTuple tuple = new WaitingListTuple(tupleInWaitingList);
			tuple.getApprove().setOnAction(e -> approveReservation(tupleInWaitingList, tuple));
			if (tupleInWaitingList.getReservetionStatus().equals("waitingList"))
				tuple.getApprove().setVisible(false);
			tuple.getCancel().setOnAction(e -> cancelReservation(tupleInWaitingList, tuple));
			reservationList.add(tuple);
		}

		parkNameCol.setCellValueFactory(new PropertyValueFactory<WaitingListTuple, String>("parkname"));
		timeCol.setCellValueFactory(new PropertyValueFactory<WaitingListTuple, String>("dateAndTimeString"));
		approveCol.setCellValueFactory(new PropertyValueFactory<WaitingListTuple, String>("approve"));
		cancellCol.setCellValueFactory(new PropertyValueFactory<WaitingListTuple, String>("cancel"));
		reservationTable.setItems(reservationList);

	}

	/**
	 * cancel the reservation from waiting list table
	 * 
	 * @param reservasion
	 * @param tuple
	 */
	private void cancelReservation(Reservation reservasion, WaitingListTuple tuple) {
		String alertTitel = "Wating list cancel";
		String alertHeader = "You are about to remove a reservation\n from waiting list";
		String alertBody = "Are you shure you want to remove\nthe waiting list request?";
		if (AlertBox.display(alertTitel, alertHeader, alertBody)) {
			RequestHandler rh = new RequestHandler(controllerName.WaitingListController, "removeFromWaitingList",
					gson.toJson(reservasion));
			ClientUI.chat.accept(gson.toJson(rh));
			String answer = ChatClient.serverMsg;
			switch (answer) {
			case "fail":
				PopUp.display("ERROR", answer);
				break;
			default:
				PopUp.display("SUCCESS", answer);
				getTableView().getItems().remove(tuple);
				reservationTable.refresh();
				break;
			}
		}
	}

	private TableView<WaitingListTuple> getTableView() {
		return reservationTable;
	}

	/**
	 * approve the reservation from waiting list
	 * 
	 * @param reservasion
	 * @param tuple
	 */
	private void approveReservation(Reservation reservasion, WaitingListTuple tuple) {
		String alertTitel = "Wating list approve";
		String alertHeader = "You are about to approve a reservation\n from waiting list";
		String alertBody = "Are you shure you want to approve\nthe waiting list request?";
		if (AlertBox.display(alertTitel, alertHeader, alertBody)) {
			RequestHandler rh = new RequestHandler(controllerName.ReservationController,
					"addToReservationTableFromWaitingList", gson.toJson(reservasion));
			ClientUI.chat.accept(gson.toJson(rh));
			Reservation reservationFromServer;
			String answer = ChatClient.serverMsg;
			switch (answer) {
			case "fail update reservation ID":
				PopUp.display("Error", answer);
				break;
			case "fail insert reservation to DB":
				PopUp.display("Error", answer);
				break;
			default:
				reservationFromServer = gson.fromJson(answer, Reservation.class);
				PopUp.display("Success", "Reservation was placed successfuly\n " + "your Reservation id is: "
						+ reservationFromServer.getReservationID() + "\nPrice:" + reservationFromServer.getPrice());
				getTableView().getItems().remove(tuple);
				reservationTable.refresh();
				break;
			}
		}

	}

	/**
	 * go back to the main page
	 * 
	 * @param event
	 */
	@FXML
	void back(MouseEvent event) {
		StaticPaneMainPageClient.clientMainPane.getChildren().clear();
		try {
			FXMLFunctions.loadSceneToMainPane(BlankVisitorController.class, "BlankVisitor.fxml",
					StaticPaneMainPageClient.clientMainPane);
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

}
