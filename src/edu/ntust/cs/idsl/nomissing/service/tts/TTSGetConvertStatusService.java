package edu.ntust.cs.idsl.nomissing.service.tts;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import edu.ntust.cs.idsl.nomissing.http.NoMissingHttpClient;
import edu.ntust.cs.idsl.nomissing.http.parameter.TTSGetConvertStatusParameter;
import edu.ntust.cs.idsl.nomissing.http.response.TTSGetConvertStatusProperty;
import edu.ntust.cs.idsl.nomissing.http.resultcode.TTSGetConvertStatusResultCode;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
public class TTSGetConvertStatusService extends TextToSpeechService {

	private static final String TAG = TTSGetConvertStatusService.class.getSimpleName();	
	private static final String ACTION = "edu.ntust.cs.idsl.nomissing.action.GET_CONVERT_STATUS";
	
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
		String convertID = extras.getString(EXTRA_CONVERT_ID);
		
		RequestParams params = new RequestParams();
        params.add(TTSGetConvertStatusParameter.CONVERT_ID, convertID);
		
        NoMissingHttpClient.setAsync(false);
		NoMissingHttpClient.ttsGetConvertStatus(app.getSettings().getUUID(), app.getSettings().getAccessToken(), params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				Log.v(TAG, response.toString());	
				
				try {
					int status = Integer.valueOf(response.getString(TTSGetConvertStatusProperty.STATUS_CODE));
					if (status == TTSGetConvertStatusResultCode.CONVERT_STATUS_COMPLETED) {
						String audioURL = response.getString(TTSGetConvertStatusProperty.RESULT_URL);
						extras.putString(EXTRA_AUDIO_URL, audioURL);
						
						if (getSuccessor() != null) 
							getSuccessor().startAction(TTSGetConvertStatusService.this, extras);
					} else {
						handleAction(extras);
					}
				} catch (JSONException e) {
					Log.e(TAG, e.toString());	
				}
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				Log.e(TAG, errorResponse.toString());
			}
        });	
	}	
	
	@Override
	protected void startAction(Context context, Bundle extras) {
		Intent intent = new Intent(context, TTSGetConvertStatusService.class);
		intent.setAction(ACTION);
		intent.putExtras(extras);
		context.startService(intent);
	}
	
}
