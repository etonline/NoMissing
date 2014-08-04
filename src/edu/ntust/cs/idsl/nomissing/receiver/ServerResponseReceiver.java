package edu.ntust.cs.idsl.nomissing.receiver;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import edu.ntust.cs.idsl.nomissing.dao.ChimeDAO;
import edu.ntust.cs.idsl.nomissing.http.NoMissingResultCode;
import edu.ntust.cs.idsl.nomissing.model.Chime;
import edu.ntust.cs.idsl.nomissing.service.TTSGetConvertStatusService;
import edu.ntust.cs.idsl.nomissing.util.ToastMaker;

public class ServerResponseReceiver extends BroadcastReceiver {
	
	public static final String TAG = ServerResponseReceiver.class.getSimpleName();
	
	public static final String ACTION_TTS_CONVERT_TEXT_RESPONSE = "edu.ntust.cs.idsl.nomissing.action.TTS_CONVERT_CONVERT_RESPONSE";
	public static final String ACTION_TTS_GET_CONVERT_STATUS_RESPONSE = "edu.ntust.cs.idsl.nomissing.action.TTS_CONVERT_CONVERT_RESPONSE";
	
	public ServerResponseReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		
		if (intent.getAction().equals(ACTION_TTS_CONVERT_TEXT_RESPONSE)) {
			int chimeID = intent.getIntExtra("chimeID", -1);
			String response = intent.getStringExtra("response");
			Log.v(TAG, response);
			
			try {
				JSONObject jsonObject = new JSONObject(response);
				String convertID = jsonObject.getString("resultConvertID");
				int resultCode = Integer.valueOf(jsonObject.getString("resultCode"));
				
				if (resultCode == NoMissingResultCode.CONVERT_TEXT_SUCCESS) {
					Intent newIntent = new Intent(context, TTSGetConvertStatusService.class);
					newIntent.setAction(TTSGetConvertStatusService.ACTION_GET_CONVERT_STATUS);
					newIntent.putExtra("chimeID", chimeID);
					newIntent.putExtra("convertID", convertID);
					context.startService(newIntent);				
				} else {
					ToastMaker.toast(context, "�y���ɲ��ͥ���");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}			
		}
		
		if (intent.getAction().equals(ACTION_TTS_GET_CONVERT_STATUS_RESPONSE)) {
			int chimeID = intent.getIntExtra("chimeID", -1);
			String convertID = intent.getStringExtra("convertID");
			String response = intent.getStringExtra("response");
			Log.v(TAG, response);			
			
			try {
				JSONObject jsonObject = new JSONObject(response);
				int statusCode = Integer.valueOf(jsonObject.getString("statusCode"));
				
				if (statusCode != NoMissingResultCode.CONVERT_STATUS_COMPLETED) {
					Intent newIntent = new Intent(context, TTSGetConvertStatusService.class);
					newIntent.setAction(TTSGetConvertStatusService.ACTION_GET_CONVERT_STATUS);
					newIntent.putExtra("chimeID", chimeID);
					newIntent.putExtra("convertID", convertID);
					context.startService(newIntent);			
				} else {
					String audio = jsonObject.getString("audio");
					ChimeDAO chimeDAO = ChimeDAO.getInstance(context);
					Chime chime = chimeDAO.find(chimeID);
					chime.setAudio(audio);
					chimeDAO.update(chime);
					
					ToastMaker.toast(context, audio);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}				
		}
	}
	
}
