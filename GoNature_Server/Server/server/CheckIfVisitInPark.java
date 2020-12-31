package server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import dataBase.DataBase;

public class CheckIfVisitInPark implements Runnable {
	Connection con = DataBase.getInstance().getConnection();

	@SuppressWarnings("static-access")
	@Override
	public void run() {
		System.out.println("here");
		Timestamp check = new Timestamp(0);
		check = check.valueOf(LocalDateTime.now());
		try {
			PreparedStatement query = con.prepareStatement(
					"UPDATE gonaturedb.reservetions set reservetionStatus = \"Canceled\" where (reservationID <> \"\" and reservetionStatus = \"Approved\" and dateAndTime < ?);");
			query.setTimestamp(1, check);
			DataBase.getInstance().update(query);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
