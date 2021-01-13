package departmentManagerReports;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import client.ChatClient;
import client.ClientController;
import client.ClientUI;
import client.Ichat;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import login.IdentificationController;
import popup.PopUp;
import popup.PopUp.IPopUp;

class DepartmentManagerVisitReportControllerTest {
	private JFXPanel panel;
	public static String controllMessage = null;
	DepartmentManagerVisitReportController departmentManagerVisitReportController;
	public static String popMessage = null;

	// class for replace the server answer
	class MyChat implements Ichat {

		@Override
		public void handleMessageFromServer(Object msg) {
		}

		@Override
		public void handleMessageFromClientUI(Object message) {
			switch (controllMessage) {
			case "faild":
				ChatClient.serverMsg = "faild";
				break;
			case "empty":
				ChatClient.serverMsg = "";
				break;
			case "data":
				ChatClient.serverMsg = "8,20 10,20 11,30";
				break;

			}

		}

		@Override
		public void quit() {
		}

	}

	// replace the POPUP and save the error message
	class MyPopUp implements IPopUp {

		@Override
		public void display(String titel, String message) {
			popMessage = message;

		}
	}

	@BeforeEach
	void setUp() throws Exception {
		panel = new JFXPanel();
		ClientUI.chat = new ClientController(new MyChat());
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(
				DepartmentManagerVisitReportController.class.getResource("DepartmentManagerVisitReport.fxml"));
		loader.load();
		departmentManagerVisitReportController = loader.getController();
		PopUp.popUpForCheck = new MyPopUp();

	}

	@Test
	// check if non of the parks chosen
	// input: disable the buttons
	// expected: pop answer : no selected park
	void testNoParkChoose() {
		departmentManagerVisitReportController.setDate(2020, 12, 1);
		departmentManagerVisitReportController.setComboBox("subscriber");
		departmentManagerVisitReportController.setParkForReport(false, false, false);
		departmentManagerVisitReportController.report(null);
		assertEquals(departmentManagerVisitReportController.getPie1().getData().get(0).getName(), "No Activities");
		assertEquals("no selected park", popMessage);
	}

	@Test
	// check failed DB answer
	// input: set which park to check
	// expected: pop answer :faild
	void testFailedSearchInDataBase() {
		departmentManagerVisitReportController.setDate(2020, 12, 1);
		departmentManagerVisitReportController.setComboBox("subscriber");
		departmentManagerVisitReportController.setParkForReport(true, false, false);
		controllMessage = "faild";
		departmentManagerVisitReportController.report(null);
		assertEquals("faild", popMessage);

	}

	@Test
	// check empty data for the park
	// input: set which park to check
	// expected: pieChart set : No Activities
	void testEmptyDataFromDataBase() {
		departmentManagerVisitReportController.setDate(2020, 12, 1);
		departmentManagerVisitReportController.setComboBox("subscriber");
		departmentManagerVisitReportController.setParkForReport(true, false, false);
		controllMessage = "empty";
		departmentManagerVisitReportController.report(null);
		assertEquals(departmentManagerVisitReportController.getPie1().getData().get(0).getName(), "No Activities");
	}

	@Test
	// check data for the park
	// input: set which park to check
	// expected: pieChart not equals : No Activities
	void testDataFromDataBase() {
		departmentManagerVisitReportController.setDate(2020, 12, 1);
		departmentManagerVisitReportController.setComboBox("subscriber");
		departmentManagerVisitReportController.setParkForReport(true, false, false);
		controllMessage = "data";
		departmentManagerVisitReportController.report(null);
		assertNotEquals(departmentManagerVisitReportController.getPie1().getData().get(0).getName(), "No Activities");
	}

}
