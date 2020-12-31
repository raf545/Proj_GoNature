package departmentManagerReports;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
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
import javafx.scene.control.CheckBox;
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
    private CheckBox baniasCB;

    @FXML
    private CheckBox safariCB;

    @FXML
    private CheckBox niagaraCB;

    @FXML
    private Button selectAllBtn;
    
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
	    	String year = datePick.getValue().getYear() + "";
	    	String month = datePick.getValue().getMonthValue() + "";
	    	String day = datePick.getValue().getDayOfMonth() + "";
	    	
	    	String type = typeComboBox.getValue();
	    	System.out.println(type);
	    	//String type = "instructor";
	    	String park1,park2,park3;
	       	park1 = (niagaraCB.isSelected())?"Niagara":"noPark";
	       	park2 = (baniasCB.isSelected())?"Banias":"noPark";
	       	park3 = (safariCB.isSelected())?"Safari":"noPark";
	       	if(park1 == park2 && park2 == park3 && park3 == "noPark")
	       	{
	       		showEmptyPark();
	       		showEmptyPark();
	       		return;
	       	}
	    	getEntryDetailsByHours(type,year,month,day,park1,park2,park3);
	    	getExitDetailsByHours(type,year,month,day,park1,park2,park3);

	    }
		public void getEntryDetailsByHours(String type,String year,String month,String day,String park1,String park2,String park3) {
			String stringToSend = type + " " + year + " " + month + " " + day + " " + park1 + " " + park2 + " " + park3;
 			RequestHandler rh = new RequestHandler(controllerName.DepartmentManagerSystemController, "getEntryDetailsByHours", stringToSend);
			ClientUI.chat.accept(gson.toJson(rh));
			analyzeAnswerFromServer();
		}
		
		public void getExitDetailsByHours(String type,String year,String month,String day,String park1,String park2,String park3) {
			String stringToSend = type + " " + year + " " + month + " " + day + " " + park1 + " " + park2 + " " + park3;
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
			if(!answer.contains(","))
			{
				showEmptyPark();
				return;
			}
			
			ArrayList<String[]> details = new ArrayList<String[]>();
			String[] tables = answer.split(" ");

			for (String s : tables) 
				details.add(s.split(","));
			
			showPieChart(details);
		}
		private void showEmptyPark()
		{
			PieChart.Data emptySlice = new PieChart.Data("No Activities", 1);
			PieChart myPie = (myChart == 0)?pieOne:pieTwo;
			myChart = 1;
			myPie.getData().add(emptySlice);
		}
		private void showPieChart(ArrayList<String[]> details)
		{
	    	PieChart.Data slices[] = new PieChart.Data[details.size()];
	    	String hour;
	    	int numberOfPeople;
	    	
	    	PieChart myPie = (myChart == 0)?pieOne:pieTwo;
	    	myChart = 1;
	    	for (int i = 0; i < slices.length; i++) 
	    	{
	    		hour = details.get(i)[0];
	    		numberOfPeople = Integer.parseInt(details.get(i)[1]);
	    		slices[i] = new PieChart.Data( "Hour: " + getHourTemplate(hour) + " - " + numberOfPeople , numberOfPeople);				
	    		myPie.getData().add(slices[i]);
	    	}
		}
		
		public void setTypeComboBoxOptions() {

			typeComboBox.getItems().addAll("instructor","family","subscriber","guest");
			datePick.setValue(LocalDate.now());
			typeComboBox.setValue("subscriber");
		}
		
		public String getHourTemplate(String hour)
		{
			return (Integer.parseInt(hour) < 10)? String.format("0%s:00", hour):String.format("%s:00", hour);
		}
		
	    @FXML
	    void selectAllCheckBoxes(ActionEvent event) {
	    	safariCB.setSelected(true);
	    	baniasCB.setSelected(true);
	    	niagaraCB.setSelected(true);
	    }
}
