package edu.ntust.cs.idsl.nomissing.dao;
//package edu.ntust.cs.idsl.nomissing.dao.calendar;
//
//import android.annotation.SuppressLint;
//import android.content.ContentUris;
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.net.Uri;
//import android.provider.CalendarContract.Calendars;
//import android.provider.CalendarContract.Instances;
//import android.provider.CalendarContract.Reminders;
//import edu.ntust.cs.idsl.nomissing.model.Calendar;
//import edu.ntust.cs.idsl.nomissing.model.Event;
//import edu.ntust.cs.idsl.nomissing.model.Reminder;
//
//@SuppressLint("NewApi")
//public class ReminderDao extends CalendarProviderDao implements IReminderDao {
//
//	public ReminderDao(Context context) {
//		super(context);
//	}
//
//	@Override
//	public long insert(Reminder reminder) {
//		ContentValues values = new ContentValues();
//		values.put(Reminders.MINUTES, reminder.getMinutes());
//		values.put(Reminders.EVENT_ID, reminder.getEventID());
//		values.put(Reminders.METHOD, Reminders.METHOD_ALERT);
//		Uri uri = contentResolver.insert(Reminders.CONTENT_URI, values);
//
//		long reminderID = Long.parseLong(uri.getLastPathSegment());
//		return reminderID;
//	}
//
//	@Override
//	public Reminder find(long eventID) {
//		Reminder reminder = new Reminder();  
//		Cursor cursor = null;
//		
//		String selection = Reminders.EVENT_ID + " = ?";
//		String[] selectionArgs = new String[] { String.valueOf(eventID) };
//		
//		cursor = contentResolver.query(Reminders.CONTENT_URI, REMINDERS_PROJECTION, selection, selectionArgs, null);
//		while (cursor.moveToNext()) {
//			reminder.setReminderID(cursor.getLong(REMINDERS_ID_INDEX));
//			reminder.setEventID(cursor.getLong(REMINDERS_EVENT_ID_INDEX));
//			reminder.setMinutes(cursor.getInt(REMINDERS_MINUTES_INDEX));
//		}	
//		
//		return reminder;
//	}
//
//}
