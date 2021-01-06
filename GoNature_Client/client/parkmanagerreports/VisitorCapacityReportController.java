package parkmanagerreports;

import java.io.IOException;
import java.util.Calendar;

import com.google.gson.Gson;

import Reports.CapacityData;
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
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import monthDetails.Months;
import popup.PopUp;
import requestHandler.RequestHandler;
import requestHandler.controllerName;

/**class that generate and show the visitor capacity report
 * @author zivi9
 *
 */
public class VisitorCapacityReportController {
	Employee parkManager;
	Gson gson = new Gson();
	 @FXML
	private Label parkName;

    @FXML
    private Text backBtn;

    @FXML
    private ComboBox<String> comboYear;

    @FXML
    private ComboBox<String> comboMonth;

    @FXML
    private Button showbtn;
    
    @FXML
    private ListView<CapacityData> listview;

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
    	
    	listview.getItems().clear();
		ReportData data=new ReportData(parkManager.getParkName(), comboYear.getValue(), comboMonth.getValue());
	    askVisitorCapacityReportFromServer("VisitorCapacityReport", data );
		
    	
    }
    /**send a request to the server and call methods that will analyze the answer
     * @param funcname
     * @param data
     */
    private void askVisitorCapacityReportFromServer(String funcname, ReportData data) {
    	RequestHandler rh = new RequestHandler(controllerName.ReportsController, funcname,gson.toJson(data));
		ClientUI.chat.accept(gson.toJson(rh));
		analyzeAnswerFromServer();
		
	}
    

	/**set the server answer into list view
	 * 
	 */
	private void analyzeAnswerFromServer() {
		 String answer = ChatClient.serverMsg;
		 if(answer.equals("faild"))
			 PopUp.display("No capacity reports", "The park " + parkManager.getParkName() + " was fully booked at : " + Months.values()[Integer.parseInt(comboMonth.getValue())-1] + " " + comboYear.getValue());
		 else
		 {
		 CapacityData [] data= new  CapacityData [30];
		 String[][] res= gson.fromJson(answer,String[][].class);
		 for (int i = 0; i < 30; i++) {
			 	data[i]=new CapacityData(res[i][0], res[i][1]);

		}
		
		 for (int i = 0; i < 30; i++) {
			 if(!data[i].getDate().equals(""))
				 listview.getItems().add(data[i]);
			
		}
		 
		 }
			
		
	}

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

}
