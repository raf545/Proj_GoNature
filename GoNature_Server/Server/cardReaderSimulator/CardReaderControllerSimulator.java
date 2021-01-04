package cardReaderSimulator;

import java.io.IOException;

import com.google.gson.Gson;

import cardReader.IdAndParkAndNum;
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
    private TextField numOfVisitorsTxt;
    
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

		if (numOfVisitorsTxt.getText().isEmpty())
			popError.append("Must enter the number of visitors\n");
		
		if (popError.length() > 0) {
			PopUp.display("Error", popError.toString());
		} else {
			IdAndParkAndNum idAndParkAndNum = new IdAndParkAndNum(enterTxt.getText(), enterParkPicker.getValue(),numOfVisitorsTxt.getText());

			String answerFromServer = CardReaderController.getInstance().router("enterPark", gson.toJson(idAndParkAndNum),
					null);
			PopUp.display("Card reader simulation", answerFromServer);

			if (answerFromServer.contains("Entered successfully"))
				popUpPayment(enterTxt.getText());
		}
		numOfVisitorsTxt.clear();
    	enterTxt.clear();
    	enterParkPicker.getSelectionModel().clearSelection();

	}

	private void popUpPayment(String id) {
		try {
			Stage primaryStage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(PaymentController.class.getResource("payment.fxml"));
			Pane root = loader.load();
			Scene sc = new Scene(root);
			primaryStage.setTitle("Payment");
			primaryStage.setScene(sc);
			PaymentController PaymentController = loader.getController();
			PaymentController.checkCredit(id);
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
			IdAndParkAndNum idAndParkAndNum = new IdAndParkAndNum(exitTxt.getText(), exitParkPicker.getValue(),null);
			String answerFromServer = CardReaderController.getInstance().router("exitPark", gson.toJson(idAndParkAndNum),
					null);
			PopUp.display("Card reader simulation", answerFromServer);
		}
		exitTxt.clear();
    	exitParkPicker.getSelectionModel().clearSelection();
	}

    @FXML
    void clearTxtEntrance(ActionEvent event) {

    	exitTxt.clear();
    	exitParkPicker.getSelectionModel().clearSelection();
    
    }

    @FXML
    void clearTxtExit(ActionEvent event) {
    	numOfVisitorsTxt.clear();
    	enterTxt.clear();
    	enterParkPicker.getSelectionModel().clearSelection();
    }

}
