package edu.ntust.cs.idsl.nomissing.dao;

import android.content.Context;
import edu.ntust.cs.idsl.nomissing.dao.calendar.CalendarProviderDaoFactory;
import edu.ntust.cs.idsl.nomissing.dao.sqlite.SQLiteDaoFactory;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
public abstract class DaoFactory {

    private static final int DEFAULT_CALENDAR = 0;

    public static SQLiteDaoFactory getSQLiteDaoFactory() {
        return new SQLiteDaoFactory();
    }

    public static CalendarProviderDaoFactory getCalendarProviderDaoFactory() {
        return new CalendarProviderDaoFactory();
    }

    public static DaoFactory getEventDaoFactory(long calendarID) {
        DaoFactory daoFactory = null;

        if (calendarID == DEFAULT_CALENDAR) {
            daoFactory = new SQLiteDaoFactory();
        } else {
            daoFactory = new CalendarProviderDaoFactory();
        }

        return daoFactory;
    }

    public abstract IEventDao createEventDao(Context context);
}
