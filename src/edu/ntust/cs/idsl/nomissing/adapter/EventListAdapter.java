package edu.ntust.cs.idsl.nomissing.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.model.Event;

@SuppressLint("SimpleDateFormat")
public class EventListAdapter extends BaseAdapter {

	private Context context;
	private List<Event> events;
	
	public EventListAdapter(Context context, List<Event> events) {
		this.context = context;
		this.events = events;
	}

	@Override
	public int getCount() {
		return events.size();
	}

	@Override
	public Object getItem(int position) {
		return events.get(position);
	}

	@Override
	public long getItemId(int position) {
		return events.get(position).getEventID();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(android.R.layout.simple_list_item_1, null);
		}

		TextView textViewEvent = (TextView) convertView.findViewById(android.R.id.text1);
		textViewEvent.setText(events.get(position).getTitle());

		return convertView;
	}

}