package edu.ntust.cs.idsl.nomissing.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.alarm.AlarmHandlerFactory;
import edu.ntust.cs.idsl.nomissing.dao.DaoFactory;
import edu.ntust.cs.idsl.nomissing.dao.sqlite.SQLiteDaoFactory;
import edu.ntust.cs.idsl.nomissing.fragment.CalendarFragment;
import edu.ntust.cs.idsl.nomissing.global.NoMissingApp;
import edu.ntust.cs.idsl.nomissing.model.Event;
import edu.ntust.cs.idsl.nomissing.model.Reminder;
import edu.ntust.cs.idsl.nomissing.service.TTSConvertTextService;
import edu.ntust.cs.idsl.nomissing.util.ToastMaker;

@SuppressLint({ "NewApi", "SimpleDateFormat" })
public class SetEventActivity extends Activity implements OnClickListener, OnCheckedChangeListener {

	public static final String TAG = SetEventActivity.class.getSimpleName();
	private NoMissingApp app;
	
	private EditText editTextTitle;
	private EditText editTextLocation;
	private EditText editTextStartDate;
	private EditText editTextStartTime;
	private EditText editTextEndDate;
	private EditText editTextEndTime;
	private EditText editTextReminderDate;
	private EditText editTextReminderTime;
	private EditText editTextDescription;
	private CheckBox checkBoxRemider;
	
	private long calendarID;
	private long eventID;
	private long reminderID;
	private long startMillis;
	private long endMillis;
	
	final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/M/d (EEE)");
	final SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a");
	
	private Calendar startCalendar;
	private Calendar endCalendar;
	private Calendar reminderCalendar;
	
