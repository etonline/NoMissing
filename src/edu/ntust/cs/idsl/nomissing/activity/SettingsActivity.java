package edu.ntust.cs.idsl.nomissing.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.view.Menu;
import android.view.MenuItem;
import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.global.NoMissingApp;
import edu.ntust.cs.idsl.nomissing.util.SeekBarPreference;
import edu.ntust.cs.idsl.nomissing.util.ToastMaker;


@SuppressLint({ "NewApi", "SimpleDateFormat" })
public class SettingsActivity extends PreferenceActivity implements OnPreferenceChangeListener {
	
	private static final String TAG = SettingsActivity.class.getSimpleName();
	private NoMissingApp app;
	
	private ListPreference prefTTSSpeaker;
	private SeekBarPreference prefTTSVolume;
	private SeekBarPreference prefTTSSpeed;
	
	private String mTTSSpeaker;
	private int mTTSVolume;
	private int mTTSSpeed;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref_settings);
		
		app = (NoMissingApp)getApplicationContext();
		
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);	
        
		mTTSSpeaker = app.userSettings.getTTSSpeaker();
		mTTSVolume = app.userSettings.getTTSVolume();
		mTTSSpeed = app.userSettings.getTTSSpeed();
		
		setPrefTTSSpeaker();
		setPrefTTSVolume();
		setPrefTTSSpeed();
	}
	
	private void setPrefTTSSpeaker() {
        prefTTSSpeaker = (ListPreference) findPreference("tts_speaker");
        prefTTSSpeaker.setSummary(mTTSSpeaker);
        prefTTSSpeaker.setOnPreferenceChangeListener(this);
	}
	
	private void setPrefTTSVolume() {
		prefTTSVolume = (SeekBarPreference) findPreference("tts_volume");
        prefTTSVolume.init(0, 100, mTTSVolume);
        prefTTSVolume.setSummary(String.valueOf(mTTSVolume));
        prefTTSVolume.setOnPreferenceChangeListener(this);
	}
	
	private void setPrefTTSSpeed() {
		prefTTSSpeed = (SeekBarPreference) findPreference("tts_speed");
		prefTTSSpeed.init(-10, 10, mTTSSpeed);
		prefTTSSpeed.setSummary(String.valueOf(mTTSSpeed));	
		prefTTSSpeed.setOnPreferenceChangeListener(this);
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
			finish();
			return true;
			
		case R.id.action_settings_ok:
			saveSettings();
			finish();
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		if (preference == prefTTSSpeaker) {
			mTTSSpeaker = (String)newValue;
			preference.setSummary((String)newValue);
		}
		
		else if (preference == prefTTSVolume) {
			mTTSVolume = (int)newValue;
			preference.setSummary(String.valueOf(newValue));
		}
		
		else if (preference == prefTTSSpeed) {
			mTTSSpeed = (int)newValue;
			preference.setSummary(String.valueOf(newValue));
		}
		return true;
	}

	private void saveSettings() {
		app.userSettings.setTTSSpeaker(mTTSSpeaker);
		app.userSettings.setTTSVolume(mTTSVolume);
		app.userSettings.setTTSSpeed(mTTSSpeed);
		
		ToastMaker.toast(this, getString(R.string.toast_save_settings));
	}
	
}
