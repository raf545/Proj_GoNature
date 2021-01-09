package parkManager;

import com.google.gson.Gson;

import client.ChatClient;
import client.ClientUI;
import employee.Employee;
import employee.MainPageEmployeeController;
import requestHandler.RequestHandler;
import requestHandler.controllerName;

/**
 * 
 * This class is representing a thread that wakes up every 30 seconds
 * and updates the amounts of the park capacity in the specific 
 * Park Manager page
 *
 */
public class ParkManagerCapacityThred implements Runnable {

	Employee employee;
	MainPageParkManagerController mainPageParkManagerController;
	Gson gson = new Gson();

	public ParkManagerCapacityThred(Employee employee, MainPageParkManagerController mainPageParkManagerController) {
		this.employee = employee;
		this.mainPageParkManagerController = mainPageParkManagerController;
	}

	@Override
	public void run() {
		while (true) {
			RequestHandler rh = new RequestHandler(controllerName.EmployeeSystemController,
					"getAmountOfPeopleTodayInPark", employee.getParkName());
			ClientUI.chat.accept(gson.toJson(rh));
			String answer = ChatClient.serverMsg;
			mainPageParkManagerController.performCapacityUpdate(answer);
			Thread.currentThread();
			try {
				Thread.sleep(1000 * 30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
