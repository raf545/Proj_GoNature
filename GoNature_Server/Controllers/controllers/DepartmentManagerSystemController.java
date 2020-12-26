package controllers;

import java.sql.ResultSet;
import java.sql.SQLException;

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
		case "updateNewChangesToDB":
			String[] values = data.split(" ");
			return updateNewChangesToDB(values[0],values[1]);

		}
		return data;
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
	private String updateNewChangesToDB(String status,String parkName)
	{
//		try {
//			
//			String myStatus = gson.fromJson(status, String.class);
//			String myParkName = gson.fromJson(parkName, String.class);
//			String query="UPDATE `gonaturedb`.`parkdetailsapproval` SET '" + myStatus + "'= 'finished' WHERE (`parkname` = '" + myParkName +"');\r\n"
//					+ "";
//			
//			boolean answer = DataBase.getInstance().update(query);
//			if (answer) 
//			{
//				return "true";	
//			}
//		} 
//		catch (Exception e) {
//			e.printStackTrace();
//		}
//		
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
