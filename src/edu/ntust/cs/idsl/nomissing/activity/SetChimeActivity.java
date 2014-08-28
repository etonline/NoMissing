package edu.ntust.cs.idsl.nomissing.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.net.Uri;
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
import edu.ntust.cs.idsl.nomissing.dao.DaoFactory;
import edu.ntust.cs.idsl.nomissing.global.Constant;
import edu.ntust.cs.idsl.nomissing.global.NoMissingApp;
import edu.ntust.cs.idsl.nomissing.model.Chime;
import edu.ntust.cs.idsl.nomissing.service.tts.TextToSpeechService;
import edu.ntust.cs.idsl.nomissing.util.Connectivity;
import edu.ntust.cs.idsl.nomissing.util.ToastMaker;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
@SuppressLint({ "NewApi", "SimpleDateFormat" })
public class SetChimeActivity extends PreferenceActivity implements OnPreferenceClickListener, OnPreferenceChangeListener {
	
	private static final String KEY_CHIME_ENABLED = "chime_enabled";
	private static final String KEY_CHIME_TIME = "chime_time";
	private static final String KEY_CHIME_REPEATING = "chime_repeating";
	private static final SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
	
	private NoMissingApp app;
	
	private CheckBoxPreference prefChimeEnabled;
	private Preference prefChimeTime;
	private ListPreference prefChimeRepeating;
	
	private int chimeID;
	private int chimeHour;
	private int chimeMinute;
	private boolean isChimeEnabled;
	private boolean isChimeRepeating;
	private final boolean isTrigged = false;
	
