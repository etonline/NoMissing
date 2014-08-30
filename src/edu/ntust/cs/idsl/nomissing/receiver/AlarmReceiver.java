package edu.ntust.cs.idsl.nomissing.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import edu.ntust.cs.idsl.nomissing.activity.ChimeActivity;
import edu.ntust.cs.idsl.nomissing.activity.EventActivity;
import edu.ntust.cs.idsl.nomissing.dao.DaoFactory;
import edu.ntust.cs.idsl.nomissing.model.Chime;
import edu.ntust.cs.idsl.nomissing.model.Reminder;
import edu.ntust.cs.idsl.nomissing.model.Weather;
import edu.ntust.cs.idsl.nomissing.notification.NotificationHandlerFactory;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
public class AlarmReceiver extends BroadcastReceiver {

    public static final String TAG = AlarmReceiver.class.getSimpleName();
    public static final String ACTION_RECEIVE_REMINDER_ALARM = "edu.ntust.cs.idsl.nomissing.action.RECEIVE_REMINDER_ALARM";
    public static final String ACTION_RECEIVE_CHIME_ALARM = "edu.ntust.cs.idsl.nomissing.action.RECEIVE_CHIME_ALARM";
    public static final String ACTION_RECEIVE_WEATHER_ALARM = "edu.ntust.cs.idsl.nomissing.action.RECEIVE_WEATHER_ALARM";
    public static final String EXTRA_ENTITY_ID = "edu.ntust.cs.idsl.nomissing.extra.ENTITY_ID";

    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            Log.v(TAG, intent.getAction());
            final String action = intent.getAction();
            final long id = intent.getLongExtra(EXTRA_ENTITY_ID, 0);

            if (ACTION_RECEIVE_REMINDER_ALARM.equals(action)) {
                handleActionReceiveReminderAlarm(context, id);
                return;
            }

            if (ACTION_RECEIVE_CHIME_ALARM.equals(action)) {
                handleActionReceiveChimeAlarm(context, id);
                return;
            }

            if (ACTION_RECEIVE_WEATHER_ALARM.equals(action)) {
                handleActionReceiveWeatherAlarm(context, id);
                return;
            }
        }
    }

    private void handleActionReceiveReminderAlarm(Context context, long id) {
        Reminder reminder = DaoFactory.getSQLiteDaoFactory().createReminderDao(context).find(id);
        NotificationHandlerFactory.createReminderNotificationHandler(context).sendNotification(reminder);
        EventActivity.startAction(context, id);
    }

    private void handleActionReceiveChimeAlarm(Context context, long id) {
        Chime chime = DaoFactory.getSQLiteDaoFactory().createChimeDao(context).find((int) id);
        NotificationHandlerFactory.createChimeNotificationHandler(context).sendNotification(chime);
        ChimeActivity.startAction(context, (int) id);
    }

    private void handleActionReceiveWeatherAlarm(Context context, long id) {
        Weather weather = DaoFactory.getSQLiteDaoFactory().createWeatherDao(context).find((int) id);
        NotificationHandlerFactory.createWeatherNotificationHandler(context).sendNotification(weather);
    }

    public static Intent getActionReceiveReminderAlarm(Context context, long reminderID) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(ACTION_RECEIVE_REMINDER_ALARM);
        intent.putExtra(EXTRA_ENTITY_ID, reminderID);
        return intent;
    }

    public static Intent getActionReceiveChimeAlarm(Context context, long chimeID) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(ACTION_RECEIVE_CHIME_ALARM);
        intent.putExtra(EXTRA_ENTITY_ID, chimeID);
        return intent;
    }

    public static Intent getActionReceiveWeatherAlarm(Context context, long weatherID) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(ACTION_RECEIVE_WEATHER_ALARM);
        intent.putExtra(EXTRA_ENTITY_ID, weatherID);
        return intent;
    }

}
