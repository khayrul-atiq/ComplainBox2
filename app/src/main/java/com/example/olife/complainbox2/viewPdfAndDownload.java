package com.example.olife.complainbox2;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;

import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class viewPdfAndDownload extends AppCompatActivity {

    private static final int  MEGABYTE = 1024 * 1024;
    private NotificationManager mNotifyManager;
    private Builder build;
    private ProgressDialog pDialog;
    int id = 1;
    String file_url,fileName,title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notice_pdf);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle extras = getIntent().getExtras();
        title = extras.getString("title");
        file_url = extras.getString("selected_url");
        fileName = extras.getString("fileName");

        setTitle(title);

        new viewPdfFromServer().execute(file_url);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void downloadRequest(){
        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        build = new NotificationCompat.Builder(viewPdfAndDownload.this);
        build.setContentTitle("Download")
                .setContentText("Download in progress")
                .setSmallIcon(R.drawable.ic_arrow_downward_black_24dp);
        new DownloadFile().execute(file_url,fileName);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.download || item.getItemId()==R.id.action_settings){
            downloadRequest();

        }

        else return super.onOptionsItemSelected(item);
        return  true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.download_menu, menu);
        return true;
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right);
    }

    private class DownloadFile extends AsyncTask<String, Integer, String>{



        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            // Displays the progress bar for the first time.
            build.setProgress(100, 0, false);
            mNotifyManager.notify(id, build.build());
        }

        @Override
        protected String doInBackground(String... strings) {
            String fileUrl = strings[0];
            String fileName = strings[1];
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "Download");
            if(!folder.exists()){
                folder.mkdir();
            }

            fileName = getStoringFileName(folder,fileName);

            File pdfFile = new File(folder, fileName);

            try{
                pdfFile.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
            //FileDownloader.downloadFile(fileUrl, pdfFile);

            try {

                URL url = new URL(fileUrl);
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                //urlConnection.setRequestMethod("GET");
                //urlConnection.setDoOutput(true);
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                FileOutputStream fileOutputStream = new FileOutputStream(pdfFile);
                int totalSize = urlConnection.getContentLength();

                byte[] buffer = new byte[MEGABYTE];
                int bufferLength = 0;
                int calculatedLength = 0;
                while((bufferLength = inputStream.read(buffer))>0 ){
                    fileOutputStream.write(buffer, 0, bufferLength);
                    calculatedLength+=bufferLength;
                    publishProgress((calculatedLength / totalSize) * 100);
                }
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            build.setProgress(100, values[0], false);
            mNotifyManager.notify(id, build.build());
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            build.setContentText("Download complete");
            // Removes the progress bar
            build.setProgress(0, 0, false);
            mNotifyManager.notify(id, build.build());
        }


        String getStoringFileName(File folder,String fileName){

            String []array = fileName.split("\\.");
            fileName = array[0];
            String fileExtention = "."+array[1];
            int count = 0;
            for (File f : folder.listFiles()) {
                if (f.isFile()){
                    String name = f.getName();
                    if(name.contains(fileName)) count++;
                }
            }

            if(count>0)fileName += "("+count+")"+fileExtention;
            else fileName += fileExtention;
            return fileName;
        }
    }


    private class  viewPdfFromServer extends AsyncTask<String,Void,InputStream> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(viewPdfAndDownload.this);
            pDialog.setMessage("Loading notice. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        @Override
        protected InputStream doInBackground(String... strings) {

            InputStream inputStream = null;

            try {
                URL url = new URL(strings[0]);

                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                    if(httpURLConnection.getResponseCode()==200){
                        inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            pDialog.dismiss();
            super.onPostExecute(inputStream);
            PDFView pdfView = findViewById(R.id.pdfView);
            pdfView.fromStream(inputStream).load();
        }
    }
}
