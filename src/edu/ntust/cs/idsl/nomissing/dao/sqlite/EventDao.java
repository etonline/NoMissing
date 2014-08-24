package edu.ntust.cs.idsl.nomissing.dao.sqlite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import edu.ntust.cs.idsl.nomissing.dao.IEventDao;
import edu.ntust.cs.idsl.nomissing.database.NoMissingDB;
import edu.ntust.cs.idsl.nomissing.model.Event;

public class EventDao extends SQLiteDao implements IEventDao {

	public EventDao(Context context) {
		super(context);
	}

	@Override
	public long insert(Event event) {
		open();

		ContentValues values = new ContentValues();
//		values.put(NoMissingDB.EVENTS_KEY_ID, event.getId()); 
		values.put(NoMissingDB.EVENTS_KEY_CALENDAR_ID, event.getCalendarID());
		values.put(NoMissingDB.EVENTS_KEY_TITLE, event.getTitle());
		values.put(NoMissingDB.EVENTS_KEY_LOCATION, event.getLocation());
		values.put(NoMissingDB.EVENTS_KEY_DESCRIPTION, event.getDescription());
		values.put(NoMissingDB.EVENTS_KEY_START_TIME, event.getStartTime());
		values.put(NoMissingDB.EVENTS_KEY_END_TIME, event.getEndTime());
		values.put(NoMissingDB.EVENTS_KEY_HAS_REMINDER, event.hasReminder());
		values.put(NoMissingDB.EVENTS_KEY_CREATED_AT, event.getCreatedAt());
		values.put(NoMissingDB.EVENTS_KEY_UPDATED_AT, event.getUpdatedAt());

		long row = db.insert(NoMissingDB.TABLE_EVENTS, null, values);
		close();
		
		return row;
	}

	@Override
	public long update(Event event) {
		open();

		ContentValues values = new ContentValues();
//		values.put(NoMissingDB.EVENTS_KEY_ID, event.getId()); 
		values.put(NoMissingDB.EVENTS_KEY_CALENDAR_ID, event.getCalendarID());
		values.put(NoMissingDB.EVENTS_KEY_TITLE, event.getTitle());
		values.put(NoMissingDB.EVENTS_KEY_LOCATION, event.getLocation());
		values.put(NoMissingDB.EVENTS_KEY_DESCRIPTION, event.getDescription());
		values.put(NoMissingDB.EVENTS_KEY_START_TIME, event.getStartTime());
		values.put(NoMissingDB.EVENTS_KEY_END_TIME, event.getEndTime());
		values.put(NoMissingDB.EVENTS_KEY_HAS_REMINDER, event.hasReminder());
		values.put(NoMissingDB.EVENTS_KEY_CREATED_AT, event.getCreatedAt());
		values.put(NoMissingDB.EVENTS_KEY_UPDATED_AT, event.getUpdatedAt());

		int row =  db.update(NoMissingDB.TABLE_EVENTS, values, NoMissingDB.EVENTS_KEY_ID + " = ?",
				new String[] { String.valueOf(event.getId()) });
		
		close();
		
		return row;
	}

	@Override
	public long delete(long id) {
		open();
		int row = db.delete(NoMissingDB.TABLE_EVENTS, 
				NoMissingDB.EVENTS_KEY_ID + " = ?", new String[] { String.valueOf(id) });
		close();
		
		return row;
	}

