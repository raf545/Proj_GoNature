package managerApproveChanges;

import java.io.IOException;

import guiCommon.StaticPaneMainPageDepartmentManager;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class ApproveChangesController {

	@FXML
	private Text exitBtn;

	@FXML
	private AnchorPane anchorPane;

	@FXML
	void quitScene(MouseEvent event) throws IOException {
		StaticPaneMainPageDepartmentManager.DepartmentManagerMainPane.getChildren().clear();
	
	}

}
