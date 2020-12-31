package cardReader;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.stage.Stage;

public class paymentController {

    @FXML
    private Button payBtn;

    @FXML
    void pay(ActionEvent event) {
        
        Stage stage = (Stage) payBtn.getScene().getWindow();
        stage.close();
		
    }

}
