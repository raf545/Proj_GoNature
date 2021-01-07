package departmentManagerReports;

import java.io.IOException;
import java.time.LocalDate;
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

/**
 * All the visit reports of the park will be shown in this page.
 * @author Shay Maryuma
 */
public class DepartmentManagerVisitReportController {

	Gson gson = new Gson();

	private int myChart = 0; //Will determine if we are handling the first chart or the second (prevent retyping code).
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

	/**
	 * Load the last window
	 * @param event 
	 * @throws IOException
	 */
	@FXML
	void quitScene(MouseEvent event) throws IOException {
		DepartmentManagerChooseReportController controller = FXMLFunctions.loadSceneToMainPane(DepartmentManagerChooseReportController.class, "DepartmentManagerChooseReport.fxml", StaticPaneMainPageDepartmentManager.DepartmentManagerMainPane).getController();
		controller.setComboBoxOptions();
	}
	
	/**
	 * Get the date from the datePicker and the Type from the comboBox
	 * and with that data get the entry and exit details from the DB
	 * in case that the user selected no parks, it will show an empty park report pie.
	 * @param event
	 */
	@FXML
	void report(ActionEvent event) {

		myChart = 0;
		pieOne.getData().clear();
		pieTwo.getData().clear();
		String year = datePick.getValue().getYear() + "";
		String month = datePick.getValue().getMonthValue() + "";
		String day = datePick.getValue().getDayOfMonth() + "";

		String type = typeComboBox.getValue();
		String park1,park2,park3;
		park1 = (niagaraCB.isSelected())?"Niagara":"noPark";
		park2 = (baniasCB.isSelected())?"Banias":"noPark";
		park3 = (safariCB.isSelected())?"Safari":"noPark";
		if(park1 == park2 && park2 == park3 && park3 == "noPark")
		{
			showEmptyPark(); // show Entry pie
			showEmptyPark(); // show Exit pie
			return;
		}
		getEntryDetailsByHours(type,year,month,day,park1,park2,park3);
		getExitDetailsByHours(type,year,month,day,park1,park2,park3);

	}
	
	/**
	 * Function that will get the visit report splitted to hours.
	 * For instance, at 8:00 there was 10 people that entered the park
	 * Send details to the server and wait for an answer.
	 * @param type Instructor/Family/Subscriber/Guest
	 * @param year
	 * @param month
	 * @param day
	 * @param park1 Option 1: for example "Banias"
	 * @param park2 Option 2: for example "Niagara"
	 * @param park3 Option 3: for example "Safari"
	 */
	public void getEntryDetailsByHours(String type,String year,String month,String day,String park1,String park2,String park3) {
		String stringToSend = type + " " + year + " " + month + " " + day + " " + park1 + " " + park2 + " " + park3;
		RequestHandler rh = new RequestHandler(controllerName.DepartmentManagerSystemController, "getEntryDetailsByHours", stringToSend);
		ClientUI.chat.accept(gson.toJson(rh));
		analyzeAnswerFromServer();
	}
	/**
	 * Function that will get the visit report splitted to hours.
	 * For instance, at 8:00 there was 10 people that exited the park
	 * Send details to the server and wait for an answer.
	 * @param type Instructor/Family/Subscriber/Guest
	 * @param year
	 * @param month
	 * @param day
	 * @param park1 Option 1: for example "Banias"
	 * @param park2 Option 2: for example "Niagara"
	 * @param park3 Option 3: for example "Safari"
	 */
	public void getExitDetailsByHours(String type,String year,String month,String day,String park1,String park2,String park3) {
		String stringToSend = type + " " + year + " " + month + " " + day + " " + park1 + " " + park2 + " " + park3;
		RequestHandler rh = new RequestHandler(controllerName.DepartmentManagerSystemController, "getExitDetailsByHours", stringToSend);
		ClientUI.chat.accept(gson.toJson(rh));
		analyzeAnswerFromServer();
	}

	/**
	 * Get the details back from the query that sent to the db and continue to showDetails() if success.
	 */
	private void analyzeAnswerFromServer() {
		String answer = ChatClient.serverMsg;		
		if(!answer.equals("faild"))
			showDetails(answer);
	}	

	/**
	 * Show details of entering and exiting the park of specific day.
	 * The details will be shown as a pie chart.
	 * @param answer will include all the details that came back from the query as String. 
	 * The answer will be split with " ".
	 */
	private void showDetails(String answer)
	{
		if(!answer.contains(","))
		{
			showEmptyPark();
			return;
		}
		//The data that came back from the query separated with " "
		//Each Row separated with ","
		ArrayList<String[]> details = new ArrayList<String[]>();
		String[] tables = answer.split(" ");

		for (String s : tables) 
			details.add(s.split(","));

		showPieChart(details);
	}
	
	/**
	 * In case of no activity, show this empty pie chart.
	 */
	private void showEmptyPark()
	{
		PieChart.Data emptySlice = new PieChart.Data("No Activities", 1);
		PieChart myPie = (myChart == 0)?pieOne:pieTwo;
		myChart = 1;
		myPie.getData().add(emptySlice);
	}
	
	/**
	 * Represent the pie chart to the user.
	 * @param details will include all the details about the entering and exiting the park.
	 */
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
	/**
	 * A function that must be called when loading the screen
	 * set the combo box options and value.
	 */
	public void setTypeComboBoxOptions() {

		typeComboBox.getItems().addAll("instructor","family","subscriber","guest");
		datePick.setValue(LocalDate.now());
		typeComboBox.setValue("subscriber");
	}

	/**
	 * Given a number, return the way it should look as an hour.
	 * @param hour = '8'
	 * @return '08:00'
	 */
	public String getHourTemplate(String hour)
	{
		return (Integer.parseInt(hour) < 10)? String.format("0%s:00", hour):String.format("%s:00", hour);
	}

	/**
	 * By clicking that button, all parks will be chosen to show to the user.
	 * If the user marked all, the query will be applied for each park.
	 * @param event
	 */
	@FXML
	void selectAllCheckBoxes(ActionEvent event) {
		safariCB.setSelected(true);
		baniasCB.setSelected(true);
		niagaraCB.setSelected(true);
	}
}
