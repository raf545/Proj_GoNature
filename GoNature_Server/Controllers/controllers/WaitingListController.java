package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.google.gson.Gson;

import dataBase.DataBase;
import ocsf.server.ConnectionToClient;
import reservation.Reservation;

public class WaitingListController {

	private static WaitingListController waitingListControllerInstacne = null;

	Gson gson = new Gson();

	Connection con = DataBase.getInstance().getConnection();

	// Constructors ****************************************************
	/**
	 * 
	 * This method returns the single instance of this class and if it dosen't yet
	 * have a instance it creates one and return it.
	 * 
	 * @return ReservationController class instance
	 */
	public static WaitingListController getInstance() {

		if (waitingListControllerInstacne == null)
			waitingListControllerInstacne = new WaitingListController();
		return waitingListControllerInstacne;
	}

	public String loginRouter(String functionName, String data, ConnectionToClient client) {

		switch (functionName) {
		case "enterToWaitingList":
			return enterToWaitingList(data, client);
		case "getWaitingList":
			return getWaitingList(data, client);
		default:
			return "fail";
		}
	}

	/**
	 * check if the reservation can enter to the waiting list
	 * 
	 * @param data   reservation data
	 * @param client get the client data
	 * @return the function return if the reservation enter to the waiting list
	 */
	private String enterToWaitingList(String data, ConnectionToClient client) {
		Reservation reservation = gson.fromJson(data, Reservation.class);
		ResultSet res = null;
		try {
			// check if already the reservation in waiting list table
			PreparedStatement query = con.prepareStatement(
					"SELECT * FROM gonaturedb.waitinglist WHERE personalID = ? and parkname = ? and dateAndTime = ?");
			query.setString(1, reservation.getPersonalID());
			query.setString(2, reservation.getParkname());
			query.setTimestamp(3, reservation.getDateAndTime());
			res = DataBase.getInstance().search(query);
			if (DataBase.getInstance().isEmpty(res) != 0)
				return "You're already in the waiting list in this park and at this time";
			// inserts to waiting list
			query = con.prepareStatement("INSERT INTO gonaturedb.waitinglist VALUES (?, ?, ?, ?, ?, ?, ?);");
			query.setString(1, reservation.getPersonalID());
			query.setString(2, reservation.getParkname());
			query.setString(3, reservation.getNumofvisitors());
			query.setString(4, reservation.getReservationtype());
			query.setString(5, reservation.getEmail());
			query.setTimestamp(6, reservation.getDateAndTime());
			query.setString(7, reservation.getPhone());
			if (DataBase.getInstance().update(query) == false)
				return "fail";
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "You entered to waiting list";
	}

	private String getWaitingList(String data, ConnectionToClient client) {
		ArrayList<Reservation> myWaitingList = new ArrayList<>();

		String personalId = gson.fromJson(data, String.class);
		String query = "SELECT * FROM gonaturedb.waitinglist where personalID = \"" + personalId + "\";";
		ResultSet reservationTupels;
		reservationTupels = DataBase.getInstance().search(query);
		try {
			while (reservationTupels.next()) {
				Reservation reservation = new Reservation();
				reservation.setPersonalID(reservationTupels.getString("personalID"));
				reservation.setParkname(reservationTupels.getString("parkname"));
				reservation.setNumofvisitors(reservationTupels.getString("numofvisitors"));
				reservation.setReservationtype(reservationTupels.getString("reservationtype"));
				reservation.setEmail(reservationTupels.getString("email"));
				reservation.setDate(reservationTupels.getTimestamp("dateAndTime"));
				reservation.setPhone("phone");
				myWaitingList.add(reservation);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return gson.toJson(myWaitingList.toArray());
	}

}
