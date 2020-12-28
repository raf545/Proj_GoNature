package departmentManagerReports;

import java.io.IOException;

import fxmlGeneralFunctions.FXMLFunctions;
import guiCommon.StaticPaneMainPageDepartmentManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class DepartmentManagerVisitReportController {

    @FXML
    private Text backBtn;

    @FXML
    private ComboBox<String> monthComboBox;

    @FXML
    private ComboBox<String> yearComboBox;

    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private Button reportButton;

    @FXML
    private PieChart pieOne;
    
	  @FXML
	    void quitScene(MouseEvent event) throws IOException {
		  //	StaticPaneMainPageDepartmentManager.DepartmentManagerMainPane.getChildren().clear();
		  DepartmentManagerChooseReportController controller = FXMLFunctions.loadSceneToMainPane(DepartmentManagerChooseReportController.class, "DepartmentManagerChooseReport.fxml", StaticPaneMainPageDepartmentManager.DepartmentManagerMainPane).getController();
		  controller.setComboBoxOptions();
	  }
	    @FXML
	    void report(ActionEvent event) {
	    	PieChart.Data slices[] = new PieChart.Data[12];
	    	for (int i = 0; i < slices.length; i++) 
	    	{
	    		slices[i] = new PieChart.Data( "check " + i, 5);				
	    		pieOne.getData().add(slices[i]);
	    	}

	    }
	    
		public void setTypeComboBoxOptions() {
			typeComboBox.getItems().addAll("Group","Family","Subscriber","Guest");
		}

}
