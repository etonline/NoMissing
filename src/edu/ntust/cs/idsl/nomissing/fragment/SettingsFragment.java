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
import edu.ntust.cs.idsl.nomissing.dao.DaoFactory;
import edu.ntust.cs.idsl.nomissing.global.NoMissingApp;
import edu.ntust.cs.idsl.nomissing.model.Calendar;
import edu.ntust.cs.idsl.nomissing.preference.SeekBarPreference;
import edu.ntust.cs.idsl.nomissing.preference.SettingsManager;
import edu.ntust.cs.idsl.nomissing.util.ToastMaker;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
@SuppressLint("InlinedApi")
public class SettingsFragment extends PreferenceFragment implements OnPreferenceClickListener, OnPreferenceChangeListener {

    private static final String KEY_CALENDAR_ID = "calendar_id";
    private static final String KEY_TTS_SPEAKER = "tts_speaker";
    private static final String KEY_TTS_VOLUME = "tts_volume";
    private static final String KEY_TTS_SPEED = "tts_speed";

    private NoMissingApp app;

    private Preference prefCalendar;
    private ListPreference prefTTSSpeaker;
    private SeekBarPreference prefTTSVolume;
    private SeekBarPreference prefTTSSpeed;

    private long calendarID;
    private String ttsSpeaker;
    private int ttsVolume;
    private int ttsSpeed;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_settings);
        setHasOptionsMenu(true);
        app = (NoMissingApp) getActivity().getApplicationContext();

        calendarID = app.getSettings().getCalendarID();
        ttsSpeaker = app.getSettings().getTTSSpeaker();
        ttsVolume = app.getSettings().getTTSVolume();
        ttsSpeed = app.getSettings().getTTSSpeed();

        setPrefCalendar();
        setPrefTTSSpeaker();
        setPrefTTSVolume();
        setPrefTTSSpeed();
    }

    private void setPrefCalendar() {
        prefCalendar = (Preference) findPreference(KEY_CALENDAR_ID);
        List<Calendar> calendars = DaoFactory.getCalendarProviderDaoFactory()
                .createCalendarDao(getActivity()).findAll();
        prefCalendar.setSummary(Calendar.getCalendarNameById(calendars,
                calendarID));
        prefCalendar.setOnPreferenceClickListener(this);
    }

    private void setPrefTTSSpeaker() {
        prefTTSSpeaker = (ListPreference) findPreference(KEY_TTS_SPEAKER);
        prefTTSSpeaker.setSummary(ttsSpeaker);
        prefTTSSpeaker.setOnPreferenceChangeListener(this);
    }

    private void setPrefTTSVolume() {
        final int MIN_TTS_VOLUME = 0;
        final int MAX_TTS_VOLUME = 100;
        prefTTSVolume = (SeekBarPreference) findPreference(KEY_TTS_VOLUME);
        prefTTSVolume.init(MIN_TTS_VOLUME, MAX_TTS_VOLUME, ttsVolume);
        prefTTSVolume.setSummary(String.valueOf(ttsVolume));
        prefTTSVolume.setOnPreferenceChangeListener(this);
    }

    private void setPrefTTSSpeed() {
        final int MIN_TTS_SPEED = -10;
        final int MAX_TTS_SPEED = 10;
        prefTTSSpeed = (SeekBarPreference) findPreference(KEY_TTS_SPEED);
        prefTTSSpeed.init(MIN_TTS_SPEED, MAX_TTS_SPEED, ttsSpeed);
        prefTTSSpeed.setSummary(String.valueOf(ttsSpeed));
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
        case R.id.action_default_settings:
            setDefaultSettings();
            return true;        
        case R.id.action_save_settings:
            saveSettings();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == prefTTSSpeaker) {
            ttsSpeaker = (String) newValue;
            preference.setSummary((String) newValue);
        }

        else if (preference == prefTTSVolume) {
            ttsVolume = (int) newValue;
            preference.setSummary(String.valueOf(newValue));
        }

        else if (preference == prefTTSSpeed) {
            ttsSpeed = (int) newValue;
            preference.setSummary(String.valueOf(newValue));
        }

        return true;
    }

    private void saveSettings() {
        app.getSettings().setCalendarID(calendarID);
        app.getSettings().setTTSSpeaker(ttsSpeaker);
        app.getSettings().setTTSVolume(ttsVolume);
        app.getSettings().setTTSSpeed(ttsSpeed);
        ToastMaker.toast(getActivity(), getString(R.string.toast_save_settings));
    }

    private void setDefaultSettings() {
        calendarID = SettingsManager.DEFAULT_CALENDAR_ID;
        ttsSpeaker = SettingsManager.DEFAULT_TTS_SPEAKER;
        ttsVolume = SettingsManager.DEFAULT_TTS_VOLUME;
        ttsSpeed = SettingsManager.DEFAULT_TTS_SPEED;

        prefCalendar.setSummary(R.string.default_calendar);
        prefTTSSpeaker.setSummary(ttsSpeaker);
        prefTTSVolume.setSummary(String.valueOf(ttsVolume));
        prefTTSSpeed.setSummary(String.valueOf(ttsSpeed));
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference == prefCalendar)
            openSettingCalendarDialog();
        return true;
    }

    private void openSettingCalendarDialog() {
        final List<Calendar> calendars = DaoFactory.getCalendarProviderDaoFactory().createCalendarDao(getActivity()).findByAccessLevel(Calendars.CAL_ACCESS_OWNER);

        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.dialog_set_calendar)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setItems(Calendar.getNameOfCalendars(calendars).toArray(new String[calendars.size()]),
                        new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                calendarID = calendars.get(which).getId();
                                prefCalendar.setSummary(calendars.get(which).getName());
                            }
                        }).show();
    }

}
