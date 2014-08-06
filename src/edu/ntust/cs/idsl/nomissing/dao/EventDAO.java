package edu.ntust.cs.idsl.nomissing.dao;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Instances;
import android.util.Log;
import edu.ntust.cs.idsl.nomissing.model.Event;

@SuppressLint({ "SimpleDateFormat", "NewApi" })
public class EventDAO implements ICalendarDAO<Event> {

	private static EventDAO instance;
	private Context context;
	
	public static final String[] INSTANCE_PROJECTION = new String[] {
	    Instances.EVENT_ID,  
	    Instances.TITLE, 
	    Instances.EVENT_LOCATION,
	    Instances.BEGIN,    
	    Instances.END
	  };
	  
	private static final int PROJECTION_ID_INDEX = 0;
	private static final int PROJECTION_TITLE_INDEX = 1;
	private static final int PROJECTION_LOCATION_INDEX = 2;
	private static final int PROJECTION_BEGIN_INDEX = 3;
	private static final int PROJECTION_END_INDEX = 4;

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
		values.put(Events.DTSTART, entity.getStart());
		values.put(Events.DTEND, entity.getEnd());
		values.put(Events.TITLE, entity.getTitle());
		values.put(Events.DESCRIPTION, entity.getDescription());
		values.put(Events.CALENDAR_ID, entity.getCalendarID());
		values.put(Events.EVENT_TIMEZONE, entity.getTimezone());
		Uri uri = cr.insert(Events.CONTENT_URI, values);

		// get the event ID that is the last element in the Uri
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
		values.put(Events.TITLE, entity.getTitle());
//		values.put(Events.DESCRIPTION, entity.getDescription());
//		values.put(Events.CALENDAR_ID, entity.getCalendarID());
//		values.put(Events.EVENT_TIMEZONE, entity.getTimezone());		
		String[] selArgs = new String[]{Long.toString(entity.getEventID())};

		updateUri = ContentUris.withAppendedId(Events.CONTENT_URI, entity.getEventID());
		int rows = context.getContentResolver().update(updateUri, values, null, null);
		return rows;
	}

	@Override
	public long delete(long id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Event> find(long startMillis, long endMillis) {
		List<Event> events = new ArrayList<Event>();
		  
		Cursor cur = null;
		ContentResolver cr = context.getContentResolver();

		Uri.Builder builder = Instances.CONTENT_URI.buildUpon();
		ContentUris.appendId(builder, startMillis);
		ContentUris.appendId(builder, endMillis);

		// Submit the query
		cur =  cr.query(builder.build(), INSTANCE_PROJECTION, null, null, null);
		while (cur.moveToNext()) {
			Event event = new Event();
			
		    String title = null;
		    long eventID = 0;
		    long beginVal = 0; 
		    String location = null;
		    long endVal = 0; 
		    
		    // Get the field values
		    eventID = cur.getLong(PROJECTION_ID_INDEX);
		    beginVal = cur.getLong(PROJECTION_BEGIN_INDEX);
		    title = cur.getString(PROJECTION_TITLE_INDEX);
		    location = cur.getString(PROJECTION_LOCATION_INDEX);
		    endVal = cur.getLong(PROJECTION_END_INDEX);
		    
		    event.setEventID(eventID);
		    event.setTitle(title);
		    event.setStart(beginVal);
		    event.setLocation(location);
		    event.setEnd(endVal);
		    events.add(event);
		}
		cur.close();
		return events;
	}

	@Override
	public Event find(long id, long startMillis, long endMillis) {
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
		    String title = null;
		    long eventID = 0;
		    long beginVal = 0; 
		    String location = null;
		    long endVal = 0; 
		    
		    // Get the field values
		    eventID = cur.getLong(PROJECTION_ID_INDEX);
		    beginVal = cur.getLong(PROJECTION_BEGIN_INDEX);
		    title = cur.getString(PROJECTION_TITLE_INDEX);
		    location = cur.getString(PROJECTION_LOCATION_INDEX);
		    endVal = cur.getLong(PROJECTION_END_INDEX);
		    
		    event.setEventID(eventID);
		    event.setTitle(title);
		    event.setStart(beginVal);
		    event.setLocation(location);
		    event.setEnd(endVal);
		}
		cur.close();
		
		return event;
	}		
		

	
}
