package edu.ntust.cs.idsl.nomissing.service;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import edu.ntust.cs.idsl.nomissing.global.NoMissingApp;
import edu.ntust.cs.idsl.nomissing.http.NoMissingHttpClient;
import edu.ntust.cs.idsl.nomissing.http.parameter.RegisterParameter;
import edu.ntust.cs.idsl.nomissing.http.response.RegisterProperty;
import edu.ntust.cs.idsl.nomissing.http.resultcode.RegisterResultCode;

public class RegistrationService extends IntentService {
    
    private static final String TAG = RegistrationService.class.getSimpleName();
    private static final String ACTION = "edu.ntust.cs.idsl.nomissing.action.REGISTER";
    private static final String EXTRA_UUID = "edu.ntust.cs.idsl.nomissing.extra_UUID";
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
                final String uuid = intent.getStringExtra(EXTRA_UUID);
                handleAction(uuid);
            }
        }
    }
    
    private void handleAction(String uuid) {
        RequestParams params = new RequestParams();
        params.add(RegisterParameter.UUID, uuid);

        NoMissingHttpClient.setAsync(false);
        NoMissingHttpClient.register(params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i(NoMissingHttpClient.TAG, response.toString());

                try {
                    int code = response.getInt(RegisterProperty.CODE);
                    String message = response.getString(RegisterProperty.MESSAGE);

                    switch (code) {
                    case RegisterResultCode.SUCCESS:
                        String accessToken = response.getString(RegisterProperty.ACCESS_TOKEN);
                        app.getSettings().setAccessToken(accessToken);
                        app.getSettings().setRegistered(true);
                        break;

                    case RegisterResultCode.INVALID_REGISTRATION:
                        Log.e(TAG, message);
                        break;

                    case RegisterResultCode.UUID_ALREADY_IN_USE:
                        Log.e(TAG, message);
                        break;
                    }
                } catch (JSONException e) {
                    Log.i(NoMissingHttpClient.TAG, e.toString());
                }
            }
        });        
    }
    
    public static void startService(Context context, String uuid) {
        Intent intent = new Intent(context, RegistrationService.class);
        intent.setAction(ACTION);
        intent.putExtra(EXTRA_UUID, uuid);
        context.startService(intent);
    }

}
