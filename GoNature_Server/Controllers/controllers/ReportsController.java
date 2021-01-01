package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
		int sum[]=new int[30];
		for (int i = 0; i < sum.length; i++) {
			sum[i]=0;
		}
		ReportData visitorReport = gson.fromJson(data, ReportData.class);
		String arr[][]=new String[30][2];
		for (int i = 0; i < 30; i++) {
			for (int k = 0; k < 2; k++) {
				arr[i][k]="";
			}
			
		}
		int capacity=0;
		ArrayList <String> reportdata = new ArrayList<String>();
		@SuppressWarnings("deprecation")
		Timestamp fromTime = new Timestamp(Integer.parseInt(visitorReport.getYear())-1900, Integer.parseInt(visitorReport.getMonth())-1, 1, 00, 00, 00, 00);
//		Timestamp fromTime = Timestamp.valueOf(LocalDateTime.of(Integer.parseInt(visitorReport.getYear()), Integer.parseInt(visitorReport.getMonth()), 1, 00, 00, 00, 00));
		@SuppressWarnings("deprecation")
		Timestamp toTime = new Timestamp(Integer.parseInt(visitorReport.getYear())-1900,  Integer.parseInt(visitorReport.getMonth())-1, 30, 23, 59, 00, 00);
		
		ResultSet res;
		PreparedStatement querycap;
		try {	
			switch (visitorReport.getParkname()) {
			case "Banias":
				querycap = con.prepareStatement("SELECT num FROM gonaturedb.uptodateinformation WHERE nameOfVal='parkCapacityBanias';");
				res = DataBase.getInstance().search(querycap);
				res.next();
				capacity=res.getInt(1);
				break;
			case "Niagara":
				querycap = con.prepareStatement("SELECT num FROM gonaturedb.uptodateinformation WHERE nameOfVal='parkCapacityNiagara';");
				res = DataBase.getInstance().search(querycap);
				res.next();
				capacity=res.getInt(1);
				break;
			case "Safari":
				querycap = con.prepareStatement("SELECT num FROM gonaturedb.uptodateinformation WHERE nameOfVal='parkCapacitySafari';");
				res = DataBase.getInstance().search(querycap);
				res.next();
				capacity=res.getInt(1);
				break;
			}
			capacity=capacity*3;	
			PreparedStatement query1 = con.prepareStatement("SELECT entryTime,numberOfVisitors FROM gonaturedb.cardreader WHERE (entryTime between ? and ?) AND parkname=?;");
			query1.setTimestamp(1, fromTime);
			query1.setTimestamp(2, toTime);
			query1.setString(3, visitorReport.getParkname());
			res = DataBase.getInstance().search(query1);
			while (res.next())
			{
				arr[res.getTimestamp(1).toLocalDateTime().toLocalDate().getDayOfMonth()-1][0]=res.getTimestamp(1).toLocalDateTime().toLocalDate().toString();	
				sum[res.getTimestamp(1).toLocalDateTime().toLocalDate().getDayOfMonth()-1]+=Integer.parseInt(res.getString(2));
			}
			for (int i = 0; i < 30; i++) {
				if(sum[i]-capacity<0)
				{
					
					arr[i][1]=String.format("%.2f",sum[i]/Double.valueOf(capacity));
				}else
				{
					arr[i][0]="";
				}
			}
			int t=0;
			PreparedStatement query=con.prepareStatement("INSERT IGNORE INTO `gonaturedb`.`capacityreport` (`createdate`, `month`, `parkname`, `capacityrep`) VALUES ( ?, ?, ?, ?);");
			for (int i = 0; i < 30; i++) {
				if(!arr[i][0].equals("")) {
					query.setString(1, arr[i][0]);
					t=fromTime.toLocalDateTime().toLocalDate().getMonth().getValue();
					query.setString(2,String.valueOf(t));
					query.setString(3, visitorReport.getParkname());
					query.setString(4, arr[i][1]);
					DataBase.getInstance().update(query);
				}
				
			}
				

		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return gson.toJson(arr,String[][].class);
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
			
			String queryrev = "INSERT IGNORE INTO `gonaturedb`.`revenuereport` (`createdate`, `parkname`, `singlerev`, `familyrev`, `grouprev`) VALUES (" +"\""+fromTime+"\""+", "+"\""+visitorReport.getParkname()+"\""+", "+reportdata.get(0)+", "+reportdata.get(1)+", "+reportdata.get(2)+");";
			DataBase.getInstance().update(queryrev);
			
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
			PreparedStatement querytotalin=con.prepareStatement("INSERT IGNORE INTO `gonaturedb`.`totalvisitorreport` (`createdate`, `parkname`, `singlesun`, `familysun`, `groupsun`, `singlemon`, `familymon`, `groupmon`, `singletues`, `familytues`, `grouptues`, `singlewed`, `familywed`, `groupwed`, `singlethu`, `familythu`, `groupthu`, `singlefri`, `familyfri`, `groupfri`, `singlesat`, `familysat`, `groupsat`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
			querytotalin.setTimestamp(1, fromTime);
			querytotalin.setString(2,visitorReport.getParkname());
			for (int i = 0; i < 7; i++) {
				for (int j = 0; j < 3; j++) {
					querytotalin.setString(3*i+3+j, String.valueOf(reportdata[i][j]));
					
				}
				System.out.println();
				
			}
			DataBase.getInstance().update(querytotalin);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return gson.toJson(reportdata,int[][].class);
	}


	
}
