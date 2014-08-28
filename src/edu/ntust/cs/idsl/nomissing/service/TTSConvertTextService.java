package edu.ntust.cs.idsl.nomissing.service;

import java.util.HashMap;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import edu.ntust.cs.idsl.nomissing.global.NoMissingApp;
import edu.ntust.cs.idsl.nomissing.http.NoMissingHttpClient;
import edu.ntust.cs.idsl.nomissing.http.parameter.TTSConvertTextParameter;
import edu.ntust.cs.idsl.nomissing.receiver.ServerResponseReceiver;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
public class TTSConvertTextService extends IntentService {
	
	private static final String TAG = TTSConvertTextService.class.getSimpleName();
	
	public static final String ACTION_CONVERT_TEXT = "edu.ntust.cs.idsl.nomissing.action.CONVERT_TEXT";
	private NoMissingApp app;
	private String category;
	private long id;
	
	public TTSConvertTextService() {
		super("GetWeatherDataService");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		app = (NoMissingApp)getApplicationContext();
		
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (intent.getAction().equals(ACTION_CONVERT_TEXT)) {
			category = intent.getStringExtra("category");
			id = intent.getLongExtra("id", -1);
			
			String TTStext = intent.getStringExtra(TTSConvertTextParameter.TTS_TEXT);
			String TTSSpeaker = intent.getStringExtra(TTSConvertTextParameter.TTS_SPEAKER);
			int volume = intent.getIntExtra(TTSConvertTextParameter.VOLUME, 100);
			int speed = intent.getIntExtra(TTSConvertTextParameter.SPEED, 0);
			String outtype = intent.getStringExtra(TTSConvertTextParameter.OUTPUT_TYPE);
			
			RequestParams params = new RequestParams();
	        params.add(TTSConvertTextParameter.TTS_TEXT, TTStext);
	        params.add(TTSConvertTextParameter.TTS_SPEAKER, TTSSpeaker);
	        params.add(TTSConvertTextParameter.VOLUME, Integer.toString(volume));
	        params.add(TTSConvertTextParameter.SPEED, Integer.toString(speed));
	        params.add(TTSConvertTextParameter.OUTPUT_TYPE, outtype);
			
	        NoMissingHttpClient.setAsync(false);
			NoMissingHttpClient.ttsConvertText(app.getSettings().getUUID(), app.getSettings().getAccessToken(), params, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					Log.v(TAG, response.toString());	
					
					Intent intent = new Intent(TTSConvertTextService.this, ServerResponseReceiver.class);
					intent.putExtra("category", category);
					intent.putExtra("id", id);
					intent.putExtra("response", response.toString());
					intent.setAction(ServerResponseReceiver.ACTION_TTS_CONVERT_TEXT_RESPONSE);
	                sendBroadcast(intent);
				}
	        });	
		}
	}

}
