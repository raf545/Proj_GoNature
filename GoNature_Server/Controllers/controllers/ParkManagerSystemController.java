package controllers;

import com.google.gson.Gson;

import ocsf.server.ConnectionToClient;

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
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
	
	
	
	
}
