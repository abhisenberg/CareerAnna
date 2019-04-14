package com.careeranna.careeranna.fragement.dashboard_fragements;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
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
import com.careeranna.careeranna.data.Article;
import com.careeranna.careeranna.data.CourseWithLessData;
import com.careeranna.careeranna.fragement.MyCourseTabFragment;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;

import java.util.ArrayList;

import io.paperdb.Paper;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MyCoursesFragment extends Fragment implements MyCoursesAdapterNew.OnItemClickListener
        , RecyclerViewPager.OnPageChangedListener {
    public static final String TAG = "MyCourseFragment";

    MyCoursesAdapterNew myCoursesAdapterNew;

    CardView cardView;

    SearchView search;
    private ArrayList<CourseWithLessData> course, tempCourse;

    RecyclerView mRecyclerView;
    private int rv_currentPage;


    TabLayout tabLayout;

    private Context context;

    ViewPager viewPager;

    public MyCoursesFragment() {
        // Required empty public constructor
    }

    public void add(ArrayList<CourseWithLessData> course) {

        this.course = course;
        this.tempCourse = course;

        viewPager.setAdapter(new PagerAdapter(getFragmentManager(), tabLayout.getTabCount()));

        Boolean paid = false;

        for(CourseWithLessData courseWithLessData: course) {
            if(courseWithLessData.getCategory_id().equals("paid")) {
                paid = true;
                break;
            }
        }


        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setVisibility(View.VISIBLE);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        if(!paid) {
            viewPager.setCurrentItem(1);
        }

//        if (course.size() == 0) {
//            cardView.setVisibility(View.VISIBLE);
//        }
//
//        myCoursesAdapterNew = new MyCoursesAdapterNew(getApplicationContext(), tempCourse);
//        mRecyclerView.setAdapter(myCoursesAdapterNew);
//
//        myCoursesAdapterNew.setOnItemClicklistener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_course, container, false);

        course = new ArrayList<>();

        this.context = inflater.getContext();

        Paper.init(context);

        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager_courses);

        viewPager.setOffscreenPageLimit(3);

        context = inflater.getContext();
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        tabLayout.addTab(tabLayout.newTab().setText("My Course"));
        tabLayout.addTab(tabLayout.newTab().setText("Articles"));
        tabLayout.addTab(tabLayout.newTab().setText("Premium Courses"));
        tabLayout.addTab(tabLayout.newTab().setText("Free Courses"));

//        mRecyclerView = view.findViewById(R.id.my_courses);
//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
//        mRecyclerView.setLayoutManager(linearLayoutManager);
//        cardView = view.findViewById(R.id.card);
//
//        search = view.findViewById(R.id.search);
//        rv_currentPage = 0;
//
//        search.setSuggestionsAdapter(null);
//        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                closeKeyboard();
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//
//                tempCourse = new ArrayList<>();
//                for (CourseWithLessData courseWithLessData : course) {
//                    if (courseWithLessData.getCourse_name().toLowerCase().contains(newText)) {
//                        tempCourse.add(courseWithLessData);
//                    }
//                }
//
//
//                myCoursesAdapterNew = new MyCoursesAdapterNew(getApplicationContext(), tempCourse);
//                mRecyclerView.setAdapter(myCoursesAdapterNew);
//                myCoursesAdapterNew.setOnItemClicklistener(MyCoursesFragment.this);
//
//                return true;
//            }
//        });

        return view;
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;

        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 0) {
                return "My Courses";
            } else if(position == 1) {
                return "Articles";
            } else if(position == 2){
                return "Premium Courses";
            } else {
                return "Free Courses";
            }
        }

        @Override
        public Fragment getItem(int position) {

            MyCourseTabFragment fragmentTab = new MyCourseTabFragment();

            if(position == 0) {
                fragmentTab.addCourses(course);
                return fragmentTab;
            } else if (position == 1){
                ArticlesFragment articlesFragment = new ArticlesFragment();
                articlesFragment.setmArticles(new ArrayList<Article>());
                return articlesFragment;
            } else if (position == 2) {
                PremiumFragment paidCoursesFragment = new PremiumFragment();
                return paidCoursesFragment;
            }  else {
                FreeCoursesFragment freeCoursesFragment = new FreeCoursesFragment();
                return freeCoursesFragment;
            }

        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
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

    @Override
    public void onItemClick(int position) {

        if (getContext() != null) {
            if (course.get(position).getCategory_id().equals("15")) {
                Intent intent = new Intent(getApplicationContext(), ParticularCourse.class);
                intent.putExtra("course_name", tempCourse.get(position).getCourse_name());
                intent.putExtra("course_ids", tempCourse.get(position).getCourse_ID());
                intent.putExtra("course_image", tempCourse.get(position).getCourse_imageURL());
                intent.putExtra("type", tempCourse.get(position).getCategory_id());
                getContext().startActivity(intent);
            } else {
                Intent intent = new Intent(getApplicationContext(), Exams.class);
                intent.putExtra("course_name", tempCourse.get(position).getCourse_name());
                intent.putExtra("course_ids", tempCourse.get(position).getCourse_ID());
                intent.putExtra("course_image", tempCourse.get(position).getCourse_imageURL());
                intent.putExtra("type", tempCourse.get(position).getCategory_id());
                getContext().startActivity(intent);

            }
        }
    }


    @Override
    public void OnPageChanged(int i, int i1) {
        Log.d(TAG, "OnPageChanged: oldPage = " + i + ", newPage = " + i1);
        rv_currentPage = i1;
    }
}
