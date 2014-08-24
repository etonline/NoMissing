package edu.ntust.cs.idsl.nomissing.notification;

import android.app.PendingIntent;
import android.content.Context;

public abstract class NotificationHandler<T> {
	protected Context context;
	
	public NotificationHandler(Context context) {
		this.context = context;
	}
	
	public abstract void sendNotification(T entity);
	
	protected abstract PendingIntent getPendingIntent(T entity);
	
}
