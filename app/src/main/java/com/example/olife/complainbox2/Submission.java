package com.example.olife.complainbox2;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsStates;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import at.markushi.ui.CircleButton;



import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.google.android.gms.common.api.GoogleApiClient.*;

public class Submission extends AppCompatActivity implements
        ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

    private ProgressDialog pDialog;


    private JSONParser jParser = new JSONParser();
    private JSONArray all_institute = null;

    private CircleButton problem_submission_camera;
    private ImageView problem_submission_image;
    private EditText problem_description;
    private Uri file;
    private static String capturedImagePath;
    private int GALLARY_REQUEST = 1, CAPTURE_REQUEST = 11;
    private String url_to_send_problem,submittedProblemImagePath, category,email="empyt",problemDescription="empty",wardNo="empty",imageString="empty";


    private static String TAG_SUCCESS;

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;

    private double latitude,longitude;


    Bitmap bitmap;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_submission);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        } else
            Toast.makeText(this, "Not Connected!", Toast.LENGTH_SHORT).show();


        Bundle extras = getIntent().getExtras();
        category = extras.getString(getResources().getString(R.string.problem_title));
        this.setTitle(category);

        LinearLayout submission = findViewById(R.id.submission);
        submission.setBackgroundResource(extras.getInt(getResources().getString(R.string.background_image)));


        TAG_SUCCESS = getResources().getString(R.string.success_tag);
        url_to_send_problem = getResources().getString(R.string.complain_box_domain)+getResources().getString(R.string.problem_url);

        problem_submission_image = findViewById(R.id.problem_submission_image);
        problem_description = findViewById(R.id.problem_description);
        problem_submission_camera = findViewById(R.id.problem_submission_camera);

        // permission check
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            problem_submission_camera.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                problem_submission_camera.setEnabled(true);
            }
        }
    }


    public void captureProblemPhoto(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = Uri.fromFile(getOutputMediaFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);

        startActivityForResult(intent, CAPTURE_REQUEST);
    }

    public void selectProblemPhotoFromGallery(View view){
        getGallary();
    }

    private void getGallary(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent.createChooser(intent,"Select File"),GALLARY_REQUEST);
    }


    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "ComplainBoxPicture");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        capturedImagePath = mediaStorageDir.getPath() + File.separator + "IMG_"+ timeStamp + ".jpg";
        return new File(capturedImagePath);
    }



    private String getRealPathFromURI(Uri contentURI) {
        String filePath;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            filePath = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            filePath = cursor.getString(idx);
            cursor.close();
        }
        return filePath;
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right);
    }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK && requestCode!=1000 ){

            if(requestCode == GALLARY_REQUEST){
                file = data.getData();
                problem_submission_image.setImageURI(file);
                problem_submission_image.setScaleType(ImageView.ScaleType.FIT_CENTER);
                submittedProblemImagePath = getRealPathFromURI(file);
            }

            if(requestCode == CAPTURE_REQUEST){
                submittedProblemImagePath = capturedImagePath;
                problem_submission_image.setImageURI(file);
                problem_submission_image.setScaleType(ImageView.ScaleType.FIT_CENTER);
            }
            Toast.makeText(this,submittedProblemImagePath,Toast.LENGTH_SHORT).show();
        }



        if(requestCode == 1000){

            final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);

            if(resultCode == Activity.RESULT_OK ){
                getLocation();
            }
            else if(resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(this, "Location Service not Enabled", Toast.LENGTH_SHORT).show();
            }
        }

    }



    private String imageToString() {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        bitmap = Bitmap.createScaledBitmap(bitmap,720,650,true);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageByte, Base64.DEFAULT);
    }


    public void getLocation2(View view){

        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url_to_send_problem, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.
                try {
                    JSONObject jesonObject = new JSONObject(response);
                    String res = jesonObject.getString("response");
                    Toast.makeText(Submission.this,res,Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
                Toast.makeText(Submission.this, error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("image",imageToString());
                MyData.put("name","hello");
                MyData.put("category","problem");//Add the data you'd like to send to the server.
                MyData.put("problemDescription",problemDescription);
                MyData.put("latitude",Double.toString(latitude));
                MyData.put("longitude",Double.toString(longitude));
                MyData.put("email",email);

                return MyData;
            }
        };

        MyRequestQueue.add(MyStringRequest);
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
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(Submission.this, 1000);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
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

}


/*
*
*


        progressDialog = new ProgressDialog(Submission.this);
        progressDialog.setMessage("Uploading, please wait...");
        progressDialog.show();

        bitmap = BitmapFactory.decodeFile(submittedProblemImagePath);
        //converting image to base64 string
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        //sending image to server
        StringRequest request = new StringRequest(Request.Method.POST, url_to_send_problem, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                if(s.equals("true")){
                    Toast.makeText(Submission.this, "Uploaded Successful", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(Submission.this, "Some error occurred!", Toast.LENGTH_LONG).show();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(Submission.this, "Some error occurred -> "+volleyError, Toast.LENGTH_LONG).show();;
            }
        }) {
            //adding parameters to send
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("image", imageString);
                return parameters;
            }
        };

        RequestQueue rQueue = Volley.newRequestQueue(Submission.this);
        rQueue.add(request);

    // problemDescription = problem_description.getText().toString();

    Bitmap bitmap = BitmapFactory.decodeFile(submittedProblemImagePath);

    ByteArrayOutputStream stream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);

                byte[]  byteArray= stream.toByteArray();
                imageString= Base64.encodeToString(byteArray,Base64.DEFAULT);



//Showing the progress dialog
final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_to_send_problem,
        new Response.Listener<String>() {
@Override
public void onResponse(String s) {
        //Disimissing the progress dialog
        loading.dismiss();
        //Showing toast message of the response
        Toast.makeText(Submission.this, s , Toast.LENGTH_LONG).show();
        }
        },
        new Response.ErrorListener() {
@Override
public void onErrorResponse(VolleyError volleyError) {
        //Dismissing the progress dialog
        loading.dismiss();

        //Showing toast
        Toast.makeText(Submission.this, ""+volleyError, Toast.LENGTH_LONG).show();
        }
        }){
@Override
protected Map<String, String> getParams() throws AuthFailureError {
        //Converting Bitmap to String
        //Creating parameters
        Map<String,String> params = new Hashtable<String, String>();
        params.put("category", category);
        params.put("image", "jjjj");
        //returning parameters
        return params;
        }
        };


        MySingleton.getmInstance(Submission.this).addToRequestQue(stringRequest);
//Creating a Request Queue
//RequestQueue requestQueue = Volley.newRequestQueue(this);

//Adding request to the queue
//requestQueue.add(stringRequest);

//      RequestQueue rQueue = Volley.newRequestQueue(Submission.this);
//        rQueue.add(stringRequest);


       List<NameValuePair> params = new ArrayList<NameValuePair>();
        // getting JSON string from URL

        params.add(new BasicNameValuePair("category", category));
        params.add(new BasicNameValuePair("problemDescription", problemDescription));
        params.add(new BasicNameValuePair("latitude", Double.toString(latitude)));
        params.add(new BasicNameValuePair("longitude", Double.toString(longitude)));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("wardNo", wardNo));
        params.add(new BasicNameValuePair("image", imageString));

        JSONObject json = jParser.makeHttpRequest(url_to_send_problem, "POST", params);





        //new SendProblemInformationToServer().execute();
*
* */




/*
*
*   private class SendProblemInformationToServer extends AsyncTask<String, String, String> {


         //* Before starting background thread Show Progress Dialog


    protected void onPreExecute() {
        super.onPreExecute();
        Log.d("All Notices: ", "preexecute");
        pDialog = new ProgressDialog(Submission.this);
        pDialog.setMessage("Sending problem information. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }


    // * getting All products from url


    protected String doInBackground(String... args) {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        // getting JSON string from URL






        params.add(new BasicNameValuePair("category", category));
        params.add(new BasicNameValuePair("problemDescription", problemDescription));
        params.add(new BasicNameValuePair("latitude", Double.toString(latitude)));
        params.add(new BasicNameValuePair("longitude", Double.toString(longitude)));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("wardNo", wardNo));
        params.add(new BasicNameValuePair("image", imageString));

        JSONObject json = jParser.makeHttpRequest(url_to_send_problem, "POST", params);

        // Check your log cat for JSON reponse

        try {
            // Checking for SUCCESS TAG
            int success = json.getInt(TAG_SUCCESS);

            if (success == 1) {


                Toast.makeText(Submission.this,"success",Toast.LENGTH_SHORT).show();

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
        return null;
    }


   //  * After completing background task Dismiss the progress dialog
     * *
    protected void onPostExecute(String file_url) {
        // dismiss the dialog after getting all products
        pDialog.dismiss();
        // updating UI from Background Thread
    }

}

*
* */
