package edu.ntust.cs.idsl.nomissing.dao;

import edu.ntust.cs.idsl.nomissing.dao.calendar.CalendarProviderDaoFactory;
import edu.ntust.cs.idsl.nomissing.dao.sqlite.SQLiteDaoFactory;

public abstract class DaoFactory {

	public static final int SQLITE = 0;
	public static final int CALENDAR_PROVIDER = 1;
	
	public static SQLiteDaoFactory getSQLiteDaoFactory() {
		return new SQLiteDaoFactory();
	}

	public static CalendarProviderDaoFactory getCalendarProviderDaoFactory() {
		return new CalendarProviderDaoFactory();
	}
	
	public static DaoFactory getDaoFactory(int factory) {
		DaoFactory daoFactory = null;
		switch (factory) {
		case SQLITE:
			daoFactory = new SQLiteDaoFactory();
			break;
		case CALENDAR_PROVIDER:
			daoFactory = new CalendarProviderDaoFactory();
		default:
			break;
		}
		return daoFactory;
	}
}
