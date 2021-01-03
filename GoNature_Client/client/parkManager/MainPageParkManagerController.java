package parkManager;

import java.io.IOException;

import com.google.gson.Gson;

import client.ClientUI;
import employee.BlankEmployeeController;
import employee.Employee;
import fxmlGeneralFunctions.FXMLFunctions;
import guiCommon.StaticPaneMainPageParkManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import login.SignInEmployeeController;
import parkMangerChanges.ParkMangerChangesController;
import parkmanagerreports.ParkManagerReportsController;
import parkmanagerreports.numberOfVisitorsReportController;
import requestHandler.RequestHandler;
import requestHandler.controllerName;

/**
 * the main page of an park manager,contains all the metods that he can do
 * 
 * @author zivi9
 *
 */
public class MainPageParkManagerController {

	Gson gson = new Gson();

	@FXML
	private Pane parkManagerMainPane;

	@FXML
	private Button parkReportBtn;

	@FXML
	private Button editParkBtn;

	@FXML
	private Hyperlink logoutBtn;

	@FXML
	private Label manePageParkName;
	
	  @FXML
	    private Text parknamew;

	Employee parkManager;

	/**
	 * set the information of this park manager
	 * 
	 * @param parkManagerEmp
	 * @throws IOException 
	 */
	public void setParkManagerEmployee(Employee parkManagerEmp) throws IOException {
		parkManager = parkManagerEmp;
		FXMLFunctions.loadSceneToMainPane(	BlankParkManagerController.class, "BlankParkManager.fxml", parkManagerMainPane);
		manePageParkName.setText("Hello " + parkManagerEmp.getName() + " " + parkManagerEmp.getLasstName());
		parknamew.setText(parkManager.getParkName()+" Park Manager" );
	}

	/**
	 * open the page of edit the park, with this page the park manager can edit
	 * parameters about this park
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void editPark(ActionEvent event) throws IOException {
		StaticPaneMainPageParkManager.parkManagerMainPane = parkManagerMainPane;

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ParkMangerChangesController.class.getResource("ParkManagerChanges.fxml"));

		Pane root = loader.load();
		parkManagerMainPane.getChildren().clear();
		parkManagerMainPane.getChildren().add(root);
		ParkMangerChangesController pmcController = loader.getController();
		pmcController.initializeSlidersAndSetParkManager(parkManager);
	}

	/**
	 * open the page that manager could generate reports
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void parkReport(ActionEvent event) throws IOException {
		StaticPaneMainPageParkManager.parkManagerMainPane = parkManagerMainPane;

		ParkManagerReportsController controller = FXMLFunctions
				.loadSceneToMainPane(ParkManagerReportsController.class, "ParkManagerReports.fxml", parkManagerMainPane)
				.getController();
		controller.setComboBoxOptions(parkManager);

	}

	/**
	 * log out and return to the login page
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void logout(ActionEvent event) throws IOException {
		FXMLFunctions.logOutFromMainPage(parkReportBtn.getScene());

	}

}
