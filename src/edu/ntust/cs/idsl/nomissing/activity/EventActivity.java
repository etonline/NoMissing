package edu.ntust.cs.idsl.nomissing.activity;

import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.dao.sqlite.SQLiteDaoFactory;
import edu.ntust.cs.idsl.nomissing.global.NoMissingApp;
import edu.ntust.cs.idsl.nomissing.model.Event;
import edu.ntust.cs.idsl.nomissing.model.Reminder;
import edu.ntust.cs.idsl.nomissing.service.MediaPlayerService;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
@SuppressLint("NewApi")
public class EventActivity extends Activity {

	private NoMissingApp app;
	private Event event;
	private Reminder reminder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event);
		
		app = (NoMissingApp) getApplicationContext();
		
		long reminderID = getIntent().getLongExtra("id", 0);
		reminder = SQLiteDaoFactory.createReminderDao(this).find(reminderID);
//		event = SQLiteDaoFactory.createEventDao(this).find(reminder.getEventID());
		
		openEventDialog(event);
	}
	
	
	private void openEventDialog(final Event event) {
		SimpleDateFormat formatter = new SimpleDateFormat("a h:mm");
		StringBuilder message = new StringBuilder();
		message.append(getString(R.string.event_title) + event.getTitle() + "\n");
		if (!event.getLocation().isEmpty()) 
			message.append(getString(R.string.event_location) + event.getLocation() + "\n");
		message.append(getString(R.string.event_start_time) + formatter.format(event.getStartTime()) + "\n");
		message.append(getString(R.string.event_end_time) + formatter.format(event.getEndTime()));
		
		AlertDialog cityWeatherDialog = new AlertDialog.Builder(this)
		.setTitle(getTitle())
		.setIcon(android.R.drawable.ic_dialog_info)
		.setMessage(message)
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
				startTTSAudio(reminder.getAudio());
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
