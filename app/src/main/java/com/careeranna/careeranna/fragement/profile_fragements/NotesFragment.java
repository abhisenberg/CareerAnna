package com.careeranna.careeranna.fragement.profile_fragements;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.careeranna.careeranna.Pdf;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.adapter.ExpandableList_Adapter;
import com.careeranna.careeranna.adapter.PdfAdapter;
import com.careeranna.careeranna.data.Topic;
import com.careeranna.careeranna.data.Unit;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotesFragment extends Fragment {

    TextView heading;

    ArrayList<String> urls;

    RecyclerView recyclerView;

    String status;

    private String[] imageUrls = new String[] {
            "https://cdn2.iconfinder.com/data/icons/web-and-apps-interface/32/OK-512.png",
            "https://cdn2.iconfinder.com/data/icons/web-and-apps-interface/32/OK-512.png",
            "https://3mxwfc45nzaf2hj9w92hd04y-wpengine.netdna-ssl.com/wp-content/uploads/2015/03/inside-page-style-blank-3.jpg",
            "https://cdn2.iconfinder.com/data/icons/web-and-apps-interface/32/OK-512.png",
            "https://cdn2.iconfinder.com/data/icons/web-and-apps-interface/32/OK-512.png",
            "https://3mxwfc45nzaf2hj9w92hd04y-wpengine.netdna-ssl.com/wp-content/uploads/2015/03/inside-page-style-blank-3.jpg",
    };

    public NotesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        heading = view.findViewById(R.id.weekText);

        recyclerView = view.findViewById(R.id.pdf_rv);

        return view;
    }

    public void addPdf(ArrayList<String> pdfs, String status) {
        this.urls = pdfs;
        this.status = status;

        if(!status.equals("No pdf")) {

            GridLayoutManager mGridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
            recyclerView.setLayoutManager(mGridLayoutManager);
            ArrayList<String> pdf = new ArrayList<>();

            for(String string: urls) {
                pdf.add(string);
            }

            PdfAdapter pdfAdapter = new PdfAdapter(getApplicationContext(), pdf);
            recyclerView.setAdapter(pdfAdapter);
        }
        heading.setText(status);
    }

}
