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
import edu.ntust.cs.idsl.nomissing.util.ToastMaker;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
@SuppressLint({ "NewApi", "SimpleDateFormat" })
public class HomeFragment extends Fragment implements OnItemClickListener {

    private static final int REQUEST_SET = 0;
    public static final int RESULT_CREATE = 1;
    public static final int RESULT_UPDATE = 2;
    public static final int RESULT_CANCEL = 3;
    public static final int RESULT_DELETE = 4;

    private NoMissingApp app;
    private long calenderID;

    private List<Event> events;
    private TextView textViewAgenda;
    private ListView listViewAgenda;

    public HomeFragment() {
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

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

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
            setEvent(calenderID, 0, 0, 0);
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
        setEvent(calenderID, eventID, startMillis, endMillis);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode != REQUEST_SET)
            return;

        getEvents();

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

    private void setEvent(long calenderID, long eventID, long startMillis, long endMillis) {
        Intent intent = EventSetterActivity.getAction(getActivity(), calenderID, eventID, startMillis, endMillis);
        startActivityForResult(intent, REQUEST_SET);
    }

}
