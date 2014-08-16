package edu.ntust.cs.idsl.nomissing.fragment;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.support.v4.preference.PreferenceFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.global.NoMissingApp;
import edu.ntust.cs.idsl.nomissing.util.SeekBarPreference;
import edu.ntust.cs.idsl.nomissing.util.ToastMaker;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
public class SettingsFragment extends PreferenceFragment implements OnPreferenceChangeListener {

	private NoMissingApp app;
	
	private ListPreference prefTTSSpeaker;
	private SeekBarPreference prefTTSVolume;
	private SeekBarPreference prefTTSSpeed;
	
	private String mTTSSpeaker;
	private int mTTSVolume;
	private int mTTSSpeed;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref_settings);
		setHasOptionsMenu(true);
		app = (NoMissingApp)getActivity().getApplicationContext();	
		
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
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_settings, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings_ok:
			saveSettings();
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
		
		ToastMaker.toast(getActivity(), getString(R.string.toast_save_settings));
	}

}
