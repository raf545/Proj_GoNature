package server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import dataBase.DataBase;
import reservation.Reservation;

public class CheckWaitingListPlace implements Runnable {
	Reservation myReservation;
	Connection con = DataBase.getInstance().getConnection();

	public CheckWaitingListPlace(Reservation myReservation) {
		this.myReservation = myReservation;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		List<Reservation> waitingList = new ArrayList<>();
		Timestamp time = myReservation.getDateAndTime();
		Timestamp from = new Timestamp(time.getYear(), time.getMonth(), time.getDate(), time.getHours() - 3, 00, 00,
				00);
		Timestamp to = new Timestamp(time.getYear(), time.getMonth(), time.getDate(), time.getHours() + 3, 00, 00, 00);

		try {
			PreparedStatement query = con.prepareStatement(
					"SELECT * FROM gonaturedb.waitinglist where parkname = ? and dateAndTime between ? and ? Order by dateAndTime;");
			query.setString(1, myReservation.getParkname());
			query.setTimestamp(2, from);
			query.setTimestamp(3, to);
			ResultSet res = DataBase.getInstance().search(query);
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
			for (Reservation r : waitingList)
				System.out.println(r);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
