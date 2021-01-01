
package employee;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class BlankEmployeeController {

    @FXML
    private Label empname;

    @FXML
    private Label empemail;

    @FXML
    private Label emppark;

    @FXML
    private Label empid;

    private static Employee employee;




	public void setEmployee(Employee employeeall) {
		employee = employeeall;
	}




	public void setBlank()
	{
		empname.setText(employee.getName());
		empemail.setText(employee.getEmail());
		emppark.setText(employee.getParkName());
		empid.setText(employee.getEmployeeId());
	}
    
}


	
