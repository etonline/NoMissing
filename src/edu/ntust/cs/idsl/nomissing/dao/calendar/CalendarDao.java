package edu.ntust.cs.idsl.nomissing.dao.calendar;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.provider.CalendarContract.Calendars;
import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.dao.ICalendarDao;
import edu.ntust.cs.idsl.nomissing.model.Calendar;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
@SuppressLint("InlinedApi")
public class CalendarDao extends CalendarProviderDao implements ICalendarDao {

    public CalendarDao(Context context) {
        super(context);
    }

    @Override
    public List<Calendar> findAll() {
        List<Calendar> calendars = new ArrayList<Calendar>();
        Calendar defaultCalendar = Calendar.getDefaultCalendar();
        defaultCalendar.setName(context.getString(R.string.default_calendar));
        calendars.add(defaultCalendar);

        Cursor cursor = contentResolver.query(calendarsURI, CALENDARS_PROJECTION, null, null, null);
        while (cursor.moveToNext()) {
            long calendarID = cursor.getLong(CALENDARS_ID_INDEX);
            String calendarName = cursor.getString(CALENDARS_DISPLAY_NAME_INDEX);
            Calendar calendar = new Calendar(calendarID, calendarName);
            calendars.add(calendar);
        }
        cursor.close();

        return calendars;
    }

    @Override
    public List<Calendar> findByAccessLevel(int accessLevel) {
        List<Calendar> calendars = new ArrayList<Calendar>();
        Calendar defaultCalendar = Calendar.getDefaultCalendar();
        defaultCalendar.setName(context.getString(R.string.default_calendar));
        calendars.add(defaultCalendar);

        String selection = Calendars.CALENDAR_ACCESS_LEVEL + " = ?";
        String[] selectionArgs = new String[] { String.valueOf(Calendars.CAL_ACCESS_OWNER) };

        Cursor cursor = contentResolver.query(calendarsURI, CALENDARS_PROJECTION, selection, selectionArgs, null);
        while (cursor.moveToNext()) {
            long calendarID = cursor.getLong(CALENDARS_ID_INDEX);
            String calendarName = cursor.getString(CALENDARS_DISPLAY_NAME_INDEX);
            Calendar calendar = new Calendar(calendarID, calendarName);
            calendars.add(calendar);
        }
        cursor.close();

        return calendars;
    }

}
