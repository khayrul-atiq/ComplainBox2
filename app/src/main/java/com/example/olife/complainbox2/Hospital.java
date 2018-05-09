package com.example.olife.complainbox2;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Hospital extends AppCompatActivity {

    private ListView listView;
    ProgressDialog pDialog;
    private String title, url_all_institute;

    private JSONParser jParser = new JSONParser();
    JSONArray all_institute = null;

    static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;

    ArrayList<InstituteInformation> allInstituteList = new ArrayList<>();

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_INSTITUTES = "institutes";
    private static final String TAG_INSTITUTE_NAME = "institutionName";
    private static final String TAG_INSTITUTE_LOCATION = "instituteLocation";
    private static final String TAG_LONGITUDE = "longitude";
    private static final String TAG_LATITUDE = "latitude";
    private static final String TAG_PHONE = "phone";


    private double latitude,longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        listView = findViewById(R.id.list);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle extras = getIntent().getExtras();

        title = extras.getString("title");
        url_all_institute = extras.getString("url");

        this.setTitle(title + " list");

        getLocation();
        new LoadAllInstituteFormServer().execute();

    }


    @Override
    public boolean onSupportNavigateUp() {
        Toast.makeText(this, "back press", Toast.LENGTH_SHORT).show();
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }


    void getLocation() {


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION);
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if(location != null) {
            latitude= location.getLatitude();
            longitude = location.getLongitude();

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_LOCATION:
                getLocation();
                break;

        }
    }


    class LoadAllInstituteFormServer extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */

        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("All Notices: ", "preexecute");
            pDialog = new ProgressDialog(Hospital.this);
            pDialog.setMessage("Loading "+title+" information. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         * */





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
            Log.d("All Notices: ", allInstituteList.toString());
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            getLocation();
            // updating UI from Background Thread
            InstituteCustomeAdapter adapter = new InstituteCustomeAdapter(Hospital.this,allInstituteList,latitude,longitude);
            listView.setAdapter(adapter);

        }

    }




}
