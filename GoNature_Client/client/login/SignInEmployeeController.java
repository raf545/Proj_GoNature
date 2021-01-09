package login;

import java.io.IOException;

import com.google.gson.Gson;

import client.ChatClient;
import client.ClientUI;
import departmentManager.DepartmentManagerCapacityThread;
import departmentManager.MainPageDepartmentManagerController;
import employee.EmployeeCapacityThred;
import employee.Employee;
import employee.MainPageEmployeeController;
import fxmlGeneralFunctions.FXMLFunctions;
import guiCommon.StaticPaneMainPageEmployee;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import parkManager.MainPageParkManagerController;
import parkManager.ParkManagerCapacityThred;
import popup.PopUp;
import popup.PopUpWinController;
import requestHandler.RequestHandler;
import requestHandler.controllerName;

/**
 * This class inserts the user into the system as employee
 * 
 * @author Yaniv Sokolov
 *
 */
public class SignInEmployeeController {
	Gson gson = new Gson();
	@FXML
	private TextField IDTxt;

	@FXML
	private Button ContinueBtn;

	@FXML
	private PasswordField PasswordTxt;

	@FXML
	private Text BackBtn;

	/**
	 * back to the main page
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void Back(MouseEvent event) throws IOException {
		Stage primaryStage = new Stage();
		// get a handle to the stage
		Stage stage = (Stage) BackBtn.getScene().getWindow();
		// do what you have to do
		stage.close();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(GoNatureLoginController.class.getResource("GoNatureLogin.fxml"));
		Pane root = loader.load();
		Scene sc = new Scene(root);
		primaryStage.setTitle("Login");
		primaryStage.setScene(sc);
		primaryStage.show();

	}

	/**
	 * check if the id is valid or not and insert the user
	 * 
	 * @param event
	 */
	@FXML
	void Continue(ActionEvent event) {
		@SuppressWarnings("unused")
		String fxmlName;
		@SuppressWarnings("unused")
		String title;
		Employee employee = new Employee(IDTxt.getText(), PasswordTxt.getText());
		RequestHandler rh = new RequestHandler(controllerName.LoginController, "employeeLogIn", gson.toJson(employee));
		ClientUI.chat.accept(gson.toJson(rh));
		analyzeAnswerFromServer();
	}

	/**
	 * check the answer from the server
	 */
	private void analyzeAnswerFromServer() {
		@SuppressWarnings("unused")
		String fxmlName = null;
		@SuppressWarnings("unused")
		String title = null;

		switch (ChatClient.serverMsg) {
		case "employee not found":
			PopUp.display("Error", "employee not found");
			break;

		case "wrong password":
			PopUp.display("Error", "wrong password");
			break;
		case "already connected":
			PopUp.display("Error", "already connected");
			break;
		default:
			Employee employee = gson.fromJson(ChatClient.serverMsg, Employee.class);

			switch (employee.getTypeOfEmployee()) {
			case "department manager":
				openDepartmentManagerGui(employee);
				break;
			case "park manager":
				openParkManagerGui(employee);
				break;
			case "employee":
				openEmployeeGui(employee);
				break;

			default:
				break;
			}
		}
	}

	/**
	 * insert the user as employee
	 * 
	 * @param employee
	 */
	private void openEmployeeGui(Employee employee) {
		try {
			Stage primaryStage = new Stage();
			Stage stage = (Stage) ContinueBtn.getScene().getWindow();
			stage.close();

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainPageEmployeeController.class.getResource("MainPageEmployee.fxml"));
			Pane root = loader.load();
			Scene sc = new Scene(root);

			MainPageEmployeeController mainPageEmployeeController = loader.getController();
			Runnable run = new EmployeeCapacityThred(employee,mainPageEmployeeController);
			Thread t = new Thread(run);
			mainPageEmployeeController.setEmp(employee,t);
			t.start();
			StaticPaneMainPageEmployee.employeeMainPane = mainPageEmployeeController.getPane();
			primaryStage.setOnCloseRequest(e -> FXMLFunctions.closeMainPage());
			primaryStage.setTitle("Employee Main Page");
			primaryStage.setScene(sc);
			primaryStage.show();
		} catch (IOException e) {
			System.out.println("Load Faild");
		}
	}

	/**
	 * insert the user an park manager
	 * 
	 * @param parkManager
	 */
	private void openParkManagerGui(Employee parkManager) {
		try {
			Stage primaryStage = new Stage();
			Stage stage = (Stage) ContinueBtn.getScene().getWindow();
			stage.close();

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainPageParkManagerController.class.getResource("MainPageParkManager.fxml"));
			Pane root = loader.load();

			MainPageParkManagerController mainPageParkManagerController = loader.getController();
			Runnable run = new ParkManagerCapacityThred(parkManager,mainPageParkManagerController);
			Thread t = new Thread(run);
			mainPageParkManagerController.setParkManagerEmployee(parkManager,t);
			t.start();

			Scene sc = new Scene(root);
			primaryStage.setOnCloseRequest(e -> FXMLFunctions.closeMainPage());
			primaryStage.setTitle("Main Page Park Manager");
			primaryStage.setScene(sc);
			primaryStage.show();
		} catch (IOException e) {
			System.out.println("Load Faild");
		}
	}

	/**
	 * insert the user as department manager
	 * 
	 * @param employee
	 */
	private void openDepartmentManagerGui(Employee employee) {
		try {
			Stage primaryStage = new Stage();
			Stage stage = (Stage) ContinueBtn.getScene().getWindow();
			stage.close();

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainPageDepartmentManagerController.class.getResource("MainPageDepartmentManager.fxml"));
			Pane root = loader.load();

			Scene sc = new Scene(root);

			MainPageDepartmentManagerController mainPageDepartmentManagerController = loader.getController();
			Runnable run = new DepartmentManagerCapacityThread(employee,mainPageDepartmentManagerController);
			Thread t = new Thread(run);
			mainPageDepartmentManagerController.setDepDetails(employee,t);
			t.start();

			primaryStage.setOnCloseRequest(e -> FXMLFunctions.closeMainPage());
			primaryStage.setTitle("Main Page Department Manager");
			primaryStage.setScene(sc);
			primaryStage.show();
		} catch (IOException e) {
			System.out.println("Load Faild");
		}
	}

}