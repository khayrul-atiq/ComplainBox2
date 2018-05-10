package com.example.olife.complainbox2;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ProfileAcivity extends Fragment {

    private ProgressDialog pDialog;

    private JSONArray all_problems = null;

    private String userName,userEmail;

    private ArrayList<ProblemInformation> problem_list;

    private static String url_all_submitted_problems,TAG_SUCCESS,TAG_PROBLEMS,TAG_PROBLEM_DESCRIPTION,TAG_PROBLEM_CATEGORY,TAG_PROBLEM_SUBMISSION_DATE,TAG_PROBLEM_STATUS;

    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_acivity, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = getActivity().findViewById(R.id.user_submitted_problem_list);
        initializeUrlAndTag();

        getUserInformation();

        //Log.d("called","profile acivity");

        if(userName.equals("anonymous")){
            //Log.d("called","profile acivity");
            problem_list.add(new ProblemInformation(null,null,null,null));
            ProblemInformationCustomAdapter adapter = new ProblemInformationCustomAdapter(getActivity(),problem_list,false);
            listView.setAdapter(adapter);
        }
        else LoadAllSubmittedFormServer();
    }

    private void getUserInformation(){

        userName = "anonymous";
        userEmail = "anonymous@example.com";

    }

    private void initializeUrlAndTag(){

        problem_list = new ArrayList<>();

        url_all_submitted_problems = getResources().getString(R.string.complain_box_domain) + getResources().getString(R.string.submitted_problem_url);
        TAG_SUCCESS = getResources().getString(R.string.success_tag);
        TAG_PROBLEMS = getResources().getString(R.string.problems_tag);
        TAG_PROBLEM_DESCRIPTION = getResources().getString(R.string.problem_description_tag);
        TAG_PROBLEM_CATEGORY = getResources().getString(R.string.problem_category_tag);
        TAG_PROBLEM_SUBMISSION_DATE = getResources().getString(R.string.problem_submission_date_tag);
        TAG_PROBLEM_STATUS = getResources().getString(R.string.problem_status_tag);

    }


    private void LoadAllSubmittedFormServer(){


        RequestQueue MyRequestQueue = Volley.newRequestQueue(getActivity());

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading notices. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url_all_submitted_problems, new Response.Listener<String>() {
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
                        all_problems = json.getJSONArray(TAG_PROBLEMS);

                        // looping through All Products
                        for (int i = 0; i < all_problems.length(); i++) {
                            JSONObject c = all_problems.getJSONObject(i);


                            String problemDescription,problemCategory,problemSubmissionDate,problemStatus;
                            // Storing each json item in variable
                            //String notice_title,notice_file_name,notice_date;

                            problemDescription = c.getString(TAG_PROBLEM_DESCRIPTION);
                            problemCategory = c.getString(TAG_PROBLEM_CATEGORY);
                            problemSubmissionDate = c.getString(TAG_PROBLEM_SUBMISSION_DATE);
                            problemStatus = c.getString(TAG_PROBLEM_STATUS);


                            // creating new list

                            problem_list.add(new ProblemInformation( problemDescription,  problemCategory,  problemSubmissionDate,  problemStatus));

                        }

                        ProblemInformationCustomAdapter adapter = new ProblemInformationCustomAdapter(getActivity(),problem_list,true);
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
