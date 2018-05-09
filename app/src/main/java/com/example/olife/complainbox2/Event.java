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

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Event extends Fragment {

    ProgressDialog pDialog;

    private static String url_all_events = "http://10.100.104.78/android/getAllEvent.php";

    JSONParser jParser = new JSONParser();
    JSONArray all_events = null;

    ArrayList<EventInformation> event_List;
    ListView listView;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_EVENTS = "events";
    private static final String TAG_EVENT_TITLE = "eventTitle";
    private static final String TAG_EVENT_DESCRIPTION = "eventDescription";
    private static final String TAG_EVENT_PUBLISHING_DATE = "eventPublishingDate";
    private static final String TAG_DATE_TIME= "eventDateTime";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Event");

        event_List = new ArrayList<>();

        listView = getActivity().findViewById(R.id.event_list);

        new LoadAllEventFormServer().execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(),event_List.get(i).toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }



    class LoadAllEventFormServer extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading events. Please wait...");
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

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            EventCustomAdapter adapter = new EventCustomAdapter(getActivity(),event_List);
            listView.setAdapter(adapter);
        }

    }

}
