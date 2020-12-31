package departmentManagerReports;

import java.util.ArrayList;
import java.util.Calendar;

import com.google.gson.Gson;

import Reports.ReportData;
import client.ChatClient;
import client.ClientUI;
import employee.Employee;
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
    
    @FXML
    void goBack(MouseEvent event) {

    }

    @FXML
    void showReport(ActionEvent event) {
    	String year = comboYear.getValue() + "";
    	String month = comboMonth.getValue() + "";
    	String parkName = comboPark.getValue();	
    	getMonthlyRevenueFromDB(year,month,parkName);

    }
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
	
	public void getMonthlyRevenueFromDB(String year,String month,String parkName) {
		String stringToSend = year + " " + month + " " + parkName;
			RequestHandler rh = new RequestHandler(controllerName.DepartmentManagerSystemController, "getMonthlyRevenueFromDB", stringToSend);
		ClientUI.chat.accept(gson.toJson(rh));
		analyzeAnswerFromServer();
	}
	
	//analyzeAnswerFromServer
	private void analyzeAnswerFromServer() {
		String answer = ChatClient.serverMsg;		
		if(!answer.equals("faild"))
			showDetails(answer);
		else showEmptyDetails();
	}
	private void showEmptyDetails()
	{
		singleTF.setText("0");
		familyTF.setText("0");
		groupTF.setText("0");
		totalTF.setText("0");
	}
	private void showDetails(String answer)
	{
		String[] details = answer.split(" ");
		int totalPrice = Integer.parseInt(details[0]) + Integer.parseInt(details[1]) + Integer.parseInt(details[2]);
		singleTF.setText(details[0]);
		familyTF.setText(details[1]);
		groupTF.setText(details[2]);
		totalTF.setText(totalPrice + "");				
	}

}
