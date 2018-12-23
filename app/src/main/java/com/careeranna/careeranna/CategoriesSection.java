package com.careeranna.careeranna;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.careeranna.careeranna.adapter.CoursesSectionAdapter;
import com.careeranna.careeranna.data.Category;
import com.careeranna.careeranna.data.Course;
import com.careeranna.careeranna.data.SubCategory;
import com.careeranna.careeranna.helper.RecyclerViewCoursesAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategoriesSection extends AppCompatActivity implements RecyclerViewCoursesAdapter.OnItemClickListener {

    public static final String TAG = "AppCompat";

    private ArrayList<String> names;
    private ArrayList<String> urls;
    private ArrayList<Course> courses;

    Spinner subCategory;

    RecyclerViewCoursesAdapter recyclerViewAdapter;

    CoursesSectionAdapter coursesSectionAdapter;

    ProgressDialog progressDialog;

    ArrayList<SubCategory> subCategories;

    private String[] imageUrls = new String[] {
            "https://4.bp.blogspot.com/-qf3t5bKLvUE/WfwT-s2IHmI/AAAAAAAABJE/RTy60uoIDCoVYzaRd4GtxCeXrj1zAwVAQCLcBGAs/s1600/Machine-Learning.png",
            "https://cdn-images-1.medium.com/max/2000/1*SSutxOFoBUaUmgeNWAPeBA.jpeg",
            "https://www.digitalvidya.com/wp-content/uploads/2016/02/Master_Digital_marketng-1170x630.jpg"
    };

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories_section);

        recyclerView = findViewById(R.id.categoriesCourses);

        subCategory = findViewById(R.id.categorySpinner);

        names = new ArrayList<>();
        urls = new ArrayList<>();

        Category category = (Category) getIntent().getSerializableExtra("Category");
        getSupportActionBar().setTitle(category.getName());

        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Loading Sub Category Please Wait ... ");
        progressDialog.show();

        progressDialog.setCancelable(false);

        courses = new ArrayList<>();

        subCategories = new ArrayList<>();

        String id = category.getId();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://careeranna.com/api/subCategory.php?id="+id;
        Log.d("url_res", url);
        StringRequest stringRequest  = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<String> subcategories = new ArrayList<>();
                        try {
                            Log.i("url_response", response.toString());
                            JSONArray SubCategoryArray = new JSONArray(response.toString());
                            for(int i=0;i<SubCategoryArray.length();i++) {
                                JSONObject Category = SubCategoryArray.getJSONObject(i);
                                subcategories.add(Category.getString("EXAM_NAME"));
                                subCategories.add(new SubCategory(Category.getString("EXAM_NAME_ID"),
                                        Category.getString("EXAM_NAME_ID"),
                                        Category.getString("CATEGORY_ID"),
                                        Category.getString("ACTIVE_STATUS")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CategoriesSection.this,
                                android.R.layout.simple_spinner_item,subcategories);
                        subCategory.setAdapter(adapter);
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

        requestQueue.add(stringRequest);

        subCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SubCategory subCategory = subCategories.get(position);

                progressDialog = new ProgressDialog(CategoriesSection.this);

                progressDialog.setMessage("Loading Courses Please Wait ... ");
                progressDialog.show();

                progressDialog.setCancelable(false);

                RequestQueue requestQueue1 = Volley.newRequestQueue(CategoriesSection.this);
                String url1 = "https://careeranna.com/api/product.php?id="+subCategory.getEXAM_NAME_ID();
                Log.d("url_res", url1);
                StringRequest stringRequest1  = new StringRequest(Request.Method.GET, url1,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {

                                    courses = new ArrayList<>();
                                    names = new ArrayList<>();
                                    urls = new ArrayList<>();

                                    Log.i("url_response", response.toString());
                                    JSONArray CategoryArray = new JSONArray(response.toString());
                                    for(int i=0;i<CategoryArray.length();i++) {
                                        JSONObject Category = CategoryArray.getJSONObject(i);
                                        names.add(Category.getString("course_name"));
                                        urls.add("https://www.careeranna.com/"+Category.getString("product_image").replace("\\",""));
                                        courses.add(new Course(Category.getString("product_id"),
                                                Category.getString("course_name"),
                                                "https://www.careeranna.com/"+Category.getString("product_image").replace("\\",""),
                                                Category.getString("category_id"),
                                                Category.getString("price")
                                                , Category.getString("description"),
                                                Category.getString("video_url").replace("\\","")));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                coursesSectionAdapter = new CoursesSectionAdapter(courses, CategoriesSection.this);

                                recyclerView.setAdapter(coursesSectionAdapter);

                                coursesSectionAdapter.setOnItemClicklistener(CategoriesSection.this);
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
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        coursesSectionAdapter = new CoursesSectionAdapter(courses, this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(coursesSectionAdapter);
        coursesSectionAdapter.setOnItemClicklistener(this);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getApplicationContext(), PurchaseCourses.class);
        intent.putExtra("Course", courses.get(position));
        startActivity(intent);
    }

}
