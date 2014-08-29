package edu.ntust.cs.idsl.nomissing.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import edu.ntust.cs.idsl.nomissing.activity.ChimeActivity;
import edu.ntust.cs.idsl.nomissing.activity.EventActivity;
import edu.ntust.cs.idsl.nomissing.dao.DaoFactory;
import edu.ntust.cs.idsl.nomissing.model.Chime;
import edu.ntust.cs.idsl.nomissing.model.Reminder;
import edu.ntust.cs.idsl.nomissing.model.Weather;
import edu.ntust.cs.idsl.nomissing.notification.NotificationHandlerFactory;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
public class AlarmReceiver extends BroadcastReceiver {
	
	public static final String TAG = AlarmReceiver.class.getSimpleName();
	public static final String ACTION_REMINDER_ALARM = "edu.ntust.cs.idsl.nomissing.action.REMINDER_ALARM";
	public static final String ACTION_CHIME_ALARM = "edu.ntust.cs.idsl.nomissing.action.CHIME_ALARM";
	public static final String ACTION_WEATHER_ALARM = "edu.ntust.cs.idsl.nomissing.action.WEATHER_ALARM";
	public static final String EXTRA_ENTITY_ID = "edu.ntust.cs.idsl.nomissing.extra.ENTITY_ID";
	
	public AlarmReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent != null) {
			Log.v(TAG, intent.getAction());	
			final String action = intent.getAction();
			final long id = intent.getLongExtra(EXTRA_ENTITY_ID, 0);
			
			if (ACTION_REMINDER_ALARM.equals(action)) {		
				handleActionReminderAlarm(context, id);
				return;
			}
			
			if (ACTION_CHIME_ALARM.equals(action)) {		
				handleActionChimeAlarm(context, id);
				return;
			}			
			
			if (ACTION_WEATHER_ALARM.equals(action)) {		
				handleActionWeatherAlarm(context, id);
				return;
			}			
		}
	}
	
	private void handleActionReminderAlarm(Context context, long id) {
		Reminder reminder = DaoFactory.getSQLiteDaoFactory().createReminderDao(context).find(id);
		NotificationHandlerFactory.createReminderNotificationHandler(context).sendNotification(reminder);
		EventActivity.startActivity(context, id);
	}
	
	private void handleActionChimeAlarm(Context context, long id) {
		Chime chime = DaoFactory.getSQLiteDaoFactory().createChimeDao(context).find((int)id);
		NotificationHandlerFactory.createChimeNotificationHandler(context).sendNotification(chime);
		ChimeActivity.startActivity(context, (int)id);
	}
	
	private void handleActionWeatherAlarm(Context context, long id) {
		Weather weather = DaoFactory.getSQLiteDaoFactory().createWeatherDao(context).find((int)id);
		NotificationHandlerFactory.createWeatherNotificationHandler(context).sendNotification(weather);	
	}
	
	public static Intent getActionReminderAlarm(Context context, long reminderID) {
		Intent intent = new Intent(context, AlarmReceiver.class);
		intent.setAction(ACTION_REMINDER_ALARM);
		intent.putExtra(EXTRA_ENTITY_ID, reminderID);
		return intent;
	}
	
	public static Intent getActionChimeAlarm(Context context, long chimeID) {
		Intent intent = new Intent(context, AlarmReceiver.class);
		intent.setAction(ACTION_CHIME_ALARM);
		intent.putExtra(EXTRA_ENTITY_ID, chimeID);
		return intent;
	}
	
	public static Intent getActionWeatherAlarm(Context context, long weatherID) {
		Intent intent = new Intent(context, AlarmReceiver.class);
		intent.setAction(ACTION_WEATHER_ALARM);
		intent.putExtra(EXTRA_ENTITY_ID, weatherID);
		return intent;
	}
	
}
