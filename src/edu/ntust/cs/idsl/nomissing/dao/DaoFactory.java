package edu.ntust.cs.idsl.nomissing.dao;

import android.content.Context;
import edu.ntust.cs.idsl.nomissing.dao.calendar.CalendarProviderDaoFactory;
import edu.ntust.cs.idsl.nomissing.dao.sqlite.SQLiteDaoFactory;

public abstract class DaoFactory {

//	public static final int SQLITE = 0;
//	public static final int CALENDAR_PROVIDER = 1;
	
	public static SQLiteDaoFactory getSQLiteDaoFactory() {
		return new SQLiteDaoFactory();
	}

	public static CalendarProviderDaoFactory getCalendarProviderDaoFactory() {
		return new CalendarProviderDaoFactory();
	}
	
	public static DaoFactory getDaoFactory(long calendarID) {
		DaoFactory daoFactory = null;
		
		if (calendarID > 0) {
			daoFactory = new SQLiteDaoFactory();
		} else {
			daoFactory = new CalendarProviderDaoFactory();
		}

		return daoFactory;
	}
	
	public abstract IEventDao createEventDao(Context context);
}
