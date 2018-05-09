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

public class ApplicationFormCustomAdapter extends ArrayAdapter<ApplicationFormInformation>{



    private final Context context;
    private ArrayList<ApplicationFormInformation> values = new ArrayList<>();

    public ApplicationFormCustomAdapter(Context context, ArrayList<ApplicationFormInformation> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        if(values.get(position).isHeader()){
            //if (convertView == null) {
                convertView = inflater.inflate(R.layout.layout_application_form_head, null);
            //}

            TextView application_form_head = convertView.findViewById(R.id.application_form_head);

            application_form_head.setText(values.get(position).getApplicationFormType());

            convertView.setOnClickListener(null);

        }
        else{
            //if (convertView == null) {
                convertView = inflater.inflate(R.layout.layout_application_row, null);
            //}

            TextView application_form_title = convertView.findViewById(R.id.application_form_title);
            TextView application_form_description = convertView.findViewById(R.id.application_form_description);
            TextView application_form_uploaded_date = convertView.findViewById(R.id.application_form_uploaded_date);

            application_form_title.setText(values.get(position).getApplicationFormTitle());
            application_form_description.setText(values.get(position).getApplicationFormDescription());
            application_form_uploaded_date.setText(application_form_uploaded_date.getText()+values.get(position).getApplicationFormUploadedDate());

        }

        return convertView;
    }


}
