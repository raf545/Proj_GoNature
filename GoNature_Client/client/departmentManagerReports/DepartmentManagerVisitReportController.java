package departmentManagerReports;

import java.io.IOException;

import fxmlGeneralFunctions.FXMLFunctions;
import guiCommon.StaticPaneMainPageDepartmentManager;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class DepartmentManagerVisitReportController {

    @FXML
    private Text backBtn;
    
	  @FXML
	    void quitScene(MouseEvent event) throws IOException {
		  //	StaticPaneMainPageDepartmentManager.DepartmentManagerMainPane.getChildren().clear();
		  DepartmentManagerChooseReportController controller = FXMLFunctions.loadSceneToMainPane(DepartmentManagerChooseReportController.class, "DepartmentManagerChooseReport.fxml", StaticPaneMainPageDepartmentManager.DepartmentManagerMainPane).getController();
		  controller.setComboBoxOptions();
	  }
}
