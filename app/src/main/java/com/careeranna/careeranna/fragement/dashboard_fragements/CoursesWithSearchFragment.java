package com.careeranna.careeranna.fragement.dashboard_fragements;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.careeranna.careeranna.R;
import com.careeranna.careeranna.activity.MyCourses;
import com.careeranna.careeranna.activity.PurchaseCourseActivity;
import com.careeranna.careeranna.adapter.CoursesAdapter;
import com.careeranna.careeranna.data.Course;
import com.careeranna.careeranna.data.MyPaidCourse;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class CoursesWithSearchFragment extends Fragment implements CoursesAdapter.OnItemClickListener{

    ArrayList<Course> courses, tempCourses;
    RecyclerView recyclerView;
    ArrayList<MyPaidCourse> purchasedPaidCourses;

    CoursesAdapter coursesAdapter;
    private Context context;

    SearchView searchTrends;

    public CoursesWithSearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_courses_with_search, container, false);

        recyclerView =  view.findViewById(R.id.courses_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext(), LinearLayoutManager.VERTICAL, false));

        context = inflater.getContext();

        coursesAdapter = new CoursesAdapter(this.courses, context);
        recyclerView.setAdapter(coursesAdapter);
        searchTrends = view.findViewById(R.id.searchTrend);
        coursesAdapter.setOnItemClickListener(this);

        searchTrends.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                closeKeyboard();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                courses.clear();
                if(s.length() > 0) {
                    for(Course currentCourse: tempCourses){
                            if(currentCourse.getName().trim().toLowerCase().contains(s.trim().toLowerCase())){
                                courses.add(currentCourse);
                            }
                        }

                        coursesAdapter.notifyDataSetChanged();
                } else {
                    courses.addAll(tempCourses);
                    coursesAdapter.notifyDataSetChanged();
                }
                return true;
            }
        });

        return view;
    }

    public void addCourses(ArrayList<Course> courses, ArrayList<MyPaidCourse> purchasedPaidCourses) {

        this.courses = new ArrayList<>();
        this.tempCourses = new ArrayList<>();
        this.purchasedPaidCourses = new ArrayList<>();
        this.courses.addAll(courses);
        this.tempCourses.addAll(courses);
        this.purchasedPaidCourses.addAll(purchasedPaidCourses);
    }


    @Override
    public void onItemClick1(String type, int position) {
        if (amIConnect()) {
            Intent intent = new Intent(context, PurchaseCourseActivity.class);
            intent.putExtra("Course", courses.get(position));
            intent.putExtra("my_paid_course", purchasedPaidCourses);
            startActivity(intent);
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
}
