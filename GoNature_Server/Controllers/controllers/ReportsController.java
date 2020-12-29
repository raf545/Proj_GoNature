package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.google.gson.Gson;

import Reports.TotalVisitorsReport;
import dataBase.DataBase;
import ocsf.server.ConnectionToClient;

public class ReportsController {
	Gson gson = new Gson();
	Connection con = DataBase.getInstance().getConnection();

	private static ReportsController reportsControllerGetInstance = null;

	private ReportsController() {

	}
	public static ReportsController getInstance() {

		if (reportsControllerGetInstance == null)
			reportsControllerGetInstance = new ReportsController();
		return reportsControllerGetInstance ;
	}

	public String getFunc(String MethodName, String data, ConnectionToClient client) {

		switch (MethodName) {
		case "TotalVisitorsReport":
			return getTotalVisitorsReport(data, client);
		case "RevenueReport":
			return getRevenueReport(data, client);

		}
		return "faild";
	}
	
	
	private String getRevenueReport(String data, ConnectionToClient client) {
		
		TotalVisitorsReport visitorReport = gson.fromJson(data, TotalVisitorsReport.class);
		ArrayList <String> reportdata = new ArrayList<String>();
		@SuppressWarnings("deprecation")
		Timestamp fromTime = new Timestamp(Integer.parseInt(visitorReport.getYear())-1900, Integer.parseInt(visitorReport.getMonth())-1, 1, 00, 00, 00, 00);
		@SuppressWarnings("deprecation")
		Timestamp toTime = new Timestamp(Integer.parseInt(visitorReport.getYear())-1900,  Integer.parseInt(visitorReport.getMonth())-1, 30, 23, 59, 00, 00);
		try {
			PreparedStatement query1 = con.prepareStatement("SELECT SUM(price) FROM gonaturedb.cardreader WHERE (typeOfVisitor= \"subscriber\"  || typeOfVisitor= \"guest\") AND (entryTime between ? and ?) AND parkname=?;");
			query1.setTimestamp(1, fromTime);
			query1.setTimestamp(2, toTime);
			query1.setString(3, visitorReport.getParkname());
			ResultSet res = DataBase.getInstance().search(query1);
			if (DataBase.getInstance().isEmpty(res) != 0)
				reportdata.add(String.valueOf(res.getDouble(1)));
			else
				reportdata.add(String.valueOf(0));
			
			PreparedStatement query2 = con.prepareStatement("SELECT SUM(price) FROM gonaturedb.cardreader WHERE typeOfVisitor= \"family\" AND (entryTime between ? and ?) AND parkname=?;");
			query2.setTimestamp(1, fromTime);
			query2.setTimestamp(2, toTime);
			query2.setString(3, visitorReport.getParkname());
			ResultSet res2 = DataBase.getInstance().search(query2);
			if (DataBase.getInstance().isEmpty(res2) != 0)
				reportdata.add(String.valueOf(res2.getDouble(1)));
			else
				reportdata.add(String.valueOf(0));
			
			
			PreparedStatement query3 = con.prepareStatement("SELECT SUM(price) FROM gonaturedb.cardreader WHERE typeOfVisitor= \"instructor\" AND (entryTime between ? and ?) AND parkname=?;");
			query3.setTimestamp(1, fromTime);
			query3.setTimestamp(2, toTime);
			query3.setString(3, visitorReport.getParkname());
			ResultSet res3 = DataBase.getInstance().search(query3);
			if (DataBase.getInstance().isEmpty(res3) != 0)
				reportdata.add(String.valueOf(res3.getDouble(1)));
			else
				reportdata.add(String.valueOf(0));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return gson.toJson(reportdata);
	}
	
	
	
	
	
	
	
	private String getTotalVisitorsReport(String data, ConnectionToClient client) {
		TotalVisitorsReport visitorReport = gson.fromJson(data, TotalVisitorsReport.class);
		ArrayList <String> reportdata = new ArrayList<String>();
		@SuppressWarnings("deprecation")
		Timestamp fromTime = new Timestamp(Integer.parseInt(visitorReport.getYear())-1900, Integer.parseInt(visitorReport.getMonth())-1, 1, 00, 00, 00, 00);
		@SuppressWarnings("deprecation")
		Timestamp toTime = new Timestamp(Integer.parseInt(visitorReport.getYear())-1900,  Integer.parseInt(visitorReport.getMonth())-1, 30, 23, 59, 00, 00);
		try {
			PreparedStatement query1 = con.prepareStatement("SELECT SUM(CONVERT(numberOfVisitors,DECIMAL)) FROM gonaturedb.cardreader WHERE (typeOfVisitor= \"subscriber\"  || typeOfVisitor= \"guest\") AND (entryTime between ? and ?) AND parkname=?;");
			query1.setTimestamp(1, fromTime);
			query1.setTimestamp(2, toTime);
			query1.setString(3, visitorReport.getParkname());
			ResultSet res = DataBase.getInstance().search(query1);
			if (DataBase.getInstance().isEmpty(res) != 0)
				reportdata.add(String.valueOf(res.getInt(1)));
			else
				reportdata.add(String.valueOf(0));
			
			PreparedStatement query2 = con.prepareStatement("SELECT SUM(CONVERT(numberOfVisitors,DECIMAL)) FROM gonaturedb.cardreader WHERE typeOfVisitor= \"family\" AND (entryTime between ? and ?) AND parkname=?;");
			query2.setTimestamp(1, fromTime);
			query2.setTimestamp(2, toTime);
			query2.setString(3, visitorReport.getParkname());
			ResultSet res2 = DataBase.getInstance().search(query2);
			if (DataBase.getInstance().isEmpty(res2) != 0)
				reportdata.add(String.valueOf(res2.getInt(1)));
			else
				reportdata.add(String.valueOf(0));
			
			
			PreparedStatement query3 = con.prepareStatement("SELECT SUM(CONVERT(numberOfVisitors,DECIMAL)) FROM gonaturedb.cardreader WHERE typeOfVisitor= \"instructor\" AND (entryTime between ? and ?) AND parkname=?;");
			query3.setTimestamp(1, fromTime);
			query3.setTimestamp(2, toTime);
			query3.setString(3, visitorReport.getParkname());
			ResultSet res3 = DataBase.getInstance().search(query3);
			if (DataBase.getInstance().isEmpty(res3) != 0)
				reportdata.add(String.valueOf(res3.getInt(1)));
			else
				reportdata.add(String.valueOf(0));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return gson.toJson(reportdata);
	}


	
}
