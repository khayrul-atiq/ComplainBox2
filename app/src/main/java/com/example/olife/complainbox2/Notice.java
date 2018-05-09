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

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Notice extends Fragment {

    ProgressDialog pDialog;

    private static String url_all_notices = "http://10.100.104.78/android/getAllNotice.php";
            //String.valueOf(R.string.complain_box_domain) + String.valueOf(R.string.notice_path);

    private JSONParser jParser = new JSONParser();
    JSONArray all_notices = null;

    ArrayList<NoticeInformation> notice_List=new ArrayList<>();

    ListView listView;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_NOTICES = "notices";
    private static final String TAG_NOTICE_TITLE = "noticeTitle";
    private static final String TAG_NOTICE_FILE_NAME = "noticeFileName";
    private static final String TAG_DATE = "date";

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

        listView = getActivity().findViewById(R.id.nolice_list);

        Log.d("All Notices: ", "start  ");
        new LoadAllNoticeFormServer().execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                Intent intent = new Intent(getActivity(), viewNoticePDF.class);
                NoticeInformation selectedNotice = notice_List.get(i);
                intent.putExtra("selected_url", "http://10.100.104.78/SERVER/NoticePDF/"+selectedNotice.getNotice_file_name());
                intent.putExtra("title",selectedNotice.getNotice_title());
                intent.putExtra("fileName",selectedNotice.getNotice_file_name());

                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left);

                Toast.makeText(getActivity(),notice_List.get(i).getNotice_title(),Toast.LENGTH_SHORT).show();
            }
        });


    }


    class LoadAllNoticeFormServer extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
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

        /**
         * getting All products from url
         * */





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
                        String notice_id,notice_title,notice_file_name,notice_date;

                        notice_id = "ggg0";
                        notice_title = c.getString(TAG_NOTICE_TITLE);
                        notice_date = c.getString(TAG_DATE);
                        notice_file_name = c.getString(TAG_NOTICE_FILE_NAME);

                        // creating new list

                        notice_List.add(new NoticeInformation(notice_id,notice_title,notice_file_name,notice_date));
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

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread

            NoticeCustomAdapter adapter = new NoticeCustomAdapter(getActivity(),notice_List);
            listView.setAdapter(adapter);

        }

    }

}