	private Event event;
	private Reminder reminder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_event);
		app = (NoMissingApp)getApplicationContext();

        calendarID = getIntent().getLongExtra("calendarID", 0);
		eventID = getIntent().getLongExtra("eventID", 0);
		startMillis = getIntent().getLongExtra("startMillis", 0);
		endMillis = getIntent().getLongExtra("endMillis", 0);
		
		getEvent();
		getReminder();
		setStartCalendar();
		setEndCalendar();
		setReminderCalendar();
		setActionBar();
		setViewComponents();
	}
	
	private void getEvent() {
		event = (eventID != 0) 
				? DaoFactory.getEventDaoFactory(calendarID).createEventDao(this).find(eventID, calendarID, startMillis, endMillis)
				: new Event();
	}
	
	private void getReminder() {
		reminder = (eventID != 0) 
			? DaoFactory.getSQLiteDaoFactory().createReminderDao(this).find(calendarID, eventID)
			: new Reminder();
		reminderID = reminder.getId();
	}
	
	private void setStartCalendar() {
		startCalendar = Calendar.getInstance();
		if(startMillis != 0) startCalendar.setTimeInMillis(startMillis);
		if(eventID != 0) startCalendar.setTimeInMillis(event.getStartTime());		
	}
	
	private void setEndCalendar() {
		endCalendar = Calendar.getInstance();
		if(endMillis != 0) endCalendar.setTimeInMillis(endMillis);
		if(eventID != 0) endCalendar.setTimeInMillis(event.getEndTime());			
	}
	
	private void setReminderCalendar() {
		reminderCalendar = Calendar.getInstance();
		reminderCalendar.setTime(startCalendar.getTime());	
		reminderCalendar.set(Calendar.SECOND, 0);
		reminderCalendar.set(Calendar.MILLISECOND, 0);
	}
	
	private void setActionBar() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);		
	}
	
	private void setViewComponents() {
		editTextTitle = (EditText) findViewById(R.id.editTextTitle);
		editTextLocation = (EditText) findViewById(R.id.editTextLocation);
		editTextStartDate = (EditText) findViewById(R.id.editTextStartDate);
		editTextStartTime = (EditText) findViewById(R.id.editTextStartTime);
		editTextEndDate = (EditText) findViewById(R.id.editTextEndDate);
		editTextEndTime = (EditText) findViewById(R.id.editTextEndTime);
		editTextReminderDate = (EditText) findViewById(R.id.editTextReminderDate);
		editTextReminderTime = (EditText) findViewById(R.id.editTextReminderTime);
		editTextDescription = (EditText) findViewById(R.id.editTextDescription);
		checkBoxRemider = (CheckBox) findViewById(R.id.checkBoxReminder);

		editTextTitle.setText(event.getTitle());
		editTextLocation.setText(event.getLocation());
		editTextStartDate.setText(dateFormatter.format(startCalendar.getTime()));
		editTextStartTime.setText(timeFormatter.format(startCalendar.getTime()));
		editTextEndDate.setText(dateFormatter.format(endCalendar.getTime()));
		editTextEndTime.setText(timeFormatter.format(endCalendar.getTime()));
		editTextDescription.setText(event.getDescription());
		editTextReminderDate.setText(dateFormatter.format(reminderCalendar.getTime()));
		editTextReminderTime.setText(timeFormatter.format(reminderCalendar.getTime()));
		editTextReminderDate.setEnabled(event.hasReminder());
		editTextReminderTime.setEnabled(event.hasReminder());
		checkBoxRemider.setChecked(event.hasReminder());
		
		editTextStartDate.setOnClickListener(this);
		editTextStartTime.setOnClickListener(this);
		editTextEndDate.setOnClickListener(this);
		editTextEndTime.setOnClickListener(this);
		editTextReminderDate.setOnClickListener(this);
		editTextReminderTime.setOnClickListener(this);

		checkBoxRemider.setOnCheckedChangeListener(this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_set_event, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			SetEventActivity.this.finish();
			return true;
			
		case R.id.action_set_event_ok:
			
			if (!isValidDateRange(startCalendar, endCalendar)) {
				ToastMaker.toast(this, "¤é´Á³]¸m¿ù»~");
				return false;
			}
			
			if (eventID == 0) {
				createEvent();
				SetEventActivity.this.setResult(CalendarFragment.RESULT_CREATE);
			} else {
				updateEvent();	
				SetEventActivity.this.setResult(CalendarFragment.RESULT_UPDATE);
			}
			SetEventActivity.this.finish();	
			return true;
			
		case R.id.action_delete_event:
			if (eventID == 0) {
				SetEventActivity.this.setResult(CalendarFragment.RESULT_CANCEL);
			} else {
				DaoFactory.getEventDaoFactory(calendarID).createEventDao(this).delete((int)eventID);
				SetEventActivity.this.setResult(CalendarFragment.RESULT_DELETE);
				
			}
			SetEventActivity.this.finish();		
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.editTextStartDate:
			new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
	            @Override
	            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
	        		startCalendar.set(Calendar.YEAR, year);
	        		startCalendar.set(Calendar.MONTH, monthOfYear);	
	        		startCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);	
	        		editTextStartDate.setText(dateFormatter.format(startCalendar.getTime()));
	            }  
	        }, 
	        startCalendar.get(Calendar.YEAR), 
	        startCalendar.get(Calendar.MONTH), 
	        startCalendar.get(Calendar.DAY_OF_MONTH)).show();
			
			break;
			
		case R.id.editTextStartTime:
			new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
	        		startCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
	        		startCalendar.set(Calendar.MINUTE, minute);	
	        		editTextStartTime.setText(timeFormatter.format(startCalendar.getTime()));			
				}
			}, 
			startCalendar.get(Calendar.HOUR_OF_DAY),
			startCalendar.get(Calendar.MINUTE),
			DateFormat.is24HourFormat(this)).show();
			
			break;
			
		case R.id.editTextEndDate:
			new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
	            @Override
	            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
	        		endCalendar.set(Calendar.YEAR, year);
	        		endCalendar.set(Calendar.MONTH, monthOfYear);	
	        		endCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);	
	        		editTextEndDate.setText(dateFormatter.format(endCalendar.getTime()));
	            }  
	        }, 
	        endCalendar.get(Calendar.YEAR), 
	        endCalendar.get(Calendar.MONTH), 
	        endCalendar.get(Calendar.DAY_OF_MONTH)).show();
			
			break;
			
		case R.id.editTextEndTime:
			new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					endCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
					endCalendar.set(Calendar.MINUTE, minute);	
	        		editTextEndTime.setText(timeFormatter.format(endCalendar.getTime()));			
				}
			}, 
			endCalendar.get(Calendar.HOUR_OF_DAY), 
			endCalendar.get(Calendar.MINUTE), 
			DateFormat.is24HourFormat(this)).show();		
			break;
			
		case R.id.editTextReminderDate:
			new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
	            @Override
	            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
	            	reminderCalendar.set(Calendar.YEAR, year);
	            	reminderCalendar.set(Calendar.MONTH, monthOfYear);	
	            	reminderCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);	
	        		editTextEndDate.setText(dateFormatter.format(reminderCalendar.getTime()));
	            }  
	        }, 
	        reminderCalendar.get(Calendar.YEAR), 
	        reminderCalendar.get(Calendar.MONTH), 
	        reminderCalendar.get(Calendar.DAY_OF_MONTH)).show();
			
			break;
			
		case R.id.editTextReminderTime:
			new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					reminderCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
					reminderCalendar.set(Calendar.MINUTE, minute);	
	        		editTextReminderTime.setText(timeFormatter.format(reminderCalendar.getTime()));			
				}
			}, 
			reminderCalendar.get(Calendar.HOUR_OF_DAY), 
			reminderCalendar.get(Calendar.MINUTE), 
			DateFormat.is24HourFormat(this)).show();		
			break;			
			
		default:
			break;
		}
		
	}
	
	private void createEvent() {
		long currentTime = System.currentTimeMillis();
		
		event.setCalendarID(calendarID);
		event.setTitle(editTextTitle.getText().toString());
		event.setLocation(editTextLocation.getText().toString());
		event.setStartTime(startCalendar.getTimeInMillis());
		event.setEndTime(endCalendar.getTimeInMillis());
		event.setReminder(checkBoxRemider.isChecked());
		event.setDescription(editTextDescription.getText().toString());
		event.setCreatedAt(currentTime);
		event.setUpdatedAt(currentTime);
		
		eventID = DaoFactory.getEventDaoFactory(calendarID).createEventDao(this).insert(event);
//		ToastMaker.toast(this, String.valueOf(eventID));
		
		reminder.setCalendarID(calendarID);
		reminder.setEventID(eventID);
		reminder.setReminderTime(reminderCalendar.getTimeInMillis());
		reminder.setCreatedAt(currentTime);
		reminder.setUpdatedAt(currentTime);
		reminderID = DaoFactory.getSQLiteDaoFactory().createReminderDao(this).insert(reminder);
		
		reminder.setId(reminderID);
		ToastMaker.toast(this, "ReminderID:" + reminderID);
		AlarmHandlerFactory.createReminderAlarmHandler(this).setAlarm(reminder);
		
		getTTSAudio();
	}	
	
	private void updateEvent() {
		long currentTime = System.currentTimeMillis();
		
		event.setTitle(editTextTitle.getText().toString());
		event.setLocation(editTextLocation.getText().toString());
		event.setStartTime(startCalendar.getTimeInMillis());
		event.setEndTime(endCalendar.getTimeInMillis());
		event.setReminder(checkBoxRemider.isChecked());
		event.setDescription(editTextDescription.getText().toString());
		DaoFactory.getEventDaoFactory(calendarID).createEventDao(this).update(event);	
		
		reminder.setReminderTime(reminderCalendar.getTimeInMillis());
		reminder.setUpdatedAt(currentTime);
		DaoFactory.getSQLiteDaoFactory().createReminderDao(this).update(reminder);
		
		AlarmHandlerFactory.createReminderAlarmHandler(this).cancelAlarm(reminder);
		AlarmHandlerFactory.createReminderAlarmHandler(this).setAlarm(reminder);
		
		getTTSAudio();
	}
	
	private boolean isValidDateRange(Calendar startCalendar, Calendar endCalendar) {
		boolean isValidDateRange = false;
		if (startCalendar.compareTo(endCalendar) == -1) {
			isValidDateRange = true;
		}
		return isValidDateRange;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {	
		if (checkBoxRemider.isChecked()) {
			editTextReminderDate.setEnabled(true);
			editTextReminderTime.setEnabled(true);
		} else {
			editTextReminderDate.setEnabled(false);
			editTextReminderTime.setEnabled(false);
		}
	}
	
	private void getTTSAudio() {
		Intent intent = new Intent(this, TTSConvertTextService.class);
		intent.setAction(TTSConvertTextService.ACTION_CONVERT_TEXT);
		intent.putExtra("category", "reminder");
		intent.putExtra("id", reminderID);
		intent.putExtra(TTSConvertTextService.PARAM_TTS_TEXT, reminder.getStringForTTS(event));
		intent.putExtra(TTSConvertTextService.PARAM_TTS_SPEAKER, app.getSettings().getTTSSpeaker());
		intent.putExtra(TTSConvertTextService.PARAM_VOLUME, app.getSettings().getTTSVolume());
		intent.putExtra(TTSConvertTextService.PARAM_SPEED, app.getSettings().getTTSSpeed());
		intent.putExtra(TTSConvertTextService.PARAM_OUTPUT_TYPE, "wav");
		startService(intent);				
	}

}
