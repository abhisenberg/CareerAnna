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
import com.careeranna.careeranna.InsideWithoutSignFragment;
import com.careeranna.careeranna.PurchaseCourseDetail;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.VideoWithComment;
import com.careeranna.careeranna.adapter.FreeCourseAdapter;
import com.careeranna.careeranna.adapter.TrendingVideosAdapter;
import com.careeranna.careeranna.data.Course;
import com.careeranna.careeranna.data.FreeVideos;
import com.careeranna.careeranna.user.ExploreNotSIActivity;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;

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

    RecyclerViewPager trending, recyclerView1, freeCorse, paidCourse;;

    public static int position = 0, position_latest = 0;

    public static int position_free = 0, position_paid = 0;

    ProgressDialog progressDialog;

    ImageView arrow_t_l,arrow_t_r,arrow_l_r, arrow_l_l;

    ImageView arrow_p_l,arrow_p_r,arrow_f_r, arrow_f_l;

    public ExploreNew() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.activity_my_explore_new, container, false);
        trending = view.findViewById(R.id.trending_rv);

        recyclerView1 = view.findViewById(R.id.latest_rv);

        freeCorse = view.findViewById(R.id.free_course_rv);

        paidCourse = view.findViewById(R.id.paid_courses_rv);


        arrow_t_l = view.findViewById(R.id.arrow_t_l);
        arrow_t_r = view.findViewById(R.id.arrow_t_r);

        arrow_l_l = view.findViewById(R.id.arrow_l_l);
        arrow_l_r = view.findViewById(R.id.arrow_l_r);

        arrow_f_l = view.findViewById(R.id.arrow_f_l);
        arrow_f_r = view.findViewById(R.id.arrow_f_r);

        arrow_p_l = view.findViewById(R.id.arrow_p_l);
        arrow_p_r = view.findViewById(R.id.arrow_p_r);

        initalizeVideos();

        arrow_t_r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position == 0) {
                    arrow_t_l.setVisibility(View.VISIBLE);
                }
                position += 1;
                trending.smoothScrollToPosition(position);
                if(position+1 == trendingvVideos.size()) {
                    arrow_t_r.setVisibility(View.INVISIBLE);
                }
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
                if(position_latest+1 == freeVideos.size()) {
                    arrow_l_r.setVisibility(View.INVISIBLE);
                }
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

        recyclerView1.setFlingFactor(0.1f);
        recyclerView1.fling(1, 1);
        recyclerView1.setVerticalFadingEdgeEnabled(true);

        recyclerView1.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
//                updateState(scrollState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
//                mPositionText.setText("First: " + trendingPager.getFirstVisiblePosition());
                int childCount = recyclerView1.getChildCount();
                int width = recyclerView1.getChildAt(0).getWidth();
                int padding = (recyclerView1.getWidth() - width) / 2;
//                mCountText.setText("Count: " + childCount);

                for (int j = 0; j < childCount; j++) {
                    View v = recyclerView.getChildAt(j);
                    //往左 从 padding 到 -(v.getWidth()-padding) 的过程中，由大到小
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
                        //往右 从 padding 到 recyclerView.getWidth()-padding 的过程中，由大到小
                        if (v.getLeft() <= recyclerView.getWidth() - padding) {
                            rate = (recyclerView.getWidth() - padding - v.getLeft()) * 1f / v.getWidth();
                        }
                        v.setScaleY(0.9f + rate * 0.1f);
                        v.setScaleX(0.9f + rate * 0.1f);
                    }
                }
            }
        });

        freeCorse.setFlingFactor(0.1f);
        freeCorse.fling(1, 1);
        freeCorse.setVerticalFadingEdgeEnabled(true);

        freeCorse.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
//                updateState(scrollState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
//                mPositionText.setText("First: " + trendingPager.getFirstVisiblePosition());
                int childCount = freeCorse.getChildCount();
                int width = freeCorse.getChildAt(0).getWidth();
                int padding = (freeCorse.getWidth() - width) / 2;
