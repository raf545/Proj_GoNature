package parkChange;

public class ParkChangeDetails {
	private String parkCapacity;
	private String difference;
	private String discount;
	private String parkName;
	
	
	public ParkChangeDetails(String parkCapacity,String difference,String discount,String parkName)
	{
		this.parkCapacity=parkCapacity;
		this.difference=difference;
		this.discount=discount;
		this.parkName=parkName;
	}



	public String getParkName() {
		return parkName;
	}



	public void setParkName(String parkName) {
		this.parkName = parkName;
	}



	public String getParkCapacity() {
		return parkCapacity;
	}



	public void setParkCapacity(String parkCapacity) {
		this.parkCapacity = parkCapacity;
	}



	public String getDifference() {
		return difference;
	}



	public void setDifference(String difference) {
		this.difference = difference;
	}



	





	public String getDiscount() {
		return discount;
	}


	public void setDiscount(String discount) {
		this.discount = discount;
	}
	@Override
	public String toString() {
		return "("+"\""+  parkName+"\", " + parkCapacity + ", " + difference + ", " + discount + ")";
	}
	
}
