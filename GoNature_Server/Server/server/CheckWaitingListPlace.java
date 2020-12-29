package server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import dataBase.DataBase;
import javafx.application.Platform;
import popup.PopUp;
import reservation.Reservation;

public class CheckWaitingListPlace implements Runnable {
	Reservation myReservation;
	Connection con = DataBase.getInstance().getConnection();

	public CheckWaitingListPlace(Reservation myReservation) {
		this.myReservation = myReservation;
	}

	@SuppressWarnings({ "deprecation", "static-access" })
	@Override
	public void run() {
		List<Reservation> waitingList = new ArrayList<>();
		Timestamp time = myReservation.getDateAndTime();
		Timestamp from = new Timestamp(time.getYear(), time.getMonth(), time.getDate(), time.getHours() - 3, 00, 00,
				00);
		Timestamp to = new Timestamp(time.getYear(), time.getMonth(), time.getDate(), time.getHours() + 3, 00, 00, 00);
		String queryForCancelReservation;
		String reservationIdForCancelReservation = myReservation.getReservationID();
		boolean flag = true;
		ResultSet res;
		int numOfVisitorsThatCancel = Integer.parseInt(myReservation.getNumofvisitors());

		try {
			PreparedStatement query = con.prepareStatement(
					"SELECT * FROM gonaturedb.waitinglist where parkname = ? and dateAndTime between ? and ? Order by enterToWaitingList;");
			query.setString(1, myReservation.getParkname());
			query.setTimestamp(2, from);
			query.setTimestamp(3, to);
			res = DataBase.getInstance().search(query);
			while (res.next()) {
				Reservation reservation = new Reservation();
				reservation.setPersonalID(res.getString("personalID"));
				reservation.setParkname(res.getString("parkname"));
				reservation.setNumofvisitors(res.getString("numofvisitors"));
				reservation.setReservationtype(res.getString("reservationtype"));
				reservation.setEmail(res.getString("email"));
				reservation.setDateAndTime(res.getTimestamp("dateAndTime"));
				reservation.setPhone(res.getString("phone"));
				reservation.setReservationtype(res.getString("waitingListStatus"));
				waitingList.add(reservation);
			}
			for (Reservation waitingListCheck : waitingList) {
				if (numOfVisitorsThatCancel >= Integer.parseInt(waitingListCheck.getNumofvisitors())) {
					Timestamp timestamp1 = new Timestamp(0);
					timestamp1 = timestamp1.valueOf(LocalDateTime.now());
					timestamp1.setHours(timestamp1.getHours() + 1);

					Timestamp timestamp2 = new Timestamp(0);

					// update relevant tuple to send message
					query = con.prepareStatement(
							"UPDATE gonaturedb.waitinglist SET waitingListStatus = \"sendMessage\" WHERE (personalID = ?) and (parkname = ?) and (dateAndTime = ?);");
					query.setString(1, waitingListCheck.getPersonalID());
					query.setString(2, waitingListCheck.getParkname());
					query.setTimestamp(3, waitingListCheck.getDateAndTime());
					DataBase.getInstance().update(query);
					// send email to the client
					String message = "send to " + waitingListCheck.getPersonalID() + "\nemail: "
							+ waitingListCheck.getEmail() + "\nsend to phone number:" + waitingListCheck.getPhone()
							+ "\nA place is available on the waiting list, please log in to confirm the order ";
					Platform.runLater(() -> {
						PopUp.display("send message to client mail", message);
					});

					// flag turn to false if the tuple deleted from the waiting list
					flag = true;
					while ((timestamp1.after(timestamp2.valueOf(LocalDateTime.now()))) && flag) {

						query = con.prepareStatement(
								"SELECT * FROM gonaturedb.waitinglist where personalID = ? and parkname = ? and dateAndTime = ?;");
						query.setString(1, waitingListCheck.getPersonalID());
						query.setString(2, waitingListCheck.getParkname());
						query.setTimestamp(3, waitingListCheck.getDateAndTime());
						res = DataBase.getInstance().search(query);
						if (DataBase.getInstance().isEmpty(res) == 0) {
							flag = false;
							numOfVisitorsThatCancel -= Integer.parseInt(waitingListCheck.getNumofvisitors());
						}
						if (flag == true) {
							Thread.currentThread();
							Thread.sleep(1000 * 60);
						}

					}
					query = con.prepareStatement(
							"SELECT * FROM gonaturedb.waitinglist where personalID = ? and parkname = ? and dateAndTime = ?;");
					query.setString(1, waitingListCheck.getPersonalID());
					query.setString(2, waitingListCheck.getParkname());
					query.setTimestamp(3, waitingListCheck.getDateAndTime());
					res = DataBase.getInstance().search(query);
					if (DataBase.getInstance().isEmpty(res) != 0) {
						query = con.prepareStatement(
								"DELETE FROM gonaturedb.waitinglist WHERE (personalID = ?) and (parkname = ?) and (dateAndTime = ?);");
						query.setString(1, waitingListCheck.getPersonalID());
						query.setString(2, waitingListCheck.getParkname());
						query.setTimestamp(3, waitingListCheck.getDateAndTime());
						DataBase.getInstance().update(query);

					}

				}
			}

			// set cancel to reservation that we cancel
			queryForCancelReservation = "UPDATE gonaturedb.reservetions SET reservetionStatus = \"Canceled\" WHERE reservationID = \""
					+ reservationIdForCancelReservation + "\";";
			DataBase.getInstance().update(queryForCancelReservation);
		} catch (SQLException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
