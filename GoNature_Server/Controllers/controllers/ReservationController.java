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
					"select sum(numofvisitors) from gonaturedb.reservetions events where dateAndTime between ? and ? and parkname = ?;");
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
			PreparedStatement query = con
					.prepareStatement("INSERT INTO gonaturedb.reservetions VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);");
			query.setString(1, reservation.getReservationID());
			query.setString(2, reservation.getPersonalID());
			query.setString(3, reservation.getParkname());
			query.setString(4, reservation.getNumofvisitors());
			query.setString(5, reservation.getReservationtype());
			query.setString(6, reservation.getEmail());
			query.setTimestamp(7, reservation.getDateAndTime());
			query.setDouble(8, reservation.getPrice());
			query.setString(9, reservation.getReservetionStatus());
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

}
