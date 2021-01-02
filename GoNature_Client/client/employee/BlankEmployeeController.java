
package employee;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**contains the data about this employee and present at the front main page
 * @author zivi9
 *
 */
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




	/**save the informations of the employee for the all the instance of this class
	 * @param employeeall
	 */
	public void setEmployee(Employee employeeall) {
		employee = employeeall;
	}




	/**set the information of the employee at the labels
	 * 
	 */
	public void setBlank()
	{
		empname.setText(employee.getName());
		empemail.setText(employee.getEmail());
		emppark.setText(employee.getParkName());
		empid.setText(employee.getEmployeeId());
	}
    
}


	
