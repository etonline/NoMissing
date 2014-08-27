package edu.ntust.cs.idsl.nomissing.dao.calendar;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Instances;
import edu.ntust.cs.idsl.nomissing.dao.IEventDao;
import edu.ntust.cs.idsl.nomissing.model.Event;

@SuppressLint("NewApi")
public class EventDao extends CalendarProviderDao implements IEventDao {

	public static final String[] INSTANCE_PROJECTION = new String[] {
	    Instances.EVENT_ID,  
	    Instances.CALENDAR_ID,
	    Instances.TITLE, 
	    Instances.EVENT_LOCATION,
	    Instances.DESCRIPTION,
	    Instances.BEGIN,    
	    Instances.END,
	    Instances.HAS_ALARM,
	    Instances.ALL_DAY,
	    Instances.RRULE,
	};
	  
	private static final int PROJECTION_EVENT_ID_INDEX = 0;
	private static final int PROJECTION_CALENDAR_ID_INDEX = 1;
	private static final int PROJECTION_TITLE_INDEX = 2;
	private static final int PROJECTION_LOCATION_INDEX = 3;
	private static final int PROJECTION_DESCRIPTION_INDEX = 4;
	private static final int PROJECTION_BEGIN_INDEX = 5;
	private static final int PROJECTION_END_INDEX = 6;
	private static final int PROJECTION_HAS_ALARM_INDEX = 7;
	private static final int PROJECTION_ALL_DAY_INDEX = 8;
	private static final int PROJECTION_RRULE_INDEX = 9;	
	
	public EventDao(Context context) {
		super(context);
	}

	@Override
	public long insert(Event event) {
		ContentValues values = new ContentValues();
		values.put(Events.CALENDAR_ID, event.getCalendarID());
		values.put(Events.TITLE, event.getTitle());
		values.put(Events.EVENT_LOCATION, event.getLocation());
		values.put(Events.DTSTART, event.getStartTime());
		values.put(Events.DTEND, event.getEndTime());
		values.put(Events.HAS_ALARM, event.hasReminder());
		values.put(Events.DESCRIPTION, event.getDescription());
		values.put(Events.EVENT_TIMEZONE, TimeZone.getDefault().getDisplayName());
		
		Uri uri = contentResolver.insert(Events.CONTENT_URI, values);
		long eventID = Long.parseLong(uri.getLastPathSegment());
		return eventID;
	}

	@Override
	public long update(Event event) {
		ContentValues values = new ContentValues();
		values.put(Events.TITLE, event.getTitle());
		values.put(Events.EVENT_LOCATION, event.getLocation());
		values.put(Events.DTSTART, event.getStartTime());
		values.put(Events.DTEND, event.getEndTime());
		values.put(Events.HAS_ALARM, event.hasReminder());
		values.put(Events.DESCRIPTION, event.getDescription());	

		Uri uri = ContentUris.withAppendedId(Events.CONTENT_URI, event.getId());
		int row = context.getContentResolver().update(uri, values, null, null);
		return row;
	}

	@Override
	public long delete(long eventID) {
		Uri uri = ContentUris.withAppendedId(Events.CONTENT_URI, eventID);
		int row = context.getContentResolver().delete(uri, null, null);
		return row;
	}

	@Override
	public List<Event> findAll() {
		List<Event> events = new ArrayList<Event>();
		  
		Cursor cursor = contentResolver.query(Events.CONTENT_URI, EVENTS_PROJECTION, null, null, null);
		while (cursor.moveToNext()) {
			Event event = new Event();    
		    event.setId(cursor.getLong(cursor.getColumnIndex(Events._ID)));
		    event.setCalendarID(cursor.getLong(cursor.getColumnIndex(Events.CALENDAR_ID)));
		    event.setTitle(cursor.getString(cursor.getColumnIndex(Events.TITLE)));
		    event.setLocation(cursor.getString(cursor.getColumnIndex(Events.EVENT_LOCATION)));
		    event.setReminder(cursor.getInt(cursor.getColumnIndex(Events.HAS_ALARM)) > 0);
		    event.setStartTime(cursor.getLong(cursor.getColumnIndex(Events.DTSTART)));
		    event.setEndTime(cursor.getLong(cursor.getColumnIndex(Events.DTEND)));
		    event.setDescription(cursor.getString(cursor.getColumnIndex(Events.DESCRIPTION)));
		    events.add(event);
		}
		cursor.close();
		
		return events;
	}
	
