package edu.ntust.cs.idsl.nomissing.preference;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import edu.ntust.cs.idsl.nomissing.activity.LoginActivity;

public class SessionManager {

	private static SessionManager instance;

	private Context context;
	private SharedPreferences pref;
	private Editor editor;

	private int PRIVATE_MODE = 0;

	private static final String PREF_NAME = "SessionPref";
	private static final String IS_LOGIN = "IsLoggedIn";
	public static final String KEY_USERNAME = "username";
	public static final String KEY_PASSWORD = "password";

	private SessionManager(Context context) {
		this.context = context;
		pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

	public static synchronized SessionManager getInstance(Context context) {
		if (instance == null) {
			instance = new SessionManager(context);
		}
		return instance;
	}
	
	/**
	 * Create login session
	 * 
	 * @param username
	 * @param password
	 */
	public void createLoginSession(String username, String password) {
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);

		// Storing name in pref
		editor.putString(KEY_USERNAME, username);

		// Storing email in pref
		editor.putString(KEY_PASSWORD, password);

		// commit changes
		editor.commit();
	}
	
	/**
	 * Check login method wil check user login status If false it will redirect
	 * user to login page Else won't do anything
	 */
	public void checkLogin() {
		if (!this.isLoggedIn()) {
			Intent i = new Intent(context, LoginActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		}
	}

	/**
	 * Get stored session data
	 * 
	 * @return HashMap
	 */
	public HashMap<String, String> getUserData() {
		HashMap<String, String> user = new HashMap<String, String>();
		user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));
		user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));

		return user;
	}
	
	/**
	 * Clear session details
	 */
	public void logoutUser() {
		// Clearing all data from Shared Preferences
		editor.clear();
		editor.commit();

		// After logout redirect user to Loing Activity
		Intent i = new Intent(context, LoginActivity.class);
		// Closing all the Activities
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		// Add new Flag to start new Activity
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		// Staring Login Activity
		context.startActivity(i);
	}
	
	/**
	 * Quick check for login
	 * Get Login State
	 * 
	 * @return boolean
	 */
	public boolean isLoggedIn() {
		return pref.getBoolean(IS_LOGIN, false);
	}
}
