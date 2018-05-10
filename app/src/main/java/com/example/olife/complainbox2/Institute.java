package com.example.olife.complainbox2;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.location.LocationListener;

import static com.google.android.gms.common.api.GoogleApiClient.*;

public class Institute extends AppCompatActivity implements ConnectionCallbacks,OnConnectionFailedListener, LocationListener {

    private ListView listView;
    private ProgressDialog pDialog;


    private JSONParser jParser = new JSONParser();
    private JSONArray all_institute = null;

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;

    private int flag =0;
    private double latitude,longitude;
    private String title, url_all_institute;

    private ArrayList<InstituteInformation> allInstituteList = new ArrayList<>();
    private HashMap<String, Integer> backgroundColor = new HashMap<String, Integer>();

    private static String TAG_SUCCESS,TAG_INSTITUTES,TAG_INSTITUTE_NAME,TAG_INSTITUTE_LOCATION,TAG_LONGITUDE,TAG_LATITUDE,TAG_PHONE ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);


        if(isNetworkConnected()){
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

            if (mGoogleApiClient != null) {
                mGoogleApiClient.connect();
            } else
                Toast.makeText(this, "Not Connected!", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this,getResources().getString(R.string.network_error_message),Toast.LENGTH_SHORT).show();
            onBackPressed();
        }

        initializeColorAndTag();

        listView = findViewById(R.id.list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle extras = getIntent().getExtras();
        title = extras.getString(getResources().getString(R.string.institute_title_key));
        url_all_institute = extras.getString(getResources().getString(R.string.institute_url_key));

        this.setTitle(title + " list");


    }


    private void initializeColorAndTag(){

        backgroundColor.put(getResources().getString(R.string.Hospital),getResources().getColor(R.color.Hospital));
        backgroundColor.put(getResources().getString(R.string.Police_station),getResources().getColor(R.color.Police));
        backgroundColor.put(getResources().getString(R.string.Fire_station), getResources().getColor(R.color.Fire));

        TAG_SUCCESS = getResources().getString(R.string.success_tag);
        TAG_INSTITUTES = getResources().getString(R.string.institutes_tag);
        TAG_INSTITUTE_NAME = getResources().getString(R.string.institute_name_tag);
        TAG_INSTITUTE_LOCATION = getResources().getString(R.string.institute_location_tag);
        TAG_LONGITUDE = getResources().getString(R.string.longitude_tag);
        TAG_LATITUDE = getResources().getString(R.string.latitude_tag);
        TAG_PHONE = getResources().getString(R.string.phone_tag);

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == 1000){

            final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);

            if(resultCode == Activity.RESULT_OK ){
                flag = 1;
                getLocation();
            }
            else if(resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(this, "Current Location Service not Enabled", Toast.LENGTH_SHORT).show();
            }
            LoadAllInstituteFormServer();
            //new LoadAllInstituteFormServer().execute();
        }

    }



    /*Ending the updates for the location service*/
    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        settingRequest();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Connection Suspended!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection Failed!", Toast.LENGTH_SHORT).show();
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, 90000);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i("Current Location", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    /*Method to get the enable location settings dialog*/
    public void settingRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);    // 10 seconds, in milliseconds
        mLocationRequest.setFastestInterval(1000);   // 1 second, in milliseconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {

                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can
                        // initialize location requests here.
                        getLocation();
                        LoadAllInstituteFormServer();
                        //new LoadAllInstituteFormServer().execute();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(Institute.this, 1000);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                        LoadAllInstituteFormServer();
                        //new LoadAllInstituteFormServer().execute();
                        break;
                }

            }

        });
    }
    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1000);
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    1000);
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        } else {
            /*Getting the location after aquiring location service*/
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);

            if (mLastLocation != null) {

                latitude = mLastLocation.getLatitude();
                longitude = mLastLocation.getLongitude();
            } else {
                /*if there is no last known location. Which means the device has no data for the loction currently.
                 * So we will get the current location.
                 * For this we'll implement Location Listener and override onLocationChanged*/
                Log.i("Current Location", "No data for location found");

                if (!mGoogleApiClient.isConnected())
                    mGoogleApiClient.connect();

                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }
        }
    }
    /*When Location changes, this method get called. */
    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        latitude = mLastLocation.getLatitude();
        longitude = mLastLocation.getLongitude();
    }




    private void LoadAllInstituteFormServer(){


        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);

        pDialog = new ProgressDialog(Institute.this);
        pDialog.setMessage("Loading "+title+" information. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url_all_institute , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.
                try {

                    JSONObject json = new JSONObject(response);

                    int success = json.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        // products found
                        // Getting Array of Products
                        all_institute = json.getJSONArray(TAG_INSTITUTES);

                        // looping through All Products
                        for (int i = 0; i < all_institute.length(); i++) {
                            JSONObject c = all_institute.getJSONObject(i);
                            // Storing each json item in variable
                            String instituteName,instituteLocation,latitude,longitude,phone;
                            instituteName = c.getString(TAG_INSTITUTE_NAME);
                            instituteLocation = c.getString(TAG_INSTITUTE_LOCATION);
                            latitude = c.getString(TAG_LATITUDE);
                            longitude = c.getString(TAG_LONGITUDE);
                            phone = c.getString(TAG_PHONE);
                            // creating new list

                            allInstituteList.add(new InstituteInformation( instituteName,  instituteLocation,  latitude,  longitude,  phone));
                        }

                        if(flag==1)waitForAWhile(5000);

                        InstituteCustomeAdapter adapter = new InstituteCustomeAdapter(Institute.this,allInstituteList,latitude,longitude,backgroundColor.get(title));
                        listView.setAdapter(adapter);

                    } else {
                        // no products found

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                pDialog.dismiss();
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
                Toast.makeText(Institute.this, error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("category", title);
                return MyData;
            }
        };

        MyRequestQueue.add(MyStringRequest);


    }

    private void waitForAWhile(int millisecond){
        try {
            Thread.currentThread();
            Thread.sleep(millisecond);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



}

/*
*
*
*
*
    class LoadAllInstituteFormServer extends AsyncTask<String, String, String> {


        /// * Before starting background thread Show Progress Dialog


        private void waitForAWhile(int millisecond){
            try {
                Thread.currentThread();
                Thread.sleep(millisecond);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("All Notices: ", "preexecute");
            pDialog = new ProgressDialog(Institute.this);
            pDialog.setMessage("Loading "+title+" information. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }


        // * getting All products from url


        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            params.add(new BasicNameValuePair("category", title));
            Log.d("All Notices: ", url_all_institute);
            JSONObject json = jParser.makeHttpRequest(url_all_institute, "POST", params);

            // Check your log cat for JSON reponse

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    all_institute = json.getJSONArray(TAG_INSTITUTES);

                    // looping through All Products
                    for (int i = 0; i < all_institute.length(); i++) {
                        JSONObject c = all_institute.getJSONObject(i);
                        // Storing each json item in variable
                        String instituteName,instituteLocation,latitude,longitude,phone;
                        instituteName = c.getString(TAG_INSTITUTE_NAME);
                        instituteLocation = c.getString(TAG_INSTITUTE_LOCATION);
                        latitude = c.getString(TAG_LATITUDE);
                        longitude = c.getString(TAG_LONGITUDE);
                        phone = c.getString(TAG_PHONE);
                        // creating new list

                        allInstituteList.add(new InstituteInformation( instituteName,  instituteLocation,  latitude,  longitude,  phone));
                    }
                } else {
                    // no products found

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(flag==1)waitForAWhile(5000);
            Log.d("All Notices: ", allInstituteList.toString());
            return null;
        }


        // * After completing background task Dismiss the progress dialog

        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            InstituteCustomeAdapter adapter = new InstituteCustomeAdapter(Institute.this,allInstituteList,latitude,longitude,backgroundColor.get(title));
            listView.setAdapter(adapter);
        }

    }
*
*
*
*
*
* */
