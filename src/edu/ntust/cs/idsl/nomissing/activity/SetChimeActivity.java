package edu.ntust.cs.idsl.nomissing.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TimePicker;
import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.dao.ChimeDAO;
import edu.ntust.cs.idsl.nomissing.fragment.ChimeFragment;
import edu.ntust.cs.idsl.nomissing.global.NoMissingApp;
import edu.ntust.cs.idsl.nomissing.model.Chime;
import edu.ntust.cs.idsl.nomissing.service.TTSConvertTextService;
import edu.ntust.cs.idsl.nomissing.util.AlarmUtil;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
@SuppressLint({ "NewApi", "SimpleDateFormat" })
public class SetChimeActivity extends PreferenceActivity implements OnPreferenceChangeListener,
		TimePickerDialog.OnTimeSetListener {
	
	private static final String TAG = SetChimeActivity.class.getSimpleName();
	private NoMissingApp app;
	
	private CheckBoxPreference enabledPref;
	private Preference timePref;
	private ListPreference repeatPref;
	
	private int chimeID;
	private int hour;
	private int minute;
	private boolean isEnabled;
	private boolean isRepeating;
	private final boolean isTrigged = false;
	
	private ChimeDAO chimeDAO;
	private Chime chime;
	private Calendar calendar;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_add_chime);
		addPreferencesFromResource(R.xml.pref_chime);
		
		app = (NoMissingApp)getApplicationContext();
		
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);	

		enabledPref = (CheckBoxPreference) findPreference("enabled");
		timePref = (Preference) findPreference("time");
		repeatPref = (ListPreference) findPreference("repeat");
		
		enabledPref.setOnPreferenceChangeListener(this);
		repeatPref.setOnPreferenceChangeListener(this);
		
		chimeDAO = ChimeDAO.getInstance(this);
		
		chimeID = getIntent().getIntExtra("chimeID", -1);
		chime = (chimeID != -1 ) ? chimeDAO.find(chimeID) : new Chime();
		
		hour = chime.getHour();
		minute = chime.getMinute();
		isEnabled = chime.isEnabled();
		isRepeating = chime.isRepeating();
		
		enabledPref.setChecked(isEnabled);
		repeatPref.setSummary(chime.getRepeating());
		updateTime();
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		
		if (preference == timePref) {
			calendar = Calendar.getInstance();
			hour = calendar.get(Calendar.HOUR_OF_DAY);
			minute = calendar.get(Calendar.MINUTE);			
			new TimePickerDialog(this, this, hour, minute, DateFormat.is24HourFormat(this)).show();
		} 

		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		
		if (preference == repeatPref) {
			isRepeating = (newValue.equals("1")) ? true : false;
			String summmary = (newValue.equals("0")) ? "一次" : "每天";
			preference.setSummary(summmary);
		}
		
		if (preference == enabledPref) {
			isEnabled = (boolean)newValue;
		}
		
		return true;
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		this.hour = hourOfDay;
		this.minute = minute;
		updateTime();
	}	
	
	private void updateTime() {
		calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
		String timeString = simpleDateFormat.format(calendar.getTime());
		timePref.setSummary(timeString);		
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
			if (chimeID == -1) {
				createChime();
				SetChimeActivity.this.setResult(ChimeFragment.RESULT_CREATE);
				SetChimeActivity.this.finish();
			} else {
				updateChime();
				SetChimeActivity.this.setResult(ChimeFragment.RESULT_UPDATE);
				SetChimeActivity.this.finish();			
			}
			return true;
			
		case R.id.action_delete_chime:
			if (chimeID == -1) {
				SetChimeActivity.this.setResult(ChimeFragment.RESULT_CANCEL);
				SetChimeActivity.this.finish();	
			} else {
				chimeDAO.delete(chimeID);
				AlarmUtil.cancelChimeAlarm(this, chime);
				SetChimeActivity.this.setResult(ChimeFragment.RESULT_DELETE);
				SetChimeActivity.this.finish();	
			}
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
	}	
	
	private void createChime() {
		chimeID = chimeDAO.getNextID();
		long currentTime = System.currentTimeMillis();
		
		chime.setId(chimeID);
		chime.setHour(hour);
		chime.setMinute(minute);
		chime.setEnabled(isEnabled);
		chime.setRepeating(isRepeating);
		chime.setTriggered(isTrigged);
		chime.setCreatedAt(currentTime);
		chime.setUpdatedAt(currentTime);
		
		chimeDAO.insert(chime);
		AlarmUtil.setChimeAlarm(this, chime);		
		
		Intent intent = new Intent(this, TTSConvertTextService.class);
		intent.setAction(TTSConvertTextService.ACTION_CONVERT_TEXT);
		intent.putExtra("chimeID", chime.getId());
		intent.putExtra(TTSConvertTextService.PARAM_TTS_TEXT, chime.getStringForTTS());
		intent.putExtra(TTSConvertTextService.PARAM_TTS_SPEAKER, app.userSettings.getTTSSpeaker());
		intent.putExtra(TTSConvertTextService.PARAM_VOLUME, app.userSettings.getTTSVolume());
		intent.putExtra(TTSConvertTextService.PARAM_SPEED, app.userSettings.getTTSSpeed());
		intent.putExtra(TTSConvertTextService.PARAM_OUTPUT_TYPE, "wav");
		SetChimeActivity.this.startService(intent);
	}
	
	private void updateChime() {
		long currentTime = System.currentTimeMillis();
		
		chime.setHour(hour);
		chime.setMinute(minute);
		chime.setEnabled(isEnabled);
		chime.setRepeating(isRepeating);
		chime.setTriggered(isTrigged);
		chime.setUpdatedAt(currentTime);
		
		chimeDAO.update(chime);
		AlarmUtil.setChimeAlarm(this, chime);		
		
		Intent intent = new Intent(this, TTSConvertTextService.class);
		intent.setAction(TTSConvertTextService.ACTION_CONVERT_TEXT);
		intent.putExtra("chimeID", chime.getId());
		intent.putExtra(TTSConvertTextService.PARAM_TTS_TEXT, chime.getStringForTTS());
		intent.putExtra(TTSConvertTextService.PARAM_TTS_SPEAKER, app.userSettings.getTTSSpeaker());
		intent.putExtra(TTSConvertTextService.PARAM_VOLUME, app.userSettings.getTTSVolume());
		intent.putExtra(TTSConvertTextService.PARAM_SPEED, app.userSettings.getTTSSpeed());
		intent.putExtra(TTSConvertTextService.PARAM_OUTPUT_TYPE, "wav");
		SetChimeActivity.this.startService(intent);		
	}

}
