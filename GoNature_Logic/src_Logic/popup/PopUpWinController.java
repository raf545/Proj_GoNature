package popup;



import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PopUpWinController {

    @FXML
    private Button okButton;

    @FXML
    private Text messageField;

    @FXML
    void closePopUp(ActionEvent event) {
    	Stage stage;
    	stage = (Stage) okButton.getScene().getWindow();
    	stage.close();
    }

	public void setMessage(String message) {
		messageField.setText(message);
		
	}

}
