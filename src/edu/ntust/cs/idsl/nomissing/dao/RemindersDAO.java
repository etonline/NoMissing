package edu.ntust.cs.idsl.nomissing.dao;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.CalendarContract.Reminders;

@SuppressLint("NewApi")
public class RemindersDAO {
	
	private static RemindersDAO instance;
	private Context context;
	
	private RemindersDAO(Context context) {
		this.context = context;
	}
	
	public static synchronized RemindersDAO getInstance(Context context) {
		if (instance == null) {
			instance = new RemindersDAO(context);
		}
		return instance;
	}	
	
	public void insert(long eventID, int minutes) {
		ContentResolver cr = context.getContentResolver();
		ContentValues values = new ContentValues();
		values.put(Reminders.MINUTES, minutes);
		values.put(Reminders.EVENT_ID, eventID);
		values.put(Reminders.METHOD, Reminders.METHOD_ALERT);
		Uri uri = cr.insert(Reminders.CONTENT_URI, values);
	}
}
