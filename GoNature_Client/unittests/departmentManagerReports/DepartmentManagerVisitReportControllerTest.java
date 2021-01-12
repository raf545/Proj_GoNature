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

			}

		}

		@Override
		public void quit() {

		}

	}

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
	void testNoParkChoose() {
		departmentManagerVisitReportController.setDate(2020, 12, 1);
		departmentManagerVisitReportController.setComboBox("subscriber");
		departmentManagerVisitReportController.setParkForReport(false, false, false);
		departmentManagerVisitReportController.report(null);
		assertEquals(departmentManagerVisitReportController.getPie1().getData().get(0).getName(), "No Activities");
		assertEquals("no slected park", popMessage);
	}

	@Test
	void testFailedSearchInDataBase() {
		departmentManagerVisitReportController.setDate(2020, 12, 1);
		departmentManagerVisitReportController.setComboBox("subscriber");
		departmentManagerVisitReportController.setParkForReport(true, false, false);
		controllMessage = "faild";
		departmentManagerVisitReportController.report(null);
		assertEquals("faild", popMessage);

	}

	@Test
	void testEmptyDataFromDataBase() {
		departmentManagerVisitReportController.setDate(2020, 12, 1);
		departmentManagerVisitReportController.setComboBox("subscriber");
		departmentManagerVisitReportController.setParkForReport(true, false, false);
		controllMessage = "empty";
		departmentManagerVisitReportController.report(null);
		assertEquals(departmentManagerVisitReportController.getPie1().getData().get(0).getName(), "No Activities");
	}

}
