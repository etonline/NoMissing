package edu.ntust.cs.idsl.nomissing.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.activity.SetWeatherActivity;
import edu.ntust.cs.idsl.nomissing.activity.WeatherActivity;
import edu.ntust.cs.idsl.nomissing.adapter.WeatherExpandListAdapter;
import edu.ntust.cs.idsl.nomissing.model.ProgressStatus;
import edu.ntust.cs.idsl.nomissing.notification.NotificationHandlerFactory;
import edu.ntust.cs.idsl.nomissing.receiver.ServerResponseReceiver;
import edu.ntust.cs.idsl.nomissing.service.weather.WeatherService;
import edu.ntust.cs.idsl.nomissing.util.ToastMaker;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
public class WeatherFragment extends Fragment implements OnChildClickListener {

	private ExpandableListView expandableListView;
	private MenuItem menuItemRefresh;
	
	private void setBroadcastReceiver() {
		IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ServerResponseReceiver.ACTION_GET_WEATHER_DATE_START);
        intentFilter.addAction(ServerResponseReceiver.ACTION_GET_WEATHER_DATE_PROGRASS_UPDATE);
        intentFilter.addAction(ServerResponseReceiver.ACTION_GET_WEATHER_DATE_FINISH);
        intentFilter.addAction(ServerResponseReceiver.ACTION_GET_WEATHER_DATE_FAILURE);
		getActivity().registerReceiver(GetWeatherDataReceiver, intentFilter);	
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		setBroadcastReceiver();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_weather, container, false);
        
        expandableListView = (ExpandableListView)rootView.findViewById(R.id.expandableListView);
        expandableListView.setAdapter(new WeatherExpandListAdapter(getActivity()));
        expandableListView.setOnChildClickListener(this);

        return rootView;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_weather, menu);
		menuItemRefresh = (MenuItem) menu.findItem(R.id.action_refresh_weather_data);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_refresh_weather_data:
			WeatherService.startService(getActivity(), new Bundle());
			return true;
		case R.id.action_set_weather:
			SetWeatherActivity.startActivity(getActivity());
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}	

	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
		WeatherActivity.startActivity(getActivity(), (int)id);
		return true;
	}	
	
	private final BroadcastReceiver GetWeatherDataReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(ServerResponseReceiver.ACTION_GET_WEATHER_DATE_START)) {
				menuItemRefresh.setVisible(false);
				ToastMaker.toast(context, R.string.toast_refresh_weather_data_start);	
				ProgressStatus progressStatus = new ProgressStatus(
						ProgressStatus.START,
						context.getString(R.string.action_refresh_weather_data),
						context.getString(R.string.toast_refresh_weather_data_start), 0);
				NotificationHandlerFactory.createProgressNotificationHandler(context).sendNotification(progressStatus);		
			}
			
			if(intent.getAction().equals(ServerResponseReceiver.ACTION_GET_WEATHER_DATE_PROGRASS_UPDATE)) {
				double progress = intent.getDoubleExtra("progress", 0);
				ProgressStatus progressStatus = new ProgressStatus(
						ProgressStatus.PROGRESS_UPDATE,
						context.getString(R.string.action_refresh_weather_data),
						context.getString(R.string.toast_refresh_weather_data_start), (int)progress);
				NotificationHandlerFactory.createProgressNotificationHandler(context).sendNotification(progressStatus);
			}
			
			if(intent.getAction().equals(ServerResponseReceiver.ACTION_GET_WEATHER_DATE_FINISH)) {
				ProgressStatus progressStatus = new ProgressStatus(
						ProgressStatus.FINISH,
						context.getString(R.string.action_refresh_weather_data),
						context.getString(R.string.toast_refresh_weather_data_finish), 100);
				NotificationHandlerFactory.createProgressNotificationHandler(context).sendNotification(progressStatus);
				menuItemRefresh.setVisible(true);
				ToastMaker.toast(context, R.string.toast_refresh_weather_data_finish);
			}
			
			if(intent.getAction().equals(ServerResponseReceiver.ACTION_GET_WEATHER_DATE_FAILURE)) {
				menuItemRefresh.setVisible(true);
				ToastMaker.toast(context, R.string.toast_refresh_weather_data_failure);
			}
		}
	};

}
