package edu.ntust.cs.idsl.nomissing.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import edu.ntust.cs.idsl.nomissing.constant.City;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
public class SettingsManager {

    private static SettingsManager instance;

    private Context context;
    private SharedPreferences pref;
    private Editor editor;

    public static final String PREF_NAME = "SettingsPref";

    public static final String KEY_INITIALIZED = "initialized";
    public static final String KEY_REGISTERED = "registered";
    public static final String KEY_UUID = "uuid";
    public static final String KEY_ACCESS_TOKEN = "access_token";

    public static final String KEY_CALENDAR_ID = "calendar_id";

    public static final String KEY_TTS_SPEAKER = "tts_speaker";
    public static final String KEY_TTS_VOLUME = "tts_volume";
    public static final String KEY_TTS_SPEED = "tts_speed";

    public static final String KEY_WEATHER_TTS_ENABLED = "weather_tts_enabled";
    public static final String KEY_WEATHER_REMINDER_ENABLED = "weather_reminder_enabled";
    public static final String KEY_WEATHER_REMINDER_HOUR = "weather_reminder_hour";
    public static final String KEY_WEATHER_REMINDER_MINUTE = "weather_reminder_minute";
    public static final String KEY_WEATHER_REMINDER_CITY = "weather_reminder_city";

    public static final long DEFAULT_CALENDAR_ID = -1;
    public static final String DEFAULT_TTS_SPEAKER = "Bruce";
    public static final int DEFAULT_TTS_VOLUME = 100;
    public static final int DEFAULT_TTS_SPEED = 0;

    private SettingsManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public static synchronized SettingsManager getInstance(Context context) {
        if (instance == null) {
            instance = new SettingsManager(context);
        }
        return instance;
    }

    public void setInitialized(boolean value) {
        editor.putBoolean(KEY_INITIALIZED, value);
        editor.commit();
    }

    public void setRegistered(boolean value) {
        editor.putBoolean(KEY_REGISTERED, value);
        editor.commit();
    }

    public void setUUID(String value) {
        editor.putString(KEY_UUID, value);
        editor.commit();
    }

    public void setAccessToken(String value) {
        editor.putString(KEY_ACCESS_TOKEN, value);
        editor.commit();
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

    public boolean isInitialized() {
        return pref.getBoolean(KEY_INITIALIZED, false);
    }

    public boolean isRegistered() {
        return pref.getBoolean(KEY_REGISTERED, false);
    }

    public String getUUID() {
        return pref.getString(KEY_UUID, "");
    }

    public String getAccessToken() {
        return pref.getString(KEY_ACCESS_TOKEN, "");
    }

    public long getCalendarID() {
        return pref.getLong(KEY_CALENDAR_ID, DEFAULT_CALENDAR_ID);
    }

    public String getTTSSpeaker() {
        return pref.getString(KEY_TTS_SPEAKER, DEFAULT_TTS_SPEAKER);
    }

    public int getTTSVolume() {
        return pref.getInt(KEY_TTS_VOLUME, DEFAULT_TTS_VOLUME);
    }

    public int getTTSSpeed() {
        return pref.getInt(KEY_TTS_SPEED, DEFAULT_TTS_SPEED);
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
        return pref.getInt(KEY_WEATHER_REMINDER_CITY,
                City.TAIPEI_CITY.getCityID());
    }

}
