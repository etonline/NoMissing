package edu.ntust.cs.idsl.nomissing.util;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract.Calendars;

@SuppressLint({ "NewApi", "UseSparseArrays" })
public class CalendarUtil {

	// Projection array. Creating indices for this array instead of doing
	// dynamic lookups improves performance.
	public static final String[] EVENT_PROJECTION = new String[] {
	    Calendars._ID,                           // 0
	    Calendars.ACCOUNT_NAME,                  // 1
	    Calendars.CALENDAR_DISPLAY_NAME,         // 2
	    Calendars.OWNER_ACCOUNT,                  // 3
	    Calendars.CALENDAR_ACCESS_LEVEL			// 4
	};
	  
	// The indices for the projection array above.
	private static final int PROJECTION_ID_INDEX = 0;
	private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
	private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
	private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;
	private static final int PROJECTION_ALLOWED_AVAILABILITY_INDEX = 4;
	
	
	public static HashMap<Long, String> getCalendar(Context context, String account) {
		HashMap<Long, String> calendar = new HashMap<Long, String>();
		
		Cursor cur = null;
		ContentResolver cr = context.getContentResolver();
		Uri uri = Calendars.CONTENT_URI;   
		String selection = "((" + Calendars.ACCOUNT_NAME + " = ?) AND (" 
		                        + Calendars.ACCOUNT_TYPE + " = ?) AND ("
		                        + Calendars.OWNER_ACCOUNT + " = ?))";
		String[] selectionArgs = new String[] {account, "com.google", account}; 
		
//		String selection = "((" + Calendars.ACCOUNT_NAME + " = ?) AND (" 
//                + Calendars.ACCOUNT_TYPE + " = ?))";
//		String[] selectionArgs = new String[] {account, "com.google"}; 		
		
		// Submit the query and get a Cursor object back. 
		cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
		
		while (cur.moveToNext()) {
		    long calID = 0;
		    String displayName = null;
		    String accountName = null;
		    String ownerName = null;
		    String availbility = null;
		      
		    // Get the field values
		    calID = cur.getLong(PROJECTION_ID_INDEX);
		    displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
		    accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
		    ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);
		    availbility = cur.getString(PROJECTION_ALLOWED_AVAILABILITY_INDEX);
		    
		    ToastMaker.toast(context, calID + " | " + displayName + " | " + availbility);
		    calendar.put(calID, displayName);
		}		
		
		return calendar;
	}
	
}
