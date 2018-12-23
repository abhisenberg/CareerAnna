package com.careeranna.careeranna.fragement.user_myprofile_fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.careeranna.careeranna.R;
import com.careeranna.careeranna.adapter.UserFragmentsAdapters.UserCoursesAdapter;
import com.careeranna.careeranna.data.CourseWithLessData;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserCoursesFragment extends Fragment {

    public static final String TAG = "UserCoursesFragment";

    RecyclerView rv_user_courses;
    UserCoursesAdapter adapter;
    View zero_courses_display;

    public UserCoursesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_courses, container, false);

        adapter = new UserCoursesAdapter(getContext());
        zero_courses_display = view.findViewById(R.id.user_courses_zero_items);
        rv_user_courses = view.findViewById(R.id.user_courses);
        rv_user_courses.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rv_user_courses.setAdapter(adapter);

        zero_courses_display.setVisibility(View.INVISIBLE);
        return view;
    }

    public void setCourses(ArrayList<CourseWithLessData> courseList){
        Log.d(TAG, "setCourses: Courses loaded = "+courseList.size());
        if (courseList.size() == 0)
            zero_courses_display.setVisibility(View.VISIBLE);
        else
            zero_courses_display.setVisibility(View.INVISIBLE);

        adapter.updateData(courseList);
    }


}