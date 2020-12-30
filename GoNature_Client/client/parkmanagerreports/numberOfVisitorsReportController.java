package parkmanagerreports;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import com.google.gson.Gson;

import Reports.ReportData;
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
		    private Button showbtn;

		    @FXML
		    private Label parkName;

		    @FXML
		    private Label singleLSUN;

		    @FXML
		    private Label familyLSUN;

		    @FXML
		    private Label groupLSUN;

		    @FXML
		    private Label totalLSUN;

		    @FXML
		    private Label singleLMON;

		    @FXML
		    private Label familyLMON;

		    @FXML
		    private Label groupLMON;

		    @FXML
		    private Label totalLMON;

		    @FXML
		    private Label singleLTUES;

		    @FXML
		    private Label familyLTUES;

		    @FXML
		    private Label groupLTUES;

		    @FXML
		    private Label totalLTUES;

		    @FXML
		    private Label singleLWED;

		    @FXML
		    private Label familyLWED;

		    @FXML
		    private Label groupLWED;

		    @FXML
		    private Label totalLWED;

		    @FXML
		    private Label singleLTHU;

		    @FXML
		    private Label familyLTHU;

		    @FXML
		    private Label groupLTHU;

		    @FXML
		    private Label totalLTHU;

		    @FXML
		    private Label singleLFRI;

		    @FXML
		    private Label familyLFRI;

		    @FXML
		    private Label groupLFRI;

		    @FXML
		    private Label totalLFRI;

		    @FXML
		    private Label singleLSAT;

		    @FXML
		    private Label familyLSAT;

		    @FXML
		    private Label groupLSAT;

		    @FXML
		    private Label totalLSAT;

		    @FXML
		    private Label singleLMONTH;

		    @FXML
		    private Label familyLMONTH;

		    @FXML
		    private Label groupLMONTH;

		    @FXML
		    private Label totalLMONTH;
		    


	    @FXML
	    void showReport(ActionEvent event) {
	    	ReportData data=new ReportData(parkManager.getParkName(), comboYear.getValue(), comboMonth.getValue());
	    	
	    	
	    	askTotalVisitorsReportFromServer("TotalVisitorsReport", data );

	    }
	    private void askTotalVisitorsReportFromServer(String funcname, ReportData data) {
	    	RequestHandler rh = new RequestHandler(controllerName.ReportsController, funcname,gson.toJson(data));
			ClientUI.chat.accept(gson.toJson(rh));
			analyzeAnswerFromServer();
			
		}
		private void analyzeAnswerFromServer() {
				int sum=0;
			    String answer = ChatClient.serverMsg;
				int[][] res= gson.fromJson(answer, int[][].class);
				singleLSUN.setText(String.valueOf(res[0][0]));
				singleLMON.setText(String.valueOf(res[1][0]));
				singleLTUES.setText(String.valueOf(res[2][0]));
				singleLWED.setText(String.valueOf(res[3][0]));
				singleLTHU.setText(String.valueOf(res[4][0]));
				singleLFRI.setText(String.valueOf(res[5][0]));
				singleLSAT.setText(String.valueOf(res[6][0]));
				familyLSUN.setText(String.valueOf(res[0][1]));
				familyLMON.setText(String.valueOf(res[1][1]));
				familyLTUES.setText(String.valueOf(res[2][1]));
				familyLWED.setText(String.valueOf(res[3][1]));
				familyLTHU.setText(String.valueOf(res[4][1]));
				familyLFRI.setText(String.valueOf(res[5][1]));
				familyLSAT.setText(String.valueOf(res[6][1]));
				groupLSUN.setText(String.valueOf(res[0][2]));
				groupLMON.setText(String.valueOf(res[1][2]));
				groupLTUES.setText(String.valueOf(res[2][2]));
				groupLWED.setText(String.valueOf(res[3][2]));
				groupLTHU.setText(String.valueOf(res[4][2]));
				groupLFRI.setText(String.valueOf(res[5][2]));
				groupLSAT.setText(String.valueOf(res[6][2]));
				for(int i=0;i<3;i++)
				{
					sum=sum+res[0][i];
				}
				totalLSUN.setText(String.valueOf(sum));
				sum=0;
				for(int i=0;i<3;i++)
				{
					sum=sum+res[1][i];
				}
				totalLMON.setText(String.valueOf(sum));
				sum=0;
				for(int i=0;i<3;i++)
				{
					sum=sum+res[2][i];
				}
				totalLTUES.setText(String.valueOf(sum));
				sum=0;
				for(int i=0;i<3;i++)
				{
					sum=sum+res[3][i];
				}
				totalLWED.setText(String.valueOf(sum));
				sum=0;
				for(int i=0;i<3;i++)
				{
					sum=sum+res[4][i];
				}
				totalLTHU.setText(String.valueOf(sum));
				sum=0;
				for(int i=0;i<3;i++)
				{
					sum=sum+res[5][i];
				}
				totalLFRI.setText(String.valueOf(sum));
				sum=0;
				for(int i=0;i<3;i++)
				{
					sum=sum+res[6][i];
				}
				totalLSAT.setText(String.valueOf(sum));
				sum=0;
				for(int i=0;i<7;i++)
				{
					sum=sum+res[i][0];
				}
				singleLMONTH.setText(String.valueOf(sum));
				sum=0;
				for(int i=0;i<7;i++)
				{
					sum=sum+res[i][1];
				}
				familyLMONTH.setText(String.valueOf(sum));
				sum=0;
				for(int i=0;i<7;i++)
				{
					sum=sum+res[i][2];
				}
				groupLMONTH.setText(String.valueOf(sum));
				sum=0;
				
				sum=Integer.parseInt(groupLMONTH.getText())+Integer.parseInt(familyLMONTH.getText())+Integer.parseInt(singleLMONTH.getText());
				totalLMONTH.setText(String.valueOf(sum));
				sum=0;
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
