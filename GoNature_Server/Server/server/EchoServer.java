// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
package server;

import java.io.IOException;
import com.google.gson.Gson;

import controllers.CardReaderController;
import controllers.DepartmentManagerSystemController;
import controllers.EmployeeSystemController;
import controllers.LoginController;
import controllers.ParkManagerSystemController;
import controllers.ReportsController;
import controllers.ReservationController;
import controllers.WaitingListController;
import dataBase.DataBase;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;
import requestHandler.RequestHandler;
import requestHandler.controllerName;
import runner.PeriodicallyRunner;
import serverGui.ServerGuiController;

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
	@Override
	public void handleMessageFromClient(Object msg, ConnectionToClient client) {
		// System.out.println("Message received : " + msg + " from " + client);
		String clientMsg = (String) msg;
		RequestHandler rh = gson.fromJson(clientMsg, RequestHandler.class);
		controllerName myCon = rh.getMyCon();
		String answer = null;
		switch (myCon) {
		case CardReaderController:
			answer = CardReaderController.getInstance().router(rh.getFunc(), rh.getData(), client);
			break;
		case EmployeeSystemController:
			answer = EmployeeSystemController.getInstance().getFunc(rh.getFunc(), rh.getData(), client);
			break;
		case LoginController:
			answer = LoginController.getInstance().loginRouter(rh.getFunc(), rh.getData(), client);
			break;
		case ReportsController:
			answer = ReportsController.getInstance().getFunc(rh.getFunc(), rh.getData(), client);
			break;
		case ReservationController:
			answer = ReservationController.getInstance().loginRouter(rh.getFunc(), rh.getData(), client);
			break;
		case ServiceRepresentativeController:
			break;
		case WaitingListController:
			answer = WaitingListController.getInstance().loginRouter(rh.getFunc(), rh.getData(), client);
			break;
		case ParkManagerSystemController:
			answer = ParkManagerSystemController.getInstance().getFunc(rh.getFunc(), rh.getData(), client);
			break;
		case DepartmentManagerSystemController:
			answer = DepartmentManagerSystemController.getInstance().getFunc(rh.getFunc(), rh.getData(), client);
			break;
		}
		try {
			client.sendToClient(answer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections. this method start 4 different threads in
	 * different time for clean waiting list , send message for relevant client with
	 * reservation day before , check if client don't approve the message , check if
	 * have reservation that approved and don't come to the park
	 */
	@Override
	protected void serverStarted() {
		DataBase.getInstance();
		if (DataBase.getInstance().setConnection()) {
			serverPortControllerInstance.setConnectToDB();

		}
		PeriodicallyRunner.runEveryDayAt(8, 0, new SendMessageToExistingReservationsDayBefore());
		PeriodicallyRunner.runEveryDayAt(10, 0, new CheckIfApproveReservation());
		PeriodicallyRunner.runEveryDayAt(6, 0, new DeleteIrrelevantWaitingList());
		for (int i = 8; i < 21; i++) {
			PeriodicallyRunner.runEveryDayAt(i, 28, new CheckIfVisitInPark());
			PeriodicallyRunner.runEveryDayAt(i, 58, new CheckIfVisitInPark());
		}
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	@Override
	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
	}

	// METHODS DESIGNED TO BE OVERRIDDEN BY CONCRETE SUBCLASSES ---------

	@Override
	protected void clientConnected(ConnectionToClient client) {

		Runnable r = new Runnable() {
			@Override
			public void run() {
				while (client.isAlive()) {
					try {
						client.join();

					} catch (InterruptedException e) {
					}
				}
				clientDisconnected(client);
			}
		};

		new Thread(r).start();
	}

	@Override
	protected void clientDisconnected(ConnectionToClient client) {
		String id = (String) client.getInfo("ID");
		String table = (String) client.getInfo("Table");
		String query;

		if (id == null) {
			System.out.println("client dis!!");
			return;
		}
		if (table.equals("logedin")) {
			query = "DELETE FROM gonaturedb." + table + " WHERE id = " + id + ";";
		} else if (table.equals("employees")) {
			query = "UPDATE gonaturedb." + table + " SET connected = 0 WHERE employeeId = " + client.getInfo("ID")
					+ ";";
		} else {
			query = "UPDATE gonaturedb." + table + " SET connected = 0 WHERE id = " + id + ";";
		}
		DataBase.getInstance().update(query);

	}

}
//End of EchoServer class
