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
	private int numberOfPeopleHalfCanceled = 1;
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
	    	getReservationHalfCancelationDetails(year,month,day,parkName);
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
		
		public void getReservationHalfCancelationDetails(String year,String month,String day,String parkName) {
			String stringToSend = year + " " + month + " " + day + " " + parkName;
 			RequestHandler rh = new RequestHandler(controllerName.DepartmentManagerSystemController, "getReservationHalfCancelationDetails", stringToSend);
			ClientUI.chat.accept(gson.toJson(rh));
			analyzeAnswerFromServer3();
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
		//analyzeAnswerFromServer
		private void analyzeAnswerFromServer3() {
			String answer = ChatClient.serverMsg;
			if(!answer.equals("faild"))
				numberOfPeopleHalfCanceled = Integer.parseInt(answer);
		}
		
		private void showDetails()
		{		
					
			if(totalReservations == 0)
			{
				showNoReservationsPie();
				return;
			}
			//Turn to precentage and show it as pie
			double cancelPrecentage = (double)((double)numberOfPeopleCanceled/totalReservations)*100;
			double halfCancelationPrecentage = (double)((double)numberOfPeopleHalfCanceled/totalReservations)*100;
			double totalPrecentage =  (double)(100 -  cancelPrecentage - halfCancelationPrecentage);
			
			String s1 = String.format("Number of people canceled = %d => %.2f%%",numberOfPeopleCanceled, cancelPrecentage );
			String s2 = String.format("Number of people half canceled = %d => %.2f%%",numberOfPeopleHalfCanceled, halfCancelationPrecentage );
			
			PieChart.Data precentageSlice = new PieChart.Data(s1, cancelPrecentage);
			PieChart.Data precentageSlice2 = new PieChart.Data(s2, halfCancelationPrecentage);
			PieChart.Data precentageSlice3 = new PieChart.Data("Total reservations = " +  totalReservations, totalPrecentage); //will show the rest
			
			pieOne.getData().add(precentageSlice);
			pieOne.getData().add(precentageSlice2);
			pieOne.getData().add(precentageSlice3);
		}
		
		private void showNoReservationsPie()
		{
			PieChart.Data emptySlice = new PieChart.Data("No Reservations", 1);
			pieOne.getData().add(emptySlice);
		}

		public void setTypeComboBoxOptions() {
			parkComboBox.getItems().addAll("Banias","Safari","Niagara");
			datePick.setValue(LocalDate.now());
			parkComboBox.setValue("Banias");
		}
		
}
