package parkmanagerreports;

import java.util.ArrayList;
import java.util.Calendar;

import com.google.gson.Gson;

import Reports.TotalVisitorsReport;
import client.ChatClient;
import client.ClientUI;
import employee.Employee;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import popup.PopUp;
import requestHandler.RequestHandler;
import requestHandler.controllerName;

public class numberOfVisitorsReportController {
	Gson gson = new Gson();
	
	
	
		Employee parkManager;
		
		   @FXML
		    private ComboBox<String> comboYear;

		    @FXML
		    private ComboBox<String> comboMonth;

		    @FXML
		    private TextField singleTF;

		    @FXML
		    private Button showbtn;

		    @FXML
		    private TextField familyTF;

		    @FXML
		    private TextField groupTF;

		    @FXML
		    private TextField totalTF;

	    @FXML
	    void showReport(ActionEvent event) {
	    	TotalVisitorsReport data=new TotalVisitorsReport(parkManager.getParkName(), comboYear.getValue(), comboMonth.getValue());
	    	
	    	
	    	askTotalVisitorsReportFromServer("TotalVisitorsReport", data );

	    }
	    private void askTotalVisitorsReportFromServer(String funcname, TotalVisitorsReport data) {
	    	RequestHandler rh = new RequestHandler(controllerName.ReportsController, funcname,gson.toJson(data));
			ClientUI.chat.accept(gson.toJson(rh));
			analyzeAnswerFromServer();
			
		}
		private void analyzeAnswerFromServer() {
			    String answer = ChatClient.serverMsg;
				ArrayList <String> res= gson.fromJson(answer, ArrayList.class);
				singleTF.setText(res.get(0));
				familyTF.setText(res.get(1));
				groupTF.setText(res.get(2));
				totalTF.setText(String.valueOf(Integer.parseInt(res.get(0))+Integer.parseInt(res.get(1))+Integer.parseInt(res.get(2))));

				
		}
		public void setParkManager(Employee employee) {
	    	int thisyear = Calendar.getInstance().get(Calendar.YEAR);
	    	for(int i=thisyear-7;i<=thisyear;i++)
	    		comboYear.getItems().add(String.valueOf(i));
	    	for(int j=1;j<13;j++)
	    		comboMonth.getItems().add(String.valueOf(j));
			parkManager = employee;
			

		}
	
	
}
