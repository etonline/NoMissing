package edu.ntust.cs.idsl.nomissing.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import edu.ntust.cs.idsl.nomissing.activity.ChimeActivity;
import edu.ntust.cs.idsl.nomissing.activity.EventActivity;
import edu.ntust.cs.idsl.nomissing.dao.sqlite.SQLiteDaoFactory;
import edu.ntust.cs.idsl.nomissing.model.Chime;
import edu.ntust.cs.idsl.nomissing.model.Reminder;
import edu.ntust.cs.idsl.nomissing.model.Weather;
import edu.ntust.cs.idsl.nomissing.notification.NotificationHandlerFactory;

public class AlarmReceiver extends BroadcastReceiver {
	
	public static final String TAG = AlarmReceiver.class.getSimpleName();
	
	public static final String ACTION_REMINDER_ALARM = "edu.ntust.cs.idsl.nomissing.action.REMINDER_ALARM";
	public static final String ACTION_CHIME_ALARM = "edu.ntust.cs.idsl.nomissing.action.CHIME_ALARM";
	public static final String ACTION_WEATHER_ALARM = "edu.ntust.cs.idsl.nomissing.action.WEATHER_ALARM";
	
	public AlarmReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.v(TAG, intent.getAction());		
		
		if (intent.getAction().equals(ACTION_REMINDER_ALARM)) {
			long reminderID = intent.getIntExtra("id", 0);
			Reminder reminder = SQLiteDaoFactory.createReminderDao(context).find(reminderID);
			
			NotificationHandlerFactory.createReminderNotificationHandler(context).sendNotification(reminder);
			
			Intent newIntent = new Intent(context, EventActivity.class);
			newIntent.putExtra("id", reminderID);
			newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
			context.startActivity(newIntent);	
		}		
		
		if (intent.getAction().equals(ACTION_CHIME_ALARM)) {
			int chimeID = intent.getIntExtra("id", 0);
			Chime chime = SQLiteDaoFactory.createChimeDao(context).find(chimeID);
			
			NotificationHandlerFactory.createChimeNotificationHandler(context).sendNotification(chime);
			
			Intent newIntent = new Intent(context, ChimeActivity.class);
			newIntent.putExtra("id", chimeID);
			newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
			context.startActivity(newIntent);	
		}
		
		if (intent.getAction().equals(ACTION_WEATHER_ALARM)) {
			int cityID = intent.getIntExtra("id", 0);
			Weather weather = SQLiteDaoFactory.createWeatherDao(context).find(cityID);
			NotificationHandlerFactory.createWeatherNotificationHandler(context).sendNotification(weather);
		}

	}
	
}
