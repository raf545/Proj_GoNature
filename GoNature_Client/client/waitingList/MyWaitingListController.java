package waitingList;

import java.io.IOException;

import com.google.gson.Gson;

import client.ChatClient;
import client.ClientUI;
import guiCommon.StaticPaneMainPageClient;
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
import popup.PopUp;
import requestHandler.RequestHandler;
import requestHandler.controllerName;
import reservation.OptionalReservationsTuple;
import reservation.Reservation;
import reservation.WaitingListQuestionController;
import reservation.WaitingListTuple;

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

	private void cancelReservation(Reservation reservasion, WaitingListTuple tuple) {
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

	private TableView<WaitingListTuple> getTableView() {
		return reservationTable;
	}

	private void approveReservation(Reservation reservasion, WaitingListTuple tuple) {
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

	@FXML
	void back(MouseEvent event) {
		StaticPaneMainPageClient.clientMainPane.getChildren().clear();
	}

}
