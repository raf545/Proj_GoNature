package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.google.gson.Gson;

import cardReader.CardReader;
import cardReader.IdAndParkAndNum;
import dataBase.DataBase;
import ocsf.server.ConnectionToClient;
import reservation.Reservation;

/**
 * this class is responsible for all the card reader functions and operations, it contains the mechanism of the entrance
 * and the exit of the park and the communication with the data base.
 * 
 * @author dan
 *
 */
public class CardReaderController {

	private String answerToCilent = "no reservation found for this day";
	Gson gson = new Gson();
	Connection con = DataBase.getInstance().getConnection();
	private static CardReaderController CardReaderControllerInstacne = null;

	private CardReaderController() {
	}

	/**
	 * generates a CardReaderController instance if never created else, returns the existing one.
	 * @return an instance of card reader controller class.
	 */
	public static CardReaderController getInstance() {

		if (CardReaderControllerInstacne == null)
			CardReaderControllerInstacne = new CardReaderController();
		return CardReaderControllerInstacne;
	}

	/**
	 * This method route to a specific method, enter to the park or exit from the park
	 * @param functionName - the name of the function that have to be called
	 * @param data - the data that need to be sent to the function
	 * @param client - the clients connection details 
	 * @return the answer from the called function or null if no function have been called
	 */
	public String router(String functionName, String data, ConnectionToClient client) {

		switch (functionName) {
		case "enterPark":
			return enterPark(data, client);
		case "exitPark":
			return exitPark(data, client);
		}

		return null;
	}

