package employee;

import java.io.IOException;

import com.google.gson.Gson;

import cardReader.CardReaderController;
import client.ClientUI;
import familySubWorker.NewFamilySubWorkerController;
import groupLeader.NewGroupLeaderWorkerController;
import guiCommon.StaticPaneMainPageEmployee;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import login.IdentificationController;
import login.SignInEmployeeController;
import requestHandler.RequestHandler;
import requestHandler.controllerName;
import reservation.ReservationForOccasionalVisitorController;

public class MainPageEmployeeController {
	@FXML
	private Pane mainPane;

	@FXML
	private ImageView openNewFamilySubBtnP;

	@FXML
	private Button NewFamilySubBtn;

	@FXML
	private ImageView openNewInstructorBtnP;

	@FXML
	private Button NewInstructorBtn;

	@FXML
	private Button readerSimulation;

	@FXML
	private Hyperlink logoutBtn;

	Gson gson = new Gson();

	@SuppressWarnings("unused")
	private Employee employee;

	void setEmp(Employee employeeFromDB) {
		employee = employeeFromDB;
	}

	public Pane getPane() {
		return mainPane;
	}

	@FXML
	void openNewFamilySub(ActionEvent event) throws IOException {
//		FIXME
//		StaticPaneMainPageEmployee.employeeMainPane = mainPane;
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(NewFamilySubWorkerController.class.getResource("newfamilysubworker.fxml"));

		Pane root = loader.load();
		mainPane.getChildren().clear();
		mainPane.getChildren().add(root);

	}

	@FXML
	void openNewInstructor(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(NewGroupLeaderWorkerController.class.getResource("newGroupLeaderworkerns.fxml"));

		Pane root = loader.load();
		mainPane.getChildren().clear();
		mainPane.getChildren().add(root);
	}

	@FXML
	void logout(ActionEvent event) throws IOException {
		RequestHandler rh = new RequestHandler(controllerName.LoginController, "logout", "");
		ClientUI.chat.accept(gson.toJson(rh));

		Stage primaryStage = new Stage();
		// get a handle to the stage
		Stage stage = (Stage) NewInstructorBtn.getScene().getWindow();
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

	@FXML
	void openReaderSimulation(ActionEvent event) throws IOException {
//		FIXME
//		StaticPaneMainPageEmployee.employeeMainPane = mainPane;
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(CardReaderController.class.getResource("readerSimulation.fxml"));
		Pane root = loader.load();
		CardReaderController cardReaderControllerController = loader.getController();
		cardReaderControllerController.setPrkNameComboBox();
		mainPane.getChildren().clear();
		mainPane.getChildren().add(root);
	}

	@FXML
	void reservationForOccasionalVisitor(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(
				ReservationForOccasionalVisitorController.class.getResource("ReservationForOccasionalVisitor.fxml"));
		Pane root = loader.load();
		ReservationForOccasionalVisitorController reservationForOccasionalVisitorController = loader.getController();
		reservationForOccasionalVisitorController.setIdentFields();
		mainPane.getChildren().clear();
		mainPane.getChildren().add(root);
	}

}