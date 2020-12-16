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
		case "GuestID":
			return GuestID(data);
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
//			query = "UPDATE gonaturedb." + Table + " SET connected = 1 WHERE id = " + res.getString(1) + ";";
//			SqlConnector.getInstance().executeToDB(query)
			if (setLoginToDB(client, Table)) {
				return "connected succsesfuly";
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "error";

	}

	private String GuestID(String data) {
		if (isConnected(data))
			return "all ready connected";
		else {
			if (addToTableinDb(data, "logedin"))
				return "connected succsesfuly";
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
		String query = "SELECT * FROM gonaturedb.logedin WHERE id = " + id + ";";
		ResultSet res = SqlConnector.getInstance().searchInDB(query);
		if (isEmpty(res) != 0)
			return true;
		return false;
	}

	private boolean addToTableinDb(String data, String tableName) {
		String query = "INSERT INTO gonaturedb." + tableName + " (id) VALUES (" + data + ");";
		if (SqlConnector.getInstance().executeToDB(query))
			return true;
		return false;
	}

	private boolean setLoginToDB(ConnectionToClient client, String Table) {
		String query = "UPDATE gonaturedb." + Table + " SET connected = 1 WHERE id = " + client.getInfo("ID") + ";";
		if (SqlConnector.getInstance().executeToDB(query))
			return true;
		return false;
	}

}
