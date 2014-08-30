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
import edu.ntust.cs.idsl.nomissing.http.parameter.TTSConvertTextParameter;
import edu.ntust.cs.idsl.nomissing.http.response.TTSConvertTextProperty;
import edu.ntust.cs.idsl.nomissing.receiver.TTSServiceReceiver;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
public class TTSConvertTextService extends TextToSpeechService {

    private static final String TAG = TTSConvertTextService.class.getSimpleName();
    private static final String ACTION = "edu.ntust.cs.idsl.nomissing.action.CONVERT_TEXT";

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
        String ttsText = extras.getString(EXTRA_TTS_TEXT);
        String ttsSpeaker = extras.getString(EXTRA_TTS_SPEAKER);
        int volume = extras.getInt(EXTRA_TTS_VOLUME);
        int speed = extras.getInt(EXTRA_TTS_SPEED);
        String outputType = extras.getString(EXTRA_TTS_OUTPUT_TYPE);

        RequestParams params = new RequestParams();
        params.add(TTSConvertTextParameter.TTS_TEXT, ttsText);
        params.add(TTSConvertTextParameter.TTS_SPEAKER, ttsSpeaker);
        params.add(TTSConvertTextParameter.VOLUME, Integer.toString(volume));
        params.add(TTSConvertTextParameter.SPEED, Integer.toString(speed));
        params.add(TTSConvertTextParameter.OUTPUT_TYPE, outputType);

        NoMissingHttpClient.setAsync(false);
        NoMissingHttpClient.ttsConvertText(app.getSettings().getUUID(), app.getSettings().getAccessToken(), params,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.v(TAG, response.toString());

                        try {
                            String convertID = response.getString(TTSConvertTextProperty.RESULT_CONVERT_ID);
                            extras.putString(EXTRA_CONVERT_ID, convertID);
                            sendBroadcast(TTSServiceReceiver.getActionReceiveTTSServiceStart(TTSConvertTextService.this));

                            if (successor != null)
                                successor.startAction(TTSConvertTextService.this, extras);
                        } catch (JSONException e) {
                            Log.e(TAG, e.toString());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.e(TAG, errorResponse.toString());
                        sendBroadcast(TTSServiceReceiver.getActionReceiveTTSServiceFailure(TTSConvertTextService.this));
                    }
                });
    }

    @Override
    protected void startAction(Context context, Bundle extras) {
        Intent intent = new Intent(context, TTSConvertTextService.class);
        intent.setAction(ACTION);
        intent.putExtras(extras);
        context.startService(intent);
    }
}
