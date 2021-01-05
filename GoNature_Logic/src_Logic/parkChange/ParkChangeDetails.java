package parkChange;

/**
 * define the data that has to be save for park change request from the server
 * 
 * @author Ziv Tziunit
 *
 */
public class ParkChangeDetails {
	private String parkCapacity;
	private String difference;
	private String discount;
	private String parkName;
	private String oldParkCapacity;
	private String oldDifference;
	private String oldDiscount;

	public ParkChangeDetails(String parkCapacity, String difference, String discount, String parkName,
			String oldParkCapacity, String oldDifference, String oldDiscount) {
		this.parkCapacity = parkCapacity;
		this.difference = difference;
		this.discount = discount;
		this.parkName = parkName;
		this.oldParkCapacity = oldParkCapacity;
		this.oldDiscount = oldDiscount;
		this.oldDifference = oldDifference;
	}

	public String getOldParkCapacity() {
		return oldParkCapacity;
	}

	public void setOldParkCapacity(String oldParkCapacity) {
		this.oldParkCapacity = oldParkCapacity;
	}

	public String getOldDifference() {
		return oldDifference;
	}

	public void setOldDifference(String oldDifference) {
		this.oldDifference = oldDifference;
	}

	public String getOldDiscount() {
		return oldDiscount;
	}

	public void setOldDiscount(String oldDiscount) {
		this.oldDiscount = oldDiscount;
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
		return "(" + "\"" + parkName + "\", " + parkCapacity + ", " + difference + ", " + discount + ", "
				+ oldParkCapacity + ", " + oldDifference + ", " + oldDiscount + ")";
	}

}
