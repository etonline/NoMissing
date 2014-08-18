package edu.ntust.cs.idsl.nomissing.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.activity.MainActivity;
import edu.ntust.cs.idsl.nomissing.activity.WeatherActivity;
import edu.ntust.cs.idsl.nomissing.dao.SQLiteDAOFactory;
import edu.ntust.cs.idsl.nomissing.model.Chime;
import edu.ntust.cs.idsl.nomissing.model.Weather;

public class NotificationUtil {

	public static void sendNotification(Context context, Weather weather) {
		boolean vibrate = true;
			
		NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
			.setAutoCancel(true)
			.setContentIntent(getPendingIntent(context, weather.getCityID()))
			.setContentTitle(context.getString(R.string.title_activity_weather))
			.setContentText(weather.getCity())
			.setDefaults(vibrate ? Notification.DEFAULT_ALL : Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS)
			.setTicker(context.getString(R.string.title_activity_weather))
			.setSmallIcon(R.drawable.ic_launcher)
			.setWhen(System.currentTimeMillis());
		
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(weather.getCityID(), notification.build());		
	}
	
	public static void sendNotification(Context context, Chime chime) {
		boolean vibrate = true;
		
		PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(), 0, new Intent(), 0);
		
		NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
			.setAutoCancel(true)
			.setContentIntent(pendingIntent)
			.setContentTitle(context.getString(R.string.title_activity_chime))
			.setContentText(chime.getTime())
			.setDefaults(vibrate ? Notification.DEFAULT_ALL : Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS)
			.setTicker(chime.getTime())
			.setSmallIcon(R.drawable.ic_launcher)
			.setWhen(System.currentTimeMillis());
		
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(chime.getId(), notification.build());
	}

	private static PendingIntent getPendingIntent(Context context, int id) {
		Intent intent =  new Intent(context, WeatherActivity.class);
		intent.putExtra("id", id);
		return PendingIntent.getActivity(context, id, intent, 0);
	}
	
}
