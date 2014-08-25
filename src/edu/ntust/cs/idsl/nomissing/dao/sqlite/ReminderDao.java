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

	public ReminderDao(Context context) {
		super(context);
	}

	@Override
	public List<Reminder> findAll() {
		List<Reminder> reminders = new ArrayList<Reminder>();
		String selectQuery = "SELECT  * FROM " + NoMissingDB.TABLE_REMINDERS;
		
		open();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Reminder reminder = new Reminder(
						cursor.getLong(0),
						cursor.getLong(1),
						cursor.getLong(2),
						cursor.getLong(3),
						cursor.getString(4),
						cursor.getLong(5),
						cursor.getLong(6)
						);
				reminders.add(reminder);
			} while (cursor.moveToNext());
		}
		cursor.close();
		close();
		
		return reminders;
	}

	@Override
	public Reminder find(long calendarID, long eventID) {
		String selectQuery = "SELECT  * FROM " + NoMissingDB.TABLE_REMINDERS +
				" WHERE " + NoMissingDB.REMINDERS_KEY_CALENDAR_ID + " = " + calendarID +
				" AND " + NoMissingDB.REMINDERS_KEY_EVENT_ID + " = " + eventID;
		
		open();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor != null)
			cursor.moveToFirst();
		Reminder reminder = new Reminder(cursor.getLong(0), cursor.getLong(1),
				cursor.getLong(2), cursor.getLong(3), cursor.getString(4),
				cursor.getLong(5), cursor.getLong(6));
		cursor.close();
		close();

		return reminder;
	}

	@Override
	public long insert(Reminder reminder) {
		open();

		ContentValues values = new ContentValues();
		values.put(NoMissingDB.REMINDERS_KEY_CALENDAR_ID, reminder.getCalendarID()); 
		values.put(NoMissingDB.REMINDERS_KEY_EVENT_ID, reminder.getEventID()); 
		values.put(NoMissingDB.REMINDERS_KEY_REMINDER_TIME, reminder.getReminderTime()); 
		values.put(NoMissingDB.REMINDERS_KEY_AUDIO, reminder.getAudio()); 
		values.put(NoMissingDB.REMINDERS_KEY_CREATED_AT, reminder.getCreatedAt()); 
		values.put(NoMissingDB.REMINDERS_KEY_UPDATED_AT, reminder.getUpdatedAt()); 

		long row = db.insert(NoMissingDB.TABLE_REMINDERS, null, values);
		close();
		
		return row;
	}

	@Override
	public long update(Reminder reminder) {
		open();

		ContentValues values = new ContentValues();
		values.put(NoMissingDB.REMINDERS_KEY_ID, reminder.getId()); 
		values.put(NoMissingDB.REMINDERS_KEY_CALENDAR_ID, reminder.getCalendarID()); 
		values.put(NoMissingDB.REMINDERS_KEY_EVENT_ID, reminder.getEventID()); 
		values.put(NoMissingDB.REMINDERS_KEY_REMINDER_TIME, reminder.getReminderTime()); 
		values.put(NoMissingDB.REMINDERS_KEY_AUDIO, reminder.getAudio()); 
		values.put(NoMissingDB.REMINDERS_KEY_CREATED_AT, reminder.getCreatedAt()); 
		values.put(NoMissingDB.REMINDERS_KEY_UPDATED_AT, reminder.getUpdatedAt()); 

		int row =  db.update(NoMissingDB.TABLE_REMINDERS, values, NoMissingDB.REMINDERS_KEY_ID + " = ?",
				new String[] { String.valueOf(reminder.getId()) });
		
		close();
		
		return row;
	}

	@Override
	public long delete(long id) {
		open();
		int row = db.delete(NoMissingDB.TABLE_REMINDERS, 
				NoMissingDB.REMINDERS_KEY_ID + " = ?", new String[] { String.valueOf(id) });
		close();
		
		return row;
	}

	@Override
	public Reminder find(long id) {
		String selectQuery = "SELECT  * FROM " + NoMissingDB.TABLE_REMINDERS +
				" WHERE " + NoMissingDB.REMINDERS_KEY_ID + " = " + id;
		
		open();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor != null)
			cursor.moveToFirst();
		Reminder reminder = new Reminder(cursor.getLong(0), cursor.getLong(1),
				cursor.getLong(2), cursor.getLong(3), cursor.getString(4),
				cursor.getLong(5), cursor.getLong(6));
		cursor.close();
		close();

		return reminder;
	}

}
