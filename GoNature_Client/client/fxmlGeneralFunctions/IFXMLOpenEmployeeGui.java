package fxmlGeneralFunctions;

import employee.Employee;
import javafx.scene.text.Text;

public interface IFXMLOpenEmployeeGui {

	/**
	 * insert the user as employee
	 * 
	 * @param employee
	 */
	void openEmployeeGui(Employee employee, Text BackBtn);

	/**
	 * insert the user an park manager
	 * 
	 * @param parkManager
	 */
	void openParkManagerGui(Employee parkManager, Text BackBtn);

	/**
	 * insert the user as department manager
	 * 
	 * @param employee
	 */
	void openDepartmentManagerGui(Employee employee, Text BackBtn);

}