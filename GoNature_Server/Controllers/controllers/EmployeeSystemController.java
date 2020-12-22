package controllers;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.gson.Gson;

import dataBase.DataBase;
import employee.Employee;
import ocsf.server.ConnectionToClient;
import reservation.Reservation;
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
		ResultSet res = DataBase.getInstance().search(query);
		if (isEmpty(res) != 0)
			return "subscriber already exist";
		query = "SELECT * FROM gonaturedb.uptodateinformation WHERE nameOfVal = \"subscriberID\";";
		res = DataBase.getInstance().search(query);
		if (isEmpty(res) == 0) {
			System.out.println("here!!");
			return "";
		}
		try {
			System.out.println(res.getString(1) + "!!!!!!");
			subidr = res.getInt(2);
			subscriber.setSubscriberId(String.valueOf(subidr));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		query = "INSERT INTO gonaturedb.subscriber (id, subscriberid, name, lastName, phone, email, numOfMembers, creditCardNumber, subscriberTypre) VALUES "
				+ subscriber.toString() + ";";
		if (DataBase.getInstance().update(query)) {
			subidr++;
			query = "UPDATE gonaturedb.uptodateinformation SET num = " + subidr + " WHERE (nameOfVal = \"subscriberID\");";
			if (DataBase.getInstance().update(query)) {
				return gson.toJson(subscriber);
			}
		}

		return "fail";
	}

	private String addInstructorSub(String data, ConnectionToClient client) {
		Subscriber subscriber = gson.fromJson(data, Subscriber.class);
		String query = "SELECT * FROM gonaturedb.subscriber WHERE id = " + subscriber.getId() + " OR subscriberid = "
				+ subscriber.getSubscriberid() + ";";
		ResultSet res = DataBase.getInstance().search(query);
		if (isEmpty(res) != 0)
			return "Instructor already exist";

		query = "INSERT INTO gonaturedb.subscriber (id, subscriberid, name, lastName, phone, email, numOfMembers, creditCardNumber, subscriberTypre) VALUES "
				+ subscriber.toString() + ";";
		if (DataBase.getInstance().update(query))
			return "success";
		return "fail";
	}

	private int isEmpty(ResultSet rs) {
		int size = 0;
		if (rs != null) {
			try {
				rs.last();
				size = rs.getRow(); 
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return size;
	}

}