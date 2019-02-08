package com.careeranna.careeranna.fragement.dashboard_fragements;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.support.v7.widget.SearchView;
import android.widget.RelativeLayout;

import com.careeranna.careeranna.activity.MyCourses;
import com.careeranna.careeranna.activity.PurchaseCourseDetail;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.activity.VideoWithComment;
import com.careeranna.careeranna.adapter.FreeCourseAdapter;
import com.careeranna.careeranna.adapter.TrendingVideosAdapter;
import com.careeranna.careeranna.data.Constants;
import com.careeranna.careeranna.data.Course;
import com.careeranna.careeranna.data.FreeVideos;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;

import java.util.ArrayList;
import java.util.Date;

import io.paperdb.Paper;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.facebook.FacebookSdk.getApplicationContext;

public class ExploreNew extends Fragment implements TrendingVideosAdapter.OnItemClickListener, FreeCourseAdapter.OnItemClickListener {

    public static final String TAG = "ExploreNew";

    ArrayList<FreeVideos> freeVideos, trendingVideos, tempVideos, tempLatestVideos;

    ArrayList<Course> paidCourses, freeCourses, tempPaidCourses, tempFreeCourses;

    int language;

    CardView loading_card, zero_search_results;

    SearchView searchTrends;

    TrendingVideosAdapter trendingVideosAdapter;

    FreeCourseAdapter paidCourseAdapter, freeCourseAdapter;

    TrendingVideosAdapter freeVideosAdapter;

    RecyclerViewPager trendingVideosRVP, latestVideosRVP, freeCourseRVP, paidCoursesRVP;

    Button trending_btn, latest_btn, freeCourses_btn, premiumCourses_btn, filter_btn;

    Button cat, gk, upsc, reset;

    public static int position = 0, position_latest = 0;

    public static int position_free = 0, position_paid = 0;

    ImageView arrow_t_l, arrow_t_r, arrow_l_r, arrow_l_l;

    ImageView arrow_p_l, arrow_p_r, arrow_f_r, arrow_f_l;

    CardView trendingCard, premiumCard, freeCard, latestCard, filterCard;

    Context context;

    String exam_category;

    RelativeLayout relativeLayout;

    public ExploreNew() {
        // Required empty public constructor
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private void initializeVariables(View view) {

        trendingCard = view.findViewById(R.id.trending_card);
        premiumCard = view.findViewById(R.id.premium_card);
        freeCard = view.findViewById(R.id.free_card);
        latestCard = view.findViewById(R.id.latest_card);

        filter_btn = view.findViewById(R.id.filterTrend);

        relativeLayout = view.findViewById(R.id.relative);

        loading_card = view.findViewById(R.id.loading_card);

        trendingVideosRVP = view.findViewById(R.id.trending_rv);
        latestVideosRVP = view.findViewById(R.id.latest_rv);
        freeCourseRVP = view.findViewById(R.id.free_course_rv);
        paidCoursesRVP = view.findViewById(R.id.paid_courses_rv);
        filterCard = view.findViewById(R.id.filter_card);

        searchTrends = view.findViewById(R.id.searchTrend);

        exam_category = "";

        cat = view.findViewById(R.id.cat);
        gk = view.findViewById(R.id.gk);
        upsc = view.findViewById(R.id.upsc);

        trending_btn = view.findViewById(R.id.trending);
        latest_btn = view.findViewById(R.id.latest);
        freeCourses_btn = view.findViewById(R.id.free);
        premiumCourses_btn = view.findViewById(R.id.premium);

        arrow_t_l = view.findViewById(R.id.arrow_t_l);
        arrow_t_r = view.findViewById(R.id.arrow_t_r);

        arrow_l_l = view.findViewById(R.id.arrow_l_l);
        arrow_l_r = view.findViewById(R.id.arrow_l_r);

        arrow_f_l = view.findViewById(R.id.arrow_f_l);
        arrow_f_r = view.findViewById(R.id.arrow_f_r);

        arrow_p_l = view.findViewById(R.id.arrow_p_l);
        arrow_p_r = view.findViewById(R.id.arrow_p_r);

        reset = view.findViewById(R.id.reset);

        filterCard.setVisibility(GONE);

        zero_search_results = view.findViewById(R.id.no_record_card);
    }

    @Override
    @NonNull
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragement_my_explore_new, container, false);

