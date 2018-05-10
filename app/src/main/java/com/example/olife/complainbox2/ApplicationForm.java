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


public class ApplicationForm extends Fragment {




    private ProgressDialog pDialog;

    private JSONParser jParser = new JSONParser();
    private JSONArray all_applications = null;

    private ArrayList<ApplicationFormInformation> application_form_List;
    private ArrayList<ApplicationFormInformation> application_form_List2;
    private HashMap<String,Boolean> uniqueApplicationType;

    private ListView listView;

    private static  String  url_all_application_forms,TAG_SUCCESS,TAG_APPLICATION_FORMS,TAG_APPLICATION_FORM_TITLE,TAG_APPLICATION_FORM_DESCRIPTION,TAG_APPLICATION_FORM_FILE_NAME,TAG_APPLICATION_FORM_TYPE,TAG_APPLICATION_FORM_UPLOADED_DATE;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_application_form, container, false);
    }

    private void initializeApplicationFormUrlAndTag(){

        listView = getActivity().findViewById(R.id.application_form_list);

        application_form_List=new ArrayList<>();
        application_form_List2=new ArrayList<>();
        uniqueApplicationType = new HashMap<>();

        url_all_application_forms = getResources().getString(R.string.complain_box_domain)+getResources().getString(R.string.application_form_url);
        TAG_SUCCESS = getResources().getString(R.string.success_tag);
        TAG_APPLICATION_FORMS = getResources().getString(R.string.application_forms_tag);
        TAG_APPLICATION_FORM_TITLE = getResources().getString(R.string.application_form_title_tag);
        TAG_APPLICATION_FORM_DESCRIPTION = getResources().getString(R.string.application_form_description_tag);
        TAG_APPLICATION_FORM_FILE_NAME = getResources().getString(R.string.application_form_file_name_tag);
        TAG_APPLICATION_FORM_TYPE = getResources().getString(R.string.application_form_type_tag);
        TAG_APPLICATION_FORM_UPLOADED_DATE = getResources().getString(R.string.application_form_uploaded_date_tag);

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Application Form");


        initializeApplicationFormUrlAndTag();

        LoadAllApplicationFormFromServer();

        ///  new LoadAllApplicationFormFromServer().execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getActivity(), viewPdfAndDownload.class);
                ApplicationFormInformation selectedApplicationForm = application_form_List2.get(i);
                intent.putExtra("selected_url", getResources().getString(R.string.complain_box_domain)+getResources().getString(R.string.file_url)+selectedApplicationForm.getApplicationFormFileName());
                intent.putExtra("title",selectedApplicationForm.getApplicationFormTitle());
                intent.putExtra("fileName",selectedApplicationForm.getApplicationFormFileName());

                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left);

                Toast.makeText(getActivity(),application_form_List2.get(i).getApplicationFormTitle(),Toast.LENGTH_SHORT).show();
            }
        });

    }



    private void LoadAllApplicationFormFromServer(){

        RequestQueue MyRequestQueue = Volley.newRequestQueue(getActivity());

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading application forms. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url_all_application_forms, new Response.Listener<String>() {
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
                        all_applications = json.getJSONArray(TAG_APPLICATION_FORMS);

                        // looping through All Products
                        for (int i = 0; i < all_applications.length(); i++) {
                            JSONObject c = all_applications.getJSONObject(i);

                            String applicationFormTitle,applicationFormDescription,applicationFormFileName,applicationFormType,applicationFormUploadedDate;

                            applicationFormTitle = c.getString(TAG_APPLICATION_FORM_TITLE);
                            applicationFormDescription = c.getString(TAG_APPLICATION_FORM_DESCRIPTION);
                            applicationFormFileName = c.getString(TAG_APPLICATION_FORM_FILE_NAME);
                            applicationFormType = c.getString(TAG_APPLICATION_FORM_TYPE);
                            applicationFormUploadedDate = c.getString(TAG_APPLICATION_FORM_UPLOADED_DATE);

                            uniqueApplicationType.put(applicationFormType,true);

                            // creating new list
                            application_form_List.add( new ApplicationFormInformation( applicationFormTitle, applicationFormDescription, applicationFormFileName, applicationFormType, applicationFormUploadedDate));

                        }

                        for(String key: uniqueApplicationType.keySet()){
                            application_form_List2.add(new ApplicationFormInformation(true,key));

                            for(ApplicationFormInformation afi: application_form_List){
                                if(afi.getApplicationFormType().equals(key)) application_form_List2.add(afi);
                            }
                        }

                        ApplicationFormCustomAdapter adapter = new ApplicationFormCustomAdapter(getActivity(),application_form_List2);
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
*
    class LoadAllApplicationFormFromServer extends AsyncTask<String, String, String> {


         //* Before starting background thread Show Progress Dialog

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("All Notices: ", "preexecute");
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading application forms. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        // * getting All products from url






        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            Log.d("All Notices: ", url_all_application_forms);
            JSONObject json = jParser.makeHttpRequest(url_all_application_forms, "POST", params);

            // Check your log cat for JSON reponse

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    all_applications = json.getJSONArray(TAG_APPLICATION_FORMS);

                    // looping through All Products
                    for (int i = 0; i < all_applications.length(); i++) {
                        JSONObject c = all_applications.getJSONObject(i);

                        String applicationFormTitle,applicationFormDescription,applicationFormFileName,applicationFormType,applicationFormUploadedDate;

                        applicationFormTitle = c.getString(TAG_APPLICATION_FORM_TITLE);
                        applicationFormDescription = c.getString(TAG_APPLICATION_FORM_DESCRIPTION);
                        applicationFormFileName = c.getString(TAG_APPLICATION_FORM_FILE_NAME);
                        applicationFormType = c.getString(TAG_APPLICATION_FORM_TYPE);
                        applicationFormUploadedDate = c.getString(TAG_APPLICATION_FORM_UPLOADED_DATE);

                        uniqueApplicationType.put(applicationFormType,true);

                        // creating new list
                        application_form_List.add( new ApplicationFormInformation( applicationFormTitle, applicationFormDescription, applicationFormFileName, applicationFormType, applicationFormUploadedDate));
                    }
                } else {
                    // no products found
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("All Notices: ", application_form_List.toString());
            return null;
        }

        // * After completing background task Dismiss the progress dialog

        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread

            for(String key: uniqueApplicationType.keySet()){
                application_form_List2.add(new ApplicationFormInformation(true,key));

                for(ApplicationFormInformation afi: application_form_List){
                    if(afi.getApplicationFormType().equals(key)) application_form_List2.add(afi);
                }
            }

            ApplicationFormCustomAdapter adapter = new ApplicationFormCustomAdapter(getActivity(),application_form_List2);
            listView.setAdapter(adapter);

        }

    }
*
*
*
*
*
* */
