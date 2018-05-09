package com.example.olife.complainbox2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import java.util.HashMap;
import java.util.List;


public class ApplicationForm extends Fragment {




    ProgressDialog pDialog;

    private static String url_all_application_forms = "http://10.100.104.78/android/getAllApplicationForm.php";
    //String.valueOf(R.string.complain_box_domain) + String.valueOf(R.string.notice_path);

    private JSONParser jParser = new JSONParser();
    JSONArray all_applications = null;

    ArrayList<ApplicationFormInformation> application_form_List=new ArrayList<>();
    ArrayList<ApplicationFormInformation> application_form_List2=new ArrayList<>();
    HashMap<String,Boolean> uniqueApplicationType = new HashMap<>();

    ListView listView;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_APPLICATION_FORMS = "applicationForms";
    private static final String TAG_APPLICATION_FORM_TITLE = "applicationFormTitle";
    private static final String TAG_APPLICATION_FORM_DESCRIPTION = "applicationFormDescription";
    private static final String TAG_APPLICATION_FORM_FILE_NAME = "applicationFormFileName";
    private static final String TAG_APPLICATION_FORM_TYPE = "applicationFormType";
    private static final String TAG_APPLICATION_FORM_UPLOADED_DATE = "applicationFormUploadedDate";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_application_form, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Application Form");

        listView = getActivity().findViewById(R.id.application_form_list);

        new LoadAllApplicationFormFromServer().execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getActivity(), viewNoticePDF.class);
                ApplicationFormInformation selectedApplicationForm = application_form_List2.get(i);
                intent.putExtra("selected_url", "http://10.100.104.78/SERVER/NoticePDF/"+selectedApplicationForm.getApplicationFormFileName());
                intent.putExtra("title",selectedApplicationForm.getApplicationFormTitle());
                intent.putExtra("fileName",selectedApplicationForm.getApplicationFormFileName());

                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left);

                Toast.makeText(getActivity(),application_form_List2.get(i).getApplicationFormTitle(),Toast.LENGTH_SHORT).show();
            }
        });



    }

    class LoadAllApplicationFormFromServer extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
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

        /**
         * getting All products from url
         * */





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

        /**
         * After completing background task Dismiss the progress dialog
         * **/
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


}
