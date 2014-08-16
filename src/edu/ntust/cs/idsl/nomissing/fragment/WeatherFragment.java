package edu.ntust.cs.idsl.nomissing.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import edu.ntust.cs.idsl.nomissing.adapter.WeatherExpandListAdapter;
import edu.ntust.cs.idsl.nomissing.dao.SQLiteDAOFactory;
import edu.ntust.cs.idsl.nomissing.dao.WeatherDAO;
import edu.ntust.cs.idsl.nomissing.global.NoMissingApp;
import edu.ntust.cs.idsl.nomissing.model.Weather;
import edu.ntust.cs.idsl.nomissing.service.MediaPlayerService;

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
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_weather, container, false);
        
        expandableListView = (ExpandableListView)rootView.findViewById(R.id.expandableListView);
        WeatherExpandListAdapter weatherExpandListAdapter = new WeatherExpandListAdapter(getActivity());
        expandableListView.setAdapter(weatherExpandListAdapter);
        expandableListView.setOnChildClickListener(this);
        
        return rootView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_weather, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_set_weather:
			startActivity(new Intent(getActivity(), SetWeatherActivity.class));
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
	}	

	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
		Weather weather = SQLiteDAOFactory.getWeatherDAO(getActivity()).find((int)id);
		openCityWeatherDialog(weather);
		return true;
	}
	
	private void openCityWeatherDialog(final Weather weather) {
		AlertDialog cityWeatherDialog = new AlertDialog.Builder(getActivity())
		.setTitle(weather.getCity())
		.setIcon(android.R.drawable.ic_dialog_info)
		.setMessage(weather.getMemo())
		.setNegativeButton(R.string.alert_dialog_close,
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			})
		.create();

		cityWeatherDialog.setOnShowListener(new DialogInterface.OnShowListener() {
			@Override
			public void onShow(DialogInterface dialog) {
				if (app.userSettings.isWeatherTTSEnabled()) 
					startTTSAudio(weather.getAudio());
			}
		});
		
		cityWeatherDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				if (app.userSettings.isWeatherTTSEnabled()) 
					stopTTSAudio();
			}
		});
		
		cityWeatherDialog.show();
	}
	
	private void startTTSAudio(String audioURL) {
		Intent startIntent = new Intent(getActivity(), MediaPlayerService.class);
		startIntent.setAction(MediaPlayerService.ACTION_PLAY);
		startIntent.putExtra("audioURL", audioURL);		
		getActivity().startService(startIntent);			
	}
	
	private void stopTTSAudio() {
		Intent stopIntent = new Intent(getActivity(), MediaPlayerService.class);
		stopIntent.setAction(MediaPlayerService.ACTION_STOP);
		getActivity().startService(stopIntent);			
	}

}
