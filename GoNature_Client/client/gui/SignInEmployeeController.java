package gui;

import java.io.IOException;

import org.omg.CORBA.Request;

import com.google.gson.Gson;

import client.ChatClient;
import client.ClientUI;
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
import logic.Employee;
import logic.Reservation;
import logic.Subscriber;
import popup.PopUp;
import requestHandler.RequestHandler;
import requestHandler.controllerName;

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
		loader.setLocation(this.getClass().getResource("GoNatureLogin.fxml"));
		Pane root = loader.load();
		Scene sc = new Scene(root);
		primaryStage.setTitle("Login");
		primaryStage.setScene(sc);
		primaryStage.show();

	}

	@FXML
	void Continue(ActionEvent event) {
		Employee employee = new Employee(IDTxt.getText(), PasswordTxt.getText());
		RequestHandler rh = new RequestHandler(controllerName.LoginController, "employeeLogIn", gson.toJson(employee));
		ClientUI.chat.accept(gson.toJson(rh));
		analyzeAnswerFromServer();
	}

	private void analyzeAnswerFromServer() {
		switch (ChatClient.serverMsg) {
		case "employee not found":
			PopUp.display("Error", "employee not found");
			break;

		case "wrong password":
			PopUp.display("Error", "wrong password");
			break;
		default:
			Employee employee = gson.fromJson(ChatClient.serverMsg, Employee.class);
			openEmployeeGui(employee);
		}
	}

	private void openEmployeeGui(Employee employee) {
		try {
			Stage primaryStage = new Stage();
			Stage stage = (Stage) ContinueBtn.getScene().getWindow();
			// FIXME primaryStage.setOnCloseRequest(value);
			stage.close();

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(this.getClass().getResource("MainPageEmployee.fxml"));
			Pane root = loader.load();

			MainPageEmployeeController mainPageEmployeeController = loader.getController();
			mainPageEmployeeController.setEmp(employee);

			Scene sc = new Scene(root);
			primaryStage.setTitle("Main page for emlpoyee");
			primaryStage.setScene(sc);
			primaryStage.show();
		} catch (IOException e) {
			System.out.println("Load Faild");
		}
	}

}
