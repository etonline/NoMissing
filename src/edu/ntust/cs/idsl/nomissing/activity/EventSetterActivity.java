package edu.ntust.cs.idsl.nomissing.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import edu.ntust.cs.idsl.nomissing.constant.Category;
import edu.ntust.cs.idsl.nomissing.dao.DaoFactory;
import edu.ntust.cs.idsl.nomissing.global.NoMissingApp;
import edu.ntust.cs.idsl.nomissing.model.Event;
import edu.ntust.cs.idsl.nomissing.model.Reminder;
import edu.ntust.cs.idsl.nomissing.service.tts.TextToSpeechService;
import edu.ntust.cs.idsl.nomissing.util.ToastMaker;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
@SuppressLint({ "NewApi", "SimpleDateFormat" })
public class EventSetterActivity extends Activity implements OnClickListener, OnCheckedChangeListener {

    public static final int REQUEST_CREATE = 0;
    public static final int REQUEST_UPDATE = 1;
    public static final int RESULT_CREATE = 2;
    public static final int RESULT_UPDATE = 3;
    public static final int RESULT_CANCEL = 4;
    public static final int RESULT_DELETE = 5;
    
    private static final String ACTION = "edu.ntust.cs.idsl.nomissing.action.SET_EVENT";
    private static final String EXTRA_REQUEST_CODE = "edu.ntust.cs.idsl.nomissing.extra.REQUEST_CODE";
    private static final String EXTRA_EVENT_ID = "edu.ntust.cs.idsl.nomissing.extra.CALENDAR_ID";
    private static final String EXTRA_CALENDAR_ID = "edu.ntust.cs.idsl.nomissing.extra.EVENT_ID";
    private static final String EXTRA_START_MILLIS = "edu.ntust.cs.idsl.nomissing.extra.START_MILLIS";
    private static final String EXTRA_END_MILLIS = "edu.ntust.cs.idsl.nomissing.extra.END_MILLIS";
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/M/d (EEE)");
    private static final SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a");
   
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

    private Calendar startCalendar;
    private Calendar endCalendar;
    private Calendar reminderCalendar;

    private Event event;
    private Reminder reminder;
    
    private int requestCode;

    public static Intent getAction(Context context, int requestCode, long calendarID, long eventID, long startMillis, long endMillis) {
        Intent intent = new Intent(context, EventSetterActivity.class);
        intent.setAction(ACTION);
        intent.putExtra(EXTRA_REQUEST_CODE, requestCode);
        intent.putExtra(EXTRA_CALENDAR_ID, calendarID);
        intent.putExtra(EXTRA_EVENT_ID, eventID);
        intent.putExtra(EXTRA_START_MILLIS, startMillis);
        intent.putExtra(EXTRA_END_MILLIS, endMillis);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_setter);
        app = (NoMissingApp) getApplicationContext();
        requestCode = getIntent().getIntExtra(EXTRA_REQUEST_CODE, 0);
        
        calendarID = getIntent().getLongExtra(EXTRA_CALENDAR_ID, 0);
        eventID = getIntent().getLongExtra(EXTRA_EVENT_ID, 0);
        startMillis = getIntent().getLongExtra(EXTRA_START_MILLIS, 0);
        endMillis = getIntent().getLongExtra(EXTRA_END_MILLIS, 0);

