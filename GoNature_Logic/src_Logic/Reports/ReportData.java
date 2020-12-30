package Reports;

public class ReportData {

	
	private String parkname;
	private String year;
	private String month;
	
	public ReportData(String parkname, String year, String month) {
		this.parkname = parkname;
		this.year = year;
		this.month = month;
	}

	public String getParkname() {
		return parkname;
	}

	public void setParkname(String parkname) {
		this.parkname = parkname;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}
	
	
	
	
	
}
