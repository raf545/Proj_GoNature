package serverGui;

import Server.ServerUI;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * @author Dan Gutchin
 * @author Yaniv Sokolov
 * @author Rafael elkoby
 * @version December 3 2020
 */
public class ServerPortController {

// Instance variables **********************************************
	@FXML
	private Button btnDone;

	/**
	 * portxt the field in witch the user type in the server port in the GUI
	 */
	@FXML
	private TextField portxt;

	@FXML
	private Button btnExit;

	public Button getBtnExit() {
		return btnExit;
	}

	/**
	 * statusTxt the field in witch the user can see the connection status in the
	 * GUI
	 */
	@FXML
	private TextField statusTxt;
	/**
	 * hostTxt the field in witch the user can see who is the host in the GUI
	 */
	@FXML
	private TextField hostTxt;
	/**
	 * ipTxt the field in witch the user can see his IP Address in the GUI
	 */
	@FXML
	private TextField ipTxt;

	@FXML
	private TextField dbStatus;

// Getters methods **********************************************

	/**
	 * @return
	 */
	private String getport() {
		return portxt.getText();
	}

// Setters methods **********************************************
	/**
	 * This method gets a client info to update the GUI
	 * 
	 * @param ip   The client IP
	 * @param host The host IP
	 */
	public void setInfoClient(String ip, String host) {
		Platform.runLater(() -> {
			statusTxt.setText("Connected");
			ipTxt.setText(ip);
			hostTxt.setText(host);

		});
	}

	/**
	 * This method sets all the server GUI fields to be empty and set the status to
	 * disconnected
	 */
	public void setDisconectClientFields() {
		Platform.runLater(() -> {
			statusTxt.setText("disConnected");
			ipTxt.setText("");
			hostTxt.setText("");
		});
	}

	public void setConnectToDB() {
		Platform.runLater(() -> {
			dbStatus.setText("Connected");
		});
	}

// Instance methods ************************************************
	/**
	 * This method sets the serve port with a given user port and sets the relevant
	 * GUI fields
	 */
	@FXML
	void setPort(ActionEvent event) {
		String p;

		p = getport();
		btnDone.setDisable(true);
//		ipTxt.setDisable(true);
//		statusTxt.setDisable(true);
//		hostTxt.setDisable(true);

		if (p.trim().isEmpty()) {
			System.out.println("You must enter a port number");

		} else {

			portxt.setDisable(true);
			statusTxt.setText("disConnected");
			ipTxt.setText("");
			hostTxt.setText("");

			ServerUI.runServer(p, this);

		}

	}

	/**
	 * This method exits the serve window and the close the server
	 */
	@FXML
	void serverExitBtn(ActionEvent event) {
		System.out.println("exit tool");
		/*
		 * FIXME System.exit(0); is a to strong exit and closes the VM and not only the
		 * server!!!!!!
		 */
		System.exit(0);
	}

	/**
	 * This method start and displays the GUI on to the screen
	 * 
	 * @throws Exception
	 * @param primaryStage the primary stage for this application
	 */
//	public void start(Stage primaryStage) throws Exception {
//		Parent root = FXMLLoader.load(this.getClass().getResource("ServerPort.fxml"));
//
//		Scene scene = new Scene(root);
//		primaryStage.setTitle("Server");
//		primaryStage.setScene(scene);
//
//		primaryStage.show();
//	}

}
//End of serverPortController class
