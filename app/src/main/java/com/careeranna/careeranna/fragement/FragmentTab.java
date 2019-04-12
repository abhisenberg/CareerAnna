package com.careeranna.careeranna.fragement;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.careeranna.careeranna.R;
import com.careeranna.careeranna.activity.PurchaseCourseActivity;
import com.careeranna.careeranna.activity.PurchaseCourseDetail;
import com.careeranna.careeranna.adapter.CoursesAdapter;
import com.careeranna.careeranna.data.Course;
import com.careeranna.careeranna.data.MyPaidCourse;

import java.util.ArrayList;

public class FragmentTab extends Fragment implements CoursesAdapter.OnItemClickListener{

    RecyclerView mRecyclerView;

    private ArrayList<Course> courses;

    CoursesAdapter coursesAdapter;

    android.app.AlertDialog.Builder builder;

    Context context;

    ArrayList<MyPaidCourse> myPaidCourses;

    android.app.AlertDialog alert;

    public FragmentTab() {
        // Required empty public constructor
    }

    public void addList(ArrayList<Course> courses, ArrayList<MyPaidCourse> myPaidCourses) {

        this.courses = new ArrayList<>();
        this.courses.addAll(courses);

        this.myPaidCourses = new ArrayList<>();
        this.myPaidCourses.addAll(myPaidCourses);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        context = inflater.getContext();

        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        coursesAdapter = new CoursesAdapter(courses, inflater.getContext());
        coursesAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(coursesAdapter);
        return view;
    }

    @Override
    public void onItemClick1(String type, int position) {

        Intent intent = new Intent(context, PurchaseCourseActivity.class);
        intent.putExtra("Course", courses.get(position));
        intent.putExtra("my_paid_course", myPaidCourses);
        startActivity(intent);

    }

}
