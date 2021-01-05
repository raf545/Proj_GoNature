package departmentManagerReports;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import com.google.gson.Gson;
import Reports.CapacityData;
import client.ChatClient;
import client.ClientUI;
import fxmlGeneralFunctions.FXMLFunctions;
import guiCommon.StaticPaneMainPageDepartmentManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import monthDetails.Months;
import popup.PopUp;
import requestHandler.RequestHandler;
import requestHandler.controllerName;

/**
 * All the visit capacity reports of the park will be shown in this page.
 * @author Shay Maryuma
 */
public class DepartmentManagerVisitorCapacityController {

	Gson gson = new Gson();
	
    @FXML
    private Text backBtn;

    @FXML
    private ComboBox<String> comboYear;

    @FXML
    private ComboBox<String> comboMonth;

    @FXML
    private ComboBox<String> comboPark;
    
    @FXML
    private Button showbtn;

    @FXML
    private ListView<CapacityData> listview;
    
    /**
     * Load the last window
     * @param event 
     * @throws IOException
     */
    @FXML
    void goBack(MouseEvent event) throws IOException {
    	DepartmentManagerChooseParkManReportController controller = FXMLFunctions.loadSceneToMainPane(DepartmentManagerChooseParkManReportController.class, "DepartmentManagerChooseParkManReport.fxml" ,StaticPaneMainPageDepartmentManager.DepartmentManagerMainPane).getController();
    	controller.setComboBoxOptions();
    }
    
    /**
     * Get the details from the combo boxes that the user chose and send a query to the DB.
     * @param event
     */
    @FXML
    void showReport(ActionEvent event) {
    	String year = comboYear.getValue() + "";
    	String month = comboMonth.getValue() + "";
    	String parkName = comboPark.getValue();	
    	getParkManagerCapacityReport(year,month,parkName);
   
    }
	/**
	 * Function that will get the park manager capacity details
	 * Send details to the server and wait for an answer.
	 * @param year
	 * @param month
	 * @param parkName
	 */
	public void getParkManagerCapacityReport(String year,String month,String parkName) {
		listview.getItems().clear();
		String stringToSend = year + " " + month + " " + parkName;
		RequestHandler rh = new RequestHandler(controllerName.DepartmentManagerSystemController, "getParkManagerCapacityReport", stringToSend);
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
		else PopUp.display("No data", "No data for park " + comboPark.getValue() + " in : " + Months.values()[Integer.parseInt(comboMonth.getValue())-1] + " " + comboYear.getValue());
	
	}
	/**
	 * Add the details from the db into a listView
	 * The details will be shown as CapacityData.toString() template.
	 * @param answer will include all the details that came back from the query as String. 
	 * The answer will be split with " ".
	 */
	private void showDetails(String answer)
	{

		ArrayList<String[]> al = new ArrayList<String[]>();
		String[] tables = answer.split(",");
		for (String string : tables) 
			al.add(string.split(" "));
		
		for(int i = 0; i < al.size(); i++)
			listview.getItems().add(new CapacityData(al.get(i)[0],al.get(i)[3]));

	}
	/**
	 * A function that must be called when loading the screen
	 * set the combo box options and value.
	 */
    public void setComboBoxes() {
    	int thisYear = Calendar.getInstance().get(Calendar.YEAR);
    	int thisMonth=Calendar.getInstance().get(Calendar.MONTH);
    	
    	for(int i= thisYear-7; i<=thisYear ;i++)
    		comboYear.getItems().add(String.valueOf(i));
    	
    	
    	for(int j=1;j<13;j++)
    		comboMonth.getItems().add(String.valueOf(j));

    	
    	comboPark.getItems().addAll("Banias","Niagara","Safari");
    	
    	comboYear.setValue(String.valueOf(thisYear));
    	comboMonth.setValue(String.valueOf(thisMonth)+1);
    	comboPark.setValue("Banias");
		
	}
}
