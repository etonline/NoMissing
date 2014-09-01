package edu.ntust.cs.idsl.nomissing.service;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import edu.ntust.cs.idsl.nomissing.constant.Requester;
import edu.ntust.cs.idsl.nomissing.global.NoMissingApp;
import edu.ntust.cs.idsl.nomissing.http.NoMissingHttpClient;
import edu.ntust.cs.idsl.nomissing.http.parameter.RegisterParameter;
import edu.ntust.cs.idsl.nomissing.http.response.RegisterProperty;
import edu.ntust.cs.idsl.nomissing.http.resultcode.RegisterResultCode;
import edu.ntust.cs.idsl.nomissing.receiver.RegistrationReceiver;

public class RegistrationService extends IntentService {

    private static final String TAG = RegistrationService.class.getSimpleName();
    private static final String ACTION = "edu.ntust.cs.idsl.nomissing.action.REGISTER";
    private static final String EXTRA_REQUESTER = "edu.ntust.cs.idsl.nomissing.extra_REQUESTER";

    public static final String REQUESTER_APP = "NoMissingApp";
    public static final String REQUESTER_TTS = "TextToSpeechService";
    public static final String REQUESTER_WEATHER = "WeatherService";

    private NoMissingApp app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = (NoMissingApp) getApplicationContext();
    }

    public RegistrationService() {
        super("RegistrationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION.equals(action)) {
                final Requester requster = (Requester) intent.getSerializableExtra(EXTRA_REQUESTER);
                final Bundle extras = intent.getExtras();
                handleAction(requster, extras);
            }
        }
    }

    private void handleAction(final Requester requester, final Bundle extras) {
        RequestParams params = new RequestParams();
        params.add(RegisterParameter.UUID, app.getSettings().getUUID());

        NoMissingHttpClient.setAsync(false);
        NoMissingHttpClient.register(params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                    JSONObject response) {
                Log.i(NoMissingHttpClient.TAG, response.toString());

                try {
                    int code = response.getInt(RegisterProperty.CODE);
                    String message = response
                            .getString(RegisterProperty.MESSAGE);

                    switch (code) {
                    case RegisterResultCode.SUCCESS:
                        String accessToken = response
                                .getString(RegisterProperty.ACCESS_TOKEN);
                        sendBroadcast(RegistrationReceiver.getAction(
                                RegistrationService.this, accessToken,
                                requester, extras));
                        break;

                    case RegisterResultCode.INVALID_REGISTRATION:
                        Log.e(TAG, message);
                        break;

                    case RegisterResultCode.UUID_ALREADY_IN_USE:
                        Log.e(TAG, message);
                        break;
                    }
                } catch (JSONException e) {
                    Log.i(TAG, e.toString());
                }
            }
        });
    }

    public static void startService(Context context, Requester requester,
            Bundle extras) {
        Intent intent = new Intent(context, RegistrationService.class);
        intent.setAction(ACTION);
        intent.putExtra(EXTRA_REQUESTER, requester);
        intent.putExtras(extras);
        context.startService(intent);
    }

}
