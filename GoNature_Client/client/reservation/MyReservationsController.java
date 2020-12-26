package reservation;

import guiCommon.StaticPaneMainPageClient;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class MyReservationsController {

	@FXML
	private Text backBtn;

	@FXML
	private AnchorPane reservationPane;

	@FXML
	void back(MouseEvent event) {
		StaticPaneMainPageClient.clientMainPane.getChildren().clear();
	}

}
