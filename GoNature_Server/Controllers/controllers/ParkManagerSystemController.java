package controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.google.gson.Gson;


import dataBase.DataBase;
import ocsf.server.ConnectionToClient;
import parkChange.ParkChangeDetails;


public class ParkManagerSystemController {
	Gson gson = new Gson();

	private static ParkManagerSystemController ParkManagerSystemControllerInstacne = null;

	private ParkManagerSystemController() {

	}

	public static ParkManagerSystemController getInstance() {

		if (ParkManagerSystemControllerInstacne == null)
			ParkManagerSystemControllerInstacne = new ParkManagerSystemController();
		return ParkManagerSystemControllerInstacne;
	}
	
	public String getFunc(String MethodName, String data, ConnectionToClient client) throws SQLException {

		switch (MethodName) {
		case "changeParkDetails":
			return changeParkDetails(data, client);
		case "initParkValues":
			return initParkValues(data, client);
	
		}
		return "faild";
	}

	private String initParkValues(String data, ConnectionToClient client) throws SQLException {
		 String queryc = "SELECT * FROM gonaturedb.uptodateinformation WHERE nameOfVal = \"parkCapacity"+data+"\"" +";";
		 String queryd = "SELECT * FROM gonaturedb.uptodateinformation WHERE nameOfVal = \"parkDifference"+data+"\"" +";";
		 String queryds = "SELECT * FROM gonaturedb.uptodateinformation WHERE nameOfVal = \"parkDiscount"+data+"\"" +";";
		 ResultSet res = DataBase.getInstance().search(queryc);
		if (isEmpty(res) == 0) {
			return "faild";
		}
		ArrayList<String> result=new ArrayList <String>();
			result.add(res.getString(2));
			
		  res = DataBase.getInstance().search(queryd);
			if (isEmpty(res) == 0) {
				return "faild";
			}
			result.add(res.getString(2));
		
			 res = DataBase.getInstance().search(queryds);
				if (isEmpty(res) == 0) {
					return "faild";
				}
				result.add(res.getString(2));
		        return gson.toJson(result);
	}

	private String changeParkDetails(String data, ConnectionToClient client) {
		ParkChangeDetails parkdetails = gson.fromJson(data, ParkChangeDetails.class);
		String query="INSERT INTO `gonaturedb`.`parkdetailsapproval` (`parkname`, `parkcapacity`, `difference`, `discount`) VALUES "
				+parkdetails.toString()+ ";" ;
		if (DataBase.getInstance().update(query)) {
			return "The changing are waiting for approval";
		}
		return "faild";
	}
	
	
	
	private int isEmpty(ResultSet rs) {
		int size = 0;
		if (rs != null) {
			try {
				rs.last();
				size = rs.getRow(); 
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return size;
	}
	
	
	
	
}
