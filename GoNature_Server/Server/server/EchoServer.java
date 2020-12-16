// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
package server;

import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;

import controllers.LoginController;
import logic.Visitor;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;
import requestHandler.RequestHandler;
import requestHandler.controllerName;
import serverGui.ServerGuiController;
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
	Gson gson = new Gson();
	private ServerGuiController serverPortControllerInstance;
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
	public EchoServer(int port, ServerGuiController sPC) {
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
		// System.out.println("Message received : " + msg + " from " + client);
		String clientMsg = (String) msg;
		RequestHandler rh = gson.fromJson(clientMsg, RequestHandler.class);
		controllerName myCon = rh.getMyCon();
		String answer = null;
		switch (myCon) {
		case CardReaderController:
			break;
		case EmployeeSystemController:
			break;
		case LoginController:
			answer = LoginController.getInstance().getFunc(rh.getFunc(), rh.getData(), client);
			break;
		case ReportsController:
			break;
		case ReservationController:
			break;
		case ServiceRepresentativeController:
			break;
		case WaitingListController:
			break;
		}
		try {
			client.sendToClient(answer);
		} catch (IOException e) {
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
	@Override
	protected void clientConnected(ConnectionToClient client) {
		System.out.println("client!!");
		Runnable r = new Runnable() {
			public void run() {
				while (client.isAlive()) {
					try {
						client.join();

					} catch (InterruptedException e) {
						// TODO: handle exception
					}
				}
				clientDisconnected(client);
			}
		};

		new Thread(r).start();
	}

	protected void clientDisconnected(ConnectionToClient client) {
//		System.out.println("client dis!!");
		String id = (String) client.getInfo("ID");
		String Table = (String) client.getInfo("Table");

		if (id == null) {
			System.out.println("client dis!!");
			return;
		}
		String query = "UPDATE gonaturedb." + Table + " SET connected = 0 WHERE id = " + client.getInfo("ID") + ";";
		SqlConnector.getInstance().executeToDB(query);
	}

}
//End of EchoServer class
