package com.careeranna.careeranna.fragement.dashboard_fragements;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import com.careeranna.careeranna.activity.SignInActivity;
import com.careeranna.careeranna.adapter.ExamArrayAdapter;
import com.careeranna.careeranna.adapter.FreeCourseAdapter;
import com.careeranna.careeranna.data.Constants;
import com.careeranna.careeranna.data.Course;
import com.careeranna.careeranna.data.SubCategory;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;

import java.util.ArrayList;

import io.paperdb.Paper;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.facebook.FacebookSdk.getApplicationContext;

public class ExploreNew extends Fragment implements FreeCourseAdapter.OnItemClickListener, ExamArrayAdapter.OnItemClickListener {

    public static final String TAG = "ExploreNew";

    ArrayList<Course> paidCourses, freeCourses, tempPaidCourses, tempFreeCourses;

    int language;

    CardView loading_card, zero_search_results;

    SearchView searchTrends;

    FreeCourseAdapter paidCourseAdapter, freeCourseAdapter;

    RecyclerView freeCourseRVP, paidCoursesRVP;

    Button freeCourses_btn, premiumCourses_btn;

    public static int position = 0;

    CardView premiumCard, freeCard;

    Context context;

    RelativeLayout relativeLayout;

    RecyclerView chooseExam;

    ArrayList<SubCategory> subCategories;

    ExamArrayAdapter subCategoryArrayAdapter;

    public ExploreNew() {
        // Required empty public constructor
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private void initializeVariables(View view) {

        premiumCard = view.findViewById(R.id.premium_card);
        freeCard = view.findViewById(R.id.free_card);
        relativeLayout = view.findViewById(R.id.relative);

        loading_card = view.findViewById(R.id.loading_card);

        freeCourseRVP = view.findViewById(R.id.free_course_rv);
        paidCoursesRVP = view.findViewById(R.id.paid_courses_rv);

        chooseExam = view.findViewById(R.id.choose_exam_rv);

        searchTrends = view.findViewById(R.id.searchTrend);

        freeCourses_btn = view.findViewById(R.id.free);
        premiumCourses_btn = view.findViewById(R.id.premium);

        zero_search_results = view.findViewById(R.id.no_record_card);
    }

    @Override
    @NonNull
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragement_my_explore_new, container, false);

        this.context = inflater.getContext();


        if ( paidCourses == null && freeCourses == null ) {
            paidCourses = new ArrayList<>();
            freeCourses = new ArrayList<>();
            subCategories = new ArrayList<>();

            tempPaidCourses = new ArrayList<>();
            tempFreeCourses = new ArrayList<>();
        }
        Paper.init(context);

        initializeVariables(view);

        hidingRecycler();

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


        freeCourseRVP.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        paidCoursesRVP.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        chooseExam.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        paidCourseAdapter = new FreeCourseAdapter(paidCourses, context);
        paidCoursesRVP.setAdapter(paidCourseAdapter);

        freeCourseAdapter = new FreeCourseAdapter(freeCourses, context);
        freeCourseRVP.setAdapter(freeCourseAdapter);

        subCategoryArrayAdapter = new ExamArrayAdapter(subCategories, context);
        chooseExam.setAdapter(subCategoryArrayAdapter);

        paidCourseAdapter.setOnItemClicklistener(ExploreNew.this);
        freeCourseAdapter.setOnItemClicklistener(ExploreNew.this);
        subCategoryArrayAdapter.setOnItemClicklistener(ExploreNew.this);


        return view;
    }


    public void addFree(ArrayList<Course> paidCourses,
                        ArrayList<Course> freeCourses,
                        ArrayList<SubCategory> subCategories) {

        this.paidCourses.addAll(paidCourses);
        paidCourseAdapter.notifyDataSetChanged();
        this.freeCourses.addAll(freeCourses);
        freeCourseAdapter.notifyDataSetChanged();
        this.subCategories.addAll(subCategories);
        subCategoryArrayAdapter.notifyDataSetChanged();

        this.tempFreeCourses.addAll(freeCourses);
        this.tempPaidCourses.addAll(paidCourses);

        initializeVideos();

    }

    private void initializeVideos() {


        searchTrends.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {


                closeKeyboard();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                zero_search_results.setVisibility(View.GONE);

                if (premiumCard.getVisibility() == VISIBLE){

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

    private void hidingRecycler() {

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

        final int sdk = android.os.Build.VERSION.SDK_INT;

        premiumCourses_btn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        freeCourses_btn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        premiumCourses_btn.setTypeface(null, Typeface.NORMAL);
        freeCourses_btn.setTypeface(null, Typeface.NORMAL);

        freeCard.setVisibility(View.GONE);
        premiumCard.setVisibility(View.GONE);
        zero_search_results.setVisibility(View.GONE);
        searchTrends.setQuery("", false);
        searchTrends.clearFocus();

        if (i == 1) {
            freeCard.setVisibility(VISIBLE);
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                premiumCourses_btn.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.border_explore_non_active));
                freeCourses_btn.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.border_explore_active));
            } else {
                premiumCourses_btn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.border_explore_non_active));
                freeCourses_btn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.border_explore_active));
            }
            freeCourses_btn.setTextColor(getResources().getColor(R.color.colorPrimary));
            premiumCourses_btn.setTextColor(getResources().getColor(R.color.non_active_tab));
            chooseExam.setVisibility(GONE);
        } else if (i == 3) {
            premiumCard.setVisibility(View.VISIBLE);
            chooseExam.setVisibility(VISIBLE);
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                freeCourses_btn.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.border_explore_non_active));
                premiumCourses_btn.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.border_explore_active));
            } else {
                freeCourses_btn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.border_explore_non_active));
                premiumCourses_btn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.border_explore_active));
            }
            premiumCourses_btn.setTextColor(getResources().getColor(R.color.colorPrimary));
            freeCourses_btn.setTextColor(getResources().getColor(R.color.non_active_tab));
        }

    }

    public boolean isEmptyArrayList() {

        if ( this.freeCourses != null && this.paidCourses != null) {
            if (this.freeCourses.size() > 0 && this.paidCourses.size() > 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onCategorySelected(int position) {
        paidCourses.clear();

        SubCategory subCategory = subCategories.get(position);
        for (Course course : tempPaidCourses) {
            if (course.getCategory_id().equals(subCategory.getEXAM_NAME_ID())) {
                paidCourses.add(course);
            }
        }

        paidCourseAdapter.notifyDataSetChanged();
    }
}
