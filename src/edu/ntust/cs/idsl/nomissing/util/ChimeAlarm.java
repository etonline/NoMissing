package edu.ntust.cs.idsl.nomissing.util;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import edu.ntust.cs.idsl.nomissing.model.Chime;
import edu.ntust.cs.idsl.nomissing.receiver.AlarmReceiver;

public class ChimeAlarm {

	private Context context;
	
	public ChimeAlarm(Context context) {
		this.context = context;
	}

	public void setAlarm(Chime chime) {
		int hour = chime.getHour();
		int minute = chime.getMinute();

		if (chime.isEnabled() && !chime.isTriggered()) {
			long timeInMillis = calculateAlarm(hour, minute).getTimeInMillis();
			AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);	
			
			if (chime.isRepeating()) {
				alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, 10 * 1000, getPendingIntent(chime.getId()));
			} else {
				alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, getPendingIntent(chime.getId()));
			}	
		} else {
			cancelAlarm(chime);
		}

	}
	
	public void cancelAlarm(Chime chime) {
		PendingIntent pendingIntent = getPendingIntent(chime.getId());
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);		
		alarmManager.cancel(pendingIntent);
		pendingIntent.cancel();
	}
	
	private PendingIntent getPendingIntent(int id) {
		Intent intent =  new Intent(context, AlarmReceiver.class);
		intent.putExtra("chimeID", id);
		return PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}
	
	public Calendar calculateAlarm(int hour, int minute) {
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
	
}
