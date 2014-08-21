package edu.ntust.cs.idsl.nomissing.service;

import java.util.HashMap;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import edu.ntust.cs.idsl.nomissing.dao.SQLiteDao;
import edu.ntust.cs.idsl.nomissing.dao.SQLiteDaoFactory;
import edu.ntust.cs.idsl.nomissing.dao.WeatherDao;
import edu.ntust.cs.idsl.nomissing.global.NoMissingApp;
import edu.ntust.cs.idsl.nomissing.http.NoMissingHttpClient;
import edu.ntust.cs.idsl.nomissing.http.NoMissingRoute;
import edu.ntust.cs.idsl.nomissing.model.Weather;

public class GetWeatherDataService extends IntentService {
	
	private static final String TAG = "GetWeatherDataService";
	private NoMissingApp app;
	private String username;
	private String password;
	
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
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		NoMissingHttpClient.getInstance(false);
		NoMissingHttpClient.get(NoMissingRoute.WEATHER, username, password, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
				Log.v(TAG, response.toString());	
				
				JSONObject jsonObject;
				try {
					for (int i = 0; i < response.length(); i++) {
						jsonObject = response.getJSONObject(i);
						
						int cityid = Integer.parseInt(jsonObject.getString("cityid"));
						String city = jsonObject.getString("name");
						String stno = jsonObject.getString("stno");
						String time = jsonObject.getString("time");
						String memo = jsonObject.getString("memo");
						String audio = jsonObject.getString("audio");
						String createdAt = jsonObject.getString("created_at");
						String updatedAt = jsonObject.getString("updated_at");	
						
						Weather weather = new Weather(cityid, city,stno,time,
								memo,audio,createdAt,updatedAt);
						
						SQLiteDaoFactory.getWeatherDao(getApplicationContext()).update(weather);	
					}
				} 
				catch (JSONException e) {
					e.printStackTrace();
				}
			}
        });			
	}

}
