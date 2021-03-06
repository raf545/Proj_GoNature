package server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import dataBase.DataBase;

/**
 * Delete Irrelevant Waiting List
 * 
 * @author yansokolov
 *
 */
public class DeleteIrrelevantWaitingList implements Runnable {
	Connection con = DataBase.getInstance().getConnection();

	/**
	 * this thread run every day and delete irrelevant waiting list tuple from DB
	 */
	@Override
	public void run() {
		Timestamp check = new Timestamp(System.currentTimeMillis());
		check.setDate(check.getDate() + 1);
		PreparedStatement query;
		try {
			query = con.prepareStatement(
					"DELETE FROM gonaturedb.waitinglist WHERE (personalID <> \"\") AND (dateAndTime < ?);");
			query.setTimestamp(1, check);
			DataBase.getInstance().update(query);
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

}
