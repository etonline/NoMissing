package edu.ntust.cs.idsl.nomissing.calendar;

import android.content.Context;

public class CalendarProviderDaoFactory {
	
	public static CalendarDao getCalendarDao(Context context) {
		return new CalendarDao(context);
	}

	public static EventDao getEventDao(Context context) {
		return new EventDao(context);
	}

}
