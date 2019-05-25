package com.careeranna.careeranna.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.careeranna.careeranna.JW_Player_Files.KeepScreenOnHandler;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.adapter.CourseContentAdapter;
import com.careeranna.careeranna.data.Course;
import com.careeranna.careeranna.data.Topic;
import com.careeranna.careeranna.data.Unit;
import com.careeranna.careeranna.data.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.events.FullscreenEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.paperdb.Paper;

import static android.view.View.GONE;

public class NotificationCourseActivity extends AppCompatActivity implements VideoPlayerEvents.OnFullscreenListener{

    Course course;

    ImageView iv_course_image;

    RelativeLayout progress_bar_layout;

    TextView tv_courseName,
            tv_instructor_name,
            tv_cost,
            tv_striked_cost,
            tv_discount,
            course_rating_number,
            course_number_of_ratings,
            course_enrollments;

    Button enroll_now, add_to_cart, already_enrolled;

    WebView wv_productDesc;

    private JWPlayerView playerView;

    boolean already_enrolled_in_course = false;

    boolean isAvailable = false;

    RatingBar ratingBar;

    String course_id = "";
    String course_url = "https://www.careeranna.com/api/fetchNotificationCourseDetailApp.php?product_id=";

    RecyclerView course_content_rv;

    User user;

