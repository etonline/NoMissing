package edu.ntust.cs.idsl.nomissing.alarm;

import android.content.Context;

public class AlarmHandlerFactory {
	public static ReminderAlarmHandler createReminderAlarmHandler(Context context) {
		return new ReminderAlarmHandler(context);
	}

	public static ChimeAlarmHandler createChimeAlarmHandler(Context context) {
		return new ChimeAlarmHandler(context);
	}

	public static WeatherAlarmHandler createWeatherAlarmHandler(Context context) {
		return new WeatherAlarmHandler(context);
	}	
}
