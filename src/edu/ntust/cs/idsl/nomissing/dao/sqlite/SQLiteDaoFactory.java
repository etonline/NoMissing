package edu.ntust.cs.idsl.nomissing.dao.sqlite;

import edu.ntust.cs.idsl.nomissing.dao.DaoFactory;
import edu.ntust.cs.idsl.nomissing.dao.IEventDao;
import edu.ntust.cs.idsl.nomissing.dao.calendar.EventDao;
import android.content.Context;

public class SQLiteDaoFactory extends DaoFactory {
	
	public static WeatherDao createWeatherDao(Context context) {
		return new WeatherDao(context);
	}
	
	public static ChimeDao createChimeDao(Context context) {
		return new ChimeDao(context);
	}		
	
	public static ReminderDao createReminderDao(Context context) {
		return new ReminderDao(context);
	}	
	
	@Override
	public IEventDao createEventDao(Context context) {
		return new EventDao(context);
	}
	
}
