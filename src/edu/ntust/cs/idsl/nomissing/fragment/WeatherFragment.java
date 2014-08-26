package edu.ntust.cs.idsl.nomissing.fragment;

import android.app.ProgressDialog;
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
import edu.ntust.cs.idsl.nomissing.global.NoMissingApp;
import edu.ntust.cs.idsl.nomissing.receiver.ServerResponseReceiver;
import edu.ntust.cs.idsl.nomissing.service.GetWeatherDataService;
import edu.ntust.cs.idsl.nomissing.util.ToastMaker;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
public class WeatherFragment extends Fragment implements OnChildClickListener {

	private NoMissingApp app;
	private ExpandableListView expandableListView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		app = (NoMissingApp)getActivity().getApplicationContext();
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
	
	private void setBroadcastReceiver() {
		IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ServerResponseReceiver.ACTION_GET_WEATHER_DATE_START);
        intentFilter.addAction(ServerResponseReceiver.ACTION_GET_WEATHER_DATE_PROGRASS_UPDATE);
        intentFilter.addAction(ServerResponseReceiver.ACTION_GET_WEATHER_DATE_FINISH);
        intentFilter.addAction(ServerResponseReceiver.ACTION_GET_WEATHER_DATE_FAILURE);
		getActivity().registerReceiver(GetWeatherDataReceiver, intentFilter);	
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_weather, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_refresh_weather_data:
			getActivity().startService(new Intent(getActivity(), GetWeatherDataService.class));
			return true;
		case R.id.action_set_weather:
			startActivity(new Intent(getActivity(), SetWeatherActivity.class));
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}	

	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
		Intent intent = new Intent(getActivity(), WeatherActivity.class);
		intent.putExtra("id", (int)id);
		getActivity().startActivity(intent);	
		return true;
	}
	
	private final BroadcastReceiver GetWeatherDataReceiver = new BroadcastReceiver() {
		final int progressMax = 100;
		ProgressDialog progressDialog;
		
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(ServerResponseReceiver.ACTION_GET_WEATHER_DATE_START)) {
				ToastMaker.toast(getActivity(), R.string.toast_refresh_weather_data_start);
				progressDialog = new ProgressDialog(getActivity());
				progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				progressDialog.setMessage(getString(R.string.progress_dialog_refresh_data));
				progressDialog.setMax(progressMax);
				progressDialog.setCancelable(false);
				progressDialog.show();			
			}
			
			if(intent.getAction().equals(ServerResponseReceiver.ACTION_GET_WEATHER_DATE_PROGRASS_UPDATE)) {
				double progress = intent.getDoubleExtra("progress", 0);
				progressDialog.setProgress((int)progress);
			}
			
			if(intent.getAction().equals(ServerResponseReceiver.ACTION_GET_WEATHER_DATE_FINISH)) {
				progressDialog.dismiss();
				ToastMaker.toast(getActivity(), R.string.toast_refresh_weather_data_finish);
			}
			
			if(intent.getAction().equals(ServerResponseReceiver.ACTION_GET_WEATHER_DATE_FAILURE)) {
				progressDialog.dismiss();
				ToastMaker.toast(getActivity(), R.string.toast_refresh_weather_data_failure);
			}
		}
	};

}
