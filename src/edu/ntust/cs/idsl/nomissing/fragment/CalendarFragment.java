package edu.ntust.cs.idsl.nomissing.fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import android.accounts.Account;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
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
import edu.ntust.cs.idsl.nomissing.dao.EventDAO;
import edu.ntust.cs.idsl.nomissing.model.Event;
import edu.ntust.cs.idsl.nomissing.util.AccountUtil;
import edu.ntust.cs.idsl.nomissing.util.CalendarUtil;
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
	
	private long calenderID;
	
	private CaldroidFragment caldroidFragment;
	
	private TextView textViewDate;
	private ImageView imageViewAdd;
	private ListView listViewEvents;
	private EventListAdapter adapter;
	
	private EventDAO eventDAO;
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
		
//		Intent addAccountIntent = new Intent(android.provider.Settings.ACTION_ADD_ACCOUNT)
//	    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//	    addAccountIntent.putExtra(Settings.EXTRA_ACCOUNT_TYPES, new String[] {"com.google"});
//	    getActivity().startActivity(addAccountIntent); 
		
		eventDAO = EventDAO.getInstance(getActivity());
	
		// get calendars
		Account account = AccountUtil.getGoogleAccount(getActivity());
		
		HashMap<Long, String> calendars = CalendarUtil.getCalendar(getActivity(), account.name);
		
		for(Entry<Long, String> entry : calendars.entrySet()) {
		    long key = entry.getKey();
		    String value = entry.getValue();
		    Log.v("TAG", key + " | " + value);
		    calenderID = key;
		    
		    //ToastMaker.toast(getActivity(), key + " | " + value);
		}

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
    	long startMillis = dayEvents.get(position).getStart();
    	long endMillis = dayEvents.get(position).getEnd();		
    	setEvent(calenderID, eventID, startMillis, endMillis);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (requestCode != REQUEST_SET) return;
		
		setCaldroid();
		
		switch (resultCode) {
		case RESULT_CREATE:
			ToastMaker.toast(getActivity(), "建立");
			break;
		case RESULT_UPDATE:
			ToastMaker.toast(getActivity(), "更新");
			break;
		case RESULT_CANCEL:
			ToastMaker.toast(getActivity(), "取消");
			break;
		case RESULT_DELETE:
			ToastMaker.toast(getActivity(), "刪除");
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
        		calendar.set(year, month - 1, 1, 0, 0);
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
		long startMillis = calendar.getTimeInMillis();	
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		long endMillis = calendar.getTimeInMillis();

		monthEvents = eventDAO.find(startMillis, endMillis);
		for(Event event : monthEvents) {
			calendar.setTimeInMillis(event.getStart());
			caldroidFragment.setBackgroundResourceForDate(R.color.indianred, calendar.getTime());
			caldroidFragment.setTextColorForDate(R.color.white, calendar.getTime());
			checkWithinNextDay(calendar, event.getEnd());
		}  
	}
	
	private void setDayEvents(Calendar calendar) {
		Calendar day = Calendar.getInstance();
		day.setTime(calendar.getTime());
		day.set(Calendar.HOUR_OF_DAY, 0);
		day.set(Calendar.MINUTE, 0);
		day.set(Calendar.SECOND, 0);
		
		textViewDate.setText(formatter.format(calendar.getTime()));
    	long startMillis = day.getTimeInMillis();
    	day.add(Calendar.DAY_OF_YEAR, 1);
    	long endMillis = day.getTimeInMillis();
    	
    	dayEvents = eventDAO.find(startMillis, endMillis);
    	adapter = new EventListAdapter(getActivity(), dayEvents);
    	listViewEvents.setAdapter(adapter);  		
	}
	
	private void checkWithinNextDay(Calendar calendar, long endMillis) {
		calendar.add(Calendar.DAY_OF_YEAR, 1);
    	calendar.set(Calendar.HOUR_OF_DAY, 0);
    	calendar.set(Calendar.MINUTE, 0);
    	calendar.set(Calendar.SECOND, 0);
    	
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
		Intent intent = new Intent(getActivity(), SetEventActivity.class);
		intent.putExtra("calendarID", calenderID);
		if (eventID != 0) intent.putExtra("eventID", eventID);
		if (startMillis != 0) intent.putExtra("startMillis", startMillis);
		if (endMillis != 0) intent.putExtra("endMillis", endMillis);
		startActivityForResult(intent, REQUEST_SET);			
	}	
	
}
