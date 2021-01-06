package parkmanagerreports;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import com.google.gson.Gson;

import Reports.ReportData;
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
import monthDetails.Months;
import popup.PopUp;
import requestHandler.RequestHandler;
import requestHandler.controllerName;

/**class that generate and show the monthly revenue report
 * @author zivi9
 *
 */
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

    /**set the information of the park manager and set the combo boxes 
     * @param employee
     */
    public void setParkManager(Employee employee) {
    	int thisyear = Calendar.getInstance().get(Calendar.YEAR);
    	int thismonth=Calendar.getInstance().get(Calendar.MONTH);
    	thismonth++;
    	for(int i=thisyear-7;i<=thisyear;i++)
    		comboYear.getItems().add(String.valueOf(i));
    	comboYear.setValue(String.valueOf(thisyear));
    	for(int j=1;j<13;j++)
    		comboMonth.getItems().add(String.valueOf(j));
    	comboMonth.setValue(String.valueOf(thismonth));
		parkManager = employee;
		parkName.setText(parkManager.getParkName());
		
	}
    
    
    /**go back to the park manager report main page 
     * @param event
     * @throws IOException
     */
    @FXML
    void goBack(MouseEvent event) throws IOException {
    	ParkManagerReportsController controller = FXMLFunctions.loadSceneToMainPane(ParkManagerReportsController.class, "ParkManagerReports.fxml", StaticPaneMainPageParkManager.parkManagerMainPane).getController();
		controller.setComboBoxOptions(parkManager);
    	 

    }
    
    
    
    
    
    /**save the request and call methods that will send it to the server
     * @param event
     */
    @FXML
    void showReport(ActionEvent event) {
    	ReportData data=new ReportData(parkManager.getParkName(), comboYear.getValue(), comboMonth.getValue());   	
    	askTotalVisitorsReportFromServer("RevenueReport", data );

    }
    
    
    
    /**send a request to the server and call methods that will analyze the answer 
     * @param funcname
     * @param data
     */
    private void askTotalVisitorsReportFromServer(String funcname, ReportData data) {
    	RequestHandler rh = new RequestHandler(controllerName.ReportsController, funcname,gson.toJson(data));
		ClientUI.chat.accept(gson.toJson(rh));
		analyzeAnswerFromServer();
		
	
    }
    
    
    /**set the server answer into labels 
     * 
     */
    @SuppressWarnings("unchecked")
	private void analyzeAnswerFromServer() {
	    String answer = ChatClient.serverMsg;
	    if(answer.equals("faild"))
	    	showEmptyDetails();
	    else
	    {
		ArrayList <String> res= gson.fromJson(answer, ArrayList.class);
		singleTF.setText(res.get(0));
		familyTF.setText(res.get(1));
		groupTF.setText(res.get(2));
		totalTF.setText(String.valueOf(Double.parseDouble(res.get(0))+Double.parseDouble(res.get(1))+Double.parseDouble(res.get(2))));
	    }
}
    /**
	 * The data that came from the data base was empty.
	 * Set single,family,group and total as 0.
	 */
	private void showEmptyDetails()
	{
		singleTF.setText("0$");
		familyTF.setText("0$");
		groupTF.setText("0$");
		totalTF.setText("0$");
		PopUp.display("No revenues", "No revenue for park " + parkManager.getParkName() + " at : " + Months.values()[Integer.parseInt(comboMonth.getValue())-1] + " " + comboYear.getValue());
		
	}

}
