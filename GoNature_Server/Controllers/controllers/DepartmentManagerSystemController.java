package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.google.gson.Gson;

import dataBase.DataBase;
import ocsf.server.ConnectionToClient;
import reservation.Reservation;


public class DepartmentManagerSystemController {
	
	Gson gson = new Gson();
	Connection con = DataBase.getInstance().getConnection();

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
			return getEntryDetailsByHours(values3[0],values3[1],values3[2],values3[3],values3[4],values3[5],values3[6]);
		case "getExitDetailsByHours":
			String[] values4 = data.split(" ");
			return getExitDetailsByHours(values4[0],values4[1],values4[2],values4[3],values4[4],values4[5],values4[6]);
		case "getReservationCancelationDetails":
			String[] values5 = data.split(" ");
			return getReservationCancelationDetails(values5[0],values5[1],values5[2],values5[3]);
		case "getNumberOfReservationForSpecificDay":
			String[] values6 = data.split(" ");
			return getNumberOfReservationForSpecificDay(values6[0],values6[1],values6[2],values6[3]);
		case "getReservationHalfCancelationDetails":
			String[] values7 = data.split(" ");
			return getReservationHalfCancelationDetails(values7[0],values7[1],values7[2],values7[3]);
		//Get from park manager reports
		case "getTotalVisitorReportFromParkManager":
			String[] values8 = data.split(" ");
			return getTotalVisitorReportFromParkManager(values8[0],values8[1],values8[2]);
		case "getMonthlyRevenueFromDB":
			String[] values9 = data.split(" ");
			return getMonthlyRevenueFromDB(values9[0],values9[1],values9[2]);
		case "getParkManagerCapacityReport":
			String[] values10 = data.split(" ");
			return getParkManagerCapacityReport(values10[0],values10[1],values10[2]);			
		}
		return data;
	} 
	
	//Functions for visitor reports
	private String getEntryDetailsByHours(String type,String year,String month,String day,String park1,String park2,String park3)
	{
		try {
		//String query;
		StringBuilder sb = new StringBuilder();
		ResultSet rs;
		
		PreparedStatement query = con.prepareStatement("SELECT HOUR(gonaturedb.cardreader.entryTime) as hoursEntry, SUM(numberOfVisitors) as counter\r\n"
				+ "FROM gonaturedb.cardreader\r\n"
				+ "WHERE typeOfVisitor = ? AND YEAR(entryTime) = ? AND MONTH(entryTime) = ? AND DAY(entryTime) = ? AND (parkName = ? OR parkName = ? OR parkName = ?)\r\n"
				+ "GROUP BY HOUR(entryTime)\r\n"
				+ "ORDER BY hoursEntry ASC");
		
		query.setString(1, type);
		query.setInt(2, Integer.parseInt(year));
		query.setInt(3, Integer.parseInt(month));
		query.setInt(4, Integer.parseInt(day));
		query.setString(5, park1);
		query.setString(6, park2);
		query.setString(7, park3);
		
//		query = "SELECT HOUR(gonaturedb.cardreader.entryTime) as hoursEntry, SUM(numberOfVisitors) as counter\r\n"
//				+ "FROM gonaturedb.cardreader\r\n"
//				+ "WHERE typeOfVisitor = \"" + type + "\" AND YEAR(entryTime) = " + year + " AND MONTH(entryTime) = " + month + " AND DAY(entryTime) = " + day +"\r\n "
//				+ "GROUP BY HOUR(entryTime)\r\n"
//				+ "ORDER BY hoursEntry ASC";

		
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
	
	private String getExitDetailsByHours(String type,String year,String month,String day,String park1,String park2,String park3)
	{
		try {
		StringBuilder sb = new StringBuilder();
		ResultSet rs;
		
		PreparedStatement query = con.prepareStatement("SELECT HOUR(gonaturedb.cardreader.exitTime) as hoursExit, SUM(numberOfVisitors) as counter\r\n"
				+ "FROM gonaturedb.cardreader\r\n"
				+ "WHERE typeOfVisitor = ? AND YEAR(exitTime) = ? AND MONTH(exitTime) = ? AND DAY(exitTime) = ? AND (parkName = ? OR parkName = ? OR parkName = ?)\r\n"
				+ "GROUP BY HOUR(exitTime)\r\n"
				+ "ORDER BY hoursExit ASC");
		
		query.setString(1, type);
		query.setInt(2, Integer.parseInt(year));
		query.setInt(3, Integer.parseInt(month));
		query.setInt(4, Integer.parseInt(day));
		query.setString(5, park1);
		query.setString(6, park2);
		query.setString(7, park3);

//		query = "SELECT HOUR(gonaturedb.cardreader.exitTime) as hoursExit, SUM(numberOfVisitors) as counter\r\n"
//				+ "FROM gonaturedb.cardreader\r\n"
//				+ "WHERE typeOfVisitor = \"" + type + "\" AND YEAR(exitTime) = " + year + " AND MONTH(exitTime) = " + month + " AND DAY(exitTime) = " + day +"\r\n"
//				+ "GROUP BY HOUR(exitTime)\r\n"
//				+ "ORDER BY hoursExit ASC";
		
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
	//CancellationReport
	private String getReservationCancelationDetails(String year,String month,String day,String parkName)
	{
		try
		{	
		ResultSet rs;
		StringBuilder sb = new StringBuilder();
		PreparedStatement query = con.prepareStatement("SELECT gonaturedb.reservetions.dateAndTime as theDay, SUM(numofvisitors) as NumberOfPeopleCanceled\r\n"
				+ "FROM gonaturedb.reservetions\r\n"
				+ "WHERE YEAR(dateAndTime) = ? AND MONTH(dateAndTime) = ? AND DAY(dateAndTime) = ? AND parkName = ? AND (reservetionStatus = \"Canceled\")\r\n"
				+ "");
		
		query.setInt(1, Integer.parseInt(year));
		query.setInt(2, Integer.parseInt(month));
		query.setInt(3, Integer.parseInt(day));
		query.setString(4, parkName);

		rs = DataBase.getInstance().search(query);
		if (isEmpty(rs) != 0) 
		{
			rs.first();
			sb.append(rs.getInt(2));
			return sb.toString();
		}
				
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		}	
		return "faild";	
	}
	private String getReservationHalfCancelationDetails(String year,String month,String day,String parkName)
	{
		try
		{	
		ResultSet rs;
		StringBuilder sb = new StringBuilder();
		PreparedStatement query = con.prepareStatement("SELECT gonaturedb.reservetions.dateAndTime as theDay, SUM(numofvisitors) as NumberOfPeopleCanceled\r\n"
				+ "FROM gonaturedb.reservetions\r\n"
				+ "WHERE YEAR(dateAndTime) = ? AND MONTH(dateAndTime) = ? AND DAY(dateAndTime) = ? AND parkName = ? AND (reservetionStatus = \"halfCanceled\")\r\n"
				+ "");
		
		query.setInt(1, Integer.parseInt(year));
		query.setInt(2, Integer.parseInt(month));
		query.setInt(3, Integer.parseInt(day));
		query.setString(4, parkName);

		rs = DataBase.getInstance().search(query);
		if (isEmpty(rs) != 0) 
		{
			rs.first();
			sb.append(rs.getInt(2));
			return sb.toString();
		}
				
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		}	
		return "faild";	
	}
	private String getNumberOfReservationForSpecificDay(String year,String month,String day,String parkName)
	{
		try
		{	
		ResultSet rs;	
		PreparedStatement query = con.prepareStatement("SELECT gonaturedb.reservetions.dateAndTime as theDay, SUM(numofvisitors) as NumberOfPeopleCanceled\r\n"
				+ "FROM gonaturedb.reservetions\r\n"
				+ "WHERE YEAR(dateAndTime) = ? AND MONTH(dateAndTime) = ? AND DAY(dateAndTime) = ? AND parkName = ? \r\n");
		
		query.setInt(1, Integer.parseInt(year));
		query.setInt(2, Integer.parseInt(month));
		query.setInt(3, Integer.parseInt(day));
		query.setString(4, parkName);


		rs = DataBase.getInstance().search(query);
		if (isEmpty(rs) != 0) 
		{
			rs.first();
			return rs.getInt(2) + "";
		}
				
		} 
		catch (SQLException e)
		{
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
			try
			{
				rs.last();
				size = rs.getRow(); 
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return size;
	}
	
	//For park manager reports -----------------------------------------------------------------
	private String getTotalVisitorReportFromParkManager(String year,String month,String parkName)
	{
		try
		{	
		ResultSet rs;
		StringBuilder sb = new StringBuilder();
		PreparedStatement query = con.prepareStatement("SELECT * FROM gonaturedb.totalvisitorreport\r\n"
				+ "WHERE YEAR(createdate) = ? AND MONTH(createdate) = ?  AND parkName = ?");
		
		query.setInt(1, Integer.parseInt(year));
		query.setInt(2, Integer.parseInt(month));
		query.setString(3, parkName);

		rs = DataBase.getInstance().search(query);
		if (isEmpty(rs) != 0) 
		{
			for (int i = 2; i < 24; i++) 
				sb.append(rs.getString(i) + " ");
				
			return sb.toString();
		}
				
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		}	
		return "faild";	
	}
	
	private String getMonthlyRevenueFromDB(String year,String month,String parkName)
	{
		try
		{	
		ResultSet rs;
		StringBuilder sb = new StringBuilder();
		PreparedStatement query = con.prepareStatement("SELECT * FROM gonaturedb.revenuereport\r\n"
				+ "WHERE YEAR(createdate) = ? AND MONTH(createdate) = ?  AND parkName = ?");
		
		query.setInt(1, Integer.parseInt(year));
		query.setInt(2, Integer.parseInt(month));
		query.setString(3, parkName);

		rs = DataBase.getInstance().search(query);
		if (isEmpty(rs) != 0) 
		{
			sb.append(rs.getString(3) + " " + rs.getString(4) + " " + rs.getString(5));		
			return sb.toString();
		}
				
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		}	
		return "faild";	
	}
	private String getParkManagerCapacityReport(String year,String month,String parkName)
	{
		try
		{	
		ResultSet rs;
		StringBuilder sb = new StringBuilder();
		PreparedStatement query = con.prepareStatement("SELECT * FROM gonaturedb.capacityreport\r\n"
				+ "WHERE YEAR(createdate) = ? AND MONTH(createdate) = ?  AND parkName = ?");
		
		query.setInt(1, Integer.parseInt(year));
		query.setInt(2, Integer.parseInt(month));
		query.setString(3, parkName);

		rs = DataBase.getInstance().search(query);
		if (isEmpty(rs) != 0) 
		{
			sb.append(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " +  rs.getString(4) + ",");		
			return sb.toString();
		}
				
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		}	
		return "faild";	
	}
	
	
	
}
