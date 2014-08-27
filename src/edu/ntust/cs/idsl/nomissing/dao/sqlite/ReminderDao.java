package edu.ntust.cs.idsl.nomissing.dao.sqlite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import edu.ntust.cs.idsl.nomissing.dao.IReminderDao;
import edu.ntust.cs.idsl.nomissing.database.NoMissingDB;
import edu.ntust.cs.idsl.nomissing.model.Reminder;

public class ReminderDao extends SQLiteDao implements IReminderDao {

	private static final String[] REMINDERS_COLUMNS = new String[] {
		NoMissingDB.REMINDERS_KEY_ID,
		NoMissingDB.REMINDERS_KEY_CALENDAR_ID,
		NoMissingDB.REMINDERS_KEY_EVENT_ID,
		NoMissingDB.REMINDERS_KEY_REMINDER_TIME,
		NoMissingDB.REMINDERS_KEY_AUDIO,
		NoMissingDB.REMINDERS_KEY_CREATED_AT,
		NoMissingDB.REMINDERS_KEY_UPDATED_AT
	};
	
	public ReminderDao(Context context) {
		super(context);
	}

	@Override
	public long insert(Reminder reminder) {
		ContentValues values = new ContentValues();
		values.put(NoMissingDB.REMINDERS_KEY_CALENDAR_ID, reminder.getCalendarID()); 
		values.put(NoMissingDB.REMINDERS_KEY_EVENT_ID, reminder.getEventID()); 
		values.put(NoMissingDB.REMINDERS_KEY_REMINDER_TIME, reminder.getReminderTime()); 
		values.put(NoMissingDB.REMINDERS_KEY_AUDIO, reminder.getAudio()); 
		values.put(NoMissingDB.REMINDERS_KEY_CREATED_AT, reminder.getCreatedAt()); 
		values.put(NoMissingDB.REMINDERS_KEY_UPDATED_AT, reminder.getUpdatedAt()); 

		open();
		long row = db.insert(NoMissingDB.TABLE_REMINDERS, null, values);
		close();
		
		return row;
	}

	@Override
	public long update(Reminder reminder) {
		String whereClause = NoMissingDB.REMINDERS_KEY_ID + " = ?";
		String[] whereArgs = new String[] { String.valueOf(reminder.getId()) };

		ContentValues values = new ContentValues();
		values.put(NoMissingDB.REMINDERS_KEY_ID, reminder.getId()); 
		values.put(NoMissingDB.REMINDERS_KEY_CALENDAR_ID, reminder.getCalendarID()); 
		values.put(NoMissingDB.REMINDERS_KEY_EVENT_ID, reminder.getEventID()); 
		values.put(NoMissingDB.REMINDERS_KEY_REMINDER_TIME, reminder.getReminderTime()); 
		values.put(NoMissingDB.REMINDERS_KEY_AUDIO, reminder.getAudio()); 
		values.put(NoMissingDB.REMINDERS_KEY_CREATED_AT, reminder.getCreatedAt()); 
		values.put(NoMissingDB.REMINDERS_KEY_UPDATED_AT, reminder.getUpdatedAt()); 

		open();
		int row =  db.update(NoMissingDB.TABLE_REMINDERS, values, whereClause, whereArgs);
		close();
		
		return row;
	}

	@Override
	public long delete(long id) {
		String whereClause = NoMissingDB.REMINDERS_KEY_ID + " = ?";
		String[] whereArgs = new String[] { String.valueOf(id) };
		
		open();
		int row = db.delete(NoMissingDB.TABLE_REMINDERS, whereClause, whereArgs);
		close();
		
		return row;
	}
	
