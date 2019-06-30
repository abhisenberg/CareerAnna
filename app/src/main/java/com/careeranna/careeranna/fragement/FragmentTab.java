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
import com.careeranna.careeranna.activity.PurchaseCourseDetail;
import com.careeranna.careeranna.adapter.FreeCourseAdapter;
import com.careeranna.careeranna.data.Course;
import com.careeranna.careeranna.data.MyPaidCourse;
import com.careeranna.careeranna.fragement.dashboard_fragements.PaidCoursesFragment;

import java.util.ArrayList;

public class FragmentTab extends Fragment implements FreeCourseAdapter.OnItemClickListener{

    RecyclerView mRecyclerView;

    private ArrayList<Course> courses;

    FreeCourseAdapter freeCourseAdapter;

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
        freeCourseAdapter = new FreeCourseAdapter(courses, inflater.getContext());
        freeCourseAdapter.setOnItemClicklistener(this);
        mRecyclerView.setAdapter(freeCourseAdapter);
        return view;
    }

    @Override
    public void onItemClick1(String type, int position) {


        if(myPaidCourses.size() > 0) {
            for (MyPaidCourse paidCourse: myPaidCourses) {
                if(paidCourse.getProduct_id().equalsIgnoreCase(courses.get(position).getId())) {
                    Toast.makeText(context, "You Have Already Purchased This Course", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
        startActivity(new Intent(context, PurchaseCourseDetail.class).putExtra("Course", courses.get(position)));

    }

}
