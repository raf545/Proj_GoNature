package gui;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class MyWaitingListController {

	@FXML
	private Text backBtn;

	@FXML
	void back(MouseEvent event) {
		StaticPaneMainPageClient.clientMainPane.getChildren().clear();
	}

}
