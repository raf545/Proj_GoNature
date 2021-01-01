package parkManager;

import java.io.IOException;

import com.google.gson.Gson;

import client.ClientUI;
import employee.Employee;
import fxmlGeneralFunctions.FXMLFunctions;
import guiCommon.StaticPaneMainPageParkManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import login.SignInEmployeeController;
import parkMangerChanges.ParkMangerChangesController;
import parkmanagerreports.ParkManagerReportsController;
import parkmanagerreports.numberOfVisitorsReportController;
import requestHandler.RequestHandler;
import requestHandler.controllerName;

public class MainPageParkManagerController {

	Gson gson = new Gson();

	@FXML
	private Pane parkManagerMainPane;

	@FXML
	private Button parkReportBtn;

	@FXML
	private Button editParkBtn;

	@FXML
	private Button newReservationBtn;

	@FXML
	private Hyperlink logoutBtn;

	Employee parkManager;

	public void setParkManagerEmployee(Employee parkManagerEmp) {
		parkManager = parkManagerEmp;
	}

	@FXML
	void editPark(ActionEvent event) throws IOException {
		StaticPaneMainPageParkManager.parkManagerMainPane = parkManagerMainPane;
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ParkMangerChangesController.class.getResource("ParkManagerChanges.fxml"));

		Pane root = loader.load();
		parkManagerMainPane.getChildren().clear();
		parkManagerMainPane.getChildren().add(root);
		ParkMangerChangesController pmcController = loader.getController();
		pmcController.initializeSlidersAndSetParkMan(parkManager);
	}

	@FXML
	void newResrvation(ActionEvent event) {

	}

	@FXML
	void parkReport(ActionEvent event) throws IOException {
		StaticPaneMainPageParkManager.parkManagerMainPane = parkManagerMainPane;
		
		ParkManagerReportsController controller=FXMLFunctions.loadSceneToMainPane(ParkManagerReportsController.class, "ParkManagerReports.fxml", parkManagerMainPane).getController();
		controller.setComboBoxOptions(parkManager);

	}

	@FXML
	void logout(ActionEvent event) throws IOException {
		RequestHandler rh = new RequestHandler(controllerName.LoginController, "logout", "");
		ClientUI.chat.accept(gson.toJson(rh));

		Stage primaryStage = new Stage();
		// get a handle to the stage
		Stage stage = (Stage) editParkBtn.getScene().getWindow();
		// do what you have to do
		stage.close();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(SignInEmployeeController.class.getResource("SignInEmployee.fxml"));
		Pane root = loader.load();
		Scene sc = new Scene(root);
		primaryStage.setTitle("Sign In Employee");
		primaryStage.setScene(sc);
		primaryStage.show();
	}

}
