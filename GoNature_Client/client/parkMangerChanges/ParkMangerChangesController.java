package parkMangerChanges;

import com.google.gson.Gson;

import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
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
    private TextField DiscountTF;

    @FXML
    private Slider discountSlider;

    @FXML
    private Text helpBtn;

    @FXML
    void saveParkMangerChanges(ActionEvent event) {
    
		

    	sendParkManagerChangeRequestToServer("changeParkDetails", null);
			//analyzeAnswerFromServer();
		

	}
    
    private void sendParkManagerChangeRequestToServer(String loginType, Subscriber subscriber) {
		RequestHandler rh = new RequestHandler(controllerName.EmployeeSystemController, loginType,
				gson.toJson(subscriber));
		ClientUI.chat.accept(gson.toJson(rh));
	}

    
    
    public void initializeSliders () {
    	parkCapacitySlider.setMin(0);
    	parkCapacitySlider.setMax(1000);
    	differenceSlider.setMin(0);
    	differenceSlider.setMax(500);
    	visitingTimeSlider.setMin(1);
    	visitingTimeSlider.setMax(12);
    	discountSlider.setMin(0);
    	discountSlider.setMax(100);
    	
    }

}
