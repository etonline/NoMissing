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
import edu.ntust.cs.idsl.nomissing.receiver.ServerResponseReceiver;

public class TTSGetConvertStatusService extends IntentService {
	
	public static String ACTION_GET_CONVERT_STATUS = "edu.ntust.cs.idsl.nomissing.action.GET_CONVERT_STATUS";
	public static final String TAG = "TTSGetConvertStatusService";
	
	public static final String PARAM_CONVERT_ID = "convertID";
	
	private NoMissingApp app;
	private String username;
	private String password;
	
	private int chimeID;
	private String convertID;
	
	public TTSGetConvertStatusService() {
		super("GetWeatherDataService");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		app = (NoMissingApp)getApplicationContext();
		
		HashMap<String, String> user = app.session.getUserData();
		username = user.get("username");
		password = user.get("password");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (intent.getAction().equals(ACTION_GET_CONVERT_STATUS)) {
			chimeID = intent.getIntExtra("chimeID", -1);
			convertID = intent.getStringExtra(PARAM_CONVERT_ID);
			
			RequestParams params = new RequestParams();
	        params.add(PARAM_CONVERT_ID, convertID);
			
	        NoMissingHttpClient.getInstance(false);
			NoMissingHttpClient.ttsGetConvertStatus(username, password, params, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					Log.v(TAG, response.toString());	
					
					Intent intent = new Intent(TTSGetConvertStatusService.this, ServerResponseReceiver.class);
					intent.putExtra("response", response.toString());
					intent.putExtra("chimeID", chimeID);
					intent.putExtra("convertID", convertID);
					intent.setAction(ServerResponseReceiver.ACTION_TTS_GET_CONVERT_STATUS_RESPONSE);
	                sendBroadcast(intent);
				}
	        });	
		}
	}

}
