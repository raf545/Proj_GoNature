package parkManager;

import java.io.IOException;

import com.google.gson.Gson;

import client.ClientUI;
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
import login.SignInEmployeeController;
import requestHandler.RequestHandler;
import requestHandler.controllerName;

public class MainPageParkManagerController {

	Gson gson = new Gson();

	@FXML
	private Pane parkManaferPane;

	@FXML
	private Button parkReportBtn;

	@FXML
	private Button editParkBtn;

	@FXML
	private Button newReservationBtn;

	@FXML
	private Text quitBtn;

	@FXML
	private Hyperlink logoutBtn;

	@FXML
	void editPark(ActionEvent event) {

	}

	@FXML
	void newResrvation(ActionEvent event) {

	}

	@FXML
	void parkReport(ActionEvent event) {

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
