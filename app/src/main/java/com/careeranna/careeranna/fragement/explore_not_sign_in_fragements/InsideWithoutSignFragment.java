package com.careeranna.careeranna.fragement.explore_not_sign_in_fragements;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.careeranna.careeranna.activity.PurchaseCourseDetail;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.adapter.FreeCourseAdapter;
import com.careeranna.careeranna.data.Course;
import com.careeranna.careeranna.data.UrlConstants;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class InsideWithoutSignFragment extends Fragment implements FreeCourseAdapter.OnItemClickListener {


    ArrayList<Course> courses, freecourse;

    Button free_btn, premium_btn;


    RecyclerView free_course_recyler, paid_course_recyler;

    public static int position_free = 0, position_paid = 0;

    ProgressDialog progressDialog;

    ImageView arrow_paid_left, arrow_paid_right, arrow_free_right, arrow_free_left;

    CardView  premiumCard, freeCard;

    public InsideWithoutSignFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inside_without_sign_in, container, false);

        free_course_recyler = view.findViewById(R.id.free_course_rv);

        paid_course_recyler = view.findViewById(R.id.paid_courses_rv);

        premiumCard = view.findViewById(R.id.premium_card);
        freeCard = view.findViewById(R.id.free_card);

        initalizeVideos();

        free_btn = view.findViewById(R.id.free);
        premium_btn = view.findViewById(R.id.premium);

        hidingRecylerView();

        return view;
    }


    private void addpaid_course_recyler() {

        courses = new ArrayList<>();

        if(getContext() != null) {
            progressDialog = new ProgressDialog(getContext());

            progressDialog.setMessage(getString(R.string.loading_premium_courses));
            progressDialog.show();

            progressDialog.setCancelable(false);

            RequestQueue requestQueue1 = Volley.newRequestQueue(getContext());
            StringRequest stringRequest1 = new StringRequest(Request.Method.GET,
                    UrlConstants.FETCH_PREMIUM_COURSE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                courses = new ArrayList<>();

                                Log.i("url_response", response.toString());
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray CategoryArray = jsonObject.getJSONArray("paid");
                                for (int i = 0; i < 20; i++) {
                                    JSONObject Category = CategoryArray.getJSONObject(i);
                                    courses.add(new Course(Category.getString("product_id"),
                                                    Category.getString("course_name"),
                                                    "https://www.careeranna.com/" + Category.getString("product_image").replace("\\", ""),
                                                    Category.getString("exam_id"),
                                                    Category.getString("discount")
                                                    , "",
                                                    Category.getString("video_url").replace("\\", ""),
                                                    "Paid",
                                                    Category.getString("price")
                                            )
                                    );
                                    courses.get(i).setType("Paid");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            FreeCourseAdapter freeCourseAdapter = new FreeCourseAdapter(courses, getApplicationContext());

                            paid_course_recyler.setAdapter(freeCourseAdapter);

                            freeCourseAdapter.setOnItemClicklistener(InsideWithoutSignFragment.this);

                            progressDialog.dismiss();

                            addFree();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            FreeCourseAdapter freeCourseAdapter = new FreeCourseAdapter(courses, getApplicationContext());

                            paid_course_recyler.setAdapter(freeCourseAdapter);

                            freeCourseAdapter.setOnItemClicklistener(InsideWithoutSignFragment.this);

                            progressDialog.dismiss();

                            addFree();
                        }
                    }
            );

            requestQueue1.add(stringRequest1);
        }
    }


    private void addFree() {

        if(getContext() != null) {
            freecourse = new ArrayList<>();

            progressDialog = new ProgressDialog(getContext());

            progressDialog.setMessage(getString(R.string.loading_free_courses));
            progressDialog.show();

            progressDialog.setCancelable(false);

            RequestQueue requestQueue1 = Volley.newRequestQueue(getContext());
            StringRequest stringRequest1 = new StringRequest(Request.Method.GET,
                    UrlConstants.FETCH_FREE_COURSE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                Log.i("url_response", response.toString());
                                JSONArray CategoryArray = new JSONArray(response.toString());
                                for (int i = 0; i < CategoryArray.length(); i++) {
                                    JSONObject Category = CategoryArray.getJSONObject(i);
                                    freecourse.add(new Course(Category.getString("course_id"),
                                            Category.getString("name"),
                                            "https://www.careeranna.com/" + Category.getString("image").replace("\\", ""),
                                            Category.getString("eid"),
                                            "0"
                                            , "meta_description", ""));
                                    freecourse.get(i).setType("Free");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            progressDialog.dismiss();


                            FreeCourseAdapter freeCourseAdapter1 = new FreeCourseAdapter(freecourse, getApplicationContext());

                            free_course_recyler.setAdapter(freeCourseAdapter1);

                            freeCourseAdapter1.setOnItemClicklistener(InsideWithoutSignFragment.this);

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();

                            FreeCourseAdapter freeCourseAdapter1 = new FreeCourseAdapter(freecourse, getApplicationContext());

                            free_course_recyler.setAdapter(freeCourseAdapter1);

                            freeCourseAdapter1.setOnItemClicklistener(InsideWithoutSignFragment.this);

                        }
                    }
            );

            requestQueue1.add(stringRequest1);
        }
    }


    private void initalizeVideos() {

        free_course_recyler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        paid_course_recyler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        addpaid_course_recyler();
    }



    private void hidingRecylerView() {

        free_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                freeCard.setVisibility(View.VISIBLE);
                premiumCard.setVisibility(View.GONE);
                free_btn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                premium_btn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                free_btn.setTypeface(null, Typeface.BOLD);
                premium_btn.setTypeface(null, Typeface.NORMAL);
            }
        });

        premium_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                premiumCard.setVisibility(View.VISIBLE);
                freeCard.setVisibility(View.GONE);
                premium_btn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                free_btn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                premium_btn.setTypeface(null, Typeface.BOLD);
                free_btn.setTypeface(null, Typeface.NORMAL);
            }
        });
    }

    @Override
    public void onItemClick1(String type, int position) {

        if (type.equals("Free")) {
            startActivity(new Intent(getApplicationContext(), PurchaseCourseDetail.class).putExtra("Course", freecourse.get(position)));
        }
        if (type.equals("Paid")) {
            startActivity(new Intent(getApplicationContext(), PurchaseCourseDetail.class).putExtra("Course", courses.get(position)));
        }
    }

}
