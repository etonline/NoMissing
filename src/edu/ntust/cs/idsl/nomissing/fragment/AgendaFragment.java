package edu.ntust.cs.idsl.nomissing.fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.activity.EventSetterActivity;
import edu.ntust.cs.idsl.nomissing.adapter.AgendaListAdapter;
import edu.ntust.cs.idsl.nomissing.dao.DaoFactory;
import edu.ntust.cs.idsl.nomissing.global.NoMissingApp;
import edu.ntust.cs.idsl.nomissing.model.Event;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
@SuppressLint({ "NewApi", "SimpleDateFormat" })
public class AgendaFragment extends Fragment implements OnItemClickListener {

    private NoMissingApp app;
    private long calenderID;

    private List<Event> events;
    private TextView textViewAgenda;
    private ListView listViewAgenda;

    public AgendaFragment() {
    }

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

        textViewAgenda = (TextView) rootView.findViewById(R.id.textViewAgenda);
        listViewAgenda = (ListView) rootView.findViewById(R.id.listViewAgenda);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM ");
        Calendar calendar = Calendar.getInstance();
        textViewAgenda.setText(simpleDateFormat.format(calendar.getTime()) + getString(R.string.home_fragment_agenda));

        listViewAgenda.setOnItemClickListener(this);

        getEvents();

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_home, menu);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        long eventID = id;
        long startMillis = events.get(position).getStartTime();
        long endMillis = events.get(position).getEndTime();
        startActivityForResult(EventSetterActivity.getAction(getActivity(), EventSetterActivity.REQUEST_UPDATE, 
                calenderID, eventID, startMillis, endMillis), EventSetterActivity.REQUEST_UPDATE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        getEvents();
    }

    private void getEvents() {
        Calendar calendar = Calendar.getInstance();
        long startMillis = calendar.getTimeInMillis();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        long endMillis = calendar.getTimeInMillis();

        events = DaoFactory.getEventDaoFactory(calenderID).createEventDao(getActivity()).find(calenderID, startMillis, endMillis);
        AgendaListAdapter adapter = new AgendaListAdapter(getActivity(), events);
        listViewAgenda.setAdapter(adapter);
    }

}