        this.context = inflater.getContext();


        if (freeVideos == null && trendingVideos == null && paidCourses == null && freeCourses == null && tempVideos == null) {
            freeVideos = new ArrayList<>();
            trendingVideos = new ArrayList<>();
            paidCourses = new ArrayList<>();
            freeCourses = new ArrayList<>();

            tempVideos = new ArrayList<>();
            tempLatestVideos = new ArrayList<>();
            tempPaidCourses = new ArrayList<>();
            tempFreeCourses = new ArrayList<>();
        }
        Paper.init(context);

        initializeVariables(view);

        hidingRecycler();

        hideArrows();

        try {
            language = Paper.book().read(Constants.LANGUAGE);
            Log.d("in_try", language + " ");
            if (language != 1) {
                relativeLayout.setVisibility(GONE);
            }
        } catch (NullPointerException e) {
            language = 1;
            Log.d("in_catch", language + " ");
            Paper.book().write(Constants.LANGUAGE, language);
        }

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        trendingVideosRVP.setLayoutManager(linearLayoutManager1);

        latestVideosRVP.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        freeCourseRVP.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        paidCoursesRVP.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        trendingVideosAdapter = new TrendingVideosAdapter(trendingVideos, context);
        trendingVideosRVP.setAdapter(trendingVideosAdapter);

        freeVideosAdapter = new TrendingVideosAdapter(freeVideos, context);
        latestVideosRVP.setAdapter(freeVideosAdapter);

        paidCourseAdapter = new FreeCourseAdapter(paidCourses, context);
        paidCoursesRVP.setAdapter(paidCourseAdapter);

        freeCourseAdapter = new FreeCourseAdapter(freeCourses, context);
        freeCourseRVP.setAdapter(freeCourseAdapter);

        paidCourseAdapter.setOnItemClicklistener(ExploreNew.this);
        freeVideosAdapter.setOnItemClicklistener(ExploreNew.this);
        trendingVideosAdapter.setOnItemClicklistener(ExploreNew.this);
        freeCourseAdapter.setOnItemClicklistener(ExploreNew.this);

        filter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterCard.setVisibility(VISIBLE);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exam_category = "";
                filterCard.setVisibility(GONE);
                zero_search_results.setVisibility(GONE);

                cat.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                gk.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                upsc.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                cat.setTypeface(null, Typeface.NORMAL);
                gk.setTypeface(null, Typeface.NORMAL);
                upsc.setTypeface(null, Typeface.NORMAL);

