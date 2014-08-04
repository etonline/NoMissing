package edu.ntust.cs.idsl.nomissing.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.global.NoMissingApp;
import edu.ntust.cs.idsl.nomissing.util.SeekBarPreference;


@SuppressLint({ "NewApi", "SimpleDateFormat" })
public class SettingsActivity extends PreferenceActivity implements OnPreferenceChangeListener {
	
	private static final String TAG = SettingsActivity.class.getSimpleName();
	private NoMissingApp app;
	
	private ListPreference prefSpeaker;
	private SeekBarPreference prefVolume;
	private SeekBarPreference prefSpeed;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref_settings);
		
		app = (NoMissingApp)getApplicationContext();
		
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);	
        
        prefSpeaker = (ListPreference) findPreference("tts_speaker");
        prefVolume = (SeekBarPreference) findPreference("tts_volume");
        prefSpeed = (SeekBarPreference) findPreference("tts_speed");

        prefVolume.init(0, 100, app.userSettings.getTTSVolume());
        prefSpeed.init(-10, 10, app.userSettings.getTTSSpeed());
        
        prefSpeaker.setOnPreferenceChangeListener(this);
        prefVolume.setOnPreferenceChangeListener(this);
        prefSpeed.setOnPreferenceChangeListener(this);
        
        prefSpeaker.setSummary(app.userSettings.getTTSSpeaker());
        prefVolume.setSummary(String.valueOf(app.userSettings.getTTSVolume()));
        prefSpeed.setSummary(String.valueOf(app.userSettings.getTTSSpeed()));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_settings, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case android.R.id.home:
			SettingsActivity.this.finish();
			return true;
			
		case R.id.action_settings_ok:
			SettingsActivity.this.finish();
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		
		if (preference == prefSpeaker) {
			app.userSettings.setTTSSpeaker((String)newValue);
			preference.setSummary((String)newValue);
		}
		
		if (preference == prefVolume) {
			app.userSettings.setTTSVolume((int)newValue);
			preference.setSummary(String.valueOf(newValue));
		}
		
		if (preference == prefSpeed) {
			app.userSettings.setTTSSpeed((int)newValue);
			preference.setSummary(String.valueOf(newValue));
		}
		
		return false;
	}


}
