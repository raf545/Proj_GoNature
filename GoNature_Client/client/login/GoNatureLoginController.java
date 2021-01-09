package login;

import java.io.IOException;

import faq.FaqController;
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

/**
 * 
 * this class is the open gui for the program and give to the client options to
 * login
 * 
 * @author Yaniv Sokolov
 *
 */
public class GoNatureLoginController {

	@FXML
	private Hyperlink ClickHereBtn;

	@FXML
	private Button SignInBtn;

	@FXML
	private Button signInEmpBtn;

	@FXML
	private Circle ExitBtn;

	/**
	 * This function directs you to help connect
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void ClickHere(ActionEvent event) throws IOException {
		Stage primaryStage = new Stage();
		// get a handle to the stage
		Stage stage = (Stage) SignInBtn.getScene().getWindow();
		// do what you have to do
		stage.close();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(FaqController.class.getResource("FAQ.fxml"));
		Pane root = loader.load();
		Scene sc = new Scene(root);
		primaryStage.setTitle("FAQ");
		primaryStage.setScene(sc);
		primaryStage.setResizable(false);
		primaryStage.show();
		primaryStage.setResizable(false);
	}

	@FXML
	void Exit(MouseEvent event) {
		// get a handle to the stage
		Stage stage = (Stage) ExitBtn.getScene().getWindow();
		// do what you have to do
		stage.close();
	}

	/**
	 * this button direct you to sign in gui
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void SignIn(ActionEvent event) throws IOException {
		Stage primaryStage = new Stage();
		// get a handle to the stage
		Stage stage = (Stage) SignInBtn.getScene().getWindow();
		// do what you have to do
		stage.close();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(IdentificationController.class.getResource("Identification.fxml"));
		Pane root = loader.load();
		Scene sc = new Scene(root);
		primaryStage.setTitle("Login");
		primaryStage.setScene(sc);
		primaryStage.show();
		primaryStage.setResizable(false);

	}

	/**
	 * this button direct you to employee sign in
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void signInEmployee(ActionEvent event) throws IOException {
		Stage primaryStage = new Stage();
		// get a handle to the stage
		Stage stage = (Stage) SignInBtn.getScene().getWindow();
		// do what you have to do
		stage.close();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(SignInEmployeeController.class.getResource("SignInEmployee.fxml"));
		Pane root = loader.load();
		Scene sc = new Scene(root);
		primaryStage.setTitle("Employee Login");
		primaryStage.setScene(sc);
		primaryStage.show();
		primaryStage.setResizable(false);

	}

}
