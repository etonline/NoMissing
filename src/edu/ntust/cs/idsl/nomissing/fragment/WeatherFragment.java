package edu.ntust.cs.idsl.nomissing.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.activity.WeatherActivity;
import edu.ntust.cs.idsl.nomissing.activity.WeatherSetterActivity;
import edu.ntust.cs.idsl.nomissing.adapter.WeatherExpandListAdapter;
import edu.ntust.cs.idsl.nomissing.service.weather.WeatherService;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
public class WeatherFragment extends Fragment implements OnChildClickListener {

    private static final String ACTION_REFRESH_VISIBLE = "edu.ntust.cs.idsl.nomissing.action.RECEIVE_REFRESH_VISIBLE";
    private static final String ACTION_REFRESH_INVISIBLE = "edu.ntust.cs.idsl.nomissing.action.RECEIVE_REFRESH_INVISIBLE";
    private ExpandableListView expandableListView;
    private MenuItem menuItemRefresh;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_REFRESH_VISIBLE);
        intentFilter.addAction(ACTION_REFRESH_INVISIBLE);
        getActivity().registerReceiver(refreshVisibilityReceiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(refreshVisibilityReceiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_weather, container, false);

        expandableListView = (ExpandableListView) rootView.findViewById(R.id.expandableListView);
        expandableListView.setAdapter(new WeatherExpandListAdapter(getActivity()));
        expandableListView.setOnChildClickListener(this);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_weather, menu);
        menuItemRefresh = (MenuItem) menu.findItem(R.id.action_refresh_data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.action_refresh_data:
            WeatherService.startService(getActivity(), new Bundle());
            return true;
        case R.id.action_set_weather:
            WeatherSetterActivity.startActivity(getActivity());
            return true;

        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        WeatherActivity.startActivity(getActivity(), (int) id);
        return true;
    }

    private final BroadcastReceiver refreshVisibilityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                final String action = intent.getAction();

                if (ACTION_REFRESH_VISIBLE.equals(action)) {
                    menuItemRefresh.setVisible(true);
                    return;
                }
                
                if (ACTION_REFRESH_INVISIBLE.equals(action)) {
                    menuItemRefresh.setVisible(false);
                }
            } 
        }
    };  
    
    public static Intent getActionReceiveRefreshInvisible(Context context) {
        Intent intent = new Intent();
        intent.setAction(ACTION_REFRESH_INVISIBLE);
        return intent;
    }
    
    public static Intent getActionReceiveRefreshVisible(Context context) {
        Intent intent = new Intent();
        intent.setAction(ACTION_REFRESH_VISIBLE);
        return intent;
    }

}
