package login;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

import client.ChatClient;
import client.ClientController;
import client.ClientUI;
import client.Ichat;
import employee.Employee;
import fxmlGeneralFunctions.IFXMLOpenEmployeeGui;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.text.Text;
import popup.PopUp;
import popup.PopUp.IPopUp;

class SignInEmployeeControllerTest {

	private JFXPanel panel;
	SignInEmployeeController signInEmployeeController;
	public static String popMessageEmp = "";
	public static String controllMessage = null;
	public static Employee employeeCheck = null;
	Gson gson = new Gson();

	class MyChat implements Ichat {

		@Override
		public void handleMessageFromServer(Object msg) {
		}

		@Override
		public void handleMessageFromClientUI(Object message) {
			switch (controllMessage) {
			case "employee not found":
				ChatClient.serverMsg = "employee not found";
				break;
			case "wrong password":
				ChatClient.serverMsg = "wrong password";
				break;
			case "already connected":
				ChatClient.serverMsg = "already connected";
				break;
			case "set employee":
				ChatClient.serverMsg = gson
						.toJson(new Employee("id", "password", "name", "lasstName", "email", "employee", "parkName"));
				break;
			case "set park manager":
				ChatClient.serverMsg = gson.toJson(
						new Employee("id", "password", "name", "lasstName", "email", "park manager", "parkName"));
				break;
			case "set department manager":
				ChatClient.serverMsg = gson.toJson(
						new Employee("id", "password", "name", "lasstName", "email", "department manager", "parkName"));
				break;
			}

		}

		@Override
		public void quit() {

		}

	}

	class MyFXMLOpenEmployeeGui implements IFXMLOpenEmployeeGui {

		@Override
		public void openEmployeeGui(Employee employee, Text BackBtn) {
			SignInEmployeeControllerTest.employeeCheck = employee;

		}

		@Override
		public void openParkManagerGui(Employee parkManager, Text BackBtn) {
			SignInEmployeeControllerTest.employeeCheck = parkManager;

		}

		@Override
		public void openDepartmentManagerGui(Employee employee, Text BackBtn) {
			SignInEmployeeControllerTest.employeeCheck = employee;

		}

	}

	class MyPopUp implements IPopUp {

		@Override
		public void display(String titel, String message) {
			SignInEmployeeControllerTest.popMessageEmp += message;

		}
	}

	@BeforeEach
	void setUp() throws Exception {
		panel = new JFXPanel();
		ClientUI.chat = new ClientController(new MyChat());
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(SignInEmployeeController.class.getResource("SignInEmployee.fxml"));
		loader.load();
		signInEmployeeController = loader.getController();
		signInEmployeeController.setFxmlOpenGui(new MyFXMLOpenEmployeeGui());
		PopUp.popUpForCheck = new MyPopUp();
		popMessageEmp = "";
	}

	@Test
	void testEmptyID() {
		signInEmployeeController.setIDTxt("");
		signInEmployeeController.setPasswordTxt("123");
		signInEmployeeController.Continue(null);
		assertEquals("Must enter id\n", popMessageEmp);
	}

	@Test
	void testEmptyPassword() {
		signInEmployeeController.setIDTxt("123");
		signInEmployeeController.setPasswordTxt("");
		signInEmployeeController.Continue(null);
		assertEquals("Must enter password\n", popMessageEmp);
	}

	@Test
	void testEmptyIDAndPassword() {
		signInEmployeeController.setIDTxt("");
		signInEmployeeController.setPasswordTxt("");
		signInEmployeeController.Continue(null);
		assertEquals("Must enter id\nMust enter password\n", popMessageEmp);
	}

	@Test
	void testEmployeeNotConnectedFound() {
		signInEmployeeController.setIDTxt("1234");
		signInEmployeeController.setPasswordTxt("3214");
		controllMessage = "employee not found";
		signInEmployeeController.setFxmlOpenGui(new MyFXMLOpenEmployeeGui());
		signInEmployeeController.Continue(null);
		assertEquals("employee not found", popMessageEmp);
	}

	@Test
	void testEmployeeNotConnectedWrongPassword() {
		signInEmployeeController.setIDTxt("1234");
		signInEmployeeController.setPasswordTxt("3214");
		controllMessage = "wrong password";
		signInEmployeeController.setFxmlOpenGui(new MyFXMLOpenEmployeeGui());
		signInEmployeeController.Continue(null);
		assertEquals("wrong password", popMessageEmp);
	}

	@Test
	void testEmployeeAllreadyConnected() {
		signInEmployeeController.setIDTxt("1234");
		signInEmployeeController.setPasswordTxt("3214");
		controllMessage = "already connected";
		signInEmployeeController.setFxmlOpenGui(new MyFXMLOpenEmployeeGui());
		signInEmployeeController.Continue(null);
		assertEquals("already connected", popMessageEmp);
	}

	@Test
	void testEmployeeConnected() {
		signInEmployeeController.setIDTxt("1234");
		signInEmployeeController.setPasswordTxt("3214");
		controllMessage = "set employee";
		signInEmployeeController.setFxmlOpenGui(new MyFXMLOpenEmployeeGui());
		signInEmployeeController.Continue(null);
		assertEquals("employee", employeeCheck.getTypeOfEmployee());
	}

	@Test
	void testParkManagerConnected() {
		signInEmployeeController.setIDTxt("1234");
		signInEmployeeController.setPasswordTxt("3214");
		controllMessage = "set park manager";
		signInEmployeeController.setFxmlOpenGui(new MyFXMLOpenEmployeeGui());
		signInEmployeeController.Continue(null);
		assertEquals("park manager", employeeCheck.getTypeOfEmployee());
	}

	@Test
	void testDepartmentManagerConnected() {
		signInEmployeeController.setIDTxt("1234");
		signInEmployeeController.setPasswordTxt("3214");
		controllMessage = "set department manager";
		signInEmployeeController.setFxmlOpenGui(new MyFXMLOpenEmployeeGui());
		signInEmployeeController.Continue(null);
		assertEquals("department manager", employeeCheck.getTypeOfEmployee());
	}

}
