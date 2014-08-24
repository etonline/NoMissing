package edu.ntust.cs.idsl.nomissing.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import edu.ntust.cs.idsl.nomissing.dao.ISimpleDao;
import edu.ntust.cs.idsl.nomissing.dao.sqlite.SQLiteDaoFactory;
import edu.ntust.cs.idsl.nomissing.global.NoMissingApp;
import edu.ntust.cs.idsl.nomissing.http.NoMissingHttpClient;
import edu.ntust.cs.idsl.nomissing.model.Chime;
import edu.ntust.cs.idsl.nomissing.model.Weather;
import edu.ntust.cs.idsl.nomissing.receiver.ServerResponseReceiver;
import edu.ntust.cs.idsl.nomissing.util.FileUtil;
import edu.ntust.cs.idsl.nomissing.util.ToastMaker;

public class GetAudioFileService extends IntentService {
	
	public static String ACTION_GET_AUDIO_FILE = "edu.ntust.cs.idsl.nomissing.action.GET_AUDIO_FILE";
	public static final String TAG = GetAudioFileService.class.getSimpleName();
	private final static String[] allowedContentTypes = {"audio/x-wav"};
	
	private NoMissingApp app;
	
	private int id;
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
			Log.v(TAG, "start");
			
			id = intent.getIntExtra("id", 0);
			category = intent.getStringExtra("category");
			url = intent.getStringExtra("url");
			Log.i(TAG, url);
			
	        NoMissingHttpClient.getInstance(false);
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
	
	private void setAudio(String category, int id, String audio) {
		if (category.equals("chime")) {
			Chime chime = SQLiteDaoFactory.createChimeDao(this).find(id);
			chime.setAudio(audio);
			SQLiteDaoFactory.createChimeDao(this).update(chime);
		}
		
		Log.i(TAG, "complete");
	}

}
