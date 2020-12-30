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

	private String answerToCilent = "no reservation found for this day";
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

	private String enterPark(String data, ConnectionToClient client) {

		answerToCilent = "no reservation found for this day";
		IdAndPark idAndPark = gson.fromJson(data, IdAndPark.class);
		ResultSet reservationTupels;
		reservationTupels = checkReservationExistence(idAndPark);

		if (DataBase.getInstance().isEmpty(reservationTupels) != 0) {
			try {
				if (checkVisitorInPark(reservationTupels)) {

					updateReservationStatus(reservationTupels);
					CardReader cardReader = updateCardReader(reservationTupels,
							reservationTupels.getString("reservationID"), idAndPark,
							new Timestamp(System.currentTimeMillis()));

					updateParkCurrentCappacity(cardReader);
					answerToCilent = "Entered successfully";
					return answerToCilent;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return answerToCilent;
	}

	private ResultSet checkReservationExistence(IdAndPark idAndPark) {

		ResultSet reservationTupels;
		Timestamp twentyMinutsPlus;
		Timestamp twentyMinutsMinus;
		Timestamp reservationTime;
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());

		long currentTimeMilli = currentTime.getTime();
		long delayTime = 20 * 60 * 1000;
		twentyMinutsPlus = new Timestamp(currentTimeMilli + delayTime);
		twentyMinutsMinus = new Timestamp(currentTimeMilli - delayTime);

		PreparedStatement query = null;

		try {

			query = con.prepareStatement(
					"select * from gonaturedb.reservetions where personalID = ? and parkname = ? and (reservetionStatus = \"Approved\" OR reservetionStatus = \"inPark\");");
			query.setString(1, idAndPark.getId());
			query.setString(2, idAndPark.getParkName());

			reservationTupels = DataBase.getInstance().search(query);

			while (reservationTupels.next()) {

				reservationTime = new Timestamp(
						reservationTupels.getTimestamp("dateAndTime").getTime() - 120 * 60 * 1000);

				if (reservationTime.getYear() == currentTime.getYear()
						&& reservationTime.getMonth() == currentTime.getMonth()
						&& reservationTime.getDay() == currentTime.getDay()) {

					if (reservationTime.after(twentyMinutsPlus)
							&& reservationTupels.getString("reservetionStatus").equals("Approved")) {
						answerToCilent = "you arrived too early your reservation is at " + reservationTime.getHours()
								+ ":" + reservationTime.getMinutes();
						return null;
					}

					if (reservationTime.before(twentyMinutsMinus)
							&& reservationTupels.getString("reservetionStatus").equals("Approved")) {
						answerToCilent = "you are late your reservation was at " + reservationTime.getHours() + ":"
								+ reservationTime.getMinutes();
						return null;
					}

					if ((reservationTime.before(twentyMinutsPlus) && reservationTime.after(twentyMinutsMinus))
							|| reservationTupels.getString("reservetionStatus").equals("inPark"))
						return reservationTupels;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	private void updateReservationStatus(ResultSet reservationTupels) throws SQLException {
		String reservationId = reservationTupels.getString("reservationID");
		String updatequery = "UPDATE gonaturedb.reservetions SET reservetionStatus = \"inPark\" WHERE reservationID = \""
				+ reservationId + "\";";

		DataBase.getInstance().update(updatequery);
	}

	private CardReader updateCardReader(ResultSet reservationTupels, String reservationId, IdAndPark idAndPark,
			Timestamp currentTime) throws SQLException {
		CardReader cardReader = new CardReader(reservationId, idAndPark.getId(), reservationTupels.getString("phone"),
				currentTime, null, reservationTupels.getString("numofvisitors"), idAndPark.getParkName(),
				reservationTupels.getString("reservationtype"), reservationTupels.getDouble("price"));

		PreparedStatement insertQuery = con.prepareStatement(
				"INSERT INTO gonaturedb.cardreader (reservationID, personalID, phoneNumber, entryTime, exitTime, numberOfVisitors, parkName, typeOfVisitor, price) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?);");

		insertQuery.setString(1, cardReader.getReservationID());
		insertQuery.setString(2, cardReader.getPersonalID());
		insertQuery.setString(3, cardReader.getPhoneNumber());
		insertQuery.setTimestamp(4, cardReader.getEntryTime());
		insertQuery.setTimestamp(5, cardReader.getExitTime());
		insertQuery.setString(6, cardReader.getNumberOfVisitors());
		insertQuery.setString(7, cardReader.getParkName());
		insertQuery.setString(8, cardReader.getTypeOfVisitor());
		insertQuery.setDouble(9, cardReader.getPrice());

		DataBase.getInstance().update(insertQuery);
		return cardReader;
	}

	private void updateParkCurrentCappacity(CardReader cardReader) throws SQLException {
		String searchQuery = null;
		String parkName = null;
		switch (cardReader.getParkName()) {
		case "Banias":
			searchQuery = "select num from gonaturedb.uptodateinformation where nameOfVal = \"BaniasCurrentCapacity\";";
			parkName = "BaniasCurrentCapacity";
			break;
		case "Safari":
			searchQuery = "select num from gonaturedb.uptodateinformation where nameOfVal = \"SafariCurrentCapacity\";";
			parkName = "SafariCurrentCapacity";
			break;
		case "Niagara":
			searchQuery = "select num from gonaturedb.uptodateinformation where nameOfVal = \"NiagaraCurrentCapacity\";";
			parkName = "NiagaraCurrentCapacity";
			break;
		}

		ResultSet currentCapacity = DataBase.getInstance().search(searchQuery);
		int currentparkcapacity = 0;
		while (currentCapacity.next()) {
			currentparkcapacity = currentCapacity.getInt("num");
		}
		currentparkcapacity += Integer.parseInt(cardReader.getNumberOfVisitors());

		String updatequery = "UPDATE gonaturedb.uptodateinformation SET num = " + currentparkcapacity
				+ " WHERE nameOfVal = \"" + parkName + "\";";
		DataBase.getInstance().update(updatequery);
	}

	private boolean checkVisitorInPark(ResultSet reservationTupels) throws SQLException {

		String reservationID = reservationTupels.getString("reservationID");
		ResultSet reservationInPark;

		PreparedStatement searchQuery = con
				.prepareStatement("select * from gonaturedb.cardreader where reservationID = ? ;");
		searchQuery.setString(1, reservationID);
		reservationInPark = DataBase.getInstance().search(searchQuery);
		if (DataBase.getInstance().isEmpty(reservationInPark) == 0
				|| reservationInPark.getTimestamp("exitTime") != null) {
			return true;
		}

		answerToCilent = "The visitor (s) belonging to this identity card are already in the park "
				+ reservationInPark.getString("parkname");
		return false;
	}

	private String exitPark(String data, ConnectionToClient client) {
		
		IdAndPark idAndPark = gson.fromJson(data, IdAndPark.class);
		ResultSet cardReaderInPark;
		PreparedStatement searchQuery;
		try {
			searchQuery = con.prepareStatement(
					"select * from gonaturedb.cardreader where personalID = ? AND parkName = ?  AND exitTime is NULL;");
			searchQuery.setString(1, idAndPark.getId());
			searchQuery.setString(2, idAndPark.getParkName());
			cardReaderInPark = DataBase.getInstance().search(searchQuery);

			if (DataBase.getInstance().isEmpty(cardReaderInPark) == 0)
				return "ERROR - The visitor (s) belonging to this identity card not in the park";

			updateReservationOnExit(cardReaderInPark.getString("reservationID"));
			updateParkCurrentCapacityOnExit(idAndPark.getParkName(), cardReaderInPark.getString("numberOfVisitors"));
			updateCardReader(cardReaderInPark.getString("reservationID"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Exit successfully";
	}

	private void updateCardReader(String reservationID) throws SQLException {

		PreparedStatement updateQuery = con
				.prepareStatement("UPDATE gonaturedb.cardreader SET exitTime = ? WHERE reservationID = ?;");
		updateQuery.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
		updateQuery.setString(2, reservationID);
		DataBase.getInstance().update(updateQuery);
	}

	private void updateParkCurrentCapacityOnExit(String parkName, String numberOfVisitors) throws SQLException {

		String searchQuery = null;
		switch (parkName) {
		case "Banias":
			searchQuery = "select num from gonaturedb.uptodateinformation where nameOfVal = \"BaniasCurrentCapacity\";";
			parkName = "BaniasCurrentCapacity";
			break;
		case "Safari":
			searchQuery = "select num from gonaturedb.uptodateinformation where nameOfVal = \"SafariCurrentCapacity\";";
			parkName = "SafariCurrentCapacity";
			break;
		case "Niagara":
			searchQuery = "select num from gonaturedb.uptodateinformation where nameOfVal = \"NiagaraCurrentCapacity\";";
			parkName = "NiagaraCurrentCapacity";
			break;
		}

		ResultSet currentCapacity = DataBase.getInstance().search(searchQuery);
		int currentparkcapacity = 0;
		while (currentCapacity.next()) {
			currentparkcapacity = currentCapacity.getInt("num");
		}
		currentparkcapacity -= Integer.parseInt(numberOfVisitors);

		String updatequery = "UPDATE gonaturedb.uptodateinformation SET num = " + currentparkcapacity
				+ " WHERE nameOfVal = \"" + parkName + "\";";
		DataBase.getInstance().update(updatequery);

	}

	private void updateReservationOnExit(String reservationId) {

		String updatequery = "UPDATE gonaturedb.reservetions SET reservetionStatus = \"Used\" WHERE reservationID = \""
				+ reservationId + "\";";
		DataBase.getInstance().update(updatequery);
	}

}
