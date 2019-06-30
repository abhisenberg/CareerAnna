package com.careeranna.careeranna.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.careeranna.careeranna.JW_Player_Files.KeepScreenOnHandler;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.adapter.CourseContentAdapter;
import com.careeranna.careeranna.data.Course;
import com.careeranna.careeranna.data.MyPaidCourse;
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

public class PurchaseCourseActivity extends AppCompatActivity implements VideoPlayerEvents.OnFullscreenListener{

    public static final String TAG = "PurchaseCourseDetail";

    Course course;

    User user;

    ArrayList<MyPaidCourse> purchasedPaidCourses;

    Button enroll_now, add_to_cart, already_enrolled;

    Boolean isAvailable = false;

    android.app.AlertDialog.Builder builder;

    android.app.AlertDialog alert;

    ProgressBar progressBar;
    private JWPlayerView playerView;

    ProgressDialog progressDialog;

    WebView wv_productDescription;

    RecyclerView course_content_rv;

    RelativeLayout relativeLayout;

    TextView tv_courseName,
            tv_instructor_name,
            tv_cost,
            tv_striked_cost,
            tv_discount,
            course_rating_number,
            course_number_of_ratings,
            course_enrollments;

    RatingBar ratingBar;

    ImageView iv_demoCourseImage;

