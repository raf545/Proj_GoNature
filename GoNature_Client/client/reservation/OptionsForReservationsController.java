package reservation;

import guiCommon.StaticPaneMainPageClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

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

	public void loadOptionalReservationsToTable(Reservation[] optionalReservation) {
		this.optionalReservation = optionalReservation;
		for (Reservation reservation : optionalReservation) {
			System.out.println(reservation);
			OptionalReservationsTuple tuple = new OptionalReservationsTuple(reservation);
			reservationList.add(tuple);
		}

		parkNameCol.setCellValueFactory(new PropertyValueFactory<OptionalReservationsTuple, String>("parkname"));
		timeCol.setCellValueFactory(new PropertyValueFactory<OptionalReservationsTuple, String>("dateAndTimeString"));
		actionCol.setCellValueFactory(new PropertyValueFactory<OptionalReservationsTuple, String>("approve"));
		reservationTable.setItems(reservationList);
		
		
	}

	@FXML
	void back(MouseEvent event) {
		StaticPaneMainPageClient.clientMainPane.getChildren().clear();
	}

}
