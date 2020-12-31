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

public class MainPageDepartmentManagerController {

	Gson gson = new Gson();

	@FXML
	private Pane departmentManagerPane;

    @FXML
    void logout(ActionEvent event) {
    	FXMLFunctions.logOutFromMainPage(departmentManagerPane.getScene());
    }

    @FXML
    void openApproveChange(ActionEvent event) throws IOException {
    	//Save the main pane in order to have access to it later.
    	StaticPaneMainPageDepartmentManager.DepartmentManagerMainPane = departmentManagerPane;
    	ApproveChangesController controller = FXMLFunctions.loadSceneToMainPane(ApproveChangesController.class, "ApproveChanges.fxml" , departmentManagerPane).getController();
    	controller.getChangesFromDataBase();
    }

    @FXML
    void openManagerReports(ActionEvent event) throws IOException {
    	StaticPaneMainPageDepartmentManager.DepartmentManagerMainPane = departmentManagerPane;
    	DepartmentManagerChooseReportController controller = FXMLFunctions.loadSceneToMainPane(DepartmentManagerChooseReportController.class, "DepartmentManagerChooseReport.fxml" , departmentManagerPane).getController();
    	controller.setComboBoxOptions();
    }

    @FXML
    void openNewReservation(ActionEvent event) throws IOException {
    	

    }
    
    @FXML
    void openParkManagerReports(ActionEvent event) throws IOException {
    	StaticPaneMainPageDepartmentManager.DepartmentManagerMainPane = departmentManagerPane;
    	DepartmentManagerChooseParkManReportController controller = FXMLFunctions.loadSceneToMainPane(DepartmentManagerChooseParkManReportController.class, "DepartmentManagerChooseParkManReport.fxml" , departmentManagerPane).getController();
    	controller.setComboBoxOptions();
    }

    
}
