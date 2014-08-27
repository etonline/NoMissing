package edu.ntust.cs.idsl.nomissing.fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.activity.SetEventActivity;
import edu.ntust.cs.idsl.nomissing.adapter.EventListAdapter;
import edu.ntust.cs.idsl.nomissing.dao.DaoFactory;
import edu.ntust.cs.idsl.nomissing.dao.sqlite.SQLiteDaoFactory;
import edu.ntust.cs.idsl.nomissing.global.NoMissingApp;
import edu.ntust.cs.idsl.nomissing.model.Event;
import edu.ntust.cs.idsl.nomissing.util.ToastMaker;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
@SuppressLint({ "NewApi", "SimpleDateFormat" })
public class CalendarFragment extends CaldroidFragment implements OnClickListener, OnItemClickListener {
	
	private static final int REQUEST_SET = 0;
	public static final int RESULT_CREATE = 1;
	public static final int RESULT_UPDATE = 2;
	public static final int RESULT_CANCEL = 3;
	public static final int RESULT_DELETE = 4;
	
	private NoMissingApp app;
	private long calenderID;
	
	private CaldroidFragment caldroidFragment;
	
	private TextView textViewDate;
	private ImageView imageViewAdd;
	private ListView listViewEvents;
	private EventListAdapter adapter;
	
	private List<Event> monthEvents;
	private List<Event> dayEvents;
	
	final SimpleDateFormat formatter = new SimpleDateFormat("yyyy/M/d (E)");
	private Calendar selectedDate;

	public CalendarFragment() {
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		
		app = (NoMissingApp)getActivity().getApplicationContext();
		calenderID = app.getSettings().getCalendarID();
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);
        
        textViewDate = (TextView)rootView.findViewById(R.id.textViewDate);
        imageViewAdd = (ImageView)rootView.findViewById(R.id.imageViewAdd);
        listViewEvents = (ListView)rootView.findViewById(R.id.listViewEvents);
        listViewEvents.setOnItemClickListener(this);
        imageViewAdd.setOnClickListener(this);
        setCaldroid();
       
