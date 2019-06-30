package com.careeranna.careeranna.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.activity.MainActivity;
import com.careeranna.careeranna.data.CourseWithLessData;
import com.careeranna.careeranna.data.User;
import com.careeranna.careeranna.fragement.user_myprofile_fragments.TermsOfServiceFragment;
import com.careeranna.careeranna.fragement.user_myprofile_fragments.UserMyProfileFragment;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.util.ArrayList;

import io.paperdb.Paper;

public class MyProfile_2 extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    public static final String TAG = "MyProfile_2";

    User user;
    FirebaseAuth mAuth;
    Fragment fragment;
    Button bt_signOut;              //Make the signout button visible only when showing the UserProfile fragment
    RequestQueue requestQueue;
    ProgressBar progressBar;
    BottomNavigationView bottomNavigationView;

    ImageView user_image;
    TextView user_name, user_email;

    ArrayList<CourseWithLessData> coursesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_3);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Account");
        }

        Paper.init(this);

        bt_signOut = findViewById(R.id.bt_userp_profile_signout_3);
        user_image = findViewById(R.id.iv_profileImage_3);
        user_name = findViewById(R.id.tv_profileName_3);
        user_email = findViewById(R.id.tv_profileMail_3);
        progressBar = findViewById(R.id.my_profile_3_progress_bar);
        bottomNavigationView = findViewById(R.id.myProfile_bottom_navigation_3);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setItemIconTintList(null);
        bt_signOut.setOnClickListener(this);

        String cache = Paper.book().read("user");
        if(cache != null && !cache.isEmpty()){
            user = new Gson().fromJson(cache, User.class);
            user_name.setText(user.getUser_username());
            user_email.setText(user.getUser_email());
            /*
        If there is no image url provided, then write the initial letter of the username
         */
            TextView initialAlphabet = findViewById(R.id.user_profile_3_username_initial);
            if(!user.getUser_photo().isEmpty()){
                initialAlphabet.setVisibility(View.INVISIBLE);
                Glide.with(this).load(user.getUser_photo()).into(user_image);
            }
            else
            {
                initialAlphabet.setVisibility(View.VISIBLE);
                initialAlphabet.setText(user.getUser_username().substring(0,1));
            }
        }

        requestQueue = Volley.newRequestQueue(this);

        /*
        Currently there is no use of the progress bar so hide it, enable it if in future versions there
        is some use for it.
         */
        progressBar.setVisibility(View.INVISIBLE);

        /*
        Show myCourses first at this MyProfile screen
         */
        loadUserProfileFragment();

    }

    public void loadUserProfileFragment(){
        fragment = new UserMyProfileFragment();
        loadFragment(fragment);
        bt_signOut.setVisibility(View.VISIBLE);         //Make the signout button visible only when showing the UserProfile fragment
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//        progressBar.setVisibility(View.VISIBLE);
        switch (menuItem.getItemId()){
            case R.id.navigation_myCourses:
//                loadUserCoursesFragment();
                loadUserProfileFragment();
                return true;

            case R.id.navigation_terms:
                fragment = new TermsOfServiceFragment();
                loadFragment(fragment);
                bt_signOut.setVisibility(View.INVISIBLE);
                return true;
        }
        return false;
    }

    private void loadFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.myProfile_fragment_3, fragment);
        transaction.commit();
//        if(!(fragment instanceof UserCoursesFragment))
//            progressBar.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_userp_profile_signout_3:
                signOut();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
    }

    public void signOut(){
        mAuth = FirebaseAuth.getInstance();
        if(mAuth != null) {
            mAuth.signOut();
            LoginManager.getInstance().logOut();
        }
        Paper.delete("user");
    }
}
