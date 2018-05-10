package com.example.olife.complainbox2;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Event extends Fragment {

    private  ProgressDialog pDialog;
    private  JSONParser jParser = new JSONParser();
    private  JSONArray all_events = null;

    private  ArrayList<EventInformation> event_List;
    private  ListView listView;

    private static String url_all_events,TAG_SUCCESS,TAG_EVENTS,TAG_EVENT_TITLE,TAG_EVENT_DESCRIPTION,TAG_EVENT_PUBLISHING_DATE,TAG_DATE_TIME;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event, container, false);
    }

    private void initializeEventUrlAndTag(){

        event_List = new ArrayList<>();

        listView = getActivity().findViewById(R.id.event_list);

        url_all_events = getResources().getString(R.string.complain_box_domain)+getResources().getString(R.string.event_url);
        TAG_SUCCESS = getResources().getString(R.string.success_tag);
        TAG_EVENTS = getResources().getString(R.string.events_tag);
        TAG_EVENT_TITLE = getResources().getString(R.string.event_title_tag);
        TAG_EVENT_DESCRIPTION = getResources().getString(R.string.event_description_tag);
        TAG_EVENT_PUBLISHING_DATE =getResources().getString(R.string.event_publishing_date_tag);
        TAG_DATE_TIME = getResources().getString(R.string.event_date_time_tag);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Event");

        initializeEventUrlAndTag();


        LoadAllEventFormServer();

        //new LoadAllEventFormServer().execute();
/*
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(),event_List.get(i).toString(),Toast.LENGTH_SHORT).show();
            }
        });

        */
    }

    private void LoadAllEventFormServer(){

        RequestQueue MyRequestQueue = Volley.newRequestQueue(getActivity());
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading events. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url_all_events, new Response.Listener<String>() {
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
                        all_events = json.getJSONArray(TAG_EVENTS);

                        // looping through All Products
                        for (int i = 0; i < all_events.length(); i++) {
                            JSONObject c = all_events.getJSONObject(i);

                            // Storing each json item in variable
                            String event_title,event_description,event_date,event_publish_date;

                            event_title = c.getString(TAG_EVENT_TITLE);
                            event_description = c.getString(TAG_EVENT_DESCRIPTION);
                            event_date = c.getString(TAG_DATE_TIME);
                            event_publish_date = c.getString(TAG_EVENT_PUBLISHING_DATE);

                            // creating new list

                            event_List.add(new EventInformation(event_title,event_description,event_date,event_publish_date));

                        }

                        // updating UI from Background Thread
                        EventCustomAdapter adapter = new EventCustomAdapter(getActivity(),event_List);
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
                Toast.makeText(getActivity(), error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();

                return MyData;
            }
        };

        MyRequestQueue.add(MyStringRequest);

    }





}


/*
*
*
*
*     class LoadAllEventFormServer extends AsyncTask<String, String, String> {


        /// * Before starting background thread Show Progress Dialog

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading events. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /// * getting All products from url


        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_events, "GET", params);

            // Check your log cat for JSON reponse

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    all_events = json.getJSONArray(TAG_EVENTS);

                    // looping through All Products
                    for (int i = 0; i < all_events.length(); i++) {
                        JSONObject c = all_events.getJSONObject(i);

                        // Storing each json item in variable
                        String event_title,event_description,event_date,event_publish_date;

                        event_title = c.getString(TAG_EVENT_TITLE);
                        event_description = c.getString(TAG_EVENT_DESCRIPTION);
                        event_date = c.getString(TAG_DATE_TIME);
                        event_publish_date = c.getString(TAG_EVENT_PUBLISHING_DATE);

                        // creating new list

                        event_List.add(new EventInformation(event_title,event_description,event_date,event_publish_date));
                    }
                } else {
                    // no products found

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("All Notices: ", event_List.toString());
            return null;
        }

        /// * After completing background task Dismiss the progress dialog

        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            EventCustomAdapter adapter = new EventCustomAdapter(getActivity(),event_List);
            listView.setAdapter(adapter);
        }

    }
*
*
* */
