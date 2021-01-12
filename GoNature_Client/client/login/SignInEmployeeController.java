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
import fxmlGeneralFunctions.FXMLOpenEmployeeGui;
import fxmlGeneralFunctions.IFXMLOpenEmployeeGui;
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
import login.SignInEmployeeControllerTest.MyFXMLOpenEmployeeGui;
import parkManager.MainPageParkManagerController;
import parkManager.ParkManagerCapacityThred;
import popup.PopUp;
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

	IFXMLOpenEmployeeGui fxmlOpenEmployeeGui = new FXMLOpenEmployeeGui();

	public void setFxmlOpenGui(MyFXMLOpenEmployeeGui myFXMLOpenEmployeeGui) {
		fxmlOpenEmployeeGui = myFXMLOpenEmployeeGui;
	}

	public void setPasswordTxt(String password) {
		PasswordTxt.setText(password);
	}

	public void setIDTxt(String id) {
		IDTxt.setText(id);
	}

	public String getPasswordTxt() {
		return PasswordTxt.getText();
	}

	public String getID() {
		return IDTxt.getText();
	}

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
		primaryStage.setResizable(false);

	}

	/**
	 * check if the id is valid or not and insert the user
	 * 
	 * @param event
	 */
	@FXML
	void Continue(ActionEvent event) {
		StringBuilder popError = new StringBuilder();

		if (getID().isEmpty()) {
			popError.append("Must enter id\n");
		}
		if (getPasswordTxt().isEmpty()) {
			popError.append("Must enter password\n");
		}

		if (popError.length() > 0) {
			PopUp.popUpForCheck.display("Error", popError.toString());
		} else {

			Employee employee = new Employee(IDTxt.getText(), PasswordTxt.getText());
			RequestHandler rh = new RequestHandler(controllerName.LoginController, "employeeLogIn",
					gson.toJson(employee));
			ClientUI.chat.accept(gson.toJson(rh));
			analyzeAnswerFromServer();
		}
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
			PopUp.popUpForCheck.display("Error", "employee not found");
			break;

		case "wrong password":
			PopUp.popUpForCheck.display("Error", "wrong password");
			break;
		case "already connected":
			PopUp.popUpForCheck.display("Error", "already connected");
			break;
		default:
			Employee employee = gson.fromJson(ChatClient.serverMsg, Employee.class);

			switch (employee.getTypeOfEmployee()) {
			case "department manager":
				fxmlOpenEmployeeGui.openDepartmentManagerGui(employee, BackBtn);
				break;
			case "park manager":
				fxmlOpenEmployeeGui.openParkManagerGui(employee, BackBtn);
				break;
			case "employee":
				fxmlOpenEmployeeGui.openEmployeeGui(employee, BackBtn);
				break;

			default:
				break;
			}
		}
	}

}