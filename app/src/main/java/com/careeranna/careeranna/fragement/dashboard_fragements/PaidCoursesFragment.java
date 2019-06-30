package com.careeranna.careeranna.fragement.dashboard_fragements;

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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.careeranna.careeranna.R;
import com.careeranna.careeranna.data.Course;
import com.careeranna.careeranna.data.MyPaidCourse;
import com.careeranna.careeranna.data.PaidCourse;
import com.careeranna.careeranna.data.SubCategory;
import com.careeranna.careeranna.data.User;
import com.careeranna.careeranna.fragement.FragmentTab;
import com.careeranna.careeranna.helper.NewApi;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PaidCoursesFragment extends Fragment  {

    ArrayList<SubCategory> subCategories;

    ArrayList<Course> paidCourses, tempPaidCourses;

    ArrayList<MyPaidCourse> purchasedPaidCourses;

    TabLayout tabLayout;

    private Context context;

    ViewPager viewPager;

    ProgressBar progressBar;

    User user;

    public PaidCoursesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_paid_courses, container, false);

        Paper.init(inflater.getContext());

        String cache = Paper.book().read("user");
        if (cache != null && !cache.isEmpty()) {
            user = new Gson().fromJson(cache, User.class);
        }


        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager_courses);
        progressBar = view.findViewById(R.id.progress);

        context = inflater.getContext();

        populatePaidCourseSubCategory();

        return view;
    }

    private void populatePaidCourseSubCategory() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NewApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NewApi api = retrofit.create(NewApi.class);

        Call<List<SubCategory>> call = api.getPaidCourseSubCategory();

        call.enqueue(new Callback<List<SubCategory>>() {
            @Override
            public void onResponse(Call<List<SubCategory>> call, Response<List<SubCategory>> response) {
                subCategories = new ArrayList<>();

                List<SubCategory> subCategoriesList = response.body();

                for(SubCategory subCategory: subCategoriesList) {
                    subCategories.add(subCategory);
                    tabLayout.addTab(tabLayout.newTab().setText(subCategory.getEXAM_NAME()));
                }

                populatePaidCourses();

            }

            @Override
            public void onFailure(Call<List<SubCategory>> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void populatePaidCourses() {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NewApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NewApi api = retrofit.create(NewApi.class);

        Call<List<PaidCourse>> call = api.getPaidCourses();

        call.enqueue(new Callback<List<PaidCourse>>() {
            @Override
            public void onResponse(Call<List<PaidCourse>> call, Response<List<PaidCourse>> response) {

                paidCourses = new ArrayList<>();
                tempPaidCourses = new ArrayList<>();

                List<PaidCourse> paidCoursesList = response.body();

                for(PaidCourse  paidCourse: paidCoursesList) {
                    paidCourses.add(new Course(paidCourse.getProduct_id(),
                            paidCourse.getCourse_name(),
                            "https://www.careeranna.com/" + paidCourse.getProduct_image().replace("\\", ""),
                            paidCourse.getExam_id(),
                            paidCourse.getDiscount(),
                            "",
                            "",
                            "Paid",
                           paidCourse.getPrice(),
                           paidCourse.getAverage_rating(),
                            paidCourse.getTotal_rating(),
                            paidCourse.getLearners_count()));
                }

                tempPaidCourses.addAll(paidCourses);

                populateMyPaidCoursesId();
            }

            @Override
            public void onFailure(Call<List<PaidCourse>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void populateMyPaidCoursesId() {

        Map<String, String> data = new HashMap<>();
        data.put("user_id", user.getUser_id());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NewApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NewApi api = retrofit.create(NewApi.class);

        Call<List<MyPaidCourse>> call = api.getMyPaidCourse(data);

        call.enqueue(new Callback<List<MyPaidCourse>>() {
            @Override
            public void onResponse(Call<List<MyPaidCourse>> call, Response<List<MyPaidCourse>> response) {
                progressBar.setVisibility(View.GONE);

                purchasedPaidCourses = new ArrayList<>();

                List<MyPaidCourse> myPaidCoursesList = response.body();

                purchasedPaidCourses.addAll(myPaidCoursesList);

                viewPager.setAdapter(new PaidCoursesFragment.PagerAdapter(getFragmentManager(), tabLayout.getTabCount()));

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

            @Override
            public void onFailure(Call<List<MyPaidCourse>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public class PagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;

        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return subCategories.get(position).getEXAM_NAME();
        }

        @Override
        public Fragment getItem(int position) {

            FragmentTab fragmentTab = new FragmentTab();

            paidCourses.clear();

            for(Course course: tempPaidCourses) {

                if(course.getId().equals("62")) {
                    if(subCategories.get(position).getEXAM_NAME_ID().equals("2")
                    ||subCategories.get(position).getEXAM_NAME_ID().equals("20")
                    ||subCategories.get(position).getEXAM_NAME_ID().equals("16")
                    ||subCategories.get(position).getEXAM_NAME_ID().equals("3")
                    ||subCategories.get(position).getEXAM_NAME_ID().equals("8")) {
                        paidCourses.add(course);

                        continue;
                    }
                }

                if(course.getCategory_id().equals(subCategories.get(position).getEXAM_NAME_ID())) {

                    paidCourses.add(course);
                }

            }

            fragmentTab.addList(paidCourses, purchasedPaidCourses);

            return fragmentTab;

        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }


}
