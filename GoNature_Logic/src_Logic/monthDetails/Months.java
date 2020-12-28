package monthDetails;

public enum Months {
	JANUARY(1),
	FEBRUARY(2),
	MARCH(3),
	APRIL(4),
	MAY(5),
	JUNE(6),
	JULY(7),
	AUGUST(8),
	SEPTEMBER(9),
	OCTOBER(10),
	NOVEMBER(11),
	DECEMBER(12);

	Months(int i) {
		//------ Use this for timestamps: Months.APRIL.ordinal(); ----------
	}
	public static int getNumberOfDays(Months m)
	{
		switch(m)
		{
		case JANUARY:
			return 31;
		case FEBRUARY:
			return 28;	//Need to pay attention to Leap Year
		case MARCH:
			return 31;
		case APRIL:
			return 30;
		case MAY:
			return 31;
		case JUNE:
			return 30;
		case JULY:
			return 31;
		case AUGUST:
			return 31;
		case SEPTEMBER:
			return 30;
		case OCTOBER:
			return 31;
		case NOVEMBER:
			return 30;
		case DECEMBER:
			return 31;					
		}
		return 0;
		
	}

}
