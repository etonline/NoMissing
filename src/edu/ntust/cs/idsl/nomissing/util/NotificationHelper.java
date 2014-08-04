package edu.ntust.cs.idsl.nomissing.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.activity.MainActivity;
import edu.ntust.cs.idsl.nomissing.model.Chime;

public class NotificationHelper {

	public static void sendNotification(Context context, Chime chime) {
		boolean vibrate = true;
		
		NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
			.setAutoCancel(true)
			.setContentIntent(getPendingIntent(context, chime.getId()))
			.setContentTitle("»y­µ³ø®É")
			.setContentText(chime.getTime())
			.setDefaults(vibrate ? Notification.DEFAULT_ALL : Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS)
			.setTicker(chime.getTime())
			.setSmallIcon(R.drawable.ic_launcher)
			.setWhen(System.currentTimeMillis());
		
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(chime.getId(), notification.build());
	}

	private static PendingIntent getPendingIntent(Context context, int id) {
		Intent intent =  new Intent(context, MainActivity.class);
		intent.putExtra("chimeID", id);
		return PendingIntent.getActivity(context, id, intent, 0);
	}
	
}
