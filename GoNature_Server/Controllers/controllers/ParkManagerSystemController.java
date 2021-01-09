package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.google.gson.Gson;

import dataBase.DataBase;
import ocsf.server.ConnectionToClient;
import parkChange.ParkChangeDetails;

/**
 * A server side controller class which deals with all process that are related
 * to the park manager process at the server side
 * 
 * @author Shay Maruma
 *
 */
public class ParkManagerSystemController {
	Gson gson = new Gson();
	Connection con = DataBase.getInstance().getConnection();

	/**
	 * class that saves park change request and get the current values from the
	 * server
	 * 
	 */
	private static ParkManagerSystemController ParkManagerSystemControllerInstacne = null;

	private ParkManagerSystemController() {

	}

	/**
	 * singleton constructor
	 * 
	 * @return instance of this class
	 */
	public static ParkManagerSystemController getInstance() {

		if (ParkManagerSystemControllerInstacne == null)
			ParkManagerSystemControllerInstacne = new ParkManagerSystemController();
		return ParkManagerSystemControllerInstacne;
	}

	/**
	 * differs between change park details request and get the current values of
	 * this park
	 * 
	 * @param MethodName
	 * @param data
	 * @param client
	 * @return answer from server
	 */
	public String getFunc(String MethodName, String data, ConnectionToClient client) {

		switch (MethodName) {
		case "changeParkDetails":
			return changeParkDetails(data, client);
		case "initParkValues":
			return initParkValues(data, client);
		case "getAmountOfPeopleTodayInPark":
			return getAmountOfPeopleTodayInPark(data);

		}
		return "faild";
	}

	/**
	 * @return The amount of people in the worker park
	 */
	private String getAmountOfPeopleTodayInPark(String data)
	{
		try
		{	
		ResultSet rs;
		String [] cap = new String[2];
		PreparedStatement query = con.prepareStatement("SELECT num FROM gonaturedb.uptodateinformation\r\n"
				+ "WHERE nameOfVal = ? \r\n"
				+ "");
		if(data.equals("Banias")) {
			query.setString(1, "BaniasCurrentCapacity");
			rs = DataBase.getInstance().search(query);
			if (isEmpty(rs) != 0) 
				cap[0]=String.valueOf(rs.getInt(1));
			query.setString(1, "parkCapacityBanias");
			rs = DataBase.getInstance().search(query);
			if (isEmpty(rs) != 0) 
				cap[1]=String.valueOf(rs.getInt(1));
		}else if(data.equals("Safari")) {
			query.setString(1, "SafariCurrentCapacity");
			rs = DataBase.getInstance().search(query);
			if (isEmpty(rs) != 0) 
				cap[0]=String.valueOf(rs.getInt(1));
			query.setString(1, "parkCapacitySafari");
			rs = DataBase.getInstance().search(query);
			if (isEmpty(rs) != 0) 
				cap[1]=String.valueOf(rs.getInt(1));
			
		}else  {
			query.setString(1, "NiagaraCurrentCapacity");
			rs = DataBase.getInstance().search(query);
			if (isEmpty(rs) != 0) 
				cap[0]=String.valueOf(rs.getInt(1));
			query.setString(1, "parkCapacityNiagara");
			rs = DataBase.getInstance().search(query);
			if (isEmpty(rs) != 0) 
				cap[1]=String.valueOf(rs.getInt(1));
		}
		
		
		return gson.toJson(cap);
				
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		}	
		return "faild";	
	}

	/**
	 * get the current values of this park
	 * 
	 * @param data
	 * @param client
	 * @return park value
	 */
	private String initParkValues(String data, ConnectionToClient client) {

		String queryc = "SELECT * FROM gonaturedb.uptodateinformation WHERE nameOfVal = \"parkCapacity" + data + "\""
				+ ";";
		String queryd = "SELECT * FROM gonaturedb.uptodateinformation WHERE nameOfVal = \"parkDifference" + data + "\""
				+ ";";
		String queryds = "SELECT * FROM gonaturedb.uptodateinformation WHERE nameOfVal = \"parkDiscount" + data + "\""
				+ ";";

		ResultSet res = DataBase.getInstance().search(queryc);

		if (isEmpty(res) == 0) {
			return "faild";
		}

		ArrayList<String> result1 = new ArrayList<String>();
		try {
			result1.add(String.valueOf(res.getDouble(2)));
		} catch (SQLException e) {
			e.printStackTrace();
		}

		res = DataBase.getInstance().search(queryd);
		if (isEmpty(res) == 0) {
			return "faild";
		}
		try {
			result1.add(String.valueOf(res.getDouble(2)));
		} catch (SQLException e) {
			e.printStackTrace();
		}

		res = DataBase.getInstance().search(queryds);
		if (isEmpty(res) == 0) {
			return "faild";
		}
		try {
			result1.add(String.valueOf(res.getDouble(2)));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return gson.toJson(result1);

	}

	/**
	 * update park details request
	 * 
	 * @param data
	 * @param client
	 * @return answer from server
	 */
	private String changeParkDetails(String data, ConnectionToClient client) {
		ParkChangeDetails parkdetails = gson.fromJson(data, ParkChangeDetails.class);
		String query = "UPDATE `gonaturedb`.`parkdetailsapproval` SET `parkcapacity` = " + parkdetails.getParkCapacity()
				+ ", `difference` = " + parkdetails.getDifference() + ", `discount` = " + parkdetails.getDiscount()
				+ ", `oldparkcapacity` = " + parkdetails.getOldParkCapacity() + ", `olddifference` = "
				+ parkdetails.getOldDifference() + ", `olddiscount` = " + parkdetails.getOldDiscount()
				+ ", `capacitystatus` = 'waiting', `differencestatus` = 'waiting', `discountstatus` = 'waiting' WHERE (`parkname` = "
				+ "\"" + parkdetails.getParkName() + "\"" + ");";

		if (DataBase.getInstance().update(query)) {
			return "The changing are waiting for approval";
		}
		return "faild";
	}

	/**
	 * check if the result set is empty
	 * 
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
