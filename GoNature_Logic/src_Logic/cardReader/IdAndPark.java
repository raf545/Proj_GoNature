package cardReader;

public class IdAndPark {

	private String id;
	private String parkName;

	public IdAndPark(String id, String parkName) {
		this.id = id;
		this.parkName = parkName;
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
