package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class NewReservationController {

    @FXML
    private Button ContinueBtn;

    @FXML
    private ComboBox<?> ChooseParkComboBox;

    @FXML
    private DatePicker WantedDatePicker;

    @FXML
    private ComboBox<?> HourComboBox;

    @FXML
    private TextField EmailTxt;

    @FXML
    private Button minusBtn;

    @FXML
    private Label numOfVisitorTxt;

    @FXML
    private Button plusBtn;

    @FXML
    private Hyperlink ClickHereLink;

    @FXML
    private Text BackBtn;
    
    public void setIdentFields() {
    	//ChooseParkComboBox.getItems().addAll("","","");
    	//HourComboBox.getItems().add
	}

    @FXML
    void Back(MouseEvent event) {

    	StaticPaneMainPageClient.clientMainPane.getChildren().clear();
    }

    @FXML
    void ClickHereLink(ActionEvent event) {

    }

    @FXML
    void Continue(ActionEvent event) {

    }
 
    @FXML
    void minus(ActionEvent event) {

    }

    @FXML
    void plus(ActionEvent event) {

    }

}
