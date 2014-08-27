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

	private static final String[] EVENTS_COLUMNS = new String[] {
		NoMissingDB.EVENTS_KEY_ID,
		NoMissingDB.EVENTS_KEY_CALENDAR_ID,
		NoMissingDB.EVENTS_KEY_TITLE,
		NoMissingDB.EVENTS_KEY_LOCATION,
		NoMissingDB.EVENTS_KEY_DESCRIPTION,
		NoMissingDB.EVENTS_KEY_START_TIME,
		NoMissingDB.EVENTS_KEY_END_TIME,
		NoMissingDB.EVENTS_KEY_HAS_REMINDER,
		NoMissingDB.EVENTS_KEY_CREATED_AT,
		NoMissingDB.EVENTS_KEY_UPDATED_AT
	};
	
	public EventDao(Context context) {
		super(context);
	}

	@Override
	public long insert(Event event) {
		ContentValues values = new ContentValues();
		values.put(NoMissingDB.EVENTS_KEY_CALENDAR_ID, event.getCalendarID());
		values.put(NoMissingDB.EVENTS_KEY_TITLE, event.getTitle());
		values.put(NoMissingDB.EVENTS_KEY_LOCATION, event.getLocation());
		values.put(NoMissingDB.EVENTS_KEY_DESCRIPTION, event.getDescription());
		values.put(NoMissingDB.EVENTS_KEY_START_TIME, event.getStartTime());
		values.put(NoMissingDB.EVENTS_KEY_END_TIME, event.getEndTime());
		values.put(NoMissingDB.EVENTS_KEY_HAS_REMINDER, event.hasReminder());
		values.put(NoMissingDB.EVENTS_KEY_CREATED_AT, event.getCreatedAt());
		values.put(NoMissingDB.EVENTS_KEY_UPDATED_AT, event.getUpdatedAt());

		open();
		long row = db.insert(NoMissingDB.TABLE_EVENTS, null, values);
		close();
		
		return row;
	}

	@Override
	public long update(Event event) {
		String whereClause = NoMissingDB.EVENTS_KEY_ID + " = ?";
		String[] whereArgs = new String[] { String.valueOf(event.getId()) };

		ContentValues values = new ContentValues();
		values.put(NoMissingDB.EVENTS_KEY_CALENDAR_ID, event.getCalendarID());
		values.put(NoMissingDB.EVENTS_KEY_TITLE, event.getTitle());
		values.put(NoMissingDB.EVENTS_KEY_LOCATION, event.getLocation());
		values.put(NoMissingDB.EVENTS_KEY_DESCRIPTION, event.getDescription());
		values.put(NoMissingDB.EVENTS_KEY_START_TIME, event.getStartTime());
		values.put(NoMissingDB.EVENTS_KEY_END_TIME, event.getEndTime());
		values.put(NoMissingDB.EVENTS_KEY_HAS_REMINDER, event.hasReminder());
		values.put(NoMissingDB.EVENTS_KEY_CREATED_AT, event.getCreatedAt());
		values.put(NoMissingDB.EVENTS_KEY_UPDATED_AT, event.getUpdatedAt());

		open();
		int row =  db.update(NoMissingDB.TABLE_EVENTS, values, whereClause, whereArgs);
		close();
		
		return row;
	}

	@Override
	public long delete(long id) {
		String whereClause = NoMissingDB.EVENTS_KEY_ID + " = ?";
		String[] whereArgs = new String[] { String.valueOf(id) };
		
		open();
		int row = db.delete(NoMissingDB.TABLE_EVENTS, whereClause, whereArgs);
		close();
		
		return row;
	}

	public List<Event> findAll() {
		List<Event> events = new ArrayList<Event>();
		
		open();
		Cursor cursor = db.query(NoMissingDB.TABLE_EVENTS, EVENTS_COLUMNS, null, null, null, null, null);
		while (cursor.moveToNext()) {
			Event event = new Event();
			event.setId(cursor.getLong(cursor.getColumnIndex(NoMissingDB.EVENTS_KEY_ID)));
			event.setCalendarID(cursor.getLong(cursor.getColumnIndex(NoMissingDB.EVENTS_KEY_CALENDAR_ID)));
			event.setTitle(cursor.getString(cursor.getColumnIndex(NoMissingDB.EVENTS_KEY_TITLE)));
			event.setLocation(cursor.getString(cursor.getColumnIndex(NoMissingDB.EVENTS_KEY_LOCATION)));
			event.setDescription(cursor.getString(cursor.getColumnIndex(NoMissingDB.EVENTS_KEY_DESCRIPTION)));
			event.setStartTime(cursor.getLong(cursor.getColumnIndex(NoMissingDB.EVENTS_KEY_START_TIME)));
			event.setEndTime(cursor.getLong(cursor.getColumnIndex(NoMissingDB.EVENTS_KEY_END_TIME)));
			event.setReminder(cursor.getInt(cursor.getColumnIndex(NoMissingDB.EVENTS_KEY_HAS_REMINDER)) > 0);
			event.setCreatedAt(cursor.getLong(cursor.getColumnIndex(NoMissingDB.EVENTS_KEY_CREATED_AT)));
			event.setUpdatedAt(cursor.getLong(cursor.getColumnIndex(NoMissingDB.EVENTS_KEY_UPDATED_AT)));
			events.add(event);
		}
		cursor.close();
		close();
		
		return events;
	}
	
	public List<Event> find(long calendarID, long startMillis, long endMillis) {
		List<Event> eventList = new ArrayList<Event>();
		
		String selection = 
				"((" + NoMissingDB.EVENTS_KEY_CALENDAR_ID + " = ?) " + 
				"AND (" + NoMissingDB.EVENTS_KEY_START_TIME + " >= ?) " + 
				"AND ("+ NoMissingDB.EVENTS_KEY_END_TIME + " <= ?))";
		
		String[] selectionArgs = new String[] {
				String.valueOf(calendarID), 
				String.valueOf(startMillis), 
				String.valueOf(endMillis)};
		
		open();
		Cursor cursor = db.query(NoMissingDB.TABLE_EVENTS, EVENTS_COLUMNS, selection, selectionArgs, null, null, null);
		while (cursor.moveToNext()) {
			Event event = new Event();
			event.setId(cursor.getLong(cursor.getColumnIndex(NoMissingDB.EVENTS_KEY_ID)));
			event.setCalendarID(cursor.getLong(cursor.getColumnIndex(NoMissingDB.EVENTS_KEY_CALENDAR_ID)));
			event.setTitle(cursor.getString(cursor.getColumnIndex(NoMissingDB.EVENTS_KEY_TITLE)));
			event.setLocation(cursor.getString(cursor.getColumnIndex(NoMissingDB.EVENTS_KEY_LOCATION)));
			event.setDescription(cursor.getString(cursor.getColumnIndex(NoMissingDB.EVENTS_KEY_DESCRIPTION)));
			event.setStartTime(cursor.getLong(cursor.getColumnIndex(NoMissingDB.EVENTS_KEY_START_TIME)));
			event.setEndTime(cursor.getLong(cursor.getColumnIndex(NoMissingDB.EVENTS_KEY_END_TIME)));
			event.setReminder(cursor.getInt(cursor.getColumnIndex(NoMissingDB.EVENTS_KEY_HAS_REMINDER)) > 0);
			event.setCreatedAt(cursor.getLong(cursor.getColumnIndex(NoMissingDB.EVENTS_KEY_CREATED_AT)));
			event.setUpdatedAt(cursor.getLong(cursor.getColumnIndex(NoMissingDB.EVENTS_KEY_UPDATED_AT)));
			eventList.add(event);
		}
		cursor.close();
		close();
		
		return eventList;		
	}
	
	@Override
	public Event find(long eventID, long calendarID, long startMillis, long endMillis) {
		Event event = new Event();
				
		String selection = 
				"((" + NoMissingDB.EVENTS_KEY_ID + " = ?) " + 
				"AND (" + NoMissingDB.EVENTS_KEY_CALENDAR_ID + " >= ?) " + 
				"AND (" + NoMissingDB.EVENTS_KEY_START_TIME + " >= ?) " + 
				"AND ("+ NoMissingDB.EVENTS_KEY_END_TIME + " <= ?))";
		
		String[] selectionArgs = new String[] {
				String.valueOf(eventID), 
				String.valueOf(calendarID), 
				String.valueOf(startMillis), 
				String.valueOf(endMillis)};
		
		open();
		Cursor cursor = db.query(NoMissingDB.TABLE_EVENTS, EVENTS_COLUMNS, selection, selectionArgs, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			event.setId(cursor.getLong(cursor.getColumnIndex(NoMissingDB.EVENTS_KEY_ID)));
			event.setCalendarID(cursor.getLong(cursor.getColumnIndex(NoMissingDB.EVENTS_KEY_CALENDAR_ID)));
			event.setTitle(cursor.getString(cursor.getColumnIndex(NoMissingDB.EVENTS_KEY_TITLE)));
			event.setLocation(cursor.getString(cursor.getColumnIndex(NoMissingDB.EVENTS_KEY_LOCATION)));
			event.setDescription(cursor.getString(cursor.getColumnIndex(NoMissingDB.EVENTS_KEY_DESCRIPTION)));
			event.setStartTime(cursor.getLong(cursor.getColumnIndex(NoMissingDB.EVENTS_KEY_START_TIME)));
			event.setEndTime(cursor.getLong(cursor.getColumnIndex(NoMissingDB.EVENTS_KEY_END_TIME)));
			event.setReminder(cursor.getInt(cursor.getColumnIndex(NoMissingDB.EVENTS_KEY_HAS_REMINDER)) > 0);
			event.setCreatedAt(cursor.getLong(cursor.getColumnIndex(NoMissingDB.EVENTS_KEY_CREATED_AT)));
			event.setUpdatedAt(cursor.getLong(cursor.getColumnIndex(NoMissingDB.EVENTS_KEY_UPDATED_AT)));
		}
		cursor.close();
		close();
		
		return event;		
	}
	
	public Event find(long id) {
		Event event = new Event();
		String selection = NoMissingDB.EVENTS_KEY_ID + "=?";
		String[] selectionArgs = new String[] { String.valueOf(id) };
		
		open();
		Cursor cursor = db.query(NoMissingDB.TABLE_EVENTS, EVENTS_COLUMNS, selection, selectionArgs, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			event.setId(cursor.getLong(cursor.getColumnIndex(NoMissingDB.EVENTS_KEY_ID)));
			event.setCalendarID(cursor.getLong(cursor.getColumnIndex(NoMissingDB.EVENTS_KEY_CALENDAR_ID)));
			event.setTitle(cursor.getString(cursor.getColumnIndex(NoMissingDB.EVENTS_KEY_TITLE)));
			event.setLocation(cursor.getString(cursor.getColumnIndex(NoMissingDB.EVENTS_KEY_LOCATION)));
			event.setDescription(cursor.getString(cursor.getColumnIndex(NoMissingDB.EVENTS_KEY_DESCRIPTION)));
			event.setStartTime(cursor.getLong(cursor.getColumnIndex(NoMissingDB.EVENTS_KEY_START_TIME)));
			event.setEndTime(cursor.getLong(cursor.getColumnIndex(NoMissingDB.EVENTS_KEY_END_TIME)));
			event.setReminder(cursor.getInt(cursor.getColumnIndex(NoMissingDB.EVENTS_KEY_HAS_REMINDER)) > 0);
			event.setCreatedAt(cursor.getLong(cursor.getColumnIndex(NoMissingDB.EVENTS_KEY_CREATED_AT)));
			event.setUpdatedAt(cursor.getLong(cursor.getColumnIndex(NoMissingDB.EVENTS_KEY_UPDATED_AT)));
		}
		cursor.close();
		close();
		
		return event;
	}

}
