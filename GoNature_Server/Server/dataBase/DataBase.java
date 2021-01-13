package dataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This Class is responsible of Data Base management and manipulations
 * 
 * @author Dan Gutchin
 * @author Yaniv Sokolov
 * @author Rafael elkoby
 * @version December 3 2020
 */

public class DataBase {
	// Class variables *************************************************
	private static DataBase SqlConnectorInstace = null;
	private Connection connection = null;
	private String jdbcURL = "jdbc:mysql://localhost/gonaturedb?serverTimezone=CAT";
	private String jdbcuser = "root";
	private String jdbcPass = "root";
	// Constructors ****************************************************

	private DataBase() {

	}

	// Class Getters *************************************************
	public Connection getConnection() {
		return connection;
	}

	// Instance methods *************************************************
	/**
	 * Set the SQL connector instance by given DB.
	 * @param db 
	 */
	public static void setInstance(DataBase db) {
		SqlConnectorInstace = db;
	}
	
	
	/**
	 * This method reads info from the DB and returns the query answer
	 * 
	 * @param SQL query of search character and not update
	 * @returns ResultSet if found returns the tuple\s as ResultSet if not found
	 *          returns a empty ResultSet
	 * 
	 */
	public ResultSet search(String query) {
		ResultSet res = null;

		try {
			PreparedStatement ps = connection.prepareStatement(query);
			res = ps.executeQuery();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;

	}

	public ResultSet search(PreparedStatement query) {
		ResultSet res = null;

		try {
			res = query.executeQuery();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;

	}

	/**
	 * 
	 * This method writes info to the DB and returns a boolean answer
	 * 
	 * @param SQL query of update character and not search
	 * @return True if update succeeded
	 * @return False else
	 */
	public boolean update(String query) {
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.executeUpdate();
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean update(PreparedStatement query) {
		try {
			query.executeUpdate();
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * This method return a instance of the database class and if never created
	 * before creates one
	 * 
	 * @return the instance of the DataBase class
	 */
	public static DataBase getInstance() {
		if (SqlConnectorInstace == null)
			SqlConnectorInstace = new DataBase();

		return SqlConnectorInstace;
	}

	/**
	 * 
	 * Sets the connection to the DataBase
	 * 
	 * @return True if the connection has succeeded.
	 * @return False else
	 */
	public boolean setConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			System.out.println("Driver definition succeed");
		} catch (Exception ex) {
			/* handle the error */
			System.out.println("Driver definition failed");
		}

		try {
			Connection conn = DriverManager.getConnection(jdbcURL, jdbcuser, jdbcPass);
			connection = conn;
			System.out.println("SQL connection succeed");
			return true;

		} catch (SQLException ex) {/* handle any errors */
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			return false;
		}
	}

	/**
	 * 
	 * This method calculate the ResultSet size
	 * 
	 * @param resultSet
	 * @return 0 if the ResultSet is empty, and the number of tuples else
	 */
	public int getResultSetSize(ResultSet resultSet) {
		int size = 0;
		if (resultSet != null) {
			try {
				resultSet.last(); // moves cursor to the last row
				size = resultSet.getRow(); // get row id
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return size;
	}
}
