package controllers;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.gson.Gson;

import logic.Employee;
import logic.Reservation;
import logic.Subscriber;
import ocsf.server.ConnectionToClient;
import sqlConnection.SqlConnector;

public class LoginController {
	Gson gson = new Gson();

	private static LoginController loginControllerInstacne = null;

	private LoginController() {

	}

	public static LoginController getInstance() {

		if (loginControllerInstacne == null)
			loginControllerInstacne = new LoginController();
		return loginControllerInstacne;
	}

	public String getFunc(String MethodName, String data, ConnectionToClient client) {

		switch (MethodName) {
		case "Guest ID":
			return GuestID(data, client);
		case "Subscriber":
			return SubscriberLogin(data, "subscriber", client);
		case "Reservation ID":
			return ReservationIDLogin(data, client);
		case "employeeLogIn":
			return employeeLogIn(data, client);

		}
		return data;
	}

	private String SubscriberLogin(String data, String Table, ConnectionToClient client) {
		String query = "SELECT * FROM gonaturedb." + Table + " WHERE id = " + data + " OR subscriberid = " + data + ";";
		ResultSet res = SqlConnector.getInstance().searchInDB(query);
		try {
			if (isEmpty(res) == 0)
				return "not subscriber";
			if (res.getInt("connected") == 1)
				return "all ready connected";
			client.setInfo("ID", res.getString("id"));
			client.setInfo("Table", Table);
			Subscriber subscriber = new Subscriber(res.getString("id"), res.getString("subscriberid"),
					res.getString("name"), res.getString("lastName"), res.getString("phone"), res.getString("email"),
					res.getString("numOfMembers"), res.getString("creditCardNumber"), res.getString("subscriberTypre"));
			if (setLoginToDB(client, Table)) {
				return gson.toJson(subscriber);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Check error case and create popup
		return "error";

	}

	private String GuestID(String data, ConnectionToClient client) {
		if (isConnected(data))
			return "all ready connected";
		else {
			if (addToTableinDb(data, "logedin")) {
				client.setInfo("ID", data);
				client.setInfo("Table", "logedin");
				return data;
			} else
				return "update faild";
		}

	}

	private String ReservationIDLogin(String data, ConnectionToClient client) {
		String query = "SELECT * FROM gonaturedb.reservetions WHERE reservationID = " + data + ";";
		ResultSet res = SqlConnector.getInstance().searchInDB(query);
		String answerFromGuestID = null;
		try {
			if (isEmpty(res) == 0)
				return "no reservation";

			Reservation reservation = new Reservation(res.getString("reservationID"), res.getString("personalID"),
					res.getString("parkname"), res.getString("visithour"), res.getString("numofvisitors"),
					res.getString("reservationtype"), res.getString("email"), res.getString("date"),
					res.getFloat("price"), res.getString("reservetionStatus"));
			String idFromResrvation = res.getString("personalID");

			res = SqlConnector.getInstance()
					.searchInDB("SELECT * FROM gonaturedb.subscriber WHERE id = " + idFromResrvation + ";");
			if (isEmpty(res) == 0) {
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "error";

	}

	private int isEmpty(ResultSet rs) {
		int size = 0;
		if (rs != null) {
			try {
				rs.last(); // moves cursor to the last row
				size = rs.getRow(); // get row id
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return size;
	}

	private boolean isConnected(String id) {
		// TODO is gust coonected? why there is no discription
		String query = "SELECT * FROM gonaturedb.logedin WHERE id = " + id + ";";
		ResultSet res = SqlConnector.getInstance().searchInDB(query);
		if (isEmpty(res) != 0)
			return true;
		return false;
	}

	private boolean addToTableinDb(String data, String tableName) {
		String query = "INSERT INTO gonaturedb." + tableName + " (id) VALUES (" + data + ");";
		if (SqlConnector.getInstance().updateToDB(query))
			return true;
		return false;
	}

	private boolean setLoginToDB(ConnectionToClient client, String Table) {
		String query = "UPDATE gonaturedb." + Table + " SET connected = 1 WHERE id = " + client.getInfo("ID") + ";";
		if (SqlConnector.getInstance().updateToDB(query))
			return true;
		return false;
	}

	private String employeeLogIn(String employeeData, ConnectionToClient client) {
		Employee employee = gson.fromJson(employeeData, Employee.class);
		String query = "SELECT * FROM gonaturedb.employees WHERE employeeId = " + employee.getEmployeeId() + ";";
		ResultSet res = SqlConnector.getInstance().searchInDB(query);
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

	private boolean setLoginToEmployeeDB(ConnectionToClient client, String Table) {
		String query = "UPDATE gonaturedb." + Table + " SET connected = 1 WHERE employeeId = " + client.getInfo("ID")
				+ ";";
		if (SqlConnector.getInstance().updateToDB(query))
			return true;
		return false;
	}

}