package departmentManagerReports;

import java.io.IOException;

import departmentManager.BlankDepartmentManagerController;
import fxmlGeneralFunctions.FXMLFunctions;
import guiCommon.StaticPaneMainPageDepartmentManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text; 

/**
 * A page that will help us choose the specific park manager's report in department manager.
 * @author Shay Maryuma
 */
public class DepartmentManagerChooseParkManReportController {

    @FXML
    private Text backBtn;

    @FXML
    private Button continueBtn;

    @FXML
    private ComboBox<String> reportOptions;

    /**
     * When clicking on this button, the system will move us to the chosen window.
     * The windows are Total visitors, Revenue and Capacity.
     * @param event
     * @throws IOException
     */
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
	/**
	 * Exit the specific scene, back to main page.
	 * @param event
	 * @throws IOException
	 */
    @FXML
    void quitScene(MouseEvent event) throws IOException {
    	StaticPaneMainPageDepartmentManager.DepartmentManagerMainPane.getChildren().clear();
    	FXMLFunctions.loadSceneToMainPane(BlankDepartmentManagerController.class, "BlankDepartmentManager.fxml", StaticPaneMainPageDepartmentManager.DepartmentManagerMainPane);
    }
	/**
	 * A function that must be called when loading the screen
	 * set the combo box options and value.
	 */
	public void setComboBoxOptions() {
		reportOptions.getItems().addAll("Total Visitors", "Revenue","Capacity");
		reportOptions.setValue("Total Visitors");
	}
}
