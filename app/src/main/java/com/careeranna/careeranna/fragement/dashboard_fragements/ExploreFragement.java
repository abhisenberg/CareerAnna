package com.careeranna.careeranna.fragement.dashboard_fragements;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.careeranna.careeranna.CategoriesSection;
import com.careeranna.careeranna.ExamPrepActivity;
import com.careeranna.careeranna.MyExamPrepActivity;
import com.careeranna.careeranna.PurchaseCourseDetail;
import com.careeranna.careeranna.PurchaseCourses;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.data.Category;
import com.careeranna.careeranna.data.Course;
import com.careeranna.careeranna.data.ExamPrep;
import com.careeranna.careeranna.helper.RecyclerViewAdapter;
import com.careeranna.careeranna.helper.RecyclerViewExamAdapter;
import com.careeranna.careeranna.helper.RecyclerViewTopAdapter;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ExploreFragement extends Fragment implements RecyclerViewTopAdapter.OnItemClickListener,
        RecyclerViewAdapter.OnItemClickListener,
        RecyclerViewExamAdapter.OnItemClickListener{

    ArrayList<Category> categories;
    ArrayList<Course> courses;
    ArrayList<ExamPrep> examPreps;

    RecyclerView recyclerViewCourses, recyclerViewCategory, recyclerViewExamp;

    RecyclerViewAdapter recyclerViewAdapter;
    RecyclerViewTopAdapter recyclerViewTopAdapter;
    RecyclerViewExamAdapter recyclerViewExamAdapter;

    private String[] imageUrls = new String[] {
            "https://4.bp.blogspot.com/-qf3t5bKLvUE/WfwT-s2IHmI/AAAAAAAABJE/RTy60uoIDCoVYzaRd4GtxCeXrj1zAwVAQCLcBGAs/s1600/Machine-Learning.png",
            "https://cdn-images-1.medium.com/max/2000/1*SSutxOFoBUaUmgeNWAPeBA.jpeg",
            "https://www.digitalvidya.com/wp-content/uploads/2016/02/Master_Digital_marketng-1170x630.jpg"
    };

    public ExploreFragement() {
        // Required empty public constructor
    }

    public void setCategories(ArrayList<Category> mcategories, ArrayList<Course> mCourses, ArrayList<ExamPrep> mExam) {

        categories = mcategories;
        courses = mCourses;
        examPreps = mExam;
        if(categories.size() > 0) {
            initializeCategory();
            initializeCourse();
            initializeExamprep();
        }
        if(examPreps.size() > 0) {
            initializeCategory();
            initializeCourse();
            initializeExamprep();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_explore, container, false);

        recyclerViewCategory = view.findViewById(R.id.categories);
        recyclerViewCourses = view.findViewById(R.id.top_coursesT);
        recyclerViewExamp = view.findViewById(R.id.exam_prep_rv);

        initializeCategory();
        initializeCourse();
        initializeExamprep();

        return view;
    }

    private void initializeCategory() {

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL,false);
        recyclerViewCategory.setLayoutManager(linearLayoutManager1);

        recyclerViewAdapter = new RecyclerViewAdapter(categories, getApplicationContext());
        recyclerViewCategory.setAdapter(recyclerViewAdapter);

        recyclerViewAdapter.setOnItemClicklistener(this);

    }

    private void initializeCourse() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL,false);
        recyclerViewCourses.setLayoutManager(linearLayoutManager);

        recyclerViewTopAdapter = new RecyclerViewTopAdapter(courses, getApplicationContext());
        recyclerViewCourses.setAdapter(recyclerViewTopAdapter);

        recyclerViewTopAdapter.setOnItemClicklistener(this);
    }

    private void initializeExamprep() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL,false);
        recyclerViewExamp.setLayoutManager(linearLayoutManager);

        recyclerViewExamAdapter = new RecyclerViewExamAdapter(examPreps, getApplicationContext());
        recyclerViewExamp.setAdapter(recyclerViewExamAdapter);

        recyclerViewExamAdapter.setOnItemClicklistener(this);
    }

    @Override
    public void onItemClick(int position) {

        Intent intent = new Intent(getApplicationContext(), PurchaseCourseDetail.class);
        intent.putExtra("Course", courses.get(position));
        getContext().startActivity(intent);

    }

    @Override
    public void onItemClick1(int position) {

        Intent intent = new Intent(getApplicationContext(), CategoriesSection.class);
        intent.putExtra("Category", categories.get(position));
        getContext().startActivity(intent);

    }

    @Override
    public void onItemClickExamp(int position) {

        Intent intent = new Intent(getApplicationContext(), PurchaseCourseDetail.class);
        intent.putExtra("Examp", examPreps.get(position));
        getContext().startActivity(intent);
    }
}
