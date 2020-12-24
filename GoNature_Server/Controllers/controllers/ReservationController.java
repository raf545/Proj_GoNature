package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.google.gson.Gson;

import dataBase.DataBase;
import ocsf.server.ConnectionToClient;
import reservation.Reservation;

public class ReservationController {

	private static ReservationController reservationControllerInstacne = null;
	Gson gson = new Gson();
	Connection con = DataBase.getInstance().getConnection();

	public static ReservationController getInstance() {

		if (reservationControllerInstacne == null)
			reservationControllerInstacne = new ReservationController();
		return reservationControllerInstacne;
	}

	public String loginRouter(String func, String data, ConnectionToClient client) {

		switch (func) {

		case "createNewReservation":
			return createNewReservation(data, client);

		}
		return null;
	}

	private String createNewReservation(String data, ConnectionToClient client) {
		int reservationId;
		Reservation reservation = gson.fromJson(data, Reservation.class);
		if (isThereAvailableSpace(reservation.getDateAndTime(), reservation.getParkname(),
				Integer.parseInt(reservation.getNumofvisitors())) == false)
			return "There is no availble space";
		reservationId = getAndIncreaseReservasionID();
		if (reservationId < 10000)
			return "fail update reservation ID";
		reservation.setReservationID(Integer.toString(reservationId));
		if (insertReservationToDB(reservation) == false)
			return "fail insert to DB";
		float priceForReservation = calculatePrice(reservation);
		reservation.setPrice(priceForReservation);
		return gson.toJson(reservation);

	}

	@SuppressWarnings("deprecation")
	private boolean isThereAvailableSpace(Timestamp time, String parkName, int numberOfVisitors) {
		Timestamp threeHoursAbove;
		Timestamp threeHoursBelow;
		int numberOfVisitorsAtTimeArea = 0;
		int numberOfAvailbleVisitors = 0;
		ResultSet res;
		threeHoursAbove = new Timestamp(time.getYear(), time.getMonth(), time.getDate(), time.getHours() + 3, 0, 0, 0);
		threeHoursBelow = new Timestamp(time.getYear(), time.getMonth(), time.getDate(), time.getHours() - 3, 0, 0, 0);
		try {
			// FIXME
			PreparedStatement query = con.prepareStatement(
					"select sum(numofvisitors) from gonaturedb.reservetions events where dateAndTime between ? and ? and parkname = ?;");
			query.setTimestamp(1, threeHoursBelow);
			query.setTimestamp(2, threeHoursAbove);
			query.setString(3, parkName);
			res = DataBase.getInstance().search(query);
			if (DataBase.getInstance().isEmpty(res) != 0)
				numberOfVisitorsAtTimeArea = res.getInt("sum(numofvisitors)");

			query = con.prepareStatement(
					"SELECT num FROM gonaturedb.uptodateinformation where nameOfVal = \"parkDifference\"" + parkName
							+ " ;");
			numberOfAvailbleVisitors = DataBase.getInstance().search(query).getInt("num");
			if (numberOfAvailbleVisitors < numberOfVisitorsAtTimeArea + numberOfVisitors)
				return false;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	private int getAndIncreaseReservasionID() {
		String query = "SELECT num FROM gonaturedb.uptodateinformation where nameOfVal = \"resrvationID\" ;";
		int oldReservationID = 0;
		try {
			oldReservationID = DataBase.getInstance().search(query).getInt("num");
			int newReservationID = oldReservationID + 1;
			query = "UPDATE gonaturedb.uptodateinformation SET num = " + newReservationID
					+ " WHERE (nameOfVal = \"resrvationID\");";
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return oldReservationID;
	}

	private boolean insertReservationToDB(Reservation reservation) {
		try {
			PreparedStatement query = con
					.prepareStatement("INSERT INTO gonaturedb.reservetions VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);");
			query.setString(1, reservation.getReservationID());
			query.setString(2, reservation.getPersonalID());
			query.setString(3, reservation.getParkname());
			query.setString(4, reservation.getReservationtype());
			query.setString(5, reservation.getEmail());
			query.setTimestamp(6, reservation.getDateAndTime());
			query.setFloat(7, reservation.getPrice());
			query.setString(8, reservation.getReservetionStatus());
			if (DataBase.getInstance().update(query) == false)
				;
			return false;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;

	}

	private float calculatePrice(Reservation reservation) {
		// TODO Auto-generated method stub
		return 0;
	}

	private boolean updateNumberOfVisitorsInPark(int updateNumber, String parkName) {
		String query = " UPDATE gonaturedb.uptodateinformation SET num = " + "updateNumber"
				+ " WHERE (nameOfVal = \"parkDifference" + "parkName" + "\");";
		if (DataBase.getInstance().update(query))
			return true;

		return false;

	}

}
