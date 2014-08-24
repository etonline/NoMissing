package edu.ntust.cs.idsl.nomissing.dao.calendar;

import java.util.TimeZone;

import edu.ntust.cs.idsl.nomissing.dao.DaoFactory;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.provider.CalendarContract.Attendees;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Reminders;

@SuppressLint("NewApi")
public class CalendarProviderDao {

	protected Context context;
	protected ContentResolver contentResolver; 
	
    protected Uri calendarsURI = Calendars.CONTENT_URI;  
    protected Uri eventsURI = Events.CONTENT_URI;  
    protected Uri remindersURI = Reminders.CONTENT_URI;  
    protected Uri attendeesURI = Attendees.CONTENT_URI;  
	
    /** Calendars table columns */  
    public static final String[] CALENDARS_PROJECTION = new String[] {  
        Calendars._ID,                           // 0  
        Calendars.ACCOUNT_NAME,                  // 1  
        Calendars.CALENDAR_DISPLAY_NAME,         // 2  
        Calendars.OWNER_ACCOUNT,                  // 3  
        Calendars.CALENDAR_ACCESS_LEVEL
    };  
    public static final int CALENDARS_ID_INDEX = 0;
    public static final int CALENDARS_ACCOUNT_NAME_INDEX = 1;
    public static final int CALENDARS_DISPLAY_NAME_INDEX = 2;
    public static final int CALENDARS_OWNER_ACCOUNT_INDEX = 3;
    public static final int CALENDARS_ACCESS_LEVEL_INDEX = 4;
      
    /** Events table columns */  
    public static final String[] EVENTS_PROJECTION = new String[] {  
        Events._ID,  
        Events.CALENDAR_ID,  
        Events.TITLE,  
        Events.DESCRIPTION,  
        Events.EVENT_LOCATION,  
        Events.DTSTART,  
        Events.DTEND,  
        Events.EVENT_TIMEZONE,            
        Events.HAS_ALARM,  
        Events.ALL_DAY,  
        Events.AVAILABILITY,  
        Events.ACCESS_LEVEL,  
        Events.STATUS,  
        Events.RRULE
    };  
    public static final int EVENTS_ID_INDEX = 0;
    public static final int EVENTS_CALENDAR_ID_INDEX = 1;
    public static final int EVENTS_TITLE_INDEX = 2;
    public static final int EVENTS_DESCRIPTION_INDEX = 3;
    public static final int EVENTS_LOCATION_INDEX = 4;
    public static final int EVENTS_DTSTART_INDEX = 5;
    public static final int EVENTS_DTEND_INDEX = 6;
    public static final int EVENTS_TIMEZONE_INDEX = 7;
    public static final int EVENTS_HAS_ALARM_INDEX = 8;
    public static final int EVENTS_ALL_DAY_INDEX = 9;
    public static final int EVENTS_AVAILABILITY_INDEX = 10;
    public static final int EVENTS_ACCESS_LEVEL_INDEX = 11;
    public static final int EVENTS_STATUS_INDEX = 12;
    public static final int EVENTS_RRULE_INDEX = 13;
    
    /** Reminders table columns */  
    public static final String[] REMINDERS_PROJECTION = new String[] {  
        Reminders._ID,  
        Reminders.EVENT_ID,  
        Reminders.MINUTES,  
        Reminders.METHOD,  
    };  
    public static final int REMINDERS_ID_INDEX = 0;
    public static final int REMINDERS_EVENT_ID_INDEX = 1;
    public static final int REMINDERS_MINUTES_INDEX = 2;
    public static final int REMINDERS_METHOD_INDEX = 3;
    
    /** Reminders table columns */  
    public static final String[] ATTENDEES_PROJECTION = new String[] {  
        Attendees._ID,  
        Attendees.ATTENDEE_NAME,  
        Attendees.ATTENDEE_EMAIL,  
        Attendees.ATTENDEE_STATUS  
    };  
    
    /** 
     * Constructor
     * @param resolver 
     */  
    protected CalendarProviderDao(Context context) {  
        this.context = context;
        this.contentResolver = context.getContentResolver();
    }  
    
    /**
     * Get Time Zones
     * @return String[] 
     */
    public static String[] getTimeZones(){  
        return TimeZone.getAvailableIDs();  
    }  
    
    
    
}
