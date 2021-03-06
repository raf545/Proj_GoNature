package reservation;

import java.io.IOException;
import java.sql.Timestamp;

import com.google.gson.Gson;

import client.ChatClient;
import client.ClientUI;
import fxmlGeneralFunctions.FXMLFunctions;
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
import mainVisitorPage.BlankVisitorController;
import popup.PopUp;
import requestHandler.RequestHandler;
import requestHandler.controllerName;

/**
 * show the options for the client if the time that the client choose is full
 * 
 * @author Yaniv Sokolov
 *
 */
public class OptionsForReservationsController {
	@FXML
	private Text backBtn;

	@FXML
	private Pane listPane;

	@FXML
	private TableView<OptionalReservationsTuple> reservationTable;

	@FXML
	private TableColumn<OptionalReservationsTuple, String> parkNameCol;

	@FXML
	private TableColumn<OptionalReservationsTuple, String> timeCol;

	@FXML
	private TableColumn<OptionalReservationsTuple, String> actionCol;

	ObservableList<OptionalReservationsTuple> reservationList = FXCollections.observableArrayList();

	Reservation[] optionalReservation;
	Reservation myReservation;
	Gson gson = new Gson();

	/**
	 * load all the options that available
	 * 
	 * @param optionalReservation list of free space time
	 * @param myReservation
	 */
	public void loadOptionalReservationsToTable(Reservation[] optionalReservation, Reservation myReservation) {
		this.myReservation = myReservation;
		this.optionalReservation = optionalReservation;
		for (Reservation reservation : optionalReservation) {
			OptionalReservationsTuple tuple = new OptionalReservationsTuple(reservation);
			tuple.getApprove().setOnAction(e -> approveReservatio(tuple.getDateAndTimeString()));
			reservationList.add(tuple);
		}

		parkNameCol.setCellValueFactory(new PropertyValueFactory<OptionalReservationsTuple, String>("parkname"));
		timeCol.setCellValueFactory(new PropertyValueFactory<OptionalReservationsTuple, String>("dateAndTimeString"));
		actionCol.setCellValueFactory(new PropertyValueFactory<OptionalReservationsTuple, String>("approve"));
		reservationTable.setItems(reservationList);

	}

	/**
	 * make a reservation for the new time that the client choose
	 * 
	 * @param newTime the time that the client choose
	 */
	private void approveReservatio(Timestamp newTime) {
		myReservation.setDate(newTime);
		RequestHandler rh = new RequestHandler(controllerName.ReservationController, "createNewReservation",
				gson.toJson(myReservation));
		ClientUI.chat.accept(gson.toJson(rh));
		String answerFromServer = ChatClient.serverMsg;
		switch (answerFromServer) {
		case "fail update reservation ID":
			PopUp.display("Error", answerFromServer);
			break;
		case "fail insert reservation to DB":
			PopUp.display("Error", answerFromServer);
			break;
		default:
			Reservation reservationFromServer = gson.fromJson(answerFromServer, Reservation.class);
			PopUp.display("Success", "Reservation was placed successfuly\n " + "your Reservation id is: "
					+ reservationFromServer.getReservationID() + "\nPrice:" + reservationFromServer.getPrice());
			StaticPaneMainPageClient.clientMainPane.getChildren().clear();
			try {
				FXMLFunctions.loadSceneToMainPane(BlankVisitorController.class, "BlankVisitor.fxml",
						StaticPaneMainPageClient.clientMainPane);
			} catch (IOException e) {
				
				e.printStackTrace();
			}

			break;
		}
	}

	/**
	 * back to the quistion if the client want to enter to the waiting list
	 * 
	 * @param event
	 */
	@FXML
	void back(MouseEvent event) {
		try {
			StaticPaneMainPageClient.clientMainPane.getChildren().clear();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(WaitingListQuestionController.class.getResource("WaitingListQuestion.fxml"));
			Pane root;
			root = loader.load();
			Scene sc = new Scene(root);
			WaitingListQuestionController waitingListQuestionController = loader.getController();
			waitingListQuestionController.setReservation(myReservation);
			StaticPaneMainPageClient.clientMainPane.getChildren().add(root);
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

}
