package edu.ntust.cs.idsl.nomissing.adapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import edu.ntust.cs.idsl.nomissing.model.Event;
import edu.ntust.cs.idsl.nomissing.R;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
public class AgendaExpandListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<Date> eventDateList;
    private List<List<Event>> agendaEventsList;
    private static final SimpleDateFormat groupFormatter = new SimpleDateFormat("MM.dd (EE)");
    private static final SimpleDateFormat childFormatter = new SimpleDateFormat("MM/dd HH:mm");

    public AgendaExpandListAdapter(Context context, List<Date> eventDateList, List<List<Event>> agendaEventsList) {
        this.context = context;
        this.eventDateList = eventDateList;
        this.agendaEventsList = agendaEventsList;
    }

    @Override
    public int getGroupCount() {
        return eventDateList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return agendaEventsList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return eventDateList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return agendaEventsList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return agendaEventsList.get(groupPosition).get(childPosition).getId();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.agenda_list_group_item, null);
        }

        TextView textViewDate = (TextView) convertView.findViewById(R.id.textViewDate);
        textViewDate.setText(groupFormatter.format(eventDateList.get(groupPosition)));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.agenda_list_child_item, null);
        }

        TextView textViewTitle = (TextView) convertView.findViewById(R.id.textViewTitle);
        textViewTitle.setText(agendaEventsList.get(groupPosition).get(childPosition).getTitle());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(agendaEventsList.get(groupPosition).get(childPosition).getStartTime());
        String startTime = childFormatter.format(calendar.getTime());
        calendar.setTimeInMillis(agendaEventsList.get(groupPosition).get(childPosition).getEndTime());
        String endTime = childFormatter.format(calendar.getTime());
        
        TextView textViewWTime = (TextView) convertView.findViewById(R.id.textViewTime);
        textViewWTime.setText(startTime + " - " + endTime);
        
        TextView textViewLocation = (TextView) convertView.findViewById(R.id.textViewLocation);
        textViewLocation.setText(agendaEventsList.get(groupPosition).get(childPosition).getLocation());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
