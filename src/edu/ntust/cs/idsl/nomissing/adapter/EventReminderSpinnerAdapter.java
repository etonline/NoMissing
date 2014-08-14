package edu.ntust.cs.idsl.nomissing.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.model.EventReminder;

@SuppressLint("UseSparseArrays")
public class EventReminderSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {
	
	private Context context;
	private List<Map.Entry<Integer, EventReminder>> reminderList;
	
	public EventReminderSpinnerAdapter(Context context) {
		this.context = context;
		HashMap<Integer, EventReminder> reminderMap = new LinkedHashMap<Integer, EventReminder>();
		reminderMap.put(R.string.event_reminder_zero_minute, EventReminder.ZERO_MINUTE);
		reminderMap.put(R.string.event_reminder_one_minute, EventReminder.ONE_MINUTE);
		reminderMap.put(R.string.event_reminder_five_minutes, EventReminder.FIVE_MINUTES);
		reminderMap.put(R.string.event_reminder_ten_minutes, EventReminder.TEN_MINUTES);
		reminderMap.put(R.string.event_reminder_fifteen_minutes, EventReminder.FIFTEEN_MINUTES);
		reminderMap.put(R.string.event_reminder_twenty_minutes, EventReminder.TWENTY_MINUTES);
		reminderMap.put(R.string.event_reminder_twenty_five_minutes, EventReminder.TWENTY_FIVE_MINUTES);
		reminderMap.put(R.string.event_reminder_thirty_minutes, EventReminder.THIRTY_MINUTES);
		reminderMap.put(R.string.event_reminder_forty_five_minutes, EventReminder.FORTY_FIVE_MINUTES);
		reminderMap.put(R.string.event_reminder_one_hour, EventReminder.ONE_HOUR);
		reminderMap.put(R.string.event_reminder_two_hours, EventReminder.TWO_HOURS);
		reminderMap.put(R.string.event_reminder_three_hours, EventReminder.THREE_HOURS);
		reminderMap.put(R.string.event_reminder_half_day, EventReminder.HALF_DAY);
		reminderMap.put(R.string.event_reminder_one_day, EventReminder.ONE_DAY);
		reminderMap.put(R.string.event_reminder_two_days, EventReminder.TWO_DAY);
		reminderMap.put(R.string.event_reminder_one_week, EventReminder.ONE_WEEK);
		reminderList = new ArrayList<Map.Entry<Integer, EventReminder>>();
		reminderList.addAll(reminderMap.entrySet());
	}

	@Override
	public int getCount() {
		return reminderList.size();
	}

	@Override
	public Object getItem(int position) {
		return reminderList.get(position).getValue().getMinutes();
	}

	@Override
	public long getItemId(int position) {
		
		return reminderList.get(position).getKey();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(android.R.layout.simple_list_item_1, null);
		}

		TextView textViewFreq = (TextView) convertView.findViewById(android.R.id.text1);
		textViewFreq.setText(context.getText(reminderList.get(position).getKey()));

		return convertView;
	}

}
