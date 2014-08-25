package edu.ntust.cs.idsl.nomissing.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.activity.EventActivity;
import edu.ntust.cs.idsl.nomissing.dao.sqlite.SQLiteDaoFactory;
import edu.ntust.cs.idsl.nomissing.model.Event;
import edu.ntust.cs.idsl.nomissing.model.Reminder;

public class ReminderNotificationHandler extends NotificationHandler<Reminder> {

	public ReminderNotificationHandler(Context context) {
		super(context);
	}

	@Override
	public void sendNotification(Reminder reminder) {
//		Event event = SQLiteDaoFactory.createEventDao(context).find(reminder.getEventID());
		Event event = null;
		boolean vibrate = true;
			
		NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
			.setAutoCancel(true)
			.setContentIntent(getPendingIntent(reminder))
			.setContentTitle(context.getString(R.string.title_activity_event))
			.setContentText(event.getTitle())
			.setDefaults(vibrate ? Notification.DEFAULT_ALL : Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS)
			.setTicker(event.getTitle())
			.setSmallIcon(R.drawable.ic_launcher)
			.setWhen(System.currentTimeMillis());
		
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);	
		notificationManager.notify((int)reminder.getId(), notification.build());	
	}

	@Override
	protected PendingIntent getPendingIntent(Reminder reminder) {
		Intent intent =  new Intent(context, EventActivity.class);
		intent.putExtra("id", reminder.getId());
		return PendingIntent.getActivity(context, 0, intent, 0);
	}

}
