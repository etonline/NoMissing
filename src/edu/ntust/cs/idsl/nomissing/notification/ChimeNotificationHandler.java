package edu.ntust.cs.idsl.nomissing.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.model.Chime;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
public class ChimeNotificationHandler extends NotificationHandler<Chime> {

	public ChimeNotificationHandler(Context context) {
		super(context);
	}

	@Override
	public void sendNotification(Chime chime) {
		boolean vibrate = true;
		
		NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
			.setAutoCancel(true)
			.setContentIntent( getPendingIntent(chime))
			.setContentTitle(context.getString(R.string.title_activity_chime))
			.setContentText(chime.getTime())
			.setDefaults(vibrate ? Notification.DEFAULT_ALL : Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS)
			.setTicker(chime.getTime())
			.setSmallIcon(R.drawable.ic_launcher)
			.setWhen(System.currentTimeMillis());
		
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(chime.getId(), notification.build());
	}

	@Override
	protected PendingIntent getPendingIntent(Chime chime) {
		return PendingIntent.getActivity(context.getApplicationContext(), 0, new Intent(), 0);
	}

}
