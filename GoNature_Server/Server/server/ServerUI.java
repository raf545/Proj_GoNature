package server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
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

	}

	public static void runServer(String p, ServerGuiController serverGuiController) {
		int port = 0; // Port to listen on
		sPC = serverGuiController;
		try {
			port = Integer.parseInt(p); // Set port to 5555

		} catch (Throwable t) {
			System.out.println("ERROR - Could not connect!");
		}

		EchoServer sv = new EchoServer(port, sPC);

		try {
			sv.listen(); // Start listening for connections
		} catch (Exception ex) {
			System.out.println("ERROR - Could not listen for clients!");
		}
	}

}
//End of ServerUI class