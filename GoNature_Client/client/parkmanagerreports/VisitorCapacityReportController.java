package parkmanagerreports;

import java.io.IOException;
import java.util.Calendar;

import com.google.gson.Gson;

import Reports.ReportData;
import client.ClientUI;
import employee.Employee;
import fxmlGeneralFunctions.FXMLFunctions;
import guiCommon.StaticPaneMainPageParkManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import requestHandler.RequestHandler;
import requestHandler.controllerName;

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
    void goBack(MouseEvent event) throws IOException {
    	ParkManagerReportsController controller = FXMLFunctions.loadSceneToMainPane(ParkManagerReportsController.class, "ParkManagerReports.fxml", StaticPaneMainPageParkManager.parkManagerMainPane).getController();
	
		controller.setComboBoxOptions(parkManager);
    }

    @FXML
    void showReport(ActionEvent event) {
    	ReportData data=new ReportData(parkManager.getParkName(), comboYear.getValue(), comboMonth.getValue());
    	askVisitorCapacityReportFromServer("VisitorCapacityReport", data );
    }
    private void askVisitorCapacityReportFromServer(String funcname, ReportData data) {
    	RequestHandler rh = new RequestHandler(controllerName.ReportsController, funcname,gson.toJson(data));
		ClientUI.chat.accept(gson.toJson(rh));
		analyzeAnswerFromServer();
		
	}
    

	private void analyzeAnswerFromServer() {
		// TODO Auto-generated method stub
		
	}

	public void setParkManager(Employee employee) {
    	int thisyear = Calendar.getInstance().get(Calendar.YEAR);
    	for(int i=thisyear-7;i<=thisyear;i++)
    		comboYear.getItems().add(String.valueOf(i));
    	for(int j=1;j<13;j++)
    		comboMonth.getItems().add(String.valueOf(j));
		parkManager = employee;
		parkName.setText(parkManager.getParkName());
		

	}

}
