package controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.google.gson.Gson;

import dataBase.DataBase;
import ocsf.server.ConnectionToClient;
import reservation.Reservation;


public class DepartmentManagerSystemController {
	
	Gson gson = new Gson();

	private static DepartmentManagerSystemController DepartmentManagerSystemControllerInstance = null;

	private DepartmentManagerSystemController() {

	}

	public static DepartmentManagerSystemController getInstance() {

		if (DepartmentManagerSystemControllerInstance == null)
			DepartmentManagerSystemControllerInstance = new DepartmentManagerSystemController();
		return DepartmentManagerSystemControllerInstance;
	}
	
	public String getFunc(String MethodName, String data, ConnectionToClient client) {

		switch (MethodName) {
		case "showAllApprovesAndRejects":
			return showAllApprovesAndRejects();
		case "updateFieldStatus":
			String[] values = data.split(" ");
			return updateFieldStatus(values[1],values[0]);
		case "updateParkInformation":
			String[] values2 = data.split(" ");
			return updateParkInformation(values2[0],values2[1],values2[2]);		
		case "getEntryDetailsByHours":
			String[] values3 = data.split(" ");
			return getEntryDetailsByHours(values3[0],values3[1],values3[2],values3[3]);
		case "getExitDetailsByHours":
			String[] values4 = data.split(" ");
			return getExitDetailsByHours(values4[0],values4[1],values4[2],values4[3]);
		}
		return data;
	}
	
	//Functions for visitor reports
	private String getEntryDetailsByHours(String type,String year,String month,String day)
	{
		try {
		String query;
		StringBuilder sb = new StringBuilder();
		ResultSet rs;
		
		query = "SELECT HOUR(gonaturedb.cardreader.entryTime) as hoursEntry, SUM(numberOfVisitors) as counter\r\n"
				+ "FROM gonaturedb.cardreader\r\n"
				+ "WHERE typeOfVisitor = \"" + type + "\" AND YEAR(entryTime) = " + year + " AND MONTH(entryTime) = " + month + " AND DAY(entryTime) = " + day +"\r\n"
				+ "GROUP BY HOUR(entryTime)\r\n"
				+ "ORDER BY hoursEntry ASC";
		
			rs = DataBase.getInstance().search(query);
			if (isEmpty(rs) != 0) 
			{
				rs.beforeFirst();
				while(rs.next())
				{
					sb.append(rs.getInt(1) + "," + rs.getInt(2) + " ");				
				}
			}
		return sb.toString();
				
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return "faild";	
	}
	//Functions for visitor reports
	
	private String getExitDetailsByHours(String type,String year,String month,String day)
	{
		try {
		String query;
		StringBuilder sb = new StringBuilder();
		ResultSet rs;
		
		query = "SELECT HOUR(gonaturedb.cardreader.exitTime) as hoursExit, SUM(numberOfVisitors) as counter\r\n"
				+ "FROM gonaturedb.cardreader\r\n"
				+ "WHERE typeOfVisitor = \"" + type + "\" AND YEAR(exitTime) = " + year + " AND MONTH(exitTime) = " + month + " AND DAY(exitTime) = " + day +"\r\n"
				+ "GROUP BY HOUR(exitTime)\r\n"
				+ "ORDER BY hoursExit ASC";
		
			rs = DataBase.getInstance().search(query);
			if (isEmpty(rs) != 0) 
			{
				rs.beforeFirst();
				while(rs.next())
				{
					sb.append(rs.getInt(1) + "," + rs.getInt(2) + " ");				
				}
			}
		return sb.toString();
				
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return "faild";	
	}
	private String showAllApprovesAndRejects() {
		
		try {
			String query="SELECT * FROM gonaturedb.parkdetailsapproval;";
			ResultSet rs = DataBase.getInstance().search(query);
			if (isEmpty(rs) != 0) 
			{
				StringBuilder sb = new StringBuilder();
				rs.beforeFirst();
				while(rs.next())
				{
					//Must Refactor
					sb.append(rs.getString(1) + "," + rs.getString(2) + "," + rs.getString(3) + "," + rs.getString(4)+ "," + rs.getString(5)+ "," + rs.getString(6)+ "," + rs.getString(7)+ "," + rs.getString(8)+ "," + rs.getString(9)+ "," + rs.getString(10) + " ");
				}
				return sb.toString();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return "faild";
	}
	//Change Name to update new stauts for a request
	private String updateFieldStatus(String fieldType,String parkName)
	{
		try
		{		
			String query= "UPDATE `gonaturedb`.`parkdetailsapproval` SET `" + fieldType + "status` = 'finished' WHERE (`parkname` = '" + parkName + "');";
			
			boolean answer = DataBase.getInstance().update(query);
			if (answer) 
				return "true";	
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return "faild";			
	}
	private String updateParkInformation(String parkName,String fieldType,String newData)
	{
		String nameOfVal = "park" + fieldType + parkName;
		try
		{		
			String query= "UPDATE `gonaturedb`.`uptodateinformation` SET `num` = '" + newData +"' WHERE (`nameOfVal` = '" +  nameOfVal + "');";
			
			boolean answer = DataBase.getInstance().update(query);
			if (answer) 
				return "true";	
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return "faild";	
		
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
