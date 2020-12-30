package departmentManagerReports;

import java.io.IOException;
import java.time.LocalDate;

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

public class DepartmentManagerCancelationReportsController {
	Gson gson = new Gson();

    @FXML
    private Text backBtn;

    @FXML
    private DatePicker datePick;

    @FXML
    private ComboBox<String> parkComboBox;

    @FXML
    private Button reportBtn;

    @FXML
    private PieChart pieOne;
    
	private int numberOfPeopleCanceled = 1;
	private int totalReservations = 1; //default
	  @FXML
	    void quitScene(MouseEvent event) throws IOException {
		//  	StaticPaneMainPageDepartmentManager.DepartmentManagerMainPane.getChildren().clear();
		  DepartmentManagerChooseReportController controller = FXMLFunctions.loadSceneToMainPane(DepartmentManagerChooseReportController.class, "DepartmentManagerChooseReport.fxml", StaticPaneMainPageDepartmentManager.DepartmentManagerMainPane).getController();
		  controller.setComboBoxOptions();
	  }
	    @FXML
	    void report(ActionEvent event) {
	    	pieOne.getData().clear();
	    	String year = datePick.getValue().getYear() + "";
	    	String month = datePick.getValue().getMonthValue() + "";
	    	String day = datePick.getValue().getDayOfMonth() + "";
	    	String parkName = parkComboBox.getValue();	    	
	    	getReservationCancelationDetails(year,month,day,parkName);
	    	getNumberOfReservationForSpecificDay(year,month,day,parkName);
	    }
	    
		public void getReservationCancelationDetails(String year,String month,String day,String parkName) {
			String stringToSend = year + " " + month + " " + day + " " + parkName;
 			RequestHandler rh = new RequestHandler(controllerName.DepartmentManagerSystemController, "getReservationCancelationDetails", stringToSend);
			ClientUI.chat.accept(gson.toJson(rh));
			analyzeAnswerFromServer();
		}
		public void getNumberOfReservationForSpecificDay(String year,String month,String day,String parkName) {
			String stringToSend = year + " " + month + " " + day + " " + parkName;
 			RequestHandler rh = new RequestHandler(controllerName.DepartmentManagerSystemController, "getNumberOfReservationForSpecificDay", stringToSend);
			ClientUI.chat.accept(gson.toJson(rh));
			analyzeAnswerFromServer2();
			showDetails();
		}
		
		//analyzeAnswerFromServer
		private void analyzeAnswerFromServer() {
			String answer = ChatClient.serverMsg;
			if(!answer.equals("faild"))
				numberOfPeopleCanceled = Integer.parseInt(answer);
		}
		//analyzeAnswerFromServer
		private void analyzeAnswerFromServer2() {
			String answer = ChatClient.serverMsg;		
			if(!answer.equals("faild"))
				totalReservations =  Integer.parseInt(answer);
		}
		
		private void showDetails()
		{		
					
			if(totalReservations == 0)
			{
				System.out.println("no reservations" + numberOfPeopleCanceled + " " +totalReservations);
				totalReservations = 1;
				//return;
			}
			numberOfPeopleCanceled = 220;	//take from DB
			totalReservations = 450;		//take from DB
			double cancelPrecentage = (double)((double)numberOfPeopleCanceled/totalReservations)*100;
			double totalPrecentage =  (double)(totalReservations -  (float)(numberOfPeopleCanceled/totalReservations));
			PieChart.Data precentageSlice = new PieChart.Data("Number of people canceled =" + numberOfPeopleCanceled + "=>" +  cancelPrecentage + "%", cancelPrecentage);
			PieChart.Data precentageSlice2 = new PieChart.Data("Total reservations =" +  totalPrecentage, 100 - cancelPrecentage);
			pieOne.getData().add(precentageSlice);
			pieOne.getData().add(precentageSlice2);
		}


		public void setTypeComboBoxOptions() {
			parkComboBox.getItems().addAll("Banias","Safari","Niagara");
			datePick.setValue(LocalDate.now());
		}
		
}
