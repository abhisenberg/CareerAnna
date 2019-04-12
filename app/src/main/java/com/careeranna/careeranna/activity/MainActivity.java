package com.careeranna.careeranna.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.careeranna.careeranna.R;
import com.careeranna.careeranna.adapter.SlideAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;


import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * Declaring Variables for firebase authentication
     */

    FirebaseAuth mAuth;
    public static final String TAG = "MainAct";

    private LinearLayout dotsLayout;    // Layout For the dots below landing page images

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
         * Initializing Layout Variables
         */

        ViewPager introSlider = findViewById(R.id.intro_viewpager);
        Button privacy_policy = findViewById(R.id.privacy_policy);
        introSlider.addOnPageChangeListener(viewListener);
        Button bt_explore = findViewById(R.id.bt_explore);
        dotsLayout = findViewById(R.id.intro_dots);
        Button signIn = findViewById(R.id.signIn);

        privacy_policy.setOnClickListener(this);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel =
                    new NotificationChannel("MyNotifications", "MyNotifications", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        /*
         * Initializing Paper db and retrieving fireBase user
         */

        mAuth = FirebaseAuth.getInstance();
        Paper.init(this);

        /*
         *  Hiding Action Bar
         */

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        /*
         *  Slider For App Landing Page Images
         */

        // Slider Adapter For Landing Images
        SlideAdapter slideAdapter = new SlideAdapter(this);
        introSlider.setAdapter(slideAdapter);
        addDots(0);

        /*
         *  Button Listener For Explore And Sign up Button
         */

      //  requestNotificationPermission();

        bt_explore.setOnClickListener(this);
        signIn.setOnClickListener(this);

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


    /*
     * Click Events For Explore And Sign up button
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signIn:
                Intent intent = new Intent(this, SignInActivity.class);
                startActivity(intent);
                break;

            case R.id.bt_explore:
                Intent openExplorePage = new Intent(this, ExploreNotSignActivity.class);
                startActivity(openExplorePage);
                break;

            case R.id.privacy_policy:
                String url = "https://www.careeranna.com/privacy_policy";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        FirebaseMessaging.getInstance().subscribeToTopic("test").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        });

        String cache = Paper.book().read("user");
        if (cache != null && !cache.isEmpty()) {
            startActivity(new Intent(this, MyCourses.class));
            finish();
        }

    }

}