	/**
	 * this function contains the mechanism of the entrance to the park.
	 * if all the stages passed the function will return a success string.
	 * @param data - the data of the visitors (it's id,park and visitor number) 
	 * @param client - the clients connection details
	 * @return answerToCilent parameter
	 */
	private String enterPark(String data, ConnectionToClient client) {

		answerToCilent = "no reservation found for this day";
		IdAndParkAndNum idAndParkAndNum = gson.fromJson(data, IdAndParkAndNum.class);
		ResultSet reservationTupels;
		reservationTupels = checkReservationExistence(idAndParkAndNum);

		if (DataBase.getInstance().getResultSetSize(reservationTupels) != 0) {
			try {
				
				if (checkVisitorInPark(reservationTupels)) {
					updateReservationStatus(reservationTupels);
					reservationTupels.first();
					CardReader cardReader = updateCardReader(reservationTupels,
							reservationTupels.getString("reservationID"), idAndParkAndNum,
							new Timestamp(System.currentTimeMillis()));

					updateParkCurrentCappacity(cardReader);
					answerToCilent = "Entered successfully";
					return answerToCilent + "\nthe price is: " + reservationTupels.getString("price") ;
					
				}
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}
		return answerToCilent;
	}

	/**
	 * this function checks if the given visitor has a valid reservation around 20 minutes plus or minus
	 * @param idAndPark - the data of the visitors (it's id,park and visitor number)  
	 * @return the reservation tuples on success or null on fail
	 */
	private ResultSet checkReservationExistence(IdAndParkAndNum idAndPark) {

		ResultSet reservationTupels;
		Timestamp twentyMinutsPlus;
		Timestamp twentyMinutsMinus;
		Timestamp reservationTime;
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());

		long currentTimeMilli = currentTime.getTime();
		long delayTime = 10 * 60 * 1000;
		twentyMinutsPlus = new Timestamp(currentTimeMilli + delayTime);
		twentyMinutsMinus = new Timestamp(currentTimeMilli - delayTime);

		PreparedStatement query = null;

		try {

			query = con.prepareStatement(
					"select * from gonaturedb.reservetions where personalID = ? and parkname = ? and (reservetionStatus = \"Approved\" OR reservetionStatus = \"inPark\") order by dateAndTime;");
			query.setString(1, idAndPark.getId());
			query.setString(2, idAndPark.getParkName());

			reservationTupels = DataBase.getInstance().search(query);

			while (reservationTupels.next()) {

				reservationTime = new Timestamp(reservationTupels.getTimestamp("dateAndTime").getTime());

				if (reservationTime.getYear() == currentTime.getYear()
						&& reservationTime.getMonth() == currentTime.getMonth()
						&& reservationTime.getDay() == currentTime.getDay()) {

					if (reservationTime.after(twentyMinutsPlus)
							&& reservationTupels.getString("reservetionStatus").equals("Approved")) {
						answerToCilent = "you arrived too early your reservation is at " + reservationTime.getHours()
								+ ":0" + reservationTime.getMinutes();
						return null;
					}

					if (reservationTime.before(twentyMinutsMinus)
							&& reservationTupels.getString("reservetionStatus").equals("Approved")) {
						answerToCilent = "you are late your reservation was at " + reservationTime.getHours() + ":0"
								+ reservationTime.getMinutes();
						return null;
					}

					if ((reservationTime.before(twentyMinutsPlus) && reservationTime.after(twentyMinutsMinus))
							|| reservationTupels.getString("reservetionStatus").equals("inPark")) {
						
						int numOfReservationVitors = Integer.parseInt(reservationTupels.getString("numofvisitors"));
						int numOfActualVisitors = Integer.parseInt(idAndPark.getNumOfVisitors());
						
						if(numOfActualVisitors > numOfReservationVitors) {
							answerToCilent = "your reservation is for "+ numOfReservationVitors + " and not for "+numOfActualVisitors + "\n" + 
									"Please go to the checkout to place an order for people who do not appear in the order";
							return null;
						}
						return reservationTupels;
					}
						
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	/**
	 * updates the status of a given reservation to "inPark"
	 * @param reservationTupels - the tuple that has to be updated
	 * @throws SQLException if there was a problem with the update in the data base
	 */
	private void updateReservationStatus(ResultSet reservationTupels) throws SQLException {
		reservationTupels.first();
		String reservationId = reservationTupels.getString("reservationID");
		String updatequery = "UPDATE gonaturedb.reservetions SET reservetionStatus = \"inPark\" WHERE reservationID = \""
				+ reservationId + "\";";

		DataBase.getInstance().update(updatequery);
	}

	/**
	 * inserts the card reader details to the data base
	 * @param reservationTupels - reservation from the data base
	 * @param reservationId
	 * @param idAndPark
	 * @param currentTime
	 * @return updated cardReader instance
	 * @throws SQLException if there was a problem with the update in the data base
	 */
	private CardReader updateCardReader(ResultSet reservationTupels, String reservationId, IdAndParkAndNum idAndPark,
			Timestamp currentTime) throws SQLException {
		reservationTupels.first();
		CardReader cardReader = new CardReader(reservationId, idAndPark.getId(), reservationTupels.getString("phone"),
				currentTime, null, idAndPark.getNumOfVisitors(), idAndPark.getParkName(),
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

	/**
	 * updates the park current capacity by the amount of people that entered to the park
	 * @param cardReader - the details of the visitors that entered the park
	 * @throws SQLException if there was a problem with the update in the data base
	 */
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

	/**
	 * checks if the visitors associated with the reservation are currently in the park
	 * @param reservationTupels - reservation from the data base
	 * @return true if the given visitor is currently in the park. else, returns false
	 * @throws SQLException if there was a problem with the update in the data base
	 */
	private boolean checkVisitorInPark(ResultSet reservationTupels) throws SQLException {

		reservationTupels.first();
		String reservationID = reservationTupels.getString("reservationID");
		ResultSet reservationInPark;

		PreparedStatement searchQuery = con
				.prepareStatement("select * from gonaturedb.cardreader where reservationID = ? ;");
		searchQuery.setString(1, reservationID);
		reservationInPark = DataBase.getInstance().search(searchQuery);
		if (DataBase.getInstance().getResultSetSize(reservationInPark) == 0
				|| reservationInPark.getTimestamp("exitTime") != null) {
			return true;
		}

		answerToCilent = "The visitor (s) belonging to this identity card are already in the park "
				+ reservationInPark.getString("parkname");
		return false;
	}

	/**
	 * this function contains the mechanism of the exit of the park.
	 * @param data - the data of the visitors (it's id,park and visitor number)
	 * @param client -the clients connection details
	 * @return string of error or success
	 */
	private String exitPark(String data, ConnectionToClient client) {

		IdAndParkAndNum idAndPark = gson.fromJson(data, IdAndParkAndNum.class);
		ResultSet cardReaderInPark;
		PreparedStatement searchQuery;
		try {
			searchQuery = con.prepareStatement(
					"select * from gonaturedb.cardreader where personalID = ? AND parkName = ?  AND exitTime is NULL;");
			searchQuery.setString(1, idAndPark.getId());
			searchQuery.setString(2, idAndPark.getParkName());
			cardReaderInPark = DataBase.getInstance().search(searchQuery);

			if (DataBase.getInstance().getResultSetSize(cardReaderInPark) == 0)
				return "ERROR - The visitor (s) belonging to this identity card not in the park";

			updateReservationOnExit(cardReaderInPark.getString("reservationID"));
			updateParkCurrentCapacityOnExit(idAndPark.getParkName(), cardReaderInPark.getString("numberOfVisitors"));
			updateCardReader(cardReaderInPark.getString("reservationID"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "Exit successfully";
	}

	/**
	 * updates the exit time for a given reservation in the card reader table in the data base
	 * @param reservationID
	 * @throws SQLException if there was a problem with the update in the data base
	 */
	private void updateCardReader(String reservationID) throws SQLException {

		PreparedStatement updateQuery = con
				.prepareStatement("UPDATE gonaturedb.cardreader SET exitTime = ? WHERE reservationID = ?;");
		updateQuery.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
		updateQuery.setString(2, reservationID);
		DataBase.getInstance().update(updateQuery);
	}

	/**
	 * updates the park capacity for the exit function
	 * @param parkName
	 * @param numberOfVisitors
	 * @throws SQLException if there was a problem with the update in the data base
	 */
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

	/**
	 * updates the reservation status to used
	 * @param reservationId
	 */
	private void updateReservationOnExit(String reservationId) {

		String updatequery = "UPDATE gonaturedb.reservetions SET reservetionStatus = \"Used\" WHERE reservationID = \""
				+ reservationId + "\";";
		DataBase.getInstance().update(updatequery);
	}

}
