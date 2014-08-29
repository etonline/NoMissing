package edu.ntust.cs.idsl.nomissing.activity;

import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.dao.DaoFactory;
import edu.ntust.cs.idsl.nomissing.model.Event;
import edu.ntust.cs.idsl.nomissing.model.Reminder;
import edu.ntust.cs.idsl.nomissing.service.MediaPlayerService;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
@SuppressLint({ "NewApi", "SimpleDateFormat" })
public class EventActivity extends Activity {

	private static final String ACTION = "edu.ntust.cs.idsl.nomissing.action.EventActivity";
	private static final String EXTRA_REMINDER_ID = "edu.ntust.cs.idsl.nomissing.extra.REMINDER_ID";
	private static final SimpleDateFormat formatter = new SimpleDateFormat("a h:mm");
	private Reminder reminder;
	private Event event;
	
	public static void startActivity(Context context, long reminderID) {
		Intent intent = new Intent(context, EventActivity.class);
		intent.setAction(ACTION);
		intent.putExtra(EXTRA_REMINDER_ID, reminderID);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
		context.startActivity(intent);	
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event);
		
		long reminderID = getIntent().getLongExtra(EXTRA_REMINDER_ID, 0);
		reminder = DaoFactory.getSQLiteDaoFactory().createReminderDao(this).find(reminderID);
		event = DaoFactory.getEventDaoFactory(reminder.getCalendarID()).createEventDao(this).find(reminder.getEventID());
		
		openEventDialog(event);
	}
	
	
	private void openEventDialog(final Event event) {
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
				MediaPlayerService.startActionPlay(EventActivity.this, reminder.getAudio());
			}
		});
		
		cityWeatherDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				MediaPlayerService.startActionStop(EventActivity.this, reminder.getAudio());
				finish();
			}
		});
		
		cityWeatherDialog.show();
	}

}
