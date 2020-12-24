package dataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This Class is responsibale of Data Base managment and manipulations
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
	 * This method reads info from the DB and returns the query answer
	 * 
	 * @param SQL query of serch charecter and not update
	 * @returns ResultSet if found returns the tuple\s as ResultSet if not found
	 *          returns a empty ResultSet
	 * 
	 */

	public ResultSet search(String query) {
		ResultSet res = null;

		// TODO change exception throw into something less strong
		// TODO catch and handle the SQL exception

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

		// TODO change exception throw into something less strong
		// TODO catch and handle the SQL exception

		try {
			res = query.executeQuery();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;

	}

	/**
	 * 
	 * This method wriets info to the DB and returns a boolean answer
	 * 
	 * @param SQL query of update charecter and not search
	 * @return True if update successeded
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
	 * before creats one
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
	 * @return True if the connection Successeded
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

	public int isEmpty(ResultSet resultSet) {
		int size = 0;
		if (resultSet != null) {
			try {
				resultSet.last(); // moves cursor to the last row
				size = resultSet.getRow(); // get row id
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return size;
	}
}
