package com.careeranna.careeranna;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.careeranna.careeranna.data.Course;
import com.careeranna.careeranna.data.ExamPrep;

public class ExamPrepActivity extends AppCompatActivity {

    TextView price,desc;

    Button purchaseCourse;

    VideoView videoView;

    ExamPrep examPrep;

    MediaController mediaController;

    AlertDialog.Builder mBuilder;

    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_prep);

        price = findViewById(R.id.dollar);
        videoView =  findViewById(R.id.playerView);
        desc = findViewById(R.id.descTextDetails);
        purchaseCourse = findViewById(R.id.purchase);

        Intent intent = getIntent();
        examPrep = (ExamPrep) intent.getSerializableExtra("Examp");

        mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);

        setCourse();

        videoView.start();

        purchaseCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buyCourse();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_cart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.add_to_cart) {

            buyCourse();
        }

        return super.onOptionsItemSelected(item);
    }

    private void buyCourse() {

        mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle("Are You Sure You Want To Buy This Course");
        mBuilder.setCancelable(false);
        mBuilder.setMessage("Course Name : " + examPrep.getName() +" \n Price : "+ examPrep.getPrice() );
        mBuilder.setPositiveButton("Go To Cart", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog = mBuilder.show();
    }

    private void setCourse() {

        Uri uri = Uri.parse(examPrep.getDemo_url());
        videoView.setVideoPath(uri.toString());

        desc.setText(examPrep.getDesc());

        price.setText(examPrep.getPrice());

        getSupportActionBar().setTitle(examPrep.getName());

    }

    public void hideDesc(View view) {

        if(desc.getVisibility() == View.INVISIBLE) {
            desc.setVisibility(View.VISIBLE);
        } else {
            desc.setVisibility(View.INVISIBLE);
        }

    }
}