//                mCountText.setText("Count: " + childCount);

                for (int j = 0; j < childCount; j++) {
                    View v = recyclerView.getChildAt(j);
                    //往左 从 padding 到 -(v.getWidth()-padding) 的过程中，由大到小
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
                        //往右 从 padding 到 recyclerView.getWidth()-padding 的过程中，由大到小
                        if (v.getLeft() <= recyclerView.getWidth() - padding) {
                            rate = (recyclerView.getWidth() - padding - v.getLeft()) * 1f / v.getWidth();
                        }
                        v.setScaleY(0.9f + rate * 0.1f);
                        v.setScaleX(0.9f + rate * 0.1f);
                    }
                }
            }
        });

        paidCourse.setFlingFactor(0.1f);
        paidCourse.fling(1, 1);
        paidCourse.setVerticalFadingEdgeEnabled(true);

        paidCourse.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
//                updateState(scrollState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
//                mPositionText.setText("First: " + trendingPager.getFirstVisiblePosition());
                int childCount = paidCourse.getChildCount();
                int width = paidCourse.getChildAt(0).getWidth();
                int padding = (paidCourse.getWidth() - width) / 2;
//                mCountText.setText("Count: " + childCount);

                for (int j = 0; j < childCount; j++) {
                    View v = recyclerView.getChildAt(j);
                    //往左 从 padding 到 -(v.getWidth()-padding) 的过程中，由大到小
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
                        //往右 从 padding 到 recyclerView.getWidth()-padding 的过程中，由大到小
                        if (v.getLeft() <= recyclerView.getWidth() - padding) {
                            rate = (recyclerView.getWidth() - padding - v.getLeft()) * 1f / v.getWidth();
                        }
                        v.setScaleY(0.9f + rate * 0.1f);
                        v.setScaleX(0.9f + rate * 0.1f);
                    }
                }
            }
        });

        trending.setFlingFactor(0.1f);
        trending.fling(1, 1);
        trending.setVerticalFadingEdgeEnabled(true);

        trending.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
//                updateState(scrollState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
//                mPositionText.setText("First: " + trendingPager.getFirstVisiblePosition());
                int childCount = trending.getChildCount();
                int width = trending.getChildAt(0).getWidth();
                int padding = (trending.getWidth() - width) / 2;
