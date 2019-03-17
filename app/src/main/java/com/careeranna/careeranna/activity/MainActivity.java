package com.careeranna.careeranna.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.careeranna.careeranna.R;
import com.careeranna.careeranna.data.Constants;
import com.careeranna.careeranna.user.SignUp;
import com.careeranna.careeranna.adapter.SlideAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * Declaring Variables for firebase authentication
     */

    private static final int NOTIFICATION_PERMISSION_CODE = 123;

    PopupWindow popupWindow;
    int language;

    FirebaseAuth mAuth;
    public static final String TAG = "MainAct";

    public static int counter = 0;  // Counter For Counting User Opening The App

    private LinearLayout dotsLayout;    // Layout For the dots below landing page images
    private ViewPager introSlider;      // Slider ViewPager For Images
    private SlideAdapter slideAdapter;  // Slider Adapter For Lanfing Images

    private Button bt_explore;          // Button For Explore Without Sign Up
    private Button bt_signin, privacy_policy;           // Button To Go To Sign up

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
         * Initializing Layout Variables
         */

        dotsLayout = findViewById(R.id.intro_dots);
        introSlider = findViewById(R.id.intro_viewpager);
        introSlider.addOnPageChangeListener(viewListener);
        bt_explore = findViewById(R.id.bt_explore);
        bt_signin = findViewById(R.id.signIn);
        privacy_policy  = findViewById(R.id.privacy_policy);

        privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.careeranna.com/privacy_policy";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel =
                    new NotificationChannel("MyNotifications", "MyNotifications", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        /*
         * Initializing Paper db and retrieving firebase user
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

        slideAdapter = new SlideAdapter(this);
        introSlider.setAdapter(slideAdapter);
        addDots(0);

        /*
         *  Button Listener For Explore And Sign up Button
         */

      //  requestNotificationPermission();

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


    /*
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

        FirebaseMessaging.getInstance().subscribeToTopic("test").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
            }
        });

        try {
            language = Paper.book().read(Constants.LANGUAGE);
            if (language == 2) {
                setLocale("hi");
            } else if (language == 1) {
                setLocale("en");
            }
            Log.d("in_try", language + " ");
        } catch (Exception e) {
            language = 1;
            Log.d("in_catch", language + " ");
            Paper.book().write(Constants.LANGUAGE, language);
        }

//        startActivity(new Intent(this, SignUp.class));

        //TODO: Uncomment this line when publishing updates to play store
        String cache = Paper.book().read("user");
        if (cache != null && !cache.isEmpty()) {
            startActivity(new Intent(this, MyCourses.class));
            finish();
        }

    }

    /*
     * Checking the version of the app with the playstore version
     * if the version is same no dialog if the version is older giving update dialog and redirect to playstore
     */
    /*
     * Alert Dialog For Update Which Will Send Him to Playstore Page
     */

    private void setLocale(String lang) {

        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        onConfigurationChanged(conf);
    }

    private void requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NOTIFICATION_POLICY) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_NOTIFICATION_POLICY)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed for the notification of the latest articles and videos")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_NOTIFICATION_POLICY}, NOTIFICATION_PERMISSION_CODE );
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
        }

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NOTIFICATION_POLICY}, NOTIFICATION_PERMISSION_CODE );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == NOTIFICATION_PERMISSION_CODE ) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {


        slideAdapter = new SlideAdapter(this);
        introSlider.setAdapter(slideAdapter);
        addDots(0);

        bt_signin.setText(getResources().getString(R.string.sign_in));
        bt_explore.setText(getResources().getString(R.string.explore));
        super.onConfigurationChanged(newConfig);
    }

}
