package edu.ntust.cs.idsl.nomissing.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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
        
        editTextStartDate.setOnClickListener(this);
        editTextStartTime.setOnClickListener(this);
        editTextEndDate.setOnClickListener(this);
        editTextEndTime.setOnClickListener(this);
        
		eventID = getIntent().getLongExtra("eventID", -1);
		startMillis = getIntent().getLongExtra("startMillis", -1);
		endMillis = getIntent().getLongExtra("endMillis", -1);
		
		eventDAO = EventDAO.getInstance(this);
		
		event = eventDAO.find(eventID, startMillis, endMillis);
		ToastMaker.toast(this, event.getLocation());
		
		editTextTitle.setText(event.getTitle());
		editTextLocation.setText(event.getLocation());
		
		startCalendar = Calendar.getInstance();
		startCalendar.setTimeInMillis(event.getStart());
		editTextStartDate.setText(dateFormatter.format(startCalendar.getTime()));
		editTextStartTime.setText(timeFormatter.format(startCalendar.getTime()));
		
		endCalendar = Calendar.getInstance();
		endCalendar.setTimeInMillis(event.getEnd());
		editTextEndDate.setText(dateFormatter.format(endCalendar.getTime()));
		editTextEndTime.setText(timeFormatter.format(endCalendar.getTime()));
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
			if (eventID == -1) {
//				createChime();
//				SetChimeActivity.this.setResult(ChimeFragment.RESULT_CREATE);
//				SetChimeActivity.this.finish();
			} else {
				Log.v("TAG", String.valueOf(eventID));
				event.setEventID(eventID);
				event.setTitle(editTextTitle.getText().toString());
				event.setLocation(editTextLocation.getText().toString());
				event.setStart(startCalendar.getTimeInMillis());
				event.setEnd(endCalendar.getTimeInMillis());
				eventDAO.update(event);
//				SetEventActivity.this.setResult(CalendarFragment.RESULT_UPDATE);
				SetEventActivity.this.finish();			
			}
			return true;
			
		case R.id.action_delete_chime:
			if (eventID == -1) {
//				SetChimeActivity.this.setResult(ChimeFragment.RESULT_CANCEL);
//				SetChimeActivity.this.finish();	
			} else {
//				chimeDAO.delete(chimeID);
//				AlarmUtil.cancelChimeAlarm(this, chime);
//				SetChimeActivity.this.setResult(ChimeFragment.RESULT_DELETE);
//				SetChimeActivity.this.finish();	
			}
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

}
