package edu.ntust.cs.idsl.nomissing.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.model.ProgressStatus;
import edu.ntust.cs.idsl.nomissing.notification.NotificationHandlerFactory;
import edu.ntust.cs.idsl.nomissing.util.ToastMaker;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
public class ServerResponseReceiver extends BroadcastReceiver {
	
	public static final String TAG = ServerResponseReceiver.class.getSimpleName();
	
	public static final String ACTION_TTS_CONVERT_TEXT_RESPONSE = "edu.ntust.cs.idsl.nomissing.action.TTS_CONVERT_CONVERT_RESPONSE";
	public static final String ACTION_TTS_GET_CONVERT_STATUS_RESPONSE = "edu.ntust.cs.idsl.nomissing.action.TTS_CONVERT_CONVERT_RESPONSE";
	public static final String ACTION_GET_AUDIO_FILE_RESPONSE = "edu.ntust.cs.idsl.nomissing.action.GET_AUDIO_FILE_RESPONSE";	
	
	public static final String ACTION_GET_WEATHER_DATE_START = "edu.ntust.cs.idsl.nomissing.action.START";
	public static final String ACTION_GET_WEATHER_DATE_PROGRASS_UPDATE = "edu.ntust.cs.idsl.nomissing.action.WEATHER_PROGRASS_UPDATE";
	public static final String ACTION_GET_WEATHER_DATE_FINISH = "edu.ntust.cs.idsl.nomissing.action.GET_WEATHER_DATE_SUCESS";
	public static final String ACTION_GET_WEATHER_DATE_FAILURE = "edu.ntust.cs.idsl.nomissing.action.GET_WEATHER_DATE_FAILURE";
	
	public ServerResponseReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		
		if (intent.getAction().equals(ACTION_TTS_CONVERT_TEXT_RESPONSE)) {
		
		}
		
		if (intent.getAction().equals(ACTION_TTS_GET_CONVERT_STATUS_RESPONSE)) {
			
		}
		
		if (intent.getAction().equals(ACTION_GET_AUDIO_FILE_RESPONSE)) {
			String audio = intent.getStringExtra("audio");
//			ToastMaker.toast(context, audio);
		}
		
		if (intent.getAction().equals(ACTION_GET_WEATHER_DATE_START)) {
			ToastMaker.toast(context, R.string.toast_refresh_weather_data_start);	
			ProgressStatus progressStatus = new ProgressStatus(
					ProgressStatus.START,
					context.getString(R.string.action_refresh_weather_data),
					context.getString(R.string.toast_refresh_weather_data_start), 0);
			NotificationHandlerFactory.createProgressNotificationHandler(context).sendNotification(progressStatus);
		}
		
		if(intent.getAction().equals(ACTION_GET_WEATHER_DATE_PROGRASS_UPDATE)) {
			double progress = intent.getDoubleExtra("progress", 0);
			ProgressStatus progressStatus = new ProgressStatus(
					ProgressStatus.PROGRESS_UPDATE,
					context.getString(R.string.action_refresh_weather_data),
					context.getString(R.string.toast_refresh_weather_data_start), (int)progress);
			NotificationHandlerFactory.createProgressNotificationHandler(context).sendNotification(progressStatus);
		}
		
		if(intent.getAction().equals(ACTION_GET_WEATHER_DATE_FINISH)) {
			ProgressStatus progressStatus = new ProgressStatus(
					ProgressStatus.FINISH,
					context.getString(R.string.action_refresh_weather_data),
					context.getString(R.string.toast_refresh_weather_data_finish), 100);
			NotificationHandlerFactory.createProgressNotificationHandler(context).sendNotification(progressStatus);
			ToastMaker.toast(context, R.string.toast_refresh_weather_data_finish);
		}
		
		if(intent.getAction().equals(ACTION_GET_WEATHER_DATE_FAILURE)) {
			ToastMaker.toast(context, R.string.toast_refresh_weather_data_failure);
		}		
	
	}
	
}
