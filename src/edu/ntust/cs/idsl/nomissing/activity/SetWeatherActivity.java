package edu.ntust.cs.idsl.nomissing.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TimePicker;
import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.dao.ChimeDAO;
import edu.ntust.cs.idsl.nomissing.global.NoMissingApp;
import edu.ntust.cs.idsl.nomissing.model.Chime;
import edu.ntust.cs.idsl.nomissing.model.City;
import edu.ntust.cs.idsl.nomissing.util.AlarmUtil;
import edu.ntust.cs.idsl.nomissing.util.ToastMaker;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
@SuppressLint({ "NewApi", "SimpleDateFormat" })
public class SetWeatherActivity extends PreferenceActivity implements OnPreferenceChangeListener,
		TimePickerDialog.OnTimeSetListener {
	
	private static final String TAG = SetWeatherActivity.class.getSimpleName();
	private NoMissingApp app;
	
	private CheckBoxPreference prefEnabled;
	private Preference prefTime;
	private ListPreference prefCity;
	
	private boolean isEnabled;
	private long time;
	private int city;
	
	private ChimeDAO chimeDAO;
	private AlarmUtil chimeAlarm;
	private Chime chime;
	private Calendar calendar;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_add_chime);
		addPreferencesFromResource(R.xml.pref_weather);
		
		app = (NoMissingApp)getApplicationContext();
		
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);	

		prefEnabled = (CheckBoxPreference) findPreference("enabled");
		prefTime = (Preference) findPreference("time");
		prefCity = (ListPreference) findPreference("city");
		
		prefEnabled.setOnPreferenceChangeListener(this);
		prefCity.setOnPreferenceChangeListener(this);
		
		// init prefEnabled
		prefEnabled.setChecked(app.userSettings.isWeatherEnabled());
		isEnabled = app.userSettings.isWeatherEnabled();
		
		// init prefCity
		List<String> cityValueList = new ArrayList<String>();		
		for (City city : City.values()) {	
			cityValueList.add(city.getCityName());
		}	
		prefCity.setEntries((CharSequence[]) cityValueList.toArray(new CharSequence[cityValueList.size()]));
		
		List<String> cityKeyList = new ArrayList<String>();		
		for (City city : City.values()) {	
			cityKeyList.add(String.valueOf(city.getCityID()));
		}	
		prefCity.setEntryValues((CharSequence[]) cityKeyList.toArray(new CharSequence[cityKeyList.size()]));
		
		prefCity.setSummary(City.getCityNameByCityID(app.userSettings.getWeatherCity()));
		city = app.userSettings.getWeatherCity();
		
		// init prefTime
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
		if (app.userSettings.getWeatherTime() != 0) {
			time = app.userSettings.getWeatherTime();
			calendar = Calendar.getInstance();
			calendar.setTimeInMillis(time);
			prefTime.setSummary(simpleDateFormat.format(calendar.getTime()));
		} else {
			calendar = Calendar.getInstance();
			time = calendar.getTimeInMillis();		
			prefTime.setSummary(simpleDateFormat.format(calendar.getTime()));
		}
		time = app.userSettings.getWeatherTime();
	}

	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		
		if (preference == prefTime) {
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			int minute = calendar.get(Calendar.MINUTE);			
			new TimePickerDialog(this, this, hour, minute, DateFormat.is24HourFormat(this)).show();
		} 

		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		
		if (preference == prefEnabled) {
			isEnabled = (boolean)newValue;
		}
		
		if (preference == prefCity) {
			city = Integer.valueOf((String)newValue);
			prefCity.setSummary(City.getCityNameByCityID(city));
		}
		
		return true;
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
		calendar.set(Calendar.MINUTE, minute);		
		time = calendar.getTimeInMillis();
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
		prefTime.setSummary(simpleDateFormat.format(calendar.getTime()));	
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
			SetWeatherActivity.this.finish();
			return true;
			
		case R.id.action_save:	
			AlarmUtil.cancelWeatherAlarm(this);
			app.userSettings.setWeatherEnabled(isEnabled);
			app.userSettings.setWeatherTime(time);
			app.userSettings.setWeatherCity(city);
			if (isEnabled) AlarmUtil.setWeatherAlarm(this);
			SetWeatherActivity.this.finish();

			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
	}	

}
