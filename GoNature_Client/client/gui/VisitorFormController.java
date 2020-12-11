package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.Visitor;

/**
 * @author Dan Gutchin
 * @author Yaniv Sokolov
 * @author Rafael Elkoby
 * @version December 3 2020
 */

public class VisitorFormController {

// Instance variables **********************************************
	Visitor sendV;
	/**
	 * a message list witch contains the "RequestID in index 0 and the content in
	 * the following indexes
	 */
	List<String> msgListForServer;

	Visitor v;
	/**
	 * nameText the field witch is field with the visitor name
	 */
	@FXML
	private TextField nameText;
	/**
	 * lastNameText the field witch is field with the visitor last name
	 */
	@FXML
	private TextField lastNameText;
	/**
	 * idText the field witch is field with the visitor ID
	 */
	@FXML
	private TextField idText;
	/**
	 * phoneText the field witch is field with the visitor Phone
	 */
	@FXML
	private TextField phoneText;
	/**
	 * emailText the field witch is field with the visitor Email
	 */
	@FXML
	private TextField emailText;

	@FXML
	private Button emailUpadteBtn;

	@FXML
	private Label emailUpdatedText;

	/**
	 * This method Send the server a request to update the email for a given visitor
	 *
	 * @param event
	 */
	@FXML
	void updateEmailInDB(ActionEvent event) {

		msgListForServer = new ArrayList<String>();
		msgListForServer.add(idText.getText());
		msgListForServer.add(emailText.getText());

		ClientUI.chat.accept(msgListForServer);
		emailUpdatedText.setVisible(true);

	}

	/**
	 * This method Sets all the VisitoForm GUI details of a certain visitor
	 * 
	 * @param v1 A Visitor instance
	 */
	public void loadVisitor(Visitor v1) {
		this.v = v1;
		this.idText.setText(v.getId());
		this.nameText.setText(v.getName());
		this.lastNameText.setText(v.getLastname());
		this.phoneText.setText(v.getPhone());
		this.emailText.setText(v.getEmail());
	}

	/**
	 * This method display the search screen
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void backBtn(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();

		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/gui/SerchGui.fxml").openStream());

		// how to write

		Scene scene = new Scene(root);
		primaryStage.setTitle("Visitor Managment Tool");

		primaryStage.setScene(scene);
		primaryStage.show();

	}

}
//End of VisitorFormController class
