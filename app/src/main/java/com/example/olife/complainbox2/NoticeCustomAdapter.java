package com.example.olife.complainbox2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by olife on 4/6/2018.
 */

public class NoticeCustomAdapter extends ArrayAdapter<NoticeInformation>{



    private final Context context;
    private ArrayList<NoticeInformation> values = new ArrayList<>();

    public NoticeCustomAdapter(Context context, ArrayList<NoticeInformation> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_notice_row, null);
        }

        TextView title = convertView.findViewById(R.id.notice_title);
        TextView time = convertView.findViewById(R.id.notice_time);

        title.setText(values.get(position).getNotice_title());
        time.setText(values.get(position).getNotice_date());


        return convertView;
    }


}
