package com.careeranna.careeranna.fragement.profile_fragements;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.careeranna.careeranna.ParticularCourse;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.data.Course;
import com.careeranna.careeranna.helper.RecyclerViewAdapter;
import com.careeranna.careeranna.helper.RecyclerViewCoursesAdapter;
import com.careeranna.careeranna.helper.RecyclerViewTopAdapter;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class TestFragment extends Fragment {

    ArrayList<Course> courses;

    RecyclerView recyclerViewCourses;

    RecyclerViewTopAdapter recyclerViewTopAdapter;

    public TestFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_test, container, false);


        return view;

    }

    private void initializeCourse() {

        String desc = "Organizations of all sizes and Industries, be it a financial institution or a small big data start up, everyone is using Python for their business.\n" +
                "Python is among the popular data science programming languages not only in Big data companies but also in the tech start up crowd. Around 46% of data scientists use Python.\n" +
                "Python has overtaken Java as the preferred programming language and is only second to SQL in usage today. \n" +
                "Python is finding Increased adoption in numerical computations, machine learning and several data science applications.\n" +
                "Python for data science requires data scientists to learn the usage of regular expressions, work with the scientific libraries and master the data visualization concepts.";

        courses = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL,false);
        recyclerViewCourses.setLayoutManager(linearLayoutManager);

        recyclerViewTopAdapter = new RecyclerViewTopAdapter(courses, getApplicationContext());
        recyclerViewCourses.setAdapter(recyclerViewTopAdapter);
    }
}
