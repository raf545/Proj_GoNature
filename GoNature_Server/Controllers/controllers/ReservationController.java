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
			 return createNewReservation(data,client);
			
		}
		return null;
	}

	private String createNewReservation(String data, ConnectionToClient client) {
		
		Reservation reservation = gson.fromJson(data, Reservation.class);
		
		
		
		return null;
		
	}

	
	@SuppressWarnings("deprecation")
	private boolean isThereAvailableSpace(Timestamp time) {
		
		try {
			PreparedStatement query = con.prepareStatement("select * from gonaturedb.reservetions events where dateAndTime between ? and ? ;");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Timestamp fourHursPlus;
		Timestamp fourHursMin;
		fourHursPlus = new Timestamp(time.getYear(), time.getMonth(), time.getDate(), time.getHours() + 4, 0, 0, 0);
		fourHursMin = new Timestamp(time.getYear(), time.getMonth(), time.getDate(), time.getHours() - 4, 0, 0, 0);

		ResultSet reservationTupelsAtTime;
		return false;
		
	}
	
	
	
}
