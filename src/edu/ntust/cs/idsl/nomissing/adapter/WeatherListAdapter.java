package edu.ntust.cs.idsl.nomissing.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import edu.ntust.cs.idsl.nomissing.model.Weather;

public class WeatherListAdapter extends BaseAdapter {

	private Context context;
	private List<Weather> weatherList;
	
	public WeatherListAdapter(Context context, List<Weather> weatherList) {
		this.context = context;
		this.weatherList = weatherList;
	}

	@Override
	public int getCount() {
		return weatherList.size();
	}

	@Override
	public Object getItem(int position) {
		return weatherList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return weatherList.get(position).getCityID();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(android.R.layout.simple_list_item_1, null);
		}

		TextView textViewCity = (TextView) convertView.findViewById(android.R.id.text1);
		textViewCity.setText(weatherList.get(position).getCity());

		return convertView;
	}

}
