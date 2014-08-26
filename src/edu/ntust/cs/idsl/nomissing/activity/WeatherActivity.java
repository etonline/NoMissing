package edu.ntust.cs.idsl.nomissing.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.dao.DaoFactory;
import edu.ntust.cs.idsl.nomissing.dao.sqlite.SQLiteDaoFactory;
import edu.ntust.cs.idsl.nomissing.global.NoMissingApp;
import edu.ntust.cs.idsl.nomissing.model.Weather;
import edu.ntust.cs.idsl.nomissing.service.MediaPlayerService;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
public class WeatherActivity extends Activity {
	
	private NoMissingApp app;
	private Weather weather;
	public static final String EXTRA_ID = "id"; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather);
		app = (NoMissingApp) getApplicationContext();
		
		int cityID = getIntent().getIntExtra("id", 0);
		weather = DaoFactory.getSQLiteDaoFactory().createWeatherDao(this).find(cityID);
		
		openCityWeatherDialog(weather);
	}
	
	private void openCityWeatherDialog(final Weather weather) {
		AlertDialog cityWeatherDialog = new AlertDialog.Builder(this)
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
				if (app.getSettings().isWeatherTTSEnabled()) 
					startTTSAudio(weather.getAudio());
			}
		});
		
		cityWeatherDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				if (app.getSettings().isWeatherTTSEnabled()) 
					stopTTSAudio();
				finish();
			}
		});
		
		cityWeatherDialog.show();
	}
	
	private void startTTSAudio(String audio) {
		Intent startIntent = new Intent(this, MediaPlayerService.class);
		startIntent.setAction(MediaPlayerService.ACTION_PLAY);
		startIntent.putExtra("audio", audio);		
		startService(startIntent);			
	}
	
	private void stopTTSAudio() {
		Intent stopIntent = new Intent(this, MediaPlayerService.class);
		stopIntent.setAction(MediaPlayerService.ACTION_STOP);
		startService(stopIntent);			
	}
	
}
