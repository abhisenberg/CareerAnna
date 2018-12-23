package com.careeranna.careeranna.fragement.dashboard_fragements;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.VideoWithComment;
import com.careeranna.careeranna.adapter.FreeCourseAdapter;
import com.careeranna.careeranna.adapter.TrendingVideosAdapter;
import com.careeranna.careeranna.data.Course;
import com.careeranna.careeranna.data.FreeVideos;
import com.careeranna.careeranna.user.ExploreNotSIActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ExploreNew extends Fragment implements TrendingVideosAdapter.OnItemClickListener, FreeCourseAdapter.OnItemClickListener{

    ArrayList<FreeVideos> freeVideos, trendingvVideos;

    ArrayList<Course> courses, freecourse;

    RecyclerView recyclerView, recyclerView1, freeCorse, paidCourse;

    ProgressDialog progressDialog;


    public ExploreNew() {
    }

    public void addPaidCourses(ArrayList<Course> courses, ArrayList<Course> freecourse) {

        this.courses = courses;

        this.freecourse = freecourse;

        FreeCourseAdapter freeCourseAdapter = new FreeCourseAdapter(this.courses, getApplicationContext());

        paidCourse.setAdapter(freeCourseAdapter);

        freeCourseAdapter.setOnItemClicklistener(this);

        FreeCourseAdapter freeCourseAdapter1 = new FreeCourseAdapter(this.freecourse, getApplicationContext());

        freeCorse.setAdapter(freeCourseAdapter1);

        initalizeVideos();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_my_explore_new, container, false);

        recyclerView = view.findViewById(R.id.trending_rv);

        recyclerView1 = view.findViewById(R.id.latest_rv);

        freeCorse = view.findViewById(R.id.free_course_rv);

        paidCourse = view.findViewById(R.id.paid_courses_rv);

        return view;
    }

    private void initalizeVideos() {

        freeVideos = new ArrayList<>();

        trendingvVideos = new ArrayList<>();

        freeVideos.add(new FreeVideos());
        freeVideos.add(new FreeVideos());
        freeVideos.add(new FreeVideos());
        freeVideos.add(new FreeVideos());
        freeVideos.add(new FreeVideos());

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(linearLayoutManager1);

        recyclerView1.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL,false));

        freeCorse.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL,false));

        paidCourse.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL,false));

        TrendingVideosAdapter trendingVideosAdapter = new TrendingVideosAdapter(freeVideos, getApplicationContext());

        recyclerView.setAdapter(trendingVideosAdapter);
        recyclerView1.setAdapter(trendingVideosAdapter);

        initalizeVideo();
    }

    private void initalizeVideo() {

        freeVideos = new ArrayList<>();

        trendingvVideos = new ArrayList<>();

        progressDialog = new ProgressDialog(getContext());

        progressDialog.setMessage("Loading Free Videos Please Wait ... ");
        progressDialog.show();

        progressDialog.setCancelable(false);

        RequestQueue requestQueue1 = Volley.newRequestQueue(getContext());
        String url1 = "https://careeranna.com/api/getFreeVideos.php";
        Log.d("url_res", url1);
        StringRequest stringRequest1  = new StringRequest(Request.Method.GET, url1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("url_response", response);
                            JSONArray VideosArray = new JSONArray(response);
                            for(int i=0;i<VideosArray.length();i++) {
                                JSONObject videos = VideosArray.getJSONObject(i);
                                freeVideos.add(new FreeVideos(
                                        videos.getString("id"),
                                        videos.getString("video_url").replace("\\",""),
                                        "https://www.careeranna.com/thumbnail/" +videos.getString("thumbnail"),
                                        videos.getString("totalViews"),"",
                                        videos.getString("heading")));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        TrendingVideosAdapter trendingVideosAdapter = new TrendingVideosAdapter(freeVideos, getApplicationContext());

                        recyclerView1.setAdapter(trendingVideosAdapter);

                        progressDialog.dismiss();

                        progressDialog = new ProgressDialog(getContext());

                        progressDialog.setMessage("Loading Free Videos Please Wait ... ");
                        progressDialog.show();

                        progressDialog.setCancelable(false);

                        RequestQueue requestQueue1 = Volley.newRequestQueue(getContext());
                        String url1 = "https://careeranna.com/api/getTrendingVideos.php";
                        Log.d("url_res", url1);
                        StringRequest stringRequest1  = new StringRequest(Request.Method.GET, url1,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            Log.i("url_response", response);
                                            JSONArray VideosArray = new JSONArray(response);
                                            for(int i=0;i<VideosArray.length();i++) {
                                                JSONObject videos = VideosArray.getJSONObject(i);
                                                trendingvVideos.add(new FreeVideos(
                                                        videos.getString("id"),
                                                        videos.getString("video_url").replace("\\",""),
                                                        "https://www.careeranna.com/thumbnail/" +videos.getString("thumbnail"),
                                                        videos.getString("totalViews"),"",
                                                        videos.getString("heading")));
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        TrendingVideosAdapter trendingVideosAdapter = new TrendingVideosAdapter(trendingvVideos, getApplicationContext());

                                        recyclerView.setAdapter(trendingVideosAdapter);

                                        trendingVideosAdapter.setOnItemClicklistener(ExploreNew.this);

                                        progressDialog.dismiss();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        progressDialog.dismiss();
                                    }
                                });

                        requestQueue1.add(stringRequest1);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                    }
                });

        requestQueue1.add(stringRequest1);

    }


    @Override
    public void onItemClick1(int position) {

        startActivity(new Intent(getApplicationContext(), VideoWithComment.class).putExtra("course", trendingvVideos.get(position)));
    }
}
