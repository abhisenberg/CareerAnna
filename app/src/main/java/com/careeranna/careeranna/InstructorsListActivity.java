package com.careeranna.careeranna;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.careeranna.careeranna.adapter.InstructorsAdapter;
import com.careeranna.careeranna.data.Instructor;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InstructorsListActivity extends AppCompatActivity implements RecyclerViewPager.OnPageChangedListener, Response.ErrorListener, Response.Listener<String>, View.OnClickListener {

    public static final String TAG = "InstructorsListActivity";
    RecyclerViewPager rvp_instructors;
    InstructorsAdapter adapter;
    ArrayList<Instructor> instructorsList;
    ProgressBar progressBar;
    FloatingActionButton bt_left_rvp, bt_right_rvp;

    private int rvp_current_page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructors_list);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("CareerAnna Mentors");
        }

        instructorsList = new ArrayList<>();
        rvp_current_page = 0;

        bt_left_rvp = findViewById(R.id.rvp_left_bt);
        bt_right_rvp = findViewById(R.id.rvp_right_bt);
        progressBar = findViewById(R.id.pb_instructor);
        rvp_instructors = findViewById(R.id.rvp_instructor);
        rvp_instructors.setFlingFactor(0.1f);
        rvp_instructors.setVerticalFadingEdgeEnabled(true);
        rvp_instructors.setVerticalFadingEdgeEnabled(true);
        rvp_instructors.addOnPageChangedListener(this);

        /*
        These functions are necessary to produce the flick effect on scrolling instructors list
         */
        rvp_instructors.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int childCount = rvp_instructors.getChildCount();
                int width = rvp_instructors.getChildAt(0).getWidth();
                int padding = (rvp_instructors.getWidth() - width) / 2;

                for (int j = 0; j < childCount; j++) {
                    View v = recyclerView.getChildAt(j);
                    float rate = 0;
                    ;
                    if (v.getLeft() <= padding) {
                        if (v.getLeft() >= padding - v.getWidth()) {
                            rate = (padding - v.getLeft()) * 1f / v.getWidth();
                        } else {
                            rate = 1;
                        }
                        v.setScaleY(1 - rate * 0.1f);
                        v.setScaleX(1 - rate * 0.1f);

                    } else {
                        if (v.getLeft() <= recyclerView.getWidth() - padding) {
                            rate = (recyclerView.getWidth() - padding - v.getLeft()) * 1f / v.getWidth();
                        }
                        v.setScaleY(0.9f + rate * 0.1f);
                        v.setScaleX(0.9f + rate * 0.1f);
                    }
                }
            }
        });
        rvp_instructors.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                if (rvp_instructors.getChildCount() < 3) {
                    if (rvp_instructors.getChildAt(1) != null) {
                        if (rvp_instructors.getCurrentPosition() == 0) {
                            View v1 = rvp_instructors.getChildAt(1);
                            v1.setScaleY(0.9f);
                            v1.setScaleX(0.9f);
                        } else {
                            View v1 = rvp_instructors.getChildAt(0);
                            v1.setScaleY(0.9f);
                            v1.setScaleX(0.9f);
                        }
                    }
                } else {
                    if (rvp_instructors.getChildAt(0) != null) {
                        View v0 = rvp_instructors.getChildAt(0);
                        v0.setScaleY(0.9f);
                        v0.setScaleX(0.9f);
                    }
                    if (rvp_instructors.getChildAt(2) != null) {
                        View v2 = rvp_instructors.getChildAt(2);
                        v2.setScaleY(0.9f);
                        v2.setScaleX(0.9f);
                    }
                }
            }
        });

        bt_right_rvp.setOnClickListener(this);
        bt_left_rvp.setOnClickListener(this);

        rvp_instructors.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));

        adapter = new InstructorsAdapter(this, instructorsList);
        rvp_instructors.setAdapter(adapter);
        fetchInstructors();
    }

    private void fetchInstructors(){
        /*
        TODO: Fetch instructors from server, make the Volley Singleton
         */
        final String instructorsListUrl = "https://careeranna.com/api/getInstructor.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, instructorsListUrl, this, this);
        requestQueue.add(request);
    }

    /*
    To get the current page and store it in a global variable through which we can perform button based
    scrolling.
     */
    @Override
    public void OnPageChanged(int i, int i1) {
        Log.d(TAG, "OnPageChanged: oldPage = "+i+", newPage = "+i1);
        rvp_current_page = i1;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "An error has occured!", Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.INVISIBLE);
        Log.d(TAG, "onErrorResponse: "+error.networkResponse);
    }

    /*
    The description field of the instructors is blank in the database hence the 'desc' is
    left empty.
     */
    @Override
    public void onResponse(String response) {
        Log.i(TAG, "getInstructors response: "+response);
        /*
        TODO: Add the image and description of the instructors
         */
        try {
            JSONArray instructosArray = new JSONArray(response);
            for(int i=0; i<instructosArray.length(); i++){
                JSONObject instructorJSON = instructosArray.getJSONObject(i);
                Instructor instructor = new Instructor(
                        instructorJSON.getString("USER_USERNAME"),
                        instructorJSON.getString("USER_PHOTO"),
                        "",
                        instructorJSON.getString("USER_EMAIL")
                );
                instructorsList.add(instructor);
            }
            adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.INVISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rvp_left_bt:
                rvp_instructors.post(new Runnable() {
                    @Override
                    public void run() {
                        if(rvp_current_page != 0)
                            rvp_instructors.smoothScrollToPosition(rvp_current_page-1);
                    }
                });
                break;

            case R.id.rvp_right_bt:
                rvp_instructors.post(new Runnable() {
                    @Override
                    public void run() {
                        if(rvp_current_page != instructorsList.size()-1)
                            rvp_instructors.smoothScrollToPosition(rvp_current_page+1);
                    }
                });
                break;
        }
    }
}
