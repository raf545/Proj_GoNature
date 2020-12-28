package cardReader;

import com.google.gson.Gson;

import client.ClientUI;
import guiCommon.StaticPaneMainPageClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import popup.PopUp;
import requestHandler.RequestHandler;
import requestHandler.controllerName;

public class CardReaderController {

	Gson gson = new Gson();
    @FXML
    private Text backBtn;

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
    void back(MouseEvent event) {
    	StaticPaneMainPageClient.clientMainPane.getChildren().clear();
    }

    @FXML
    void enterPark(ActionEvent event) {
    	
    	if(enterTxt.getText().isEmpty()) {
    		PopUp.display("Error", "Must Entern an ID");
    	}
    	else {
    		RequestHandler rh = new RequestHandler(controllerName.CardReaderController, "enterPark", enterTxt.getText());
			ClientUI.chat.accept(gson.toJson(rh));
    	}
    	
    }

    @FXML
    void exitPark(ActionEvent event) {

    	if(exitTxt.getText().isEmpty()) {
    		PopUp.display("Error", "Must Enter an ID");
    	}
    	else {
    		
    	}
    }

}
