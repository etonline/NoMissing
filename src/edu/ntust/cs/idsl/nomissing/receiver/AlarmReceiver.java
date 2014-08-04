package edu.ntust.cs.idsl.nomissing.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import edu.ntust.cs.idsl.nomissing.activity.AlarmActivity;
import edu.ntust.cs.idsl.nomissing.dao.ChimeDAO;
import edu.ntust.cs.idsl.nomissing.dao.WeatherDAO;
import edu.ntust.cs.idsl.nomissing.model.Chime;
import edu.ntust.cs.idsl.nomissing.model.Weather;
import edu.ntust.cs.idsl.nomissing.service.MediaPlayerService;
import edu.ntust.cs.idsl.nomissing.util.NotificationUtil;

public class AlarmReceiver extends BroadcastReceiver {
	
	public static final String TAG = AlarmReceiver.class.getSimpleName();
	
	public static final String ACTION_CHIME_ALARM = "edu.ntust.cs.idsl.nomissing.action.CHIME_ALARM";
	public static final String ACTION_WEATHER_ALARM = "edu.ntust.cs.idsl.nomissing.action.WEATHER_ALARM";
	
	public AlarmReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.v(TAG, intent.getAction());		
		
		if (intent.getAction().equals(ACTION_CHIME_ALARM)) {
			Intent newIntent = new Intent(context, AlarmActivity.class);
			newIntent.setAction(AlarmActivity.ACTION_CHIME_ALARM_DIALOG);
			newIntent.putExtra("id", intent.getIntExtra("id", -1));
			newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
			context.startActivity(newIntent);			
		}
		
		if (intent.getAction().equals(ACTION_WEATHER_ALARM)) {
			Intent newIntent = new Intent(context, AlarmActivity.class);
			newIntent.setAction(AlarmActivity.ACTION_WEATHER_ALARM_DIALOG);
			newIntent.putExtra("id", intent.getIntExtra("id", -1));
			newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
			context.startActivity(newIntent);	
		}

	}
	
}