    Boolean purchasedCourse = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_course);

        Paper.init(this);

        Intent intent = getIntent();

        progressBar = findViewById(R.id.progress_bar_course);
        enroll_now = findViewById(R.id.enroll_now);
        add_to_cart = findViewById(R.id.add_to_cart);
        course_content_rv = findViewById(R.id.course_content_rv);
        already_enrolled = findViewById(R.id.already_enrolled);

        tv_cost = findViewById(R.id.course_cost);
        tv_courseName = findViewById(R.id.course_name);
        tv_discount = findViewById(R.id.course_discount);
        tv_striked_cost = findViewById(R.id.course_striked_cost);
        tv_instructor_name = findViewById(R.id.course_instructor);
        ratingBar = findViewById(R.id.course_rating_bar);
        course_rating_number = findViewById(R.id.course_rating_number);
        course_number_of_ratings = findViewById(R.id.course_number_of_ratings);
        course_enrollments = findViewById(R.id.course_enrollments);
        iv_demoCourseImage = findViewById(R.id.iv_demoCourseImage);

        relativeLayout = findViewById(R.id.relative_loading);

        wv_productDescription = findViewById(R.id.wv_productDesc);
        wv_productDescription.setVisibility(GONE);

        course = (Course) intent.getSerializableExtra("Course");

        if(getIntent().hasExtra("my_paid_course")) {
            purchasedPaidCourses = (ArrayList<MyPaidCourse>) getIntent().getSerializableExtra("my_paid_course");
            for(MyPaidCourse myPaidCourse: purchasedPaidCourses) {
                if(myPaidCourse.getProduct_id().equalsIgnoreCase(course.getId())){
                    add_to_cart.setVisibility(GONE);
                    enroll_now.setVisibility(GONE);
                    already_enrolled.setVisibility(View.VISIBLE);
                    purchasedCourse = true;
                }
            }

        }

        playerView = (JWPlayerView) findViewById(R.id.videoView);
        new KeepScreenOnHandler(playerView, this.getWindow());

        String cache = Paper.book().read("user");
        if (cache != null && !cache.isEmpty()) {
            user = new Gson().fromJson(cache, User.class);
        } else {
            playerView.setVisibility(GONE);
        }
        String cart = Paper.book().read("cart1");

        if (cart != null && !cart.isEmpty()) {

            Log.i("details", cart);

            Gson gson = new Gson();

            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();

            ArrayList<String> arrayList = gson.fromJson(cart, type);

            for (String orderedCourse : arrayList) {

                String coursearray[] = orderedCourse.split(",");

                if (course.getId().equals(coursearray[0])) {

                    isAvailable = true;

                }
            }

            if (isAvailable) {

                builder = new android.app.AlertDialog.Builder(PurchaseCourseActivity.this);
                builder.setTitle("Already In The Cart");
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setCancelable(false);
                builder.setMessage(getString(R.string.course_already_in_cart))
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alert.dismiss();
                                startActivity(new Intent(PurchaseCourseActivity.this, MyCourses.class).putExtra("fragment_name", "cart"));
                                finish();
                            }
                        });
                alert = builder.create();
                alert.show();

            }

        }

        already_enrolled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PurchaseCourseActivity.this, MyCourses.class).putExtra( "fragment_name", "my_course"));
            }
        });

        add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(user == null) {
                    Toast.makeText(PurchaseCourseActivity.this, "Please Sign In To Add To Cart", Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(PurchaseCourseActivity.this, "Course Is Already Present In The Cart", Toast.LENGTH_SHORT).show();

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
                Toast.makeText(PurchaseCourseActivity.this, "Product Added To Cart", Toast.LENGTH_SHORT).show();
            }
        });

        enroll_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cache = Paper.book().read("user");
                if (cache != null && !cache.isEmpty()) {

                } else {
                    Toast.makeText(PurchaseCourseActivity.this, "Please Sign Up To Enroll", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PurchaseCourseActivity.this, SignInActivity.class));
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
                } else
                    paidCourseCheckout(price);
            }
        });

        ratingBar.setRating(5f);


        ratingBar.setRating(Float.valueOf(course.getRatings()));
        course_rating_number.setText(course.getRatings());
        course_number_of_ratings.setText("(" + NumberFormat.getNumberInstance(Locale.US).format(Integer.valueOf(course.getTotal_ratings())) + " Ratings)");
        if (course.getLearners_count() != null) {
            course_enrollments.setText(NumberFormat.getNumberInstance(Locale.US).format(Integer.valueOf(course.getLearners_count())));
        }
        fetchUnit();
        iv_demoCourseImage.setVisibility(View.VISIBLE);

        Glide.with(this)
                .load(course.getImageUrl())
                .into(iv_demoCourseImage);
        setCourseViews();
    }

    private void setCourseViews() {
        Uri uri;

            /*
            If "course" is not null, it is video based course.
             */
        uri = Uri.parse(course.getDemo_url());

        tv_courseName.setText(course.getName());
        tv_instructor_name.setText("By Careeranna");
        if (course.getPrice().equals("0")) {
            tv_cost.setText("Free");
            tv_discount.setText("");
            tv_striked_cost.setText("");
            add_to_cart.setVisibility(GONE);
        } else {

            playerView.setVisibility(GONE);
            String price = "₹" + course.getPrice();
            tv_cost.setText(price);

            Float price_before_disc = Float.valueOf(course.getPrice_before_discount());
            Float price_after_disc = Float.valueOf(course.getPrice());
            Float discount = (price_before_disc - price_after_disc) * 100 / price_before_disc;
            tv_discount.setText(Math.round(discount) + "% Off");
            tv_striked_cost.setText("₹" + course.getPrice_before_discount());
            tv_striked_cost.setPaintFlags(tv_striked_cost.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        }
            /*
            Setting striked through text of price before discount
             */

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(course.getName());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


    }

    private void fetchUnit() {
        final String id = course.getId();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://careeranna.com/api/getCourseContentApp.php?product_id=" + id;
        Log.i("urlInsidePurchaseCourse", url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("No results")) {
                            /*
                            If no product description is found from this table, then try it from the other
                            table.
                             */

                            RequestQueue requestQueue1 = Volley.newRequestQueue(PurchaseCourseActivity.this);
                            String url = "https://careeranna.com/api/getProductDescription.php?id=" + id;

                            StringRequest stringRequest1 = new StringRequest(Request.Method.GET,
                                    url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            if (response.equals("null")) {
                                                /*
                                                No description found.
                                                 */
                                            } else {
                                                Log.d(TAG, "ProductDescription = " + response);
//                                                Toast.makeText(PurchaseCourseDetail.this, response.substring(0,30), Toast.LENGTH_SHORT).show();

                                                try {
                                                    JSONArray descArray = new JSONArray(response);
                                                    for (int i = 0; i < descArray.length(); i++) {
                                                        JSONObject descObject = descArray.getJSONObject(i);
                                                        String htmlDesc = "<h2 style=\"text-align:center;\" >Course Content</h2>"+descObject.getString("mock_test")
                                                                + descObject.getString("prev_paper");
                                                        htmlDesc = htmlDesc.replaceAll("<div", "<div style=\"margin-top:10px\"");
//                                                        htmlDesc = htmlDesc.replaceAll("\">", "\">○ ");
                                                        Log.d(TAG, "HtmlResponse: " + htmlDesc);

                                                        wv_productDescription.loadData(htmlDesc, "text/html; charset=utf-8", "UTF-8");
                                                        wv_productDescription.setVisibility(View.VISIBLE);
                                                        course_content_rv.setVisibility(GONE);

                                                    }
                                                } catch (JSONException e) {
                                                    Log.d(TAG, "HtmlResponseError: ");
                                                    e.printStackTrace();
                                                }
                                                relativeLayout.setVisibility(GONE);
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.d(TAG, "onErrorResponse_2nd try: ");
                                            relativeLayout.setVisibility(GONE);
                                        }
                                    }
                            );

                            requestQueue1.add(stringRequest1);

                        } else {
                            Drawable check = getApplicationContext().getResources().getDrawable(R.drawable.ic_check_circle_black_24dp);
                            ArrayList<Unit> mUnits = new ArrayList<>();
                            try {
                                JSONArray jsonArray = new JSONArray(response);
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
                                course_content_rv.setLayoutManager(new LinearLayoutManager(PurchaseCourseActivity.this, LinearLayoutManager.VERTICAL, false));

                                CourseContentAdapter courseContentAdapter = new CourseContentAdapter(mUnits, PurchaseCourseActivity.this);
                                course_content_rv.setAdapter(courseContentAdapter);
                                courseContentAdapter.addPurchaseCourse(PurchaseCourseActivity.this);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            /*
                            addCourseUnit call
                            pass the course
                             */


                            relativeLayout.setVisibility(GONE);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.networkResponse);
                Toast.makeText(PurchaseCourseActivity.this, "Something Went Wrong Try Again", Toast.LENGTH_SHORT).show();
                relativeLayout.setVisibility(GONE);
            }
        });
        requestQueue.add(stringRequest);
    }



    public void freeCourseCheckout() {
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue requestQueue = Volley.newRequestQueue(PurchaseCourseActivity.this);
        String url = "https://careeranna.com/api/addProduct.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        add_to_cart.setVisibility(GONE);
                        enroll_now.setVisibility(GONE);
                        already_enrolled.setVisibility(View.VISIBLE);
                        purchasedCourse = true;
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.INVISIBLE);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }}

    public void playVideo(String videoUrl) {
        if(user == null) {
            startActivity(new Intent(PurchaseCourseActivity.this, SignInActivity.class));
        } else {
            if (course.getType().equals("Free")) {
                if (!purchasedCourse) {
                    freeCourseCheckout();
                }
                iv_demoCourseImage.setVisibility(View.GONE);
                playerView.setVisibility(View.VISIBLE);
                PlaylistItem playlistItem = new PlaylistItem.Builder()
                        .file(videoUrl)
                        .build();
                playerView.load(playlistItem);
                playerView.play();
            } else {
                if (purchasedCourse) {
                    startActivity(new Intent(PurchaseCourseActivity.this, MyCourses.class).putExtra("fragment_name", "my_course"));
                } else {
                    Toast.makeText(this, "Please Enroll Now To Access The Content!", Toast.LENGTH_SHORT).show();
                }
            }
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
