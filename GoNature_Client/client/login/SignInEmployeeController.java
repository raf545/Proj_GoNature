package login;

import java.io.IOException;

import com.google.gson.Gson;

import client.ChatClient;
import client.ClientUI;
import departmentManager.MainPageDepartmentManagerController;
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
import popup.PopUp;
import popup.PopUpWinController;
import requestHandler.RequestHandler;
import requestHandler.controllerName;

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

//MainPageParkManager
	// "MainPageEmployee.fxml"
	//
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
			mainPageEmployeeController.setEmp(employee);

			StaticPaneMainPageEmployee.employeeMainPane = mainPageEmployeeController.getPane();
			primaryStage.setOnCloseRequest(e -> FXMLFunctions.closeMainPage());
			primaryStage.setTitle("Employee Main Page");
			primaryStage.setScene(sc);
			primaryStage.show();
		} catch (IOException e) {
			System.out.println("Load Faild");
		}
	}

	private void openParkManagerGui(Employee parkManager) {
		try {
			Stage primaryStage = new Stage();
			Stage stage = (Stage) ContinueBtn.getScene().getWindow();
			stage.close();

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainPageParkManagerController.class.getResource("MainPageParkManager.fxml"));
			Pane root = loader.load();

			MainPageParkManagerController mainPageParkManagerController = loader.getController();
			mainPageParkManagerController.setParkManagerEmployee(parkManager);

			Scene sc = new Scene(root);
			primaryStage.setOnCloseRequest(e -> FXMLFunctions.closeMainPage());
			primaryStage.setTitle("Main Page Park Manager");
			primaryStage.setScene(sc);
			primaryStage.show();
		} catch (IOException e) {
			System.out.println("Load Faild");
		}
	}

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
			mainPageDepartmentManagerController.setDepDetails(employee);
			mainPageDepartmentManagerController.getAmountOfPeopleTodayInPark();
			
			primaryStage.setOnCloseRequest(e -> FXMLFunctions.closeMainPage());
			primaryStage.setTitle("Main Page Department Manager");
			primaryStage.setScene(sc);
			primaryStage.show();
		} catch (IOException e) {
			System.out.println("Load Faild");
		}
	}

}