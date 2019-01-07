package com.careeranna.careeranna.fragement.dashboard_fragements;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.careeranna.careeranna.activity.FilterOfCategory;
import com.careeranna.careeranna.activity.PurchaseCourseDetail;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.activity.SortByCourse;
import com.careeranna.careeranna.adapter.FreeCourseAdapter;
import com.careeranna.careeranna.adapter.MyCoursesAdapterNew;
import com.careeranna.careeranna.data.Course;
import com.careeranna.careeranna.data.CourseWithLessData;
import com.careeranna.careeranna.data.FreeVideos;
import com.careeranna.careeranna.data.SubCategory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class CategoryFragment extends Fragment implements FreeCourseAdapter.OnItemClickListener {

    ProgressDialog progressDialog;

    SearchView search;

    private ArrayList<String> ids1;
    private ArrayList<Course> courses;

    private ArrayList<Course> tempCourse, temp;

    ArrayList<SubCategory> subCategories, subCategoriesfree;

    RecyclerView recyclerView;

    ArrayList<FreeVideos> freeVideos;

    String id, user_id, freecategory_id;

    Button filterSub, sortSub;

    CardView cardView;

    android.app.AlertDialog.Builder builder;

    android.app.AlertDialog alert;


    public CategoryFragment() {
        // Required empty public constructor
    }

    public void addSubCategory(String id, String freecategory_id, String user_id) {

        this.id = id;
        this.user_id = user_id;
        this.freecategory_id = freecategory_id;

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        recyclerView = view.findViewById(R.id.categoriesCourses);

        cardView = view.findViewById(R.id.card);

        filterSub = view.findViewById(R.id.filterSub);

        sortSub = view.findViewById(R.id.sort);

        closeKeyboard();

        temp = new ArrayList<>();

        courses = new ArrayList<>();

        search = view.findViewById(R.id.search);

        search.setSuggestionsAdapter(null);

        search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                FreeCourseAdapter freeCourseAdapter = new FreeCourseAdapter(tempCourse, getApplicationContext());
                recyclerView.setAdapter(freeCourseAdapter);
                freeCourseAdapter.setOnItemClicklistener(CategoryFragment.this);

                closeKeyboard();
                return true;
            }
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                closeKeyboard();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                ArrayList<Course> tempCourse1 = new ArrayList<>();
                for (Course courseWithLessData : tempCourse) {
                    if (courseWithLessData.getName().toLowerCase().contains(newText)) {
                        tempCourse1.add(courseWithLessData);
                    }
                }

                FreeCourseAdapter freeCourseAdapter = new FreeCourseAdapter(tempCourse1, getApplicationContext());
                recyclerView.setAdapter(freeCourseAdapter);
                freeCourseAdapter.setOnItemClicklistener(CategoryFragment.this);

                return true;
            }
        });


        tempCourse = new ArrayList<>();

        subCategories = new ArrayList<>();
        subCategoriesfree = new ArrayList<>();

        filterSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FilterOfCategory.class);
                intent.putExtra("categories", subCategories);
                startActivityForResult(intent, 111);
            }
        });


        sortSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SortByCourse.class);
                intent.putExtra("categories", subCategories);
                startActivityForResult(intent, 111);
            }
        });

        progressDialog = new ProgressDialog(getContext());

        progressDialog.setMessage(getString(R.string.loading_subcategory));
        progressDialog.show();

        progressDialog.setCancelable(false);

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        final String url = "https://www.careeranna.com/api/getCourseByCategory.php?id=" + id + "&free=" + freecategory_id;
        Log.d("url_res", url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<String> subcategories = new ArrayList<>();
                        try {
                            Log.i("url_response", response.toString());
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray SubCategoryArray = jsonObject.getJSONArray("paid");
                            for (int i = 0; i < SubCategoryArray.length(); i++) {
                                JSONObject Category = SubCategoryArray.getJSONObject(i);
                                subcategories.add(Category.getString("EXAM_NAME"));
                                subCategories.add(new SubCategory(Category.getString("EXAM_NAME_ID"),
                                        Category.getString("EXAM_NAME"),
                                        Category.getString("CATEGORY_ID"),
                                        Category.getString("ACTIVE_STATUS")));
                            }
                            JSONArray SubCategoryArray1 = jsonObject.getJSONArray("free");
                            for (int i = 0; i < SubCategoryArray1.length(); i++) {
                                JSONObject Category = SubCategoryArray1.getJSONObject(i);
                                subcategories.add(Category.getString("name"));
                                subCategoriesfree.add(new SubCategory(Category.getString("eid"),
                                        Category.getString("name"),
                                        Category.getString("cid"),
                                        Category.getString("status")));
                            }
                            subCategories.addAll(subCategoriesfree);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        progressDialog.dismiss();

                        populateCourse();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressDialog.dismiss();

                    }
                }
        );

        requestQueue.add(stringRequest);

        freeVideos = new ArrayList<>();

        freeVideos.add(new FreeVideos());

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        FreeCourseAdapter freeCourseAdapter = new FreeCourseAdapter(courses, getApplicationContext());

        recyclerView.setAdapter(freeCourseAdapter);

        freeCourseAdapter.setOnItemClicklistener(this);

        return view;
    }

    private void populateCourse() {

        progressDialog = new ProgressDialog(getContext());

        progressDialog.setMessage(getString(R.string.loading_courses));
        progressDialog.show();

        progressDialog.setCancelable(false);

        RequestQueue requestQueue1 = Volley.newRequestQueue(getContext());
        String url1 = "https://careeranna.com/api/getAllCourse.php";
        Log.d("url_res", url1);
        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            courses = new ArrayList<>();

                            Log.i("url_response", response.toString());
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray CategoryArray = jsonObject.getJSONArray("paid");
                            for (int i = 0; i < CategoryArray.length(); i++) {
                                JSONObject Category = CategoryArray.getJSONObject(i);
                                courses.add(new Course(Category.getString("product_id"),
                                        Category.getString("course_name"),
                                        "https://www.careeranna.com/" + Category.getString("product_image").replace("\\", ""),
                                        Category.getString("exam_id"),
                                        Category.getString("discount")
                                        , "",
                                        Category.getString("video_url").replace("\\", "")));
                            }
                            JSONArray FreeArray = jsonObject.getJSONArray("free");
                            for (int i = 0; i < FreeArray.length(); i++) {
                                JSONObject Category = FreeArray.getJSONObject(i);
                                temp.add(new Course(Category.getString("course_id"),
                                        Category.getString("name"),
                                        "https://www.careeranna.com/" + Category.getString("image").replace("\\", ""),
                                        Category.getString("examIds"),
                                        "0"
                                        , "",
                                        ""));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        progressDialog.dismiss();

                        tempCourse = new ArrayList<>();

                        for (SubCategory subCategory : subCategories) {
                            if (subCategory.getCATEGORY_ID().equals(id)) {
                                for (Course course : courses) {
                                    if (!course.getPrice().equals("0")) {
                                        if (course.getCategory_id().equals(subCategory.getEXAM_NAME_ID())) {
                                            tempCourse.add(course);
                                        }
                                    }
                                }
                            }
                        }

                        for (SubCategory subCategory : subCategoriesfree) {
                            if (subCategory.getCATEGORY_ID().equals(freecategory_id)) {
                                for (Course course : temp) {
                                    if (course.getPrice().equals("0")) {
                                        if (course.getCategory_id().contains(subCategory.getEXAM_NAME_ID())) {
                                            tempCourse.add(course);
                                        }
                                    }
                                }
                            }
                        }

                        FreeCourseAdapter freeCourseAdapter = new FreeCourseAdapter(tempCourse, getApplicationContext());

                        recyclerView.setAdapter(freeCourseAdapter);

                        if (tempCourse.size() == 0) {
                            cardView.setVisibility(View.VISIBLE);
                            filterSub.setEnabled(false);
                            sortSub.setEnabled(false);

                        } else {
                            cardView.setVisibility(View.INVISIBLE);
                            myCourse();
                        }


                        freeCourseAdapter.setOnItemClicklistener(CategoryFragment.this);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                    }
                }
        );

        requestQueue1.add(stringRequest1);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 111) {

            if (resultCode == Activity.RESULT_OK) {
                ArrayList<Course> tempCourse1 = new ArrayList<>();

                ArrayList<SubCategory> subCategories = (ArrayList<SubCategory>) data.getSerializableExtra("category");

                    if (subCategories.size() > 0) {
                        for (SubCategory subCategory : subCategories) {
                            if (subCategory.getCATEGORY_ID().equals(freecategory_id)) {
                                for (Course course : tempCourse) {
                                    if (course.getPrice().equals("0")) {
                                        if (course.getCategory_id().contains(subCategory.getEXAM_NAME_ID())) {
                                            tempCourse1.add(course);
                                        }
                                    }
                                }
                            }
                        }
                        for (SubCategory subCategory : subCategories) {
                            if (subCategory.getCATEGORY_ID().equals(id)) {
                                for (Course course : tempCourse) {
                                    if (!course.getPrice().equals("0")) {
                                        if (course.getCategory_id().equals(subCategory.getEXAM_NAME_ID())) {
                                            tempCourse1.add(course);
                                        }
                                    }
                                }
                            }
                        }
                    }

                FreeCourseAdapter freeCourseAdapter = new FreeCourseAdapter(tempCourse1, getApplicationContext());
                recyclerView.setAdapter(freeCourseAdapter);

                Toast.makeText(getApplicationContext(), "List Updated", Toast.LENGTH_SHORT).show();
                freeCourseAdapter.setOnItemClicklistener(this);

            }

            if(resultCode == Activity.RESULT_CANCELED) {

                FreeCourseAdapter freeCourseAdapter = new FreeCourseAdapter(tempCourse, getApplicationContext());
                recyclerView.setAdapter(freeCourseAdapter);

                Toast.makeText(getApplicationContext(), "Filter Cancelled", Toast.LENGTH_SHORT).show();
                freeCourseAdapter.setOnItemClicklistener(this);
            }
        }

    }

    public void myCourse() {

        ids1 = new ArrayList<>();

        progressDialog = new ProgressDialog(getContext());

        progressDialog.setMessage(getString(R.string.loading_your_courses));
        progressDialog.show();

        progressDialog.setCancelable(false);

        RequestQueue requestQueue1 = Volley.newRequestQueue(getContext());
        final String url1 = "https://careeranna.com/api/getMyCourse.php?user=" + user_id;
        Log.d("url_res", url1);
        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("url_response", response.toString());
                            JSONArray coursesArray = new JSONArray(response);
                            for (int i = 0; i < coursesArray.length(); i++) {
                                JSONObject Category = coursesArray.getJSONObject(i);
                                ids1.add(Category.getString("product_id"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                    }
                }
        );

        requestQueue1.add(stringRequest1);
    }

    @Override
    public void onItemClick1(String type, int position) {

        if (ids1.size() > 0) {
            for (String id : ids1) {
                if (id.equals(tempCourse.get(position).getId())) {

                    builder = new android.app.AlertDialog.Builder(getContext());
                    builder.setTitle("Already purchased");
                    builder.setIcon(R.mipmap.ic_launcher);
                    builder.setCancelable(false);
                    builder.setMessage("You have already purchased this course!")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    alert.dismiss();
                                }
                            });
                    alert = builder.create();
                    alert.show();
                    return;
                }
            }
        }
        startActivity(new Intent(getApplicationContext(), PurchaseCourseDetail.class).putExtra("Course", tempCourse.get(position)));

    }

    private void closeKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}

