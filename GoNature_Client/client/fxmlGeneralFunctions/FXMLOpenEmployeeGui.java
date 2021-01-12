package fxmlGeneralFunctions;

import java.io.IOException;

import departmentManager.DepartmentManagerCapacityThread;
import departmentManager.MainPageDepartmentManagerController;
import employee.Employee;
import employee.EmployeeCapacityThred;
import employee.MainPageEmployeeController;
import guiCommon.StaticPaneMainPageEmployee;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import parkManager.MainPageParkManagerController;
import parkManager.ParkManagerCapacityThred;

public class FXMLOpenEmployeeGui implements IFXMLOpenEmployeeGui {

	/**
	 * insert the user as employee
	 * 
	 * @param employee
	 */
	@Override
	public void openEmployeeGui(Employee employee, Text BackBtn) {
		try {
			Stage primaryStage = new Stage();
			Stage stage = (Stage) BackBtn.getScene().getWindow();
			stage.close();

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainPageEmployeeController.class.getResource("MainPageEmployee.fxml"));
			Pane root = loader.load();
			Scene sc = new Scene(root);

			MainPageEmployeeController mainPageEmployeeController = loader.getController();
			Runnable run = new EmployeeCapacityThred(employee, mainPageEmployeeController);
			Thread t = new Thread(run);
			mainPageEmployeeController.setEmp(employee, t);
			t.start();
			StaticPaneMainPageEmployee.employeeMainPane = mainPageEmployeeController.getPane();
			primaryStage.setOnCloseRequest(e -> FXMLFunctions.closeMainPage());
			primaryStage.setTitle("Employee Main Page");
			primaryStage.setScene(sc);
			primaryStage.show();
			primaryStage.setResizable(false);
		} catch (IOException e) {
			System.out.println("Load Faild");
		}
	}

	/**
	 * insert the user an park manager
	 * 
	 * @param parkManager
	 */
	@Override
	public void openParkManagerGui(Employee parkManager, Text BackBtn) {
		try {
			Stage primaryStage = new Stage();
			Stage stage = (Stage) BackBtn.getScene().getWindow();
			stage.close();

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainPageParkManagerController.class.getResource("MainPageParkManager.fxml"));
			Pane root = loader.load();

			MainPageParkManagerController mainPageParkManagerController = loader.getController();
			Runnable run = new ParkManagerCapacityThred(parkManager, mainPageParkManagerController);
			Thread t = new Thread(run);
			mainPageParkManagerController.setParkManagerEmployee(parkManager, t);
			t.start();

			Scene sc = new Scene(root);
			primaryStage.setOnCloseRequest(e -> FXMLFunctions.closeMainPage());
			primaryStage.setTitle("Main Page Park Manager");
			primaryStage.setScene(sc);
			primaryStage.show();
			primaryStage.setResizable(false);
		} catch (IOException e) {
			System.out.println("Load Faild");
		}
	}

	/**
	 * insert the user as department manager
	 * 
	 * @param employee
	 */
	@Override
	public void openDepartmentManagerGui(Employee employee, Text BackBtn) {
		try {
			Stage primaryStage = new Stage();
			Stage stage = (Stage) BackBtn.getScene().getWindow();
			stage.close();

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainPageDepartmentManagerController.class.getResource("MainPageDepartmentManager.fxml"));
			Pane root = loader.load();

			Scene sc = new Scene(root);

			MainPageDepartmentManagerController mainPageDepartmentManagerController = loader.getController();
			Runnable run = new DepartmentManagerCapacityThread(employee, mainPageDepartmentManagerController);
			Thread t = new Thread(run);
			mainPageDepartmentManagerController.setDepDetails(employee, t);
			t.start();

			primaryStage.setOnCloseRequest(e -> FXMLFunctions.closeMainPage());
			primaryStage.setTitle("Main Page Department Manager");
			primaryStage.setScene(sc);
			primaryStage.show();
			primaryStage.setResizable(false);
		} catch (IOException e) {
			System.out.println("Load Faild");
		}
	}
}