                if (trendingCard.getVisibility() == VISIBLE) {
                    trendingVideos.clear();
                    trendingVideos.addAll(tempVideos);
                    trendingVideosAdapter.notifyDataSetChanged();
                } else if (latestCard.getVisibility() == VISIBLE) {
                    freeVideos.clear();
                    freeVideos.addAll(tempLatestVideos);
                    freeVideosAdapter.notifyDataSetChanged();
                }

            }
        });

        cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                zero_search_results.setVisibility(GONE);
                exam_category = "CAT";
                cat.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                gk.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                upsc.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                cat.setTypeface(null, Typeface.BOLD);
                gk.setTypeface(null, Typeface.NORMAL);
                upsc.setTypeface(null, Typeface.NORMAL);

                if (trendingCard.getVisibility() == VISIBLE) {
                    /*
                    trendingVideos is the array list present in trendingCard view.
                     */
                    trendingVideos.clear();

                    for (FreeVideos currentFreeVideo : tempVideos) {
                        if (currentFreeVideo.getTags().trim().toLowerCase().contains("CAT".toLowerCase())||
                                currentFreeVideo.getTags().trim().toLowerCase().contains("MBA".toLowerCase())) {
                            trendingVideos.add(currentFreeVideo);
                        }
                    }

                    trendingVideosAdapter.notifyDataSetChanged();


                    if (trendingVideos != null) {
                        if (trendingVideos.size() == 0) {
                            zero_search_results.setVisibility(View.VISIBLE);
                        } else {
                            trendingVideosRVP.scrollToPosition(0);
                        }
                    }
                } else if (latestCard.getVisibility() == VISIBLE) {
                    /*
                    freeVideos is the arraylist that is present in the latestCard view.
                     */
                    freeVideos.clear();

                    for (FreeVideos CurrentLatestVideo : tempLatestVideos) {
                        if (CurrentLatestVideo.getTags().trim().toLowerCase().contains("CAT".toLowerCase())) {
                            freeVideos.add(CurrentLatestVideo);
                        }
                    }

                    freeVideosAdapter.notifyDataSetChanged();

                    if (freeVideos != null) {
                        if (freeVideos.size() == 0) {
                            zero_search_results.setVisibility(View.VISIBLE);
                        } else {
                            latestVideosRVP.scrollToPosition(0);
                        }
                    }

                    filterCard.setVisibility(GONE);

                }
            }
        });

        upsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                zero_search_results.setVisibility(GONE);
                exam_category = "UPSC";
                upsc.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                gk.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                cat.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                upsc.setTypeface(null, Typeface.BOLD);
                gk.setTypeface(null, Typeface.NORMAL);
                cat.setTypeface(null, Typeface.NORMAL);

                if (trendingCard.getVisibility() == VISIBLE) {
                    /*
                    trendingVideos is the array list present in trendingCard view.
                     */
                    trendingVideos.clear();

                    for (FreeVideos currentFreeVideo : tempVideos) {
                        if (currentFreeVideo.getTags().trim().toLowerCase().contains("UPSC".toLowerCase())) {
                            trendingVideos.add(currentFreeVideo);
                        }
                    }

                    trendingVideosAdapter.notifyDataSetChanged();


                    if (trendingVideos != null) {
                        if (trendingVideos.size() == 0) {
                            zero_search_results.setVisibility(View.VISIBLE);
                        } else {
                            trendingVideosRVP.scrollToPosition(0);
                        }
                    }
                } else if (latestCard.getVisibility() == VISIBLE) {
                    /*
                    freeVideos is the arraylist that is present in the latestCard view.
                     */
                    freeVideos.clear();

                    for (FreeVideos CurrentLatestVideo : tempLatestVideos) {
                        if (CurrentLatestVideo.getTags().trim().toLowerCase().contains("UPSC".toLowerCase())) {
                            freeVideos.add(CurrentLatestVideo);
                        }
                    }

                    freeVideosAdapter.notifyDataSetChanged();

                    if (freeVideos != null) {
                        if (freeVideos.size() == 0) {
                            zero_search_results.setVisibility(View.VISIBLE);
                        } else {
                            latestVideosRVP.scrollToPosition(0);
                        }
                    }

                    filterCard.setVisibility(GONE);

                }

            }
        });

        gk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                zero_search_results.setVisibility(GONE);
                exam_category = "GK";
                gk.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                cat.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                upsc.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                gk.setTypeface(null, Typeface.BOLD);
                cat.setTypeface(null, Typeface.NORMAL);
                upsc.setTypeface(null, Typeface.NORMAL);

                if (trendingCard.getVisibility() == VISIBLE) {
                    /*
                    trendingVideos is the array list present in trendingCard view.
                     */
                    trendingVideos.clear();

                    for (FreeVideos currentFreeVideo : tempVideos) {
                        if (currentFreeVideo.getTags().trim().toLowerCase().contains("GK".toLowerCase())) {
                            trendingVideos.add(currentFreeVideo);
                        }
                    }

                    trendingVideosAdapter.notifyDataSetChanged();


                    if (trendingVideos != null) {
                        if (trendingVideos.size() == 0) {
                            zero_search_results.setVisibility(View.VISIBLE);
                        } else {
                            trendingVideosRVP.scrollToPosition(0);
                        }
                    }
                }else if (latestCard.getVisibility() == VISIBLE) {
                    /*
                    freeVideos is the arraylist that is present in the latestCard view.
                     */
                    freeVideos.clear();


                    for (FreeVideos CurrentLatestVideo : tempLatestVideos) {
                        if (CurrentLatestVideo.getTags().trim().toLowerCase().contains("GK".toLowerCase())) {
                            freeVideos.add(CurrentLatestVideo);
                        }
                    }

                    freeVideosAdapter.notifyDataSetChanged();

                    if (freeVideos != null) {
                        if (freeVideos.size() == 0) {
                            zero_search_results.setVisibility(View.VISIBLE);
                        } else {
                            latestVideosRVP.scrollToPosition(0);
                        }
                    }

                    filterCard.setVisibility(GONE);

                }
                filterCard.setVisibility(GONE);
            }
        });

        return view;
    }


    public void addFree(ArrayList<FreeVideos> freeVideos,
                        final ArrayList<FreeVideos> trendingVideos1,
                        ArrayList<Course> paidCourses,
                        ArrayList<Course> freeCourses) {

        Log.d(TAG, "addFree: Size of trendingVideos: "+trendingVideos1.size());

        this.trendingVideos.addAll(trendingVideos1);
        trendingVideosAdapter.notifyDataSetChanged();
        this.freeVideos.addAll(freeVideos);
        freeVideosAdapter.notifyDataSetChanged();
        this.paidCourses.addAll(paidCourses);
        paidCourseAdapter.notifyDataSetChanged();
        this.freeCourses.addAll(freeCourses);
        freeCourseAdapter.notifyDataSetChanged();

        this.tempVideos.addAll(trendingVideos1);
        this.tempLatestVideos.addAll(freeVideos);
        this.tempFreeCourses.addAll(freeCourses);
        this.tempPaidCourses.addAll(paidCourses);


        initializeVideos();

    }

    private void initializeVideos() {

        latestVideosRVP.setFlingFactor(0.1f);
        latestVideosRVP.fling(1, 1);
        latestVideosRVP.setVerticalFadingEdgeEnabled(true);

        latestVideosRVP.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            @NonNull
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
//                updateState(scrollState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
//                mPositionText.setText("First: " + trendingPager.getFirstVisiblePosition());
    /*            if (latestVideosRVP.getCurrentPosition() > 0) {
                    arrow_l_l.setVisibility(View.VISIBLE);
                } else if (latestVideosRVP.getCurrentPosition() + 2 == freeVideos.size()) {
                    arrow_l_r.setVisibility(View.VISIBLE);
                } else if (latestVideosRVP.getCurrentPosition() + 1 == freeVideos.size()) {
                    arrow_l_r.setVisibility(View.INVISIBLE);
                } else if (latestVideosRVP.getCurrentPosition() == 0) {
                    arrow_l_l.setVisibility(View.INVISIBLE);
                }
    */

                try {
                    int childCount = latestVideosRVP.getChildCount();
                    int width = latestVideosRVP.getChildAt(0).getWidth();
                    int padding = (latestVideosRVP.getWidth() - width) / 2;
//                mCountText.setText("Count: " + childCount);

                    for (int j = 0; j < childCount; j++) {
                        View v = recyclerView.getChildAt(j);
                        //往左 从 padding 到 -(v.getWidth()-padding) 的过程中，由大到小
                        float rate = 0;

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
                } catch (Exception e) {

                }
            }
        });

        freeCourseRVP.setFlingFactor(0.1f);
        freeCourseRVP.fling(1, 1);
        freeCourseRVP.setVerticalFadingEdgeEnabled(true);

        freeCourseRVP.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
