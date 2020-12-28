package departmentManagerReports;

import java.io.IOException;

import fxmlGeneralFunctions.FXMLFunctions;
import guiCommon.StaticPaneMainPageDepartmentManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

//Pay Attention: this fxml's name changed from DepartmentManagerCancelationVisitorsReport to this new name.
public class DepartmentManagerChooseReportController {

    @FXML
    private Text backBtn;

    @FXML
    private Button continueBtn;

    @FXML
    private ComboBox<String> reportOptions;
    
    
    @FXML
    void continueToSpecificReport(ActionEvent event) throws IOException {
    	String selectedCombo = reportOptions.getSelectionModel().getSelectedItem();
    	if(selectedCombo == null)
    		return;
    	switch(selectedCombo)
    	{
    	case "Visitors":
    		DepartmentManagerVisitReportController dmvrController;
    		dmvrController = FXMLFunctions.loadSceneToMainPane(DepartmentManagerVisitReportController.class, "DepartmentManagerVisitReport.fxml", StaticPaneMainPageDepartmentManager.DepartmentManagerMainPane).getController();
    		dmvrController.setTypeComboBoxOptions();
    		break;
    	case "Cancelations":
    		FXMLFunctions.loadSceneToMainPane(DepartmentManagerCancelationReportsController.class, "DepartmentManagerCancelationReports.fxml", StaticPaneMainPageDepartmentManager.DepartmentManagerMainPane);
    		break; 
    	}
    }

    @FXML
    void quitScene(MouseEvent event) throws IOException {
    	StaticPaneMainPageDepartmentManager.DepartmentManagerMainPane.getChildren().clear();
    }
    
	public void setComboBoxOptions() {
		reportOptions.getItems().addAll("Visitors", "Cancelations");
	}

}

