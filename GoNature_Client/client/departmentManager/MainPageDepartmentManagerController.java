package departmentManager;
import java.io.IOException;
import com.google.gson.Gson;
import departmentManagerReports.DepartmentManagerChooseParkManReportController;
import departmentManagerReports.DepartmentManagerChooseReportController;
import fxmlGeneralFunctions.FXMLFunctions;
import guiCommon.StaticPaneMainPageDepartmentManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import managerApproveChanges.ApproveChangesController;


/**
 * This page will be the main page for Department Manager
 * All the options for reports and functions that the manager can make will be here.
 * @author Shay Maryuma
 *
 */
public class MainPageDepartmentManagerController {


	Gson gson = new Gson();

	@FXML
	private Pane departmentManagerPane;

    /**
     * Logout from account, go back to the main page.
     * @param event
     */
    @FXML
    void logout(ActionEvent event) {
    	FXMLFunctions.logOutFromMainPage(departmentManagerPane.getScene());
    }

    /**
     * Open the window "Approve Changes" for department manager.
     * The department manager can approve changes that made by park manager
     * @param event
     * @throws IOException
     */
    @FXML
    void openApproveChange(ActionEvent event) throws IOException {
    	//Save the main pane in order to have access to it later.
    	StaticPaneMainPageDepartmentManager.DepartmentManagerMainPane = departmentManagerPane;
    	ApproveChangesController controller = FXMLFunctions.loadSceneToMainPane(ApproveChangesController.class, "ApproveChanges.fxml" , departmentManagerPane).getController();
    	controller.getChangesFromDataBase();
    }

    /**
     * Open the window "Manager Reports" for department manager.
     * The department manager can view cancellation and visit reports.
     * @param event
     * @throws IOException
     */
    @FXML
    void openManagerReports(ActionEvent event) throws IOException {
    	StaticPaneMainPageDepartmentManager.DepartmentManagerMainPane = departmentManagerPane;
    	DepartmentManagerChooseReportController controller = FXMLFunctions.loadSceneToMainPane(DepartmentManagerChooseReportController.class, "DepartmentManagerChooseReport.fxml" , departmentManagerPane).getController();
    	controller.setComboBoxOptions();
    }

    
    /**
     * Open the window "Park Manager Reports" for department manager.
     * The reports here are different from department manager reports,
     * those reports belong to each park manager and will be shown here.
     * @param event
     * @throws IOException
     */
    @FXML
    void openParkManagerReports(ActionEvent event) throws IOException {
    	StaticPaneMainPageDepartmentManager.DepartmentManagerMainPane = departmentManagerPane;
    	DepartmentManagerChooseParkManReportController controller = FXMLFunctions.loadSceneToMainPane(DepartmentManagerChooseParkManReportController.class, "DepartmentManagerChooseParkManReport.fxml" , departmentManagerPane).getController();
    	controller.setComboBoxOptions();
    }

    
}
