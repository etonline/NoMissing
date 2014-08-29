package edu.ntust.cs.idsl.nomissing.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.activity.EventActivity;
import edu.ntust.cs.idsl.nomissing.dao.DaoFactory;
import edu.ntust.cs.idsl.nomissing.model.Event;
import edu.ntust.cs.idsl.nomissing.model.Reminder;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
public class ReminderNotificationHandler extends NotificationHandler<Reminder> {

	public ReminderNotificationHandler(Context context) {
		super(context);
	}

	@Override
	public void sendNotification(Reminder reminder) {
		Event event = DaoFactory.getEventDaoFactory(reminder.getCalendarID()).createEventDao(context).find(reminder.getEventID());
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
		return PendingIntent.getActivity(context.getApplicationContext(), 0, new Intent(), 0);
	}

}
