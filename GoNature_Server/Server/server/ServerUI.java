package server;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import popup.PopUp;
import serverGui.ServerGuiController;

public class ServerUI extends Application {

	final public static int DEFAULT_PORT = 5555;
	static ServerGuiController sPC;

	public static void main(String args[]) throws Exception {
		launch(args);
	} // end main

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ServerGuiController.class.getResource("ServerGUI.fxml"));
		Pane root = loader.load();
		Scene sc = new Scene(root);
		primaryStage.setTitle("Server");
		primaryStage.setScene(sc);
		primaryStage.show();
		primaryStage.setResizable(false);

		primaryStage.setOnCloseRequest((event) -> {
			System.exit(0);
		});

	}

	/**
	 * after push done button start the connection
	 * 
	 * @param p                   get the port from the user and start connection
	 * @param serverGuiController the gui controller
	 */
	public static void runServer(String p, ServerGuiController serverGuiController) {
		int port = 0; // Port to listen on
		sPC = serverGuiController;
		try {
			port = Integer.parseInt(p); // Set port to 5555

		} catch (Throwable t) {
			System.out.println("ERROR - Could not connect!");
		}
		if (port == DEFAULT_PORT) {
			EchoServer sv = new EchoServer(port, sPC);

			try {
				sv.listen(); // Start listening for connections
			} catch (Exception ex) {
				System.out.println("ERROR - Could not listen for clients!");
			}
		} else {
			PopUp.display("ERROR", "no existing port");
		}
	}

}
//End of ServerUI class