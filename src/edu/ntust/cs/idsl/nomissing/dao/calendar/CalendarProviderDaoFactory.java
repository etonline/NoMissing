package edu.ntust.cs.idsl.nomissing.dao.calendar;

import edu.ntust.cs.idsl.nomissing.dao.DaoFactory;
import android.content.Context;

public class CalendarProviderDaoFactory extends DaoFactory {
	
	public static CalendarDao createCalendarDao(Context context) {
		return new CalendarDao(context);
	}

	public static EventDao creatEventDao(Context context) {
		return new EventDao(context);
	}
	
//	public static ReminderDao createReminder(Context context) {
//		return new ReminderDao(context);
//	}

}
