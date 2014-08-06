package edu.ntust.cs.idsl.nomissing.fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import android.accounts.Account;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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

@SuppressLint({ "NewApi", "SimpleDateFormat" })
public class CalendarFragment extends CaldroidFragment implements OnItemClickListener {
	
	private CaldroidFragment caldroidFragment;
	
	private TextView textViewDate;
	private ListView listViewEvents;
	private EventListAdapter adapter;
	
	private EventDAO eventDAO;
	private List<Event> monthEvents;
	private List<Event> dayEvents;
	
	public CalendarFragment() {
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		eventDAO = EventDAO.getInstance(getActivity());
		
		// get calendars
		Account account = AccountUtil.getGoogleAccount(getActivity());
		HashMap<Long, String> calendars = CalendarUtil.getCalendar(getActivity(), account.name);
		
		for(Entry<Long, String> entry : calendars.entrySet()) {
		    long key = entry.getKey();
		    String value = entry.getValue();
		    Log.v("TAG", key + " | " + value);
		}

	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);
        
        textViewDate = (TextView)rootView.findViewById(R.id.textViewDate);
        listViewEvents = (ListView)rootView.findViewById(R.id.listViewEvents);
        listViewEvents.setOnItemClickListener(this);
        setCaldroid();
       
        return rootView;
    }
	
	private void setCaldroid() {
		caldroidFragment = new CaldroidFragment();
		final SimpleDateFormat formatter = new SimpleDateFormat("yyyy / M / d (EEE)");

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
            	textViewDate.setText(formatter.format(date));
            	
            	Calendar calendar = Calendar.getInstance();
            	calendar.setTime(date);
            	long startMillis = calendar.getTimeInMillis();
            	
            	calendar.add(Calendar.DAY_OF_YEAR, 1);
            	long endMillis = calendar.getTimeInMillis();
            	
            	dayEvents = eventDAO.find(startMillis, endMillis);
            	adapter = new EventListAdapter(getActivity(), dayEvents);
            	listViewEvents.setAdapter(adapter);        
            }

            @Override
            public void onChangeMonth(int month, int year) {          	
        		Calendar calendar = Calendar.getInstance();
        		calendar.set(Calendar.MONTH, month - 1);
        		calendar.set(Calendar.DAY_OF_MONTH, 1);
        		long startMillis = calendar.getTimeInMillis();	
        		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        		long endMillis = calendar.getTimeInMillis();

        		monthEvents = eventDAO.find(startMillis, endMillis);
        		for(Event event : monthEvents) {
        			calendar.setTimeInMillis(event.getStart());
        			caldroidFragment.setBackgroundResourceForDate(R.color.blue, calendar.getTime());
        			caldroidFragment.setTextColorForDate(R.color.white, calendar.getTime());
        		}       
            }

            @Override
            public void onLongClickDate(Date date, View view) {
            }

            @Override
            public void onCaldroidViewCreated() {
            	textViewDate.setText(formatter.format(new Date()));
            }

        };

        caldroidFragment.setCaldroidListener(listener);	
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		long eventID = id;
    	long startMillis = dayEvents.get(position).getStart();
    	long endMillis = dayEvents.get(position).getEnd();		
		
		Intent intent = new Intent(getActivity(), SetEventActivity.class);
		intent.putExtra("eventID", eventID);
		intent.putExtra("startMillis", startMillis);
		intent.putExtra("endMillis", endMillis);
		
		startActivityForResult(intent, 1);
	}


}
