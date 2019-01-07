package com.careeranna.careeranna.fragement.explore_not_sign_in_fragements;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.careeranna.careeranna.activity.PurchaseCourseDetail;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.activity.VideoWithComment;
import com.careeranna.careeranna.adapter.FreeCourseAdapter;
import com.careeranna.careeranna.adapter.TrendingVideosAdapter;
import com.careeranna.careeranna.data.Course;
import com.careeranna.careeranna.data.FreeVideos;
import com.careeranna.careeranna.data.UrlConstants;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Fragment For Explore WithOut Sign Up
 */
public class InsideWithoutSignFragment extends Fragment  implements TrendingVideosAdapter.OnItemClickListener, FreeCourseAdapter.OnItemClickListener{

    ArrayList<FreeVideos> freeVideos, trendingvVideos;

    ArrayList<Course> courses, freecourse;

    Button trending_btn, latest_btn, free_btn, premium_btn;

    TrendingVideosAdapter trendingVideosAdapter;

    TrendingVideosAdapter freeVideosAdapter;

    RecyclerViewPager trending, latest_recycler, free_course_recyler, paid_course_recyler;

    public static int position = 0, position_latest = 0;

    public static int position_free = 0, position_paid = 0;

    ProgressDialog progressDialog;

    ImageView arrow_trending_left,arrow_trending_right,arrow_latest_right, arrow_latest_left;

    ImageView arrow_paid_left,arrow_paid_right,arrow_free_right, arrow_free_left;

    CardView trendingCard, premiumCard, freeCard, latestCard;

    public InsideWithoutSignFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_inside_without_sign_in, container, false);
        trending = view.findViewById(R.id.trending_rv);

        latest_recycler = view.findViewById(R.id.latest_rv);

        free_course_recyler = view.findViewById(R.id.free_course_rv);

        paid_course_recyler = view.findViewById(R.id.paid_courses_rv);
        
        arrow_trending_left = view.findViewById(R.id.arrow_t_l);
        arrow_trending_right = view.findViewById(R.id.arrow_t_r);

        arrow_latest_left = view.findViewById(R.id.arrow_l_l);
        arrow_latest_right = view.findViewById(R.id.arrow_l_r);

        arrow_free_left = view.findViewById(R.id.arrow_f_l);
        arrow_free_right = view.findViewById(R.id.arrow_f_r);

        arrow_paid_left = view.findViewById(R.id.arrow_p_l);
        arrow_paid_right = view.findViewById(R.id.arrow_p_r);

        trendingCard = view.findViewById(R.id.trending_card);
        premiumCard = view.findViewById(R.id.premium_card);
        freeCard = view.findViewById(R.id.free_card);
        latestCard = view.findViewById(R.id.latest_card);

        initalizeVideos();

        trending_btn = view.findViewById(R.id.trending);
        latest_btn = view.findViewById(R.id.latest);
        free_btn = view.findViewById(R.id.free);
        premium_btn = view.findViewById(R.id.premium);

        hidingRecylerView();

        arrow_trending_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = trending.getCurrentPosition();
                if(position == 0) {
                    arrow_trending_left.setVisibility(View.VISIBLE);
                }
                position += 1;
                trending.smoothScrollToPosition(position);
                if(position+1 == trendingvVideos.size()) {
                    arrow_trending_right.setVisibility(View.INVISIBLE);
                }
            }
        });

        arrow_trending_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = trending.getCurrentPosition();
                if(position == 1) {
                    arrow_trending_left.setVisibility(View.INVISIBLE);
                }
                position -= 1;
                trending.smoothScrollToPosition(position);
            }
        });

        arrow_latest_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position_latest = latest_recycler.getCurrentPosition();
                if(position_latest == 0) {
                    arrow_latest_left.setVisibility(View.VISIBLE);
                }
                position_latest += 1;
                latest_recycler.smoothScrollToPosition(position_latest);
                if(position_latest+1 == freeVideos.size()) {
                    arrow_latest_right.setVisibility(View.INVISIBLE);
                }
            }
        });

        arrow_latest_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position_latest = latest_recycler.getCurrentPosition();
                if(position_latest == 1) {
                    arrow_latest_left.setVisibility(View.INVISIBLE);
                }
                position_latest -= 1;
                latest_recycler.smoothScrollToPosition(position_latest);
            }
        });


        arrow_free_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position_free = free_course_recyler.getCurrentPosition();
                if(position_free == 0) {
                    arrow_free_left.setVisibility(View.VISIBLE);
                }
                position_free += 1;
                free_course_recyler.smoothScrollToPosition(position_free);
            }
        });

        arrow_free_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position_free = free_course_recyler.getCurrentPosition();
                if(position_free == 1) {
                    arrow_free_left.setVisibility(View.INVISIBLE);
                }
                position_free -= 1;
                free_course_recyler.smoothScrollToPosition(position_free);
            }
        });

        arrow_paid_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position_paid = paid_course_recyler.getCurrentPosition();
                if(position_paid == 0) {
                    arrow_paid_left.setVisibility(View.VISIBLE);
                }
                position_paid += 1;
                paid_course_recyler.smoothScrollToPosition(position_paid);
            }
        });

        arrow_paid_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position_paid = paid_course_recyler.getCurrentPosition();
                if(position_paid == 1) {
                    arrow_paid_left.setVisibility(View.INVISIBLE);
                }
                position_paid -= 1;
                paid_course_recyler.smoothScrollToPosition(position_paid);
            }
        });


        latest_recycler.setFlingFactor(0.1f);
        latest_recycler.fling(1, 1);
        latest_recycler.setVerticalFadingEdgeEnabled(true);

        latest_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
