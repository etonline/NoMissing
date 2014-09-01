package edu.ntust.cs.idsl.nomissing.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import edu.ntust.cs.idsl.nomissing.constant.Requester;
import edu.ntust.cs.idsl.nomissing.global.NoMissingApp;
import edu.ntust.cs.idsl.nomissing.service.tts.TextToSpeechService;
import edu.ntust.cs.idsl.nomissing.service.weather.WeatherService;

public class RegistrationReceiver extends BroadcastReceiver {

    private static final String TAG = RegistrationReceiver.class.getSimpleName();
    private static final String ACTION = "edu.ntust.cs.idsl.nomissing.action.RECEIVE_REGISTRATION"; 
    
    private static final String EXTRA_REQUESTER = "edu.ntust.cs.idsl.nomissing.extra_REQUESTER";
    private static final String EXTRA_ACCESS_TOKEN = "edu.ntust.cs.idsl.nomissing.extra_ACCESS_TOKEN";
    
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            Log.v(TAG, intent.getAction());
            final String action = intent.getAction();

            if (ACTION.equals(action)) {
                final String accessToken = intent.getStringExtra(EXTRA_ACCESS_TOKEN);
                final Requester requester = (Requester)intent.getSerializableExtra(EXTRA_REQUESTER);
                final Bundle extras = intent.getExtras();
                handleAction(context, requester, accessToken, extras);
            }
        }
    }
    
    private void handleAction(Context context, Requester requester, String accessToken, Bundle extras) {
        NoMissingApp app = (NoMissingApp)context.getApplicationContext();
        app.setRegistered(accessToken);
        
        if (Requester.NOMISSING_APP.equals(requester)) {
            return;
        }
        
        if (Requester.TTS_SERVICE.equals(requester)) {
            TextToSpeechService.startService(context, extras);
            return;
        }
        
        if (Requester.WEATHER_SERVICE.equals(requester)) {
            WeatherService.startService(context, extras);
            return;
        }
    }
    
    public static Intent getAction(Context context, String accessToken, Requester requester, Bundle extras) {
        Intent intent = new Intent(context, RegistrationReceiver.class);
        intent.setAction(ACTION);
        intent.putExtra(EXTRA_ACCESS_TOKEN, accessToken);
        intent.putExtra(EXTRA_REQUESTER, requester);
        intent.putExtras(extras);
        return intent;
    }

}
