package server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import dataBase.DataBase;

/**
 * check if client miss his reservation
 * 
 * @author yansokolov
 *
 */
public class CheckIfVisitInPark implements Runnable {
	Connection con = DataBase.getInstance().getConnection();

	/**
	 * Check if have reservation that approved and don't come to the park
	 */
	@SuppressWarnings("static-access")
	@Override
	public void run() {
		System.out.println("here");
		Timestamp check = new Timestamp(0);
		check = check.valueOf(LocalDateTime.now());
		try {
			PreparedStatement query = con.prepareStatement(
					"UPDATE gonaturedb.reservetions set reservetionStatus = \"Canceled\" where (reservationID <> \"\" AND (reservetionStatus = \"Valid\" OR reservetionStatus = \"halfCanceled\" OR reservetionStatus = \"Approved\") AND dateAndTime < ?);");
			query.setTimestamp(1, check);
			DataBase.getInstance().update(query);

		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

}