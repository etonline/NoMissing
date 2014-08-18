package edu.ntust.cs.idsl.nomissing.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import edu.ntust.cs.idsl.nomissing.model.City;

public class NoMissingDB extends SQLiteOpenHelper {
		
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "NoMissing.db";

	// Table names
	public static final String TABLE_TASKS = "tasks";
	public static final String TABLE_CHIMES = "chimes";
	public static final String TABLE_WEATHER = "weather";

	// Column names
	public static final String EVENTS_KEY_ID = "_id";
	public static final String EVENTS_KEY_CALENDAR_ID = "calendar_id";
	public static final String EVENTS_KEY_TITLE = "title";
	public static final String EVENTS_KEY_LOCATION = "location";
	public static final String EVENTS_KEY_DESCRIPTION = "description";
	public static final String EVENTS_KEY_START = "start";
	public static final String EVENTS_KEY_END = "end";
	public static final String EVENTS_KEY_ALL_DAY = "all_day";
	public static final String EVENTS_KEY_RRULE = "rrule";
	public static final String EVENTS_KEY_REMINDER = "reminder";
	
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
		createTasksTable(db);
		createChimesTable(db);
		createWeatherTable(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHIMES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEATHER);
		createTasksTable(db);
		createChimesTable(db);
		createWeatherTable(db);		
	}

	/*
	 * Create task table
	 */
	private void createTasksTable(SQLiteDatabase db) {
		
	}		
	
	/*
	 * Create chimes table
	 */	
	private void createChimesTable(SQLiteDatabase db) {
		String CREATE_CHIMES_TABLE = "CREATE TABLE " + TABLE_CHIMES + "(" + 
				CHIMES_KEY_ID + " INTEGER PRIMARY KEY," + 
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
