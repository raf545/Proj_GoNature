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
 * A page that will help us choose the specific report in department manager.
 * @author Shay Maryuma
 */
public class DepartmentManagerChooseReportController {

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
    	case "Visitors":
    		DepartmentManagerVisitReportController dmvrController;
    		dmvrController = FXMLFunctions.loadSceneToMainPane(DepartmentManagerVisitReportController.class, "DepartmentManagerVisitReport.fxml", StaticPaneMainPageDepartmentManager.DepartmentManagerMainPane).getController();
    		dmvrController.setTypeComboBoxOptions();
    		break;
    	case "Cancelations":
    		DepartmentManagerCancelationReportsController dmcrController;
    		dmcrController = FXMLFunctions.loadSceneToMainPane(DepartmentManagerCancelationReportsController.class, "DepartmentManagerCancelationReports.fxml", StaticPaneMainPageDepartmentManager.DepartmentManagerMainPane).getController();
    		dmcrController.setTypeComboBoxOptions();
    		break; 
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
		reportOptions.getItems().addAll("Visitors", "Cancelations");
		reportOptions.setValue("Visitors");
	}

}