//                mCountText.setText("Count: " + childCount);

                for (int j = 0; j < childCount; j++) {
                    View v = recyclerView.getChildAt(j);
                    //往左 从 padding 到 -(v.getWidth()-padding) 的过程中，由大到小
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
                        //往右 从 padding 到 recyclerView.getWidth()-padding 的过程中，由大到小
                        if (v.getLeft() <= recyclerView.getWidth() - padding) {
                            rate = (recyclerView.getWidth() - padding - v.getLeft()) * 1f / v.getWidth();
                        }
                        v.setScaleY(0.9f + rate * 0.1f);
                        v.setScaleX(0.9f + rate * 0.1f);
                    }
                }
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

        return view;
    }


    private void addPaidCourse() {

        courses = new ArrayList<>();

        progressDialog = new ProgressDialog(getContext());

        progressDialog.setMessage("Loading Paid Courses Please Wait ... ");
        progressDialog.show();

        progressDialog.setCancelable(false);

        RequestQueue requestQueue1 = Volley.newRequestQueue(getContext());
        String url1 = "https://careeranna.com/api/getAllCourse.php";
        Log.d("url_res", url1);
        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            courses = new ArrayList<>();

                            Log.i("url_response", response.toString());
                            JSONArray CategoryArray = new JSONArray(response.toString());
                            for (int i = 0; i < 20; i++) {
                                JSONObject Category = CategoryArray.getJSONObject(i);
                                courses.add(new Course(Category.getString("product_id"),
                                        Category.getString("course_name"),
                                        "https://www.careeranna.com/" + Category.getString("product_image").replace("\\", ""),
                                        Category.getString("exam_id"),
                                        Category.getString("discount")
                                        , "",
                                        Category.getString("video_url").replace("\\", "")));
                                courses.get(i).setType("Paid");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        FreeCourseAdapter freeCourseAdapter = new FreeCourseAdapter(courses, getApplicationContext());

                        paidCourse.setAdapter(freeCourseAdapter);

                        freeCourseAdapter.setOnItemClicklistener(ExploreNew.this);

                        progressDialog.dismiss();

                        addFree();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        FreeCourseAdapter freeCourseAdapter = new FreeCourseAdapter(courses, getApplicationContext());

                        paidCourse.setAdapter(freeCourseAdapter);

                        freeCourseAdapter.setOnItemClicklistener(ExploreNew.this);

                        progressDialog.dismiss();

                        addFree();
                    }
                }
        );

        requestQueue1.add(stringRequest1);
    }


    private void addFree() {

        freecourse = new ArrayList<>();

        progressDialog = new ProgressDialog(getContext());

        progressDialog.setMessage("Loading Free Courses Please Wait ... ");
        progressDialog.show();

        progressDialog.setCancelable(false);

        RequestQueue requestQueue1 = Volley.newRequestQueue(getContext());
        String url1 = "https://careeranna.com/api/getFreeCourse.php";
        Log.d("url_res", url1);
        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            Log.i("url_response", response.toString());
                            JSONArray CategoryArray = new JSONArray(response.toString());
                            for (int i = 0; i < CategoryArray.length(); i++) {
                                JSONObject Category = CategoryArray.getJSONObject(i);
                                freecourse.add(new Course(Category.getString("course_id"),
                                        Category.getString("name"),
                                        "https://www.careeranna.com/" + Category.getString("image").replace("\\", ""),
                                        Category.getString("eid"),
                                        "0"
                                        , "meta_description",""));
                                freecourse.get(i).setType("Free");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        progressDialog.dismiss();


                        FreeCourseAdapter freeCourseAdapter1 = new FreeCourseAdapter(freecourse, getApplicationContext());

                        freeCorse.setAdapter(freeCourseAdapter1);

                        freeCourseAdapter1.setOnItemClicklistener(ExploreNew.this);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                        FreeCourseAdapter freeCourseAdapter1 = new FreeCourseAdapter(freecourse, getApplicationContext());

                        freeCorse.setAdapter(freeCourseAdapter1);

                        freeCourseAdapter1.setOnItemClicklistener(ExploreNew.this);

                    }
                }
        );

        requestQueue1.add(stringRequest1);
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

        progressDialog.setMessage("Loading Trending Videos Please Wait ... ");
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
                                        "Free",
                                        videos.getString("duration")));
                                trendingvVideos.get(i).setType("Trending");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        trendingVideosAdapter = new TrendingVideosAdapter(trendingvVideos, getApplicationContext());

                        trending.setAdapter(trendingVideosAdapter);

                        trendingVideosAdapter.setOnItemClicklistener(ExploreNew.this);

                        progressDialog.dismiss();

                        progressDialog = new ProgressDialog(getContext());

                        progressDialog.setMessage("Loading Latest Videos Please Wait ... ");
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
                                                        "Trending",
                                                        videos.getString("duration")
                                                ));
                                                freeVideos.get(i).setType("Latest");
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        freeVideosAdapter = new TrendingVideosAdapter(freeVideos, getApplicationContext());

                                        recyclerView1.setAdapter(freeVideosAdapter);

                                        freeVideosAdapter.setOnItemClicklistener(ExploreNew.this);

                                        progressDialog.dismiss();

                                        addPaidCourse();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                        addPaidCourse();
                                        progressDialog.dismiss();
                                    }
                                });

                        requestQueue1.add(stringRequest1);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        addPaidCourse();
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
