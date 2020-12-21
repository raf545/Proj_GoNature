package sqlConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 * This Class is responsibale of Data Base managment and manipulations
 * 
 * @author Dan Gutchin
 * @author Yaniv Sokolov
 * @author Rafael elkoby
 * @version December 3 2020
 */

public class SqlConnector {
	// Class variables *************************************************
	private static SqlConnector SqlConnectorInstace = null;
	private Connection con = null;
	private String jdbcURL = "jdbc:mysql://localhost/gonaturedb?serverTimezone=IST";
	private String jdbcuser = "root";
	private String jdbcPass = "Aa123456";
	// Constructors ****************************************************

	private SqlConnector() {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			System.out.println("Driver definition succeed");
		} catch (Exception ex) {
			/* handle the error */
			System.out.println("Driver definition failed");
		}

		try {
			Connection conn = DriverManager.getConnection(jdbcURL, jdbcuser, jdbcPass);
			con = conn;
			System.out.println("SQL connection succeed");

		} catch (SQLException ex) {/* handle any errors */
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}

	}

	// Class Getters *************************************************
	public Connection getConnection() {
		return con;
	}

	// Instance methods *************************************************

	/**
	 * This method serches for info in the DB and returns the query answer
	 * 
	 * @param query SQL query of serch charecter and not update
	 * @returns ResultSet if found returns the tuple\s as ResultSet if not found
	 *          returns a empty ResultSet
	 * 
	 */

	public ResultSet searchInDB(String query) {
		ResultSet res = null;

		// TODO change exception throw into something less strong
		// TODO catch and handle the SQL exception

		try {
			PreparedStatement ps = con.prepareStatement(query);
			res = ps.executeQuery();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;

	}

	public boolean updateToDB(String query) {
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.executeUpdate();
			return true;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public static SqlConnector getInstance() {
		if (SqlConnectorInstace == null)
			SqlConnectorInstace = new SqlConnector();

		return SqlConnectorInstace;
	}
}
