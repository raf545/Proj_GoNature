package controllers;

import java.sql.ResultSet;
import java.sql.SQLException;

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

	public String getFunc(String MethodName, String data) {

		switch (MethodName) {
		case "GuestID":
			return GuestID(data);

		}
		return data;
	}

	private String GuestID(String data) {
		String query = "SELECT * FROM gonaturedb.logedin WHERE id = " + data + ";";
		ResultSet res = SqlConnector.getInstance().searchInDB(query);
		if (isEmpty(res) != 0)
			return "all ready connected";
		else {
			query = "INSERT INTO gonaturedb.logedin (id) VALUES (" + data + ");";
			if (SqlConnector.getInstance().executeToDB(query))
				return "connected succsesfuly";
			else
				return "update faild";
		}

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

}
