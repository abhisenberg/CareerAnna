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

    private String[] imageUrls = new String[] {
            "https://4.bp.blogspot.com/-qf3t5bKLvUE/WfwT-s2IHmI/AAAAAAAABJE/RTy60uoIDCoVYzaRd4GtxCeXrj1zAwVAQCLcBGAs/s1600/Machine-Learning.png",
            "https://cdn-images-1.medium.com/max/2000/1*SSutxOFoBUaUmgeNWAPeBA.jpeg",
            "https://www.digitalvidya.com/wp-content/uploads/2016/02/Master_Digital_marketng-1170x630.jpg"
    };

    public TestFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_test, container, false);

        initializeCourse();

        return view;

    }

    private void initializeCourse() {

        String desc = "Organizations of all sizes and Industries, be it a financial institution or a small big data start up, everyone is using Python for their business.\n" +
                "Python is among the popular data science programming languages not only in Big data companies but also in the tech start up crowd. Around 46% of data scientists use Python.\n" +
                "Python has overtaken Java as the preferred programming language and is only second to SQL in usage today. \n" +
                "Python is finding Increased adoption in numerical computations, machine learning and several data science applications.\n" +
                "Python for data science requires data scientists to learn the usage of regular expressions, work with the scientific libraries and master the data visualization concepts.";

        courses = new ArrayList<>();
        courses.add(new Course("1",  "Machine Learning", imageUrls[0], "1", "6999",
                desc, "android.resource://com.careeranna.careeranna/"+R.raw.video));
        courses.add(new Course("2",  "Python", imageUrls[1], "2", "4999",
                desc, "android.resource://com.careeranna.careeranna/"+R.raw.video));
        courses.add(new Course("3",  "Marketing", imageUrls[2], "3", "5999",
                desc, "android.resource://com.careeranna.careeranna/"+R.raw.video));
        courses.add(new Course("4",  "Machine Learning", imageUrls[0], "4", "6999",
                desc, "android.resource://com.careeranna.careeranna/"+R.raw.video));
        courses.add(new Course("5",  "Python", imageUrls[1], "5", "3999",
                desc, "android.resource://com.careeranna.careeranna/"+R.raw.video));
        courses.add(new Course("6",  "Marketing", imageUrls[2], "1", "7999",
                desc, "android.resource://com.careeranna.careeranna/"+R.raw.video));
        courses.add(new Course("7",  "Machine Learning", imageUrls[0], "2", "8999",
                desc, "android.resource://com.careeranna.careeranna/"+R.raw.video));
        courses.add(new Course("8",  "Python", imageUrls[1], "2", "3999",
                desc, "android.resource://com.careeranna.careeranna/"+R.raw.video));
        courses.add(new Course("8",  "Marketing", imageUrls[2], "1", "4999",
                desc, "android.resource://com.careeranna.careeranna/"+R.raw.video));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL,false);
        recyclerViewCourses.setLayoutManager(linearLayoutManager);

        recyclerViewTopAdapter = new RecyclerViewTopAdapter(courses, getApplicationContext());
        recyclerViewCourses.setAdapter(recyclerViewTopAdapter);
    }
}
