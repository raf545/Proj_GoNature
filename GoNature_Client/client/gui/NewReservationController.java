package gui;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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
	private Hyperlink ClickHereLink;

	@FXML
	private Text BackBtn;

	@FXML
	void Back(MouseEvent event) {

	}

	@FXML
	void ClickHereLink(ActionEvent event) {

	}

	@FXML
	void Continue(ActionEvent event) throws IOException {

	}

}
