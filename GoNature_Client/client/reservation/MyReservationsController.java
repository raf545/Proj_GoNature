package reservation;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import guiCommon.StaticPaneMainPageClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class MyReservationsController {

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
			reservationList.add(tuple);						
		}
		
		reservationCol.setCellValueFactory(new PropertyValueFactory<MyReservationTuple, String>("reservationID"));
		parkNameCol.setCellValueFactory(new PropertyValueFactory<MyReservationTuple, String>("parkname"));
		timeCol.setCellValueFactory(new PropertyValueFactory<MyReservationTuple, String>("dateAndTimeString"));
		actionCol.setCellValueFactory(new PropertyValueFactory<MyReservationTuple, String>("approve"));
		actionCol1.setCellValueFactory(new PropertyValueFactory<MyReservationTuple, String>("Cancel"));
		reservationTable.setItems(reservationList);
	}

}
