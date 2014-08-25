package edu.ntust.cs.idsl.nomissing.fragment;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.provider.CalendarContract.Calendars;
import android.support.v4.preference.PreferenceFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.dao.calendar.CalendarProviderDaoFactory;
import edu.ntust.cs.idsl.nomissing.global.NoMissingApp;
import edu.ntust.cs.idsl.nomissing.model.Calendar;
import edu.ntust.cs.idsl.nomissing.util.SeekBarPreference;
import edu.ntust.cs.idsl.nomissing.util.ToastMaker;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
@SuppressLint("InlinedApi")
public class SettingsFragment extends PreferenceFragment implements OnPreferenceClickListener, OnPreferenceChangeListener {

	private NoMissingApp app;
	
	private Preference prefCalendar;
	private ListPreference prefTTSSpeaker;
	private SeekBarPreference prefTTSVolume;
	private SeekBarPreference prefTTSSpeed;
	
	private long mCalendarID;
	private String mTTSSpeaker;
	private int mTTSVolume;
	private int mTTSSpeed;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref_settings);
		setHasOptionsMenu(true);
		app = (NoMissingApp)getActivity().getApplicationContext();	
		
		mCalendarID = app.getSettings().getCalendarID();
		mTTSSpeaker = app.getSettings().getTTSSpeaker();
		mTTSVolume = app.getSettings().getTTSVolume();
		mTTSSpeed = app.getSettings().getTTSSpeed();
		
		setPrefCalendar();
		setPrefTTSSpeaker();
		setPrefTTSVolume();
		setPrefTTSSpeed();
	}
	
	private void setPrefCalendar() {
		prefCalendar = (Preference) findPreference("calendar_id");
		List<Calendar> calendars = CalendarProviderDaoFactory.createCalendarDao(getActivity()).findAll();
		for(Calendar calendar : calendars) {
			if (calendar.getId() == mCalendarID) {
				prefCalendar.setSummary(calendar.getName());
				break;
			}
		}
		prefCalendar.setOnPreferenceClickListener(this);
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
		app.getSettings().setCalendarID(mCalendarID);
		app.getSettings().setTTSSpeaker(mTTSSpeaker);
		app.getSettings().setTTSVolume(mTTSVolume);
		app.getSettings().setTTSSpeed(mTTSSpeed);
		
		ToastMaker.toast(getActivity(), getString(R.string.toast_save_settings));
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		if (preference == prefCalendar) {
			openSettingCalendarDialog();
		}
		return true;
	}
	
	private void openSettingCalendarDialog() {
		final List<Calendar> calendars = 
				CalendarProviderDaoFactory.createCalendarDao(getActivity()).findByAccessLevel(Calendars.CAL_ACCESS_OWNER);
	
		new AlertDialog.Builder(getActivity())
		.setTitle(R.string.dialog_set_calendar)
		.setIcon(android.R.drawable.ic_dialog_info)
		.setItems(Calendar.getNameOfCalendars(calendars).toArray(new String[calendars.size()]), new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mCalendarID = calendars.get(which).getId();
				prefCalendar.setSummary(calendars.get(which).getName());
			}
		}).show();
	}

}
