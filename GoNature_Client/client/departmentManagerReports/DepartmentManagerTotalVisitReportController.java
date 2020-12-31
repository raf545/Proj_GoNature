package departmentManagerReports;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;

import com.google.gson.Gson;

import client.ChatClient;
import client.ClientUI;
import fxmlGeneralFunctions.FXMLFunctions;
import guiCommon.StaticPaneMainPageDepartmentManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import requestHandler.RequestHandler;
import requestHandler.controllerName;

public class DepartmentManagerTotalVisitReportController {

	Gson gson = new Gson();
	
	private final int yearExpansion = 5;
	private final int fromYear = 2020;
    @FXML
    private Text backbtn;

    @FXML
    private ComboBox<Integer> comboYear;

    @FXML
    private ComboBox<Integer> comboMonth;

    @FXML
    private ComboBox<String> parkComboBox;
    
    @FXML
    private Button showbtn;

    @FXML
    private TableView<ReportListObject> mainTable;

    @FXML
    private TableColumn<ReportListObject, String> typeCol;

    @FXML
    private TableColumn<ReportListObject, String> sundayCol;

    @FXML
    private TableColumn<ReportListObject, String> mondayCol;

    @FXML
    private TableColumn<ReportListObject, String> tuesdayCol;

    @FXML
    private TableColumn<ReportListObject, String> wednesdayCol;

    @FXML
    private TableColumn<ReportListObject, String> thursdayCol;

    @FXML
    private TableColumn<ReportListObject, String> fridayCol;

    @FXML
    private TableColumn<ReportListObject, String> saturdayCol;

    @FXML
    private TableColumn<ReportListObject, String> monthlyCol;

    @FXML
    void goBack(MouseEvent event) throws IOException {
    	  DepartmentManagerChooseParkManReportController controller = FXMLFunctions.loadSceneToMainPane(DepartmentManagerChooseParkManReportController.class, "DepartmentManagerChooseParkManagerReport.fxml", StaticPaneMainPageDepartmentManager.DepartmentManagerMainPane).getController();
		  controller.setComboBoxOptions();
    }
    
    ObservableList<ReportListObject> data = FXCollections.observableArrayList();
  
    
    @FXML
    void showReport(ActionEvent event) {
    	String year = comboYear.getValue() + "";
    	String month = comboMonth.getValue() + "";
    	String parkName = parkComboBox.getValue();	
    	
    	getReservationHalfCancelationDetails(year,month,parkName);
		initializeTable();
  

    }
    
	public void getReservationHalfCancelationDetails(String year,String month,String parkName) {
		String stringToSend = year + " " + month  + " " + parkName;
		RequestHandler rh = new RequestHandler(controllerName.DepartmentManagerSystemController, "getTotalVisitorReportFromParkManager", stringToSend);
		ClientUI.chat.accept(gson.toJson(rh));
		analyzeAnswerFromServer();
	}
	
	//analyzeAnswerFromServer
	private void analyzeAnswerFromServer() {
		String answer = ChatClient.serverMsg;
		if(!answer.equals("faild"))
				fillTable(answer);
	}
	private void fillTable(String answer)
	{
		data.clear();
		String[] details = answer.split(" ");
		int sum1 = Integer.parseInt(details[1]) +  Integer.parseInt(details[2]) +  Integer.parseInt(details[3]);
		int sum2 = Integer.parseInt(details[4]) +  Integer.parseInt(details[5]) +  Integer.parseInt(details[6]);
		int sum3 = Integer.parseInt(details[7]) +  Integer.parseInt(details[8]) +  Integer.parseInt(details[9]);
		int sum4 = Integer.parseInt(details[10]) +  Integer.parseInt(details[11]) +  Integer.parseInt(details[12]);
		int sum5 = Integer.parseInt(details[13]) +  Integer.parseInt(details[14]) +  Integer.parseInt(details[15]);
		int sum6 = Integer.parseInt(details[16]) +  Integer.parseInt(details[17]) +  Integer.parseInt(details[18]);
		int sum7 = Integer.parseInt(details[19]) +  Integer.parseInt(details[20]) +  Integer.parseInt(details[21]);
		
		int sumCol1 = Integer.parseInt(details[1]) +Integer.parseInt(details[4]) + Integer.parseInt(details[7]) +  Integer.parseInt(details[10]) +Integer.parseInt(details[13]) + Integer.parseInt(details[16]);
		int sumCol2 = Integer.parseInt(details[2]) +Integer.parseInt(details[5]) + Integer.parseInt(details[8]) +  Integer.parseInt(details[11]) +Integer.parseInt(details[14]) + Integer.parseInt(details[15]);
		int sumCol3 = Integer.parseInt(details[3]) +Integer.parseInt(details[6]) + Integer.parseInt(details[9]) +  Integer.parseInt(details[12]) +Integer.parseInt(details[15]) + Integer.parseInt(details[17]);
							
		data.add(new ReportListObject(details[0],details[1],details[4],details[7],details[10],details[13],details[16],details[19],sumCol1 + ""));
		data.add(new ReportListObject(details[0],details[2],details[5],details[8],details[11],details[14],details[17],details[20],sumCol2 + ""));
		data.add(new ReportListObject(details[0],details[3],details[6],details[9],details[12],details[15],details[18],details[21],sumCol3 + ""));
		
		//Total data
		data.add(new ReportListObject("Total",sum1 + "",sum2 + "",sum3 + "",sum4 + "",sum5 + "",sum6 + "",sum7 + "",sumCol1 + sumCol2 + sumCol3 + ""));
		
		
	}
	private void initializeTable()
	{
		typeCol.setCellValueFactory(new PropertyValueFactory<ReportListObject, String>("type"));
		sundayCol.setCellValueFactory(new PropertyValueFactory<ReportListObject, String>("sunday"));
		mondayCol.setCellValueFactory(new PropertyValueFactory<ReportListObject, String>("monday"));
		tuesdayCol.setCellValueFactory(new PropertyValueFactory<ReportListObject, String>("tuesday"));
		wednesdayCol.setCellValueFactory(new PropertyValueFactory<ReportListObject, String>("wednesday"));
		thursdayCol.setCellValueFactory(new PropertyValueFactory<ReportListObject, String>("thursday"));
		fridayCol.setCellValueFactory(new PropertyValueFactory<ReportListObject, String>("friday"));
		saturdayCol.setCellValueFactory(new PropertyValueFactory<ReportListObject, String>("saturday"));
		monthlyCol.setCellValueFactory(new PropertyValueFactory<ReportListObject, String>("monthly"));
		mainTable.setItems(data);
	}
	public void setComboBoxDetails()
	{
		comboYear.setValue(fromYear);
		
		for (int i = yearExpansion; i > 0; i--) 
			comboYear.getItems().add(fromYear - i);
		
		for (int i = 0; i < yearExpansion; i++) 
			comboYear.getItems().add(fromYear + i);
		
		for (int i = 1; i <= 12; i++) 
			comboMonth.getItems().add(i);
		
		comboMonth.setValue(1);
		
		parkComboBox.setValue("Banias");
		parkComboBox.getItems().addAll("Banias", "Niagara","Safari");
		
		
	}
}
