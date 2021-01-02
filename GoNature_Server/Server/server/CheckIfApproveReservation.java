package server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dataBase.DataBase;
import javafx.application.Platform;
import popup.PopUp;

public class CheckIfApproveReservation implements Runnable {

	Connection con = DataBase.getInstance().getConnection();

	/**
	 * if the client don't approve the reservation after 2 hours the reservation is
	 * cancelled
	 */
	@Override
	public void run() {
		try {
			PreparedStatement query;
			ResultSet res;
			query = con.prepareStatement(
					"select personalID,email,phone from gonaturedb.reservetions where reservetionStatus = \"sendApprovalMessage\";");

			res = DataBase.getInstance().search(query);
			if (DataBase.getInstance().getResultSetSize(res) == 0)
				return;
			do {
				// send email to the client
				String message = "send to " + res.getString("personalID") + " \nemail: " + res.getString("email")
						+ "\nsend to phone number:" + res.getString("phone") + "\nyour reservation is cancel ";
				Platform.runLater(() -> {
					PopUp.display("send message to client mail and phone", message);
				});
			} while (res.next());

			query = con.prepareStatement(
					"Update gonaturedb.reservetions SET reservetionStatus = \"Canceled\" WHERE (reservationID <> \"\" AND reservetionStatus = \"sendApprovalMessage\");");
			DataBase.getInstance().update(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
