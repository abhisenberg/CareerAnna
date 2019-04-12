package com.careeranna.careeranna.fragement.dashboard_fragements;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.careeranna.careeranna.R;
import com.careeranna.careeranna.activity.PurchaseCourseActivity;
import com.careeranna.careeranna.activity.PurchaseCourseDetail;
import com.careeranna.careeranna.adapter.CoursesAdapter;
import com.careeranna.careeranna.data.Course;
import com.careeranna.careeranna.data.FreeCourse;
import com.careeranna.careeranna.data.MyPaidCourse;
import com.careeranna.careeranna.data.User;
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

import static com.facebook.FacebookSdk.getApplicationContext;

public class FreeCoursesFragment extends Fragment implements CoursesAdapter.OnItemClickListener {

    CoursesAdapter coursesAdapter;

    RecyclerView courses_rv;

    private ArrayList<Course> freeCourses, tempCourses;

    android.support.v7.widget.SearchView searchView;

    ProgressBar progressBar;

    CardView no_course_card;

    TextView no_course_tv;

    Context context;

    User user;

    ArrayList<MyPaidCourse> purchasedPaidCourses;

    public FreeCoursesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_free_courses, container, false);

        courses_rv = view.findViewById(R.id.free_courses_rv);
        courses_rv.setLayoutManager(new LinearLayoutManager(inflater.getContext(), LinearLayoutManager.VERTICAL, false));

        searchView = view.findViewById(R.id.search_free_course);

        progressBar = view.findViewById(R.id.progress);

        no_course_card = view.findViewById(R.id.no_course_card);

        no_course_tv = view.findViewById(R.id.no_course_tv);

        freeCourses = new ArrayList<>();

        context = inflater.getContext();

        Paper.init(inflater.getContext());

        String cache = Paper.book().read("user");
        if (cache != null && !cache.isEmpty()) {
            user = new Gson().fromJson(cache, User.class);
        }

        populateCourses(inflater.getContext());

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                closeKeyboard();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                freeCourses.clear();

                for (Course course: tempCourses) {
                    if(course.getName().trim().toLowerCase().contains(newText.trim().toLowerCase())) {
                        freeCourses.add(course);
                    }
                }

                if(freeCourses.size() == 0) {
                    no_course_card.setVisibility(View.VISIBLE);
                    no_course_tv.setText("Couldn't Find Any Course Matches ' " + newText + " '");
                } else {
                    no_course_card.setVisibility(View.GONE);
                }

                coursesAdapter.notifyDataSetChanged();

                return true;
            }
        });

        return view;

    }

    @Override
    public void onItemClick1(String type, int position) {

        Intent intent = new Intent(context, PurchaseCourseActivity.class);
        intent.putExtra("Course", freeCourses.get(position));
        intent.putExtra("my_paid_course", purchasedPaidCourses);
        startActivity(intent);

    }

    public void populateCourses(final Context context) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NewApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NewApi api = retrofit.create(NewApi.class);

        Call<List<FreeCourse>> call = api.getFreeCourses();

        call.enqueue(new Callback<List<FreeCourse>>() {
            @Override
            public void onResponse(Call<List<FreeCourse>> call, Response<List<FreeCourse>> response) {
                List<FreeCourse> coursesList = response.body();

                for(FreeCourse course: coursesList) {
                    freeCourses.add(new Course(course.getCourse_id(),
                            course.getName(),
                            "https://www.careeranna.com/" + course.getImage().replace("\\", ""),
                            "1",
                            "0"
                            , "",
                            "",
                            "Free",
                            "0",
                            course.getAverage_rating(),
                            course.getTotal_rating(),
                            course.getLearners_count()));
                }

                tempCourses = new ArrayList<>();

                tempCourses.addAll(freeCourses);

               populateMyPaidCoursesId();
            }

            @Override
            public void onFailure(Call<List<FreeCourse>> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    private void closeKeyboard() {
        if (getActivity() != null) {
            View view = getActivity().getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
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
            public void onResponse(Call<List<MyPaidCourse>> call, retrofit2.Response<List<MyPaidCourse>> response) {

                purchasedPaidCourses = new ArrayList<>();

                List<MyPaidCourse> myPaidCoursesList = response.body();

                purchasedPaidCourses.addAll(myPaidCoursesList);

                coursesAdapter = new CoursesAdapter(freeCourses, getApplicationContext());

                courses_rv.setAdapter(coursesAdapter);

                coursesAdapter.setOnItemClickListener(FreeCoursesFragment.this);

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<MyPaidCourse>> call, Throwable t) {

                coursesAdapter = new CoursesAdapter(freeCourses, getApplicationContext());

                courses_rv.setAdapter(coursesAdapter);

                coursesAdapter.setOnItemClickListener(FreeCoursesFragment.this);

                progressBar.setVisibility(View.GONE);
            }
        });

    }
}
