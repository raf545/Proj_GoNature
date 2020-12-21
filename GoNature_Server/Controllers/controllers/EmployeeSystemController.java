package controllers;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.gson.Gson;

import employee.Employee;
import ocsf.server.ConnectionToClient;
import reservation.Reservation;
import sqlConnection.SqlConnector;
import subscriber.Subscriber;

public class EmployeeSystemController {
	Gson gson = new Gson();

	private static EmployeeSystemController EmployeeSystemControllerInstacne = null;

	private EmployeeSystemController() {

	}

	public static EmployeeSystemController getInstance() {

		if (EmployeeSystemControllerInstacne == null)
			EmployeeSystemControllerInstacne = new EmployeeSystemController();
		return EmployeeSystemControllerInstacne;
	}

	public String getFunc(String MethodName, String data, ConnectionToClient client) {

		switch (MethodName) {
		case "addFamilySub":
			return addFamilySub(data, client);
			
		case "addInstructorSub":
			return addInstructorSub(data, client);

		}
		return data;
	}

	private String addFamilySub(String data, ConnectionToClient client) {
		Subscriber subscriber = gson.fromJson(data, Subscriber.class);
		int subidr = 0;
		String query = "SELECT * FROM gonaturedb.subscriber WHERE id = " + subscriber.getId() + " OR subscriberid = "
				+ subscriber.getSubscriberid() + ";";
		ResultSet res = SqlConnector.getInstance().searchInDB(query);
		if (isEmpty(res) != 0)
			return "subscriber already exist";
		query="SELECT * FROM gonaturedb.uptodateinformation WHERE nameOfVal = subscriberID;";
		res=SqlConnector.getInstance().searchInDB(query);
		try {
			subidr=res.getInt("value");
			subscriber.setSubscriberId(String.valueOf(subidr));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		query = "INSERT INTO gonaturedb.subscriber (id, subscriberid, name, lastName, phone, email, numOfMembers, creditCardNumber, subscriberTypre) VALUES "
				+ subscriber.toString() + ";";
		if (SqlConnector.getInstance().updateToDB(query)) {
			subidr++;
			query="UPDATE gonaturedb.uptodateinformation SET value = "+subidr+" WHERE (nameOfVal = subscriberID);";
			return gson.toJson(subscriber);
		}
			
		return "fail";
	}
	
	
	
	private String addInstructorSub(String data, ConnectionToClient client) {
		Subscriber subscriber = gson.fromJson(data, Subscriber.class);
		String query = "SELECT * FROM gonaturedb.subscriber WHERE id = " + subscriber.getId() + " OR subscriberid = "
				+ subscriber.getSubscriberid() + ";";
		ResultSet res = SqlConnector.getInstance().searchInDB(query);
		if (isEmpty(res) != 0)
			return "Instructor already exist";
	
			
		query = "INSERT INTO gonaturedb.subscriber (id, subscriberid, name, lastName, phone, email, numOfMembers, creditCardNumber, subscriberTypre) VALUES "
				+ subscriber.toString() + ";";
		if (SqlConnector.getInstance().updateToDB(query))
			return "success";
		return "fail";
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