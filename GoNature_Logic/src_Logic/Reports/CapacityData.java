package Reports;

import java.util.Collection;

public class CapacityData  {
	@Override
	public String toString() {
		return date +" - Park at "+  Float.valueOf(amoutOfP)*100 +"% capacity" ;
	}


	private String date;
	private String amoutOfP;
	
	
	public CapacityData(String date, String amoutOfP) {
		super();
		this.date = date;
		this.amoutOfP = amoutOfP;
	}


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public String getAmoutOfP() {
		return amoutOfP;
	}


	public void setAmoutOfP(String amoutOfP) {
		this.amoutOfP = amoutOfP;
	}

}
