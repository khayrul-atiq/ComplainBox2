package com.example.olife.complainbox2;

import android.content.Context;
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

public class ProblemSubmission extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_problem_submission, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Problem Submission");

        CircleButton road_damage = getActivity().findViewById(R.id.road_damage);
        CircleButton westage = getActivity().findViewById(R.id.westage);
        CircleButton footpath = getActivity().findViewById(R.id.footpath);
        CircleButton drainage = getActivity().findViewById(R.id.drainage);
        CircleButton road_light = getActivity().findViewById(R.id.road_light);
        CircleButton mosquito = getActivity().findViewById(R.id.mosquito);
        CircleButton illegal_parking = getActivity().findViewById(R.id.illegal_parking);
        CircleButton corruption = getActivity().findViewById(R.id.corruption);
        CircleButton repair = getActivity().findViewById(R.id.repair);
        CircleButton other = getActivity().findViewById(R.id.other);

        road_damage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Submission.class);
                i.putExtra("message_title","Road Damage");
                i.putExtra("background_image",R.drawable.ic_road_damage);


                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left);
            }
        });

        westage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Submission.class);
                i.putExtra("message_title","Westage");
                i.putExtra("background_image",R.drawable.ic_westage);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left);
            }
        });

        footpath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Submission.class);
                i.putExtra("message_title","Footpath");
                i.putExtra("background_image",R.drawable.ic_footpath);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left);
            }
        });

        drainage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Submission.class);
                i.putExtra("message_title","Drainage");
                i.putExtra("background_image",R.drawable.ic_drainage);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left);
            }
        });

        road_light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Submission.class);
                i.putExtra("message_title","Road Light");
                i.putExtra("background_image",R.drawable.ic_road_light);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left);
            }
        });

        mosquito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Submission.class);
                i.putExtra("message_title","Mosquito");
                i.putExtra("background_image",R.drawable.ic_mosquito);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left);
            }
        });

        illegal_parking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Submission.class);
                i.putExtra("message_title","Illegal Parking");
                i.putExtra("background_image",R.drawable.ic_illegal_parking);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left);
            }
        });

        corruption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Submission.class);
                i.putExtra("message_title","Corruption");
                i.putExtra("background_image",R.drawable.ic_corruption);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left);
            }
        });

        repair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Submission.class);
                i.putExtra("message_title","Repair");
                i.putExtra("background_image",R.drawable.ic_repair);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left);
            }
        });

        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Submission.class);
                i.putExtra("message_title","Others");
                i.putExtra("background_image",R.drawable.ic_other);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left);
            }
        });

    }




}
