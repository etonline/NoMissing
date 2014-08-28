package edu.ntust.cs.idsl.nomissing.service.tts;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.Header;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.loopj.android.http.BinaryHttpResponseHandler;

import edu.ntust.cs.idsl.nomissing.dao.DaoFactory;
import edu.ntust.cs.idsl.nomissing.http.NoMissingHttpClient;
import edu.ntust.cs.idsl.nomissing.model.Chime;
import edu.ntust.cs.idsl.nomissing.model.Reminder;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
public class GetAudioFileService extends TextToSpeechService {

	private static final String TAG = GetAudioFileService.class.getSimpleName();
	private static final String ACTION = "edu.ntust.cs.idsl.nomissing.action.GET_CONVERT_STATUS";
	private static final  String[] allowedContentTypes = {"audio/x-wav"};
	
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
	protected void handleAction(final Bundle extras) {
		final String audioURL = extras.getString(EXTRA_AUDIO_URL);
		final String category = extras.getString(EXTRA_CATEGORY);
		final long entityID = extras.getLong(EXTRA_ENTITY_ID);
		
        NoMissingHttpClient.setAsync(false);
        NoMissingHttpClient.download(audioURL, new BinaryHttpResponseHandler(allowedContentTypes) {
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] response) {
				String audio = saveAudio(category, entityID, response);
				setAudio(category, entityID, audio);
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] response, Throwable throwable) {
				Log.i(TAG, throwable.toString());
			}
		});
	}
	
	@Override
	protected void startAction(Context context, Bundle extras) {
		Intent intent = new Intent(context, GetAudioFileService.class);
		intent.setAction(ACTION);
		intent.putExtras(extras);
		context.startService(intent);
	}
	
	private String saveAudio(String category, long entityID, byte[] response) {
		File dirFile = getDir(category, Context.MODE_PRIVATE);
		File file = new File(dirFile, String.valueOf(entityID));
		
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
	
	
	private void setAudio(String category, long id, String audio) {
		if (category.equals(CATEGORY_REMINDER)) {
			Reminder reminder = DaoFactory.getSQLiteDaoFactory().createReminderDao(this).find(id);
			reminder.setAudio(audio);
			DaoFactory.getSQLiteDaoFactory().createReminderDao(this).update(reminder);
		}
		
		if (category.equals(CATEGORY_CHIME)) {
			Chime chime = DaoFactory.getSQLiteDaoFactory().createChimeDao(this).find((int)id);
			chime.setAudio(audio);
			DaoFactory.getSQLiteDaoFactory().createChimeDao(this).update(chime);
		}
	}

}
