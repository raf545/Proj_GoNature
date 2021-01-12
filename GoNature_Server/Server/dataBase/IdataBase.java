package dataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public interface IdataBase {

	// Class Getters *************************************************
	Connection getConnection();

	/**
	 * This method reads info from the DB and returns the query answer
	 * 
	 * @param SQL query of serch charecter and not update
	 * @returns ResultSet if found returns the tuple\s as ResultSet if not found
	 *          returns a empty ResultSet
	 * 
	 */

	ResultSet search(String query);

	ResultSet search(PreparedStatement query);

	/**
	 * 
	 * This method wriets info to the DB and returns a boolean answer
	 * 
	 * @param SQL query of update charecter and not search
	 * @return True if update successeded
	 * @return False else
	 */
	boolean update(String query);

	boolean update(PreparedStatement query);

	/**
	 * 
	 * Sets the connection to the DataBase
	 * 
	 * @return True if the connection Successeded
	 * @return False else
	 */
	boolean setConnection();

	/**
	 * 
	 * This method calculate the ResultSet size
	 * 
	 * @param resultSet
	 * @return 0 if the ResultSet is empty, and the number of tuples else
	 */
	int getResultSetSize(ResultSet resultSet);

}