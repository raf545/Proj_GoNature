package serverGui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import server.ServerUI;

/**
 * @author Dan Gutchin
 * @author Yaniv Sokolov
 * @author Rafael elkoby
 * @version December 3 2020
 */
public class ServerGuiController {

// Instance variables **********************************************
	@FXML
	private TextField PorTxt;

	@FXML
	private Button DoneBtn;

	@FXML
	private TextField DBStatusTxt;

	@FXML
	private Text ExitBtn;

// Getters methods **********************************************

	/**
	 * @return
	 */
	private String getport() {
		return PorTxt.getText();
	}

// Setters methods **********************************************
	/**
	 * This method gets a client info to update the GUI
	 * 
	 * @param ip   The client IP
	 * @param host The host IP
	 */
//	public void setConnectToDB(String ip, String host) {
//		Platform.runLater(() -> {
//			DBStatusTxt.setText("Connected");
//		});
//	}

	/**
	 * This method sets all the server GUI fields to be empty and set the status to
	 * disconnected
	 */

	public void setConnectToDB() {
		Platform.runLater(() -> {
			DBStatusTxt.setText("Connected");
			PorTxt.setDisable(true);
			DoneBtn.setDisable(true);
		});
	}

// Instance methods ************************************************
	/**
	 * This method sets the serve port with a given user port and sets the relevant
	 * GUI fields
	 */
	@FXML
	void Done(ActionEvent event) {
		String p;
		p = getport();
		if (p.trim().isEmpty()) {
			System.out.println("You must enter a port number");
		} else {

			ServerUI.runServer(p, this);
		}
	}

	/**
	 * This method exits the serve window and the close the server
	 */

	@FXML
	void Exit(MouseEvent event) {
		// get a handle to the stage
		Stage stage = (Stage) ExitBtn.getScene().getWindow();
		// do what you have to do
		stage.close();
		System.exit(0);

	}
}
//End of serverPortController class
