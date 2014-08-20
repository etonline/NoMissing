package edu.ntust.cs.idsl.nomissing.global;

import edu.ntust.cs.idsl.nomissing.pref.SessionManager;
import edu.ntust.cs.idsl.nomissing.pref.SettingsManager;
import android.accounts.Account;
import android.app.Application;
import android.util.Log;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
public class NoMissingApp extends Application {
	
	public Account account;
	public SessionManager session;
	public SettingsManager userSettings;

	@Override
	public void onCreate() {
		super.onCreate();
		startSession();
		userSettings = SettingsManager.getInstance(getApplicationContext());
	}
	
	public void startSession() {
		session = SessionManager.getInstance(getApplicationContext());
		Log.d("SessionManager", "Start Session");
	}
	
	public void account() {
		
	}
}
