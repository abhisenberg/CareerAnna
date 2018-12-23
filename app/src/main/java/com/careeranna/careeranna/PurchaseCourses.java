package com.careeranna.careeranna;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.careeranna.careeranna.JW_Player_Files.KeepScreenOnHandler;
import com.careeranna.careeranna.data.Course;
import com.careeranna.careeranna.data.ExamPrep;
import com.careeranna.careeranna.data.User;
import com.google.gson.Gson;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.events.FullscreenEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;
import com.payu.india.CallBackHandler.OnetapCallback;
import com.payu.india.Model.PaymentParams;
import com.payu.india.Model.PayuConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.paperdb.Paper;

public class PurchaseCourses extends AppCompatActivity implements VideoPlayerEvents.OnFullscreenListener{

    public static final String TAG = "PurchaseCourses";

    private static final Pattern REMOVE_TAGS = Pattern.compile("<.+?>");

    private static final Pattern REMOVE_TAGS1 = Pattern.compile("&.+;");

    TextView price,desc;

    TextView description;

    Button purchaseCourse;

    JWPlayerView playerView;

    Course course;

    ExamPrep examPrep;

    ProgressBar progressBar;

    MediaController mediaController;

    AlertDialog.Builder mBuilder;

    AlertDialog alertDialog;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_courses);

        Paper.init(this);

        progressBar = findViewById(R.id.progress);
        price = findViewById(R.id.dollar);
        playerView =  findViewById(R.id.playerView);
        description = findViewById(R.id.descTextDetails);
        purchaseCourse = findViewById(R.id.purchase);

        Intent intent = getIntent();

        course = (Course) intent.getSerializableExtra("Course");

        if(course == null) {

            examPrep = (ExamPrep) intent.getSerializableExtra("Examp");
            description.setText(removeTags(examPrep.getDesc()).replace("-", "\n"));

        } else {

            description.setText(removeTags(course.getDesc()).replace("-", "\n"));
        }

        playerView.addOnFullscreenListener(this);
        new KeepScreenOnHandler(playerView, getWindow());

        setCourse();

        purchaseCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buyCourse();
            }
        });


        String cache = Paper.book().read("user");
        if(cache != null && !cache.isEmpty()) {
            user =  new Gson().fromJson(cache, User.class);
        }

    }

    public static String removeTags(String string) {
        if (string == null || string.length() == 0) {
            return string;
        }

        Matcher m = REMOVE_TAGS.matcher(string);
        String tag = m.replaceAll("");
        Matcher m1 = REMOVE_TAGS1.matcher(tag);
        return m1.replaceAll("");
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
        if(course != null ) {
            mBuilder.setMessage("Course Name : " + course.getName() + " \n Price : " + course.getPrice());
        } else {
            mBuilder.setMessage("Course Name : " + examPrep.getName() + " \n Price : " + examPrep.getPrice());
        }
        mBuilder.setPositiveButton("Go To Cart", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                progressBar.setVisibility(View.VISIBLE);
                RequestQueue requestQueue =Volley.newRequestQueue(PurchaseCourses.this);
                String url = "https://careeranna.com/api/addProduct.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(PurchaseCourses.this, response, Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        // Posting params to login url
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("user", user.getUser_id());
                        if(course != null ) {
                            params.put("product", course.getId());
                            params.put("category", course.getCategory_id());
                            params.put("name", course.getName());
                            params.put("image", course.getImageUrl());
                        } else {
                            params.put("product", examPrep.getId());
                            params.put("category", examPrep.getCategory_id());
                            params.put("name", examPrep.getName());
                            params.put("image", examPrep.getImageUrl());
                        }
                        return params;
                    }

                };
                requestQueue.add(stringRequest);

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
        Uri uri;
        if(course == null) {
            /*
            Check whether the course type is a video based course or
            exam prep. if "course" is null, it is exam prep.
             */
            uri = Uri.parse(examPrep.getDemo_url());
            price.setText(examPrep.getPrice());
            getSupportActionBar().setTitle(examPrep.getName());
        } else {
            /*
            If "course" is not null, it is video based course.
             */
            uri = Uri.parse(course.getDemo_url());
            price.setText(course.getPrice());
            getSupportActionBar().setTitle(course.getName());
        }

        PlaylistItem playlistItem = new PlaylistItem.Builder()
                .file(uri.toString())
                .build();
        playerView.load(playlistItem);
    }

    public void hideDesc(View view) {

        if(description.getVisibility() == View.INVISIBLE) {
            description.setVisibility(View.VISIBLE);
        } else {
            description.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onFullscreen(FullscreenEvent fullscreenEvent) {
        if(fullscreenEvent.getFullscreen()){
            //If fullscreen, remove action bar
            removeActionBar();
        } else {
            //If not fullscreen, show action bar
            showActionBar();
        }
    }


    @Override
    protected void onDestroy() {
        playerView.onDestroy();
        super.onDestroy();
    }

    private void showActionBar() {
        Log.d(TAG, "showActionBar: ");
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.show();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG, "onConfigurationChanged "+(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE));

        playerView.setFullscreen(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE, true);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPause() {
        playerView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        playerView.onResume();
        super.onResume();
    }

    private void removeActionBar(){
        Log.d(TAG, "removeActionBar: ");
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
    }

    @Override
    public void onBackPressed() {
        if(playerView.getFullscreen())
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        else
            super.onBackPressed();
    }
}