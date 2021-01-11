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

	class MyFxmlOpenGui implements IFxmlOpenGuiPage {

		@Override
		public void openPageUsingFxmlName(String VisitorHelloString, String VisitorType, Text stage) {
			IdentificationControllerTest.visitorTypeCheck = VisitorType;
		}
	}

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
	void testEmptyIDField() throws IOException {
		identificationController.setId("");
		identificationController.identificationContinueButton(null);
		assertEquals("Must enter id\n", popMessage);

	}

	@Test
	void testWrongIDField() throws IOException {
		identificationController.setId("abc");
		identificationController.identificationContinueButton(null);
		assertEquals("Must enter numbers\n", popMessage);

	}

	@Test
	void testAllReadyConnected() throws IOException {
		identificationController.setId("1012");
		IdentificationControllerTest.controllMessage = "all ready connected";
		identificationController.identificationContinueButton(null);
		assertEquals("all ready connected", popMessage);

	}

	@Test
	void testUpdateFailConnected() throws IOException {
		identificationController.setId("1012");
		IdentificationControllerTest.controllMessage = "update faild";
		identificationController.identificationContinueButton(null);
		assertEquals("update faild", popMessage);

	}

	@Test
	void testLoginAsGuestSuccess() throws IOException {
		identificationController.setId("123456");
		IdentificationControllerTest.controllMessage = "Guest";
		identificationController.setFxmlOpenGui(new MyFxmlOpenGui());
		identificationController.identificationContinueButton(null);
		assertEquals("guest", visitorTypeCheck);

	}

	@Test
	void testLoginAsSubscriberSuccess() throws IOException {
		identificationController.setId("1010");
		IdentificationControllerTest.controllMessage = "Subscriber";
		identificationController.setFxmlOpenGui(new MyFxmlOpenGui());
		identificationController.identificationContinueButton(null);
		assertEquals("family", visitorTypeCheck);

	}

}
