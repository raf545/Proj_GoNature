package reservation;

import com.google.gson.Gson;

import client.ChatClient;
import client.ClientUI;
import guiCommon.StaticPaneMainPageClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import popup.AlertBox;
import popup.PopUp;
import requestHandler.RequestHandler;
import requestHandler.controllerName;

public class MyReservationsController {

	Gson gson = new Gson();
	@FXML
	private Text backBtn;

	@FXML
	private Pane listPane;

	@FXML
	private TableView<MyReservationTuple> reservationTable;

	@FXML
	private TableColumn<MyReservationTuple, String> reservationCol;

	@FXML
	private TableColumn<MyReservationTuple, String> parkNameCol;

	@FXML
	private TableColumn<MyReservationTuple, String> timeCol;

	@FXML
	private TableColumn<MyReservationTuple, String> actionCol;

	@FXML
	private TableColumn<MyReservationTuple, String> actionCol1;

	ObservableList<MyReservationTuple> reservationList = FXCollections.observableArrayList();

	@FXML
	void back(MouseEvent event) {
		StaticPaneMainPageClient.clientMainPane.getChildren().clear();
	}

	public void loadReservations(Reservation[] myReservation) {

		for (Reservation reservation : myReservation) {
			MyReservationTuple tuple = new MyReservationTuple(reservation);
			tuple.getApprove().setOnAction(e -> approveReservatio(tuple.getReservationID()));
			tuple.getCancel().setOnAction(e -> cancelReservation(reservation, tuple));
			reservationList.add(tuple);
		}

		reservationCol.setCellValueFactory(new PropertyValueFactory<MyReservationTuple, String>("reservationID"));
		parkNameCol.setCellValueFactory(new PropertyValueFactory<MyReservationTuple, String>("parkname"));
		timeCol.setCellValueFactory(new PropertyValueFactory<MyReservationTuple, String>("dateAndTimeString"));
		actionCol.setCellValueFactory(new PropertyValueFactory<MyReservationTuple, String>("approve"));
		actionCol1.setCellValueFactory(new PropertyValueFactory<MyReservationTuple, String>("Cancel"));
		reservationTable.setItems(reservationList);
	}

	private void cancelReservation(Reservation reservation, MyReservationTuple tuple) {
		String alertTitel = "Reservation cancel";
		String alertHeader = "You are about to cancel a reservation";
		String alertBody = "Are you shure you want to cancel\nthe reservation?";

		if (AlertBox.display(alertTitel, alertHeader, alertBody)) {
			RequestHandler cencelRequest = new RequestHandler(controllerName.ReservationController, "cancelReservation",
					gson.toJson(reservation));
			gson.toJson(cencelRequest);
			ClientUI.chat.accept(gson.toJson(cencelRequest));
			analyzeAnswerFromServer();

			getTableView().getItems().remove(tuple);
			reservationTable.refresh();
		}
	}

	private TableView<MyReservationTuple> getTableView() {
		return reservationTable;
	}

	private void approveReservatio(String reservationId) {
		String alertTitel = "Reservation approve";
		String alertHeader = "You are about to approve a reservation";
		String alertBody = "Are you shure you want to approve\nthe reservation?";

		if (AlertBox.display(alertTitel, alertHeader, alertBody)) {
			RequestHandler cencelRequest = new RequestHandler(controllerName.ReservationController,
					"approveReservation", reservationId);
			gson.toJson(cencelRequest);
			ClientUI.chat.accept(gson.toJson(cencelRequest));
			analyzeAnswerFromServer();
		}
	}

	private void analyzeAnswerFromServer() {
		PopUp.display("Success", ChatClient.serverMsg);
	}

}
