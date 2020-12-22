package login;

import java.io.IOException;

import org.omg.CORBA.Request;

import com.google.gson.Gson;

import client.ChatClient;
import client.ClientUI;
import departmentManager.MainPageDepartmentManagerController;
import employee.Employee;
import employee.MainPageEmployeeController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import parkManager.MainPageParkManagerController;
import popup.PopUp;
import requestHandler.RequestHandler;
import requestHandler.controllerName;
import reservation.Reservation;
import subscriber.Subscriber;

public class SignInEmployeeController {
	Gson gson = new Gson();
	@FXML
	private TextField IDTxt;

	@FXML
	private Button ContinueBtn;

	@FXML
	private TextField PasswordTxt;

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
		String fxmlName;
		String title;
		Employee employee = new Employee(IDTxt.getText(), PasswordTxt.getText());
		RequestHandler rh = new RequestHandler(controllerName.LoginController, "employeeLogIn", gson.toJson(employee));
		ClientUI.chat.accept(gson.toJson(rh));
		analyzeAnswerFromServer();
	}

	private void analyzeAnswerFromServer() {
		String fxmlName = null;
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
				openDepartmentManagerGui();
				break;
			case "park manager":
				openParkManagerGui();
				break;
			case "employee":
				openEmployeeGui();
				break;

			default:
				break;
			}
		}
	}

//MainPageParkManager
	// "MainPageEmployee.fxml"
	//
	private void openEmployeeGui() {
		try {
			Stage primaryStage = new Stage();
			Stage stage = (Stage) ContinueBtn.getScene().getWindow();
			stage.close();

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainPageEmployeeController.class.getResource("MainPageEmployee.fxml"));
			Pane root = loader.load();

			Scene sc = new Scene(root);
			primaryStage.setTitle("Employee Main Page");
			primaryStage.setScene(sc);
			primaryStage.show();
		} catch (IOException e) {
			System.out.println("Load Faild");
		}
	}

	private void openParkManagerGui() {
		try {
			Stage primaryStage = new Stage();
			Stage stage = (Stage) ContinueBtn.getScene().getWindow();
			stage.close();

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainPageParkManagerController.class.getResource("MainPageParkManager.fxml"));
			Pane root = loader.load();

			Scene sc = new Scene(root);
			primaryStage.setTitle("Main Page Park Manager");
			primaryStage.setScene(sc);
			primaryStage.show();
		} catch (IOException e) {
			System.out.println("Load Faild");
		}
	}

	private void openDepartmentManagerGui() {
		try {
			Stage primaryStage = new Stage();
			Stage stage = (Stage) ContinueBtn.getScene().getWindow();
			stage.close();

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainPageDepartmentManagerController.class.getResource("MainPageDepartmentManager.fxml"));
			Pane root = loader.load();

			Scene sc = new Scene(root);
			primaryStage.setTitle("Main Page Department Manager");
			primaryStage.setScene(sc);
			primaryStage.show();
		} catch (IOException e) {
			System.out.println("Load Faild");
		}
	}

}
