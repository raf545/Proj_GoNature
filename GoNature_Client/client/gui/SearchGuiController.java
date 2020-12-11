package gui;

import java.io.IOException;

import client.ChatClient;
import client.ClientUI;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * @author Dan Gutchin
 * @author Yaniv Sokolov
 * @author Rafael Elkoby
 * @version December 3 2020
 */

public class SearchGuiController {

// Instance variables **********************************************
	/**
	 * idText the field in witch the user type in the ID in the GUI
	 */

	@FXML
	private Label idNotFoundLabel;

	@FXML
	private TextField idText;

	@FXML
	private Button searchBtn;

	@FXML
	private Button exitBtn;

// Getters methods **********************************************
	/**
	 * This method Gets the written id in the text field
	 * 
	 * @return the id field in the idText field
	 */
	private String getID() {
		return idText.getText();
	}

// Instance methods ************************************************
	/**
	 * This method closes a given java ?????
	 * 
	 * FIXME Replace Platform.exit(); with a more gentle measure
	 * 
	 * @throws IOException
	 */

	@FXML
	void ExitWin(ActionEvent event) throws IOException {
		Platform.exit();
		ClientUI.chat.accept("close");

	}

	/**
	 * This method sends the given id number to the server, witch executes a SQL
	 * query, and get the relevant info about a given id. then displays it on the
	 * VisitoForm GUI.
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void SerchIdInDB(ActionEvent event) throws IOException {
		String id;
		FXMLLoader loader = new FXMLLoader();

		id = getID();
		if (id.trim().isEmpty()) {

			System.out.println("You must enter an id number");

		} else {
			ClientUI.chat.accept(id);

			if (ChatClient.v1.getId().equals("Error")) {
				System.out.println("ID Not Found");
				idNotFoundLabel.setVisible(true);

			} else {
				System.out.println("ID Found");
				((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
				Stage primaryStage = new Stage();
				Pane root = loader.load(getClass().getResource("/gui/VisitorForm.fxml").openStream());
				VisitorFormController VisitorFormController = loader.getController();
				VisitorFormController.loadVisitor(ChatClient.v1);

				// how to write

				Scene scene = new Scene(root);
				primaryStage.setTitle("Visitor Managment Tool");

				primaryStage.setScene(scene);
				primaryStage.show();
			}
		}

	}

	/**
	 * This method start and displays the GUI on to the screen
	 * 
	 * @throws Exception
	 * @param primaryStage the primary stage for this application
	 */
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/SerchGui.fxml"));

		Scene scene = new Scene(root);
		primaryStage.setTitle("Search");
		primaryStage.setScene(scene);

		primaryStage.show();

	}

}
//End of SearchController class
