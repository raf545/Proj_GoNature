package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.google.gson.Gson;

import cardReader.CardReader;
import cardReader.IdAndPark;
import dataBase.DataBase;
import ocsf.server.ConnectionToClient;
import reservation.Reservation;

public class CardReaderController {

	Gson gson = new Gson();
	Connection con = DataBase.getInstance().getConnection();
	private static CardReaderController CardReaderControllerInstacne = null;

	private CardReaderController() {
	}

	public static CardReaderController getInstance() {

		if (CardReaderControllerInstacne == null)
			CardReaderControllerInstacne = new CardReaderController();
		return CardReaderControllerInstacne;
	}

	public String router(String functionName, String data, ConnectionToClient client) {

		switch (functionName) {
		case "enterPark":
			return enterPark(data, client);
		case "exitPark":
			return exitPark(data, client);
		}

		return null;
	}

	@SuppressWarnings("deprecation")
	private String enterPark(String data, ConnectionToClient client) {

		ResultSet reservationTupels;
		IdAndPark idAndPark = gson.fromJson(data, IdAndPark.class);
		Timestamp twentyMinutsPlus;
		Timestamp twentyMinutsMinus;
		Timestamp reservationTime;
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		;
		long currentTimeMilli = currentTime.getTime();
		long delayTime = 20 * 60 * 1000;
		twentyMinutsPlus = new Timestamp(currentTimeMilli + delayTime);
		twentyMinutsMinus = new Timestamp(currentTimeMilli - delayTime);

		PreparedStatement query = null;

		try {

			query = con.prepareStatement(
					"select * from gonaturedb.reservetions where personalID = ? and parkname = ? and (reservetionStatus = \"Valid\" );");
			query.setString(1, idAndPark.getId());
			query.setString(2, idAndPark.getParkName());

			reservationTupels = DataBase.getInstance().search(query);

			while (reservationTupels.next()) {

				reservationTime = reservationTupels.getTimestamp("dateAndTime");

				if (reservationTime.getYear() == currentTime.getYear()
						&& reservationTime.getMonth() == currentTime.getMonth()
						&& reservationTime.getDay() == currentTime.getDay()) {

					if (reservationTime.after(twentyMinutsPlus))
						return "you arrived too early your reservation is at " + reservationTime.getHours() + ":"
								+ reservationTime.getMinutes();

					if (reservationTime.before(twentyMinutsMinus))
						return "you are late your reservation was at " + reservationTime.getHours() + ":"
								+ reservationTime.getMinutes();

					if (reservationTime.before(twentyMinutsPlus) && reservationTime.after(twentyMinutsMinus)) {
						String reservationId = reservationTupels.getString("reservationID");
						String updatequery = "UPDATE gonaturedb.reservetions SET reservetionStatus = \"inPark\" WHERE reservationID = \""
								+ reservationId + "\";";
						
						DataBase.getInstance().update(updatequery);

						CardReader cardReader = new CardReader(reservationId, idAndPark.getId(),reservationTupels.getString("phone"),
								currentTime, reservationTupels.getString("numofvisitors"), idAndPark.getParkName(),
								reservationTupels.getString("reservationtype"));
						
						PreparedStatement insertQuery = con.prepareStatement(
								"INSERT INTO gonaturedb.cardreader (reservationID, personalID, phoneNumber, entryTime, numberOfVisitors, parkName, typeOfVisitor) VALUES (?, ?, ?, ?, ?, ?, ?);");
						
						insertQuery.setString(1, cardReader.getReservationID());
						insertQuery.setString(2, cardReader.getPersonalID());
						insertQuery.setString(3, cardReader.getPhoneNumber());
						insertQuery.setTimestamp(4, cardReader.getEntryTime());
						insertQuery.setString(5, cardReader.getNumberOfVisitors());
						insertQuery.setString(6, cardReader.getParkName());
						insertQuery.setString(7, cardReader.getTypeOfVisitor());
						
						if (DataBase.getInstance().update(query) == false)
							return "insert fail";
						
						String searchQuery = null;
						switch(cardReader.getParkName()) {
						case "Banias":
							searchQuery = "select num from gonaturedb.reservetions where nameOfVal = baniasCurrentCapacity ";
							break;
						case "Safari":
							searchQuery = "select num from gonaturedb.reservetions where nameOfVal = safariCurrentCapacity ";
							break;
						case "Niagara":
							searchQuery = "select num from gonaturedb.reservetions where nameOfVal = niagaraCurrentCapacity ";
							break;
						}
						
						ResultSet currentCapacity = DataBase.getInstance().search(query);
						int currentparkcapacity = currentCapacity.getInt("num");
						
						
						
						
						
						
						
						
						return "Entered successfully";
					}
				}
			}
			
			return "no reservation found for this day";

		} catch (SQLException e) {
			e.printStackTrace();
			return "query fail";
		}

		
	}

	private String exitPark(String id, ConnectionToClient client) {
		// TODO Auto-generated method stub
		return null;
	}

}
