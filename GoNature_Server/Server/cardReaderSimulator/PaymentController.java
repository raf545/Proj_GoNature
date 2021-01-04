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

public class PaymentController {
	
	
	@FXML
	private TextField creditCardText;

	@FXML
	private TextField dateText;

	@FXML
	private TextField cvcText;

	@FXML
	private Button payBtn;

	@FXML
	void pay(ActionEvent event) {

		StringBuilder popError = new StringBuilder();

		if (creditCardText.getText().isEmpty())
			popError.append("Must Entern a Credit card\n");

		if (dateText.getText().isEmpty())
			popError.append("Must Entern a date\n");

		if (cvcText.getText().isEmpty())
			popError.append("Must Entern a cvc\n");

		if (popError.length() > 0) {
			PopUp.display("Error", popError.toString());
		} else {
			PopUp.display("success", "the payment was succesful");
			Stage stage = (Stage) payBtn.getScene().getWindow();
			stage.close();
		}

	}

	void checkCredit(String id){
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
