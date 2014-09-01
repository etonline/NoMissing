package edu.ntust.cs.idsl.nomissing.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.model.ProgressStatus;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
public class ProgressNotificationHandler extends
        NotificationHandler<ProgressStatus> {

    private static final int progressMax = 100;

    public ProgressNotificationHandler(Context context) {
        super(context);
    }

    @Override
    public void sendNotification(ProgressStatus progressStatus) {
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context).setAutoCancel(true)
                .setContentIntent(getPendingIntent(progressStatus))
                .setContentTitle(progressStatus.getTitle())
                .setContentText(progressStatus.getMessage())
                .setSmallIcon(R.drawable.ic_launcher)
                .setTicker(progressStatus.getMessage());

        if (progressStatus.getStatus() != ProgressStatus.FINISH) {
            notification.setProgress(progressMax, progressStatus.getProgress(), false);
            notification.setNumber(progressStatus.getProgress());
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification.build());
        
        if ((progressStatus.getStatus() == ProgressStatus.FINISH)) {
            notificationManager.cancel(0);
        }        
    }

    @Override
    protected PendingIntent getPendingIntent(ProgressStatus progressStatus) {
        return PendingIntent.getActivity(context.getApplicationContext(), 0, new Intent(), 0);
    }

}
