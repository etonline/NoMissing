package edu.ntust.cs.idsl.nomissing.notification;

import android.content.Context;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
public class NotificationHandlerFactory {

	public static ReminderNotificationHandler createReminderNotificationHandler(Context context) {
		return new ReminderNotificationHandler(context);
	}

	public static ChimeNotificationHandler createChimeNotificationHandler(Context context) {
		return new ChimeNotificationHandler(context);
	}

	public static WeatherNotificationHandler createWeatherNotificationHandler(Context context) {
		return new WeatherNotificationHandler(context);
	}
	
	public static ProgressNotificationHandler createProgressNotificationHandler(Context context) {
		return new ProgressNotificationHandler(context);
	}

}
