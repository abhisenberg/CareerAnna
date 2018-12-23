package com.careeranna.careeranna;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Pdf extends AppCompatActivity {

    com.github.barteksc.pdfviewer.PDFView pdfViewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        pdfViewer = findViewById(R.id.pdf);
        pdfViewer.fromAsset("pdfview.pdf").load();

    }
}
