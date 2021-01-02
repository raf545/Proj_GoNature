package parkMangerChanges;

import java.util.ArrayList;

import com.google.gson.Gson;
import javafx.scene.control.Label;
import client.ChatClient;
import client.ClientUI;
import employee.Employee;
import guiCommon.StaticPaneMainPageParkManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import parkChange.ParkChangeDetails;
import popup.PopUp;
import requestHandler.RequestHandler;
import requestHandler.controllerName;

/**class that contains all the methods that park manager could edit his park with 
 * @author zivi9
 *
 */
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

	/**save the request and call methods that will send it to the server
	 * @param event
	 */
	@FXML
	void saveParkMangerChanges(ActionEvent event) {

		ParkChangeDetails parkDetails = new ParkChangeDetails(parkCapacityTF.getText(), differenceTF.getText(),
				discountTF.getText(),parkManager.getParkName(),oldParkcapacity,oldDifference,oldDiscount);

		sendParkManagerChangeRequestToServer("changeParkDetails", parkDetails);
		analyzeAnswerFromServer();

	}

	/**send a request to the server and call methods that will analyze the answer
	 * @param changeParkDetails
	 * @param parkDetails
	 */
	private void sendParkManagerChangeRequestToServer(String changeParkDetails, ParkChangeDetails parkDetails) {
		RequestHandler rh = new RequestHandler(controllerName.ParkManagerSystemController, changeParkDetails,
				gson.toJson(parkDetails));
		ClientUI.chat.accept(gson.toJson(rh));
		analyzeAnswerFromServer();
	}

	/**initialize sliders and set the default sliders value by calling methods that send the request to the server 
	 * @param employee
	 */
	public void initializeSlidersAndSetParkManager(Employee employee) {
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

	/**set the default sliders value by request from the server 
	 * 
	 */
	private void initParkValues() {
		RequestHandler rh = new RequestHandler(controllerName.ParkManagerSystemController, "initParkValues",parkManager.getParkName());
		ClientUI.chat.accept(gson.toJson(rh));
		analyzeAnswerFromServer();
	}

	/**set the text fields by the values of the sliders 
	 * @param event
	 */
	@FXML
	void OnDragCapacity(MouseEvent event) {
		parkCapacityTF.setText(String.valueOf((int) parkCapacitySlider.getValue()));

	}

	/**set the text fields by the values of the sliders 
	 * @param event
	 */
	@FXML
	void OnDragDifference(MouseEvent event) {
		differenceTF.setText(String.valueOf((int) differenceSlider.getValue()));

	}

	/**set the text fields by the values of the sliders 
	 * @param event
	 */
	@FXML
	void OnDragDiscount(MouseEvent event) {
		discountTF.setText(String.valueOf((int) discountSlider.getValue()));

	}

	/**set the text fields by the values of the sliders 
	 * @param event
	 */
	@FXML
	void differenceQuestion(MouseEvent event) {
		String answer="available capacity for visitors that didnt make a reservation before they came to the park";
		PopUp.display("difference details", answer);
		

	}

	/**analyze Answers From Server case if its failed / changes success/ set default sliders value 
	 * 
	 */
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

	
	
	
	/**return to the default sliders values 
	 * @param event
	 */
	@FXML
    void retDefultValue(ActionEvent event) {
		parkCapacitySlider.setValue(Integer.valueOf(oldParkcapacity));
		differenceSlider.setValue(Integer.valueOf(oldDifference));
		discountSlider.setValue(Integer.valueOf(oldDiscount));
		differenceTF.setText(String.valueOf((int) differenceSlider.getValue()));
		discountTF.setText(String.valueOf((int) discountSlider.getValue()));
		parkCapacityTF.setText(String.valueOf((int) parkCapacitySlider.getValue()));
    }
	
	 /**go back to park manager main page
	 * @param event
	 */
	@FXML
	    void backButton(MouseEvent event) {
		 StaticPaneMainPageParkManager.parkManagerMainPane.getChildren().clear();

	    }


}
