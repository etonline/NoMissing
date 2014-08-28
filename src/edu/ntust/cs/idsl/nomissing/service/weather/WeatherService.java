package edu.ntust.cs.idsl.nomissing.service.weather;

import android.app.IntentService;
import android.content.Context;
import android.os.Bundle;
import edu.ntust.cs.idsl.nomissing.global.NoMissingApp;
import edu.ntust.cs.idsl.nomissing.service.tts.GetAudioFileService;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
public abstract class WeatherService extends IntentService {
	
	protected WeatherService successor;
	protected NoMissingApp app; 
	
	public WeatherService() {
		super("GetWeatherDataService");
	}
	
	public static void startService(Context context, Bundle extras) {
		new GettingDataService().startAction(context, extras);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		app = (NoMissingApp)getApplicationContext();		
		
		if (getClass().equals(GettingDataService.class))
			successor = new GettingAudioService();
		
		if (getClass().equals(GetAudioFileService.class))
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
