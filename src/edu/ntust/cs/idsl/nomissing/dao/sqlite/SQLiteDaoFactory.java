package edu.ntust.cs.idsl.nomissing.dao.sqlite;

import edu.ntust.cs.idsl.nomissing.dao.DaoFactory;
import android.content.Context;

public class SQLiteDaoFactory extends DaoFactory {
	
	public static WeatherDao createWeatherDao(Context context) {
		return new WeatherDao(context);
	}
	
	public static ChimeDao createChimeDao(Context context) {
		return new ChimeDao(context);
	}		
	
	public static EventDao createEventDao(Context context) {
		return new EventDao(context);
	}	
	
	public static ReminderDao createReminderDao(Context context) {
		return new ReminderDao(context);
	}	
	
}
