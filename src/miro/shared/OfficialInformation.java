package miro.shared;

public class OfficialInformation {
	public static final int CURRENT_YEAR = 2010;

	public static final Record[] numberDaysByMonthArray = {
			new Record(30, new Time(1, CURRENT_YEAR)),
			new Record(29, new Time(2, CURRENT_YEAR)),
			new Record(31, new Time(3, CURRENT_YEAR)),
			new Record(31, new Time(4, CURRENT_YEAR)),
			new Record(30, new Time(5, CURRENT_YEAR)),
			new Record(31, new Time(6, CURRENT_YEAR)),
			new Record(30, new Time(7, CURRENT_YEAR)),
			new Record(30, new Time(8, CURRENT_YEAR)),
			new Record(31, new Time(9, CURRENT_YEAR)),
			new Record(31, new Time(10, CURRENT_YEAR)),
			new Record(31, new Time(11, CURRENT_YEAR)),
			new Record(31, new Time(12, CURRENT_YEAR)) };

	public static final Record[] numberOfficialHolidaysArray = {
			new Record(1, new Time(1, CURRENT_YEAR)),
			new Record(0, new Time(2, CURRENT_YEAR)),
			new Record(0, new Time(3, CURRENT_YEAR)),
			new Record(1, new Time(4, CURRENT_YEAR)),
			new Record(3, new Time(5, CURRENT_YEAR)),
			new Record(0, new Time(6, CURRENT_YEAR)),
			new Record(1, new Time(7, CURRENT_YEAR)),
			new Record(0, new Time(8, CURRENT_YEAR)),
			new Record(0, new Time(9, CURRENT_YEAR)),
			new Record(0, new Time(10, CURRENT_YEAR)),
			new Record(4, new Time(11, CURRENT_YEAR)),
			new Record(5, new Time(12, CURRENT_YEAR)) };
}
