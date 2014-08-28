package edu.ntust.cs.idsl.nomissing.adapter;

import java.text.SimpleDateFormat;
import java.util.List;

import android.R.raw;
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

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
@SuppressLint("SimpleDateFormat")
public class AgendaListAdapter extends BaseAdapter {

	private Context context;
	private List<Event> events;
	
	public AgendaListAdapter(Context context, List<Event> events) {
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
		return events.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.agenda_list_item, null);
		}

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd HH:mm");

		TextView textViewTitle = (TextView) convertView.findViewById(R.id.textViewTitle);
		TextView textViewLocation = (TextView) convertView.findViewById(R.id.textViewLocation);
		TextView textViewTime = (TextView) convertView.findViewById(R.id.textViewTime);
		
		String startTime = simpleDateFormat.format(events.get(position).getStartTime());
		String endTime = simpleDateFormat.format(events.get(position).getEndTime());
		
		textViewTitle.setText(events.get(position).getTitle());
		textViewLocation.setText(events.get(position).getLocation());
		textViewTime.setText(startTime + " - " + endTime);

		return convertView;
	}

}
