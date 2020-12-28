package waitingList;

import guiCommon.StaticPaneMainPageClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import reservation.OptionalReservationsTuple;
import reservation.Reservation;
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

	public void loadReservations(Reservation[] myWaitinigList) {
		this.myWaitinigList = myWaitinigList;
		for (Reservation tupleInWaitingList : myWaitinigList) {
			WaitingListTuple tuple = new WaitingListTuple(tupleInWaitingList);
			reservationList.add(tuple);
		}

		parkNameCol.setCellValueFactory(new PropertyValueFactory<WaitingListTuple, String>("parkname"));
		timeCol.setCellValueFactory(new PropertyValueFactory<WaitingListTuple, String>("dateAndTimeString"));
		approveCol.setCellValueFactory(new PropertyValueFactory<WaitingListTuple, String>("approve"));
		cancellCol.setCellValueFactory(new PropertyValueFactory<WaitingListTuple, String>("cancel"));
		reservationTable.setItems(reservationList);

	}

	@FXML
	void back(MouseEvent event) {
		StaticPaneMainPageClient.clientMainPane.getChildren().clear();
	}

}
