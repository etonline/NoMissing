package edu.ntust.cs.idsl.nomissing.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.constant.NavDrawerItem;

public class NavDrawerListAdapter extends BaseAdapter {

	private Context context;
	private List<NavDrawerItem> navItems;

	public NavDrawerListAdapter(Context context, List<NavDrawerItem> navItems) {
		this.context = context;
		this.navItems = navItems;
	}

	@Override
	public int getCount() {
		return navItems.size();
	}

	@Override
	public Object getItem(int position) {
		return navItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.drawer_list_item, null);
		}

		ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
		TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
		TextView txtCount = (TextView) convertView.findViewById(R.id.counter);

		imgIcon.setImageResource(navItems.get(position).getIcon());
		txtTitle.setText(navItems.get(position).getTitle());

		// displaying count
		// check whether it set visible or not
		if (navItems.get(position).isCounterVisible()) {
			txtCount.setText(String.valueOf(navItems.get(position).getCount()));
		} else {
			// hide the counter view
			txtCount.setVisibility(View.GONE);
		}

		return convertView;
	}

}
