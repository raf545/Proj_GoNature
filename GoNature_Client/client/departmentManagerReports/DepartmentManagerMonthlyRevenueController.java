package departmentManagerReports;

import java.io.IOException;
import java.util.Calendar;
import com.google.gson.Gson;
import client.ChatClient;
import client.ClientUI;
import fxmlGeneralFunctions.FXMLFunctions;
import guiCommon.StaticPaneMainPageDepartmentManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import requestHandler.RequestHandler;
import requestHandler.controllerName;


/**
 * All the revenues of the park will be shown in this page.
 * @author Shay Maryuma
 */
public class DepartmentManagerMonthlyRevenueController {

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

    @FXML
    private ComboBox<String> comboPark;
    
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
    	getMonthlyRevenueFromDB(year,month,parkName);

    }
	
	/**
	 * Function that will get the revenue from the DB
	 * Send details to the server and wait for an answer.
	 * @param year
	 * @param month
	 * @param parkName
	 */
	public void getMonthlyRevenueFromDB(String year,String month,String parkName) {
		String stringToSend = year + " " + month + " " + parkName;
			RequestHandler rh = new RequestHandler(controllerName.DepartmentManagerSystemController, "getMonthlyRevenueFromDB", stringToSend);
		ClientUI.chat.accept(gson.toJson(rh));
		analyzeAnswerFromServer();
	}
	
	
	/**
	 * Get the details back from the query that sent to the db and continue to showDetails() if success.
	 * In case of fail, continue to showEmptyDetails().
	 */
	private void analyzeAnswerFromServer() {
		String answer = ChatClient.serverMsg;		
		if(!answer.equals("faild"))
			showDetails(answer);
		else showEmptyDetails();
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
	}
	
	/**
	 * Update the text field with the data that came from the query.
	 * The text fields are single,family,group and total.
	 * @param answer will include all the details that came back from the query as String. 
	 * The answer will be split with " ".
	 */
	private void showDetails(String answer)
	{
		String[] details = answer.split(" ");
		int totalPrice = Integer.parseInt(details[0]) + Integer.parseInt(details[1]) + Integer.parseInt(details[2]);
		singleTF.setText(details[0] + "$");
		familyTF.setText(details[1]+ "$");
		groupTF.setText(details[2]+ "$");
		totalTF.setText(totalPrice + "$");				
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
    	comboMonth.setValue(String.valueOf(thisMonth));
    	comboPark.setValue("Banias");
		
	}
}
