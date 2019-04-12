package com.careeranna.careeranna.fragement.explore_not_sign_in_fragements;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.careeranna.careeranna.activity.PurchaseCourseActivity;
import com.careeranna.careeranna.activity.PurchaseCourseDetail;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.adapter.CoursesAdapter;
import com.careeranna.careeranna.data.Course;
import com.careeranna.careeranna.data.FreeCourse;
import com.careeranna.careeranna.data.PaidCourse;
import com.careeranna.careeranna.helper.NewApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.facebook.FacebookSdk.getApplicationContext;

public class InsideWithoutSignFragment extends Fragment implements CoursesAdapter.OnItemClickListener {


    ArrayList<Course> paidCoursesList, freeCoursesList;

    Button free_courses_btn, premium_courses_btn;

    RecyclerView free_courses_rv, paid_courses_rv;

    ProgressDialog progressDialog;

    RelativeLayout relativeLayout;

    CardView premiumCoursesCard, freeCoursesCard;

    public InsideWithoutSignFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inside_without_sign_in, container, false);

        free_courses_rv = view.findViewById(R.id.free_course_rv);

        paid_courses_rv = view.findViewById(R.id.paid_courses_rv);

        premiumCoursesCard = view.findViewById(R.id.premium_card);
        freeCoursesCard = view.findViewById(R.id.free_card);

        relativeLayout = view.findViewById(R.id.relative_loading);

        free_courses_btn = view.findViewById(R.id.free);
        premium_courses_btn = view.findViewById(R.id.premium);

        hidingFreeAndPremiumRecycler();

        initializeVideos();

        return view;
    }

    private void initializeVideos() {

        free_courses_rv.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        paid_courses_rv.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        populatePaidCourses();
    }


    private void populatePaidCourses() {

        progressDialog = new ProgressDialog(getContext());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NewApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NewApi api = retrofit.create(NewApi.class);

        Call<List<PaidCourse>> call = api.getPaidCourses();

        call.enqueue(new Callback<List<PaidCourse>>() {
            @Override
            public void onResponse(Call<List<PaidCourse>> call, retrofit2.Response<List<PaidCourse>> response) {

                paidCoursesList = new ArrayList<>();

                List<PaidCourse> paidCoursesList = response.body();

                for(PaidCourse  paidCourse: paidCoursesList) {
                    InsideWithoutSignFragment.this.paidCoursesList.add(new Course(paidCourse.getProduct_id(),
                            paidCourse.getCourse_name(),
                            "https://www.careeranna.com/" + paidCourse.getProduct_image().replace("\\", ""),
                            "free",
                            paidCourse.getDiscount(),
                            "",
                            "",
                            "Paid",
                            paidCourse.getPrice(),
                            paidCourse.getAverage_rating(),
                            paidCourse.getTotal_rating(),
                            paidCourse.getLearners_count()));
                }

                CoursesAdapter coursesAdapter = new CoursesAdapter(InsideWithoutSignFragment.this.paidCoursesList, getApplicationContext());

                paid_courses_rv.setAdapter(coursesAdapter);

                coursesAdapter.setOnItemClickListener(InsideWithoutSignFragment.this);

                relativeLayout.setVisibility(View.GONE);

                populateFreeCourses();
            }

            @Override
            public void onFailure(Call<List<PaidCourse>> call, Throwable t) {
                CoursesAdapter coursesAdapter = new CoursesAdapter(paidCoursesList, getApplicationContext());

                paid_courses_rv.setAdapter(coursesAdapter);

                coursesAdapter.setOnItemClickListener(InsideWithoutSignFragment.this);

                relativeLayout.setVisibility(View.GONE);

                populateFreeCourses();
            }
        });


    }

    public void populateFreeCourses() {

        freeCoursesList = new ArrayList<>();

        relativeLayout.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NewApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NewApi api = retrofit.create(NewApi.class);

        Call<List<FreeCourse>> call = api.getFreeCourses();

        call.enqueue(new Callback<List<FreeCourse>>() {
            @Override
            public void onResponse(Call<List<FreeCourse>> call, retrofit2.Response<List<FreeCourse>> response) {
                List<FreeCourse> coursesList = response.body();

                for(FreeCourse course: coursesList) {
                    freeCoursesList.add(new Course(course.getCourse_id(),
                            course.getName(),
                            "https://www.careeranna.com/" + course.getImage().replace("\\", ""),
                            "paid",
                            "0"
                            , "",
                            "",
                            "Free",
                            "0",
                            course.getAverage_rating(),
                            course.getTotal_rating(),
                            course.getLearners_count()));
                }

                relativeLayout.setVisibility(View.GONE);

                CoursesAdapter coursesAdapter = new CoursesAdapter(freeCoursesList, getApplicationContext());

                free_courses_rv.setAdapter(coursesAdapter);

                coursesAdapter.setOnItemClickListener(InsideWithoutSignFragment.this);

            }

            @Override
            public void onFailure(Call<List<FreeCourse>> call, Throwable t) {

                relativeLayout.setVisibility(View.GONE);

                CoursesAdapter coursesAdapter = new CoursesAdapter(freeCoursesList, getApplicationContext());

                free_courses_rv.setAdapter(coursesAdapter);

                coursesAdapter.setOnItemClickListener(InsideWithoutSignFragment.this);

            }
        });

    }


    private void hidingFreeAndPremiumRecycler() {

        free_courses_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                freeCoursesCard.setVisibility(View.VISIBLE);
                premiumCoursesCard.setVisibility(View.GONE);
                premium_courses_btn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.border_explore_non_active));
                free_courses_btn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.border_explore_active));
                free_courses_btn.setTextColor(getResources().getColor(R.color.colorPrimary));
                premium_courses_btn.setTextColor(getResources().getColor(R.color.non_active_tab));
            }
        });

        premium_courses_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                premiumCoursesCard.setVisibility(View.VISIBLE);
                freeCoursesCard.setVisibility(View.GONE);
                free_courses_btn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.border_explore_non_active));
                premium_courses_btn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.border_explore_active));
                premium_courses_btn.setTextColor(getResources().getColor(R.color.colorPrimary));
                free_courses_btn.setTextColor(getResources().getColor(R.color.non_active_tab));
            }
        });
    }

    @Override
    public void onItemClick1(String type, int position) {

        if (type.equals("Free")) {
            startActivity(new Intent(getApplicationContext(), PurchaseCourseActivity.class).putExtra("Course", freeCoursesList.get(position)));
        }
        if (type.equals("Paid")) {
            startActivity(new Intent(getApplicationContext(), PurchaseCourseActivity.class).putExtra("Course", paidCoursesList.get(position)));
        }
    }

}
