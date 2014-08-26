package edu.ntust.cs.idsl.nomissing.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import edu.ntust.cs.idsl.nomissing.constant.City;

public class NoMissingDB extends SQLiteOpenHelper {
		
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "NoMissing.db";

	// Table names
	public static final String TABLE_EVENTS = "events";
	public static final String TABLE_REMINDERS = "reminders";
	public static final String TABLE_CHIMES = "chimes";
	public static final String TABLE_WEATHER = "weather";

	// Column names
	public static final String EVENTS_KEY_ID = "_id";
	public static final String EVENTS_KEY_CALENDAR_ID = "calendar_id";
	public static final String EVENTS_KEY_TITLE = "title";
	public static final String EVENTS_KEY_LOCATION = "location";
	public static final String EVENTS_KEY_DESCRIPTION = "description";
	public static final String EVENTS_KEY_START_TIME = "start_time";
	public static final String EVENTS_KEY_END_TIME = "end_time";
	public static final String EVENTS_KEY_HAS_REMINDER = "has_reminder";
	public static final String EVENTS_KEY_CREATED_AT = "created_at";
	public static final String EVENTS_KEY_UPDATED_AT = "updated_at";
	
	public static final String REMINDERS_KEY_ID = "_id";
	public static final String REMINDERS_KEY_CALENDAR_ID = "calendar_id";
	public static final String REMINDERS_KEY_EVENT_ID = "event_id";
	public static final String REMINDERS_KEY_REMINDER_TIME = "reminder_time";
	public static final String REMINDERS_KEY_AUDIO = "audio";
	public static final String REMINDERS_KEY_CREATED_AT = "created_at";
	public static final String REMINDERS_KEY_UPDATED_AT = "updated_at";
	
	public static final String CHIMES_KEY_ID = "_id";
	public static final String CHIMES_KEY_HOUR = "hour";
	public static final String CHIMES_KEY_MINUTE = "minute";
	public static final String CHIMES_KEY_IS_ENABLED = "is_enabled";
	public static final String CHIMES_KEY_IS_REPEATING = "is_repeating";
	public static final String CHIMES_KEY_IS_TRIGGERED = "is_triggered";
	public static final String CHIMES_KEY_AUDIO = "audio";
	public static final String CHIMES_KEY_CREATED_AT = "created_at";
	public static final String CHIMES_KEY_UPDATED_AT = "updated_at";
	
	public static final String WEATHER_KEY_CITYID = "cityid";
	public static final String WEATHER_KEY_NAME = "name";
	public static final String WEATHER_KEY_STNO = "stno";
	public static final String WEATHER_KEY_TIME = "time";
	public static final String WEATHER_KEY_MEMO = "memo";
	public static final String WEATHER_KEY_AUDIO = "audio";
	public static final String WEATHER_KEY_CREATED_AT = "created_at";
	public static final String WEATHER_KEY_UPDATED_AT = "updated_at";
	
	public NoMissingDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createEventsTable(db);
		createRemindersTable(db);
		createChimesTable(db);
		createWeatherTable(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDERS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHIMES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEATHER);
		
		createEventsTable(db);
		createRemindersTable(db);
		createChimesTable(db);
		createWeatherTable(db);		
	}

	/*
	 * Create events table
	 */
	private void createEventsTable(SQLiteDatabase db) {
		String CREATE_EVENTS_TABLE = "CREATE TABLE " + TABLE_EVENTS + "(" + 
				EVENTS_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + 
				EVENTS_KEY_CALENDAR_ID + " INTEGER," + 
				EVENTS_KEY_TITLE + " TEXT," + 
				EVENTS_KEY_LOCATION + " TEXT," + 
				EVENTS_KEY_DESCRIPTION + " TEXT," + 
				EVENTS_KEY_START_TIME + " DATETIME," +
				EVENTS_KEY_END_TIME + " DATETIME," +
				EVENTS_KEY_HAS_REMINDER + " INTEGER," +
				EVENTS_KEY_CREATED_AT + " DATETIME," + 
				EVENTS_KEY_UPDATED_AT + " DATETIME" + ")";
		db.execSQL(CREATE_EVENTS_TABLE);
	}		
	
	/*
	 * Create reminders table
	 */
	private void createRemindersTable(SQLiteDatabase db) {
		String CREATE_REMINDERS_TABLE = "CREATE TABLE " + TABLE_REMINDERS + "(" + 
				REMINDERS_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + 
				REMINDERS_KEY_CALENDAR_ID + " INTEGER," + 
				REMINDERS_KEY_EVENT_ID + " INTEGER," + 
				REMINDERS_KEY_REMINDER_TIME + " DATETIME," + 
				REMINDERS_KEY_AUDIO + " TEXT," +
				REMINDERS_KEY_CREATED_AT + " DATETIME," + 
				REMINDERS_KEY_UPDATED_AT + " DATETIME" + ")";
		db.execSQL(CREATE_REMINDERS_TABLE);
	}	
	
	/*
	 * Create chimes table
	 */	
	private void createChimesTable(SQLiteDatabase db) {
		String CREATE_CHIMES_TABLE = "CREATE TABLE " + TABLE_CHIMES + "(" + 
				CHIMES_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + 
				CHIMES_KEY_HOUR + " INTEGER," + 
				CHIMES_KEY_MINUTE + " INTEGER," + 
				CHIMES_KEY_IS_ENABLED + " INTEGER," + 
				CHIMES_KEY_IS_REPEATING + " INTEGER," + 
				CHIMES_KEY_IS_TRIGGERED + " INTEGER," +
				CHIMES_KEY_AUDIO + " TEXT," +
				CHIMES_KEY_CREATED_AT + " DATETIME," + 
				CHIMES_KEY_UPDATED_AT + " DATETIME" + ")";
		db.execSQL(CREATE_CHIMES_TABLE);
	}
	
	/*
	 * Create weather table
	 */
	private void createWeatherTable(SQLiteDatabase db) {
		String CREATE_WEATHER_TABLE = "CREATE TABLE " + TABLE_WEATHER + "(" + 
				WEATHER_KEY_CITYID + " INTEGER PRIMARY KEY," + 
				WEATHER_KEY_NAME + " TEXT," + 
				WEATHER_KEY_STNO + " TEXT," + 
				WEATHER_KEY_TIME + " TEXT," + 
				WEATHER_KEY_MEMO + " TEXT," + 
				WEATHER_KEY_AUDIO + " TEXT," + 
				WEATHER_KEY_CREATED_AT + " TEXT," + 
				WEATHER_KEY_UPDATED_AT + " TEXT" + ")";
		db.execSQL(CREATE_WEATHER_TABLE);	
		
		// insert initial data
		for (City city : City.values()) {	
			ContentValues values = new ContentValues();
			values.put(WEATHER_KEY_CITYID, city.getCityID()); 
			values.put(WEATHER_KEY_NAME, city.getCityName());	
			db.insert(TABLE_WEATHER, null, values);
		}	
	}

}
