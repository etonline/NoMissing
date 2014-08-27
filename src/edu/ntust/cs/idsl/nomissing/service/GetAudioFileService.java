package edu.ntust.cs.idsl.nomissing.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.Header;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.loopj.android.http.BinaryHttpResponseHandler;

import edu.ntust.cs.idsl.nomissing.dao.DaoFactory;
import edu.ntust.cs.idsl.nomissing.global.NoMissingApp;
import edu.ntust.cs.idsl.nomissing.http.NoMissingHttpClient;
import edu.ntust.cs.idsl.nomissing.model.Chime;
import edu.ntust.cs.idsl.nomissing.model.Reminder;
import edu.ntust.cs.idsl.nomissing.receiver.ServerResponseReceiver;

public class GetAudioFileService extends IntentService {
	
	public static String ACTION_GET_AUDIO_FILE = "edu.ntust.cs.idsl.nomissing.action.GET_AUDIO_FILE";
	public static final String TAG = GetAudioFileService.class.getSimpleName();
	private final static String[] allowedContentTypes = {"audio/x-wav"};
	
	private NoMissingApp app;
	
	private long id;
	private String category;
	private String url;
	
	
	
	public GetAudioFileService() {
		super("DownloadFileService");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		app = (NoMissingApp)getApplicationContext();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (intent.getAction().equals(ACTION_GET_AUDIO_FILE)) {
			id = intent.getLongExtra("id", 0);
			category = intent.getStringExtra("category");
			url = intent.getStringExtra("url");
			Log.i(TAG, url);
			
	        NoMissingHttpClient.setAsync(false);
	        NoMissingHttpClient.download(url, new BinaryHttpResponseHandler(allowedContentTypes) {
				@Override
				public void onSuccess(int statusCode, Header[] headers, byte[] response) {
					File dirFile = getDir(category, Context.MODE_PRIVATE);
					File file = new File(dirFile, String.valueOf(id) + ".wav");
					
					try {
						FileOutputStream fileOutputStream = new FileOutputStream(file);
						fileOutputStream.write(response);
						fileOutputStream.flush();
						fileOutputStream.close();
						
						setAudio(category, id, file.toURI().toString());
						
						Intent newIntent = new Intent(GetAudioFileService.this, ServerResponseReceiver.class);
						newIntent.setAction(ServerResponseReceiver.ACTION_GET_AUDIO_FILE_RESPONSE);
						newIntent.putExtra("audio", file.toURI().toString());
						sendBroadcast(newIntent);
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
				
				@Override
				public void onFailure(int statusCode, Header[] headers, byte[] response, Throwable throwable) {
					Log.i(TAG, throwable.toString());
				}
			});
		}
	}
	
	private void setAudio(String category, long id, String audio) {
		if (category.equals("reminder")) {
			Reminder reminder = DaoFactory.getSQLiteDaoFactory().createReminderDao(this).find(id);
			reminder.setAudio(audio);
			DaoFactory.getSQLiteDaoFactory().createReminderDao(this).update(reminder);
		}
		
		if (category.equals("chime")) {
			Chime chime = DaoFactory.getSQLiteDaoFactory().createChimeDao(this).find((int)id);
			chime.setAudio(audio);
			DaoFactory.getSQLiteDaoFactory().createChimeDao(this).update(chime);
		}
	}

}
