package com.example.olife.complainbox2;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;



import at.markushi.ui.CircleButton;


public class Home extends Fragment {

    private CircleButton circleButton_emergency_support,circleButton_problem_submission,circleButton_event,circleButton_notice,circleButton_application_form,circleButton_my_profile;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        return root;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Home");

        circleButton_emergency_support = getActivity().findViewById(R.id.home_bt_emergency_support);
        circleButton_application_form = getActivity().findViewById(R.id.home_bt_application_form);
        circleButton_problem_submission = getActivity().findViewById(R.id.home_bt_problem_submission);
        circleButton_event = getActivity().findViewById(R.id.home_bt_event);
        circleButton_notice = getActivity().findViewById(R.id.home_bt_notice);
        circleButton_my_profile = getActivity().findViewById(R.id.home_bt_my_profile);

        circleButton_emergency_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.currentItem = 2;
                MainActivity.navigationView.setCheckedItem(R.id.nav_emergency_support);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left);
                ft.replace(R.id.content_frame, new EmergencySupport());
                ft.commit();
            }
        });

        circleButton_application_form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.currentItem = 5;
                MainActivity.navigationView.setCheckedItem(R.id.nav_application_form);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left);
                ft.replace(R.id.content_frame, new ApplicationForm());
                ft.commit();
            }
        });

        circleButton_problem_submission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.currentItem = 1;
                MainActivity.navigationView.setCheckedItem(R.id.nav_problem_submission);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left);
                ft.replace(R.id.content_frame, new ProblemSubmission());
                ft.commit();
            }
        });

        circleButton_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.currentItem = 4;
                MainActivity.navigationView.setCheckedItem(R.id.nav_notice);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left);
                ft.replace(R.id.content_frame, new Notice());
                ft.commit();
            }
        });

        circleButton_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.currentItem = 3;
                MainActivity.navigationView.setCheckedItem(R.id.nav_event);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left);
                ft.replace(R.id.content_frame, new Event());
                ft.commit();
            }
        });

        circleButton_my_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.currentItem = 6;
                MainActivity.navigationView.setCheckedItem(R.id.nav_my_profile);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left);
                ft.replace(R.id.content_frame, new MyProfile());
                ft.commit();
            }
        });
        
    }

}
