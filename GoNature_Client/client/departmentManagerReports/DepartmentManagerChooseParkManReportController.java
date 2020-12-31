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

public class DepartmentManagerChooseParkManReportController {

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
    	case "Total Visitors":
    		DepartmentManagerTotalVisitReportController dmtvrController;
    		dmtvrController = FXMLFunctions.loadSceneToMainPane(DepartmentManagerTotalVisitReportController.class, "DepartmentManagerTotalVisitorReport.fxml", StaticPaneMainPageDepartmentManager.DepartmentManagerMainPane).getController();
    		dmtvrController.setComboBoxDetails();
    		break;
    	case "Revenue":
    		DepartmentManagerMonthlyRevenueController controller;
    		controller = FXMLFunctions.loadSceneToMainPane(DepartmentManagerMonthlyRevenueController.class, "DepartmentManagerMonthlyRevenue.fxml", StaticPaneMainPageDepartmentManager.DepartmentManagerMainPane).getController();
    		controller.setComboBoxes();
    		break;
    	case "Capacity":
    		DepartmentManagerVisitorCapacityController controller2;
    		controller2 = FXMLFunctions.loadSceneToMainPane(DepartmentManagerVisitorCapacityController.class, "DepartmentManagerVisitorCapacity.fxml", StaticPaneMainPageDepartmentManager.DepartmentManagerMainPane).getController();
    		controller2.setComboBoxes();
    	default:
    		return;
    	} 
    }

    @FXML
    void quitScene(MouseEvent event) {
    	StaticPaneMainPageDepartmentManager.DepartmentManagerMainPane.getChildren().clear();
    }
	public void setComboBoxOptions() {
		reportOptions.getItems().addAll("Total Visitors", "Revenue","Capacity");
		reportOptions.setValue("Total Visitors");
	}
}
