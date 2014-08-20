package edu.ntust.cs.idsl.nomissing.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.ntust.cs.idsl.nomissing.model.Calendar;
import android.accounts.Account;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract.Calendars;

@SuppressLint({ "NewApi", "UseSparseArrays" })
public class CalendarUtil {

	public static final String[] EVENT_PROJECTION = new String[] {
	    Calendars._ID,                           // 0
	    Calendars.ACCOUNT_NAME,                  // 1
	    Calendars.CALENDAR_DISPLAY_NAME,         // 2
	    Calendars.OWNER_ACCOUNT                  // 3
	};
	  
	private static final int PROJECTION_ID_INDEX = 0;
	private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
	private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
	private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;
	
	public static List<Calendar> getCalendars(Context context) {
		List<Calendar> calendars = new ArrayList<Calendar>();
		Account[] googleAccounts = AccountUtil.getGoogleAccounts(context);
		
		for (Account googleAccount : googleAccounts) {
			Cursor cur = null;
			ContentResolver cr = context.getContentResolver();
			Uri uri = Calendars.CONTENT_URI;   
			
			String selection = "((" + Calendars.ACCOUNT_NAME + " = ?) AND (" 
			                        + Calendars.ACCOUNT_TYPE + " = ?) AND ("
			                        + Calendars.CALENDAR_ACCESS_LEVEL + " = ?))";
			String[] selectionArgs = new String[] {
					googleAccount.name, 
					AccountUtil.ACCOUNT_TYPE_GOOGLE, 
					String.valueOf(Calendars.CAL_ACCESS_OWNER)}; 			
			
			cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
			while (cur.moveToNext()) {
			    long calendarID = cur.getLong(PROJECTION_ID_INDEX);
			    String calendarName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
				Calendar calendar = new Calendar(calendarID, calendarName);
			    calendars.add(calendar);
			}					
		}
			
		return calendars;
	}
	
}
