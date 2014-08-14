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
import edu.ntust.cs.idsl.nomissing.model.EventFreq;

@SuppressLint("UseSparseArrays")
public class EventFreqSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {
	
	private Context context;
	private List<Map.Entry<Integer, EventFreq>> freqList;
	
	public EventFreqSpinnerAdapter(Context context) {
		this.context = context;
		HashMap<Integer, EventFreq> freqMap = new LinkedHashMap<Integer, EventFreq>();
		freqMap.put(R.string.event_frequency_single, EventFreq.SINGLE);
		freqMap.put(R.string.event_frequency_daily, EventFreq.DAILY);
		freqMap.put(R.string.event_frequency_weekly, EventFreq.WEEKLY);
		freqMap.put(R.string.event_frequency_monthly, EventFreq.MONTHLY);
		freqMap.put(R.string.event_frequency_yearly, EventFreq.YEARLY);
		freqList = new ArrayList<Map.Entry<Integer, EventFreq>>();
		freqList.addAll(freqMap.entrySet());
	}

	@Override
	public int getCount() {
		return freqList.size();
	}

	@Override
	public Object getItem(int position) {
		return freqList.get(position).getValue().getRrule();
	}

	@Override
	public long getItemId(int position) {
		
		return freqList.get(position).getKey();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(android.R.layout.simple_list_item_1, null);
		}

		TextView textViewFreq = (TextView) convertView.findViewById(android.R.id.text1);
		textViewFreq.setText(context.getText(freqList.get(position).getKey()));

		return convertView;
	}

}
