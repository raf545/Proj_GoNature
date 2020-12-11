package sqlConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import logic.Visitor;

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
	private String jdbcURL = "jdbc:mysql://localhost:3306/?user=root";
	private String jdbcuser = "root";
	private String jdbcPass = "7C034CBD7cd$";
	// Constructors *************************************************

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
	 * Updates the Email of a given visitor
	 * 
	 * @param msg given visitor and email
	 * @return "true" if updated successfuly
	 * 
	 *         TODO check if instance Arraylist FIXME never returns a false value
	 */
	public boolean updateEmailInDB(Object msg) {

		@SuppressWarnings("unchecked")
		// FIXME why do i need to suppress warning here?
		ArrayList<String> tempV = (ArrayList<String>) msg;
		try {
			PreparedStatement ps = con.prepareStatement("UPDATE gonaturedb.visitors SET email = ? WHERE (id = ?);");
			ps.setString(2, (String) tempV.get(0));
			ps.setString(1, (String) tempV.get(1));
			ps.executeUpdate();

		} catch (SQLException e) {
			// TODO create a message for the exception
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * This method searches for a specific visitor in the DB
	 * 
	 * @param msg
	 * @return IF found: returns a visitors
	 * @return Else Throws exception
	 * 
	 *         TODO change exception throw into something less strong TODO catch and
	 *         handle the SQL exception
	 * @throws SQLException
	 */

	public Visitor searchInDB(Object msg) {
		ResultSet res;
		Visitor sv = new Visitor();
		try {
			String s = (String) msg;
			PreparedStatement ps = con.prepareStatement("SELECT * FROM gonaturedb.visitors WHERE id = ?;");
			ps.setString(1, s);

			res = ps.executeQuery();

			while (res.next()) {
				sv.setId(res.getString(1));
				sv.setName(res.getString(2));
				sv.setLastname(res.getString(3));
				sv.setPhone(res.getString(4));
				sv.setEmail(res.getString(5));
			}
			res.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(sv);
		return sv;
	}

	public static SqlConnector getInstance() {
		if (SqlConnectorInstace == null)
			SqlConnectorInstace = new SqlConnector();

		return SqlConnectorInstace;
	}
}
