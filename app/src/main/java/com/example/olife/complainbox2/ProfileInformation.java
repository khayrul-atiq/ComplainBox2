package com.example.olife.complainbox2;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class ProfileInformation extends Fragment {


    private String userName,userEmail;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_information, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getUserInformation();

        TextView name = getActivity().findViewById(R.id.user_name);
        TextView email = getActivity().findViewById(R.id.user_email);

        name.setText(name.getText()+userName);
        email.setText(email.getText()+userEmail);

    }


    private void getUserInformation(){
        userName = "anonymous";
        userEmail = "anonymous@example.com";


        // server connection
    }
}
