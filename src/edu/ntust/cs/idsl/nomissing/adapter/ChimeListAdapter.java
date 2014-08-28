package edu.ntust.cs.idsl.nomissing.adapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import edu.ntust.cs.idsl.nomissing.model.Chime;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
@SuppressLint("SimpleDateFormat")
public class ChimeListAdapter extends BaseAdapter {

	private Context context;
	private List<Chime> chimes;
	
	public ChimeListAdapter(Context context, List<Chime> chimes) {
		this.context = context;
		this.chimes = chimes;
	}

	@Override
	public int getCount() {
		return chimes.size();
	}

	@Override
	public Object getItem(int position) {
		return chimes.get(position);
	}

	@Override
	public long getItemId(int position) {
		return chimes.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.chime_list_item, null);
		}

		CheckBox checkBoxEnabled = (CheckBox) convertView.findViewById(R.id.checkBoxEnabled);
		TextView textViewTime = (TextView) convertView.findViewById(R.id.textViewTime);
		TextView textViewRepeat = (TextView) convertView.findViewById(R.id.textViewRepeat);

		checkBoxEnabled.setChecked(chimes.get(position).isEnabled());
		textViewTime.setText(chimes.get(position).getTime());
		textViewRepeat.setText(chimes.get(position).getRepeating());

		return convertView;
	}

}
