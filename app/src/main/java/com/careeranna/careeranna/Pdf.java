package com.careeranna.careeranna;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import retrofit2.http.Url;

public class Pdf extends AppCompatActivity {

    com.github.barteksc.pdfviewer.PDFView pdfViewer;

    String pdf;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        pdfViewer = findViewById(R.id.pdf);


        if(getIntent().getStringExtra("pdf") != null) {
            pdf = getIntent().getStringExtra("pdf");
        //    pdfViewer.fromUri(Uri.parse("https://www.careeranna.com/uploads/lesson_related_files/"+getIntent().getStringExtra("pdf"))).load();
            new RetrievePdfStream().execute("https://www.careeranna.com/uploads/lesson_related_files/"+getIntent().getStringExtra("pdf").replaceAll("\\s", "%20"));
        } else {
            pdfViewer.fromAsset("pdfview.pdf").load();
        }

    }

    class RetrievePdfStream extends AsyncTask<String, Void, InputStream> {

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(Pdf.this);
            progressDialog.setMessage(getString(R.string.loading_just_a_sec));
            progressDialog.show();

        }

        @Override
        protected InputStream doInBackground(String... strings) {

            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if(urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {

            progressDialog.dismiss();
            pdfViewer.fromStream(inputStream).load();
        }
    }
}
