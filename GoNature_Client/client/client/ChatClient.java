// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.ChatIF;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract superclass
 * in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * 
 * @Editor Dan Gutchin
 * @Editor Yaniv Sokolov
 * @Editor Rafael elkoby
 * @version December 3 2020
 */

public class ChatClient extends AbstractClient implements Ichat {
	// Instance variables **********************************************

	/**
	 * The interface type variable. It allows the implementation of the displaY
	 * method in the client.
	 */
	ChatIF clientUI;
	
	public static Class<?> clientType = null;
	public static String clientTypeString = null;
	public static String clientIdString = null;
	public static String clientInfo = null; 
	public static String serverMsg = null;
	public static boolean awaitResponse = false;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the chat client.
	 *
	 * @param host     The server to connect to.
	 * @param port     The port number to connect on.
	 * @param clientUI The interface type variable.
	 */

	public ChatClient(String host, int port, ChatIF clientUI) throws IOException {
		super(host, port); // Call the superclass constructor
		this.clientUI = clientUI;
		// openConnection();
	}

	
	// Instance methods ************************************************

	/**
	 * This method handles the data that comes in from the server about a visitor
	 *
	 * @param msg The message from the server.
	 */
	@Override
	public void handleMessageFromServer(Object msg) {
		System.out.println("--> handleMessageFromServer");
		awaitResponse = false;
		serverMsg = (String) msg;
	}

	/**
	 * This method handles all data coming from the UI
	 *
	 * @param message The message from the UI.
	 */

	@Override
	public void handleMessageFromClientUI(Object message) {
		try {
			openConnection();// in order to send more than one message
			awaitResponse = true;
			sendToServer(message);
			// wait for response
			while (awaitResponse) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			clientUI.display("Could not send message to server: Terminating client." + e);
			quit();
		}
	}

	/**
	 * This method terminates the client.
	 */
	@Override
	public void quit() {
		try {
			closeConnection();
		} catch (IOException e) {
		}
		System.exit(0);
	}
}
//End of ChatClient class
