package departmentManager;

import java.io.IOException;

import com.google.gson.Gson;

import departmentManagerReports.DepartmentManagerChooseReportController;
import fxmlGeneralFunctions.FXMLFunctions;
import guiCommon.StaticPaneMainPageDepartmentManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import managerApproveChanges.ApproveChangesController;

public class MainPageDepartmentManagerController {

	Gson gson = new Gson();

	@FXML
	private Pane departmentManagerPane;

	@FXML
	private Button managerReportBtn;

	@FXML
	private Button approveChangesBtn;

	@FXML
	private Button newReservationBtn;

	@FXML
	private Text quitBtn;

	@FXML
    private Text logoutBtn;


    @FXML
    void logout(MouseEvent event) throws IOException {
    	FXMLFunctions.logOutFromMainPage(approveChangesBtn.getScene());
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
    void openNewReservation(ActionEvent event) {

    }
}
