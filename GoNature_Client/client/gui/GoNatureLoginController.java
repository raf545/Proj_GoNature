package gui;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class GoNatureLoginController {

	@FXML
	private Hyperlink ClickHereBtn;

	@FXML
	private Button SignInBtn;

	@FXML
	private Button signInEmpBtn;

	@FXML
	private Circle ExitBtn;

	@FXML
	void ClickHere(ActionEvent event) throws IOException {
		Stage primaryStage = new Stage();
		// get a handle to the stage
		Stage stage = (Stage) SignInBtn.getScene().getWindow();
		// do what you have to do
		stage.close();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("FAQ.fxml"));
		Pane root = loader.load();
		Scene sc = new Scene(root);
		primaryStage.setTitle("FAQ");
		primaryStage.setScene(sc);
		primaryStage.show();
	}

	@FXML
	void Exit(MouseEvent event) {
		// get a handle to the stage
		Stage stage = (Stage) ExitBtn.getScene().getWindow();
		// do what you have to do
		stage.close();
	}

	@FXML
	void SignIn(ActionEvent event) throws IOException {
		Stage primaryStage = new Stage();
		// get a handle to the stage
		Stage stage = (Stage) SignInBtn.getScene().getWindow();
		// do what you have to do
		stage.close();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("Identification.fxml"));
		Pane root = loader.load();
		Scene sc = new Scene(root);
		primaryStage.setTitle("Login");
		primaryStage.setScene(sc);
		// set comboBox
		IdentificationController identificationController = loader.getController();
		identificationController.setIdentificationComboBox();
		primaryStage.show();

	}

	@FXML
	void signInEmployee(ActionEvent event) throws IOException {
		Stage primaryStage = new Stage();
		// get a handle to the stage
		Stage stage = (Stage) SignInBtn.getScene().getWindow();
		// do what you have to do
		stage.close();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("SignInEmployee.fxml"));
		Pane root = loader.load();
		Scene sc = new Scene(root);
		primaryStage.setTitle("Employee Login");
		primaryStage.setScene(sc);
		primaryStage.show();

	}

}
