package edu.ntust.cs.idsl.nomissing.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import edu.ntust.cs.idsl.nomissing.model.Weather;
import edu.ntust.cs.idsl.nomissing.pref.SettingsManager;
import edu.ntust.cs.idsl.nomissing.receiver.AlarmReceiver;

public class WeatherAlarmHandler extends AlarmHandler<Weather> {

	public WeatherAlarmHandler(Context context) {
		super(context);
	}

	@Override
	public void setAlarm(Weather weather) {
		SettingsManager userSettings = SettingsManager.getInstance(context);
		int cityID = userSettings.getWeatherReminderCity();
		long triggerAtMillis = calculateAlarm(userSettings.getWeatherReminderHour(), userSettings.getWeatherReminderMinute()).getTimeInMillis();
		long intervalMillis = 86400 * 1000; // one day
		
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		PendingIntent pendingIntent = getPendingIntent(AlarmReceiver.ACTION_WEATHER_ALARM, weather);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerAtMillis, intervalMillis, pendingIntent);
	}

	@Override
	public void cancelAlarm(Weather weather) {
		SettingsManager userSettings = SettingsManager.getInstance(context);
		int cityID = userSettings.getWeatherReminderCity();
		
		PendingIntent pendingIntent = getPendingIntent(AlarmReceiver.ACTION_WEATHER_ALARM, weather);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);		
		alarmManager.cancel(pendingIntent);
		pendingIntent.cancel();	
	}

	@Override
	protected PendingIntent getPendingIntent(String action, Weather weather) {
		Intent intent =  new Intent(context, AlarmReceiver.class);
		intent.putExtra("id", weather.getCityID());
		intent.setAction(action);
		return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}

}
