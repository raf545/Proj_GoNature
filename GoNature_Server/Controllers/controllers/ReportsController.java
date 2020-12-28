package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

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
		
		return null;
	}
	private String getTotalVisitorsReport(String data, ConnectionToClient client) {
		TotalVisitorsReport visitorReport = gson.fromJson(data, TotalVisitorsReport.class);
		int boded=0;
		int family=0;
		int group=0;
		@SuppressWarnings("deprecation")
		Timestamp fromTime = new Timestamp(Integer.parseInt(visitorReport.getYear())-1900, Integer.parseInt(visitorReport.getMonth())-1, 1, 00, 00, 00, 00);
		@SuppressWarnings("deprecation")
		Timestamp toTime = new Timestamp(Integer.parseInt(visitorReport.getYear())-1900,  Integer.parseInt(visitorReport.getMonth())-1, 30, 23, 59, 00, 00);
		System.out.println();
		try {
			PreparedStatement query1 = con.prepareStatement("SELECT SUM(CONVERT(INT, numberOfVisitors)) FROM gonaturedb.cardreader WHERE (typeOfVisitor= \"subscriber\"  || typeOfVisitor= \"guest\") AND (entryTime between ? and ?) AND parkname=?;");
			query1.setTimestamp(1, fromTime);
			query1.setTimestamp(2, toTime);
			query1.setString(3, visitorReport.getParkname());
			ResultSet res = DataBase.getInstance().search(query1);
			if (DataBase.getInstance().isEmpty(res) != 0)
				boded = res.getInt("sum(numofvisitors)");
			System.out.println(boded);
		//	while(res.next())
		//		counter += res.getString("numberOfVisitors");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String quarey2="SELECT SUM(CONVERT(INT, numberOfVisitors)) FROM cardreader WHERE typeOfVisitor==family ;";
		String quarey3="SELECT SUM(CONVERT(INT, numberOfVisitors)) FROM cardreader WHERE typeOfVisitor==instructor ;";
	//	int t=Integer.parseInt(quarey1)+Integer.parseInt(quarey2)+Integer.parseInt(quarey3);
		//String total=String.valueOf(t);
		return null;
	}


	
}
