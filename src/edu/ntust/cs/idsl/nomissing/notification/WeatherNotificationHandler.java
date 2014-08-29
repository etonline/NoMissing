package edu.ntust.cs.idsl.nomissing.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.activity.WeatherActivity;
import edu.ntust.cs.idsl.nomissing.model.Weather;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
public class WeatherNotificationHandler extends NotificationHandler<Weather> {

	public WeatherNotificationHandler(Context context) {
		super(context);
	}

	@Override
	public void sendNotification(Weather weather) {
		boolean vibrate = true;
		
		NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
			.setAutoCancel(false)
			.setContentIntent(getPendingIntent(weather))
			.setContentTitle(context.getString(R.string.title_activity_weather))
			.setContentText(weather.getCity())
			.setDefaults(vibrate ? Notification.DEFAULT_ALL : Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS)
			.setTicker(context.getString(R.string.title_activity_weather))
			.setSmallIcon(R.drawable.ic_launcher)
			.setWhen(System.currentTimeMillis());
		
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(weather.getCityID(), notification.build());	
	}

	@Override
	protected PendingIntent getPendingIntent(Weather weather) {
		Intent intent = WeatherActivity.getAction(context, weather.getCityID());
		return PendingIntent.getActivity(context, 0, intent, 0);
	}
	
}
