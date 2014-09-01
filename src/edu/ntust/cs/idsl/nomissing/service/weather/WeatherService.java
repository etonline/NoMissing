package edu.ntust.cs.idsl.nomissing.service.weather;

import android.app.IntentService;
import android.content.Context;
import android.os.Bundle;
import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.constant.Requester;
import edu.ntust.cs.idsl.nomissing.global.NoMissingApp;
import edu.ntust.cs.idsl.nomissing.service.RegistrationService;
import edu.ntust.cs.idsl.nomissing.service.tts.TTSGetAudioService;
import edu.ntust.cs.idsl.nomissing.util.ToastMaker;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
public abstract class WeatherService extends IntentService {

    protected WeatherService successor;
    protected NoMissingApp app;

    public WeatherService() {
        super("GetWeatherDataService");
    }

    public static boolean startService(Context context, Bundle extras) {
        boolean isStart = false;

        NoMissingApp app = (NoMissingApp) context.getApplicationContext();
        
        if (!app.isNetworkAvailable()) {
            ToastMaker.toast(context, R.string.toast_network_inavailable);
        }
        
        else if (!app.isRegistered()) {
            RegistrationService.startService(context, Requester.WEATHER_SERVICE, extras);
        }
        
        else {
            new WeatherDataService().startAction(context, extras);
            isStart = true;
        }
        
        return isStart;        
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = (NoMissingApp) getApplicationContext();

        if (getClass().equals(WeatherDataService.class))
            successor = new WeatherAudioService();

        if (getClass().equals(TTSGetAudioService.class))
            successor = null;
    }

    protected WeatherService getSuccessor() {
        return successor;
    }

    protected void setSuccessor(WeatherService successor) {
        this.successor = successor;
    }

    protected abstract void handleAction(Bundle extras);

    protected abstract void startAction(Context context, Bundle extras);

}
