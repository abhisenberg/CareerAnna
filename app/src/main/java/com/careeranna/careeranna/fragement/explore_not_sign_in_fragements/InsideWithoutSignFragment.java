package com.careeranna.careeranna.fragement.explore_not_sign_in_fragements;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.data.Article;
import com.careeranna.careeranna.data.Course;
import com.careeranna.careeranna.data.MyPaidCourse;
import com.careeranna.careeranna.fragement.dashboard_fragements.ArticlesFragment;
import com.careeranna.careeranna.fragement.dashboard_fragements.CoursesWithSearchFragment;

import java.util.ArrayList;

import io.paperdb.Paper;

public class InsideWithoutSignFragment extends Fragment{


    public static final String TAG = "InsideWithoutSignFragement";

    ArrayList<Course> paidCourses, freeCourses;

    ArrayList<MyPaidCourse> purchasedPaidCourses;

    TabLayout tabLayout;

    private Context context;

    ViewPager viewPager;

    public InsideWithoutSignFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inside_without_sign_in, container, false);

        this.context = inflater.getContext();

        Paper.init(context);

        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager_courses);

        viewPager.setOffscreenPageLimit(3);

        context = inflater.getContext();
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        tabLayout.addTab(tabLayout.newTab().setText("Free Course"));
        tabLayout.addTab(tabLayout.newTab().setText("Premium Course"));
        tabLayout.addTab(tabLayout.newTab().setText("Articles"));


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
                return "Free Course";
            } else if(position == 1 ){
                return "Premium Course";
            } else {
                return "Articles";
            }
        }

        @Override
        public Fragment getItem(int position) {

            if(position == 0) {
                CoursesWithSearchFragment fragmentTab = new CoursesWithSearchFragment();
                fragmentTab.addCourses(freeCourses, purchasedPaidCourses);
                return fragmentTab;
            } else if (position == 1){
                CoursesWithSearchFragment fragmentTab = new CoursesWithSearchFragment();
                fragmentTab.addCourses(paidCourses, purchasedPaidCourses);
                return fragmentTab;
            } else {
                ArticlesFragment articlesFragment = new ArticlesFragment();
                articlesFragment.setmArticles(new ArrayList<Article>());
                return articlesFragment;
            }


        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }

    public void addFree(ArrayList<Course> paidCourses,
                        ArrayList<Course> freeCourses,
                        ArrayList<MyPaidCourse> purchasedPaidCourses) {

        this.paidCourses = new ArrayList<>();
        this.freeCourses = new ArrayList<>();
        this.purchasedPaidCourses = new ArrayList<>();

        this.paidCourses.addAll(paidCourses);
        this.freeCourses.addAll(freeCourses);
        this.purchasedPaidCourses.addAll(purchasedPaidCourses);

        viewPager.setAdapter(new PagerAdapter(getFragmentManager(), tabLayout.getTabCount()));

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

    }

}
