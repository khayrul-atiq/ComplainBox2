package com.example.olife.complainbox2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class PoliceStation extends AppCompatActivity {

    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        this.setTitle("Police Station");
        listView = findViewById(R.id.list);

/*
        ArrayList<InstituteInformation> pi = new ArrayList<>();

        pi.add(new InstituteInformation("Sahabug thana","Sahabug,Dhaka bangladesh"));
        pi.add(new InstituteInformation("Romna thana","Romna,Dhaka bangladesh"));
        pi.add(new InstituteInformation("Mirpur 12 thana","Mirpur 12, Dhaka bangladesh"));
        pi.add(new InstituteInformation("Motijil thana","Motijil,Dhaka bangladesh"));
        pi.add(new InstituteInformation("Jattarabari thana","Jattarabari,Dhaka bangladesh"));
        pi.add(new InstituteInformation("Guilistan than","Gulistan,Dhaka Bangladesh"));

        InstituteCustomeAdapter adapter = new InstituteCustomeAdapter(this,pi);

        listView.setAdapter(adapter);*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right);
    }
}
