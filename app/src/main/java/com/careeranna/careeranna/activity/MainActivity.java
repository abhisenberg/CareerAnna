package com.careeranna.careeranna.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.careeranna.careeranna.BuildConfig;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.data.Constants;
import com.careeranna.careeranna.service.VideoService;
import com.careeranna.careeranna.user.SignUp;
import com.careeranna.careeranna.adapter.SlideAdapter;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * Declaring Variables for firebase authentication
     */

    PopupWindow popupWindow;
    FirebaseAuth mAuth;
    public static final String TAG = "MainAct";

    public static int counter = 0;  // Counter For Counting User Opening The App

    private LinearLayout dotsLayout;    // Layout For the dots below landing page images
    private ViewPager introSlider;      // Slider ViewPager For Images
    private SlideAdapter slideAdapter;  // Slider Adapter For Lanfing Images

    private Button bt_explore;          // Button For Explore Without Sign Up
    private Button bt_signin;           // Button To Go To Sign up

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Initializing Layout Variables
         */

        dotsLayout = findViewById(R.id.intro_dots);
        introSlider = findViewById(R.id.intro_viewpager);
        introSlider.addOnPageChangeListener(viewListener);
        bt_explore = findViewById(R.id.bt_explore);
        bt_signin = findViewById(R.id.signIn);


        /**
         * Initializing Paper db and retrieving firebase user
         */

        mAuth = FirebaseAuth.getInstance();
        Paper.init(this);

        /**
         *  Hiding Action Bar
         */

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        /**
         *  Slider For App Landing Page Images
         */

        slideAdapter = new SlideAdapter(this);
        introSlider.setAdapter(slideAdapter);
        addDots(0);

        /**
         *  Button Listener For Explore And Sign up Button
         */

        bt_explore.setOnClickListener(this);
        bt_signin.setOnClickListener(this);
    }


    /**
     * Slider Listener For changing of active dots as per the position of the image visible
     */

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            addDots(i);
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    /**
     * Add Changing Dots Active State Accoring To Position
     *
     * @param i ->  refers to the postion of dots according to image which is visible to user
     */
    private void addDots(int i) {
        dotsLayout.removeAllViews();
        TextView[] dots = new TextView[3];

        for (int x = 0; x < dots.length; x++) {
            dots[x] = new TextView(this);
            dots[x].setText(String.valueOf(Html.fromHtml("&#8226")));
            dots[x].setTextSize(30);
            dots[x].setTextColor(getResources().getColor(R.color.light_gray));

            Log.d(TAG, "addDots: " + x);
            dotsLayout.addView(dots[x]);
        }

        dots[i].setTextColor(getResources().getColor(R.color.black));
    }


    /**
     * Click Events For Explore And Sign up button
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signIn:
                Intent intent = new Intent(this, SignUp.class);
                startActivity(intent);
                break;

            case R.id.bt_explore:

//                startService(new Intent(this, VideoService.class));
                Intent openExplorePage = new Intent(this, ExploreNotSignActivity.class);
                startActivity(openExplorePage);
                break;
        }
    }

    /**
     * Checking user count of open the app without sign up
     * and also checking if the user is login before and not sign out then redirect to dashboard if login
     * Also Checking Version Update
     */

    @Override
    protected void onResume() {
        super.onResume();

//        startActivity(new Intent(this, SignUp.class));

        //TODO: Uncomment this line when publishing updates to play store
        String cache = Paper.book().read("user");
        if (cache != null && !cache.isEmpty()) {
            startActivity(new Intent(MainActivity.this, MyCourses.class));
            finish();
        }

    }

    /**
     * Checking the version of the app with the playstore version
     * if the version is same no dialog if the version is older giving update dialog and redirect to playstore
     */
    /**
     * Alert Dialog For Update Which Will Send Him to Playstore Page
     */

}
