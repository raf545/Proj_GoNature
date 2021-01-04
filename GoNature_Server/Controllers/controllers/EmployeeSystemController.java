package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.gson.Gson;

import dataBase.DataBase;
import ocsf.server.ConnectionToClient;
import subscriber.Subscriber;

/**class that save family and instructor changes into the data base ,changes that employee can do  
 * @author zivi9
 *
 */
public class EmployeeSystemController {
	Gson gson = new Gson();
	Connection con = DataBase.getInstance().getConnection();

	private static EmployeeSystemController EmployeeSystemControllerInstacne = null;

	private EmployeeSystemController() {

	}

	/**singleton constructor
	 * @return instance of this class
	 */
	public static EmployeeSystemController getInstance() {

		if (EmployeeSystemControllerInstacne == null)
			EmployeeSystemControllerInstacne = new EmployeeSystemController();
		return EmployeeSystemControllerInstacne;
	}

	/**differs between add family request or an instructor request
	 * @param MethodName
	 * @param data
	 * @param client
	 * @return answer from server
	 */
	public String getFunc(String MethodName, String data, ConnectionToClient client) {

		switch (MethodName) {
		case "addFamilySub":
			return addFamilySub(data, client);

		case "addInstructorSub":
			return addInstructorSub(data, client);
		case "getAmountOfPeopleTodayInPark":
			return getAmountOfPeopleTodayInPark(data);	

		}
		return data;
	}

	/**
	 * @return The amount of people in the worker park
	 */
	private String getAmountOfPeopleTodayInPark(String data)
	{
		try
		{	
		ResultSet rs;
		String cap = null;
		PreparedStatement query = con.prepareStatement("SELECT num FROM gonaturedb.uptodateinformation\r\n"
				+ "WHERE nameOfVal = ? \r\n"
				+ "");
		if(data.equals("Banias")) {
			query.setString(1, "BaniasCurrentCapacity");
			rs = DataBase.getInstance().search(query);
			if (isEmpty(rs) != 0) 
				cap=String.valueOf(rs.getInt(1));
		}else if(data.equals("Safari")) {
			query.setString(1, "SafariCurrentCapacity");
			rs = DataBase.getInstance().search(query);
			if (isEmpty(rs) != 0) 
				cap=String.valueOf(rs.getInt(1));
		}else  {
			query.setString(1, "NiagaraCurrentCapacity");
			rs = DataBase.getInstance().search(query);
			if (isEmpty(rs) != 0) 
				cap=String.valueOf(rs.getInt(1));
		}
		
		
		return cap;
				
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		}	
		return "faild";	
	}

	/**save family subscriber into the data base
	 * @param data
	 * @param client
	 * @return answer from the server
	 */
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
			
			return "faild to get subscriber id";
		}
		try {
			
			subidr = res.getInt(2);
			subscriber.setSubscriberId(String.valueOf(subidr));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (Integer.parseInt(subscriber.getNumOfMembers())==1)
		{
			subscriber.setSubscriberTypre("subscriber");
		}
	
		query = "INSERT INTO gonaturedb.subscriber (id, subscriberid, firstName, lastName, phone, email, numOfMembers, creditCardNumber, subscriberTypre) VALUES "
				+ subscriber.toString2() + ";";
		if (DataBase.getInstance().update(query)) {
			subidr++;
			query = "UPDATE gonaturedb.uptodateinformation SET num = " + subidr + " WHERE (nameOfVal = \"subscriberID\");";
			if (DataBase.getInstance().update(query)) {
				return gson.toJson(subscriber);
			}
		}

		return "fail";
	}

	/**save family subscriber into the data base
	 * @param data
	 * @param client
	 * @return answer from the server
	 */
	private String addInstructorSub(String data, ConnectionToClient client) {
		int subidr = 0;
		Subscriber subscriber = gson.fromJson(data, Subscriber.class);
		String query = "SELECT * FROM gonaturedb.subscriber WHERE id = " + subscriber.getId() + " OR subscriberid = "
				+ subscriber.getSubscriberid() + ";";
		ResultSet res = DataBase.getInstance().search(query);
		if (isEmpty(res) != 0)
			return "Instructor already exist";
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
			e.printStackTrace();
		}
		query = "INSERT INTO gonaturedb.subscriber (id, subscriberid, firstName, lastName, phone, email, numOfMembers, creditCardNumber, subscriberTypre) VALUES "
				+ subscriber.toString2() + ";";
		if (DataBase.getInstance().update(query)) {
			subidr++;
			query = "UPDATE gonaturedb.uptodateinformation SET num = " + subidr + " WHERE (nameOfVal = \"subscriberID\");";
			if (DataBase.getInstance().update(query)) {
				return gson.toJson(subscriber);
			}
		}
		return "fail";
	}

	/**check if the result set is empty
	 * @param rs
	 * @return the numbers of the rows in the result set
	 */
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