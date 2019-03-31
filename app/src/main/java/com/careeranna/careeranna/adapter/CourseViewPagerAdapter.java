package com.careeranna.careeranna.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.careeranna.careeranna.R;
import com.careeranna.careeranna.data.Course;
import com.careeranna.careeranna.data.SubCategory;

import java.util.ArrayList;

public class CourseViewPagerAdapter extends PagerAdapter {

    private ArrayList<SubCategory> subCategories;
    private ArrayList<Course> courses;

    Context context;
    LayoutInflater inflater;

    public CourseViewPagerAdapter(ArrayList<SubCategory> subCategories, ArrayList<Course> courses, Context context) {
        this.subCategories = subCategories;
        this.courses = courses;
        this.context = context;
    }

    @Override
    public int getCount() {
        return subCategories.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (RelativeLayout) o;
    }
}