//                updateState(scrollState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
//                mPositionText.setText("First: " + trendingPager.getFirstVisiblePosition());
      /*          if (freeCourseRVP.getCurrentPosition() > 0) {
                    arrow_f_l.setVisibility(View.VISIBLE);
                } else if (freeCourseRVP.getCurrentPosition() + 2 == freecourse.size()) {
                    arrow_f_r.setVisibility(View.VISIBLE);
                } else if (freeCourseRVP.getCurrentPosition() + 1 == freecourse.size()) {
                    arrow_f_r.setVisibility(View.INVISIBLE);
                } else if (freeCourseRVP.getCurrentPosition() == 0) {
                    arrow_f_l.setVisibility(View.INVISIBLE);
                }


      */
                try {
                    int childCount = freeCourseRVP.getChildCount();
                    int width = freeCourseRVP.getChildAt(0).getWidth();
                    int padding = (freeCourseRVP.getWidth() - width) / 2;
//                mCountText.setText("Count: " + childCount);

                    for (int j = 0; j < childCount; j++) {
                        View v = recyclerView.getChildAt(j);
                        //往左 从 padding 到 -(v.getWidth()-padding) 的过程中，由大到小
                        float rate = 0;
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
                } catch (Exception e) {
                }
            }
        });

        paidCoursesRVP.setFlingFactor(0.1f);
        paidCoursesRVP.fling(1, 1);
        paidCoursesRVP.setVerticalFadingEdgeEnabled(true);

        paidCoursesRVP.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
