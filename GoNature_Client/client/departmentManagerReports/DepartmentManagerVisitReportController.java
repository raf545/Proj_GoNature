package departmentManagerReports;

import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;

import client.ChatClient;
import client.ClientUI;
import fxmlGeneralFunctions.FXMLFunctions;
import guiCommon.StaticPaneMainPageDepartmentManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import requestHandler.RequestHandler;
import requestHandler.controllerName;

public class DepartmentManagerVisitReportController {
	Gson gson = new Gson();
	private int myChart = 0;
    @FXML
    private Text backBtn;

    @FXML
    private DatePicker datePick;

    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private Button reportButton;

    @FXML
    private PieChart pieOne;

    @FXML
    private PieChart pieTwo;
    
	  @FXML
	    void quitScene(MouseEvent event) throws IOException {
		  //	StaticPaneMainPageDepartmentManager.DepartmentManagerMainPane.getChildren().clear();
		  DepartmentManagerChooseReportController controller = FXMLFunctions.loadSceneToMainPane(DepartmentManagerChooseReportController.class, "DepartmentManagerChooseReport.fxml", StaticPaneMainPageDepartmentManager.DepartmentManagerMainPane).getController();
		  controller.setComboBoxOptions();
	  }
	    @FXML
	    void report(ActionEvent event) {

	    	myChart = 0;
	    	pieOne.getData().clear();
	    	pieTwo.getData().clear();
//	    	String year = datePick.getValue().getYear() + "";
//	    	String month = datePick.getValue().getMonth() + "";
//	    	String day = datePick.getValue().getDayOfMonth() + "";
//	    	String type = typeComboBox.getValue();
	    	String year = "2020";
	    	String month = "12";
	    	String day = "6";
	    	String type = "family";
	    	getEntryDetailsByHours(type,year,month,day);
	    	getExitDetailsByHours(type,year,month,day);

	    }
		public void getEntryDetailsByHours(String type,String year,String month,String day) {
			String stringToSend = type + " " + year + " " + month + " " + day;
 			RequestHandler rh = new RequestHandler(controllerName.DepartmentManagerSystemController, "getEntryDetailsByHours", stringToSend);
			ClientUI.chat.accept(gson.toJson(rh));
			analyzeAnswerFromServer();
		}
		
		public void getExitDetailsByHours(String type,String year,String month,String day) {
			String stringToSend = type + " " + year + " " + month + " " + day;
 			RequestHandler rh = new RequestHandler(controllerName.DepartmentManagerSystemController, "getExitDetailsByHours", stringToSend);
			ClientUI.chat.accept(gson.toJson(rh));
			analyzeAnswerFromServer();
		}
		
		//analyzeAnswerFromServer
		private void analyzeAnswerFromServer() {
			String answer = ChatClient.serverMsg;		
			if(!answer.equals("faild"))
				showDetails(answer);
		}	

		
		private void showDetails(String answer)
		{
			ArrayList<String[]> details = new ArrayList<String[]>();
			String[] tables = answer.split(" ");

			for (String s : tables) 
				details.add(s.split(","));
						
			showPieChart(details);
		}
		
		private void showPieChart(ArrayList<String[]> details)
		{
	    	PieChart.Data slices[] = new PieChart.Data[details.size()];
	    	String hour;
	    	String numberOfPeople;
	    	
	    	PieChart myPie = (myChart == 0)?pieOne:pieTwo;
	    	myChart = 1;
	    	for (int i = 0; i < slices.length; i++) 
	    	{
	    		hour = details.get(i)[0];
	    		numberOfPeople =  details.get(i)[1];
	    		slices[i] = new PieChart.Data( "Hour: " + hour, Integer.parseInt(numberOfPeople));				
	    		myPie.getData().add(slices[i]);
	    	}
		}
		
		public void setTypeComboBoxOptions() {

			typeComboBox.getItems().addAll("Group","Family","Subscriber","Guest");
		}

}
