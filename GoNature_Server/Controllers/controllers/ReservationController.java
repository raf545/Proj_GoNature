package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;

import dataBase.DataBase;
import ocsf.server.ConnectionToClient;
import reservation.Reservation;
import server.CheckWaitingListPlace;

/**
 * 
 * A server side controller class which deals with all process that are related
 * to the reservation making process at the server side
 * 
 */
public class ReservationController {
	// Class variables *************************************************
	private static ReservationController reservationControllerInstacne = null;

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
	public static ReservationController getInstance() {

		if (reservationControllerInstacne == null)
			reservationControllerInstacne = new ReservationController();
		return reservationControllerInstacne;
	}

	// Instance methods ************************************************

	public String loginRouter(String functionName, String data, ConnectionToClient client) {

		switch (functionName) {
		case "createNewReservation":
			return createNewReservation(data, client);
		case "getReservations":
			return getReservations(data, client);
		case "showAvailableSpace":
			return showAvailableSpace(data, client);
		case "cancelReservation":
			return cancelReservation(data);
		case "approveReservation":
			return approveReservation(data);
		case "addToReservationTableFromWaitingList":
			return addToReservationTableFromWaitingList(data, client);

		default:
			return "fail";
		}
	}

	/**
	 * 
	 * This method creates a new reservation with the given data if fails return a
	 * answer string
	 * 
	 * @param data   visitor data
	 * @param client
	 * @return a answer String of the following: "There is no available space in the
	 *         park\n for the given time", "fail update reservation ID", "fail
	 *         insert reservation to DB" OR a string containing the reservation
	 *         details
	 * 
	 */
	public String createNewReservation(String data, ConnectionToClient client) {
		int reservationId;
		Reservation reservation = gson.fromJson(data, Reservation.class);
		if (isThereAvailableSpace(reservation.getDateAndTime(), reservation.getParkname(),
				Integer.parseInt(reservation.getNumofvisitors())) == false)
			return "There is no available space in the park\n for the given time";

		reservationId = getAndIncreaseReservasionID();
		if (reservationId < 10000)
			return "fail update reservation ID";
		reservation.setReservationID(Integer.toString(reservationId));

		double priceForReservation = calculatePricePreOrder(reservation);
		reservation.setPrice(priceForReservation);

		if (insertReservationToDB(reservation) == false)
			return "fail insert reservation to DB";
		return gson.toJson(reservation);

	}

