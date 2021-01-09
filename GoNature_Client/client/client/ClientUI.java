package client;

import java.io.IOException;

import cardReaderSimulator.CardReaderControllerSimulator;
import javafx.application.Application;
import javafx.application.Platform;
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
		primaryStage.setResizable(false);
		startCardReader();
	}

	private void startCardReader() {
		Platform.runLater(() -> {

			try {
				Stage cardReader = new Stage();
				FXMLLoader loaderCardReaderSimulator = new FXMLLoader();
				loaderCardReaderSimulator
						.setLocation(CardReaderControllerSimulator.class.getResource("readerSimulation.fxml"));

				Pane rootCardReaderSimulator = loaderCardReaderSimulator.load();

				Scene scCardReaderSimulator = new Scene(rootCardReaderSimulator);
				cardReader.setTitle("Card Reader simulation");
				CardReaderControllerSimulator cardReaderControllerSimulator = loaderCardReaderSimulator.getController();
				cardReaderControllerSimulator.setPrkNameComboBox();
				cardReader.setScene(scCardReaderSimulator);
				cardReader.show();
				cardReader.setResizable(false);
			} catch (IOException e) {
				e.printStackTrace();
			}

		});

	}

}
