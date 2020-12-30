package parkmanagerreports;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import com.google.gson.Gson;

import Reports.TotalVisitorsReport;
import client.ChatClient;
import client.ClientUI;
import departmentManagerReports.DepartmentManagerChooseReportController;
import employee.Employee;
import fxmlGeneralFunctions.FXMLFunctions;
import guiCommon.StaticPaneMainPageDepartmentManager;
import guiCommon.StaticPaneMainPageParkManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import parkManager.MainPageParkManagerController;
import popup.PopUp;
import requestHandler.RequestHandler;
import requestHandler.controllerName;

public class numberOfVisitorsReportController {
	Gson gson = new Gson();
	
	
	
		Employee parkManager;
		
			@FXML
			private Text backbtn;
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
		    private TextField singleTF1;

		    @FXML
		    private TextField familyTF1;

		    @FXML
		    private TextField groupTF1;

		    @FXML
		    private TextField totalTF1;

		    @FXML
		    private TextField singleTF2;

		    @FXML
		    private TextField familyTF2;

		    @FXML
		    private TextField groupTF2;

		    @FXML
		    private TextField totalTF2;

		    @FXML
		    private TextField singleTF3;

		    @FXML
		    private TextField familyTF3;

		    @FXML
		    private TextField groupTF3;

		    @FXML
		    private TextField totalTF3;

		    @FXML
		    private TextField singleTF4;

		    @FXML
		    private TextField familyTF4;

		    @FXML
		    private TextField groupTF4;

		    @FXML
		    private TextField totalTF4;

		    @FXML
		    private TextField singleTF5;

		    @FXML
		    private TextField familyTF5;

		    @FXML
		    private TextField groupTF5;

		    @FXML
		    private TextField totalTF5;

		    @FXML
		    private TextField singleTF6;

		    @FXML
		    private TextField familyTF6;

		    @FXML
		    private TextField groupTF6;

		    @FXML
		    private TextField totalTF6;

		    @FXML
		    private Label parkName;

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
			parkName.setText(parkManager.getParkName());
			

		}
		 @FXML
		    void goBack(MouseEvent event) throws IOException {
			ParkManagerReportsController controller = FXMLFunctions.loadSceneToMainPane(ParkManagerReportsController.class, "ParkManagerReports.fxml", StaticPaneMainPageParkManager.parkManagerMainPane).getController();
			controller.setComboBoxOptions(parkManager);
		    }
		  
}
