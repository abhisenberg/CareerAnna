package com.careeranna.careeranna.fragement.dashboard_fragements;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.careeranna.careeranna.activity.MyCourses;
import com.careeranna.careeranna.activity.PurchaseCourseDetail;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.activity.PurchaseCourseDetail2;
import com.careeranna.careeranna.activity.VideoWithComment;
import com.careeranna.careeranna.adapter.FreeCourseAdapter;
import com.careeranna.careeranna.adapter.TrendingVideosAdapter;
import com.careeranna.careeranna.data.Course;
import com.careeranna.careeranna.data.FreeVideos;
import com.careeranna.careeranna.fragement.NoInternetFragement;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ExploreNew extends Fragment implements TrendingVideosAdapter.OnItemClickListener, FreeCourseAdapter.OnItemClickListener {

    ArrayList<FreeVideos> freeVideos, trendingvVideos;

    ArrayList<Course> courses, freecourse;

    CardView loading_card;

    RelativeLayout relativeLayout;

    TrendingVideosAdapter trendingVideosAdapter;

    FreeCourseAdapter paidCourseAdapter, freeCourseAdapter;

    TrendingVideosAdapter freeVideosAdapter;

    RecyclerViewPager trending, recyclerView1, freeCorse, paidCourse;

    Button trending_btn, latest_btn, free_btn, premium_btn;

    public static int position = 0, position_latest = 0;

    public static int position_free = 0, position_paid = 0;

    ProgressDialog progressDialog;

    ImageView arrow_t_l, arrow_t_r, arrow_l_r, arrow_l_l;

    ImageView arrow_p_l, arrow_p_r, arrow_f_r, arrow_f_l;

    CardView trendingCard, premiumCard, freeCard, latestCard;

    Context context;

    public ExploreNew() {
        // Required empty public constructor
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragement_my_explore_new, container, false);
        trending = view.findViewById(R.id.trending_rv);

        this.context = inflater.getContext();

        recyclerView1 = view.findViewById(R.id.latest_rv);

        trendingCard = view.findViewById(R.id.trending_card);
        premiumCard = view.findViewById(R.id.premium_card);
        freeCard = view.findViewById(R.id.free_card);
        latestCard = view.findViewById(R.id.latest_card);

        loading_card = view.findViewById(R.id.loading_card);

        freeCorse = view.findViewById(R.id.free_course_rv);

        paidCourse = view.findViewById(R.id.paid_courses_rv);

        trending_btn = view.findViewById(R.id.trending);
        latest_btn = view.findViewById(R.id.latest);
        free_btn = view.findViewById(R.id.free);
        premium_btn = view.findViewById(R.id.premium);

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

        arrow_t_l = view.findViewById(R.id.arrow_t_l);
        arrow_t_r = view.findViewById(R.id.arrow_t_r);

        arrow_l_l = view.findViewById(R.id.arrow_l_l);
        arrow_l_r = view.findViewById(R.id.arrow_l_r);

        arrow_f_l = view.findViewById(R.id.arrow_f_l);
        arrow_f_r = view.findViewById(R.id.arrow_f_r);

        arrow_p_l = view.findViewById(R.id.arrow_p_l);
        arrow_p_r = view.findViewById(R.id.arrow_p_r);

        initializevideos();

        arrow_t_r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = trending.getCurrentPosition();
                if (position == 0) {
                    arrow_t_l.setVisibility(View.VISIBLE);
                }
                position += 1;
                trending.smoothScrollToPosition(position);
                if (position + 1 == trendingvVideos.size()) {
                    arrow_t_r.setVisibility(View.INVISIBLE);
                }
            }
        });

        arrow_t_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = trending.getCurrentPosition();
                if (position == 1) {
                    arrow_t_l.setVisibility(View.INVISIBLE);
                }
                position -= 1;
                trending.smoothScrollToPosition(position);
            }
        });

        arrow_l_r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position_latest = recyclerView1.getCurrentPosition();
                if (position_latest == 0) {
                    arrow_l_l.setVisibility(View.VISIBLE);
                }
                position_latest += 1;
                recyclerView1.smoothScrollToPosition(position_latest);
                if (position_latest + 1 == freeVideos.size()) {
                    arrow_l_r.setVisibility(View.INVISIBLE);
                }
            }
        });

        arrow_l_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position_latest = recyclerView1.getCurrentPosition();
                if (position_latest == 1) {
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
                if (recyclerView1.getCurrentPosition() > 0) {
                    arrow_l_l.setVisibility(View.VISIBLE);
                } else if (recyclerView1.getCurrentPosition() + 2 == freeVideos.size()) {
                    arrow_l_r.setVisibility(View.VISIBLE);
                } else if (recyclerView1.getCurrentPosition() + 1 == freeVideos.size()) {
                    arrow_l_r.setVisibility(View.INVISIBLE);
                } else if (recyclerView1.getCurrentPosition() == 0) {
                    arrow_l_l.setVisibility(View.INVISIBLE);
                }
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
                if (freeCorse.getCurrentPosition() > 0) {
                    arrow_f_l.setVisibility(View.VISIBLE);
                } else if (freeCorse.getCurrentPosition() + 2 == freecourse.size()) {
                    arrow_f_r.setVisibility(View.VISIBLE);
                } else if (freeCorse.getCurrentPosition() + 1 == freecourse.size()) {
                    arrow_f_r.setVisibility(View.INVISIBLE);
                } else if (freeCorse.getCurrentPosition() == 0) {
                    arrow_f_l.setVisibility(View.INVISIBLE);
                }
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
                if (paidCourse.getCurrentPosition() > 0) {
                    arrow_p_l.setVisibility(View.VISIBLE);
                } else if (paidCourse.getCurrentPosition() + 2 == freeVideos.size()) {
                    arrow_p_r.setVisibility(View.VISIBLE);
                } else if (paidCourse.getCurrentPosition() + 1 == freeVideos.size()) {
                    arrow_p_r.setVisibility(View.INVISIBLE);
                } else if (paidCourse.getCurrentPosition() == 0) {
                    arrow_p_l.setVisibility(View.INVISIBLE);
                }
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
                if (trending.getCurrentPosition() > 0) {
                    arrow_t_l.setVisibility(View.VISIBLE);
                } else if (trending.getCurrentPosition() + 2 == trendingvVideos.size()) {
                    arrow_t_r.setVisibility(View.VISIBLE);
                } else if (trending.getCurrentPosition() + 1 == trendingvVideos.size()) {
                    arrow_t_r.setVisibility(View.INVISIBLE);
                } else if (trending.getCurrentPosition() == 0) {
                    arrow_t_l.setVisibility(View.INVISIBLE);
                }
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
                position_free = freeCorse.getCurrentPosition();
                if (position_free == 0) {
                    arrow_f_l.setVisibility(View.VISIBLE);
                }
                position_free += 1;
                freeCorse.smoothScrollToPosition(position_free);
            }
        });

        arrow_f_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position_free = freeCorse.getCurrentPosition();
                if (position_free == 1) {
                    arrow_f_l.setVisibility(View.INVISIBLE);
                }
                position_free -= 1;
                freeCorse.smoothScrollToPosition(position_free);
            }
        });

        arrow_p_r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position_paid = paidCourse.getCurrentPosition();
                if (position_paid == 0) {
                    arrow_p_l.setVisibility(View.VISIBLE);
                }
                position_paid += 1;
                paidCourse.smoothScrollToPosition(position_paid);
            }
        });

        arrow_p_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position_paid = paidCourse.getCurrentPosition();
                if (position_paid == 1) {
                    arrow_p_l.setVisibility(View.INVISIBLE);
                }
                position_paid -= 1;
                paidCourse.smoothScrollToPosition(position_paid);
            }
        });

        return view;
    }


    public void addFree(ArrayList<FreeVideos> freeVideos,
                        ArrayList<FreeVideos> trendingvVideos,
                        ArrayList<Course> courses,
                        ArrayList<Course> freecourse) {

        this.freeVideos = freeVideos;
        this.trendingvVideos = trendingvVideos;
        this.courses = courses;
        this.freecourse = freecourse;

        initializevideos();

        trendingVideosAdapter = new TrendingVideosAdapter(trendingvVideos, context);
        trending.setAdapter(trendingVideosAdapter);

        freeVideosAdapter = new TrendingVideosAdapter(freeVideos, context);
        recyclerView1.setAdapter(freeVideosAdapter);

        paidCourseAdapter = new FreeCourseAdapter(courses, context);
        paidCourse.setAdapter(paidCourseAdapter);

        freeCourseAdapter = new FreeCourseAdapter(freecourse, context);
        freeCorse.setAdapter(freeCourseAdapter);

        paidCourseAdapter.setOnItemClicklistener(ExploreNew.this);
        freeVideosAdapter.setOnItemClicklistener(ExploreNew.this);
        trendingVideosAdapter.setOnItemClicklistener(ExploreNew.this);
        freeCourseAdapter.setOnItemClicklistener(ExploreNew.this);

    }

    private void initializevideos() {

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        trending.setLayoutManager(linearLayoutManager1);

        recyclerView1.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        freeCorse.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        paidCourse.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

    }


    @Override
    public void onItemClick1(String type, int position) {
        if (amIConnect()) {
            if (type.equals("Free")) {
                startActivity(new Intent(context, PurchaseCourseDetail.class).putExtra("Course", freecourse.get(position)));
            }
            if (type.equals("Paid")) {
                startActivity(new Intent(context, PurchaseCourseDetail.class).putExtra("Course", courses.get(position)));
            }
        } else {
            ((MyCourses) getActivity()).changeInternet();
        }
    }

    @Override
    public void onItemClick1(int position, String type) {
        if (amIConnect()) {
            if (type.equals("Trending")) {
                startActivity(new Intent(context, VideoWithComment.class).putExtra("videos", trendingvVideos.get(position)));
            }
            if (type.equals("Latest")) {
                startActivity(new Intent(context, VideoWithComment.class).putExtra("videos", freeVideos.get(position)));
            }
        } else {
            ((MyCourses) getActivity()).changeInternet();
        }
    }

    private boolean amIConnect() {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }
}
