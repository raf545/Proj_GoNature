package reservation;

import java.io.IOException;

import com.google.gson.Gson;

import client.ChatClient;
import client.ClientUI;
import fxmlGeneralFunctions.FXMLFunctions;
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
import mainVisitorPage.BlankVisitorController;
import popup.AlertBox;
import popup.PopUp;
import requestHandler.RequestHandler;
import requestHandler.controllerName;

/**
 * show the list of the reservation
 * 
 * @author Yaniv Sokolov
 *
 */
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
	void back(MouseEvent event) throws IOException {
		StaticPaneMainPageClient.clientMainPane.getChildren().clear();
		FXMLFunctions.loadSceneToMainPane(BlankVisitorController.class, "BlankVisitor.fxml",
				StaticPaneMainPageClient.clientMainPane);
	}

	/**
	 * show the reservation of the client
	 * 
	 * @param myReservation get list of reservation
	 */
	public void loadReservations(Reservation[] myReservation) {

		for (Reservation reservation : myReservation) {
			MyReservationTuple tuple = new MyReservationTuple(reservation);
			tuple.getApprove().setVisible(true);
			tuple.getApprove().setVisible(false);
			if (reservation.getReservetionStatus().equals("sendApprovalMessage"))
				tuple.getApprove().setVisible(true);
			if (reservation.getReservetionStatus().equals("Aproved")) {
				tuple.getApprove().setVisible(true);
				tuple.getApprove().setDisable(true);
				tuple.getApprove().setText("Aproved");
			}
			tuple.getApprove().setOnAction(e -> approveReservatio(tuple));
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

	/**
	 * cancel the reservation
	 * 
	 * @param reservation
	 * @param tuple
	 */
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

	/**
	 * approve the reservation
	 * 
	 * @param tuple
	 */
	private void approveReservatio(MyReservationTuple tuple) {
		String alertTitel = "Reservation approve";
		String alertHeader = "You are about to approve a reservation";
		String alertBody = "Are you shure you want to approve\nthe reservation?";

		if (AlertBox.display(alertTitel, alertHeader, alertBody)) {
			RequestHandler cencelRequest = new RequestHandler(controllerName.ReservationController,
					"approveReservation", tuple.getReservationID());
			gson.toJson(cencelRequest);
			ClientUI.chat.accept(gson.toJson(cencelRequest));
			analyzeAnswerFromServer();
			tuple.getApprove().setDisable(true);
			reservationTable.refresh();
		}
	}

	/**
	 * Analyze the answer from the server
	 */
	private void analyzeAnswerFromServer() {
		PopUp.display("Success", ChatClient.serverMsg);

	}

	private TableView<MyReservationTuple> getTableView() {
		return reservationTable;
	}
}
