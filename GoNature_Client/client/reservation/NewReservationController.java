package reservation;

import guiCommon.StaticPaneMainPageClient;
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
import popup.PopUp;

public class NewReservationController {

	@FXML
	private Button ContinueBtn;

	@FXML
	private ComboBox<String> ChooseParkComboBox;

	@FXML
	private DatePicker WantedDatePicker;

	@FXML
	private ComboBox<String> HourComboBox;

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

	private int countVisitor;

	public void setIdentFields() {
		ChooseParkComboBox.getItems().addAll("Niagara", "Banias", "Safari");
		HourComboBox.getItems().addAll("08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00",
				"17:00", "18:00");
		countVisitor = 0;
		numOfVisitorTxt.setText(String.valueOf(countVisitor));

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
		if (countVisitor == 0)
			PopUp.display("Error", "Can not order less then zero");
		else {
			countVisitor--;
			numOfVisitorTxt.setText(String.valueOf(countVisitor));
		}
	}

	@FXML
	void plus(ActionEvent event) {
		countVisitor++;
		numOfVisitorTxt.setText(String.valueOf(countVisitor));
	}

}
