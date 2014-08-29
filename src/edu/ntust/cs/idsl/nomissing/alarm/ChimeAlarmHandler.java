package edu.ntust.cs.idsl.nomissing.alarm;

import java.util.concurrent.TimeUnit;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import edu.ntust.cs.idsl.nomissing.model.Chime;
import edu.ntust.cs.idsl.nomissing.receiver.AlarmReceiver;
import edu.ntust.cs.idsl.nomissing.util.ToastMaker;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
public class ChimeAlarmHandler extends AlarmHandler<Chime> {
	
	public ChimeAlarmHandler(Context context) {
		super(context);
	}

	@Override
	public void setAlarm(Chime chime) {
		long triggerAtMillis = calculateAlarm(chime.getHour(), chime.getMinute()).getTimeInMillis();
		long intervalMillis = ONE_DAY_MILLIS;
		
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		PendingIntent pendingIntent = getPendingIntent(chime);
		
		if (chime.isRepeating()) {
			alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerAtMillis, intervalMillis, pendingIntent);
		} else {
			alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
		}	
		
		ToastMaker.toast(context, calculateDiffTime(triggerAtMillis));
	}

	@Override
	public void cancelAlarm(Chime chime) {
		PendingIntent pendingIntent = getPendingIntent(chime);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);		
		alarmManager.cancel(pendingIntent);
		pendingIntent.cancel();	
	}

	@Override
	protected PendingIntent getPendingIntent(Chime chime) {
		Intent intent = AlarmReceiver.getActionChimeAlarm(context, chime.getId());
		return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}
	
	private String calculateDiffTime(long time) {
		long diffMillis = time - System.currentTimeMillis();
		long diffHours = TimeUnit.MILLISECONDS.toHours(diffMillis) % 24;
		long diffMinutes = TimeUnit.MILLISECONDS.toMinutes(diffMillis) % 60;
		
		String diffTime = "已將語音報時設定在 " + diffHours + " 小時又 " + diffMinutes + " 分鐘後啟動";
		return diffTime;
	}

}