	/**
	 * 
	 * This method checks if there is enough space in the park for a given amount of
	 * visitors and specific time
	 * 
	 * @param time
	 * @param parkName
	 * @param numberOfVisitors
	 * @return true if there is available space, false else
	 */
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
					"select sum(numofvisitors) from gonaturedb.reservetions events where dateAndTime between ? and ? and parkname = ? and (reservetionStatus = \"Valid\" OR reservetionStatus = \"halfCanceled\");");
			query.setTimestamp(1, threeHoursBelow);
			query.setTimestamp(2, threeHoursAbove);
			query.setString(3, parkName);
			res = DataBase.getInstance().search(query);
			if (DataBase.getInstance().isEmpty(res) != 0)
				numberOfVisitorsAtTimeArea = res.getInt("sum(numofvisitors)");

			query = con.prepareStatement(
					"SELECT num FROM gonaturedb.uptodateinformation where nameOfVal = \"parkDifference" + parkName
							+ "\" ;");
			res = DataBase.getInstance().search(query);
			while (res.next()) {
				numberOfAvailbleVisitors = res.getInt("num");
			}

			if (numberOfAvailbleVisitors < numberOfVisitorsAtTimeArea + numberOfVisitors)
				return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 
	 * This method creates a reservation id using a incremented variable in the
	 * database. and increments it by 1.
	 * 
	 * @return a new reservation ID
	 */
	private int getAndIncreaseReservasionID() {
		String query = "SELECT num FROM gonaturedb.uptodateinformation where nameOfVal = \"resrvationID\" ;";
		int oldReservationID = 0;
		ResultSet res;
		try {

			res = DataBase.getInstance().search(query);

			while (res.next()) {
				oldReservationID = res.getInt("num");
			}

			int newReservationID = oldReservationID + 1;
			query = "UPDATE gonaturedb.uptodateinformation SET num = " + newReservationID
					+ " WHERE (nameOfVal = \"resrvationID\");";
			DataBase.getInstance().update(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return oldReservationID;
	}

	/**
	 * 
	 * Inserts a given reservation detail to the database
	 * 
	 * @param reservation
	 * @return true if insertion was successfully done, false else
	 */
	private boolean insertReservationToDB(Reservation reservation) {
		try {
			PreparedStatement query = con.prepareStatement(
					"INSERT INTO gonaturedb.reservetions (reservationID, personalID, parkname, numofvisitors, reservationtype, email, dateAndTime, price, phone, reservetionStatus) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
			query.setString(1, reservation.getReservationID());
			query.setString(2, reservation.getPersonalID());
			query.setString(3, reservation.getParkname());
			query.setString(4, reservation.getNumofvisitors());
			query.setString(5, reservation.getReservationtype());
			query.setString(6, reservation.getEmail());
			query.setTimestamp(7, reservation.getDateAndTime());
			query.setDouble(8, reservation.getPrice());
			query.setString(9, reservation.getPhone());
			query.setString(10, reservation.getReservetionStatus());
			if (DataBase.getInstance().update(query) == false)
				return false;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;

	}

	/**
	 * 
	 * This method calculates the price of a given reservation
	 * 
	 * @param reservation
	 * @return the price
	 */
	@SuppressWarnings("resource")
	private double calculatePricePreOrder(Reservation reservation) {
		String query = "SELECT num FROM gonaturedb.uptodateinformation where nameOfVal = \"ticketPrice\" ;";
		ResultSet res;
		int ticketPrice = 0;
		double discount = 0;
		double reservationPrice = 0;
		double discountPreOrder = 0;
		try {
			res = DataBase.getInstance().search(query);
			while (res.next()) {
				ticketPrice = res.getInt("num");

				switch (reservation.getReservationtype()) {
				case "instructor":
					query = "SELECT discountInPercentage FROM gonaturedb.discounts where discountType = \"instructorPreOrder\" ;";
					res = DataBase.getInstance().search(query);
					while (res.next()) {
						discount = res.getDouble("discountInPercentage");
					}
					reservationPrice = Integer.parseInt(reservation.getNumofvisitors()) * ticketPrice * discount;
					break;
				case "guest":
					query = "SELECT discountInPercentage FROM gonaturedb.discounts where discountType = \"PreOrder\" ;";
					res = DataBase.getInstance().search(query);
					while (res.next()) {
						discount = res.getDouble("discountInPercentage");
					}
					reservationPrice = Integer.parseInt(reservation.getNumofvisitors()) * ticketPrice * discount;
					break;
				case "subscriber":
					query = "SELECT discountInPercentage FROM gonaturedb.discounts where discountType = \"subscriber\" ;";
					res = DataBase.getInstance().search(query);
					while (res.next()) {
						discount = res.getDouble("num");
					}
					reservationPrice = ticketPrice * discount
							+ (Integer.parseInt(reservation.getNumofvisitors()) - 1) * ticketPrice;

					query = "SELECT discountInPercentage FROM gonaturedb.discounts where discountType = \"PreOrder\" ;";
					res = DataBase.getInstance().search(query);
					while (res.next()) {
						discountPreOrder = res.getDouble("discountInPercentage");
					}
					reservationPrice = discountPreOrder * reservationPrice;
					break;
				default:
					int numberOfFamily = 0;
					query = "SELECT discountInPercentage FROM gonaturedb.discounts where discountType = \"subscriber\" ;";
					res = DataBase.getInstance().search(query);
					while (res.next()) {
						discount = res.getDouble("discountInPercentage");
					}

					query = "SELECT discountInPercentage FROM gonaturedb.discounts where discountType = \"PreOrder\" ;";
					res = DataBase.getInstance().search(query);
					while (res.next()) {
						discountPreOrder = res.getDouble("discountInPercentage");
					}
					query = "SELECT numOfMembers FROM gonaturedb.subscriber where id = " + reservation.getPersonalID()
							+ ";";
					res = DataBase.getInstance().search(query);
					while (res.next()) {
						numberOfFamily = Integer.parseInt(res.getString("numOfMembers"));
					}
					if (numberOfFamily >= Integer.parseInt(reservation.getNumofvisitors()))
						reservationPrice = Integer.parseInt(reservation.getNumofvisitors()) * ticketPrice * discount;
					else
						reservationPrice = ticketPrice * discount * numberOfFamily
								+ (Integer.parseInt(reservation.getNumofvisitors()) - numberOfFamily) * ticketPrice;
					reservationPrice = discountPreOrder * reservationPrice;
					break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return reservationPrice;
	}

	private String getReservations(String data, ConnectionToClient client) {
		ArrayList<Reservation> myReservations = new ArrayList<>();

		String personalId = gson.fromJson(data, String.class);
		String query = "SELECT * FROM gonaturedb.reservetions where personalID = \"" + personalId
				+ "\" and reservetionStatus = \"Valid\";";
		ResultSet reservationTupels;

		reservationTupels = DataBase.getInstance().search(query);
		try {
			while (reservationTupels.next()) {
				Reservation reservation = new Reservation();
				reservation.setReservationID(reservationTupels.getString("reservationID"));
				reservation.setPersonalID(reservationTupels.getString("personalID"));
				reservation.setParkname(reservationTupels.getString("parkname"));
				reservation.setNumofvisitors(reservationTupels.getString("numofvisitors"));
				reservation.setReservationtype(reservationTupels.getString("reservationtype"));
				reservation.setEmail(reservationTupels.getString("email"));
				reservation.setDate(reservationTupels.getTimestamp("dateAndTime"));
				reservation.setPrice(reservationTupels.getDouble("price"));
				reservation.setReservationtype("Valid");
				reservation.setPhone(reservationTupels.getString("phone"));
				myReservations.add(reservation);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return gson.toJson(myReservations.toArray());
	}

	@SuppressWarnings("deprecation")
	private String showAvailableSpace(String data, ConnectionToClient client) {
		Reservation myReservation = gson.fromJson(data, Reservation.class);
		ArrayList<Reservation> availableReservations = new ArrayList<>();
		String parkName = myReservation.getParkname();
		int numberOfVisitors = Integer.parseInt(myReservation.getNumofvisitors());
		Timestamp myReservationTime = myReservation.getDateAndTime();
		Timestamp timeToCheck = new Timestamp(myReservationTime.getYear(), myReservationTime.getMonth(),
				myReservationTime.getDate(), myReservationTime.getHours() + 1, 0, 0, 0);
		Timestamp lastTimeToCheck = new Timestamp(myReservationTime.getYear(), myReservationTime.getMonth(),
				myReservationTime.getDate() + 2, 17, 0, 0, 0);
		while (!(timeToCheck.equals(lastTimeToCheck))) {
			if (isThereAvailableSpace(timeToCheck, parkName, numberOfVisitors)
					&& (timeToCheck.getHours() <= 16 && timeToCheck.getHours() >= 8)) {
				Reservation reservation = new Reservation();
				reservation.setReservationID(myReservation.getReservationID());
				reservation.setPersonalID(myReservation.getPersonalID());
				reservation.setParkname(myReservation.getParkname());
				reservation.setNumofvisitors(myReservation.getNumofvisitors());
				reservation.setReservationtype(myReservation.getReservationtype());
				reservation.setEmail(myReservation.getEmail());
				reservation.setDate(new Timestamp(timeToCheck.getYear(), timeToCheck.getMonth(), timeToCheck.getDate(),
						timeToCheck.getHours(), 0, 0, 0));
				reservation.setPrice(myReservation.getPrice());
				reservation.setReservationtype("Valid");
				availableReservations.add(reservation);
			}
			timeToCheck.setHours(timeToCheck.getHours() + 1);
		}
		return gson.toJson(availableReservations.toArray());
	}

	/**
	 * 
	 * This method sets a reservation status as canceled
	 * 
	 * @param reservationId
	 * @return Reservation canceled succsessfuly , Reservation wasn't canceled
	 *         properly
	 */
	private String cancelReservation(String reservation) {
		Reservation myReservation = gson.fromJson(reservation, Reservation.class);
		String reservationId = myReservation.getReservationID();
		String query = "UPDATE gonaturedb.reservetions SET reservetionStatus = \"halfCanceled\" WHERE reservationID = \""
				+ reservationId + "\";";
		Runnable run = new CheckWaitingListPlace(myReservation);
		Thread t = new Thread(run);
		t.start();
		if (DataBase.getInstance().update(query))
			return "Reservation canceled succsessfuly";
		return "Reservation wasn't canceled properly";

	}

	private String approveReservation(String reservationId) {

		String query = "UPDATE gonaturedb.reservetions SET reservetionStatus = \"Aproved\" WHERE reservationID = \""
				+ reservationId + "\";";
		if (DataBase.getInstance().update(query))
			return "Reservation approved succsessfuly";
		return "Reservation wasn't approved properly";

	}

	private String addToReservationTableFromWaitingList(String data, ConnectionToClient client) {
		int reservationId;
		Reservation reservation = gson.fromJson(data, Reservation.class);
		try {
			// delete from waiting list DB
			PreparedStatement query = con.prepareStatement(
					"DELETE FROM gonaturedb.waitinglist WHERE (personalID = ?) and (parkname = ?) and (dateAndTime = ?);");
			query.setString(1, reservation.getPersonalID());
			query.setString(2, reservation.getParkname());
			query.setTimestamp(3, reservation.getDateAndTime());
			DataBase.getInstance().update(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		reservationId = getAndIncreaseReservasionID();
		if (reservationId < 10000)
			return "fail update reservation ID";
		reservation.setReservationID(Integer.toString(reservationId));

		double priceForReservation = calculatePricePreOrder(reservation);
		reservation.setPrice(priceForReservation);
		reservation.setReservetionStatus("Valid");
		if (insertReservationToDB(reservation) == false)
			return "fail insert reservation to DB";
		return gson.toJson(reservation);
	}

}
