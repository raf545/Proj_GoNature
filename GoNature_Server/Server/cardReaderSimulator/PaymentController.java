package cardReaderSimulator;

import java.sql.ResultSet;
import java.sql.SQLException;

import dataBase.DataBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import popup.PopUp;

/**
 * this is the class of the controller for the payment gui. this class contains
 * all the payment gui buttons functions and operation.
 * 
 * @author dan
 *
 */
public class PaymentController {

	@FXML
	private TextField creditCardText;

	@FXML
	private TextField dateText;

	@FXML
	private TextField cvcText;

	@FXML
	private Button payBtn;

	/**
	 * checks if the text fileds of the gui are filled correctly. if the filled
	 * correctly a success pop up window will appear. else, an error message will
	 * appear.
	 * 
	 * @param event (mouse click)
	 */
	@FXML
	void pay(ActionEvent event) {

		StringBuilder popError = new StringBuilder();

		if (!(dateText.getText().matches("(?:0[1-9]|1[0-2])/[0-9]{2}")))
			popError.append("*Must Entern a valid date: MM/YY \n");

		if (!(creditCardText.getText().matches("[0-9]+")))
			popError.append("*Must enter only numbers in the credit card\n");

		if (creditCardText.getText().isEmpty())
			popError.append("*Must Entern a Credit card\n");

		if (dateText.getText().isEmpty())
			popError.append("*Must Entern a date\n");

		if (cvcText.getText().isEmpty())
			popError.append("*Must Entern a cvc\n");

		if (cvcText.getText().length() != 3)
			popError.append("*cvc must be 3 numbers\n");

		if (popError.length() > 0) {
			PopUp.display("Error", popError.toString());
		} else {
			PopUp.display("success", "the payment was succesful");
			Stage stage = (Stage) payBtn.getScene().getWindow();
			stage.close();
		}

	}

	/**
	 * checks if the visitor with the given id has a saved credit card number, if so
	 * print it in the credit card text field.
	 * 
	 * @param id (visitor's id)
	 */
	void checkCredit(String id) {
		String searchQuery = null;
		searchQuery = "select creditCardNumber from gonaturedb.subscriber where id = \"" + id + "\";";
		ResultSet creditCard = DataBase.getInstance().search(searchQuery);
		try {
			while (creditCard.next()) {
				creditCardText.setText(creditCard.getString("creditCardNumber"));
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

}
