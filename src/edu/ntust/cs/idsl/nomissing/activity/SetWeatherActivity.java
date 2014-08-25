package edu.ntust.cs.idsl.nomissing.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TimePicker;
import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.alarm.AlarmHandlerFactory;
import edu.ntust.cs.idsl.nomissing.constant.City;
import edu.ntust.cs.idsl.nomissing.dao.sqlite.SQLiteDaoFactory;
import edu.ntust.cs.idsl.nomissing.global.NoMissingApp;
import edu.ntust.cs.idsl.nomissing.model.Weather;
import edu.ntust.cs.idsl.nomissing.util.ToastMaker;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
@SuppressLint({ "NewApi", "SimpleDateFormat" })
public class SetWeatherActivity extends PreferenceActivity implements OnPreferenceClickListener, OnPreferenceChangeListener {
	
	private NoMissingApp app;
	
	private CheckBoxPreference prefTTSEnabled;
	private CheckBoxPreference prefReminderEnabled;
	private Preference prefReminderTime;
	private ListPreference prefReminderCity;
	
	private boolean isTTSEnabled;
	private boolean isReminderEnabled;
	private int reminderHour;
	private int reminderMinute;
	private int reminderCity;
	
	private Weather weather;
	
	private Calendar calendar;
	private SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref_weather);
		
		app = (NoMissingApp)getApplicationContext();
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);	
		
		isTTSEnabled = app.getSettings().isWeatherTTSEnabled();
		isReminderEnabled = app.getSettings().isWeatherReminderEnabled();
		reminderHour = app.getSettings().getWeatherReminderHour();
		reminderMinute = app.getSettings().getWeatherReminderMinute();
		reminderCity = app.getSettings().getWeatherReminderCity();
		
		weather = SQLiteDaoFactory.createWeatherDao(this).find(reminderCity);
		
		setPrefTTSEnabled();
		setPrefWeatherReminder();
		setPrefReminderTime();
		setPrefReminderCity();
	}
	
	private void setPrefTTSEnabled() {
		prefTTSEnabled = (CheckBoxPreference) findPreference("tts_enabled");
		prefTTSEnabled.setChecked(isTTSEnabled);
		prefTTSEnabled.setOnPreferenceChangeListener(this);
	}
	
	private void setPrefWeatherReminder() {
		prefReminderEnabled = (CheckBoxPreference) findPreference("reminder_enabled");
		prefReminderEnabled.setChecked(isReminderEnabled);
		prefReminderEnabled.setOnPreferenceChangeListener(this);
	}
	
	private void setPrefReminderTime() {
		prefReminderTime = (Preference) findPreference("reminder_time");

		calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, reminderHour);	
		calendar.set(Calendar.MINUTE, reminderMinute);			
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		prefReminderTime.setSummary(formatter.format(calendar.getTime()));

		prefReminderTime.setOnPreferenceClickListener(this);
	}
	
	private void setPrefReminderCity() {
		prefReminderCity = (ListPreference) findPreference("reminder_city");
		
		List<String> cityKeys = new ArrayList<String>();		
		for (City city : City.values()) {	
			cityKeys.add(String.valueOf(city.getCityID()));
		}	
		
		List<String> cityValues = new ArrayList<String>();		
		for (City city : City.values()) {	
			cityValues.add(getString(city.getCityName()));
		}	
		
		prefReminderCity.setEntries((CharSequence[]) cityValues.toArray(new CharSequence[cityValues.size()]));		
		prefReminderCity.setEntryValues((CharSequence[]) cityKeys.toArray(new CharSequence[cityKeys.size()]));
		prefReminderCity.setSummary(getString(City.getCity(reminderCity).getCityName()));
		prefReminderCity.setOnPreferenceChangeListener(this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_set_weather, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
			
		case R.id.action_save:	
			saveSettings();
			finish();
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
	}	

	@Override
	public boolean onPreferenceClick(Preference preference) {
		if (preference == prefReminderTime) {
			new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					reminderHour = hourOfDay;
					reminderMinute = minute;
					calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
					calendar.set(Calendar.MINUTE, minute);		
					prefReminderTime.setSummary(formatter.format(calendar.getTime()));		
				}
			}, 
			calendar.get(Calendar.HOUR_OF_DAY),
			calendar.get(Calendar.MINUTE),
			DateFormat.is24HourFormat(this)).show();			
		} 
		
		return true;
	}		
	
	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		
		if (preference == prefTTSEnabled) {
			isTTSEnabled = (boolean)newValue;
		}	
		
		else if (preference == prefReminderEnabled) {
			isReminderEnabled = (boolean)newValue;
		}
		
		else if (preference == prefReminderCity) {
			reminderCity = Integer.valueOf((String)newValue);
			prefReminderCity.setSummary(getString(City.getCity(reminderCity).getCityName()));
		}
		
		return true;
	}
	
	private void saveSettings() {
		AlarmHandlerFactory.createWeatherAlarmHandler(this).cancelAlarm(weather);
		
		app.getSettings().setWeatherTTSEnabled(isTTSEnabled);
		app.getSettings().setWeatherReminderEnabled(isReminderEnabled);
		app.getSettings().setWeatherReminderHour(reminderHour);
		app.getSettings().setWeatherReminderMinute(reminderMinute);
		app.getSettings().setWeatherReminderCity(reminderCity);
		
		weather = SQLiteDaoFactory.createWeatherDao(this).find(reminderCity);
		
		if (isReminderEnabled) 
			AlarmHandlerFactory.createWeatherAlarmHandler(this).setAlarm(weather);
		
		ToastMaker.toast(this, getString(R.string.toast_save_settings));
	}

}