	public List<Event> findAll() {
		List<Event> eventList = new ArrayList<Event>();
		String selectQuery = "SELECT  * FROM " + NoMissingDB.TABLE_EVENTS;
		
		open();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Event event = new Event();
				event.setId(cursor.getLong(0));
				event.setCalendarID(cursor.getLong(1));
				event.setTitle(cursor.getString(2));
				event.setLocation(cursor.getString(3));
				event.setDescription(cursor.getString(4));
				event.setStartTime(cursor.getLong(5));
				event.setEndTime(cursor.getLong(6));
				event.setReminder(cursor.getInt(7) > 0);
				event.setCreatedAt(cursor.getLong(8));
				event.setUpdatedAt(cursor.getLong(9));
				
				eventList.add(event);
			} while (cursor.moveToNext());
		}
		cursor.close();
		close();
		
		return eventList;
	}

	public Event find(long id) {
		open();

		Cursor cursor = db
				.query(NoMissingDB.TABLE_EVENTS, new String[] {
						NoMissingDB.EVENTS_KEY_ID,
						NoMissingDB.EVENTS_KEY_CALENDAR_ID,
						NoMissingDB.EVENTS_KEY_TITLE,
						NoMissingDB.EVENTS_KEY_LOCATION,
						NoMissingDB.EVENTS_KEY_DESCRIPTION,
						NoMissingDB.EVENTS_KEY_START_TIME,
						NoMissingDB.EVENTS_KEY_END_TIME,
						NoMissingDB.EVENTS_KEY_HAS_REMINDER,
						NoMissingDB.EVENTS_KEY_CREATED_AT,
						NoMissingDB.EVENTS_KEY_UPDATED_AT },
						NoMissingDB.EVENTS_KEY_ID + "=?",
						new String[] { String.valueOf(id) }, null, null,
						null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Event event = new Event();
		event.setId(cursor.getLong(0));
		event.setCalendarID(cursor.getLong(1));
		event.setTitle(cursor.getString(2));
		event.setLocation(cursor.getString(3));
		event.setDescription(cursor.getString(4));
		event.setStartTime(cursor.getLong(5));
		event.setEndTime(cursor.getLong(6));
		event.setReminder(cursor.getInt(7) > 0);
		event.setCreatedAt(cursor.getLong(8));
		event.setUpdatedAt(cursor.getLong(9));
		
		cursor.close();
		close();
		
		return event;
	}
	
	public List<Event> find(long calendarID, long startMillis, long endMillis) {
		List<Event> eventList = new ArrayList<Event>();
		String selectQuery = 
				"SELECT  * FROM " + NoMissingDB.TABLE_EVENTS + 
				" WHERE " + NoMissingDB.EVENTS_KEY_CALENDAR_ID + " = " + calendarID +
				" AND " + NoMissingDB.EVENTS_KEY_START_TIME + " >= " + startMillis +
				" AND " + NoMissingDB.EVENTS_KEY_END_TIME + " <= " + endMillis;
		
		open();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Event event = new Event();
				event.setId(cursor.getLong(0));
				event.setCalendarID(cursor.getLong(1));
				event.setTitle(cursor.getString(2));
				event.setLocation(cursor.getString(3));
				event.setDescription(cursor.getString(4));
				event.setStartTime(cursor.getLong(5));
				event.setEndTime(cursor.getLong(6));
				event.setReminder(cursor.getInt(7) > 0);
				event.setCreatedAt(cursor.getLong(8));
				event.setUpdatedAt(cursor.getLong(9));
				
				eventList.add(event);
			} while (cursor.moveToNext());
		}
		cursor.close();
		close();
		
		return eventList;		
	}
	
	public Event find(long eventID, long calendarID, long startMillis, long endMillis) {
		Event event = new Event();
		String selectQuery = 
				"SELECT  * FROM " + NoMissingDB.TABLE_EVENTS + 
				" WHERE " + NoMissingDB.EVENTS_KEY_CALENDAR_ID + " = " + calendarID +
				" AND " + NoMissingDB.EVENTS_KEY_ID + " = " + eventID +
				" AND " + NoMissingDB.EVENTS_KEY_START_TIME + " >= " + startMillis +
				" AND " + NoMissingDB.EVENTS_KEY_END_TIME + " <= " + endMillis;
		
		open();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
//			do {
				event.setId(cursor.getLong(0));
				event.setCalendarID(cursor.getLong(1));
				event.setTitle(cursor.getString(2));
				event.setLocation(cursor.getString(3));
				event.setDescription(cursor.getString(4));
				event.setStartTime(cursor.getLong(5));
				event.setEndTime(cursor.getLong(6));
				event.setReminder(cursor.getInt(7) > 0);
				event.setCreatedAt(cursor.getLong(8));
				event.setUpdatedAt(cursor.getLong(9));
//			} while (cursor.moveToNext());
		}
		cursor.close();
		close();
		
		return event;		
	}

}
