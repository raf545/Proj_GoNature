package client;

public interface Ichat {

	/**
	 * This method handles the data that comes in from the server about a visitor
	 *
	 * @param msg The message from the server.
	 */
	void handleMessageFromServer(Object msg);

	/**
	 * This method handles all data coming from the UI
	 *
	 * @param message The message from the UI.
	 */

	void handleMessageFromClientUI(Object message);

	/**
	 * This method terminates the client.
	 */
	void quit();

}