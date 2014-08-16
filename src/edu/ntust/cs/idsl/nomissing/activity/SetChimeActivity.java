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
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TimePicker;
import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.dao.SQLiteDAOFactory;
import edu.ntust.cs.idsl.nomissing.global.Constant;
import edu.ntust.cs.idsl.nomissing.global.NoMissingApp;
import edu.ntust.cs.idsl.nomissing.model.Chime;
import edu.ntust.cs.idsl.nomissing.service.TTSConvertTextService;
import edu.ntust.cs.idsl.nomissing.util.AlarmUtil;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
@SuppressLint({ "NewApi", "SimpleDateFormat" })
public class SetChimeActivity extends PreferenceActivity implements OnPreferenceClickListener, OnPreferenceChangeListener {
	
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
	private SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref_chime);
		
		app = (NoMissingApp)getApplicationContext();
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);	
		
		chimeID = getIntent().getIntExtra("chimeID", 0);
		chime = (chimeID != 0 ) ? SQLiteDAOFactory.getChimeDAO(this).find(chimeID) : new Chime();
		
		chimeHour = chime.getHour();
		chimeMinute = chime.getMinute();
		isChimeEnabled = chime.isEnabled();
		isChimeRepeating = chime.isRepeating();
		
		setPrefChimeEnabled();
		setPrefChimeTime();
		setPrefChimeRepeating();
	}
	
	private void setPrefChimeEnabled() {
		prefChimeEnabled = (CheckBoxPreference) findPreference("chime_enabled");
		prefChimeEnabled.setChecked(isChimeEnabled);
		prefChimeEnabled.setOnPreferenceChangeListener(this);
	}
	
	private void setPrefChimeTime() {
		prefChimeTime = (Preference) findPreference("chime_time");
		
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
		prefChimeRepeating = (ListPreference) findPreference("chime_repeating");
		prefChimeRepeating.setSummary(chime.getRepeating());
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
				getTTSAudio();
				setResult(Constant.RESULT_CODE_CREATE);
			} else {
				updateChime();
				getTTSAudio();
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
			String summmary = (newValue.equals("0")) ? "一次" : "每天";
			preference.setSummary(summmary);
		}
		
		return true;
	}
	
	private void createChime() {
		chimeID = SQLiteDAOFactory.getChimeDAO(this).getNextID();
		long currentTime = System.currentTimeMillis();
		
		chime.setId(chimeID);
		chime.setHour(chimeHour);
		chime.setMinute(chimeMinute);
		chime.setEnabled(isChimeEnabled);
		chime.setRepeating(isChimeRepeating);
		chime.setTriggered(isTrigged);
		chime.setCreatedAt(currentTime);
		chime.setUpdatedAt(currentTime);
		
		SQLiteDAOFactory.getChimeDAO(this).insert(chime);
		AlarmUtil.setChimeAlarm(this, chime);		
	}
	
	private void updateChime() {
		long currentTime = System.currentTimeMillis();
		
		chime.setHour(chimeHour);
		chime.setMinute(chimeMinute);
		chime.setEnabled(isChimeEnabled);
		chime.setRepeating(isChimeRepeating);
		chime.setTriggered(isTrigged);
		chime.setUpdatedAt(currentTime);
		
		SQLiteDAOFactory.getChimeDAO(this).update(chime);
		AlarmUtil.setChimeAlarm(this, chime);		
	}
	
	private void deleteChime() {
		SQLiteDAOFactory.getChimeDAO(this).delete(chimeID);
		AlarmUtil.cancelChimeAlarm(this, chime);		
	}
	
	private void getTTSAudio() {
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
