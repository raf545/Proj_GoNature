package controllers;

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
	
	public String getFunc(String MethodName, String data, ConnectionToClient client) {

		switch (MethodName) {
		case "changeParkDetails":
			return changeParkDetails(data, client);

	
		}
		return data;
	}

	private String changeParkDetails(String data, ConnectionToClient client) {
		ParkChangeDetails parkdetails = gson.fromJson(data, ParkChangeDetails.class);
		String query="INSERT INTO `gonaturedb`.`parkdetailsapproval` (`parkname`, `parkcapacity`, `difference`, `discount`) VALUES "
				+parkdetails.toString()+ ";" ;
		if (DataBase.getInstance().update(query)) {
			return "the changing are waiting for approval";
		}
		return "faild";
	}
	
	
	
	
	
	
	
	
}
