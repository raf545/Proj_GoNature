package controllers;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.gson.Gson;

import dataBase.DataBase;
import employee.Employee;
import ocsf.server.ConnectionToClient;
import reservation.Reservation;
import subscriber.Subscriber;

/**
 * This class is responsible for all the actions related to the sign-in and
 * log-out
 * 
 * @author rafael elkoby
 *
 */
public class LoginController {
	Gson gson = new Gson();

	private static LoginController loginControllerInstacne = null;

	private LoginController() {

	}

	/**
	 * 
	 * This method return a instance of the LoginController class and if never
	 * created before creates one
	 * 
	 * @return The instance of logincontroller
	 */
	public static LoginController getInstance() {

		if (loginControllerInstacne == null)
			loginControllerInstacne = new LoginController();
		return loginControllerInstacne;
	}

	/**
	 * 
	 * This method route to a specific login method
	 * 
	 * @param MethodName the kind of login
	 * @param data       the data for the login
	 * @param client     the specific client connection
	 * 
	 * @return fail if there is no such login route
	 */
	
	public String loginRouter(String MethodName, String data, ConnectionToClient client) {

		switch (MethodName) {
		case "Guest ID":
			return GuestID(data, client);
		case "Subscriber":
			return subscriberLogin(data, client);
		case "Reservation ID":
			return ReservationIDLogin(data, client);
		case "employeeLogIn":
			return employeeLogIn(data, client);
		case "logout":
			return logout(client);

		}
		return "fail";
	}