//                updateState(scrollState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
//                mPositionText.setText("First: " + trendingPager.getFirstVisiblePosition());
        /*        if (paidCoursesRVP.getCurrentPosition() > 0) {
                    arrow_p_l.setVisibility(View.VISIBLE);
                } else if (paidCoursesRVP.getCurrentPosition() + 2 == freeVideos.size()) {
                    arrow_p_r.setVisibility(View.VISIBLE);
                } else if (paidCoursesRVP.getCurrentPosition() + 1 == freeVideos.size()) {
                    arrow_p_r.setVisibility(View.INVISIBLE);
                } else if (paidCoursesRVP.getCurrentPosition() == 0) {
                    arrow_p_l.setVisibility(View.INVISIBLE);
                }


        */
                try {
                    int childCount = paidCoursesRVP.getChildCount();
                    int width = paidCoursesRVP.getChildAt(0).getWidth();
                    int padding = (paidCoursesRVP.getWidth() - width) / 2;
//                mCountText.setText("Count: " + childCount);

                    for (int j = 0; j < childCount; j++) {
                        View v = recyclerView.getChildAt(j);
                        //往左 从 padding 到 -(v.getWidth()-padding) 的过程中，由大到小
                        float rate = 0;

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
                } catch (Exception e) {

                }
            }
        });

        trendingVideosRVP.setFlingFactor(0.1f);
        trendingVideosRVP.fling(1, 1);
        trendingVideosRVP.setVerticalFadingEdgeEnabled(true);

        trendingVideosRVP.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
