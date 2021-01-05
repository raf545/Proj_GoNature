package cardReader;

/**
 * this class is made to contain the data of (id, park name and number of visitors) of a specific reservation.
 * 
 * @author dan
 *
 */
public class IdAndParkAndNum {

	private String id;
	private String parkName;
	private String numOfVisitors;

	/**
	 * @param id
	 * @param parkName
	 * @param numOfVisitors
	 */
	public IdAndParkAndNum(String id, String parkName, String numOfVisitors) {
		this.id = id;
		this.parkName = parkName;
		this.numOfVisitors = numOfVisitors;
	}

	public String getNumOfVisitors() {
		return numOfVisitors;
	}

	public void setNumOfVisitors(String numOfVisitors) {
		this.numOfVisitors = numOfVisitors;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParkName() {
		return parkName;
	}

	public void setParkName(String parkName) {
		this.parkName = parkName;
	}
}
