package edu.ntust.cs.idsl.nomissing.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import edu.ntust.cs.idsl.nomissing.model.Chime;
import edu.ntust.cs.idsl.nomissing.receiver.AlarmReceiver;

public class ChimeAlarmHandler extends AlarmHandler<Chime> {

	public ChimeAlarmHandler(Context context) {
		super(context);
	}

	@Override
	public void setAlarm(Chime chime) {
		long triggerAtMillis = calculateAlarm(chime.getHour(), chime.getMinute()).getTimeInMillis();
		long intervalMillis = 86400 * 1000; // one day
		
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		PendingIntent pendingIntent = getPendingIntent(AlarmReceiver.ACTION_CHIME_ALARM ,chime);
		
		if (chime.isRepeating()) {
			alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerAtMillis, intervalMillis, pendingIntent);
		} else {
			alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
		}	
	}

	@Override
	public void cancelAlarm(Chime chime) {
		PendingIntent pendingIntent = getPendingIntent(AlarmReceiver.ACTION_CHIME_ALARM, chime);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);		
		alarmManager.cancel(pendingIntent);
		pendingIntent.cancel();	
	}

	@Override
	protected PendingIntent getPendingIntent(String action, Chime chime) {
		Intent intent =  new Intent(context, AlarmReceiver.class);
		intent.putExtra("id", chime.getId());
		intent.setAction(action);
		return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}

}
