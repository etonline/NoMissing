package edu.ntust.cs.idsl.nomissing.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import edu.ntust.cs.idsl.nomissing.model.Reminder;
import edu.ntust.cs.idsl.nomissing.receiver.AlarmReceiver;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
public class ReminderAlarmHandler extends AlarmHandler<Reminder> {

	public ReminderAlarmHandler(Context context) {
		super(context);
	}

	@Override
	public void setAlarm(Reminder reminder) {
		long triggerAtMillis = reminder.getReminderTime();
		
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		PendingIntent pendingIntent = getPendingIntent(reminder);
		
		alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
	}

	@Override
	public void cancelAlarm(Reminder reminder) {
		PendingIntent pendingIntent = getPendingIntent(reminder);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);		
		alarmManager.cancel(pendingIntent);
		pendingIntent.cancel();
	}

	@Override
	protected PendingIntent getPendingIntent(Reminder reminder) {
		Intent intent = AlarmReceiver.getActionReminderAlarm(context, reminder.getId());
		return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}

}
