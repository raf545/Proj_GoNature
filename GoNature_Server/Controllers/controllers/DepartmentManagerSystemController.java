package controllers;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.gson.Gson;

import dataBase.DataBase;
import ocsf.server.ConnectionToClient;
import parkChange.ParkChangeDetails;


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
	
	public String getFunc(String MethodName, String data, ConnectionToClient client) throws SQLException {

		switch (MethodName) {
		case "approveParkChanges":
			return approveParkChanges();

		}
		return data;
	}

	private String approveParkChanges() throws SQLException {
		
		String query="SELECT * FROM gonaturedb.parkdetailsapproval;";
		ResultSet rs = DataBase.getInstance().search(query);
		if (isEmpty(rs) != 0) 
		{
			StringBuilder sb = new StringBuilder();
			rs.beforeFirst();
			while(rs.next())
			{
				sb.append(rs.getString(1) + "," + rs.getString(2) + "," + rs.getString(3) + " ");
			}
			return sb.toString();
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
