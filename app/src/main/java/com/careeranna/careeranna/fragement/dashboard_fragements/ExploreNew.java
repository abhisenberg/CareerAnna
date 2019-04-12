package com.careeranna.careeranna.fragement.dashboard_fragements;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.support.v7.widget.SearchView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.careeranna.careeranna.activity.MyCourses;
import com.careeranna.careeranna.activity.PurchaseCourseDetail;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.adapter.ExamArrayAdapter;
import com.careeranna.careeranna.adapter.CoursesAdapter;
import com.careeranna.careeranna.data.Article;
import com.careeranna.careeranna.data.Constants;
import com.careeranna.careeranna.data.Course;
import com.careeranna.careeranna.data.MyPaidCourse;
import com.careeranna.careeranna.data.SubCategory;
import com.careeranna.careeranna.fragement.FragmentTab;

import java.util.ArrayList;

import io.paperdb.Paper;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.facebook.FacebookSdk.getApplicationContext;

public class ExploreNew extends Fragment {

    public static final String TAG = "ExploreNew";

    ArrayList<Course> paidCourses, freeCourses;

    ArrayList<MyPaidCourse> purchasedPaidCourses;

    TabLayout tabLayout;

    private Context context;

    ViewPager viewPager;


    public ExploreNew() {
        // Required empty public constructor
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    @NonNull
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragement_my_explore_new, container, false);

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

        viewPager.setAdapter(new ExploreNew.PagerAdapter(getFragmentManager(), tabLayout.getTabCount()));

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
