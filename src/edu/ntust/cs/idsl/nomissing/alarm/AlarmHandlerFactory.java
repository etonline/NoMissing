package edu.ntust.cs.idsl.nomissing.alarm;

import android.content.Context;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
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
