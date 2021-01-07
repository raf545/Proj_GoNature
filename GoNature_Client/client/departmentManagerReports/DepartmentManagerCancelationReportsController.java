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

/**
 * This page will represent all the cancellations and half cancellations in the park
 * compared to the number of reports in a specific date.
 * @author Shay Maryuma
 *
 */
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
	private int numberOfPeopleHalfCanceled = 1;
	private int totalReservations = 1; 		//default value (prevent divide by 0)


	/**
	 * Exit the specific scene, back to main page.
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void quitScene(MouseEvent event) throws IOException {
		DepartmentManagerChooseReportController controller = FXMLFunctions.loadSceneToMainPane(DepartmentManagerChooseReportController.class, "DepartmentManagerChooseReport.fxml", StaticPaneMainPageDepartmentManager.DepartmentManagerMainPane).getController();
		controller.setComboBoxOptions();
	}
	
	/**
	 * Report button, take the data from the combo boxes and send it to the data base with a specific query
	 * that include year,month and the name of the park. 
	 * All the details will be returned as attributes in the controller.
	 * @param event
	 */
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

	/**
	 * Function that will be used in report function
	 * Send details to the server and wait for an answer.
	 * Will return cancellation details to "numberOfPeopleCanceled"
	 * @param year
	 * @param month
	 * @param day
	 * @param parkName
	 */
	
	public void getReservationCancelationDetails(String year,String month,String day,String parkName) {
		String stringToSend = year + " " + month + " " + day + " " + parkName;
		RequestHandler rh = new RequestHandler(controllerName.DepartmentManagerSystemController, "getReservationCancelationDetails", stringToSend);
		ClientUI.chat.accept(gson.toJson(rh));
		analyzeAnswerFromServer();
	}
	/**
	 * Function that will be used in report function
	 * Send details to the server and wait for an answer.
	 * Will return cancellation details to "totalReservations"
	 * @param year
	 * @param month
	 * @param day
	 * @param parkName
	 */
	public void getNumberOfReservationForSpecificDay(String year,String month,String day,String parkName) {
		String stringToSend = year + " " + month + " " + day + " " + parkName;
		RequestHandler rh = new RequestHandler(controllerName.DepartmentManagerSystemController, "getNumberOfReservationForSpecificDay", stringToSend);
		ClientUI.chat.accept(gson.toJson(rh));
		analyzeAnswerFromServer2();
		showDetails();
	}
	/**
	 * Function that will be used in report function
	 * Send details to the server and wait for an answer.
	 * Will return cancellation details to "numberOfPeopleHalfCanceled"
	 * @param year
	 * @param month
	 * @param day
	 * @param parkName
	 */
	public void getReservationHalfCancelationDetails(String year,String month,String day,String parkName) {
		String stringToSend = year + " " + month + " " + day + " " + parkName;
		RequestHandler rh = new RequestHandler(controllerName.DepartmentManagerSystemController, "getReservationHalfCancelationDetails", stringToSend);
		ClientUI.chat.accept(gson.toJson(rh));
		analyzeAnswerFromServer3();
	}

	/**
	 * After communicating with the server we expect to get an answer
	 * The answer for "getReservationCancelationDetails" will be here.
	 */
	private void analyzeAnswerFromServer() {
		String answer = ChatClient.serverMsg;
		if(!answer.equals("faild"))
			numberOfPeopleCanceled = Integer.parseInt(answer);
	}
	/**
	 * After communicating with the server we expect to get an answer
	 * The answer for "getNumberOfReservationForSpecificDay" will be here.
	 */
	private void analyzeAnswerFromServer2() {
		String answer = ChatClient.serverMsg;		
		if(!answer.equals("faild"))
			totalReservations =  Integer.parseInt(answer);
	}
	/**
	 * After communicating with the server we expect to get an answer
	 * The answer for "getReservationHalfCancelationDetails" will be here.
	 */
	private void analyzeAnswerFromServer3() {
		String answer = ChatClient.serverMsg;
		if(!answer.equals("faild"))
			numberOfPeopleHalfCanceled = Integer.parseInt(answer);
	}

	/**
	 * showDetails will work with the details that came from the DB and calculate the percentage of each detail.
	 * The details will be shown as pie chart that include : Cancelled reservations, Half Cancelled reservations and Total Reservations.
	 */
	private void showDetails()
	{		
		//prevent divide by 0
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
	
	/**
	 * Show a clean pie with no reservations.
	 */
	private void showNoReservationsPie()
	{
		PieChart.Data emptySlice = new PieChart.Data("No Reservations", 1);
		pieOne.getData().add(emptySlice);
	}

	/**
	 * A function that must be called when loading the screen
	 * set the combo box options and value.
	 */
	public void setTypeComboBoxOptions() {
		parkComboBox.getItems().addAll("Banias","Safari","Niagara");
		datePick.setValue(LocalDate.now());
		parkComboBox.setValue("Banias");
	}

}
