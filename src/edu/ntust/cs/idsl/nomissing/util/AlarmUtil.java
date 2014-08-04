package edu.ntust.cs.idsl.nomissing.util;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import edu.ntust.cs.idsl.nomissing.model.Chime;
import edu.ntust.cs.idsl.nomissing.model.Weather;
import edu.ntust.cs.idsl.nomissing.pref.UserSettings;
import edu.ntust.cs.idsl.nomissing.receiver.AlarmReceiver;

public final class AlarmUtil {

	public static void setChimeAlarm(Context context, Chime chime) {
		int hour = chime.getHour();
		int minute = chime.getMinute();

		if (chime.isEnabled() && !chime.isTriggered()) {
			long timeInMillis = calculateAlarm(hour, minute).getTimeInMillis();
			AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);	
			
			if (chime.isRepeating()) {
				alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, 86400 * 1000, getPendingIntent(context, AlarmReceiver.ACTION_CHIME_ALARM ,chime.getId()));
			} else {
				alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, getPendingIntent(context, AlarmReceiver.ACTION_CHIME_ALARM, chime.getId()));
			}	
		} else {
			cancelChimeAlarm(context, chime);
		}

	}
	
	public static void cancelChimeAlarm(Context context, Chime chime) {
		PendingIntent pendingIntent = getPendingIntent(context, AlarmReceiver.ACTION_CHIME_ALARM, chime.getId());
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);		
		alarmManager.cancel(pendingIntent);
		pendingIntent.cancel();
	}
	
	public static void setWeatherAlarm(Context context) {
		UserSettings userSettings = UserSettings.getInstance(context);
		long timeInMillis = calculateAlarm(userSettings.getWeatherTime()).getTimeInMillis();
		int cityID = userSettings.getWeatherCity();
		
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);	
		alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, getPendingIntent(context, AlarmReceiver.ACTION_WEATHER_ALARM, cityID));
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, 86400 * 1000, getPendingIntent(context, AlarmReceiver.ACTION_WEATHER_ALARM, cityID));
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timeInMillis);
	}
	
	public static void cancelWeatherAlarm(Context context) {
		UserSettings userSettings = UserSettings.getInstance(context);
		int cityID = userSettings.getWeatherCity();
		
		PendingIntent pendingIntent = getPendingIntent(context, AlarmReceiver.ACTION_WEATHER_ALARM, cityID);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);		
		alarmManager.cancel(pendingIntent);
		pendingIntent.cancel();
	}
	
	private static PendingIntent getPendingIntent(Context context, String action, int id) {
		Intent intent =  new Intent(context, AlarmReceiver.class);
		intent.putExtra("id", id);
		intent.setAction(action);
		return PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}
	
	private static Calendar calculateAlarm(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        
        int nowHour = calendar.get(Calendar.HOUR_OF_DAY);
        int nowMinute = calendar.get(Calendar.MINUTE);
        
        // if alarm is behind current time, advance one day
        if (hour < nowHour  ||
            hour == nowHour && minute <= nowMinute) {
        	calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);		
        
        return calendar;
	}
	
	private static Calendar calculateAlarm(long timeMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        // if alarm is behind current time, advance one day
        if (timeMillis < calendar.getTimeInMillis()) {
        	calendar.setTimeInMillis(timeMillis);
        	calendar.add(Calendar.DAY_OF_YEAR, 1);
        } else {
        	calendar.setTimeInMillis(timeMillis);
        }
        
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);	
        
        return calendar;
	}	
	
}
