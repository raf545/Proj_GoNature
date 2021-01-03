package cardReaderSimulator;

import java.io.IOException;

import com.google.gson.Gson;

import cardReader.IdAndPark;
import controllers.CardReaderController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import popup.PopUp;
import popup.PopUpWinController;
import requestHandler.RequestHandler;
import requestHandler.controllerName;

public class CardReaderControllerSimulator {

	Gson gson = new Gson();

	@FXML
	private Tab entranceBtn;

	@FXML
	private TextField enterTxt;

	@FXML
	private Button enterParkBtn;

	@FXML
	private Tab exitBtn;

	@FXML
	private TextField exitTxt;

	@FXML
	private Button exitParkBtn;

	@FXML
	private ComboBox<String> exitParkPicker;

	@FXML
	private ComboBox<String> enterParkPicker;

	public void setPrkNameComboBox() {
		enterParkPicker.getItems().addAll("Niagara", "Banias", "Safari");
		exitParkPicker.getItems().addAll("Niagara", "Banias", "Safari");
	}

	@FXML
	void enterPark(ActionEvent event) {

		StringBuilder popError = new StringBuilder();

		if (enterTxt.getText().isEmpty())
			popError.append("Must Entern an ID\n");

		if (enterParkPicker.getSelectionModel().getSelectedItem() == null)
			popError.append("Must choose Park\n");

		if (popError.length() > 0) {
			PopUp.display("Error", popError.toString());
		} else {
			IdAndPark idAndPark = new IdAndPark(enterTxt.getText(), enterParkPicker.getValue());

			String answerFromServer = CardReaderController.getInstance().router("enterPark", gson.toJson(idAndPark),
					null);
			PopUp.display("Card reader simulation", answerFromServer);

			if (answerFromServer.contains("Entered successfully"))
				popUpPayment();
		}

	}

	private void popUpPayment() {
		try {
			Stage primaryStage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(paymentController.class.getResource("payment.fxml"));
			Pane root = loader.load();
			Scene sc = new Scene(root);
			primaryStage.setTitle("Payment");
			primaryStage.setScene(sc);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@FXML
	void exitPark(ActionEvent event) {

		StringBuilder popError = new StringBuilder();

		if (exitTxt.getText().isEmpty())
			popError.append("Must Entern an ID\n");

		if (exitParkPicker.getSelectionModel().getSelectedItem() == null)
			popError.append("Must choose Park\n");

		if (popError.length() > 0) {
			PopUp.display("Error", popError.toString());
		} else {
			IdAndPark idAndPark = new IdAndPark(exitTxt.getText(), exitParkPicker.getValue());
			String answerFromServer = CardReaderController.getInstance().router("exitPark", gson.toJson(idAndPark),
					null);
			PopUp.display("Card reader simulation", answerFromServer);
		}
	}

}
