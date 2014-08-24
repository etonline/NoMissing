package edu.ntust.cs.idsl.nomissing.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import edu.ntust.cs.idsl.nomissing.constant.EventReminder;

public class EventReminderSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {
	
	private Context context;
	private List<EventReminder> eventReminders;
	
	public EventReminderSpinnerAdapter(Context context) {
		this.context = context;
		eventReminders = EventReminder.getEventReminders();
	}

	@Override
	public int getCount() {
		return eventReminders.size();
	}

	@Override
	public Object getItem(int position) {
		return eventReminders.get(position).getMinutes();
	}

	@Override
	public long getItemId(int position) {
		return eventReminders.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(android.R.layout.simple_list_item_1, null);
		}

		TextView textViewFreq = (TextView) convertView.findViewById(android.R.id.text1);
		textViewFreq.setText(eventReminders.get(position).getName());

		return convertView;
	}

}
