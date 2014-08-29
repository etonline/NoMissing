package edu.ntust.cs.idsl.nomissing.service.weather;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.http.Header;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.loopj.android.http.BinaryHttpResponseHandler;

import edu.ntust.cs.idsl.nomissing.dao.DaoFactory;
import edu.ntust.cs.idsl.nomissing.http.NoMissingHttpClient;
import edu.ntust.cs.idsl.nomissing.model.Weather;
import edu.ntust.cs.idsl.nomissing.receiver.ServerResponseReceiver;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
public class WeatherAudioService extends WeatherService {
	
	private static final String TAG = WeatherAudioService.class.getSimpleName();
	private static final String ACTION = "edu.ntust.cs.idsl.nomissing.action.GettingDataService";
	private static final String[] allowedContentTypes = {"audio/x-wav"};
	private static final String DIR = "weather";
	private static int count;
	
	@Override
	protected void onHandleIntent(Intent intent) {
		if (intent != null) {
			final String action = intent.getAction();
			if (ACTION.equals(action)) {		
				handleAction(intent.getExtras());
			}
		}
	}
	
	@Override
	protected void handleAction(Bundle extras) {
		final List<Weather> weatherList = DaoFactory.getSQLiteDaoFactory().createWeatherDao(this).findAll();
		count = 0;
		
		for (final Weather weather : weatherList) {
			String audioURL = weather.getAudio();
			count++;
			
			NoMissingHttpClient.setAsync(false);
	        NoMissingHttpClient.download(audioURL, new BinaryHttpResponseHandler(allowedContentTypes) {
				@Override
				public void onSuccess(int statusCode, Header[] headers, byte[] response) {
					String audio = saveFile(weather, response);
					setAudio(weather, audio);
					
					double prograss = 100 / (double) weatherList.size() * (double) count;
					updateProgress(prograss);
				}
				
				@Override
				public void onFailure(int statusCode, Header[] headers, byte[] response, Throwable throwable) {
					Log.i(TAG, throwable.toString());
					stopProgress();
				}
			});				
		}
		
		finishProgress();
		
		if (successor != null) 
			successor.startAction(this, extras);	
	}

	@Override
	protected void startAction(Context context, Bundle extras) {
		Intent intent = new Intent(context, WeatherAudioService.class);
		intent.setAction(ACTION);
		context.startService(intent);
	}		
	
	
	private String saveFile(Weather weather, byte[] response) {
		File dirFile = getDir(DIR, Context.MODE_PRIVATE);
		File file = new File(dirFile, String.valueOf(weather.getCityID()));
		
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(response);
			fileOutputStream.flush();
			fileOutputStream.close();
		} catch (IOException e) {
			Log.e(TAG, e.toString());
		}	
		
		return file.toURI().toString();
	}
	
	private void setAudio(Weather weather, String audio) {
		weather.setAudio(audio);
		DaoFactory.getSQLiteDaoFactory().createWeatherDao(this).update(weather);
	}
		
	private void updateProgress(double prograss) {
//		Intent intent = new Intent(GetWeatherDataService.this, ServerResponseReceiver.class);
		Intent intent = new Intent();
		intent.setAction(ServerResponseReceiver.ACTION_GET_WEATHER_DATE_PROGRASS_UPDATE);
		intent.putExtra("progress", prograss);
		sendBroadcast(intent);		
	}
	
	private void finishProgress() {
//		Intent intent = new Intent(GetWeatherDataService.this, ServerResponseReceiver.class);
		Intent intent = new Intent();
		intent.setAction(ServerResponseReceiver.ACTION_GET_WEATHER_DATE_FINISH);
		sendBroadcast(intent);		
	}
	
	private void stopProgress() {
//		Intent intent = new Intent(GetWeatherDataService.this, ServerResponseReceiver.class);
		Intent intent = new Intent();
		intent.setAction(ServerResponseReceiver.ACTION_GET_WEATHER_DATE_FAILURE);
		sendBroadcast(intent);		
	}

}