	/**
	 * 
	 * This method is responsible for login as a subscriber,instructor and family
	 * Subscriber
	 * 
	 * 
	 * @param id     the id of the subscriber
	 * @param client the specific client trying to log in
	 * 
	 * 
	 * @return "not subscriber" if the given id is not listed in the database "all
	 *         ready connected" if the id is all ready in use in a client
	 * 
	 * 
	 */
	private String subscriberLogin(String id, ConnectionToClient client) {
		String query = "SELECT * FROM gonaturedb.subscriber WHERE id = " + id + " OR subscriberid = " + id + ";";
		ResultSet res = DataBase.getInstance().search(query);
		try {
			if (isEmpty(res) == 0)
				return "not subscriber";
			if (res.getInt("connected") == 1)
				return "all ready connected";
			client.setInfo("ID", res.getString("id"));
			client.setInfo("Table", "subscriber");
			Subscriber subscriber = new Subscriber(res.getString("id"), res.getString("subscriberid"),
					res.getString("firstName"), res.getString("lastName"), res.getString("phone"),
					res.getString("email"), res.getString("numOfMembers"), res.getString("creditCardNumber"),
					res.getString("subscriberTypre"));
			if (setLoginToDB(client, "subscriber")) {
				return gson.toJson(subscriber);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "error";

	}

	/**
	 * 
	 * This method connects a guest visitor to the system
	 * 
	 * @param guestId the guest id
	 * @param client  the specific client trying to log in
	 * 
	 * @return "all ready connected" if the client is already connected guestId if
	 *         connection established "update failed" if any other case
	 * 
	 * @return data
	 */
	private String GuestID(String guestId, ConnectionToClient client) {
		if (isGuestConnected(guestId))
			return "all ready connected";
		else {
			if (addToTableinDb(guestId, "logedin")) {
				client.setInfo("ID", guestId);
				client.setInfo("Table", "logedin");
				return guestId;
			} else
				return "update faild";
		}

	}

	/**
	 * 
	 * This method is for loging in with a reservation id
	 * 
	 * @param reservationId
	 * @param client        the specific client trying to log in
	 * 
	 * @return "no reservation" if no reservation was found with the given id
	 *         answerFromGuestID if diden't return the same id reservation as gson
	 *         if found "update faild" if login failed "error" for any other case
	 */
	private String ReservationIDLogin(String reservationId, ConnectionToClient client) {
		String query = "SELECT * FROM gonaturedb.reservetions WHERE reservationID = " + reservationId + ";";
		ResultSet reservationDetails = DataBase.getInstance().search(query);
		String answerFromGuestID = null;
		try {
			if (isEmpty(reservationDetails) == 0)
				return "no reservation";

			Reservation reservation = new Reservation(reservationDetails.getString("reservationID"),
					reservationDetails.getString("personalID"), reservationDetails.getString("parkname"),
					reservationDetails.getString("numofvisitors"), reservationDetails.getString("reservationtype"),
					reservationDetails.getString("email"), reservationDetails.getTimestamp("dateAndTime"),
					reservationDetails.getFloat("price"), reservationDetails.getString("reservetionStatus"),
					reservationDetails.getString("phone"));
			String idFromResrvation = reservationDetails.getString("personalID");

			ResultSet detailsOfsubscriberFromReservation = DataBase.getInstance()
					.search("SELECT * FROM gonaturedb.subscriber WHERE id = " + idFromResrvation + ";");
			if (isEmpty(detailsOfsubscriberFromReservation) == 0) {
				answerFromGuestID = GuestID(idFromResrvation, client);
				if (answerFromGuestID.equals(idFromResrvation))
					return gson.toJson(reservation);
				return answerFromGuestID;
			}

			client.setInfo("ID", idFromResrvation);
			client.setInfo("Table", "subscriber");

			if (setLoginToDB(client, "subscriber"))
				return gson.toJson(reservation);
			return "update faild";
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "error";

	}

	/**
	 * 
	 * this method checks if a given ResultSet is empty
	 * 
	 * @param resultSet
	 * @return o if empty , the ResultSet size else
	 */
	private int isEmpty(ResultSet resultSet) {
		int size = 0;
		if (resultSet != null) {
			try {
				resultSet.last(); // moves cursor to the last row
				size = resultSet.getRow(); // get row id
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return size;
	}

	/**
	 * 
	 * This method checks if a given guest id is connected is already connected to a
	 * client
	 * 
	 * @param id
	 * @return true if connected and false else
	 */
	private boolean isGuestConnected(String id) {
		String query = "SELECT * FROM gonaturedb.logedin WHERE id = " + id + ";";
		ResultSet res = DataBase.getInstance().search(query);
		if (isEmpty(res) != 0)
			return true;
		return false;
	}

	/**
	 * 
	 * 
	 * 
	 * @param data
	 * @param tableName
	 * @return
	 */
	private boolean addToTableinDb(String data, String tableName) {
		String query = "INSERT INTO gonaturedb." + tableName + " (id) VALUES (" + data + ");";
		if (DataBase.getInstance().update(query))
			return true;
		return false;
	}

	/**
	 * @param client
	 * @param Table
	 * @return
	 */
	private boolean setLoginToDB(ConnectionToClient client, String Table) {
		String query = "UPDATE gonaturedb." + Table + " SET connected = 1 WHERE id = " + client.getInfo("ID") + ";";
		if (DataBase.getInstance().update(query))
			return true;
		return false;
	}

	/**
	 * @param employeeData
	 * @param client
	 * 
	 * @return "employee not found"
	 * @return "already connected"
	 * @return "wrong password"
	 */
	private String employeeLogIn(String employeeData, ConnectionToClient client) {
		Employee employee = gson.fromJson(employeeData, Employee.class);
		String query = "SELECT * FROM gonaturedb.employees WHERE employeeId = " + employee.getEmployeeId() + ";";
		ResultSet res = DataBase.getInstance().search(query);
		if (isEmpty(res) == 0)
			return "employee not found";

		try {

			if (res.getInt("connected") == 1)
				return "already connected";

			if (employee.getPassword().equals(res.getString("password"))) {
				Employee employeeRet = new Employee(res.getString("employeeId"), res.getString("password"),
						res.getString("name"), res.getString("lasstName"), res.getString("email"),
						res.getString("typeOfEmployee"), res.getString("parkName"));
				client.setInfo("ID", employeeRet.getEmployeeId());
				client.setInfo("Table", "employees");
				setLoginToEmployeeDB(client, "employees");
				return gson.toJson(employeeRet);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "wrong password";
	}

	/**
	 * @param client
	 * @param Table
	 * 
	 * @return
	 * @return
	 */
	private boolean setLoginToEmployeeDB(ConnectionToClient client, String Table) {
		String query = "UPDATE gonaturedb." + Table + " SET connected = 1 WHERE employeeId = " + client.getInfo("ID")
				+ ";";
		if (DataBase.getInstance().update(query))
			return true;
		return false;
	}

	/**
	 * @param client
	 * 
	 * @return ""
	 * @return ""
	 */
	private String logout(ConnectionToClient client) {

		String id = (String) client.getInfo("ID");
		String table = (String) client.getInfo("Table");
		String query;

		if (id == null) {
			return "Client dissconnected";
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
		return "Client dissconnected";
	}

}