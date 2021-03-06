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
import subscriber.Subscriber;

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
	 * Routes to a specific method of reservation
	 * 
	 * @param functionName
	 * @param data         the relevent data for the method
	 * @param client       the current client
	 * @return
	 */
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
		case "occasionalVisitor":
			return occasionalVisitor(data, client);
		case "addToExsistingReservation":
			return addToExsistingReservation(data, client);
		case "addOccasionalToExsistingReservation":
			return addOccasionalToExsistingReservation(data, client);
		default:
			return "fail";
		}
	}

	/**
	 * 
	 * adds the number of pepole to a reservation at the same park time and id of
	 * the given reservation
	 * 
	 * @param data
	 * @param client
	 * @return "There is no available space in the park\n for the given time",
	 *         "reservation updated", "instructor cant order for more then 15"
	 */
	private String addToExsistingReservation(String data, ConnectionToClient client) {
		Reservation reservationToAdd = gson.fromJson(data, Reservation.class);
		String updateQuery, reservarionId = null;
		double upDatedPrice = 0;
		int numberOfPepoleInExsistingReservation = 0;
		int numberOfGivenReservationVisitors = Integer.parseInt(reservationToAdd.getNumofvisitors());
		if (!isThereAvailableSpace(reservationToAdd.getDateAndTime(), reservationToAdd.getParkname(),
				numberOfGivenReservationVisitors))
			return "There is no available space in the park\n for the given time";
		try {
			PreparedStatement query = con.prepareStatement(
					"SELECT * FROM gonaturedb.reservetions where personalID = ? and dateAndTime = ? and parkname = ? and (reservetionStatus = \"Approved\" or reservetionStatus = \"Valid\");");

			query.setString(1, reservationToAdd.getPersonalID());
			query.setTimestamp(2, reservationToAdd.getDateAndTime());
			query.setString(3, reservationToAdd.getParkname());

			ResultSet res = DataBase.getInstance().search(query);

			while (res.next()) {
				numberOfPepoleInExsistingReservation = res.getInt("numofvisitors");
				upDatedPrice = res.getDouble("price") + calculatePricePreOrder(reservationToAdd);
				reservarionId = res.getString("reservationID");
			}

			numberOfGivenReservationVisitors = Integer.parseInt(reservationToAdd.getNumofvisitors());
			int updatedNumberOfVisitors = numberOfPepoleInExsistingReservation + numberOfGivenReservationVisitors;

			if (reservationToAdd.getReservationtype().equals("instructor"))
				if (updatedNumberOfVisitors > 15)
					return "instructor cant order for more then 15";

			updateQuery = "UPDATE gonaturedb.reservetions SET numofvisitors = " + updatedNumberOfVisitors + ", price = "
					+ upDatedPrice + " WHERE (reservationID = \"" + reservarionId + "\");";

			DataBase.getInstance().update(updateQuery);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return "reservation updated";
	}

	/**
	 * 
	 * in case of a existing reservation for a specific day time and park and id
	 * joins two reservations of a given id to one and adds the occasional visitor
	 * to the reservation
	 * 
	 * @param data   the reservation details
	 * @param client
	 * @return "There is no available space in the park\n for the given
	 *         time","instructor cant order for more then 15","Reservation
	 *         Updated","error"
	 */
	private String addOccasionalToExsistingReservation(String data, ConnectionToClient client) {
		Reservation OccasionalReservation = gson.fromJson(data, Reservation.class);
		Reservation reservation = getExsistingReservationsInTimeArea(OccasionalReservation);
		String updateQuery;
		double upDatedPrice = 0;
		int numberOfPepoleInExsistingReservation = Integer.parseInt(reservation.getNumofvisitors());
		int numberOfGivenReservationVisitors = Integer.parseInt(OccasionalReservation.getNumofvisitors());
		int updatedNumberOfVisitors = numberOfGivenReservationVisitors + numberOfPepoleInExsistingReservation;

		if (!isThereAvailableSpace(OccasionalReservation.getDateAndTime(), OccasionalReservation.getParkname(),
				numberOfGivenReservationVisitors))
			return "There is no available space in the park\n for the given time";

		if (OccasionalReservation.getReservationtype().equals("instructor"))
			if (updatedNumberOfVisitors > 15)
				return "instructor cant order for more then 15";

		String query = "SELECT * FROM gonaturedb.subscriber where id = " + reservation.getPersonalID() + ";";
		ResultSet subscriber = DataBase.getInstance().search(query);
		if (DataBase.getInstance().getResultSetSize(subscriber) == 0)
			return "error";
		Subscriber occasionalVisitor = new Subscriber(subscriber);
		OccasionalReservation.setReservationtype(occasionalVisitor.getSubscriberType());
		upDatedPrice += calculateOccasionalVisitorPrice(OccasionalReservation, occasionalVisitor);
		upDatedPrice += reservation.getPrice();

		updateQuery = "UPDATE gonaturedb.reservetions SET numofvisitors = " + updatedNumberOfVisitors + ", price = "
				+ upDatedPrice + " WHERE (reservationID = \"" + reservation.getReservationID() + "\");";

		if (DataBase.getInstance().update(updateQuery))
			return "Reservation Updated";
		return "error";
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
		if (checkExsistingReservations(reservation))
			return "reservation exists";
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
	 * check if a reservation with the same id time and park already exist
	 * 
	 * @param reservation
	 * @return true if exist false if not
	 */
	private boolean checkExsistingReservations(Reservation reservation) {
		ResultSet res;
		try {
			PreparedStatement query = con.prepareStatement(
					"SELECT * FROM gonaturedb.reservetions where personalID = ? and dateAndTime = ? and parkname = ? ;");
			query.setString(1, reservation.getPersonalID());
			query.setTimestamp(2, reservation.getDateAndTime());
			query.setString(3, reservation.getParkname());
			res = DataBase.getInstance().search(query);

			while (res.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * check if a reservation with the same id time and park already exist in the 
	 * 30 minute radios of the given reservation time
	 * 
	 * @param reservation
	 * @return
	 */
	private boolean checkExsistingReservationsInTimeArea(Reservation reservation) {
		ResultSet res;
		long delayTime = 15 * 60 * 1000;
		try {
			PreparedStatement query = con.prepareStatement(
					"SELECT * FROM gonaturedb.reservetions where personalID = ? and dateAndTime BETWEEN ? and ? and parkname = ? ;");
			long currentTime = reservation.getDateAndTime().getTime();
			Timestamp twentyMinutsPlus = new Timestamp(currentTime + delayTime);
			Timestamp twentyMinutsMinus = new Timestamp(currentTime - delayTime);
			query.setString(1, reservation.getPersonalID());
			query.setTimestamp(2, twentyMinutsMinus);
			query.setTimestamp(3, twentyMinutsPlus);
			query.setString(4, reservation.getParkname());
			res = DataBase.getInstance().search(query);

			while (res.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * get the reservations in the radios of 30 minutes from the given reservation 
	 * with the same reserving id
	 * 
	 * @param reservation
	 * @return
	 */
	private Reservation getExsistingReservationsInTimeArea(Reservation reservation) {
		ResultSet res;
		long delayTime = 15 * 60 * 1000;
		try {
			PreparedStatement query = con.prepareStatement(
					"SELECT * FROM gonaturedb.reservetions where personalID = ? and dateAndTime BETWEEN ? and ? and parkname = ? and reservetionStatus = \"Approved\";");
			long currentTime = reservation.getDateAndTime().getTime();
			Timestamp twentyMinutsPlus = new Timestamp(currentTime + delayTime);
			Timestamp twentyMinutsMinus = new Timestamp(currentTime - delayTime);
			query.setString(1, reservation.getPersonalID());
			query.setTimestamp(2, twentyMinutsMinus);
			query.setTimestamp(3, twentyMinutsPlus);
			query.setString(4, reservation.getParkname());
			res = DataBase.getInstance().search(query);

			return new Reservation(res);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
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
			PreparedStatement query = con.prepareStatement(
					"select sum(numofvisitors) from gonaturedb.reservetions events where dateAndTime between ? and ? and parkname = ? and (reservetionStatus = \"Valid\" OR reservetionStatus = \"halfCanceled\" OR reservetionStatus = \"Approved\");");
			query.setTimestamp(1, threeHoursBelow);
			query.setTimestamp(2, threeHoursAbove);
			query.setString(3, parkName);
			res = DataBase.getInstance().search(query);
			if (DataBase.getInstance().getResultSetSize(res) != 0)
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
		double parkDiscount = getParkDiscountPercentage(reservation.getParkname());
		try {
			res = DataBase.getInstance().search(query);
			while (res.next()) {

				ticketPrice = res.getInt("num");

				switch (reservation.getReservationtype()) {
				case "instructor":
					discount = getDiscontPercentage("instructorPreOrder");
					reservationPrice = Integer.parseInt(reservation.getNumofvisitors()) * ticketPrice * discount;
					break;
				case "guest":
					discount = getDiscontPercentage("PreOrder");
					reservationPrice = Integer.parseInt(reservation.getNumofvisitors()) * ticketPrice * discount;
					break;
				case "subscriber":
					discount = getDiscontPercentage("subscriber");
					reservationPrice = ticketPrice * discount
							+ (Integer.parseInt(reservation.getNumofvisitors()) - 1) * ticketPrice;
					discountPreOrder = getDiscontPercentage("PreOrder");
					reservationPrice = discountPreOrder * reservationPrice;
					break;
				default:
					int numberOfFamily = 0;
					discount = getDiscontPercentage("subscriber");
					discountPreOrder = getDiscontPercentage("PreOrder");
					numberOfFamily = getFamilyMembersNumber(reservation.getPersonalID());

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
		return reservationPrice * parkDiscount;
	}

	/**
	 * 
	 * this method returns the number of family members for a given id
	 * 
	 * @param id the subscriber id
	 * @return
	 */
	private int getFamilyMembersNumber(String id) {

		String query;
		ResultSet res;
		int numberOfFamily = 0;
		query = "SELECT numOfMembers FROM gonaturedb.subscriber where id = " + id + ";";
		res = DataBase.getInstance().search(query);
		try {
			while (res.next()) {
				numberOfFamily = Integer.parseInt(res.getString("numOfMembers"));
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return numberOfFamily;

	}

	/**
	 * 
	 * This method returns the DiscontPercentage for a specific user type
	 * 
	 * @param discounType
	 * @return a duble representing the Discont Percentage (for 15% discount will
	 *         return 0.85)
	 */
	private double getDiscontPercentage(String discounType) {
		ResultSet res;
		String query;
		double discount = 1;
		query = "SELECT discountInPercentage FROM gonaturedb.discounts where discountType = \"" + discounType + "\";";
		res = DataBase.getInstance().search(query);
		try {
			while (res.next()) {
				discount = res.getDouble("discountInPercentage");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return discount;
	}

	/**
	 * 
	 * This method gets all the reservations of a given id that are approved
	 * 
	 * @param data
	 * @param client
	 * @return all the reservations of the given id as a string (Gson)
	 */
	private String getReservations(String data, ConnectionToClient client) {
		ArrayList<Reservation> myReservations = new ArrayList<>();

		String personalId = gson.fromJson(data, String.class);
		String query = "SELECT * FROM gonaturedb.reservetions where personalID = \"" + personalId
				+ "\" and (reservetionStatus = \"Valid\" OR reservetionStatus = \"sendApprovalMessage\" OR reservetionStatus = \"Aproved\");";
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
				reservation.setReservetionStatus(reservationTupels.getString("reservetionStatus"));
				reservation.setPhone(reservationTupels.getString("phone"));
				myReservations.add(reservation);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return gson.toJson(myReservations.toArray());
	}

	/**
	 * 
	 * This method gets a list of all the available visit hours in a 2 days area
	 * 
	 * @param data   A (Gson) Reservation
	 * @param client a given client
	 * @return a list of all the available visit hours in a 2 days area (as a Gson)
	 *         String
	 */
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

	/**
	 * 
	 * changes the Status given reservation to Approved
	 * 
	 * @param reservationId
	 * @return "Reservation approved succsessfuly" ,"Reservation wasn't approved
	 *         properly"
	 */
	private String approveReservation(String reservationId) {

		String query = "UPDATE gonaturedb.reservetions SET reservetionStatus = \"Aproved\" WHERE reservationID = \""
				+ reservationId + "\";";
		if (DataBase.getInstance().update(query))
			return "Reservation approved succsessfuly";
		return "Reservation wasn't approved properly";

	}

	/**
	 * 
	 * if a waiting list request was approved it moves the reservation to the my
	 * reservation section as valid
	 * 
	 * @param data   Reservation (As Gson)
	 * @param client the current client
	 * @return the reservation
	 */
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

	/**
	 * 
	 * gets the ticket price from the data base
	 * 
	 * @return the price as a double value
	 */
	private double getTicketPrice() {
		String query;
		double ticketPrice = 0;
		ResultSet returndTicketPrice;
		query = "SELECT num FROM gonaturedb.uptodateinformation where nameOfVal = \"ticketPrice\" ;";
		returndTicketPrice = DataBase.getInstance().search(query);
		try {
			while (returndTicketPrice.next()) {
				ticketPrice = returndTicketPrice.getDouble("num");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ticketPrice;
	}

	/**
	 * 
	 * returns the calculated price of a certain occasional visitor
	 * 
	 * @param reservation
	 * @param subscriber
	 * @return price as a double
	 */
	private double calculateOccasionalVisitorPrice(Reservation reservation, Subscriber subscriber) {
		double price;
		int familyMembersNumber, numberOfVisitorsInReservation;
		numberOfVisitorsInReservation = Integer.parseInt(reservation.getNumofvisitors());

		if (reservation.getReservationtype().equals("Guest")) {
			price = getTicketPrice() * Integer.parseInt(reservation.getNumofvisitors());
			price *= getParkDiscountPercentage(reservation.getParkname());
			return price;
		} else if (reservation.getReservationtype().equals("instructor")) {
			price = getTicketPrice() * getDiscontPercentage("instructorCasualVisit") * (numberOfVisitorsInReservation);
			return price;
		} else {
			familyMembersNumber = Integer.parseInt(subscriber.getNumOfMembers());
			if (familyMembersNumber >= numberOfVisitorsInReservation) {
				price = getTicketPrice() * getDiscontPercentage("subscriber") * numberOfVisitorsInReservation;
				price *= getParkDiscountPercentage(reservation.getParkname());
				return price;
			} else {
				price = getTicketPrice() * getDiscontPercentage("subscriber") * familyMembersNumber;
				price += getTicketPrice() * (numberOfVisitorsInReservation - familyMembersNumber);
				price *= getParkDiscountPercentage(reservation.getParkname());
				return price;
			}
		}
	}

	/**
	 * 
	 * This method get a park discount
	 * 
	 * 
	 * @param parkName
	 * @return the percentage of the discount as a double 15% will return 0.85
	 */
	private double getParkDiscountPercentage(String parkName) {
		double discount = 1;
		String query = "SELECT * FROM gonaturedb.uptodateinformation where nameOfVal = \"parkDiscount" + parkName
				+ "\";";
		ResultSet returnedDiscount = DataBase.getInstance().search(query);
		try {
			while (returnedDiscount.next()) {
				discount = returnedDiscount.getDouble("num");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return discount;
	}

	/**
	 * 
	 * gets the current capacity of a given park
	 * 
	 * @param parkName
	 * @return the number of visitors
	 */
	private double getCurrentCapacity(String parkName) {
		double capacity = -1;
		String query = "SELECT * FROM gonaturedb.uptodateinformation where nameOfVal = \"" + parkName
				+ "CurrentCapacity\"";
		ResultSet returnedDiscount = DataBase.getInstance().search(query);
		try {
			while (returnedDiscount.next()) {
				capacity = returnedDiscount.getDouble("num");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return capacity;
	}

	/**
	 * 
	 * gets the maximum park capacity
	 * 
	 * @param parkName
	 * @return
	 */
	private double getParkCapacity(String parkName) {
		double capacity = -1;
		String query = "SELECT * FROM gonaturedb.uptodateinformation where nameOfVal = \"parkCapacity" + parkName
				+ "\"";
		ResultSet returnedCapacity = DataBase.getInstance().search(query);
		try {
			while (returnedCapacity.next()) {
				capacity = returnedCapacity.getDouble("num");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return capacity;
	}

	/**
	 * 
	 * this method try's to add a occasional visitor to the park and
	 * 
	 * @param data   Reservation (as Gson)
	 * @param client current client
	 * @return "No available space at the park" , "Instructor cant make a reservaion
	 *         for more then 15 pepole","faild to get park data","fail".
	 */
	private String occasionalVisitor(String data, ConnectionToClient client) {
		Reservation reservation = gson.fromJson(data, Reservation.class);
		String query = "SELECT * FROM gonaturedb.subscriber where id = " + reservation.getPersonalID() + ";";
		ResultSet returnedSubscriber = DataBase.getInstance().search(query);
		double discount, finalPrice;
		double currentCapacity = getCurrentCapacity(reservation.getParkname());
		double parkCapacity = getParkCapacity(reservation.getParkname());
		int numofvisitors = Integer.parseInt(reservation.getNumofvisitors());

		if (currentCapacity != -1 && parkCapacity != -1) {

			if (currentCapacity + numofvisitors > parkCapacity)
				return "No available space at the park";

			else {

				if (checkExsistingReservationsInTimeArea(reservation)) {
					return "reservation exists";
				}

				if (DataBase.getInstance().getResultSetSize(returnedSubscriber) == 0) {
					reservation.setReservationtype("Guest");
					reservation.setPrice(calculateOccasionalVisitorPrice(reservation, null));
					reservation.setReservationID(Integer.toString(getAndIncreaseReservasionID()));
					insertReservationToDB(reservation);
					return gson.toJson(reservation);
				} else {
					// FIXME subscriberTypre -> subscriberType in db to.
					Subscriber subscriber = new Subscriber(returnedSubscriber);
					try {
						if (subscriber.getSubscriberType().equals("instructor")) {
							if (Integer.parseInt(reservation.getNumofvisitors()) == 15)
								return "Instructor cant make a reservaion for more then 15 pepole";
						}
						reservation.setReservationtype(returnedSubscriber.getString("subscriberTypre"));
						reservation.setPrice(
								calculateOccasionalVisitorPrice(reservation, new Subscriber(returnedSubscriber)));
						reservation.setReservationID(Integer.toString(getAndIncreaseReservasionID()));
						insertReservationToDB(reservation);
						return gson.toJson(reservation);
					} catch (SQLException e) {
						e.printStackTrace();
					}

				}
			}
		} else {
			return "faild to get park data";
		}

		return "fail";
	}
}
