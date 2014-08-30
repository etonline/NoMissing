package edu.ntust.cs.idsl.nomissing.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import edu.ntust.cs.idsl.nomissing.model.Weather;
import edu.ntust.cs.idsl.nomissing.preference.SettingsManager;
import edu.ntust.cs.idsl.nomissing.receiver.AlarmReceiver;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
public class WeatherAlarmHandler extends AlarmHandler<Weather> {

    public WeatherAlarmHandler(Context context) {
        super(context);
    }

    @Override
    public void setAlarm(Weather weather) {
        SettingsManager userSettings = SettingsManager.getInstance(context);
        int hour = userSettings.getWeatherReminderHour();
        int minute = userSettings.getWeatherReminderMinute();
        long triggerAtMillis = calculateAlarm(hour, minute).getTimeInMillis();
        long intervalMillis = ONE_DAY_MILLIS;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = getPendingIntent(weather);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerAtMillis, intervalMillis, pendingIntent);
    }

    @Override
    public void cancelAlarm(Weather weather) {
        PendingIntent pendingIntent = getPendingIntent(weather);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
    }

    @Override
    protected PendingIntent getPendingIntent(Weather weather) {
        Intent intent = AlarmReceiver.getActionReceiveWeatherAlarm(context, weather.getCityID());
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}