	@Override
	public List<Event> find(long calendarID, long startMillis, long endMillis) {
		List<Event> events = new ArrayList<Event>();
		String selection = Instances.CALENDAR_ID + " = ?";
		String[] selectionArgs = new String[] {String.valueOf(calendarID)};
		
		Uri.Builder builder = Instances.CONTENT_URI.buildUpon();
		ContentUris.appendId(builder, startMillis);
		ContentUris.appendId(builder, endMillis);

		Cursor cursor =  contentResolver.query(builder.build(), INSTANCE_PROJECTION, selection, selectionArgs, null);
		while (cursor.moveToNext()) {
			Event event = new Event();    
		    event.setId(cursor.getLong(PROJECTION_EVENT_ID_INDEX));
		    event.setCalendarID(cursor.getLong(PROJECTION_CALENDAR_ID_INDEX));
		    event.setTitle(cursor.getString(PROJECTION_TITLE_INDEX));
		    event.setLocation(cursor.getString(PROJECTION_LOCATION_INDEX));
		    event.setReminder(cursor.getInt(PROJECTION_HAS_ALARM_INDEX) > 0);
		    event.setStartTime(cursor.getLong(PROJECTION_BEGIN_INDEX));
		    event.setEndTime(cursor.getLong(PROJECTION_END_INDEX));
		    event.setDescription(cursor.getString(PROJECTION_DESCRIPTION_INDEX));
		    events.add(event);
		}
		cursor.close();
		
		return events;
	}

	@Override
	public Event find(long eventID, long calendarID, long startMillis, long endMillis) {
		Event event = new Event();
		String selection = Instances.EVENT_ID + " = ?";
		String[] selectionArgs = new String[] {String.valueOf(eventID)};
		
		Uri.Builder builder = Instances.CONTENT_URI.buildUpon();
		ContentUris.appendId(builder, startMillis);
		ContentUris.appendId(builder, endMillis);

		Cursor cursor =  contentResolver.query(builder.build(), INSTANCE_PROJECTION, selection, selectionArgs, null);
		if (cursor.moveToFirst()) {
		    event.setId(cursor.getLong(PROJECTION_EVENT_ID_INDEX));
		    event.setCalendarID(cursor.getLong(PROJECTION_CALENDAR_ID_INDEX));
		    event.setTitle(cursor.getString(PROJECTION_TITLE_INDEX));
		    event.setLocation(cursor.getString(PROJECTION_LOCATION_INDEX));
		    event.setReminder(cursor.getInt(PROJECTION_HAS_ALARM_INDEX) > 0);
		    event.setStartTime(cursor.getLong(PROJECTION_BEGIN_INDEX));
		    event.setEndTime(cursor.getLong(PROJECTION_END_INDEX));
		    event.setDescription(cursor.getString(PROJECTION_DESCRIPTION_INDEX));
		}
		cursor.close();
		
		return event;
	}
	
	@Override
	public Event find(long eventID) {
		Event event = new Event();
		String selection = Events._ID + " = ?";
		String[] selectionArgs = new String[] { String.valueOf(eventID) };
		
		Cursor cursor =  contentResolver.query(Events.CONTENT_URI, EVENTS_PROJECTION, selection, selectionArgs, null);
		if (cursor.moveToFirst()) {   
		    event.setId(cursor.getLong(PROJECTION_EVENT_ID_INDEX));
		    event.setCalendarID(cursor.getLong(PROJECTION_CALENDAR_ID_INDEX));
		    event.setTitle(cursor.getString(PROJECTION_TITLE_INDEX));
		    event.setLocation(cursor.getString(PROJECTION_LOCATION_INDEX));
		    event.setReminder(cursor.getInt(PROJECTION_HAS_ALARM_INDEX) > 0);
		    event.setStartTime(cursor.getLong(PROJECTION_BEGIN_INDEX));
		    event.setEndTime(cursor.getLong(PROJECTION_END_INDEX));
		    event.setDescription(cursor.getString(PROJECTION_DESCRIPTION_INDEX));
		}
		cursor.close();
		
		return event;
	}	
	
}
