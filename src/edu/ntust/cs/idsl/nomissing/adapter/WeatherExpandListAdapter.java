package edu.ntust.cs.idsl.nomissing.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import edu.ntust.cs.idsl.nomissing.dao.SQLiteDaoFactory;
import edu.ntust.cs.idsl.nomissing.dao.WeatherDao;
import edu.ntust.cs.idsl.nomissing.model.City;
import edu.ntust.cs.idsl.nomissing.model.Region;
import edu.ntust.cs.idsl.nomissing.model.Weather;

public class WeatherExpandListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private List<Region> groupList;
	private List<List<City>> childList;
	
	public WeatherExpandListAdapter(Context context) {
		this.context = context;
		
		groupList = new ArrayList<Region>();
		groupList.add(Region.NORTHERN);
		groupList.add(Region.CENTRAL);
		groupList.add(Region.SOUTHERN);
		groupList.add(Region.EASTERN);
		groupList.add(Region.OFFSHORE_ISLANDS);
		
		List<City> northernRegion = new ArrayList<City>();
		northernRegion.add(City.TAIPEI_CITY);
		northernRegion.add(City.NEW_TAIPEI_CITY);
		northernRegion.add(City.KEELUNG_CITY);
		northernRegion.add(City.TAOYUAN_COUNTY);
		northernRegion.add(City.HSINCHU_COUNTY);
		northernRegion.add(City.HSINCHU_CITY);
		northernRegion.add(City.YILAN_COUNTY);
		
		List<City> centralRegion = new ArrayList<City>();
		centralRegion.add(City.MIAOLI_COUNTY);
		centralRegion.add(City.TAICHUNG_CITY);
		centralRegion.add(City.CHANGHUA_COUNTY);
		centralRegion.add(City.NANTOU_COUNTY);
		centralRegion.add(City.YUNLIN_COUNTY);
		
		List<City> southernRegion = new ArrayList<City>();
		southernRegion.add(City.CHIAYI_COUNTY);
		southernRegion.add(City.CHIAYI_CITY);
		southernRegion.add(City.TAINAN_CITY);
		southernRegion.add(City.KAOHSIUNG_CITY);
		southernRegion.add(City.PINGTUNG_COUNTY);
		
		List<City> easternRegion = new ArrayList<City>();
		easternRegion.add(City.HUALIEN_COUNTY);
		easternRegion.add(City.TAITUNG_COUNTY);
		
		List<City> offshoreIslandsRegion = new ArrayList<City>();
		offshoreIslandsRegion.add(City.PENGHU_COUNTY);
		offshoreIslandsRegion.add(City.KINMEN_COUNTY);
		offshoreIslandsRegion.add(City.LIENCHIANG_COUNTY);
		
		childList = new ArrayList<List<City>>();
		childList.add(northernRegion);
		childList.add(centralRegion);
		childList.add(southernRegion);
		childList.add(easternRegion);
		childList.add(offshoreIslandsRegion);
	}

	@Override
	public int getGroupCount() {
		return groupList.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return childList.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groupList.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return childList.get(groupPosition).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childList.get(groupPosition).get(childPosition).getCityID();
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(android.R.layout.simple_expandable_list_item_2, null);
		}
		
		TextView textViewRegion = (TextView)convertView.findViewById(android.R.id.text1);
		TextView textViewRegionCities = (TextView)convertView.findViewById(android.R.id.text2);
		
		textViewRegion.setText(groupList.get(groupPosition).getName());
		textViewRegionCities.setText(groupList.get(groupPosition).getCities());		
		
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
//			convertView = mInflater.inflate(android.R.layout.simple_expandable_list_item_1, null);
			convertView = mInflater.inflate(android.R.layout.simple_expandable_list_item_2, null);
		}
		
		TextView textViewCity = (TextView)convertView.findViewById(android.R.id.text1);
		textViewCity.setText(context.getString(childList.get(groupPosition).get(childPosition).getCityName()));
		
		TextView textViewWeather = (TextView)convertView.findViewById(android.R.id.text2);
		
		WeatherDao weatherDAO = SQLiteDaoFactory.getWeatherDao(context);
		Weather weather = weatherDAO.find(childList.get(groupPosition).get(childPosition).getCityID());
		textViewWeather.setSingleLine(true);
		textViewWeather.setEllipsize(TruncateAt.END);
		textViewWeather.setText(weather.getMemo());
		
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
