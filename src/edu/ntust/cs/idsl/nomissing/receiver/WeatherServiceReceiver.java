package edu.ntust.cs.idsl.nomissing.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.fragment.WeatherFragment;
import edu.ntust.cs.idsl.nomissing.model.ProgressStatus;
import edu.ntust.cs.idsl.nomissing.notification.NotificationHandlerFactory;
import edu.ntust.cs.idsl.nomissing.util.ToastMaker;

public class WeatherServiceReceiver extends BroadcastReceiver {

    private static final String TAG = WeatherServiceReceiver.class.getSimpleName();
    public static final String ACTION_RECEIVE_WEATHER_SERVICE_START = "edu.ntust.cs.idsl.nomissing.action.RECEIVE_WEATHER_SERVICE_START";
    public static final String ACTION_RECEIVE_WEATHER_SERVICE_UPDATE_PROGRESS = "edu.ntust.cs.idsl.nomissing.action.RECEIVE_WEATHER_SERVICE_UPDATE_PROGRESS";
    public static final String ACTION_RECEIVE_WEATHER_SERVICE_FINISH = "edu.ntust.cs.idsl.nomissing.action.RECEIVE_WEATHER_SERVICE_FINISH";
    public static final String ACTION_RECEIVE_WEATHER_SERVICE_FAILURE = "edu.ntust.cs.idsl.nomissing.action.RECEIVE_WEATHER_SERVICE_FAILURE";
    private static final String EXTRA_PROGRESS = "edu.ntust.cs.idsl.nomissing.extra.PROGRESS";
    
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            Log.v(TAG, intent.getAction());
            final String action = intent.getAction();

            if (ACTION_RECEIVE_WEATHER_SERVICE_START.equals(action)) {
                handleActionReceiveWeatherServiceStart(context);
                return;
            }

            if (ACTION_RECEIVE_WEATHER_SERVICE_UPDATE_PROGRESS.equals(action)) {
                final double progress = intent.getDoubleExtra(EXTRA_PROGRESS, 0);
                handleActionReceiveWeatherServiceUpdateProgress(context, progress);
                return;
            }

            if (ACTION_RECEIVE_WEATHER_SERVICE_FINISH.equals(action)) {
                handleActionReceiveWeatherServiceFinish(context);
                return;
            }
            
            if (ACTION_RECEIVE_WEATHER_SERVICE_FAILURE.equals(action)) {
                handleActionReceiveWeatherServiceFailure(context);
                return;
            }
        }
    }
    
    protected void handleActionReceiveWeatherServiceStart(Context context) {   
        ProgressStatus progressStatus = new ProgressStatus(
                ProgressStatus.START,
                context.getString(R.string.app_name),
                context.getString(R.string.refresh_weather_data_start), 0);
        NotificationHandlerFactory.createProgressNotificationHandler(context).sendNotification(progressStatus);  
        ToastMaker.toast(context, R.string.refresh_weather_data_start);   
        context.sendBroadcast(WeatherFragment.getActionReceiveRefreshInvisible(context));
    }

    protected void handleActionReceiveWeatherServiceUpdateProgress(Context context, double progress) {
        ProgressStatus progressStatus = new ProgressStatus(
                ProgressStatus.PROGRESS_UPDATE,
                context.getString(R.string.app_name),
                context.getString(R.string.refresh_weather_data_start), (int) progress);
        NotificationHandlerFactory.createProgressNotificationHandler(context).sendNotification(progressStatus);       
    }
    
    protected void handleActionReceiveWeatherServiceFinish(Context context) {
        ProgressStatus progressStatus = new ProgressStatus(
                ProgressStatus.FINISH,
                context.getString(R.string.app_name),
                context.getString(R.string.refresh_weather_data_finish), 100);
        NotificationHandlerFactory.createProgressNotificationHandler(context).sendNotification(progressStatus);
        ToastMaker.toast(context, R.string.refresh_weather_data_finish);    
        context.sendBroadcast(WeatherFragment.getActionReceiveRefreshVisible(context));   
    }
    
    protected void handleActionReceiveWeatherServiceFailure(Context context) {
        ProgressStatus progressStatus = new ProgressStatus(
                ProgressStatus.FAILURE,
                context.getString(R.string.app_name),
                context.getString(R.string.refresh_weather_data_failure), 0);
        NotificationHandlerFactory.createProgressNotificationHandler(context).sendNotification(progressStatus);
        ToastMaker.toast(context, R.string.refresh_weather_data_failure);       
        context.sendBroadcast(WeatherFragment.getActionReceiveRefreshVisible(context));
    }
    
    public static Intent getActionReceiveWeatherServiceStart(Context context) {
        Intent intent = new Intent(context, WeatherServiceReceiver.class);
        intent.setAction(ACTION_RECEIVE_WEATHER_SERVICE_START);
        return intent;
    }
    
    public static Intent getActionReceiveWeatherServiceUpdateProgress(Context context, double progress) {
        Intent intent = new Intent(context, WeatherServiceReceiver.class);
        intent.setAction(ACTION_RECEIVE_WEATHER_SERVICE_UPDATE_PROGRESS);
        intent.putExtra(EXTRA_PROGRESS, progress);
        return intent;
    }
    
    public static Intent getActionReceiveWeatherServiceFinish(Context context) {
        Intent intent = new Intent(context, WeatherServiceReceiver.class);
        intent.setAction(ACTION_RECEIVE_WEATHER_SERVICE_FINISH);
        return intent;
    }
    
    public static Intent getActionReceiveWeatherServiceFailure(Context context) {
        Intent intent = new Intent(context, WeatherServiceReceiver.class);
        intent.setAction(ACTION_RECEIVE_WEATHER_SERVICE_FAILURE);
        return intent;
    }

	
}
