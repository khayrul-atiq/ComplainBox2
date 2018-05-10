package com.example.olife.complainbox2;

import android.app.FragmentManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private HashMap < Integer, Fragment > fragmentMap = new HashMap< Integer, Fragment>();
    private HashMap < Integer, Integer > indexMap = new HashMap<Integer, Integer>();
    int i = 0;

    public static int currentItem;
    public static NavigationView navigationView;

    public void MainActivity(){}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeFragmentMap();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        navigationView = (NavigationView) findViewById(R.id.nav_view);
        MainActivity.navigationView.setNavigationItemSelectedListener(this);

        MainActivity.navigationView.setCheckedItem(R.id.nav_home);
        displaySelectedFragment(R.id.nav_home);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            if(MainActivity.currentItem != 0){
                MainActivity.navigationView.setCheckedItem(R.id.nav_home);
                displaySelectedFragment(R.id.nav_home);
            }
            else
                super.onBackPressed();
        }
    }


    private void showNetworkErrorMessage(){
        Toast.makeText(this,getResources().getString(R.string.network_error_message),Toast.LENGTH_SHORT).show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        if ( (item.getItemId()==R.id.nav_application_form || item.getItemId()==R.id.nav_event || item.getItemId()==R.id.nav_notice) && ( !isNetworkConnected()) ){
            showNetworkErrorMessage();
        }
        else {
            displaySelectedFragment(item.getItemId());
        }

        return true;
    }
    public void displaySelectedFragment(int item){
        MainActivity.currentItem = indexMap.get(item);
        Fragment fragment = fragmentMap.get(item);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //ft.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left);
        ft.replace(R.id.content_frame, fragment,Integer.toString(item));
        ft.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }



    private void initializeFragmentMap(){
        fragmentMap.put(R.id.nav_home, new Home());
        fragmentMap.put(R.id.nav_application_form,new ApplicationForm());
        fragmentMap.put(R.id.nav_emergency_support, new EmergencySupport());
        fragmentMap.put(R.id.nav_event,new Event());
        fragmentMap.put(R.id.nav_my_profile, new MyProfile());
        fragmentMap.put(R.id.nav_notice, new Notice());
        fragmentMap.put(R.id.nav_problem_submission, new ProblemSubmission());

        indexMap.put(R.id.nav_home, 0);
        indexMap.put(R.id.nav_problem_submission, 1);
        indexMap.put(R.id.nav_emergency_support, 2);
        indexMap.put(R.id.nav_event, 3);
        indexMap.put(R.id.nav_notice, 4);
        indexMap.put(R.id.nav_application_form, 5);
        indexMap.put(R.id.nav_my_profile, 6);
    }

}