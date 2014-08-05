package edu.ntust.cs.idsl.nomissing.fragment;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.activity.SetWeatherActivity;
import edu.ntust.cs.idsl.nomissing.adapter.WeatherListAdapter;
import edu.ntust.cs.idsl.nomissing.dao.WeatherDAO;
import edu.ntust.cs.idsl.nomissing.model.Weather;
import edu.ntust.cs.idsl.nomissing.service.MediaPlayerService;

@SuppressLint("NewApi")
public class WeatherFragment extends ListFragment {

	private WeatherDAO weatherDAO;
	private List<Weather> weatherList;
	private WeatherListAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		
		weatherDAO = WeatherDAO.getInstance(getActivity());
		weatherList = weatherDAO.findAll();

		adapter = new WeatherListAdapter(getActivity(), weatherList);
		setListAdapter(adapter);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_weather, container, false);
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
	public void onListItemClick(ListView l, View v, int position, long id) {	
		Weather weather = weatherDAO.find((int)id);
		
		Intent startIntent = new Intent(getActivity(), MediaPlayerService.class);
		startIntent.setAction(MediaPlayerService.ACTION_PLAY);
		startIntent.putExtra("audioURL", weather.getAudio());		
		getActivity().startService(startIntent);
		
		openCityWeatherDialog(weather);
	}
	
	private void openCityWeatherDialog(Weather weather) {
		AlertDialog cityWeatherDialog = new AlertDialog.Builder(getActivity())
		.setTitle(weather.getCity())
		.setIcon(android.R.drawable.ic_dialog_info)
		.setMessage(weather.getMemo())
		.setNegativeButton(R.string.weather_fragment_alert_dialog_close,
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			})
		.create();

		cityWeatherDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				Intent stopIntent = new Intent(getActivity(), MediaPlayerService.class);
				stopIntent.setAction(MediaPlayerService.ACTION_STOP);
				getActivity().startService(stopIntent);	
			}
		});
		
		cityWeatherDialog.show();			
	}

}
