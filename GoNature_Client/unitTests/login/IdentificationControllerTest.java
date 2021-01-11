package login;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Component;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.glass.ui.Application;

import client.ChatClient;
import client.ClientController;
import client.ClientUI;
import client.Ichat;
import fxmlGeneralFunctions.IFxmlOpenGuiPage;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

class IdentificationControllerTest {

	IdentificationController identificationController;

	class MyChat implements Ichat {

		@Override
		public void handleMessageFromServer(Object msg) {
		}

		@Override
		public void handleMessageFromClientUI(Object message) {
			if (true) {
				ChatClient.serverMsg = "1234";
			}
		}

		@Override
		public void quit() {

		}

	}

	class MyFxmlOpenGui implements IFxmlOpenGuiPage {

		@Override
		public void openPageUsingFxmlName(String VisitorHelloString, String VisitorType, Stage stage) {

		}
	}

	@BeforeEach
	void setUp() throws Exception {
		ClientUI.chat = new ClientController(new MyChat());

	}

	@Test
	void test() {
		assertTrue(true);
	}

}
