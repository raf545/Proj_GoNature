package server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import dataBase.DataBase;

public class DeleteIrrelevantWaitingList implements Runnable {
	Connection con = DataBase.getInstance().getConnection();

	@Override
	public void run() {
		Timestamp check = new Timestamp(System.currentTimeMillis());
		PreparedStatement query;
		try {
			query = con.prepareStatement(
					"DELETE FROM gonaturedb.waitinglist WHERE (personalID <> \"\") AND (dateAndTime < ?);");
			query.setTimestamp(1, check);
			DataBase.getInstance().update(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
