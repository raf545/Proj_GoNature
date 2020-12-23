package parkMangerChanges;

import com.google.gson.Gson;

import client.ChatClient;
import client.ClientUI;
import employee.Employee;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import parkChange.ParkChangeDetails;
import popup.PopUp;
import requestHandler.RequestHandler;
import requestHandler.controllerName;
import subscriber.Subscriber;

public class ParkMangerChangesController {
	Gson gson = new Gson();
	@FXML
	private Text exitBtn;

	@FXML
	private Button saveBtn;

	@FXML
	private TextField parkCapacityTF;

	@FXML
	private Slider parkCapacitySlider;

	@FXML
	private TextField differenceTF;

	@FXML
	private Slider differenceSlider;

	@FXML
	private TextField visitTimeTF;

	@FXML
	private Slider visitingTimeSlider;

	@FXML
	private TextField discountTF;

	@FXML
	private Slider discountSlider;

	@FXML
	private Text helpBtn;

	Employee parkManager;

	@FXML
	void saveParkMangerChanges(ActionEvent event) {

		ParkChangeDetails parkDetails = new ParkChangeDetails(parkCapacityTF.getText(), differenceTF.getText(),
				discountTF.getText(),parkManager.getParkName());

		sendParkManagerChangeRequestToServer("changeParkDetails", parkDetails);
		analyzeAnswerFromServer();

	}

	private void sendParkManagerChangeRequestToServer(String changeParkDetails, ParkChangeDetails parkDetails) {
		RequestHandler rh = new RequestHandler(controllerName.ParkManagerSystemController, changeParkDetails,
				gson.toJson(parkDetails));
		ClientUI.chat.accept(gson.toJson(rh));
	}

	public void initializeSlidersAndSetParkMan(Employee employee) {
		parkCapacitySlider.setMin(300);
		parkCapacitySlider.setMax(500);
		differenceSlider.setMin(0);
		differenceSlider.setMax(50);
		discountSlider.setMin(0);
		discountSlider.setMax(50);
		parkManager = employee;

	}

	@FXML
	void OnDragCapacity(MouseEvent event) {
		parkCapacityTF.setText(String.valueOf((int) parkCapacitySlider.getValue()));

	}

	@FXML
	void OnDragDifference(MouseEvent event) {
		differenceTF.setText(String.valueOf((int) differenceSlider.getValue()));

	}

	@FXML
	void OnDragDiscount(MouseEvent event) {
		discountTF.setText(String.valueOf((int) discountSlider.getValue()));

	}

	@FXML
	void differenceQuestion(MouseEvent event) {

	}

	private void analyzeAnswerFromServer() {
		String answer = ChatClient.serverMsg;
		switch (answer) {
		case "faild":
			PopUp.display("Error", answer);
			break;
		
		default:
			PopUp.display("success", answer);
			break;
		}

	}

	@FXML
	void changeParkCapacitySliderByTxt(InputMethodEvent event) {
		parkCapacitySlider.setValue(Integer.parseInt(parkCapacityTF.getText()));

	}

	@FXML
	void changeParkDifferenceSliderByTxt(InputMethodEvent event) {

	}

	@FXML
	void changeParkDiscountSliderByTxt(InputMethodEvent event) {

	}

}
