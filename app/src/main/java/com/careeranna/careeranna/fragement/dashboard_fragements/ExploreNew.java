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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.careeranna.careeranna.PurchaseCourseDetail;
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

    TrendingVideosAdapter trendingVideosAdapter;

    TrendingVideosAdapter freeVideosAdapter;

    RecyclerView trending, recyclerView1, freeCorse, paidCourse;

    public static int position = 0, position_latest = 0;

    public static int position_free = 0, position_paid = 0;

    ProgressDialog progressDialog;

    ImageView arrow_t_l,arrow_t_r,arrow_l_r, arrow_l_l;

    ImageView arrow_p_l,arrow_p_r,arrow_f_r, arrow_f_l;

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

        freeCourseAdapter1.setOnItemClicklistener(this);

        initalizeVideos();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_my_explore_new, container, false);

        trending = view.findViewById(R.id.trending_rv);

        recyclerView1 = view.findViewById(R.id.latest_rv);

        arrow_t_l = view.findViewById(R.id.arrow_t_l);
        arrow_t_r = view.findViewById(R.id.arrow_t_r);

        arrow_l_l = view.findViewById(R.id.arrow_l_l);
        arrow_l_r = view.findViewById(R.id.arrow_l_r);

        arrow_f_l = view.findViewById(R.id.arrow_f_l);
        arrow_f_r = view.findViewById(R.id.arrow_f_r);

        arrow_p_l = view.findViewById(R.id.arrow_p_l);
        arrow_p_r = view.findViewById(R.id.arrow_p_r);

        arrow_t_r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position == 0) {
                    arrow_t_l.setVisibility(View.VISIBLE);
                }
                position += 1;
                trending.smoothScrollToPosition(position);
            }
        });

        arrow_t_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position == 1) {
                    arrow_t_l.setVisibility(View.INVISIBLE);
                }
                position -= 1;
                trending.smoothScrollToPosition(position);
            }
        });

        arrow_l_r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position_latest == 0) {
                    arrow_l_l.setVisibility(View.VISIBLE);
                }
                position_latest += 1;
                recyclerView1.smoothScrollToPosition(position_latest);
            }
        });

        arrow_l_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position_latest == 1) {
                    arrow_l_l.setVisibility(View.INVISIBLE);
                }
                position_latest -= 1;
                recyclerView1.smoothScrollToPosition(position_latest);
            }
        });


        arrow_f_r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position_free == 0) {
                    arrow_f_l.setVisibility(View.VISIBLE);
                }
                position_free += 1;
                freeCorse.smoothScrollToPosition(position_free);
            }
        });

        arrow_f_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position_free == 1) {
                    arrow_f_l.setVisibility(View.INVISIBLE);
                }
                position_free -= 1;
                freeCorse.smoothScrollToPosition(position_free);
            }
        });

        arrow_p_r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position_paid == 0) {
                    arrow_p_l.setVisibility(View.VISIBLE);
                }
                position_paid += 1;
                paidCourse.smoothScrollToPosition(position_paid);
            }
        });

        arrow_p_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position_paid == 1) {
                    arrow_p_l.setVisibility(View.INVISIBLE);
                }
                position_paid -= 1;
                paidCourse.smoothScrollToPosition(position_paid);
            }
        });

        freeCorse = view.findViewById(R.id.free_course_rv);

        paidCourse = view.findViewById(R.id.paid_courses_rv);

        return view;
    }

    private void initalizeVideos() {

        freeVideos = new ArrayList<>();

        trendingvVideos = new ArrayList<>();

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL,false);
        trending.setLayoutManager(linearLayoutManager1);

        recyclerView1.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL,false));

        freeCorse.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL,false));

        paidCourse.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL,false));

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
                                        videos.getString("totalViews"),
                                        videos.getString("tags"),
                                        videos.getString("heading"),
                                        "Free",
                                        videos.getString("duration")));
                                freeVideos.get(i).setType("Latest");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        freeVideosAdapter = new TrendingVideosAdapter(freeVideos, getApplicationContext());

                        recyclerView1.setAdapter(freeVideosAdapter);

                        freeVideosAdapter.setOnItemClicklistener(ExploreNew.this);

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
                                                        videos.getString("totalViews"),
                                                        videos.getString("tags"),
                                                        videos.getString("heading"),
                                                        "Trending",
                                                        videos.getString("duration")
                                                ));
                                                trendingvVideos.get(i).setType("Trending");
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        trendingVideosAdapter = new TrendingVideosAdapter(trendingvVideos, getApplicationContext());

                                        trending.setAdapter(trendingVideosAdapter);

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
    public void onItemClick1(String type, int position) {

        if(type.equals("Free")) {
            startActivity(new Intent(getApplicationContext(), PurchaseCourseDetail.class).putExtra("Course", freecourse.get(position)));
        }
        if(type.equals("Paid")) {
            startActivity(new Intent(getApplicationContext(), PurchaseCourseDetail.class).putExtra("Course", courses.get(position)));
        }
    }

    @Override
    public void onItemClick1(int position, String type) {
        if(type.equals("Trending")) {
            startActivity(new Intent(getApplicationContext(), VideoWithComment.class).putExtra("videos", trendingvVideos.get(position)));
        }
        if(type.equals("Latest")) {
            startActivity(new Intent(getApplicationContext(), VideoWithComment.class).putExtra("videos", freeVideos.get(position)));
        }
    }
}
