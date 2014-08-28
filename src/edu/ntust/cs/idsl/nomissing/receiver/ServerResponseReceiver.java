package edu.ntust.cs.idsl.nomissing.receiver;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.dao.DaoFactory;
import edu.ntust.cs.idsl.nomissing.http.response.TTSConvertTextProperty;
import edu.ntust.cs.idsl.nomissing.http.response.TTSGetConvertStatusProperty;
import edu.ntust.cs.idsl.nomissing.http.resultcode.TTSConvertTextResultCode;
import edu.ntust.cs.idsl.nomissing.http.resultcode.TTSGetConvertStatusResultCode;
import edu.ntust.cs.idsl.nomissing.model.Chime;
import edu.ntust.cs.idsl.nomissing.model.ProgressStatus;
import edu.ntust.cs.idsl.nomissing.model.Reminder;
import edu.ntust.cs.idsl.nomissing.notification.NotificationHandlerFactory;
import edu.ntust.cs.idsl.nomissing.service.GetAudioFileService;
import edu.ntust.cs.idsl.nomissing.service.TTSGetConvertStatusService;
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
			String category = intent.getStringExtra("category");
			long id = intent.getLongExtra("id", 0);
			String response = intent.getStringExtra("response");
			Log.v(TAG, response);
			
			try {
				JSONObject jsonObject = new JSONObject(response);
				String convertID = jsonObject.getString(TTSConvertTextProperty.RESULT_CONVERT_ID);
				int resultCode = Integer.valueOf(jsonObject.getString(TTSConvertTextProperty.RESULT_CODE));
				
				if (resultCode == TTSConvertTextResultCode.SUCCESS) {
					Intent newIntent = new Intent(context, TTSGetConvertStatusService.class);
					newIntent.setAction(TTSGetConvertStatusService.ACTION_GET_CONVERT_STATUS);
					newIntent.putExtra("category", category);
					newIntent.putExtra("id", id);
					newIntent.putExtra("convertID", convertID);
					context.startService(newIntent);				
				} else {
//					ToastMaker.toast(context, "�y���ɲ��ͥ���");
				}
			} catch (JSONException e) {
				Log.v(TAG, e.toString());
			}			
		}
		
		if (intent.getAction().equals(ACTION_TTS_GET_CONVERT_STATUS_RESPONSE)) {
			String category = intent.getStringExtra("category");
			long id = intent.getLongExtra("id", 0);
			String convertID = intent.getStringExtra("convertID");
			String response = intent.getStringExtra("response");
			Log.v(TAG, response);			
			
			try {
				JSONObject jsonObject = new JSONObject(response);
				int statusCode = Integer.valueOf(jsonObject.getString(TTSGetConvertStatusProperty.STATUS_CODE));
				
				if (statusCode != TTSGetConvertStatusResultCode.CONVERT_STATUS_COMPLETED) {
					Intent newIntent = new Intent(context, TTSGetConvertStatusService.class);
					newIntent.setAction(TTSGetConvertStatusService.ACTION_GET_CONVERT_STATUS);
					newIntent.putExtra("category", category);
					newIntent.putExtra("id", id);
					newIntent.putExtra("convertID", convertID);
					context.startService(newIntent);			
				} else {
					String audio = jsonObject.getString("audio");
					setAudio(context, category, id, audio);
					
					Intent newIntent = new Intent(context, GetAudioFileService.class);
					newIntent.setAction(GetAudioFileService.ACTION_GET_AUDIO_FILE);
					newIntent.putExtra("id", id);
					newIntent.putExtra("category", category);
					newIntent.putExtra("url", audio);
					context.startService(newIntent);	
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}				
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
	
	private void setAudio(Context context, String category, long id, String audio) {
		if (category.equals("reminder")) {
			Reminder reminder = DaoFactory.getSQLiteDaoFactory().createReminderDao(context).find(id);
			reminder.setAudio(audio);
			DaoFactory.getSQLiteDaoFactory().createReminderDao(context).update(reminder);
		}
		
		if (category.equals("chime")) {
			Chime chime = DaoFactory.getSQLiteDaoFactory().createChimeDao(context).find((int)id);
			chime.setAudio(audio);
			DaoFactory.getSQLiteDaoFactory().createChimeDao(context).update(chime);
		}
		
		ToastMaker.toast(context, R.string.audio_file_created);
	}
	
}
