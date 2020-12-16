package controllers;

import java.sql.ResultSet;
import java.sql.SQLException;

import ocsf.server.ConnectionToClient;
import sqlConnection.SqlConnector;

public class LoginController {

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
		case "Family subscriber":
			return SubscriberLogin(data, "familySubscriber", client);
		case "Instructor":
			return SubscriberLogin(data, "instructor", client);
		case "Reservation ID":
			return ReservationIDLogin(data, client);

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
			if (setLoginToDB(client, Table)) {
				return "connected succsesfuly";
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//TODO Check error case and create popup
		return "error";

	}

	private String GuestID(String data, ConnectionToClient client) {
		if (isConnected(data))
			return "all ready connected";
		else {
			if (addToTableinDb(data, "logedin")) {
				client.setInfo("ID", data);
				client.setInfo("Table", "logedin");
				return "connected succsesfuly";
			}
			else
				return "update faild";
		}

	}

	private String ReservationIDLogin(String data, ConnectionToClient client) {
		String query = "SELECT * FROM gonaturedb.reservetions WHERE reservationID = " + data + ";";
		ResultSet res = SqlConnector.getInstance().searchInDB(query);
		try {
			if (isEmpty(res) == 0)
				return "no reservation";
			switch (res.getString("reservationtype")) {
			case "subscriber":
				return SubscriberLogin(res.getString("personalID"), "subscriber", client);
			case "familySubscriber":
				return SubscriberLogin(res.getString("personalID"), "familySubscriber", client);
			case "instructor":
				return SubscriberLogin(res.getString("personalID"), "instructor", client);
			}
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

}
