package com.example.olife.complainbox2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by olife on 4/6/2018.
 */

public class ProblemInformationCustomAdapter extends ArrayAdapter<ProblemInformation>{



    private final Context context;
    private ArrayList<ProblemInformation> values = new ArrayList<>();
    private boolean isAccount;

    public ProblemInformationCustomAdapter(Context context, ArrayList<ProblemInformation> values, boolean isAccount) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
        this.isAccount = isAccount;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


            if(isAccount){
                //if (convertView == null) {
                convertView = inflater.inflate(R.layout.layout_activity_information_row, null);
                //}

                TextView problemDescription = convertView.findViewById(R.id.problem_des);
                TextView problemCategory = convertView.findViewById(R.id.problem_category);
                TextView problemSubmissionDate = convertView.findViewById(R.id.problem_submission_date);
                TextView problemStatus = convertView.findViewById(R.id.problem_current_status);

                problemDescription.setText(problemDescription.getText() + values.get(position).getProblemDescription());
                problemCategory.setText(problemCategory.getText()+values.get(position).getProblemCategory());
                problemSubmissionDate.setText(problemSubmissionDate+values.get(position).getProblemSubmissionDate());
                problemStatus.setText(problemStatus.getText()+values.get(position).getProblemStatus());
            }


            else{
                Log.d("empty user", "getView: work");

                //if (convertView == null) {
                convertView = inflater.inflate(R.layout.layout_activity_information_row, null);
                //}

                TextView problemDescription = convertView.findViewById(R.id.problem_des);
                TextView problemCategory = convertView.findViewById(R.id.problem_category);
                TextView problemSubmissionDate = convertView.findViewById(R.id.problem_submission_date);
                TextView problemStatus = convertView.findViewById(R.id.problem_current_status);

                problemDescription.setText("User submitted problem list");
                problemCategory.setText("");
                problemSubmissionDate.setText("");
                problemStatus.setText("You have create a user account to view own submitted problems.");
            }


        return convertView;
    }


}
