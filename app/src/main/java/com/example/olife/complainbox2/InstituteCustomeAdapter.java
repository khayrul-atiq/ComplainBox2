package com.example.olife.complainbox2;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.ArrayList;

import at.markushi.ui.CircleButton;

/**
 * Created by olife on 4/5/2018.
 */

public class InstituteCustomeAdapter extends ArrayAdapter<InstituteInformation> {


    private final Context context;
    private ArrayList<InstituteInformation> values = new ArrayList<>();
    private double latitude,longitude;
    private Integer color;

    public InstituteCustomeAdapter(Context context, ArrayList<InstituteInformation> values,double latitude, double longitude,Integer color) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
        this.latitude = latitude;
        this.longitude = longitude;
        this.color = color;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_institute_row, null);
        }

        ImageView phone = convertView.findViewById(R.id.phone);
        ImageView location = convertView.findViewById(R.id.location);


        TextView institute_name = convertView.findViewById(R.id.institute_name);
        TextView institute_location = convertView.findViewById(R.id.institute_location);

        institute_name.setText(values.get(position).getInstituteName());
        institute_location.setText(values.get(position).getInstituteLocation());

        convertView.setBackgroundColor(color);

        // change the icon for Windows and iPhone

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", values.get(position).getPhone(), null)));

            }

        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if(latitude==0.0 && longitude==0.0){
                    intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?q="+ values.get(position).getLatitude()  +"," + values.get(position).getLongitude() +"("+ values.get(position).getInstituteName() + ")&iwloc=A&hl=es"));
                }

                else{
                    intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr="+latitude+","+longitude+"&daddr="+values.get(position).getLatitude()+","+values.get(position).getLongitude()));
                }
               context.startActivity(intent);


            }

        });

        return convertView;
    }


}
