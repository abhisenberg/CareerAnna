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
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.careeranna.careeranna.data.Course;
import com.careeranna.careeranna.data.MyPaidCourse;
import com.careeranna.careeranna.data.PaidCourse;
import com.careeranna.careeranna.fragement.NoInternetFragment;
import com.careeranna.careeranna.fragement.explore_not_sign_in_fragements.InsideWithoutSignFragment;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.helper.NewApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.view.View.GONE;

public class ExploreNotSignActivity extends AppCompatActivity implements NoInternetFragment.OnFragemntClickListener{

    public static int counter = 0;  // Counter For Counting User Opening The App

    InsideWithoutSignFragment insideWithoutSignFragment;    // Fragment Inside Explore to Show Details

    NoInternetFragment noInternetFragment;

    FragmentManager fragmentManager;                        // Fragment Manager To Change Fragments

    ArrayList<Course> coursesForExplore, freeCourseForExplore;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_not_sign);

        progressBar = findViewById(R.id.progress_bar);

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
            addPaidCourse();
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

    private void addPaidCourse() {

        coursesForExplore = new ArrayList<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NewApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NewApi api = retrofit.create(NewApi.class);

        Call<List<PaidCourse>> call = api.getPaidCourses();

        call.enqueue(new Callback<List<PaidCourse>>() {
            @Override
            public void onResponse(Call<List<PaidCourse>> call, retrofit2.Response<List<PaidCourse>> response) {

                List<PaidCourse> paidCoursesList = response.body();

                for(PaidCourse  paidCourse: paidCoursesList) {
                    coursesForExplore.add(new Course(paidCourse.getProduct_id(),
                            paidCourse.getCourse_name(),
                            "https://www.careeranna.com/" + paidCourse.getProduct_image().replace("\\", ""),
                            paidCourse.getExam_id(),
                            paidCourse.getDiscount(),
                            "",
                            "",
                            "Paid",
                            paidCourse.getPrice(),
                            paidCourse.getAverage_rating(),
                            paidCourse.getTotal_rating(),
                            paidCourse.getLearners_count()));
                }

                addFreeCoursesForExplore();
            }

            @Override
            public void onFailure(Call<List<PaidCourse>> call, Throwable t) {
            }
        });

    }

    private void addFreeCoursesForExplore() {

        freeCourseForExplore = new ArrayList<>();

        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        String url1 = "https://careeranna.com/api/getFreeCourse.php?id=" + "1";
        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray CategoryArray = new JSONArray(response.toString());
                            for (int i = 0; i < CategoryArray.length(); i++) {
                                JSONObject Category = CategoryArray.getJSONObject(i);
                                freeCourseForExplore.add(new Course(Category.getString("course_id"),
                                        Category.getString("name"),
                                        "https://www.careeranna.com/" + Category.getString("image").replace("\\", ""),
                                        Category.getString("examIds"),
                                        "0"
                                        , "meta_description",
                                        "",
                                        "Free",
                                        "0",
                                        Category.getString("average_rating"),
                                        Category.getString("total_rating"),
                                        Category.getString("learner_count")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressBar.setVisibility(GONE);
                        insideWithoutSignFragment.addFree(coursesForExplore, freeCourseForExplore, new ArrayList<MyPaidCourse>());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        );

        requestQueue1.add(stringRequest1);
    }

}
