package server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import dataBase.DataBase;
import javafx.application.Platform;
import popup.PopUp;

public class SendMessageToExistingReservationsDayBefore implements Runnable {
	Connection con = DataBase.getInstance().getConnection();

	/**
	 * this thread run every day and sends to the client a message the day before
	 * reservation a confirmation message
	 */
	@Override
	public void run() {
		System.out.println("in the approve thread");
		ResultSet res;
		PreparedStatement query;
		Timestamp fromCheck = new Timestamp(System.currentTimeMillis());
		Timestamp toCheck = new Timestamp(System.currentTimeMillis());
		fromCheck.setDate(fromCheck.getDate() + 1);
		fromCheck.setHours(00);
		fromCheck.setMinutes(0);
		fromCheck.setSeconds(0);
		toCheck.setDate(toCheck.getDate() + 1);
		toCheck.setHours(23);
		toCheck.setMinutes(0);
		toCheck.setSeconds(0);
		try {
			query = con.prepareStatement(
					"select personalID,email,phone from gonaturedb.reservetions where dateAndTime between ? and ?;");
			query.setTimestamp(1, fromCheck);
			query.setTimestamp(2, toCheck);
			res = DataBase.getInstance().search(query);
			if (DataBase.getInstance().getResultSetSize(res) == 0)
				return;
			do {
				// send email to the client
				String message = "send to " + res.getString("personalID") + " \nemail: " + res.getString("email")
						+ "\nsend to phone number:" + res.getString("phone")
						+ "\nplease enter to the system and approve your reservation ";
				Platform.runLater(() -> {
					PopUp.display("send message to client mail and phone", message);
				});
			} while (res.next());

			query = con.prepareStatement(
					"UPDATE gonaturedb.reservetions SET reservetionStatus = \"sendApprovalMessage\" WHERE (reservationID <> \"\" AND reservetionStatus = \"Valid\" AND dateAndTime between ? AND ?);");
			query.setTimestamp(1, fromCheck);
			query.setTimestamp(2, toCheck);
			DataBase.getInstance().update(query);

		} catch (SQLException e) {

			e.printStackTrace();
		}
		System.out.println("end approve thread");

	}

}
