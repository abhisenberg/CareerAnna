package com.careeranna.careeranna.user;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.careeranna.careeranna.InstructorsListActivity;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.data.CourseWithLessData;
import com.careeranna.careeranna.data.Instructor;
import com.careeranna.careeranna.data.User;
import com.careeranna.careeranna.fragement.user_myprofile_fragments.UserCertificatesFragment;
import com.careeranna.careeranna.fragement.user_myprofile_fragments.UserCoursesFragment;
import com.careeranna.careeranna.fragement.user_myprofile_fragments.UserSuggestedFragment;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

public class MyProfile_2 extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "MyProfile_2";

    User user;
    Fragment fragment;
    RequestQueue requestQueue;
    ProgressBar progressBar;
    BottomNavigationView bottomNavigationView;

    ImageView user_image;
    TextView user_name;

    ArrayList<CourseWithLessData> coursesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_2);

        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        user_image = findViewById(R.id.iv_profileImage);
        user_name = findViewById(R.id.tv_profileName);
        progressBar = findViewById(R.id.my_profile_2_progress_bar);
        bottomNavigationView = findViewById(R.id.myProfile_bottom_navigation_2);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setItemIconTintList(null);

        String cache = Paper.book().read("user");
        if(cache != null && !cache.isEmpty()){
            user = new Gson().fromJson(cache, User.class);
            user_name.setText(user.getUser_username());
            Glide
                    .with(this)
                    .load(user.getUser_photo())
                    .into(user_image);
        }

        requestQueue = Volley.newRequestQueue(this);

        /*
        Show myCourses first at this MyProfile screen
         */
        loadUserCoursesFragment();

    }

    public void loadUserCoursesFragment(){
        fragment = new UserCoursesFragment();
        fetchUserCourses();
        loadFragment(fragment);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        progressBar.setVisibility(View.VISIBLE);
        switch (menuItem.getItemId()){
            case R.id.navigation_myCourses:
                loadUserCoursesFragment();
                return true;

            case R.id.navigation_certificates:
                fragment = new UserCertificatesFragment();
                loadFragment(fragment);
                return true;

            case R.id.navigation_suggested:
                fragment = new UserSuggestedFragment();
                loadFragment(fragment);
                return true;
        }
        return false;
    }

    private void loadFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.myProfile_fragment, fragment);
        transaction.commit();
        if(!(fragment instanceof UserCoursesFragment))
            progressBar.setVisibility(View.INVISIBLE);
    }

    public void fetchUserCourses(){
        Log.d(TAG, "FetchingUserCourses ");
        final String courses_url = "https://careeranna.com/api/getMyCourse.php?user="+user.getUser_id()+"&category=15";
        StringRequest coursesRequest = new StringRequest(Request.Method.GET, courses_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            coursesList = new ArrayList<>();
                            JSONArray coursesArray = new JSONArray(response);
                            for(int i=0; i<coursesArray.length(); i++){
                                JSONObject courseJSON = coursesArray.getJSONObject(i);
                                CourseWithLessData course = new CourseWithLessData(
                                        courseJSON.getString("product_name"),
                                        courseJSON.getString("product_id"),
                                        courseJSON.getString("product_image")
                                );
                                coursesList.add(course);
                            }
                            Log.d(TAG, "fetchedCourses = "+coursesList.size());

                            if(fragment instanceof UserCoursesFragment)
                                ((UserCoursesFragment)fragment).setCourses(coursesList);

                            progressBar.setVisibility(View.INVISIBLE);
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "CourseFetchError: "+error.networkResponse);
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(MyProfile_2.this, "Network error occured!", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user", user.getUser_id());
                params.put("category", "15");
                return params;
            }
        };

        requestQueue.add(coursesRequest);
    }

    public void fetchUserCertificates(){
        /*
        TODO: fetch certificates
         */
    }

    public void fetchSuggestedCourses(){
        /*
        TODO: fetch suggested
         */
    }

}
