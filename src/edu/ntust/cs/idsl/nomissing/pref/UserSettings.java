package edu.ntust.cs.idsl.nomissing.pref;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import edu.ntust.cs.idsl.nomissing.model.City;

public class UserSettings {
	
	private static UserSettings instance;
	
	private Context context;
	private SharedPreferences pref;
	private Editor editor;

	private int PRIVATE_MODE = 0;

	private static final String PREF_NAME = "UserSettingsPref";

	private static final String KEY_TTS_SPEAKER = "tts_speaker";
	private static final String KEY_TTS_VOLUME = "tts_volume";
	private static final String KEY_TTS_SPEED = "tts_speed";
	private static final String KEY_WEATHER_TTS_ENABLED = "weather_tts_enabled";
	private static final String KEY_WEATHER_REMINDER_ENABLED = "weather_reminder_enabled";
	private static final String KEY_WEATHER_REMINDER_TIME = "weather_reminder_time";
	private static final String KEY_WEATHER_REMINDER_CITY = "weather_reminder_city";
	
	private UserSettings(Context context) {
		this.context = context;
		pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

	public static synchronized UserSettings getInstance(Context context) {
		if (instance == null) {
			instance = new UserSettings(context);
		}
		return instance;
	}
	
	public void setTTSSpeaker(String value) {
		editor.putString(KEY_TTS_SPEAKER, value);
		editor.commit();
	}
	
	public void setTTSVolume(int value) {
		editor.putInt(KEY_TTS_VOLUME, value);
		editor.commit();
	}
	
	public void setTTSSpeed(int value) {
		editor.putInt(KEY_TTS_SPEED, value);
		editor.commit();
	}
	
	public void setWeatherTTSEnabled(boolean value) {
		editor.putBoolean(KEY_WEATHER_TTS_ENABLED, value);
		editor.commit();
	}	
	
	public void setWeatherReminderEnabled(boolean value) {
		editor.putBoolean(KEY_WEATHER_REMINDER_ENABLED, value);
		editor.commit();
	}
	
	public void setWeatherReminderTime(long value) {
		editor.putLong(KEY_WEATHER_REMINDER_TIME, value);
		editor.commit();
	}
	
	public void setWeatherReminderCity(int value) {
		editor.putInt(KEY_WEATHER_REMINDER_CITY, value);
		editor.commit();
	}
	
	public String getTTSSpeaker() {
		return pref.getString(KEY_TTS_SPEAKER, "Bruce");
	}
	
	public int getTTSVolume() {
		return pref.getInt(KEY_TTS_VOLUME, 100);
	}
	
	public int getTTSSpeed() {
		return pref.getInt(KEY_TTS_SPEED, 0);
	}
	
	public boolean isWeatherTTSEnabled() {
		return pref.getBoolean(KEY_WEATHER_TTS_ENABLED, false);
	}
	
	public boolean isWeatherReminderEnabled() {
		return pref.getBoolean(KEY_WEATHER_REMINDER_ENABLED, false);
	}	
	
	public long getWeatherReminderTime() {
		return pref.getLong(KEY_WEATHER_REMINDER_TIME, 0);
	}
	
	public int getWeatherReminderCity() {
		return pref.getInt(KEY_WEATHER_REMINDER_CITY, City.TAIPEI_CITY.getCityID());
	}
	
}
