package edu.ntust.cs.idsl.nomissing.fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import android.accounts.Account;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.activity.SetEventActivity;
import edu.ntust.cs.idsl.nomissing.adapter.AgendaListAdapter;
import edu.ntust.cs.idsl.nomissing.dao.EventDAO;
import edu.ntust.cs.idsl.nomissing.model.Event;
import edu.ntust.cs.idsl.nomissing.util.AccountUtil;
import edu.ntust.cs.idsl.nomissing.util.CalendarUtil;
import edu.ntust.cs.idsl.nomissing.util.ToastMaker;

@SuppressLint({ "NewApi", "SimpleDateFormat" })
public class HomeFragment extends Fragment implements OnItemClickListener {
	
	private static final int REQUEST_SET = 0;
	public static final int RESULT_CREATE = 1;
	public static final int RESULT_UPDATE = 2;
	public static final int RESULT_CANCEL = 3;
	public static final int RESULT_DELETE = 4;
	
	private long calenderID;
	
	private EventDAO eventDAO;
	private List<Event> events;
	private TextView textViewAgenda;
	private ListView listViewAgenda;
	
	public HomeFragment(){}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
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
 
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        
        textViewAgenda = (TextView)rootView.findViewById(R.id.textViewAgenda);
        listViewAgenda = (ListView)rootView.findViewById(R.id.listViewAgenda);
        
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM ");
        Calendar calendar = Calendar.getInstance();
        textViewAgenda.setText(simpleDateFormat.format(calendar.getTime()) + getString(R.string.home_fragment_agenda));
        

		listViewAgenda.setOnItemClickListener(this);
		
		getEvents();
		
        return rootView;
    }
	


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		long eventID = id;
    	long startMillis = events.get(position).getStart();
    	long endMillis = events.get(position).getEnd();		
    	setEvent(calenderID, eventID, startMillis, endMillis);
	}	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (requestCode != REQUEST_SET) return;
		
		getEvents();
		
		switch (resultCode) {
		case RESULT_CREATE:
			ToastMaker.toast(getActivity(), "�إ�");
			break;
		case RESULT_UPDATE:
			ToastMaker.toast(getActivity(), "��s");
			break;
		case RESULT_CANCEL:
			ToastMaker.toast(getActivity(), "����");
			break;
		case RESULT_DELETE:
			ToastMaker.toast(getActivity(), "�R��");
			break;

		default:
			break;
		}
		
	}		
	
	private void getEvents() {
		Calendar calendar = Calendar.getInstance();
		long startMillis = calendar.getTimeInMillis();
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		long endMillis = calendar.getTimeInMillis();
		
		eventDAO = EventDAO.getInstance(getActivity());
		events = eventDAO.find(startMillis, endMillis);		
        AgendaListAdapter adapter = new AgendaListAdapter(getActivity(), events);
		listViewAgenda.setAdapter(adapter);
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
