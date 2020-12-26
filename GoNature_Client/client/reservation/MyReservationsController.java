package reservation;

import java.util.ArrayList;
import java.util.List;
import guiCommon.StaticPaneMainPageClient;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class MyReservationsController {

	@FXML
	private Text backBtn;

	@FXML
	private ListView<String> reservationList;

	@FXML
	void back(MouseEvent event) {
		StaticPaneMainPageClient.clientMainPane.getChildren().clear();
	}

	public void loadReservations(ArrayList<Reservation> myReservation) {
		HBox hbox = new HBox();

		}
	

}
