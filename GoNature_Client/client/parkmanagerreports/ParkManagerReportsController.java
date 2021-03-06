package parkmanagerreports;


import java.io.IOException;

import employee.Employee;
import fxmlGeneralFunctions.FXMLFunctions;
import guiCommon.StaticPaneMainPageDepartmentManager;
import guiCommon.StaticPaneMainPageParkManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import parkManager.BlankParkManagerController;


/**class the navigate the park manger to correct report
 * @author zivi9
 *
 */
public class ParkManagerReportsController {
    Employee parkManager;
    @FXML
    private Text backBtn;

    @FXML
    private Button continueBtn;
    @FXML
    private Label parkName;
    @FXML
    private ComboBox<String> parkReportOptions;

    /**open the specific report that the park manager choose
     * @param event
     */
    @FXML
    void continueToSpecificReport(ActionEvent event) {
    	String selectedCombo = parkReportOptions.getSelectionModel().getSelectedItem();
    	if(selectedCombo == null)
    		return;
    	switch(selectedCombo)
    	{
    	case "Total visitors":
    		TotalVisitorReportsController novrController;
    		try {
				novrController = FXMLFunctions.loadSceneToMainPane(TotalVisitorReportsController.class, "TotalVisitorsReports.fxml", StaticPaneMainPageParkManager.parkManagerMainPane).getController();
				novrController.setParkManager(parkManager);
			} catch (IOException e) {
				//
				e.printStackTrace();
			}
    		
    		break;
    	case "revenue":
    		MonthlyRevenueReportsController mrrcController;
    		try {
    			mrrcController = FXMLFunctions.loadSceneToMainPane(MonthlyRevenueReportsController.class, "MonthlyRevenueReports.fxml", StaticPaneMainPageParkManager.parkManagerMainPane).getController();
    			mrrcController.setParkManager(parkManager);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
    		break; 
    	case "Capacity":
    		VisitorCapacityReportController vcrController;
    		try {
    			vcrController = FXMLFunctions.loadSceneToMainPane(VisitorCapacityReportController.class, "VisitorCapacityReport.fxml", StaticPaneMainPageParkManager.parkManagerMainPane).getController();
    			vcrController.setParkManager(parkManager);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
    		break; 
    	}

    }

    /**go back to the main page of park manger
     * @param event
     * @throws IOException 
     */
    @FXML
    void goBack(MouseEvent event) throws IOException {
    StaticPaneMainPageParkManager.parkManagerMainPane.getChildren().clear();
    FXMLFunctions.loadSceneToMainPane(	BlankParkManagerController.class, "BlankParkManager.fxml",  StaticPaneMainPageParkManager.parkManagerMainPane);
    }
    public void setComboBoxOptions( Employee parkManager) {
    	parkReportOptions.getItems().addAll("Total visitors", "revenue","Capacity");
    	this.parkManager=parkManager;
    	parkName.setText(parkManager.getParkName());
	}

}