	@Override
	public List<Reminder> findAll() {
		List<Reminder> reminders = new ArrayList<Reminder>();

		open();
		Cursor cursor = db.query(NoMissingDB.TABLE_REMINDERS, REMINDERS_COLUMNS, null, null, null, null, null);
		while (cursor.moveToNext()) {
			Reminder reminder = new Reminder();
			reminder.setId(cursor.getLong(cursor.getColumnIndex(NoMissingDB.REMINDERS_KEY_ID)));
			reminder.setCalendarID(cursor.getLong(cursor.getColumnIndex(NoMissingDB.REMINDERS_KEY_CALENDAR_ID)));
			reminder.setEventID(cursor.getLong(cursor.getColumnIndex(NoMissingDB.REMINDERS_KEY_EVENT_ID)));
			reminder.setReminderTime(cursor.getLong(cursor.getColumnIndex(NoMissingDB.REMINDERS_KEY_REMINDER_TIME)));
			reminder.setAudio(cursor.getString(cursor.getColumnIndex(NoMissingDB.REMINDERS_KEY_AUDIO)));
			reminder.setCreatedAt(cursor.getLong(cursor.getColumnIndex(NoMissingDB.REMINDERS_KEY_CREATED_AT)));
			reminder.setUpdatedAt(cursor.getLong(cursor.getColumnIndex(NoMissingDB.REMINDERS_KEY_UPDATED_AT)));
			reminders.add(reminder);
		}
		cursor.close();
		close();
		
		return reminders;
	}

	@Override
	public Reminder find(long id) {
		Reminder reminder = new Reminder();
		String selection = NoMissingDB.REMINDERS_KEY_ID + " = ?";
		String[] selectionArgs = new String[] { String.valueOf(id) };
		
		open();
		Cursor cursor = db.query(NoMissingDB.TABLE_REMINDERS, REMINDERS_COLUMNS, selection, selectionArgs, null, null, null);
		if (cursor.moveToFirst()) {   
			reminder.setId(cursor.getLong(cursor.getColumnIndex(NoMissingDB.REMINDERS_KEY_ID)));
			reminder.setCalendarID(cursor.getLong(cursor.getColumnIndex(NoMissingDB.REMINDERS_KEY_CALENDAR_ID)));
			reminder.setEventID(cursor.getLong(cursor.getColumnIndex(NoMissingDB.REMINDERS_KEY_EVENT_ID)));
			reminder.setReminderTime(cursor.getLong(cursor.getColumnIndex(NoMissingDB.REMINDERS_KEY_REMINDER_TIME)));
			reminder.setAudio(cursor.getString(cursor.getColumnIndex(NoMissingDB.REMINDERS_KEY_AUDIO)));
			reminder.setCreatedAt(cursor.getLong(cursor.getColumnIndex(NoMissingDB.REMINDERS_KEY_CREATED_AT)));
			reminder.setUpdatedAt(cursor.getLong(cursor.getColumnIndex(NoMissingDB.REMINDERS_KEY_UPDATED_AT)));
		}
		cursor.close();
		close();

		return reminder;
	}	
	
	@Override
	public Reminder find(long calendarID, long eventID) {
		Reminder reminder = new Reminder();
		
		String selection = 
				"((" + NoMissingDB.REMINDERS_KEY_CALENDAR_ID + " = ?) " + 
				"AND ("+ NoMissingDB.REMINDERS_KEY_EVENT_ID + " = ?))";
		
		String[] selectionArgs = new String[] {
				String.valueOf(calendarID), 
				String.valueOf(eventID)};
		
		open();
		Cursor cursor = db.query(NoMissingDB.TABLE_REMINDERS, REMINDERS_COLUMNS, selection, selectionArgs, null, null, null);
		if (cursor.moveToFirst()) {
			reminder.setId(cursor.getLong(cursor.getColumnIndex(NoMissingDB.REMINDERS_KEY_ID)));
			reminder.setCalendarID(cursor.getLong(cursor.getColumnIndex(NoMissingDB.REMINDERS_KEY_CALENDAR_ID)));
			reminder.setEventID(cursor.getLong(cursor.getColumnIndex(NoMissingDB.REMINDERS_KEY_EVENT_ID)));
			reminder.setReminderTime(cursor.getLong(cursor.getColumnIndex(NoMissingDB.REMINDERS_KEY_REMINDER_TIME)));
			reminder.setAudio(cursor.getString(cursor.getColumnIndex(NoMissingDB.REMINDERS_KEY_AUDIO)));
			reminder.setCreatedAt(cursor.getLong(cursor.getColumnIndex(NoMissingDB.REMINDERS_KEY_CREATED_AT)));
			reminder.setUpdatedAt(cursor.getLong(cursor.getColumnIndex(NoMissingDB.REMINDERS_KEY_UPDATED_AT)));
		}
		cursor.close();
		close();

		return reminder;
	}

}
