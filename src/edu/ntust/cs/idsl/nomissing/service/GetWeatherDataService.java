package edu.ntust.cs.idsl.nomissing.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

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
import edu.ntust.cs.idsl.nomissing.dao.sqlite.SQLiteDaoFactory;
import edu.ntust.cs.idsl.nomissing.global.NoMissingApp;
import edu.ntust.cs.idsl.nomissing.http.NoMissingHttpClient;
import edu.ntust.cs.idsl.nomissing.http.NoMissingRoute;
import edu.ntust.cs.idsl.nomissing.model.Weather;
import edu.ntust.cs.idsl.nomissing.util.ToastMaker;

public class GetWeatherDataService extends IntentService {
	
	private static final String TAG = "GetWeatherDataService";
	private NoMissingApp app;
	private String username;
	private String password;
	
	private static String audioUri;
	
	public GetWeatherDataService() {
		super("GetWeatherDataService");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		app = (NoMissingApp)getApplicationContext();
		
		HashMap<String, String> user = app.session.getUserData();
		username = user.get("username");
		password = user.get("password");
		
		ToastMaker.toast(this, R.string.toast_refresh_weather_data);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		ToastMaker.toast(this, R.string.toast_refresh_weather_data_completed);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		NoMissingHttpClient.getInstance(false);
		NoMissingHttpClient.get(NoMissingRoute.WEATHER, username, password, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
				Log.v(TAG, response.toString());	
								
				try {
					for (int i = 0; i < response.length(); i++) {
						JSONObject jsonObject = response.getJSONObject(i);
						
						int cityid = Integer.parseInt(jsonObject.getString("cityid"));
						String city = jsonObject.getString("name");
						String stno = jsonObject.getString("stno");
						String time = jsonObject.getString("time");
						String memo = jsonObject.getString("memo");
						String audio = downloadTask(jsonObject.getString("audio"), String.valueOf(cityid) + ".wav");
						String createdAt = jsonObject.getString("created_at");
						String updatedAt = jsonObject.getString("updated_at");	
						
						Weather weather = new Weather(cityid, city,stno,time,
								memo,audio,createdAt,updatedAt);
						
						SQLiteDaoFactory.createWeatherDao(GetWeatherDataService.this).update(weather);
					}		
				} 
				catch (JSONException e) {
					e.printStackTrace();
				}
			}
        });	
	}
	
	private String downloadTask(String url, final String fileName) {
		final String[] allowedContentTypes = {"audio/x-wav"};
		
		NoMissingHttpClient.getInstance(false);
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
