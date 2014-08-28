package edu.ntust.cs.idsl.nomissing.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.dao.DaoFactory;
import edu.ntust.cs.idsl.nomissing.global.NoMissingApp;
import edu.ntust.cs.idsl.nomissing.http.NoMissingHttpClient;
import edu.ntust.cs.idsl.nomissing.http.NoMissingRoute;
import edu.ntust.cs.idsl.nomissing.http.response.WeatherProperty;
import edu.ntust.cs.idsl.nomissing.model.Weather;
import edu.ntust.cs.idsl.nomissing.receiver.ServerResponseReceiver;
import edu.ntust.cs.idsl.nomissing.util.Connectivity;
import edu.ntust.cs.idsl.nomissing.util.ToastMaker;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
public class GetWeatherDataService extends IntentService {
	
	private static final String TAG = GetWeatherDataService.class.getSimpleName();
	private NoMissingApp app;
	private boolean isNetworkAvailable;
	private static String audioUri;
	
	public GetWeatherDataService() {
		super("GetWeatherDataService");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		app = (NoMissingApp)getApplicationContext();		
		
		isNetworkAvailable = (Connectivity.isConnected(this)) ? true : false;
		
		if (!isNetworkAvailable) 
			ToastMaker.toast(this, R.string.toast_network_inavailable);	
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (!isNetworkAvailable)
			return;
		
		NoMissingHttpClient.setAsync(false);
		NoMissingHttpClient.get(NoMissingRoute.WEATHER, app.getSettings().getUUID(), app.getSettings().getAccessToken(), new JsonHttpResponseHandler() {
			@Override
			public void onStart() {
//				Intent intent = new Intent(GetWeatherDataService.this, ServerResponseReceiver.class);
				Intent intent = new Intent();
				intent.setAction(ServerResponseReceiver.ACTION_GET_WEATHER_DATE_START);
				sendBroadcast(intent);
			}
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
				Log.i(TAG, response.toString());	
								
				try {
					for (int i = 0; i < response.length(); i++) {
						JSONObject jsonObject = response.getJSONObject(i);
						
						int cityid = Integer.parseInt(jsonObject.getString(WeatherProperty.CITY_ID));
						String city = jsonObject.getString(WeatherProperty.NAME);
						String stno = jsonObject.getString(WeatherProperty.STNO);
						String time = jsonObject.getString(WeatherProperty.TIME);
						String memo = jsonObject.getString(WeatherProperty.MEMO);
						String audio = downloadTask(jsonObject.getString(WeatherProperty.AUDIO), String.valueOf(cityid) + ".wav");
						String createdAt = jsonObject.getString(WeatherProperty.CREATED_AT);
						String updatedAt = jsonObject.getString(WeatherProperty.UPDATED_AT);	
						
						Weather weather = new Weather(cityid, city, stno, time, memo, audio, createdAt, updatedAt);
						DaoFactory.getSQLiteDaoFactory().createWeatherDao(GetWeatherDataService.this).update(weather);
						
						double prograss = (100 / (double)response.length()) * (i + 1);
						
//						Intent intent = new Intent(GetWeatherDataService.this, ServerResponseReceiver.class);
						Intent intent = new Intent();
						intent.setAction(ServerResponseReceiver.ACTION_GET_WEATHER_DATE_PROGRASS_UPDATE);
						intent.putExtra("progress", prograss);
						sendBroadcast(intent);
					}		
					
//					Intent intent = new Intent(GetWeatherDataService.this, ServerResponseReceiver.class);
					Intent intent = new Intent();
					intent.setAction(ServerResponseReceiver.ACTION_GET_WEATHER_DATE_FINISH);
					sendBroadcast(intent);
				} 
				catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				Log.i(TAG, throwable.toString());	
//				Intent intent = new Intent(GetWeatherDataService.this, ServerResponseReceiver.class);
				Intent intent = new Intent();
				intent.setAction(ServerResponseReceiver.ACTION_GET_WEATHER_DATE_FAILURE);
				sendBroadcast(intent);
			}			
        });	
	}
	
	private String downloadTask(String url, final String fileName) {
		final String[] allowedContentTypes = {"audio/x-wav", "video/x-flv"};
		
		NoMissingHttpClient.setAsync(false);
        NoMissingHttpClient.download(url, new BinaryHttpResponseHandler(allowedContentTypes) {
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] response) {
				File dirFile = getDir("weather", Context.MODE_PRIVATE);
				File file = new File(dirFile, fileName);
				
				try {
					FileOutputStream fileOutputStream = new FileOutputStream(file);
					fileOutputStream.write(response);
					fileOutputStream.flush();
					fileOutputStream.close();
					
					audioUri = file.toURI().toString();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] response, Throwable throwable) {
				Log.i(TAG, throwable.toString());
			}
		});	
        
        return audioUri;
	}

}
