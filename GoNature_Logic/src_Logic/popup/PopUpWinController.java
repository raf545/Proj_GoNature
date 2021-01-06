package popup;



import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * 
 * This class is the popup window controller
 * 
 * @author rafaelelkoby
 *
 */
public class PopUpWinController {
// Class variables =========================
    @FXML
    private Button okButton;

    @FXML
    private Text messageField;

//  Class methods =========================
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
