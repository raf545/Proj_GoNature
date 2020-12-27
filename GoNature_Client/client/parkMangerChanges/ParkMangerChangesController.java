package parkMangerChanges;

import java.util.ArrayList;

import com.google.gson.Gson;
import javafx.scene.control.Label;
import client.ChatClient;
import client.ClientUI;
import employee.Employee;
import guiCommon.StaticPaneMainPageDepartmentManager;
import guiCommon.StaticPaneMainPageParkManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import parkChange.ParkChangeDetails;
import popup.PopUp;
import requestHandler.RequestHandler;
import requestHandler.controllerName;

public class ParkMangerChangesController {
	Gson gson = new Gson();
	String oldParkcapacity;
	String oldDifference;
	String oldDiscount;
	@FXML
    private Text backBtn;


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
	private TextField discountTF;

	@FXML
	private Slider discountSlider;

	@FXML
	private Text helpBtn;
	 @FXML
    private Hyperlink returnToDefault;

    @FXML
	private Label parkNameTitle;

	Employee parkManager;

	@FXML
	void saveParkMangerChanges(ActionEvent event) {

		ParkChangeDetails parkDetails = new ParkChangeDetails(parkCapacityTF.getText(), differenceTF.getText(),
				discountTF.getText(),parkManager.getParkName(),oldParkcapacity,oldDifference,oldDiscount);

		sendParkManagerChangeRequestToServer("changeParkDetails", parkDetails);
		analyzeAnswerFromServer();

	}

	private void sendParkManagerChangeRequestToServer(String changeParkDetails, ParkChangeDetails parkDetails) {
		RequestHandler rh = new RequestHandler(controllerName.ParkManagerSystemController, changeParkDetails,
				gson.toJson(parkDetails));
		ClientUI.chat.accept(gson.toJson(rh));
		analyzeAnswerFromServer();
	}

	public void initializeSlidersAndSetParkMan(Employee employee) {
		parkCapacitySlider.setMin(200);
		parkCapacitySlider.setMax(500);
		differenceSlider.setMin(0);
		differenceSlider.setMax(500);
		discountSlider.setMin(0);
		discountSlider.setMax(50);
		parkManager = employee;
		initParkValues();
		parkNameTitle.setText(parkManager.getParkName());
		
		

	}

	private void initParkValues() {
		RequestHandler rh = new RequestHandler(controllerName.ParkManagerSystemController, "initParkValues",parkManager.getParkName());
		ClientUI.chat.accept(gson.toJson(rh));
		analyzeAnswerFromServer();
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
		String answer="available capacity for visitors that didnt make a reservation before they came to the park";
		PopUp.display("difference details", answer);
		

	}

	private void analyzeAnswerFromServer() {
		String answer = ChatClient.serverMsg;
		switch (answer) {
		case "faild":
			PopUp.display("Error", answer);
			break;
		case "The changing are waiting for approval":
			PopUp.display("success", answer);
			break;
			
		default:
			ArrayList <String> res= gson.fromJson(answer, ArrayList.class);
			oldParkcapacity=res.get(0);
			oldDifference=res.get(1);
			oldDiscount=res.get(2);
			parkCapacitySlider.setValue(Integer.valueOf(oldParkcapacity));
			differenceSlider.setValue(Integer.valueOf(oldDifference));
			discountSlider.setValue(Integer.valueOf(oldDiscount));
			differenceTF.setText(String.valueOf((int) differenceSlider.getValue()));
			discountTF.setText(String.valueOf((int) discountSlider.getValue()));
			parkCapacityTF.setText(String.valueOf((int) parkCapacitySlider.getValue()));

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
	
	@FXML
    void retDefultValue(ActionEvent event) {
		parkCapacitySlider.setValue(Integer.valueOf(oldParkcapacity));
		differenceSlider.setValue(Integer.valueOf(oldDifference));
		discountSlider.setValue(Integer.valueOf(oldDiscount));
		differenceTF.setText(String.valueOf((int) differenceSlider.getValue()));
		discountTF.setText(String.valueOf((int) discountSlider.getValue()));
		parkCapacityTF.setText(String.valueOf((int) parkCapacitySlider.getValue()));
    }
	
	 @FXML
	    void backButton(MouseEvent event) {
		 StaticPaneMainPageParkManager.parkManagerMainPane.getChildren().clear();

	    }


}
