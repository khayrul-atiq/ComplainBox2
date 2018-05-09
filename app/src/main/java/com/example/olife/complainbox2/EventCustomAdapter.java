package com.example.olife.complainbox2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by olife on 4/6/2018.
 */

public class EventCustomAdapter extends ArrayAdapter<EventInformation>{



    private final Context context;
    private ArrayList<EventInformation> values = new ArrayList<EventInformation>();

    public EventCustomAdapter(Context context, ArrayList<EventInformation> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_event_row, null);
        }

        TextView title = convertView.findViewById(R.id.event_title);
        TextView time = convertView.findViewById(R.id.event_time);
        TextView description = convertView.findViewById(R.id.event_description);
        TextView publishingDate = convertView.findViewById(R.id.event_publishing_date);

        title.setText(values.get(position).getEvent_title());
        description.setText(values.get(position).getEvent_description());
        time.setText( time.getText()+ values.get(position).getEvent_date());
        publishingDate.setText( publishingDate.getText() +values.get(position).getEvent_publish_date());

        return convertView;
    }


}