    Boolean purchasedCourse = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_course);

        Paper.init(this);

        String cache = Paper.book().read("user");
        if (cache != null && !cache.isEmpty()) {
            user = new Gson().fromJson(cache, User.class);
        }  else {
            playerView.setVisibility(GONE);
        }

        enroll_now = findViewById(R.id.enroll_now);
        add_to_cart = findViewById(R.id.add_to_cart);
        already_enrolled = findViewById(R.id.already_enrolled);

        progress_bar_layout = findViewById(R.id.progress_bar_layout);
        iv_course_image = findViewById(R.id.iv_course_image);

        playerView = (JWPlayerView) findViewById(R.id.videoView);
        new KeepScreenOnHandler(playerView, this.getWindow());

        tv_cost = findViewById(R.id.course_cost);
        tv_courseName = findViewById(R.id.course_name);
        tv_discount = findViewById(R.id.course_discount);
        tv_striked_cost = findViewById(R.id.course_striked_cost);
        tv_instructor_name = findViewById(R.id.course_instructor);
        ratingBar = findViewById(R.id.course_rating_bar);
        course_rating_number = findViewById(R.id.course_rating_number);
        course_number_of_ratings = findViewById(R.id.course_number_of_ratings);
        course_enrollments = findViewById(R.id.course_enrollments);

        wv_productDesc = findViewById(R.id.wv_productDesc);

        course_content_rv = findViewById(R.id.course_content_rv);

        if(getIntent() != null) {
            if(getIntent().hasExtra("type")) {
                if(getIntent().hasExtra("course_id")) {
                    course_id = getIntent().getStringExtra("course_id");
                }
                if(getIntent().getStringExtra("type").equals("premium_course")) {
                    course_url += course_id+"&user_id="+user.getUser_id();
                    fetchCourseDetails();
                } else {
                    course_url = "https://www.careeranna.com/api/fetchNotificationFreeCourseDetailApp.php?product_id="+course_id+"&user_id="+user.getUser_id();
                    fetchFreeCourseDetails();
                }
            }
        }

        already_enrolled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NotificationCourseActivity.this, MyCourses.class).putExtra( "fragment_name", "my_course"));
            }
        });

        add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(user == null) {
                    Toast.makeText(NotificationCourseActivity.this, "Please Sign In To Add To Cart", Toast.LENGTH_SHORT).show();
                    return;
                }

                String cart = Paper.book().read("cart1");

                /*
                If the cart is not null, we extract the already stored cart contents and add our new course
                in the cart.
                 */
                if (cart != null && !cart.isEmpty()) {

                    Log.i("details", cart);
                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<String>>() {
                    }.getType();
                    ArrayList<String> arrayList = gson.fromJson(cart, type);

                    for (String orderedCourse : arrayList) {

                        String coursear[] = orderedCourse.split(",");

                        if (course.getId().equals(coursear[0])) {

                            isAvailable = true;

                        }
                    }

                    if (isAvailable) {

                        Toast.makeText(NotificationCourseActivity.this, "Course Is Already Present In The Cart", Toast.LENGTH_SHORT).show();

                        return;
                    }

                    String courseString = course.getId() + "," + course.getPrice() + "," + course.getImageUrl() + "," + course.getName() + "," + course.getCategory_id() + "," + course.getPrice() + "," + course.getTotal_ratings() + "," + course.getRatings();
                    arrayList.add(courseString);
                    Paper.book().write("cart1", new Gson().toJson(arrayList));
                } else {

                    String courseString = course.getId() + "," + course.getPrice() + "," + course.getImageUrl() + "," + course.getName() + "," + course.getCategory_id() + "," + course.getPrice() + "," + course.getTotal_ratings() + "," + course.getRatings();
                    ArrayList<String> array = new ArrayList<>();
                    array.add(courseString);
                    Paper.book().write("cart1", new Gson().toJson(array));
                }
                Toast.makeText(NotificationCourseActivity.this, "Product Added To Cart", Toast.LENGTH_SHORT).show();
            }
        });

        enroll_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cache = Paper.book().read("user");
                if (cache != null && !cache.isEmpty()) {

                } else {
                    Toast.makeText(NotificationCourseActivity.this, "Please Sign Up To Enroll", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(NotificationCourseActivity.this, SignInActivity.class));
                    finish();
                    return;
                }
                /*
                If course if free, directly go to freeCourseCheckout()
                if it is paid, then go to buyCourse()
                 */
                final String price;
                price = course.getPrice();
                if (course.getPrice().equals("0")) {
                    freeCourseCheckout();
                } else {
                    paidCourseCheckout(price);
                }
            }
        });

        ratingBar.setRating(5f);

    }

    public void paidCourseCheckout(final String price) {

        String cart = Paper.book().read("cart1");

        if (cart != null && !cart.isEmpty()) {

            Log.i("details", cart);
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            ArrayList<String> arrayList = gson.fromJson(cart, type);

            for (String orderedCourse : arrayList) {

                String coursear[] = orderedCourse.split(",");

                if (course.getId().equals(coursear[0])) {

                    isAvailable = true;

                }
            }

            if (isAvailable) {
                Intent intent = new Intent(this, PaymentGateway.class);
                startActivity(intent);

                return;
            }

            String courseString = course.getId() + "," + course.getPrice() + "," + course.getImageUrl() + "," + course.getName() + "," + course.getCategory_id() + "," + course.getPrice() + "," + course.getTotal_ratings() + "," + course.getRatings();
            arrayList.add(courseString);
            Paper.book().write("cart1", new Gson().toJson(arrayList));
        } else {

            String courseString = course.getId() + "," + course.getPrice() + "," + course.getImageUrl() + "," + course.getName() + "," + course.getCategory_id() + "," + course.getPrice() + "," + course.getTotal_ratings() + "," + course.getRatings();
            ArrayList<String> array = new ArrayList<>();
            array.add(courseString);
            Paper.book().write("cart1", new Gson().toJson(array));
        }

        Intent intent = new Intent(this, PaymentGateway.class);
        startActivity(intent);

    }

    public void freeCourseCheckout() {
        progress_bar_layout.setVisibility(View.VISIBLE);
        RequestQueue requestQueue = Volley.newRequestQueue(NotificationCourseActivity.this);
        String url = "https://careeranna.com/api/addProduct.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        add_to_cart.setVisibility(GONE);
                        enroll_now.setVisibility(GONE);
                        already_enrolled.setVisibility(View.VISIBLE);
                        purchasedCourse = true;
                        progress_bar_layout.setVisibility(View.INVISIBLE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress_bar_layout.setVisibility(View.INVISIBLE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user", user.getUser_id());
                params.put("product", course.getId());
                params.put("category", course.getCategory_id());
                params.put("name", course.getName());
                params.put("image", course.getImageUrl());
                return params;
            }

        };
        requestQueue.add(stringRequest);

    }


    private void fetchFreeCourseDetails() {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                course_url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            course = new Course(
                                    response.getString("course_id"),
                                    response.getString("name"),
                                    "https://www.careeranna.com/" + response.getString("image").replace("\\", ""),
                                    response.getString("eid"),
                                    "0",
                                    "", "", "Free",
                                    response.getString("price"),
                                    response.getString("average_rating"),
                                    response.getString("total_rating"),
                                    response.getString("learner_count")
                            );
                            Drawable check = getApplicationContext().getResources().getDrawable(R.drawable.ic_check_circle_black_24dp);
                            ArrayList<Unit> mUnits = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("videos");
                            for(int i=0;i<jsonArray.length();i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                if(jsonObject.has("main")) {
                                    mUnits.add(new Unit(jsonObject.getString("main"), check));
                                }
                                if(jsonObject.has("heading")) {
                                    if (mUnits.size() == 0) {
                                        Unit unit = new Unit(course.getName(), check);
                                        mUnits.add(unit);
                                    }
                                    mUnits.get(mUnits.size()-1)
                                            .topics.add(new Topic(
                                            "",
                                            jsonObject.getString("heading"),
                                            jsonObject.getString("video_url"),
                                            jsonObject.getString("duration")

                                    ));
                                }
                            }

                            if(response.has("already_enrolled")) {
                                already_enrolled_in_course = response.getInt("already_enrolled") > 0 ? true : false;
                            }
                            course_content_rv.setLayoutManager(new LinearLayoutManager(NotificationCourseActivity.this, LinearLayoutManager.VERTICAL, false));

                            CourseContentAdapter courseContentAdapter = new CourseContentAdapter(mUnits, NotificationCourseActivity.this);
                            course_content_rv.setAdapter(courseContentAdapter);

                            courseContentAdapter.addNotificationCourse(NotificationCourseActivity.this);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progress_bar_layout.setVisibility(GONE);
                        setFreeCourseDetails();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress_bar_layout.setVisibility(GONE);
                        Toast.makeText(NotificationCourseActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonObjectRequest);

    }

    private void fetchCourseDetails() {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                course_url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                         try {
                             course = new Course(
                                     response.getString("product_id"),
                                     response.getString("course_name"),
                                     "https://www.careeranna.com/" + response.getString("product_image").replace("\\", ""),
                                     response.getString("exam_id"),
                                     response.getString("discount"),
                                     "", "", "paid",
                                     response.getString("price"),
                                     response.getString("average_rating"),
                                     response.getString("total_rating"),
                                     response.getString("learners_count")
                             );
                             Drawable check = getApplicationContext().getResources().getDrawable(R.drawable.ic_check_circle_black_24dp);
                             ArrayList<Unit> mUnits = new ArrayList<>();
                                 JSONArray jsonArray = response.getJSONArray("videos");
                                 for(int i=0;i<jsonArray.length();i++) {
                                     JSONObject jsonObject = jsonArray.getJSONObject(i);
                                     if(jsonObject.has("main")) {
                                         mUnits.add(new Unit(jsonObject.getString("main"), check));
                                     }
                                     if(jsonObject.has("heading")) {
                                         if (mUnits.size() == 0) {
                                             Unit unit = new Unit(course.getName(), check);
                                             mUnits.add(unit);
                                         }
                                         mUnits.get(mUnits.size()-1)
                                                 .topics.add(new Topic(
                                                 "",
                                                 jsonObject.getString("heading"),
                                                 jsonObject.getString("video_url"),
                                                 jsonObject.getString("duration")

                                         ));
                                     }
                                 }

                                 if(response.has("already_enrolled")) {
                                     already_enrolled_in_course = response.getInt("already_enrolled") > 0 ? true : false;
                                 }

                                 course_content_rv.setLayoutManager(new LinearLayoutManager(NotificationCourseActivity.this, LinearLayoutManager.VERTICAL, false));

                                 CourseContentAdapter courseContentAdapter = new CourseContentAdapter(mUnits, NotificationCourseActivity.this);
                                 course_content_rv.setAdapter(courseContentAdapter);

                                 courseContentAdapter.addNotificationCourse(NotificationCourseActivity.this);

                                 if(mUnits.size() == 0) {
                                     course_content_rv.setVisibility(GONE);
                                         JSONArray descArray = response.getJSONArray("mock_content");
                                         for (int i = 0; i < descArray.length(); i++) {
                                             JSONObject descObject = descArray.getJSONObject(i);
                                             String htmlDesc = "<h2 style=\"text-align:center;\" >Course Content</h2>"+descObject.getString("mock_test")
                                                     + descObject.getString("prev_paper");
                                             htmlDesc = htmlDesc.replaceAll("<div", "<div style=\"margin-top:10px\"");

                                             wv_productDesc.loadData(htmlDesc, "text/html; charset=utf-8", "UTF-8");
                                             wv_productDesc.setVisibility(View.VISIBLE);
                                             course_content_rv.setVisibility(GONE);

                                         }
                                 }

                         } catch (JSONException e) {
                             Toast.makeText(NotificationCourseActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();
                         }
                        progress_bar_layout.setVisibility(GONE);
                         setCourseDetails();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress_bar_layout.setVisibility(GONE);
                        Toast.makeText(NotificationCourseActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

    private void setFreeCourseDetails() {

        Glide.with(this)
                .load(course.getImageUrl())
                .into(iv_course_image);

        ratingBar.setRating(5f);

        tv_courseName.setText(course.getName());

        ratingBar.setRating(Float.valueOf(course.getRatings()));
        course_rating_number.setText(course.getRatings());
        course_number_of_ratings.setText("(" + NumberFormat.getNumberInstance(Locale.US).format(Integer.valueOf(course.getTotal_ratings())) + " Ratings)");
        if (course.getLearners_count() != null) {
            course_enrollments.setText(NumberFormat.getNumberInstance(Locale.US).format(Integer.valueOf(course.getLearners_count())));
        }

        tv_cost.setText("Free");
        tv_discount.setVisibility(GONE);
        tv_striked_cost.setVisibility(GONE);

        add_to_cart.setVisibility(GONE);

            if(already_enrolled_in_course) {
                enroll_now.setVisibility(GONE);
                already_enrolled.setVisibility(View.VISIBLE);
            }

    }

    public void playVideo(String videoUrl) {
        if(user == null) {
            startActivity(new Intent(NotificationCourseActivity.this, SignInActivity.class));
        } else {
            if (course.getType().equals("Free")) {
                if (!already_enrolled_in_course) {
                    freeCourseCheckout();
                }
                iv_course_image.setVisibility(View.GONE);
                playerView.setVisibility(View.VISIBLE);
                PlaylistItem playlistItem = new PlaylistItem.Builder()
                        .file(videoUrl)
                        .build();
                playerView.load(playlistItem);
                playerView.play();
            } else {
                if (already_enrolled_in_course) {
                    startActivity(new Intent(NotificationCourseActivity.this, MyCourses.class).putExtra("fragment_name", "my_course"));
                } else {
                    Toast.makeText(this, "Please Enroll Now To Access The Content!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void setCourseDetails() {

        Glide.with(this)
                .load(course.getImageUrl())
                .into(iv_course_image);

        ratingBar.setRating(5f);

        tv_courseName.setText(course.getName());

        ratingBar.setRating(Float.valueOf(course.getRatings()));
        course_rating_number.setText(course.getRatings());
        course_number_of_ratings.setText("(" + NumberFormat.getNumberInstance(Locale.US).format(Integer.valueOf(course.getTotal_ratings())) + " Ratings)");
        if (course.getLearners_count() != null) {
            course_enrollments.setText(NumberFormat.getNumberInstance(Locale.US).format(Integer.valueOf(course.getLearners_count())));
        }

        String price = "₹" + course.getPrice();
        tv_cost.setText(price);

        Float price_before_disc = Float.valueOf(course.getPrice_before_discount());
        Float price_after_disc = Float.valueOf(course.getPrice());
        Float discount = (price_before_disc - price_after_disc) * 100 / price_before_disc;
        tv_discount.setText(Math.round(discount) + "% Off");
        tv_striked_cost.setText("₹" + course.getPrice_before_discount());
        tv_striked_cost.setPaintFlags(tv_striked_cost.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(course.getName());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


            if(already_enrolled_in_course) {
                add_to_cart.setVisibility(GONE);
                enroll_now.setVisibility(GONE);
                already_enrolled.setVisibility(View.VISIBLE);
            }

    }

    @Override
    public void onDestroy() {
        playerView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        playerView.pause();
        super.onPause();
    }

    @Override
    public void onResume() {
        playerView.play();
        super.onResume();
    }

    @Override
    public void onFullscreen(FullscreenEvent fullscreenEvent) {

        if (fullscreenEvent.getFullscreen()) {
            //If fullscreen, remove the action bar
            if(getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }
        } else {
            //If not fullscreen, show the action bar
            if(getSupportActionBar() != null) {
                getSupportActionBar().show();
            }
        }

    }
}
