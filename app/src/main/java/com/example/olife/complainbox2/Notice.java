package com.example.olife.complainbox2;

import android.app.ProgressDialog;
import android.content.Intent;
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


public class Notice extends Fragment {

    private ProgressDialog pDialog;

    private JSONParser jParser = new JSONParser();
    private JSONArray all_notices = null;

    private ArrayList<NoticeInformation> notice_List;
    private ListView listView;

    private static String url_all_notices,file_path,TAG_SUCCESS,TAG_NOTICES,TAG_NOTICE_TITLE,TAG_NOTICE_FILE_NAME,TAG_DATE;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notice, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Notice");

        initializeNoticeUrlAndTag();


        //new LoadAllNoticeFormServer().execute();

        LoadAllNoticeFormServer();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getActivity(), viewPdfAndDownload.class);
                NoticeInformation selectedNotice = notice_List.get(i);

                intent.putExtra("selected_url", file_path+selectedNotice.getNotice_file_name());
                intent.putExtra("title",selectedNotice.getNotice_title());
                intent.putExtra("fileName",selectedNotice.getNotice_file_name());

                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left);

                Toast.makeText(getActivity(),notice_List.get(i).getNotice_title(),Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void initializeNoticeUrlAndTag(){

        notice_List=new ArrayList<>();

        listView = getActivity().findViewById(R.id.nolice_list);

        url_all_notices = getResources().getString(R.string.complain_box_domain)+getResources().getString(R.string.notice_url);

        file_path = getResources().getString(R.string.complain_box_domain)+getResources().getString(R.string.file_url);

        TAG_SUCCESS = getResources().getString(R.string.success_tag);
        TAG_NOTICES = getResources().getString(R.string.notices_tag);
        TAG_NOTICE_TITLE = getResources().getString(R.string.notice_title_tag);
        TAG_NOTICE_FILE_NAME = getResources().getString(R.string.notice_file_name_tag);
        TAG_DATE = getResources().getString(R.string.notice_date_tag);
    }



    private void LoadAllNoticeFormServer(){
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getActivity());

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading notices. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url_all_notices, new Response.Listener<String>() {
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
                        all_notices = json.getJSONArray(TAG_NOTICES);

                        // looping through All Products
                        for (int i = 0; i < all_notices.length(); i++) {
                            JSONObject c = all_notices.getJSONObject(i);

                            // Storing each json item in variable
                            String notice_title,notice_file_name,notice_date;

                            notice_title = c.getString(TAG_NOTICE_TITLE);
                            notice_date = c.getString(TAG_DATE);
                            notice_file_name = c.getString(TAG_NOTICE_FILE_NAME);

                            // creating new list

                            notice_List.add(new NoticeInformation(notice_title,notice_file_name,notice_date));


                        }

                        NoticeCustomAdapter adapter = new NoticeCustomAdapter(getActivity(),notice_List);
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
*

    class LoadAllNoticeFormServer extends AsyncTask<String, String, String> {


        // Before starting background thread Show Progress Dialog

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("All Notices: ", "preexecute");
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading notices. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        //  getting All products from url


        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            Log.d("All Notices: ", url_all_notices);
            JSONObject json = jParser.makeHttpRequest(url_all_notices, "POST", params);

            // Check your log cat for JSON reponse

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    all_notices = json.getJSONArray(TAG_NOTICES);

                    // looping through All Products
                    for (int i = 0; i < all_notices.length(); i++) {
                        JSONObject c = all_notices.getJSONObject(i);

                        // Storing each json item in variable
                        String notice_title,notice_file_name,notice_date;

                        notice_title = c.getString(TAG_NOTICE_TITLE);
                        notice_date = c.getString(TAG_DATE);
                        notice_file_name = c.getString(TAG_NOTICE_FILE_NAME);

                        // creating new list

                        notice_List.add(new NoticeInformation(notice_title,notice_file_name,notice_date));
                    }
                } else {
                    // no products found

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("All Notices: ", notice_List.toString());
            return null;
        }


        //   After completing background task Dismiss the progress dialog

        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread

            NoticeCustomAdapter adapter = new NoticeCustomAdapter(getActivity(),notice_List);
            listView.setAdapter(adapter);

        }

    }

*
*
*
* */