        getEvent();
        getReminder();
        setStartCalendar();
        setEndCalendar();
        setReminderCalendar();
        setActionBar();
        setViewComponents();
    }

    private void getEvent() {
        if (requestCode == REQUEST_CREATE) 
            event = new Event();
        if (requestCode == REQUEST_UPDATE)
            event = DaoFactory.getEventDaoFactory(calendarID).createEventDao(this).find(eventID, calendarID, startMillis, endMillis);
    }

    private void getReminder() {
        if (requestCode == REQUEST_CREATE) 
            reminder = new Reminder();
        if (requestCode == REQUEST_UPDATE)
            reminder = DaoFactory.getSQLiteDaoFactory().createReminderDao(this).find(calendarID, eventID);
        reminderID = reminder.getId();
    }

    private void setStartCalendar() {
        startCalendar = Calendar.getInstance();
        if (startMillis != 0)
            startCalendar.setTimeInMillis(startMillis);
        if (eventID != 0)
            startCalendar.setTimeInMillis(event.getStartTime());
    }

    private void setEndCalendar() {
        endCalendar = Calendar.getInstance();
        if (endMillis != 0)
            endCalendar.setTimeInMillis(endMillis);
        if (eventID != 0)
            endCalendar.setTimeInMillis(event.getEndTime());
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
        editTextReminderDate.setEnabled(reminder.isEnabled());
        editTextReminderTime.setEnabled(reminder.isEnabled());
        checkBoxRemider.setChecked(reminder.isEnabled());

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
            EventSetterActivity.this.finish();
            return true;

        case R.id.action_set_event_ok:
            if (!isValidDateRange(startCalendar, endCalendar, reminderCalendar))
                return false;
            if (requestCode == REQUEST_CREATE) {
                createEvent();
                setResult(RESULT_CREATE);   
            }
            if (requestCode == REQUEST_UPDATE) {
                updateEvent();
                setResult(RESULT_UPDATE);               
            }
            finish();
            return true;

        case R.id.action_delete_event:
            if (requestCode == REQUEST_CREATE) {
                EventSetterActivity.this.setResult(RESULT_CANCEL);  
            }
            if (requestCode == REQUEST_UPDATE) {
                deleteEvent();
                setResult(RESULT_DELETE);              
            }            
            finish();
            return true;

        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.editTextStartDate:
            new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
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
            new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            startCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            startCalendar.set(Calendar.MINUTE, minute);
                            editTextStartTime.setText(timeFormatter.format(startCalendar.getTime()));
                        }
                    }, 
                    startCalendar.get(Calendar.HOUR_OF_DAY),
                    startCalendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(this)).show();
            break;

        case R.id.editTextEndDate:
            new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
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
            new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
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
            new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
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
            new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
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

        if (!checkBoxRemider.isChecked())
            return;

        reminder.setCalendarID(calendarID);
        reminder.setEventID(eventID);
        reminder.setReminderTime(reminderCalendar.getTimeInMillis());
        reminder.setEnabled(checkBoxRemider.isChecked());
        reminder.setTriggered(false);
        reminder.setCreatedAt(currentTime);
        reminder.setUpdatedAt(currentTime);
        reminderID = DaoFactory.getSQLiteDaoFactory().createReminderDao(this).insert(reminder);
        reminder.setId(reminderID);
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
        reminder.setUpdatedAt(currentTime);
        DaoFactory.getEventDaoFactory(calendarID).createEventDao(this).update(event);

        if (!checkBoxRemider.isChecked())
            return;
        
        reminder.setCalendarID(calendarID);
        reminder.setEventID(eventID);
        reminder.setReminderTime(reminderCalendar.getTimeInMillis());
        reminder.setEnabled(checkBoxRemider.isChecked());
        reminder.setTriggered(false);
        reminder.setUpdatedAt(currentTime);
        
        if (reminder.getId() == 0) {
            reminderID = DaoFactory.getSQLiteDaoFactory().createReminderDao(this).insert(reminder);
            reminder.setId(reminderID);
        } else {
            DaoFactory.getSQLiteDaoFactory().createReminderDao(this).update(reminder);
            AlarmHandlerFactory.createReminderAlarmHandler(this).cancelAlarm(reminder);
        }
        AlarmHandlerFactory.createReminderAlarmHandler(this).setAlarm(reminder);
        
        getTTSAudio();
    }
    
    private void deleteEvent() {    
        AlarmHandlerFactory.createReminderAlarmHandler(this).cancelAlarm(reminder);
        
        if (reminder.getAudio() != null) {
            File audio = new File(Uri.parse(reminder.getAudio()).getPath());
            audio.delete();
        }
        
        DaoFactory.getEventDaoFactory(calendarID).createEventDao(this).delete(eventID);  
        DaoFactory.getSQLiteDaoFactory().createReminderDao(this).delete(reminderID);  
    }

    private boolean isValidDateRange(Calendar startCalendar,
            Calendar endCalendar, Calendar reminderCalendar) {
        boolean isValidDateRange = true;

        if (startCalendar.compareTo(endCalendar) > 0) {
            isValidDateRange = false;
            ToastMaker.toast(this, R.string.start_time_setting_error);
        }

        if (checkBoxRemider.isChecked()) {
            if (reminderCalendar.compareTo(startCalendar) > 0) {
                isValidDateRange = false;
                ToastMaker.toast(this, R.string.reminder_time_setting_error);
            }
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
        Bundle extras = TextToSpeechService.getExtras(Category.REMINDER, reminder.getId(), 
                reminder.getStringForTTS(event), app.getSettings().getTTSSpeaker(), 
                app.getSettings().getTTSVolume(), app.getSettings().getTTSSpeed(), "wav");

        TextToSpeechService.startService(this, extras);
    }

}
