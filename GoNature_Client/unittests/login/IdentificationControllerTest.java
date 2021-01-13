package login;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

import client.ChatClient;
import client.ClientController;
import client.ClientUI;
import client.Ichat;
import fxmlGeneralFunctions.IFxmlOpenGuiPage;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import popup.PopUp;
import popup.PopUp.IPopUp;
import subscriber.Subscriber;

class IdentificationControllerTest {

	private JFXPanel panel;
	IdentificationController identificationController;
	public static String popMessage = null;
	public static String controllMessage = null;
	public static String visitorTypeCheck = null;
	Gson gson = new Gson();

	// class for replace the server answer
	class MyChat implements Ichat {

		@Override
		public void handleMessageFromServer(Object msg) {
		}

		@Override
		public void handleMessageFromClientUI(Object message) {
			switch (controllMessage) {
			case "all ready connected":
				ChatClient.serverMsg = "all ready connected";
				break;
			case "update faild":
				ChatClient.serverMsg = "update faild";
				break;
			case "Guest":
				ChatClient.serverMsg = gson
						.toJson(new Subscriber("123456", null, null, null, null, null, null, null, null));
				break;
			case "Subscriber":
				ChatClient.serverMsg = gson
						.toJson(new Subscriber("312546987", "1010", "abc", "abc", "", "", "2", null, "family"));
				break;

			}

		}

		@Override
		public void quit() {

		}

	}

	// class for replace the open windows and save the type of the visitor
	class MyFxmlOpenGui implements IFxmlOpenGuiPage {

		@Override
		public void openPageUsingFxmlName(String VisitorHelloString, String VisitorType, Text stage) {
			IdentificationControllerTest.visitorTypeCheck = VisitorType;
		}
	}

	// replace the POPUP and save the error message
	class MyPopUp implements IPopUp {

		@Override
		public void display(String titel, String message) {
			IdentificationControllerTest.popMessage = message;

		}
	}

	@BeforeEach
	void setUp() throws Exception {
		panel = new JFXPanel();
		ClientUI.chat = new ClientController(new MyChat());
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(IdentificationController.class.getResource("Identification.fxml"));
		loader.load();
		identificationController = loader.getController();
		identificationController.setFxmlOpenGui(new MyFxmlOpenGui());
		PopUp.popUpForCheck = new MyPopUp();

	}

	@Test
	// check if empty id entered
	// input: empty id
	// expected: pop answer : Must enter id\n
	void testEmptyIDField() throws IOException {
		identificationController.setId("");
		identificationController.identificationContinueButton(null);
		assertEquals("Must enter id\n", popMessage);

	}

	@Test
	// check if wrong Id entered
	// input: wrong id = "abc"
	// expected: pop answer : Must enter numbers\n
	void testWrongIDField() throws IOException {
		identificationController.setId("abc");
		identificationController.identificationContinueButton(null);
		assertEquals("Must enter numbers\n", popMessage);

	}

	@Test
	// check if the subscriber is all ready connected to the system
	// input: id = "1012"
	// expected: pop answer : all ready connected
	void testAllReadyConnected() throws IOException {
		identificationController.setId("1012");
		IdentificationControllerTest.controllMessage = "all ready connected";
		identificationController.identificationContinueButton(null);
		assertEquals("all ready connected", popMessage);

	}

	@Test
	// check if the server fail to update the DB
	// input: id = "1012"
	// expected: pop answer : update failed
	void testUpdateFailConnected() throws IOException {
		identificationController.setId("1012");
		IdentificationControllerTest.controllMessage = "update faild";
		identificationController.identificationContinueButton(null);
		assertEquals("update faild", popMessage);

	}

	@Test
	// check if the client connected successfully
	// input: id = "123456"
	// expected: visitor type = guest
	void testLoginAsGuestSuccess() throws IOException {
		identificationController.setId("123456");
		IdentificationControllerTest.controllMessage = "Guest";
		identificationController.setFxmlOpenGui(new MyFxmlOpenGui());
		identificationController.identificationContinueButton(null);
		assertEquals("guest", visitorTypeCheck);

	}

	@Test
	// check if the subscriber connected successfully
	// input: id = "1010"
	// expected: visitor type = family
	void testLoginAsSubscriberSuccess() throws IOException {
		identificationController.setId("1010");
		IdentificationControllerTest.controllMessage = "Subscriber";
		identificationController.setFxmlOpenGui(new MyFxmlOpenGui());
		identificationController.identificationContinueButton(null);
		assertEquals("family", visitorTypeCheck);

	}

}
