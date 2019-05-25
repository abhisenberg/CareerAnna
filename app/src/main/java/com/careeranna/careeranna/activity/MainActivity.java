package com.careeranna.careeranna.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.careeranna.careeranna.R;
import com.careeranna.careeranna.adapter.SlideAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * Declaring Variables for firebase authentication
     */

    FirebaseAuth mAuth;

    TextView think_careeranna,
            think_mba;

    LinearLayout sign_up_and_login;

    RelativeLayout think_layout, think_mba_layout;

    public static final String TAG = "MainAct";

    private LinearLayout dotsLayout;    // Layout For the dots below landing page images
    Button privacy_policy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
         * Initializing Layout Variables
         */

        privacy_policy = findViewById(R.id.privacy_policy);
        Button bt_explore = findViewById(R.id.bt_explore);
        Button signIn = findViewById(R.id.signIn);
        sign_up_and_login = findViewById(R.id.sign_up_and_login);

        think_careeranna = findViewById(R.id.think_careeranna);
        think_mba = findViewById(R.id.think_mba);
        think_mba_layout = findViewById(R.id.think_mba_layout);

        think_layout = findViewById(R.id.think_layout);

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


        final Animation aniSlide = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in_animation_text);
        think_mba_layout.startAnimation(aniSlide);

        think_layout.setAlpha(0f);
        privacy_policy.setAlpha(0f);
        sign_up_and_login.setAlpha(0f);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms

                think_layout.setAlpha(1f);
                final Animation aniSlide1 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in_animation_careeranna);
                think_layout.startAnimation(aniSlide1);
            }
        }, 1000);



        final Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms


                privacy_policy.setAlpha(1f);
                final Animation aniSlide1 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in_animation_privacy);
                privacy_policy.startAnimation(aniSlide1);
            }
        }, 1500);

        final Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms


                sign_up_and_login.setAlpha(1f);
                final Animation aniSlide1 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in_animation_sign_up);
                sign_up_and_login.startAnimation(aniSlide1);
            }
        }, 2000);


        /*
         *  Button Listener For Explore And Sign up Button
         */

      //  requestNotificationPermission();

        bt_explore.setOnClickListener(this);
        signIn.setOnClickListener(this);

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

        FirebaseMessaging.getInstance().subscribeToTopic("my_notifications_3").addOnSuccessListener(new OnSuccessListener<Void>() {
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
