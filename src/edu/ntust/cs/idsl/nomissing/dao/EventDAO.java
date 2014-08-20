package edu.ntust.cs.idsl.nomissing.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Instances;
import edu.ntust.cs.idsl.nomissing.model.Event;

@SuppressLint({ "SimpleDateFormat", "NewApi" })
public class EventDAO implements ICalendarDAO<Event> {

	private static EventDAO instance;
	private Context context;
	
	public static final String[] INSTANCE_PROJECTION = new String[] {
	    Instances.EVENT_ID,  
	    Instances.CALENDAR_ID,
	    Instances.TITLE, 
	    Instances.EVENT_LOCATION,
	    Instances.DESCRIPTION,
	    Instances.BEGIN,    
	    Instances.END,
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
	private static final int PROJECTION_ALL_DAY_INDEX = 7;
	private static final int PROJECTION_RRULE_INDEX = 8;

	private EventDAO(Context context) {
		this.context = context;
	}
	
	public static synchronized EventDAO getInstance(Context context) {
		if (instance == null) {
			instance = new EventDAO(context);
		}
		return instance;
	}
	
	@Override
	public long insert(Event entity) {		
		ContentResolver cr = context.getContentResolver();
		ContentValues values = new ContentValues();
		values.put(Events.CALENDAR_ID, 1);
		values.put(Events.TITLE, entity.getTitle());
		values.put(Events.EVENT_LOCATION, entity.getLocation());
		values.put(Events.DESCRIPTION, entity.getDescription());
		values.put(Events.DTSTART, entity.getStart());
		values.put(Events.DTEND, entity.getEnd());
		values.put(Events.ALL_DAY, entity.isAllDay());
		values.put(Events.EVENT_TIMEZONE, TimeZone.getDefault().getDisplayName());
		values.put(Events.RRULE, entity.getRrule());
		Uri uri = cr.insert(Events.CONTENT_URI, values);

		long eventID = Long.parseLong(uri.getLastPathSegment());
		return eventID;
	}

	@Override
	public long update(Event entity) {
		ContentResolver cr = context.getContentResolver();
		ContentValues values = new ContentValues();
		Uri updateUri = null;
		values.put(Events.DTSTART, entity.getStart());
		values.put(Events.DTEND, entity.getEnd());
		values.put(Events.ALL_DAY, entity.isAllDay());
		values.put(Events.TITLE, entity.getTitle());
		values.put(Events.EVENT_LOCATION, entity.getLocation());
		values.put(Events.DESCRIPTION, entity.getDescription());	
		values.put(Events.RRULE, entity.getRrule());

		String[] selArgs = new String[]{Long.toString(entity.getEventID())};

		updateUri = ContentUris.withAppendedId(Events.CONTENT_URI, entity.getEventID());
		int rows = context.getContentResolver().update(updateUri, values, null, null);
		return rows;
	}

	@Override
	public long delete(long id) {
		ContentResolver cr = context.getContentResolver();
		ContentValues values = new ContentValues();
		Uri deleteUri = null;
		deleteUri = ContentUris.withAppendedId(Events.CONTENT_URI, id);
		int rows = context.getContentResolver().delete(deleteUri, null, null);
		return rows;
	}

	@Override
	public List<Event> find(long calendarID, long startMillis, long endMillis) {
		List<Event> events = new ArrayList<Event>();
		  
		Cursor cur = null;
		ContentResolver cr = context.getContentResolver();	
		
		String selection = Instances.CALENDAR_ID + " = ?";
		String[] selectionArgs = new String[] {String.valueOf(calendarID)};
		
		Uri.Builder builder = Instances.CONTENT_URI.buildUpon();
		ContentUris.appendId(builder, startMillis);
		ContentUris.appendId(builder, endMillis);

		cur =  cr.query(builder.build(), INSTANCE_PROJECTION, selection, selectionArgs, null);
//		cur =  cr.query(builder.build(), INSTANCE_PROJECTION, null, null, null);
		while (cur.moveToNext()) {
			Event event = new Event();    
		    event.setEventID(cur.getLong(PROJECTION_EVENT_ID_INDEX));
		    event.setCalendarID(cur.getLong(PROJECTION_CALENDAR_ID_INDEX));
		    event.setTitle(cur.getString(PROJECTION_TITLE_INDEX));
		    event.setLocation(cur.getString(PROJECTION_LOCATION_INDEX));
		    event.setDescription(cur.getString(PROJECTION_DESCRIPTION_INDEX));
		    event.setStart(cur.getLong(PROJECTION_BEGIN_INDEX));
		    event.setEnd(cur.getLong(PROJECTION_END_INDEX));
		    event.setAllDay(cur.getInt(PROJECTION_ALL_DAY_INDEX) > 0);
		    event.setRrule(cur.getString(PROJECTION_RRULE_INDEX));
		    
		    events.add(event);
		}
		cur.close();
		return events;
	}

	@Override
	public Event find(long calendarID, long id, long startMillis, long endMillis) {
		Event event = new Event();
		
		Cursor cur = null;
		ContentResolver cr = context.getContentResolver();

		String selection = Instances.EVENT_ID + " = ?";
		String[] selectionArgs = new String[] {String.valueOf(id)};

		Uri.Builder builder = Instances.CONTENT_URI.buildUpon();
		ContentUris.appendId(builder, startMillis);
		ContentUris.appendId(builder, endMillis);

		cur =  cr.query(builder.build(), INSTANCE_PROJECTION, selection, selectionArgs, null);  
		while (cur.moveToNext()) {  
		    event.setEventID(cur.getLong(PROJECTION_EVENT_ID_INDEX));
		    event.setCalendarID(cur.getLong(PROJECTION_CALENDAR_ID_INDEX));
		    event.setTitle(cur.getString(PROJECTION_TITLE_INDEX));
		    event.setLocation(cur.getString(PROJECTION_LOCATION_INDEX));
		    event.setDescription(cur.getString(PROJECTION_DESCRIPTION_INDEX));
		    event.setStart(cur.getLong(PROJECTION_BEGIN_INDEX));
		    event.setEnd(cur.getLong(PROJECTION_END_INDEX));
		    event.setAllDay(cur.getInt(PROJECTION_ALL_DAY_INDEX) > 0);
		    event.setRrule(cur.getString(PROJECTION_RRULE_INDEX));
		}
		cur.close();
		
		return event;
	}		

}