//                updateState(scrollState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
//                mPositionText.setText("First: " + trendingPager.getFirstVisiblePosition());
                int childCount = latest_recycler.getChildCount();
                int width = latest_recycler.getChildAt(0).getWidth();
                int padding = (latest_recycler.getWidth() - width) / 2;
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

        free_course_recyler.setFlingFactor(0.1f);
        free_course_recyler.fling(1, 1);
        free_course_recyler.setVerticalFadingEdgeEnabled(true);

        free_course_recyler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
//                updateState(scrollState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
//                mPositionText.setText("First: " + trendingPager.getFirstVisiblePosition());
                int childCount = free_course_recyler.getChildCount();
                int width = free_course_recyler.getChildAt(0).getWidth();
                int padding = (free_course_recyler.getWidth() - width) / 2;
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

        paid_course_recyler.setFlingFactor(0.1f);
        paid_course_recyler.fling(1, 1);
        paid_course_recyler.setVerticalFadingEdgeEnabled(true);

        paid_course_recyler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
//                updateState(scrollState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
//                mPositionText.setText("First: " + trendingPager.getFirstVisiblePosition());
                int childCount = paid_course_recyler.getChildCount();
                int width = paid_course_recyler.getChildAt(0).getWidth();
                int padding = (paid_course_recyler.getWidth() - width) / 2;
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

        return view;
    }


    private void addpaid_course_recyler() {

        courses = new ArrayList<>();

        progressDialog = new ProgressDialog(getContext());

        progressDialog.setMessage(getString(R.string.loading_premium_courses));
        progressDialog.show();

        progressDialog.setCancelable(false);

        RequestQueue requestQueue1 = Volley.newRequestQueue(getContext());
        StringRequest stringRequest1 = new StringRequest(Request.Method.GET,
                UrlConstants.FETCH_PREMIUM_COURSE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            courses = new ArrayList<>();

                            Log.i("url_response", response.toString());
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray CategoryArray = jsonObject.getJSONArray("paid");
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

                        paid_course_recyler.setAdapter(freeCourseAdapter);

                        freeCourseAdapter.setOnItemClicklistener(InsideWithoutSignFragment.this);

                        progressDialog.dismiss();

                        addFree();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        FreeCourseAdapter freeCourseAdapter = new FreeCourseAdapter(courses, getApplicationContext());

                        paid_course_recyler.setAdapter(freeCourseAdapter);

                        freeCourseAdapter.setOnItemClicklistener(InsideWithoutSignFragment.this);

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

        progressDialog.setMessage(getString(R.string.loading_free_courses));
        progressDialog.show();

        progressDialog.setCancelable(false);

        RequestQueue requestQueue1 = Volley.newRequestQueue(getContext());
        StringRequest stringRequest1 = new StringRequest(Request.Method.GET,
                UrlConstants.FETCH_FREE_COURSE,
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

                        free_course_recyler.setAdapter(freeCourseAdapter1);

                        freeCourseAdapter1.setOnItemClicklistener(InsideWithoutSignFragment.this);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                        FreeCourseAdapter freeCourseAdapter1 = new FreeCourseAdapter(freecourse, getApplicationContext());

                        free_course_recyler.setAdapter(freeCourseAdapter1);

                        freeCourseAdapter1.setOnItemClicklistener(InsideWithoutSignFragment.this);

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

        latest_recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL,false));

        free_course_recyler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL,false));

        paid_course_recyler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL,false));

        initalizeVideo();
    }

    private void initalizeVideo() {

        freeVideos = new ArrayList<>();

        trendingvVideos = new ArrayList<>();

        progressDialog = new ProgressDialog(getContext());

        progressDialog.setMessage(getString(R.string.loading_just_a_sec));
        progressDialog.show();

        progressDialog.setCancelable(false);

        RequestQueue requestQueue1 = Volley.newRequestQueue(getContext());
        StringRequest stringRequest1  = new StringRequest(Request.Method.GET,
                UrlConstants.FETCH_TRENDING_VIDEOS,
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

                        trendingVideosAdapter.setOnItemClicklistener(InsideWithoutSignFragment.this);

                        progressDialog.dismiss();

                        progressDialog = new ProgressDialog(getContext());

                        progressDialog.setMessage(getString(R.string.loading_just_a_sec));
                        progressDialog.show();

                        progressDialog.setCancelable(false);

                        RequestQueue requestQueue1 = Volley.newRequestQueue(getContext());
                        StringRequest stringRequest1  = new StringRequest(Request.Method.GET,
                                UrlConstants.FETCH_FREE_VIDEOS,
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

                                        latest_recycler.setAdapter(freeVideosAdapter);

                                        freeVideosAdapter.setOnItemClicklistener(InsideWithoutSignFragment.this);

                                        progressDialog.dismiss();

                                        addpaid_course_recyler();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                        addpaid_course_recyler();
                                        progressDialog.dismiss();
                                    }
                                });

                        requestQueue1.add(stringRequest1);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        addpaid_course_recyler();
                        progressDialog.dismiss();
                    }
                });

        requestQueue1.add(stringRequest1);

    }

    private void hidingRecylerView() {
        trending_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trending_btn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                trendingCard.setVisibility(View.VISIBLE);
                freeCard.setVisibility(View.GONE);
                latestCard.setVisibility(View.GONE);
                premiumCard.setVisibility(View.GONE);
                trending_btn.setTypeface(null, Typeface.BOLD);
                latest_btn.setTypeface(null, Typeface.NORMAL);
                free_btn.setTypeface(null, Typeface.NORMAL);
                premium_btn.setTypeface(null, Typeface.NORMAL);
                latest_btn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                free_btn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                premium_btn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        });


        latest_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                latestCard.setVisibility(View.VISIBLE);
                trendingCard.setVisibility(View.GONE);
                freeCard.setVisibility(View.GONE);
                premiumCard.setVisibility(View.GONE);
                latest_btn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                free_btn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                premium_btn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                trending_btn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                latest_btn.setTypeface(null, Typeface.BOLD);
                trending_btn.setTypeface(null, Typeface.NORMAL);
                free_btn.setTypeface(null, Typeface.NORMAL);
                premium_btn.setTypeface(null, Typeface.NORMAL);
            }
        });

        free_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                freeCard.setVisibility(View.VISIBLE);
                trendingCard.setVisibility(View.GONE);
                latestCard.setVisibility(View.GONE);
                premiumCard.setVisibility(View.GONE);
                free_btn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                latest_btn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                trending_btn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                premium_btn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                free_btn.setTypeface(null, Typeface.BOLD);
                latest_btn.setTypeface(null, Typeface.NORMAL);
                trending_btn.setTypeface(null, Typeface.NORMAL);
                premium_btn.setTypeface(null, Typeface.NORMAL);
            }
        });

        premium_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                premiumCard.setVisibility(View.VISIBLE);
                latestCard.setVisibility(View.GONE);
                freeCard.setVisibility(View.GONE);
                trendingCard.setVisibility(View.GONE);
                premium_btn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                latest_btn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                trending_btn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                free_btn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                premium_btn.setTypeface(null, Typeface.BOLD);
                latest_btn.setTypeface(null, Typeface.NORMAL);
                free_btn.setTypeface(null, Typeface.NORMAL);
                trending_btn.setTypeface(null, Typeface.NORMAL);
            }
        });
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
