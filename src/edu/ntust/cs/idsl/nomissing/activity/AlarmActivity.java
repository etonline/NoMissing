package edu.ntust.cs.idsl.nomissing.activity;

import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.R.id;
import edu.ntust.cs.idsl.nomissing.R.layout;
import edu.ntust.cs.idsl.nomissing.dao.ChimeDAO;
import edu.ntust.cs.idsl.nomissing.dao.WeatherDAO;
import edu.ntust.cs.idsl.nomissing.model.Chime;
import edu.ntust.cs.idsl.nomissing.model.Weather;
import edu.ntust.cs.idsl.nomissing.service.MediaPlayerService;
import edu.ntust.cs.idsl.nomissing.util.NotificationUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class AlarmActivity extends Activity implements OnClickListener {

	public static final String ACTION_CHIME_ALARM_DIALOG = "edu.ntust.cs.idsl.nomissing.action.CHIME_ALARM_DIALOG";
	public static final String ACTION_WEATHER_ALARM_DIALOG = "edu.ntust.cs.idsl.nomissing.action.WEATHER_ALARM_DIALOG";
	
	private TextView mTextView;
	private Button mButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm);
		
		mTextView = (TextView)findViewById(R.id.textView);
		mButton = (Button)findViewById(R.id.button);
		
		mButton.setOnClickListener(this);
		
		if (getIntent().getAction().equals(ACTION_CHIME_ALARM_DIALOG)) {
			int chimeID = getIntent().getIntExtra("id", -1);
			ChimeDAO chimeDAO =  ChimeDAO.getInstance(this);
			Chime chime = chimeDAO.find(chimeID);
			
			setTitle("語音報時");
			mTextView.setText(chime.getTime());

			Intent newIntent = new Intent(this, MediaPlayerService.class);
			newIntent.setAction(MediaPlayerService.ACTION_PLAY);
			newIntent.putExtra("audioURL", chime.getAudio());
			startService(newIntent);
			
			if (!chime.isRepeating()) {
				chime.setTriggered(true);
				chimeDAO.update(chime);
			}			
		}
		
		if (getIntent().getAction().equals(ACTION_WEATHER_ALARM_DIALOG)) {
			int cityID = getIntent().getIntExtra("id", -1);
			WeatherDAO weatherDAO =  WeatherDAO.getInstance(this);
			Weather weather = weatherDAO.find(cityID);
			
			setTitle("語音天氣 - " + weather.getCity());
			mTextView.setText(weather.getMemo());

			Intent newIntent = new Intent(this, MediaPlayerService.class);
			newIntent.setAction(MediaPlayerService.ACTION_PLAY);
			newIntent.putExtra("audioURL", weather.getAudio());
			startService(newIntent);		
		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button:
			Intent newIntent = new Intent(this, MediaPlayerService.class);
			newIntent.setAction(MediaPlayerService.ACTION_STOP);
			startService(newIntent);
			finish();
			break;

		default:
			break;
		}
		
	}

}
