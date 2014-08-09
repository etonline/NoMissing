package edu.ntust.cs.idsl.nomissing.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.dao.EventDAO;
import edu.ntust.cs.idsl.nomissing.fragment.CalendarFragment;
import edu.ntust.cs.idsl.nomissing.global.NoMissingApp;
import edu.ntust.cs.idsl.nomissing.model.Event;
import edu.ntust.cs.idsl.nomissing.util.ToastMaker;

@SuppressLint({ "NewApi", "SimpleDateFormat" })
public class SetEventActivity extends Activity implements OnClickListener {

	public static final String TAG = SetEventActivity.class.getSimpleName();
	private NoMissingApp app;
	
	private EditText editTextTitle;
	private EditText editTextLocation;
	private EditText editTextStartDate;
	private EditText editTextStartTime;
	private EditText editTextEndDate;
	private EditText editTextEndTime;
	private EditText editTextDescription;
	
	private long calendarID;
	private long eventID;
	private long startMillis;
	private long endMillis;
	
	private EventDAO eventDAO;
	
	final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/M/d (EEE)");
	final SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a");
	
	private Calendar startCalendar;
	private Calendar endCalendar;
	
	private Event event;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_event);
		
		app = (NoMissingApp)getApplicationContext();
		
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);	
		
        editTextTitle = (EditText)findViewById(R.id.editTextTitle);
        editTextLocation = (EditText)findViewById(R.id.editTextLocation);
        editTextStartDate = (EditText)findViewById(R.id.editTextStartDate);
        editTextStartTime = (EditText)findViewById(R.id.editTextStartTime);
        editTextEndDate = (EditText)findViewById(R.id.editTextEndDate);
        editTextEndTime = (EditText)findViewById(R.id.editTextEndTime);
        editTextDescription = (EditText)findViewById(R.id.editTextDescription);
        
        editTextStartDate.setOnClickListener(this);
        editTextStartTime.setOnClickListener(this);
        editTextEndDate.setOnClickListener(this);
        editTextEndTime.setOnClickListener(this);
        
        calendarID = getIntent().getLongExtra("calendarID", -1);
		eventID = getIntent().getLongExtra("eventID", -1);
		startMillis = getIntent().getLongExtra("startMillis", -1);
		endMillis = getIntent().getLongExtra("endMillis", -1);
		
		eventDAO = EventDAO.getInstance(this);
		event = (eventID != -1 ) ? eventDAO.find(eventID, startMillis, endMillis) : new Event();
		
		editTextTitle.setText(event.getTitle());
		editTextLocation.setText(event.getLocation());
		
		startCalendar = Calendar.getInstance();
		if(eventID != -1) startCalendar.setTimeInMillis(event.getStart());
		editTextStartDate.setText(dateFormatter.format(startCalendar.getTime()));
		editTextStartTime.setText(timeFormatter.format(startCalendar.getTime()));
		
		endCalendar = Calendar.getInstance();
		if(eventID != -1) endCalendar.setTimeInMillis(event.getEnd());
		editTextEndDate.setText(dateFormatter.format(endCalendar.getTime()));
		editTextEndTime.setText(timeFormatter.format(endCalendar.getTime()));
		
		editTextDescription.setText(event.getDescription());
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
				ToastMaker.toast(this, "����]�m���~");
				return false;
			}
			
			if (eventID == -1) {
				createEvent();
				SetEventActivity.this.setResult(CalendarFragment.RESULT_CREATE);
			} else {
				updateEvent();	
				SetEventActivity.this.setResult(CalendarFragment.RESULT_UPDATE);
			}
			SetEventActivity.this.finish();	
			return true;
			
		case R.id.action_delete_event:
			if (eventID == -1) {
				SetEventActivity.this.setResult(CalendarFragment.RESULT_CANCEL);
			} else {
				eventDAO.delete(eventID);
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
			
		default:
			break;
		}
		
	}
	
	private void createEvent() {
		event.setCalendarID(calendarID);
		event.setTitle(editTextTitle.getText().toString());
		event.setLocation(editTextLocation.getText().toString());
		event.setStart(startCalendar.getTimeInMillis());
		event.setEnd(endCalendar.getTimeInMillis());
		event.setDescription(editTextDescription.getText().toString());
		long eventID = eventDAO.insert(event);
		ToastMaker.toast(this, String.valueOf(eventID));
	}	
	
	private void updateEvent() {
		event.setTitle(editTextTitle.getText().toString());
		event.setLocation(editTextLocation.getText().toString());
		event.setStart(startCalendar.getTimeInMillis());
		event.setEnd(endCalendar.getTimeInMillis());
		event.setDescription(editTextDescription.getText().toString());
		eventDAO.update(event);		
	}
	
	private boolean isValidDateRange(Calendar startCalendar, Calendar endCalendar) {
		boolean isValidDateRange = false;
		if (startCalendar.compareTo(endCalendar) == -1) {
			isValidDateRange = true;
		}
		return isValidDateRange;
	}

}
