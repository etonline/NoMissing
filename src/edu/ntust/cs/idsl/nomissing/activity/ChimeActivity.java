package edu.ntust.cs.idsl.nomissing.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.dao.sqlite.SQLiteDaoFactory;
import edu.ntust.cs.idsl.nomissing.model.Chime;
import edu.ntust.cs.idsl.nomissing.service.MediaPlayerService;

public class ChimeActivity extends Activity {
	
	private Chime chime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chime);
		
		int chimeID = getIntent().getIntExtra("id", -1);
		chime = SQLiteDaoFactory.createChimeDao(this).find(chimeID);
		openChimeDialog(chime);
			
		if (!chime.isRepeating()) {
			chime.setTriggered(true);
			SQLiteDaoFactory.createChimeDao(this).update(chime);
		}			
	}

	private void openChimeDialog(final Chime chime) {
		AlertDialog cityWeatherDialog = new AlertDialog.Builder(this)
		.setTitle(getTitle())
		.setIcon(R.drawable.ic_action_alarms)
		.setMessage(chime.getTime())
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
				startTTSAudio(chime.getAudio());
			}
		});
		
		cityWeatherDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
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
