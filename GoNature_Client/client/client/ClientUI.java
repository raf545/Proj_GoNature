package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import login.GoNatureLoginController;

/**
 * @author Dan Gutchin
 * @author Yaniv Sokolov
 * @author Rafael elkoby
 * @version December 3 2020
 */
public class ClientUI extends Application {

	public static ClientController chat; // only one instance

	public static void main(String args[]) throws Exception {
		launch(args);
	} // end main

	/**
	 * This method override the superclass method start and displays the GUI on to
	 * the screen
	 * 
	 * @param primaryStage the primary stage for this application
	 */

	@Override
	public void start(Stage primaryStage) throws Exception {
		chat = new ClientController("localhost", 5555);

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(GoNatureLoginController.class.getResource("GoNatureLogin.fxml"));

		Pane root = loader.load();

		Scene sc = new Scene(root);

		primaryStage.setTitle("Go Nature");
		primaryStage.setScene(sc);
		primaryStage.show();
	}

}