//                updateState(scrollState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
//                mPositionText.setText("First: " + trendingPager.getFirstVisiblePosition());
                if (trendingVideosRVP.getCurrentPosition() > 0) {
                    arrow_t_l.setVisibility(View.VISIBLE);
                } else if (trendingVideosRVP.getCurrentPosition() + 2 == trendingVideos.size()) {
                    arrow_t_r.setVisibility(View.VISIBLE);
                } else if (trendingVideosRVP.getCurrentPosition() + 1 == trendingVideos.size()) {
                    arrow_t_r.setVisibility(View.INVISIBLE);
                } else if (trendingVideosRVP.getCurrentPosition() == 0) {
                    arrow_t_l.setVisibility(View.INVISIBLE);
                }

                try {

                    int childCount = trendingVideosRVP.getChildCount();
                    int width = trendingVideosRVP.getChildAt(0).getWidth();
                    int padding = (trendingVideosRVP.getWidth() - width) / 2;
//                mCountText.setText("Count: " + childCount);
                    for (int j = 0; j < childCount; j++) {
                        View v = recyclerView.getChildAt(j);
                        //往左 从 padding 到 -(v.getWidth()-padding) 的过程中，由大到小
                        float rate = 0;

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
                } catch (Exception e) {

                }
            }
        });

        searchTrends.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {


                closeKeyboard();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                zero_search_results.setVisibility(View.GONE);

                if (trendingCard.getVisibility() == VISIBLE) {
                    /*
                    trendingVideos is the array list present in trendingCard view.
                     */
                    trendingVideos.clear();

                    if (newText.length() > 0) {

                        for (FreeVideos currentFreeVideo : tempVideos) {
                                if (currentFreeVideo.getTitle().trim().toLowerCase().contains(newText.trim())&&
                                        currentFreeVideo.getTags().trim().toLowerCase().contains(exam_category)) {
                                    trendingVideos.add(currentFreeVideo);
                                }
                        }

                        trendingVideosAdapter.notifyDataSetChanged();

                    } else {

                        trendingVideos.addAll(tempVideos);
                        trendingVideosAdapter.notifyDataSetChanged();
                        trendingVideosRVP.scrollToPosition(0);
                    }

                    if (trendingVideos != null) {
                        if (trendingVideos.size() == 0) {
                            zero_search_results.setVisibility(View.VISIBLE);
                        } else {
                            trendingVideosRVP.scrollToPosition(0);
                        }
                    }
                } else if (latestCard.getVisibility() == VISIBLE) {
                    /*
                    freeVideos is the arraylist that is present in the latestCard view.
                     */
                    freeVideos.clear();

                    if (newText.length() > 0) {

                        for (FreeVideos CurrentLatestVideo : tempLatestVideos) {
                            if (CurrentLatestVideo.getTitle().trim().toLowerCase().contains(newText.trim())) {
                                freeVideos.add(CurrentLatestVideo);
                            }
                        }

                        freeVideosAdapter.notifyDataSetChanged();

                    } else {

                        freeVideos.addAll(tempLatestVideos);
                        freeVideosAdapter.notifyDataSetChanged();
                    }

                    if (freeVideos != null) {
                        if (freeVideos.size() == 0) {
                            zero_search_results.setVisibility(View.VISIBLE);
                        } else {
                            latestVideosRVP.scrollToPosition(0);
                        }
                    }

                } else if (premiumCard.getVisibility() == VISIBLE){

                    paidCourses.clear();

                    if(newText.length() > 0) {
                        for(Course currentCourse : tempPaidCourses){
                            if(currentCourse.getName().trim().toLowerCase().contains(newText.trim())){
                                paidCourses.add(currentCourse);
                            }
                        }

                        paidCourseAdapter.notifyDataSetChanged();

                    } else {
                        paidCourses.addAll(tempPaidCourses);
                        paidCourseAdapter.notifyDataSetChanged();
                    }

                    if(paidCourses != null){
                        if(paidCourses.size() == 0){
                            zero_search_results.setVisibility(View.VISIBLE);
                        } else {
                            paidCoursesRVP.scrollToPosition(0);
                        }
                    }

                } else if (freeCard.getVisibility() == VISIBLE){
                    freeCourses.clear();

                    if(newText.length() > 0){
                        for(Course currentCourse: tempFreeCourses){
                            if(currentCourse.getName().trim().toLowerCase().contains(newText.trim())){
                                freeCourses.add(currentCourse);
                            }
                        }

                        freeCourseAdapter.notifyDataSetChanged();

                    } else {
                        freeCourses.addAll(tempFreeCourses);
                        freeCourseAdapter.notifyDataSetChanged();
                    }

                    if(freeCourses != null){
                        if(freeCourses.size() == 0){
                            zero_search_results.setVisibility(VISIBLE);
                        } else {
                            paidCoursesRVP.scrollToPosition(0);
                        }
                    }

                }

                return true;
            }
        });
    }

    @Override
    public void onItemClick1(String type, int position) {
        if (amIConnect()) {
            if (type.equals("Free")) {
                startActivity(new Intent(context, PurchaseCourseDetail.class).putExtra("Course", freeCourses.get(position)));
            }
            if (type.equals("Paid")) {
                startActivity(new Intent(context, PurchaseCourseDetail.class).putExtra("Course", paidCourses.get(position)));
            }
        } else {
            if (getActivity() != null) {
                ((MyCourses) getActivity()).changeInternet();
            }
        }
    }

    @Override
    public void onItemClick1(int position, String type) {
        if (amIConnect()) {
            if (type.equals("Trending")) {
                startActivity(new Intent(context, VideoWithComment.class).putExtra("videos", trendingVideos.get(position)));
            }
            if (type.equals("Latest")) {
                startActivity(new Intent(context, VideoWithComment.class).putExtra("videos", freeVideos.get(position)));
            }
        } else {
            if (getActivity() != null) {
                ((MyCourses) getActivity()).changeInternet();
            }
        }
    }


    private void closeKeyboard() {
        if (getActivity() != null) {
            View view = getActivity().getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    private boolean amIConnect() {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    private void hideArrows() {

        arrow_t_r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = trendingVideosRVP.getCurrentPosition();
                if (position == 0) {
                    arrow_t_l.setVisibility(View.VISIBLE);
                }
                position += 1;
                trendingVideosRVP.smoothScrollToPosition(position);
                if (position + 1 == trendingVideos.size()) {
                    arrow_t_r.setVisibility(View.INVISIBLE);
                }
            }
        });

        arrow_t_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = trendingVideosRVP.getCurrentPosition();
                if (position == 1) {
                    arrow_t_l.setVisibility(View.INVISIBLE);
                }
                position -= 1;
                trendingVideosRVP.smoothScrollToPosition(position);
            }
        });

        arrow_l_r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position_latest = latestVideosRVP.getCurrentPosition();
                if (position_latest == 0) {
                    arrow_l_l.setVisibility(View.VISIBLE);
                }
                position_latest += 1;
                latestVideosRVP.smoothScrollToPosition(position_latest);
                if (position_latest + 1 == freeVideos.size()) {
                    arrow_l_r.setVisibility(View.INVISIBLE);
                }
            }
        });

        arrow_l_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position_latest = latestVideosRVP.getCurrentPosition();
                if (position_latest == 1) {
                    arrow_l_l.setVisibility(View.INVISIBLE);
                }
                position_latest -= 1;
                latestVideosRVP.smoothScrollToPosition(position_latest);
            }
        });


        arrow_f_r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position_free = freeCourseRVP.getCurrentPosition();
                if (position_free == 0) {
                    arrow_f_l.setVisibility(View.VISIBLE);
                }
                position_free += 1;
                freeCourseRVP.smoothScrollToPosition(position_free);
            }
        });

        arrow_f_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position_free = freeCourseRVP.getCurrentPosition();
                if (position_free == 1) {
                    arrow_f_l.setVisibility(View.INVISIBLE);
                }
                position_free -= 1;
                freeCourseRVP.smoothScrollToPosition(position_free);
            }
        });

        arrow_p_r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position_paid = paidCoursesRVP.getCurrentPosition();
                if (position_paid == 0) {
                    arrow_p_l.setVisibility(View.VISIBLE);
                }
                position_paid += 1;
                paidCoursesRVP.smoothScrollToPosition(position_paid);
            }
        });

        arrow_p_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position_paid = paidCoursesRVP.getCurrentPosition();
                if (position_paid == 1) {
                    arrow_p_l.setVisibility(View.INVISIBLE);
                }
                position_paid -= 1;
                paidCoursesRVP.smoothScrollToPosition(position_paid);
            }
        });
    }

    private void hidingRecycler() {
        /*
        This function is for toggling between the fragments, and only showing the fragments
        of the selected item.
         */

        trending_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Show all items
                 */
                trendingVideos.addAll(tempVideos);
                trendingVideosAdapter.notifyDataSetChanged();
                changeButtonColorAndText(0);

            }
        });

        latest_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                freeVideos.addAll(tempLatestVideos);
                freeVideosAdapter.notifyDataSetChanged();
                changeButtonColorAndText(2);
            }
        });

        freeCourses_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                freeCourses.addAll(tempFreeCourses);
                freeCourseAdapter.notifyDataSetChanged();
                changeButtonColorAndText(1);
            }
        });

        premiumCourses_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paidCourses.addAll(tempPaidCourses);
                paidCourseAdapter.notifyDataSetChanged();
                changeButtonColorAndText(3);
            }
        });

        relativeLayout.setVisibility(VISIBLE);
    }


    private void changeButtonColorAndText(int i) {

        premiumCourses_btn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        latest_btn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        trending_btn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        freeCourses_btn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        premiumCourses_btn.setTypeface(null, Typeface.NORMAL);
        latest_btn.setTypeface(null, Typeface.NORMAL);
        freeCourses_btn.setTypeface(null, Typeface.NORMAL);
        trending_btn.setTypeface(null, Typeface.NORMAL);

        trendingCard.setVisibility(View.GONE);
        freeCard.setVisibility(View.GONE);
        latestCard.setVisibility(View.GONE);
        premiumCard.setVisibility(View.GONE);
        zero_search_results.setVisibility(View.GONE);
        searchTrends.setQuery("", false);
        searchTrends.clearFocus();

        if (i == 0) {
            trendingCard.setVisibility(View.VISIBLE);
            trending_btn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
            trending_btn.setTypeface(null, Typeface.BOLD);

        } else if (i == 1) {

            freeCard.setVisibility(View.VISIBLE);
            freeCourses_btn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
            freeCourses_btn.setTypeface(null, Typeface.BOLD);
        } else if (i == 2) {
            latestCard.setVisibility(View.VISIBLE);
            latest_btn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
            latest_btn.setTypeface(null, Typeface.BOLD);
        } else if (i == 3) {
            premiumCard.setVisibility(View.VISIBLE);
            premiumCourses_btn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
            premiumCourses_btn.setTypeface(null, Typeface.BOLD);
        }

    }

    public boolean isEmptyArrayList() {

        if (this.trendingVideos != null && this.freeCourses != null && this.paidCourses != null && this.freeVideos != null) {
            if (this.trendingVideos.size() > 0 && this.freeCourses.size() > 0 && this.paidCourses.size() > 0 && this.freeVideos.size() > 0) {
                return true;
            }
        }
        return false;
    }

}
