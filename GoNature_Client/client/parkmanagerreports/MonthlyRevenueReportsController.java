package parkmanagerreports;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import com.google.gson.Gson;

import Reports.TotalVisitorsReport;
import client.ChatClient;
import client.ClientUI;
import employee.Employee;
import fxmlGeneralFunctions.FXMLFunctions;
import guiCommon.StaticPaneMainPageParkManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import requestHandler.RequestHandler;
import requestHandler.controllerName;

public class MonthlyRevenueReportsController {
	Employee parkManager;
	Gson gson = new Gson();
	
	

	    @FXML
	    private Text backBtn;

	    @FXML
	    private Button showBtn;

	    @FXML
	    private TextField totalTF;

	    @FXML
	    private ComboBox<String> comboYear;

	    @FXML
	    private ComboBox<String> comboMonth;

	    @FXML
	    private TextField singleTF;

	    @FXML
	    private TextField familyTF;

	    @FXML
	    private TextField groupTF;
	    
	    @FXML
	    private Label parkName;

    public void setParkManager(Employee employee) {
    	int thisyear = Calendar.getInstance().get(Calendar.YEAR);
    	for(int i=thisyear-7;i<=thisyear;i++)
    		comboYear.getItems().add(String.valueOf(i));
    	for(int j=1;j<13;j++)
    		comboMonth.getItems().add(String.valueOf(j));
			parkManager = employee;
			parkName.setText(parkManager.getParkName());
		
		
	

	}
    
    
    @FXML
    void goBack(MouseEvent event) throws IOException {
    	ParkManagerReportsController controller = FXMLFunctions.loadSceneToMainPane(ParkManagerReportsController.class, "ParkManagerReports.fxml", StaticPaneMainPageParkManager.parkManagerMainPane).getController();
		controller.setComboBoxOptions(parkManager);
    	 

    }
    
    
    
    
    
    @FXML
    void showReport(ActionEvent event) {
    	TotalVisitorsReport data=new TotalVisitorsReport(parkManager.getParkName(), comboYear.getValue(), comboMonth.getValue());
    	
    	
    	askTotalVisitorsReportFromServer("RevenueReport", data );

    }
    
    
    
    private void askTotalVisitorsReportFromServer(String funcname, TotalVisitorsReport data) {
    	RequestHandler rh = new RequestHandler(controllerName.ReportsController, funcname,gson.toJson(data));
		ClientUI.chat.accept(gson.toJson(rh));
		analyzeAnswerFromServer();
		
	
    }
    
    
    @SuppressWarnings("unchecked")
	private void analyzeAnswerFromServer() {
	    String answer = ChatClient.serverMsg;
		ArrayList <String> res= gson.fromJson(answer, ArrayList.class);
		singleTF.setText(res.get(0));
		familyTF.setText(res.get(1));
		groupTF.setText(res.get(2));
		totalTF.setText(String.valueOf(Double.parseDouble(res.get(0))+Double.parseDouble(res.get(1))+Double.parseDouble(res.get(2))));

		
}

}