	private Chime chime;
	private Calendar calendar;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref_chime);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);	
        app = (NoMissingApp)getApplicationContext();
		
		chimeID = getIntent().getIntExtra("chimeID", 0);
		chime = (chimeID != 0 ) ? DaoFactory.getSQLiteDaoFactory().createChimeDao(this).find(chimeID) : new Chime();
		
		chimeHour = chime.getHour();
		chimeMinute = chime.getMinute();
		isChimeEnabled = chime.isEnabled();
		isChimeRepeating = chime.isRepeating();
		
		setPrefChimeEnabled();
		setPrefChimeTime();
		setPrefChimeRepeating();
	}
	
	private void setPrefChimeEnabled() {
		prefChimeEnabled = (CheckBoxPreference) findPreference(KEY_CHIME_ENABLED);
		prefChimeEnabled.setChecked(isChimeEnabled);
		prefChimeEnabled.setOnPreferenceChangeListener(this);
	}
	
	private void setPrefChimeTime() {
		prefChimeTime = (Preference) findPreference(KEY_CHIME_TIME);
		
		calendar = Calendar.getInstance();
		if (chimeID != 0) {
			calendar.set(Calendar.HOUR_OF_DAY, chimeHour);
			calendar.set(Calendar.MINUTE, chimeMinute);					
		} else {
			chimeHour = calendar.get(Calendar.HOUR);
			chimeMinute = calendar.get(Calendar.MINUTE);
		}
		prefChimeTime.setSummary(formatter.format(calendar.getTime()));		
		
		prefChimeTime.setOnPreferenceClickListener(this);
	}
	
	private void setPrefChimeRepeating() {
		prefChimeRepeating = (ListPreference) findPreference(KEY_CHIME_REPEATING);
		prefChimeRepeating.setSummary(getString(chime.getRepeating()));
		prefChimeRepeating.setOnPreferenceChangeListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_set_chime, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case android.R.id.home:
			SetChimeActivity.this.finish();
			return true;
			
		case R.id.action_set_chime_ok:
			if (chimeID == 0) {
				createChime();
				setResult(Constant.RESULT_CODE_CREATE);
			} else {
				updateChime();
				setResult(Constant.RESULT_CODE_UPDATE);		
			}
			finish();
			return true;
			
		case R.id.action_delete_chime:
			if (chimeID == 0) {
				setResult(Constant.RESULT_CODE_CANCEL);
			} else {
				deleteChime();
				setResult(Constant.RESULT_CODE_DELETE);
			}
			finish();	
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
	}		
	
	@Override
	public boolean onPreferenceClick(Preference preference) {
		if (preference == prefChimeTime) {
			new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					chimeHour = hourOfDay;
					chimeMinute = minute;
					calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
					calendar.set(Calendar.MINUTE, minute);		
					prefChimeTime.setSummary(formatter.format(calendar.getTime()));		
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
		
		if (preference == prefChimeEnabled) {
			isChimeEnabled = (boolean)newValue;
		}
		
		else if (preference == prefChimeRepeating) {
			isChimeRepeating = (newValue.equals("1")) ? true : false;
			String summmary = isChimeRepeating ? getString(R.string.pref_chime_repeating_every_day)
					: getString(R.string.pref_chime_repeating_one_time);
			preference.setSummary(summmary);
		}
		
		return true;
	}
	
	private void createChime() {
		long currentTime = System.currentTimeMillis();
		chime.setHour(chimeHour);
		chime.setMinute(chimeMinute);
		chime.setEnabled(isChimeEnabled);
		chime.setRepeating(isChimeRepeating);
		chime.setTriggered(isTrigged);
		chime.setCreatedAt(currentTime);
		chime.setUpdatedAt(currentTime);
		chimeID = DaoFactory.getSQLiteDaoFactory().createChimeDao(this).insert(chime);
		chime.setId(chimeID);
		
		if (isChimeEnabled) {
			if (!Connectivity.isConnected(this)) {
				isChimeEnabled = false;
				ToastMaker.toast(this, R.string.toast_network_inavailable);	
				return;
			}
			
			AlarmHandlerFactory.createChimeAlarmHandler(this).setAlarm(chime);	
			getTTSAudio();
		}
	}
	
	private void updateChime() {
		long currentTime = System.currentTimeMillis();
		chime.setHour(chimeHour);
		chime.setMinute(chimeMinute);
		chime.setEnabled(isChimeEnabled);
		chime.setRepeating(isChimeRepeating);
		chime.setTriggered(isTrigged);
		chime.setUpdatedAt(currentTime);
		DaoFactory.getSQLiteDaoFactory().createChimeDao(this).update(chime);
		
		AlarmHandlerFactory.createChimeAlarmHandler(this).cancelAlarm(chime);
		if (isChimeEnabled) {
			if (!Connectivity.isConnected(this)) {
				isChimeEnabled = false;
				ToastMaker.toast(this, R.string.toast_network_inavailable);	
				return;
			}
			
			AlarmHandlerFactory.createChimeAlarmHandler(this).setAlarm(chime);
			getTTSAudio();
		}
	}
	
	private void deleteChime() {
		AlarmHandlerFactory.createChimeAlarmHandler(this).cancelAlarm(chime);

		if (chime.getAudio() != null) {
			File audio = new File(Uri.parse(chime.getAudio()).getPath());
			audio.delete();
		}
		
		DaoFactory.getSQLiteDaoFactory().createChimeDao(this).delete(chimeID);
	}
	
	private void getTTSAudio() {
		Bundle extras = new Bundle();
		extras.putString(TextToSpeechService.EXTRA_CATEGORY, TextToSpeechService.CATEGORY_CHIME);
		extras.putLong(TextToSpeechService.EXTRA_ENTITY_ID, (long)chime.getId());
		extras.putString(TextToSpeechService.EXTRA_TTS_TEXT, chime.getStringForTTS());
		extras.putString(TextToSpeechService.EXTRA_TTS_SPEAKER, app.getSettings().getTTSSpeaker());
		extras.putInt(TextToSpeechService.EXTRA_TTS_VOLUME, app.getSettings().getTTSVolume());
		extras.putInt(TextToSpeechService.EXTRA_TTS_SPEED, app.getSettings().getTTSSpeed());
		extras.putString(TextToSpeechService.EXTRA_TTS_OUTPUT_TYPE, "wav");
		
		TextToSpeechService.startService(this, extras);		
	}

}
