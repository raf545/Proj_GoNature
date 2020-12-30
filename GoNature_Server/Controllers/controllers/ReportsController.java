package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.google.gson.Gson;

import Reports.ReportData;
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
			
		case "VisitorCapacityReport":
			return getVisitorCapacityReport(data, client);

		}
		return "faild";
	}
	
	
	private String getVisitorCapacityReport(String data, ConnectionToClient client) {
		// TODO Auto-generated method stub
		return null;
	}
	private String getRevenueReport(String data, ConnectionToClient client) {
		
		ReportData visitorReport = gson.fromJson(data, ReportData.class);
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
		ReportData visitorReport = gson.fromJson(data, ReportData.class);
		int day;
		int[][] reportdata=new int [7][3];
		for(int i=0;i<7;i++)
		{
			for(int j=0;j<3;j++)
				reportdata[i][j]=0;
		}
		@SuppressWarnings("deprecation")
		Timestamp fromTime = new Timestamp(Integer.parseInt(visitorReport.getYear())-1900, Integer.parseInt(visitorReport.getMonth())-1, 1, 00, 00, 00, 00);
		@SuppressWarnings("deprecation")
		Timestamp toTime = new Timestamp(Integer.parseInt(visitorReport.getYear())-1900,  Integer.parseInt(visitorReport.getMonth())-1, 30, 23, 59, 00, 00);
		//toTime.toLocalDateTime().toLocalDate().getDayOfWeek().getValue();
		try {
			PreparedStatement query1 = con.prepareStatement("SELECT entryTime,numberOfVisitors FROM gonaturedb.cardreader WHERE (typeOfVisitor= \"subscriber\"  || typeOfVisitor= \"guest\") AND (entryTime between ? and ?) AND parkname=?;");
			query1.setTimestamp(1, fromTime);
			query1.setTimestamp(2, toTime);
			query1.setString(3, visitorReport.getParkname());
			ResultSet res = DataBase.getInstance().search(query1);
			while (res.next())
			{
				day=res.getTimestamp(1).toLocalDateTime().toLocalDate().getDayOfWeek().getValue();
				if (day==7)
					day=0;
				reportdata[day][0]=reportdata[day][0] +res.getInt(2);
				
			}
			
			
			PreparedStatement query2 = con.prepareStatement("SELECT entryTime,numberOfVisitors FROM gonaturedb.cardreader WHERE typeOfVisitor= \"family\" AND (entryTime between ? and ?) AND parkname=?;");
			query2.setTimestamp(1, fromTime);
			query2.setTimestamp(2, toTime);
			query2.setString(3, visitorReport.getParkname());
			ResultSet res2 = DataBase.getInstance().search(query2);
			while (res2.next())
			{
				day=res2.getTimestamp(1).toLocalDateTime().toLocalDate().getDayOfWeek().getValue();
				if (day==7)
					day=0;
				reportdata[day][1]=reportdata[day][1] +res2.getInt(2);
				
			}
			
			
			PreparedStatement query3 = con.prepareStatement("SELECT entryTime,numberOfVisitors FROM gonaturedb.cardreader WHERE typeOfVisitor= \"instructor\" AND (entryTime between ? and ?) AND parkname=?;");
			query3.setTimestamp(1, fromTime);
			query3.setTimestamp(2, toTime);
			query3.setString(3, visitorReport.getParkname());
			ResultSet res3 = DataBase.getInstance().search(query3);
			while (res3.next())
			{
				day=res3.getTimestamp(1).toLocalDateTime().toLocalDate().getDayOfWeek().getValue();
				if (day==7)
					day=0;
				reportdata[day][2]=reportdata[day][2] +res3.getInt(2);
					
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return gson.toJson(reportdata,int[][].class);
	}


	
}
