package employee;

import com.google.gson.Gson;

import client.ChatClient;
import client.ClientUI;
import requestHandler.RequestHandler;
import requestHandler.controllerName;
/**
 * 
 * This class is representing a thread that wakes up every 30 seconds
 * and updates the amounts of the different parks capacity
 * in a specific Employee window
 * 
 */
public class EmployeeCapacityThred implements Runnable {
	Employee employee;
	MainPageEmployeeController mainPageEmployeeController;
	Gson gson = new Gson();

	public EmployeeCapacityThred(Employee employee, MainPageEmployeeController mainPageEmployeeController) {
		this.employee = employee;
		this.mainPageEmployeeController = mainPageEmployeeController;
	}

	@Override
	public void run() {
		while (true) {
			RequestHandler rh = new RequestHandler(controllerName.EmployeeSystemController,
					"getAmountOfPeopleTodayInPark", employee.getParkName());
			ClientUI.chat.accept(gson.toJson(rh));
			String answer = ChatClient.serverMsg;
			mainPageEmployeeController.performCapacityUpdate(answer);
			Thread.currentThread();
			try {
				Thread.sleep(1000 * 30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
