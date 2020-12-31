package departmentManagerReports;

import java.util.ArrayList;
import java.util.Calendar;

import com.google.gson.Gson;

import Reports.CapacityData;
import client.ChatClient;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import requestHandler.RequestHandler;
import requestHandler.controllerName;

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
    

    
    

    @FXML
    void goBack(MouseEvent event) {

    }

    @FXML
    void showReport(ActionEvent event) {
    	String year = comboYear.getValue() + "";
    	String month = comboMonth.getValue() + "";
    	String parkName = comboPark.getValue();	
    	getParkManagerCapacityReport(year,month,parkName);
   
    }

	public void getParkManagerCapacityReport(String year,String month,String parkName) {
		listview.getItems().clear();
		String stringToSend = year + " " + month + " " + parkName;
			RequestHandler rh = new RequestHandler(controllerName.DepartmentManagerSystemController, "getParkManagerCapacityReport", stringToSend);
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

		ArrayList<String[]> al = new ArrayList<String[]>();
		String[] tables = answer.split(",");
		for (String string : tables) 
			al.add(string.split(" "));
		
		for(int i = 0; i < al.size(); i++)
			listview.getItems().add(new CapacityData(al.get(i)[0],al.get(i)[3]));
		
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
	

}
