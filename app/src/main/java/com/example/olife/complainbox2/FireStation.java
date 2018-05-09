package com.example.olife.complainbox2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class FireStation extends AppCompatActivity {

    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        this.setTitle("Fire Station");
        listView = findViewById(R.id.list);


        /*ArrayList<InstituteInformation> pi = new ArrayList<>();

        pi.add(new InstituteInformation("Sahabug fire","Sahabug,Dhaka bangladesh"));
        pi.add(new InstituteInformation("Romna fire","Romna,Dhaka bangladesh"));
        pi.add(new InstituteInformation("Mirpur 12 fire","Mirpur 12, Dhaka bangladesh"));
        pi.add(new InstituteInformation("Motijil fire","Motijil,Dhaka bangladesh"));
        pi.add(new InstituteInformation("Jattarabari fire","Jattarabari,Dhaka bangladesh"));
        pi.add(new InstituteInformation("Guilistan fire","Gulistan,Dhaka Bangladesh"));

        InstituteCustomeAdapter adapter = new InstituteCustomeAdapter(this,pi);

        listView.setAdapter(adapter);*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right);
    }
}
