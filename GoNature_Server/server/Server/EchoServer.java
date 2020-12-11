// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
package Server;

import java.io.IOException;
import java.util.ArrayList;

import logic.Visitor;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;
import serverGui.ServerPortController;
import sqlConnection.SqlConnector;

/**
 * This class overrides some of the methods in the abstract superclass in order
 * to give more functionality to the server.
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

public class EchoServer extends AbstractServer {
	// Class variables *************************************************

	private ServerPortController serverPortControllerInstance;
	private SqlConnector sqlConnector;
	/**
	 * The default port to listen on.
	 */
	// final public static int DEFAULT_PORT = 5555;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the echo server.
	 *
	 * @param port The port number to connect on.
	 * 
	 */
	public EchoServer(int port, ServerPortController sPC) {
		super(port);
		this.serverPortControllerInstance = sPC;
	}

	// Instance methods ************************************************

	/**
	 * This method handles any messages received from the client. HEY
	 * 
	 * @param msg    The message received from the client.
	 * @param client The connection from which the message originated.
	 * @param
	 */
	public void handleMessageFromClient(Object msg, ConnectionToClient client) {
		Visitor sv = new Visitor();
		String message = null;
		System.out.println("Message received : " + msg + " from " + client);
		try {
			if (msg instanceof String) {
				message = (String) msg;
				if (message.equals("close")) {
					// FIXME not a proper client close
					serverPortControllerInstance.setDisconectClientFields();
					client.sendToClient("");

				} else {
					sv = sqlConnector.searchInDB(msg);
					if (sv.getId() != null)

						client.sendToClient(sv.toString());

					else
						client.sendToClient("Error");
				}
			}
			if (msg instanceof ArrayList<?>) {
				if (sqlConnector.updateEmailInDB(msg))
					client.sendToClient("succsess");
			}
		} catch (

		IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() {
		sqlConnector = SqlConnector.getInstance();
		serverPortControllerInstance.setConnectToDB();
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
	}

	// METHODS DESIGNED TO BE OVERRIDDEN BY CONCRETE SUBCLASSES ---------

	/**
	 * Hook method called each time a new client connection is accepted. The default
	 * implementation does nothing.
	 * 
	 * @param client the connection connected to the client.
	 */
	protected void clientConnected(ConnectionToClient client) {
		serverPortControllerInstance.setInfoClient(client.getInetAddress().toString(),
				client.getInetAddress().getHostAddress().toString());
	}
}
//End of EchoServer class
