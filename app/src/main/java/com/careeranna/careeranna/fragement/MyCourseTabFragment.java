package com.careeranna.careeranna.fragement;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.careeranna.careeranna.Exams;
import com.careeranna.careeranna.ParticularCourse;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.adapter.MyCoursesAdapterNew;
import com.careeranna.careeranna.data.CourseWithLessData;
import com.careeranna.careeranna.fragement.dashboard_fragements.MyCoursesFragment;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MyCourseTabFragment extends Fragment implements MyCoursesAdapterNew.OnItemClickListener
        , RecyclerViewPager.OnPageChangedListener{

    public static final String TAG = "MyCourseFragment";
    MyCoursesAdapterNew myCoursesAdapterNew;

    CardView cardView;

    SearchView search;
    private ArrayList<CourseWithLessData> course, tempCourse;

    RecyclerView mRecyclerView;

    private Context context;

    public void addCourses(ArrayList<CourseWithLessData> course){

        this.course = course;
        this.tempCourse = course;

    }

    public MyCourseTabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_my_course_tab, container, false);

        mRecyclerView = view.findViewById(R.id.my_courses);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        cardView = view.findViewById(R.id.card);

        myCoursesAdapterNew = new MyCoursesAdapterNew(getApplicationContext(), tempCourse);
        mRecyclerView.setAdapter(myCoursesAdapterNew);

        myCoursesAdapterNew.setOnItemClicklistener(this);

        search = view.findViewById(R.id.search);

        context = inflater.getContext();

        if (course.size() == 0) {
            cardView.setVisibility(View.VISIBLE);
        }

        search.setSuggestionsAdapter(null);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                closeKeyboard();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                tempCourse = new ArrayList<>();
                for (CourseWithLessData courseWithLessData : course) {
                    if (courseWithLessData.getCourse_name().toLowerCase().contains(newText)) {
                        tempCourse.add(courseWithLessData);
                    }
                }


                myCoursesAdapterNew = new MyCoursesAdapterNew(getApplicationContext(), tempCourse);
                mRecyclerView.setAdapter(myCoursesAdapterNew);
                myCoursesAdapterNew.setOnItemClicklistener(MyCourseTabFragment.this);

                return true;
            }
        });

        return view;
    }

    @Override
    public void onItemClick(int position) {

        if (context != null) {
                Intent intent = new Intent(getApplicationContext(), Exams.class);
                intent.putExtra("course_name", tempCourse.get(position).getCourse_name());
                intent.putExtra("course_ids", tempCourse.get(position).getCourse_ID());
                intent.putExtra("course_image", tempCourse.get(position).getCourse_imageURL());
                intent.putExtra("type", tempCourse.get(position).getCategory_id());
                context.startActivity(intent);
        }
    }


    @Override
    public void OnPageChanged(int i, int i1) {
        Log.d(TAG, "OnPageChanged: oldPage = " + i + ", newPage = " + i1);
    }

    private void closeKeyboard() {
        if(getActivity() != null) {
            View view = getActivity().getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }


}

