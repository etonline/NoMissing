package edu.ntust.cs.idsl.nomissing.global;

import edu.ntust.cs.idsl.nomissing.pref.SessionManager;
import edu.ntust.cs.idsl.nomissing.pref.UserSettings;
import android.app.Application;
import android.util.Log;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
public class NoMissingApp extends Application {
	
	public SessionManager session;
	public UserSettings userSettings;

	@Override
	public void onCreate() {
		super.onCreate();
		startSession();
		userSettings = UserSettings.getInstance(getApplicationContext());
	}

	public void startSession() {
		session = SessionManager.getInstance(getApplicationContext());
		Log.d("SessionManager", "Start Session");
	}
}
