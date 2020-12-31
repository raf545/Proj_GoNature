package cardReader;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import popup.PopUp;

public class paymentController {

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
			Stage stage = (Stage) payBtn.getScene().getWindow();
			stage.close();
		}

	}

}
