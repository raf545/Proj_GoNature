package faq;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import login.GoNatureLoginController;

public class FaqController {

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

}
