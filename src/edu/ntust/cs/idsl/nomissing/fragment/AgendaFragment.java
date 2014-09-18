package edu.ntust.cs.idsl.nomissing.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.activity.EventSetterActivity;
import edu.ntust.cs.idsl.nomissing.adapter.AgendaExpandListAdapter;
import edu.ntust.cs.idsl.nomissing.dao.DaoFactory;
import edu.ntust.cs.idsl.nomissing.global.NoMissingApp;
import edu.ntust.cs.idsl.nomissing.model.Event;
import edu.ntust.cs.idsl.nomissing.util.CalendarHelper;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
@SuppressLint({ "NewApi", "SimpleDateFormat" })
public class AgendaFragment extends Fragment implements OnClickListener, OnChildClickListener {

    private NoMissingApp app;
    private long calenderID;

    private TextView textViewAgenda;
    private ExpandableListView expandableListViewAgenda;
    private ImageView imageViewPrevMonth;
    private ImageView imageViewNextMonth;
    
    private List<List<Event>> agendaEventsList;
    private Calendar calendar;
    
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM ");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        app = (NoMissingApp) getActivity().getApplicationContext();
        calenderID = app.getSettings().getCalendarID();  
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_agenda, container, false);

        calendar = Calendar.getInstance();
        
        textViewAgenda = (TextView) rootView.findViewById(R.id.textViewAgenda);
        textViewAgenda.setText(formatter.format(calendar.getTime()) + getString(R.string.home_fragment_agenda));  
        
        imageViewPrevMonth = (ImageView) rootView.findViewById(R.id.imageViewPrevMonth);
        imageViewNextMonth = (ImageView) rootView.findViewById(R.id.imageViewNextMonth);
        imageViewPrevMonth.setOnClickListener(this);
        imageViewNextMonth.setOnClickListener(this);
        
        expandableListViewAgenda = (ExpandableListView) rootView.findViewById(R.id.expandableListViewAgenda);
        expandableListViewAgenda.setOnChildClickListener(this);

        getAgendaEvents(calendar);

        return rootView;
    }

    private void getAgendaEvents(Calendar calendar) {
        List<Date> eventDateList = getEventDateList(calendar);
        agendaEventsList  = getAgendaEventsList(eventDateList);
        expandableListViewAgenda.setAdapter(new AgendaExpandListAdapter(getActivity(), eventDateList, agendaEventsList));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_agenda, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.action_add_event:
            startActivityForResult(EventSetterActivity.getAction(getActivity(), EventSetterActivity.REQUEST_CREATE, 
                    calenderID, 0, 0, 0), EventSetterActivity.REQUEST_CREATE);
            return true;

        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.imageViewPrevMonth:
            setPrevMonth();
            break;
            
        case R.id.imageViewNextMonth:
            setNextMonth();
            break;

        default:
            break;
        }
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v,
            int groupPosition, int childPosition, long id) {
        long eventID = id;
        long startMillis = agendaEventsList.get(groupPosition).get(childPosition).getStartTime();
        long endMillis = agendaEventsList.get(groupPosition).get(childPosition).getEndTime();
        startActivityForResult(EventSetterActivity.getAction(getActivity(),
                EventSetterActivity.REQUEST_UPDATE, calenderID, eventID,
                startMillis, endMillis), EventSetterActivity.REQUEST_UPDATE);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        calendar = CalendarHelper.setCurrentTime(calendar);
        getAgendaEvents(calendar);
    }
    
    private List<Date> getEventDateList(Calendar calendar) {
        List<Date> eventDateList = new ArrayList<Date>(); 
        
        Calendar endOfMonth = Calendar.getInstance();
        endOfMonth.setTime(calendar.getTime());
        endOfMonth = CalendarHelper.setEndOfMonth(endOfMonth);
        
        while (calendar.getTimeInMillis() <= endOfMonth.getTimeInMillis()) {
            long startMillis = calendar.getTimeInMillis();
            calendar = CalendarHelper.setEndOfDate(calendar);
            long endMillis = CalendarHelper.setEndOfDate(calendar).getTimeInMillis();
            
            if (DaoFactory.getEventDaoFactory(calenderID).createEventDao(getActivity())
                    .find(calenderID, startMillis, endMillis).size() > 0 ) {
                eventDateList.add(calendar.getTime());
            }
                
            if (calendar.get(Calendar.DAY_OF_MONTH) 
                    == calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                break;
            } 
            
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            calendar = CalendarHelper.setStartOfDate(calendar);
        }

        return eventDateList;
    }
    
    private List<List<Event>> getAgendaEventsList(List<Date> eventDateList) {
        List<List<Event>> agendaEventsList = new ArrayList<List<Event>>();
        
        Calendar calendar = Calendar.getInstance();
        for (Date eventDate : eventDateList) {
            calendar.setTime(eventDate);
            long startMillis = CalendarHelper.setStartOfDate(calendar).getTimeInMillis();
            long endMillis = CalendarHelper.setEndOfDate(calendar).getTimeInMillis();
            
            List<Event> events = DaoFactory.getEventDaoFactory(calenderID).createEventDao(getActivity()).find(calenderID, startMillis, endMillis);
            agendaEventsList.add(events);
        }
        
        return agendaEventsList;
    }
    
    private void setPrevMonth()  {        
        calendar = CalendarHelper.setPrevMonth(calendar);
        calendar = CalendarHelper.setStartOfMonth(calendar);
        
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar = CalendarHelper.setCurrentTime(calendar);
            imageViewPrevMonth.setVisibility(View.INVISIBLE);
        }
        
        textViewAgenda.setText(formatter.format(calendar.getTime()) + getString(R.string.home_fragment_agenda));  
        getAgendaEvents(calendar);        
    }
    
    private void setNextMonth() {
        calendar = CalendarHelper.setNextMonth(calendar);
        calendar = CalendarHelper.setStartOfMonth(calendar);
        
        if (imageViewPrevMonth.getVisibility() == View.INVISIBLE) {
            imageViewPrevMonth.setVisibility(View.VISIBLE);
        }
        
        textViewAgenda.setText(formatter.format(calendar.getTime()) + getString(R.string.home_fragment_agenda));  
        getAgendaEvents(calendar);        
    }

}
