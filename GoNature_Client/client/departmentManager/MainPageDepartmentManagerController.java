package departmentManager;

import java.io.IOException;
import com.google.gson.Gson;
import client.ChatClient;
import client.ClientUI;
import departmentManagerReports.DepartmentManagerChooseParkManReportController;
import departmentManagerReports.DepartmentManagerChooseReportController;
import employee.Employee;
import fxmlGeneralFunctions.FXMLFunctions;
import guiCommon.StaticPaneMainPageDepartmentManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import managerApproveChanges.ApproveChangesController;
import requestHandler.RequestHandler;
import requestHandler.controllerName;

/**
 * This page will be the main page for Department Manager All the options for
 * reports and functions that the manager can make will be here.
 * 
 * @author Shay Maryuma
 *
 */
public class MainPageDepartmentManagerController {

	Gson gson = new Gson();

	@FXML
	private Label manPageDepName;

	@FXML
	private Pane departmentManagerPane;

    @FXML
    private Label capacityText;
    
	/**
	 * set the department manager
	 * 
	 * @param employee details
	 */
	public void setDepDetails(Employee employee) {
		manPageDepName.setText("Hello " + employee.getName() + " " + employee.getLasstName());
	}

	/**
	 * Logout from account, go back to the main page.
	 * 
	 * @param event
	 */
	@FXML
	void logout(ActionEvent event) {
		FXMLFunctions.logOutFromMainPage(departmentManagerPane.getScene());
	}

	/**
	 * Open the window "Approve Changes" for department manager. The department
	 * manager can approve changes that made by park manager
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void openApproveChange(ActionEvent event) throws IOException {
		// Save the main pane in order to have access to it later.
		StaticPaneMainPageDepartmentManager.DepartmentManagerMainPane = departmentManagerPane;
		ApproveChangesController controller = FXMLFunctions
				.loadSceneToMainPane(ApproveChangesController.class, "ApproveChanges.fxml", departmentManagerPane)
				.getController();
		controller.getChangesFromDataBase();
	}

	/**
	 * Open the window "Manager Reports" for department manager. The department
	 * manager can view cancellation and visit reports.
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void openManagerReports(ActionEvent event) throws IOException {
		StaticPaneMainPageDepartmentManager.DepartmentManagerMainPane = departmentManagerPane;
		DepartmentManagerChooseReportController controller = FXMLFunctions
				.loadSceneToMainPane(DepartmentManagerChooseReportController.class,
						"DepartmentManagerChooseReport.fxml", departmentManagerPane)
				.getController();
		controller.setComboBoxOptions();
	}

	/**
	 * Open the window "Park Manager Reports" for department manager. The reports
	 * here are different from department manager reports, those reports belong to
	 * each park manager and will be shown here.
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void openParkManagerReports(ActionEvent event) throws IOException {
		StaticPaneMainPageDepartmentManager.DepartmentManagerMainPane = departmentManagerPane;
		DepartmentManagerChooseParkManReportController controller = FXMLFunctions
				.loadSceneToMainPane(DepartmentManagerChooseParkManReportController.class,
						"DepartmentManagerChooseParkManReport.fxml", departmentManagerPane)
				.getController();
		controller.setComboBoxOptions();
	}
		
	
	/**
	 * Get from the data base how many people in parks Banias, Safari and Niagara.
	 */
	public void getAmountOfPeopleTodayInPark()
	{
		RequestHandler rh = new RequestHandler(controllerName.DepartmentManagerSystemController, "getAmountOfPeopleTodayInPark", "");
		ClientUI.chat.accept(gson.toJson(rh));
		analyzeAnswerFromServer();
		
	}	
	/**
	 * Get the details back from the query that sent to the db and continue to setCapacityTextField() if success.
	 * In case of fail, continue to showEmptyDetails().
	 */
	private void analyzeAnswerFromServer() {
		String answer = ChatClient.serverMsg;		
		if(!answer.equals("faild"))
			setCapacityTextField(answer);
	}
	
	/**
	 * Show the manager how many people in the park today.
	 * @param answer the answer from that came from the database
	 */
	private void setCapacityTextField(String answer)
	{
		String[] parkCapacities = answer.split(" ");
		int totalCapacity = Integer.parseInt(parkCapacities[0]) +  Integer.parseInt(parkCapacities[1]) +  Integer.parseInt(parkCapacities[2]);
		capacityText.setText("Banias - " + parkCapacities[0] + ", Safari - " + parkCapacities[1] + ", Niagara - " + parkCapacities[2] + ", Total - " + totalCapacity);
		
	}

}
