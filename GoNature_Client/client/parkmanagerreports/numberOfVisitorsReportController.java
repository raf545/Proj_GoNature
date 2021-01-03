package parkmanagerreports;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import com.google.gson.Gson;

import Reports.ReportData;
import client.ChatClient;
import client.ClientUI;
import departmentManagerReports.DepartmentManagerChooseReportController;
import departmentManagerReports.ReportListObject;
import employee.Employee;
import fxmlGeneralFunctions.FXMLFunctions;
import guiCommon.StaticPaneMainPageDepartmentManager;
import guiCommon.StaticPaneMainPageParkManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import parkManager.MainPageParkManagerController;
import popup.PopUp;
import requestHandler.RequestHandler;
import requestHandler.controllerName;
/**class that generate and show the number of visitors report
 * @author zivi9
 *
 */
public class numberOfVisitorsReportController {
	Gson gson = new Gson();
	
	
	
		 Employee parkManager;
		 ObservableList<ReportListObject> data = FXCollections.observableArrayList();
		 
		
		 @FXML
		    private Text backbtn;

		    @FXML
		    private ComboBox<String> comboYear;

		    @FXML
		    private ComboBox<String> comboMonth;

		    @FXML
		    private Button showbtn;

		    @FXML
		    private Label parkName;

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



	    /**save the request and call methods that will send it to the server
	     * @param event
	     */
	    @FXML
	    void showReport(ActionEvent event) {
	    	ReportData data=new ReportData(parkManager.getParkName(), comboYear.getValue(), comboMonth.getValue());
	    	
	    	initializeTable();
	    	askTotalVisitorsReportFromServer("TotalVisitorsReport", data );

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
		/**set the server answer into table view 
		 * 
		 */
		private void analyzeAnswerFromServer() {
			 int sum1=0;
			 int sum2=0;
			 int sum3=0;
			 int sum4=0;
			 int sum5=0;
			 int sum6=0;
			 int sum7=0;
			 int sumrow1=0;
			 int sumrow2=0;
			 int sumrow3=0;
			 int totalsum=0;
			    String answer = ChatClient.serverMsg;
				int[][] res= gson.fromJson(answer, int[][].class);
				
				for(int i=0;i<3;i++)
				{
					sum1=sum1+res[0][i];
				}
				for(int i=0;i<3;i++)
				{
					sum2=sum2+res[1][i];
				}
				
				for(int i=0;i<3;i++)
				{
					sum3=sum3+res[2][i];
				}
				
				for(int i=0;i<3;i++)
				{
					sum4=sum4+res[3][i];
				}
				
				for(int i=0;i<3;i++)
				{
					sum5=sum5+res[4][i];
				}
				
				for(int i=0;i<3;i++)
				{
					sum6=sum6+res[5][i];
				}
				
				for(int i=0;i<3;i++)
				{
					sum7=sum7+res[6][i];
				}
			
				for(int i=0;i<7;i++)
				{
					sumrow1=sumrow1+res[i][0];
				}
			
				for(int i=0;i<7;i++)
				{
					sumrow2=sumrow2+res[i][1];
				}
				
				for(int i=0;i<7;i++)
				{
					sumrow3=sumrow3+res[i][2];
				}
				
				totalsum=sumrow1+sumrow2+sumrow3;
				
				data.add(new ReportListObject("single",String.valueOf(res[0][0]),String.valueOf(res[1][0]),String.valueOf(res[2][0]),String.valueOf(res[3][0]),String.valueOf(res[4][0]),String.valueOf(res[5][0]),String.valueOf(res[6][0]),String.valueOf(sumrow1) ));
				data.add(new ReportListObject("family",String.valueOf(res[0][1]),String.valueOf(res[1][1]),String.valueOf(res[2][1]),String.valueOf(res[3][1]),String.valueOf(res[4][1]),String.valueOf(res[5][1]),String.valueOf(res[6][1]),String.valueOf(sumrow2) ));
				data.add(new ReportListObject("group",String.valueOf(res[0][2]),String.valueOf(res[1][2]),String.valueOf(res[2][2]),String.valueOf(res[3][2]),String.valueOf(res[4][2]),String.valueOf(res[5][2]),String.valueOf(res[6][2]),String.valueOf(sumrow3) ));
				
			
				//Total data
				data.add(new ReportListObject("total",sum1 + "",sum2 + "",sum3 + "",sum4 + "",sum5 + "",sum6 + "",sum7 + "",totalsum + ""));
				
				
				
		}
		
		/**initialize the tabke view
		 * 
		 */
		private void initializeTable()
		{
			data.clear();
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
		 /** go back to the park manager report main page
		 * @param event
		 * @throws IOException
		 */
		@FXML
		    void goBack(MouseEvent event) throws IOException {
			ParkManagerReportsController controller = FXMLFunctions.loadSceneToMainPane(ParkManagerReportsController.class, "ParkManagerReports.fxml", StaticPaneMainPageParkManager.parkManagerMainPane).getController();
			controller.setComboBoxOptions(parkManager);
		    }
		  
}
