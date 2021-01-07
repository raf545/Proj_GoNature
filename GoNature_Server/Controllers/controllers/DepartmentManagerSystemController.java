package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import com.google.gson.Gson;
import dataBase.DataBase;
import ocsf.server.ConnectionToClient;


/**
 * All the functions that we use between department manager and the database will be here.
 * @author Shay Maryuma
 *
 */
public class DepartmentManagerSystemController {
	
	Gson gson = new Gson();
	Connection con = DataBase.getInstance().getConnection();

	private static DepartmentManagerSystemController DepartmentManagerSystemControllerInstance = null;

	/**
	 * empty private constructor for singleton
	 */
	private DepartmentManagerSystemController() {

	}

	/**
	 * @return singletone instance of this controller
	 */
	public static DepartmentManagerSystemController getInstance() {

		if (DepartmentManagerSystemControllerInstance == null)
			DepartmentManagerSystemControllerInstance = new DepartmentManagerSystemController();
		return DepartmentManagerSystemControllerInstance;
	}
	
	/**
	 * Each function from department manager will be here.
	 * @param MethodName The name of the method we want to use
	 * @param data The attributes for the method
	 * @param client the link to the client
	 * @return
	 */
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
		case "getAmountOfPeopleTodayInPark":
			return getAmountOfPeopleTodayInPark();	
		}
		return data;
	} 
	
	
	/**
	 * Function that will get the visit report splitted to hours.
	 * For instance, at 8:00 there was 10 people that entered the park
	 * Send details to the server and wait for an answer.
	 * @param type Instructor/Family/Subscriber/Guest
	 * @param year
	 * @param month
	 * @param day
	 * @param park1 Option 1: for example "Banias"
	 * @param park2 Option 2: for example "Niagara"
	 * @param park3 Option 3: for example "Safari"
	 * @return Enter the park details splitted to hours for a specific day.
	 */
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
	/**
	 * Function that will get the visit report splitted to hours.
	 * For instance, at 8:00 there was 10 people that exited the park
	 * Send details to the server and wait for an answer.
	 * @param type Instructor/Family/Subscriber/Guest
	 * @param year
	 * @param month
	 * @param day
	 * @param park1 Option 1: for example "Banias"
	 * @param park2 Option 2: for example "Niagara"
	 * @param park3 Option 3: for example "Safari"
	 * @return Exit the park details splitted to hours for a specific day.
	 */
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
	/**
	 * Function that will be used in report function
	 * @param year
	 * @param month
	 * @param day
	 * @param parkName
	 * @return cancellation details
	 */
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
	/**
	 * Function that will be used in report function
	 * @param year
	 * @param month
	 * @param day
	 * @param parkName
	 * @return half cancellation details
	 */
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
	/**
	 * Function that will be used in report function
	 * @param year
	 * @param month
	 * @param day
	 * @param parkName
	 * @return Number of reservation for a specific day
	 */
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
	
	/**
	 * Get the information about the requests from park manager to department manager.
	 * @return all approves and rejects info from db
	 */
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
					for (int i = 1; i < 10; i++) 
						sb.append(rs.getString(i) + ",");
					
					sb.append(rs.getString(10) + " ");					
				}
				return sb.toString();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return "faild";
	}

	/**
	 * Update the status of a request from "waiting" to "finished"
	 * @param fieldType Instructor/Family/Subscriber/Guest
	 * @param parkName
	 * @return true if updated successfully, otherwise else.
	 */
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
	
	/**
	 * Update new data to park information.
	 * @param parkName name of park
	 * @param fieldType Instructor/Family/Subscriber/Guest
	 * @param newData new data to update in the db
	 * @return
	 */
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
	
	/**
	 * Count the amount of rows in a result set.
	 * @param rs
	 * @return 0 for empty
	 */
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
	
	/**Total visitor for each day at the week for a specific month.
	 * @param year
	 * @param month
	 * @param parkName
	 * @return Visitor report details
	 */
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
	
	/**
	 * Get year and month and return the revenue of a specific park
	 * @param year
	 * @param month
	 * @param parkName
	 * @return the monthly revenue
	 */
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
			sb.append((int)rs.getDouble(3) + " " + (int)rs.getDouble(4) + " " + (int)rs.getDouble(5));		
			return sb.toString();
		}
				
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		}	
		return "faild";	
	}
	
	/**
	 * Get year and month and return the capacity of a specific park
	 * @param year
	 * @param month
	 * @param parkName
	 * @return capacity report of specific park
	 */
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
			rs.beforeFirst();
			while(rs.next())
			{
			sb.append(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " +  rs.getString(4) + ",");
			}
			return sb.toString();
		}
				
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		}	
		return "faild";	
	}
	
	/**
	 * @return The amount of people in Banias, Safari,Niagara in this order.
	 */
	private String getAmountOfPeopleTodayInPark()
	{
		try
		{	
		ResultSet rs;
		StringBuilder sb = new StringBuilder();
		PreparedStatement query = con.prepareStatement("SELECT num FROM gonaturedb.uptodateinformation\r\n"
				+ "WHERE nameOfVal = ? \r\n"
				+ "");
		
		query.setString(1, "BaniasCurrentCapacity");
		rs = DataBase.getInstance().search(query);
		if (isEmpty(rs) != 0) 
			sb.append(rs.getInt(1) + " ");	
		
		query.setString(1, "SafariCurrentCapacity");
		rs = DataBase.getInstance().search(query);
		if (isEmpty(rs) != 0) 
			sb.append(rs.getInt(1) + " ");	
		
		query.setString(1, "NiagaraCurrentCapacity");
		rs = DataBase.getInstance().search(query);
		if (isEmpty(rs) != 0) 
			sb.append(rs.getInt(1) + "");	
		
		return sb.toString();
				
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		}	
		return "faild";	
	}
	
	
	
}
