package departmentManager;

import java.io.IOException;

import com.google.gson.Gson;

import client.ClientUI;
import employee.SignInEmployeeController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import requestHandler.RequestHandler;
import requestHandler.controllerName;

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
	private Hyperlink logoutBtn;

	@FXML
	void approveChange(ActionEvent event) {

	}

	@FXML
	void managerReport(ActionEvent event) {

	}

	@FXML
	void newReservation(ActionEvent event) {

	}

	@FXML
	void quit(MouseEvent event) {

	}

	@FXML
	void logout(ActionEvent event) throws IOException {
		RequestHandler rh = new RequestHandler(controllerName.LoginController, "logout", "");
		ClientUI.chat.accept(gson.toJson(rh));

		Stage primaryStage = new Stage();
		// get a handle to the stage
		Stage stage = (Stage) managerReportBtn.getScene().getWindow();
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
