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

    @FXML
    void continueToSpecificReport(ActionEvent event) {
    	String selectedCombo = parkReportOptions.getSelectionModel().getSelectedItem();
    	if(selectedCombo == null)
    		return;
    	switch(selectedCombo)
    	{
    	case "Total visitors":
    		numberOfVisitorsReportController novrController;
    		try {
				novrController = FXMLFunctions.loadSceneToMainPane(numberOfVisitorsReportController.class, "TotalVisitorsReports.fxml", StaticPaneMainPageParkManager.parkManagerMainPane).getController();
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

    @FXML
    void goBack(MouseEvent event) {
    StaticPaneMainPageParkManager.parkManagerMainPane.getChildren().clear();
    }
    public void setComboBoxOptions( Employee parkManager) {
    	parkReportOptions.getItems().addAll("Total visitors", "revenue","Capacity");
    	this.parkManager=parkManager;
    	parkName.setText(parkManager.getParkName());
	}

}
