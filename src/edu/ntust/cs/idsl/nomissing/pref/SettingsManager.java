package edu.ntust.cs.idsl.nomissing.pref;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import edu.ntust.cs.idsl.nomissing.constant.City;

public class SettingsManager {
	
	private static SettingsManager instance;
	
	private Context context;
	private SharedPreferences pref;
	private Editor editor;

	private int PRIVATE_MODE = 0;

	private static final String PREF_NAME = "SettingsPref";

	private static final String KEY_CALENDAR_ID = "calendar_id";
	private static final String KEY_TTS_SPEAKER = "tts_speaker";
	private static final String KEY_TTS_VOLUME = "tts_volume";
	private static final String KEY_TTS_SPEED = "tts_speed";
	private static final String KEY_WEATHER_TTS_ENABLED = "weather_tts_enabled";
	private static final String KEY_WEATHER_REMINDER_ENABLED = "weather_reminder_enabled";
	private static final String KEY_WEATHER_REMINDER_HOUR = "weather_reminder_hour";
	private static final String KEY_WEATHER_REMINDER_MINUTE = "weather_reminder_minute";
	private static final String KEY_WEATHER_REMINDER_CITY = "weather_reminder_city";
	
	private SettingsManager(Context context) {
		this.context = context;
		pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

	public static synchronized SettingsManager getInstance(Context context) {
		if (instance == null) {
			instance = new SettingsManager(context);
		}
		return instance;
	}
	
	public void setCalendarID(long value) {
		editor.putLong(KEY_CALENDAR_ID, value);
		editor.commit();
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
	
	public void setWeatherReminderHour(int value) {
		editor.putInt(KEY_WEATHER_REMINDER_HOUR, value);
		editor.commit();
	}
	
	public void setWeatherReminderMinute(int value) {
		editor.putInt(KEY_WEATHER_REMINDER_MINUTE, value);
		editor.commit();
	}
	
	public void setWeatherReminderCity(int value) {
		editor.putInt(KEY_WEATHER_REMINDER_CITY, value);
		editor.commit();
	}
	
	public long getCalendarID() {
		return pref.getLong(KEY_CALENDAR_ID, -1);
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
	
	public int getWeatherReminderHour() {
		return pref.getInt(KEY_WEATHER_REMINDER_HOUR, 0);
	}
	
	public int getWeatherReminderMinute() {
		return pref.getInt(KEY_WEATHER_REMINDER_MINUTE, 0);
	}
	
	public int getWeatherReminderCity() {
		return pref.getInt(KEY_WEATHER_REMINDER_CITY, City.TAIPEI_CITY.getCityID());
	}
	
}
