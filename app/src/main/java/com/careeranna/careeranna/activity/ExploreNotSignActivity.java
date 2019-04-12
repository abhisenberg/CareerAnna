package com.careeranna.careeranna.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.careeranna.careeranna.fragement.NoInternetFragment;
import com.careeranna.careeranna.fragement.explore_not_sign_in_fragements.InsideWithoutSignFragment;
import com.careeranna.careeranna.R;
import io.paperdb.Paper;

public class ExploreNotSignActivity extends AppCompatActivity implements NoInternetFragment.OnFragemntClickListener{

    public static int counter = 0;  // Counter For Counting User Opening The App

    InsideWithoutSignFragment insideWithoutSignFragment;    // Fragment Inside Explore to Show Details

    NoInternetFragment noInternetFragment;

    FragmentManager fragmentManager;                        // Fragment Manager To Change Fragments


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_not_sign);


        /*
         * Initializing Fragment Which will have all the free videos and courses
         */

        insideWithoutSignFragment = new InsideWithoutSignFragment();

        noInternetFragment = new NoInternetFragment();
        noInternetFragment.setOnFragementClicklistener(this);

        /*
         * Getting Fragment Manager From  The Activity
         */

        fragmentManager = getSupportFragmentManager();

        /*
         * Replacing Main Container With Fragment Inside Explore
         */

        if(amIConnect()) {
            fragmentManager.beginTransaction().replace(R.id.main_content, insideWithoutSignFragment).commit();

        } else {
            fragmentManager.beginTransaction().replace(R.id.main_content, noInternetFragment).commit();
        }

        Paper.init(this);

        showPopUpForSignUp();       // Counter For User Accessing Explore Without Sign Up
    }

    /**
     * Inflating Menu Sign In Which Has Only One Item For Sign In
     *
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.signin_button, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Menu Item For go To Sign up Activity from Without Sign In Explore
     *
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_signin:
                Intent signInActivity = new Intent(this, SignInActivity.class);
                startActivity(signInActivity);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Checking If User Is Connected To Wifi Or Mobile Internet
     *
     */

    private boolean amIConnect() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }


    /**
     * Function For Dialog For Send Him To Sign After 3rd Times He Enter Without Sign Up
     */

    public void showPopUpForSignUp() {

        int cache;
        try {
            cache = Paper.book().read("counter");
        } catch (NullPointerException e){
            cache = 0;
            Paper.book().write("counter", counter);
        }
        if(cache > -1 ) {
            counter = cache;
            Paper.book().write("counter", counter+1);
            if(counter > 2) {
                alertDialogForSignUp();
            }
        } else {
            Paper.book().write("counter", counter);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Alert Dialog For Sign up
     */

    private void alertDialogForSignUp() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Free Limit Reached");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setCancelable(false);

        builder.setMessage(getString(R.string.open_browser_limit_reached))
                .setNegativeButton("Sign Up", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(ExploreNotSignActivity.this, SignInActivity.class));
                    }
                })
                .setPositiveButton("Sign In", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(ExploreNotSignActivity.this, SignInActivity.class));
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onItemClick1() {

        fragmentManager.beginTransaction().replace(R.id.main_content, insideWithoutSignFragment).commit();

    }
}
