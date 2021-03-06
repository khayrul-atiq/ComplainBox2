package com.example.olife.complainbox2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import at.markushi.ui.CircleButton;


public class EmergencySupport extends Fragment {


    private CircleButton circleButton_999, circleButton_hospital,circleButton_police_station,circleButton_fire_station;

    private String institute_url ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_emergency_support, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Emergency Support");

        circleButton_999 = getActivity().findViewById(R.id.emergency_support_999);
        circleButton_hospital = getActivity().findViewById(R.id.emergency_support_hospital);
        circleButton_fire_station = getActivity().findViewById(R.id.emergency_support_fire_station);
        circleButton_police_station = getActivity().findViewById(R.id.emergency_support_police_station);

        institute_url = getResources().getString(R.string.complain_box_domain)+getResources().getString(R.string.institute_url);


        circleButton_999.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "999", null)));
                Toast.makeText(getActivity(),"call 999",Toast.LENGTH_SHORT).show();
            }
        });

        circleButton_hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Institute.class);
                i.putExtra(getResources().getString(R.string.institute_title_key),getResources().getString(R.string.Hospital));
                i.putExtra(getResources().getString(R.string.institute_url_key),institute_url);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left);
            }
        });

        circleButton_police_station.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Institute.class);
                i.putExtra(getResources().getString(R.string.institute_title_key),getResources().getString(R.string.Police_station));
                i.putExtra(getResources().getString(R.string.institute_url_key),institute_url);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left);
            }
        });

        circleButton_fire_station.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Institute.class);
                i.putExtra(getResources().getString(R.string.institute_title_key),getResources().getString(R.string.Fire_station));
                i.putExtra(getResources().getString(R.string.institute_url_key),institute_url);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left);
            }
        });
    }
}
