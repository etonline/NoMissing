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
import edu.ntust.cs.idsl.nomissing.constant.EventFreq;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
@Deprecated
public class EventFreqSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    private Context context;
    private List<EventFreq> eventFreqs;

    public EventFreqSpinnerAdapter(Context context) {
        this.context = context;
        eventFreqs = EventFreq.getEventFreqs();
    }

    @Override
    public int getCount() {
        return eventFreqs.size();
    }

    @Override
    public Object getItem(int position) {
        return eventFreqs.get(position).getRrule();
    }

    @Override
    public long getItemId(int position) {
        return eventFreqs.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(android.R.layout.simple_list_item_1, null);
        }

        TextView textViewFreq = (TextView) convertView.findViewById(android.R.id.text1);
        textViewFreq.setText(eventFreqs.get(position).getName());

        return convertView;
    }

}