        return rootView;
    }
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_calendar, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_search_date:
			searchDate();
			return true;
		
		case R.id.action_add_event:
			setEvent(calenderID, 0, 0, 0);
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageViewAdd:
	    	long startMillis = selectedDate.getTimeInMillis();
	    	long endMillis = selectedDate.getTimeInMillis();			
	    	setEvent(calenderID, 0, startMillis, endMillis);
			break;

		default:
			break;
		}
		
	}	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		long eventID = id;
    	long startMillis = dayEvents.get(position).getStartTime();
    	long endMillis = dayEvents.get(position).getEndTime();		
    	setEvent(calenderID, eventID, startMillis, endMillis);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (requestCode != REQUEST_SET) return;
		
		setCaldroid();
		
		switch (resultCode) {
		case RESULT_CREATE:
//			ToastMaker.toast(getActivity(), "建立");
			break;
		case RESULT_UPDATE:
//			ToastMaker.toast(getActivity(), "更新");
			break;
		case RESULT_CANCEL:
//			ToastMaker.toast(getActivity(), "取消");
			break;
		case RESULT_DELETE:
//			ToastMaker.toast(getActivity(), "刪除");
			break;

		default:
			break;
		}
		
	}	
	
	private void setCaldroid() {
		caldroidFragment = new CaldroidFragment();

        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        caldroidFragment.setArguments(args);

        FragmentManager fragManager = getActivity().getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction t = fragManager.beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();

        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
            	selectedDate.setTime(date);
            	setDayEvents(selectedDate);
            }

            @Override
            public void onChangeMonth(int month, int year) {          
            	Calendar calendar = Calendar.getInstance();
            	calendar.set(year, month - 2, 1);
            	setMonthEvents(calendar);
            }

            @Override
            public void onLongClickDate(Date date, View view) {
            }

            @Override
            public void onCaldroidViewCreated() {
            	selectedDate = Calendar.getInstance();
            	setDayEvents(selectedDate);
            }

        };

        caldroidFragment.setCaldroidListener(listener);	
	}
	
	private void setMonthEvents(Calendar calendar) {
		long startMillis = getStartOfDate(calendar).getTimeInMillis();	
		calendar.add(Calendar.MONTH, 2);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		long endMillis = getEndOfDate(calendar).getTimeInMillis();

		monthEvents = DaoFactory.getEventDaoFactory(calenderID).createEventDao(getActivity()).find(calenderID, startMillis, endMillis);
		for(Event event : monthEvents) {
			calendar.setTimeInMillis(event.getStartTime());
			caldroidFragment.setBackgroundResourceForDate(R.color.indianred, calendar.getTime());
			caldroidFragment.setTextColorForDate(R.color.white, calendar.getTime());
			checkWithinNextDay(calendar, event.getEndTime());
		}  
	}
	
	private void setDayEvents(Calendar calendar) {
		textViewDate.setText(formatter.format(calendar.getTime()));
		
		Calendar day = Calendar.getInstance();
		day.setTime(calendar.getTime());
    	long startMillis = getStartOfDate(day).getTimeInMillis();
    	long endMillis = getEndOfDate(day).getTimeInMillis();
    	
    	dayEvents = monthEvents = DaoFactory.getEventDaoFactory(calenderID).createEventDao(getActivity()).find(calenderID, startMillis, endMillis);
    	adapter = new EventListAdapter(getActivity(), dayEvents);
    	listViewEvents.setAdapter(adapter);  		
	}
	
	private void checkWithinNextDay(Calendar calendar, long endMillis) {
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		calendar = getStartOfDate(calendar);
    	
    	if (calendar.getTimeInMillis() <= endMillis) {
			caldroidFragment.setBackgroundResourceForDate(R.color.indianred, calendar.getTime());
			caldroidFragment.setTextColorForDate(R.color.white, calendar.getTime());    
			checkWithinNextDay(calendar, endMillis);
    	}
	}
	
	private void searchDate() {
		new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				selectedDate.set(year, monthOfYear, dayOfMonth);
	        	caldroidFragment.moveToDate(selectedDate.getTime());
	        	setDayEvents(selectedDate);
			}
		},
        selectedDate.get(Calendar.YEAR), 
        selectedDate.get(Calendar.MONTH), 
        selectedDate.get(Calendar.DAY_OF_MONTH)).show();	
	}

	private void setEvent(long calenderID, long eventID, long startMillis, long endMillis) {
		Log.i("TAG", String.valueOf(eventID));
		Log.i("TAG", String.valueOf(calenderID));
		Log.i("TAG", String.valueOf(startMillis));
		Log.i("TAG", String.valueOf(endMillis));
		Intent intent = new Intent(getActivity(), SetEventActivity.class);
		intent.putExtra("calendarID", calenderID);
		if (eventID != 0) intent.putExtra("eventID", eventID);
		if (startMillis != 0) intent.putExtra("startMillis", startMillis);
		if (endMillis != 0) intent.putExtra("endMillis", endMillis);
		startActivityForResult(intent, REQUEST_SET);			
	}	
	
	private Calendar getStartOfDate(Calendar calendar) {
    	calendar.set(Calendar.HOUR_OF_DAY, 0);
    	calendar.set(Calendar.MINUTE, 0);
    	calendar.set(Calendar.SECOND, 0);	
    	calendar.set(Calendar.MILLISECOND, 0);	
		return calendar;
	}
	
	private Calendar getEndOfDate(Calendar calendar) {
    	calendar.set(Calendar.HOUR_OF_DAY, 23);
    	calendar.set(Calendar.MINUTE, 59);
    	calendar.set(Calendar.SECOND, 59);	
    	calendar.set(Calendar.MILLISECOND, 999);	
		return calendar;		
	}
	
}
