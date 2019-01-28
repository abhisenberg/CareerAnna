package com.careeranna.careeranna.fragement.profile_fragements;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
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
public class NotesFragment extends Fragment implements PdfAdapter.OnItemClickListener{

    TextView heading;

    CardView cardView;

    ArrayList<String> urls;

    RecyclerView recyclerView;

    String status;
    ArrayList<String> pdfs;

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

        cardView = view.findViewById(R.id.card);

        return view;
    }

    public void addPdf(ArrayList<String> pdf, String status) {
        this.urls = pdf;
        this.status = status;

        if(!status.equals("No pdf")) {

            GridLayoutManager mGridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
            recyclerView.setLayoutManager(mGridLayoutManager);
            pdfs = new ArrayList<>();

            for(String string: urls) {
                string = string.replaceAll("/\\d","");
                string = string.replaceAll("/", " ");
                string = string.replaceAll("_", " ");
                int pos = string.indexOf(".");
                string = string.substring(0, pos);
                pdfs.add(string);
            }

            PdfAdapter pdfAdapter = new PdfAdapter(getApplicationContext(), pdfs);
            recyclerView.setAdapter(pdfAdapter);
            pdfAdapter.setOnItemClicklistener(this);
            cardView.setVisibility(View.GONE);
        } else {
            cardView.setVisibility(View.VISIBLE);
        }
        heading.setText(status);
    }

    @Override
    public void onItemClick1(int position) {
        if (urls != null) {
            startActivity(new Intent(getApplicationContext(), Pdf.class).putExtra("pdf", urls.get(position)));
        }
    }
}
