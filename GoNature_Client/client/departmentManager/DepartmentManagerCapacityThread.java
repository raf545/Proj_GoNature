package departmentManager;

import com.google.gson.Gson;

import client.ChatClient;
import client.ClientUI;
import employee.Employee;
import requestHandler.RequestHandler;
import requestHandler.controllerName;

/**
 * 
 * This class is representing a thread that wakes up every 30 seconds
 * and updates the amounts of the different parks capacity
 * in a specific Department Manager window
 * 
 */
public class DepartmentManagerCapacityThread implements Runnable {

	Employee employee;
	MainPageDepartmentManagerController mainPageDepartmentManagerController;
	Gson gson = new Gson();

	public DepartmentManagerCapacityThread(Employee employee, MainPageDepartmentManagerController mainPageDepartmentManagerController) {
		this.employee = employee;
		this.mainPageDepartmentManagerController = mainPageDepartmentManagerController;
	}

	@Override
	public void run() {
		while (true) {
			RequestHandler rh = new RequestHandler(controllerName.DepartmentManagerSystemController, "getAmountOfPeopleTodayInPark", "");
			ClientUI.chat.accept(gson.toJson(rh));
			String answer = ChatClient.serverMsg;
			mainPageDepartmentManagerController.performCapacityUpdate(answer);
			Thread.currentThread();
			try {
				Thread.sleep(1000 * 30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}


}
