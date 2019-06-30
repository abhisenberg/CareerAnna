package com.careeranna.careeranna.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.adapter.CourseContentAdapter;
import com.careeranna.careeranna.adapter.ExpandableListAdapterForNestedScroll;
import com.careeranna.careeranna.data.Course;
import com.careeranna.careeranna.data.ExamPrep;
import com.careeranna.careeranna.data.OrderedCourse;
import com.careeranna.careeranna.data.OrderedList;
import com.careeranna.careeranna.data.Topic;
import com.careeranna.careeranna.data.Unit;
import com.careeranna.careeranna.data.User;
import com.careeranna.careeranna.misc.ExpandableListViewForNestedScroll;
import com.careeranna.careeranna.user.SignUp;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.longtailvideo.jwplayer.events.FullscreenEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.paperdb.Paper;

import static android.view.View.GONE;

@SuppressWarnings("HardCodedStringLiteral")
public class PurchaseCourseDetail extends AppCompatActivity implements VideoPlayerEvents.OnFullscreenListener {

    public static final String TAG = "PurchaseCourseDetail";

    private static final Pattern REMOVE_TAGS = Pattern.compile("<.+?>");

    private static final Pattern REMOVE_TAGS1 = Pattern.compile("&.+;");

    TextView tv_courseName, tv_instructor_name, tv_star_rating, tv_user_ratings, tv_cost, tv_striked_cost,
            tv_discount, tv_enrollments, course_rating_number, course_number_of_ratings,course_enrollments;

    RatingBar ratingBar;
//    TextView price,desc,name;
//
//    TextView description;

    FloatingActionButton addTocart;

    Button enroll_now, add_to_cart;

    RecyclerView course_content_rv;

//    JWPlayerView playerView;

    AppBarLayout appBarLayout;


    ArrayList<OrderedCourse> orderedCourse;

    OrderedList orderedList;

    Course course;

    ProgressDialog progressDialog;

    android.app.AlertDialog.Builder builder;

    Snackbar snackbar;

    android.app.AlertDialog alert;

    ExamPrep examPrep;

    ProgressBar progressBar;

    MediaController mediaController;

    boolean isAvailable;

    AlertDialog.Builder mBuilder;

    AlertDialog alertDialog;

    User user;

    ExpandableListAdapterForNestedScroll listAdapter;

    ExpandableListViewForNestedScroll expandableListView;

    WebView webView;
    WebView wv_productDescription;

    ImageView iv_demoCourseImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_course_detail);

        Paper.init(this);

        tv_cost = findViewById(R.id.course_cost);
        tv_courseName = findViewById(R.id.course_name);
        tv_discount = findViewById(R.id.course_discount);
        tv_enrollments = findViewById(R.id.course_enrollments);
        tv_star_rating = findViewById(R.id.course_rating_number);
        tv_user_ratings = findViewById(R.id.course_number_of_ratings);
        tv_striked_cost = findViewById(R.id.course_striked_cost);
        tv_instructor_name = findViewById(R.id.course_instructor);
        ratingBar = findViewById(R.id.course_rating_bar);
        webView = findViewById(R.id.wv_demoVideo);
        appBarLayout = findViewById(R.id.app_bar_layout);
        iv_demoCourseImage = findViewById(R.id.iv_demoCourseImage);
        enroll_now = findViewById(R.id.enroll_now);
        add_to_cart = findViewById(R.id.add_to_cart);
        course_content_rv = findViewById(R.id.course_content_rv);
        course_rating_number = findViewById(R.id.course_rating_number);
        course_number_of_ratings = findViewById(R.id.course_number_of_ratings);
        course_enrollments = findViewById(R.id.course_enrollments);

        expandableListView = findViewById(R.id.purchaseCourse_expandableUnit);
        progressBar = findViewById(R.id.progress_bar_course);
//        price = findViewById(R.id.course_price);

//        playerView =  findViewById(R.id.playerView);
//        description = findViewById(R.id.course_description);
        addTocart = findViewById(R.id.btn_cart);
        wv_productDescription = findViewById(R.id.wv_productDesc);
        wv_productDescription.setVisibility(GONE);

        Intent intent = getIntent();
        ratingBar.setRating(5f);

        course = (Course) intent.getSerializableExtra("Course");
        if (course == null) {
            /*
            DON'T FETCH UNITS HERE BECAUSE THIS IS EXAM_PREP COURSE
             */
            Log.d(TAG, "onCreate: course is null");
            fetchUnit();
//
            examPrep = (ExamPrep) intent.getSerializableExtra("Examp");
//            description.append("\n"+removeTags(examPrep.getDesc()).replace("-", "\n"));

        } else {
            Log.d(TAG, "Course id =  " + course.getId());
            fetchUnit();
            ratingBar.setRating(Float.valueOf(course.getRatings()));
            course_rating_number.setText(course.getRatings());
            course_number_of_ratings.setText("(" + NumberFormat.getNumberInstance(Locale.US).format(Integer.valueOf(course.getTotal_ratings())) + " Ratings)");
            if(course.getLearners_count() != null) {
                course_enrollments.setText(NumberFormat.getNumberInstance(Locale.US).format(Integer.valueOf(course.getLearners_count())));
            }
//            description.append("\n"+removeTags(course.getDesc()).replace("-", "\n"));
        }

        initializeWebView();

//        playerView.addOnFullscreenListener(this);
        String cart = Paper.book().read("cart1");


        /*
        TODO: Change the color of the stars
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

                builder = new android.app.AlertDialog.Builder(PurchaseCourseDetail.this);
                builder.setTitle("Already In The Cart");
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setCancelable(false);
                builder.setMessage(getString(R.string.course_already_in_cart))
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alert.dismiss();
                                startActivity(new Intent(PurchaseCourseDetail.this, MyCourses.class));
                                finish();
                            }
                        });
                alert = builder.create();
                alert.show();

            }

//        playerView.addOnFullscreenListener(this);
//        new KeepScreenOnHandler(playerView, getWindow());
        }

        setCourseViews();

        addTocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cache = Paper.book().read("user");
                if (cache != null && !cache.isEmpty()) {
                } else {
                    Toast.makeText(PurchaseCourseDetail.this, getString(R.string.please_register_to_continue), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PurchaseCourseDetail.this, SignUp.class));
                    finish();
                }
                /*
                If course if free, directly go to freeCourseCheckout()
                if it is paid, then go to buyCourse()
                 */
                if (course != null) {
                    if (course.getPrice().equals("0")) {
                        freeCourseCheckout();
                    } else
                        buyCourse();
                } else {
                    if (examPrep.getPrice().equals("0")) {
                        freeCourseCheckout();
                    } else
                        buyCourse();
                }
            }
        });

        add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                        Toast.makeText(PurchaseCourseDetail.this, "Course Is Already Present In The Cart", Toast.LENGTH_SHORT).show();

                        return;
                    }

                    String courseString = course.getId() + "," + course.getPrice() + "," + course.getImageUrl() + "," + course.getName() + "," + course.getCategory_id() +","+course.getPrice() +","+course.getTotal_ratings()+","+course.getRatings();
                    arrayList.add(courseString);
                    Paper.book().write("cart1", new Gson().toJson(arrayList));
                } else {

                    String courseString = course.getId() + "," + course.getPrice() + "," + course.getImageUrl() + "," + course.getName() + "," + course.getCategory_id() +","+course.getPrice() +","+course.getTotal_ratings()+","+course.getRatings();
                    ArrayList<String> array = new ArrayList<>();
                    array.add(courseString);
                    Paper.book().write("cart1", new Gson().toJson(array));
                }
                Toast.makeText(PurchaseCourseDetail.this, "Product Added To Cart", Toast.LENGTH_SHORT).show();
            }
        });

        enroll_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cache = Paper.book().read("user");
                if (cache != null && !cache.isEmpty()) {
                } else {
                    Toast.makeText(PurchaseCourseDetail.this, getString(R.string.please_register_to_continue), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PurchaseCourseDetail.this, SignUp.class));
                    finish();
                }
                /*
                If course if free, directly go to freeCourseCheckout()
                if it is paid, then go to buyCourse()
                 */
                if (course != null) {
                    final String price;
                    price = course.getPrice();
                    if (course.getPrice().equals("0")) {
                        freeCourseCheckout();
                    } else
                        paidCourseCheckout(price);
                } else {
                    final String price;
                    price = examPrep.getPrice();
                    if (examPrep.getPrice().equals("0")) {
                        freeCourseCheckout();
                    } else
                        paidCourseCheckout(price);
                }
            }
        });

        String cache = Paper.book().read("user");
        if (cache != null && !cache.isEmpty()) {
            user = new Gson().fromJson(cache, User.class);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initializeWebView() {
        String videoURl = course.getDemo_url();
        if (videoURl.equals("null") || videoURl.isEmpty()) {
            webView.setVisibility(View.INVISIBLE);
            iv_demoCourseImage.setVisibility(View.VISIBLE);

            Glide.with(this)
                    .load(course.getImageUrl())
                    .into(iv_demoCourseImage);
            return;
        }

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
        });
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        String htmlPlayer = "<html><body style=\"margin : 0; padding : 0 \"><iframe width=\"100%\" height=\"100%\" src=\"" + videoURl +
                "\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
        Log.d(TAG, "demoURL: " + videoURl);
        Log.d(TAG, "html:  " + htmlPlayer);
        webView.loadData(htmlPlayer, "text/html", "utf-8");
    }

    public static String removeTags(String string) {
        if (string == null || string.length() == 0) {
            return string;
        }

        Matcher m = REMOVE_TAGS.matcher(string);
        String tag = m.replaceAll("");
        Matcher m1 = REMOVE_TAGS1.matcher(tag);
        return m1.replaceAll("");
    }


    @Override
    public void onFullscreen(FullscreenEvent fullscreenEvent) {
        if (fullscreenEvent.getFullscreen()) {
            //If fullscreen, remove action bar
        } else {
            //If not fullscreen, show action bar
        }
    }


    @Override
    protected void onDestroy() {
//        playerView.onDestroy();
        super.onDestroy();
    }

    private void showActionBar() {
        Log.d(TAG, "showActionBar: ");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG, "onConfigurationChanged " + (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE));

//        playerView.setFullscreen(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE, true);
        super.onConfigurationChanged(newConfig);
    }


    @Override
    protected void onPause() {
//        playerView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
//        playerView.onResume();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
//        if(playerView.getFullscreen())
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        else
//            super.onBackPressed();
        super.onBackPressed();

    }

    private void buyCourse() {
        mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle("Are You Sure You Want To Buy This Course?");
        mBuilder.setCancelable(true);

        final String price;
        if (course != null) {
            mBuilder.setMessage("Course Name : " + course.getName() + " \n Price : ₹" + course.getPrice());
            price = course.getPrice();
        } else {
            mBuilder.setMessage("Course Name : " + examPrep.getName() + " \n Price : ₹" + examPrep.getPrice());
            price = examPrep.getPrice();
        }
        /*
        setNegativeButton is the left button, here we are showing "Add to Cart" option instead of cancel
        and setPositiveButton is the right button, in which we are showing "Direct Checkout".

        setNegativeButton is NOT cancel button here.
         */

        mBuilder.setPositiveButton("Check Out", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                paidCourseCheckout(price);
            }
        });

        mBuilder.setNegativeButton("Add To Cart", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
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

                        Toast.makeText(PurchaseCourseDetail.this, "Course Is Already Present In The Cart", Toast.LENGTH_SHORT).show();

                        return;
                    }

                    String courseString = course.getId() + "," + course.getPrice() + "," + course.getImageUrl() + "," + course.getName() + "," + course.getCategory_id() +","+course.getPrice() +","+course.getTotal_ratings()+","+course.getRatings();
                    arrayList.add(courseString);
                    Paper.book().write("cart1", new Gson().toJson(arrayList));
                } else {

                    String courseString = course.getId() + "," + course.getPrice() + "," + course.getImageUrl() + "," + course.getName() + "," + course.getCategory_id() +","+course.getPrice() +","+course.getTotal_ratings()+","+course.getRatings();
                    ArrayList<String> array = new ArrayList<>();
                    array.add(courseString);
                    Paper.book().write("cart1", new Gson().toJson(array));
                }
                Toast.makeText(PurchaseCourseDetail.this, "Product Added To Cart", Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog = mBuilder.show();
    }

    private void freeCourseCheckout() {
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue requestQueue = Volley.newRequestQueue(PurchaseCourseDetail.this);
        String url = "https://careeranna.com/api/addProduct.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(PurchaseCourseDetail.this, response, Toast.LENGTH_SHORT).show();
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
                if (course != null) {
                    params.put("product", course.getId());
                    params.put("category", "15");
                    params.put("name", course.getName());
                    params.put("image", course.getImageUrl());
                } else {
                    params.put("product", examPrep.getId());
                    params.put("category", examPrep.getCategory_id());
                    params.put("name", examPrep.getName());
                    params.put("image", examPrep.getImageUrl());
                }
                return params;
            }

        };
        requestQueue.add(stringRequest);

    }

    private void setCourseViews() {
        Uri uri;
        if (course == null) {
            /*
            Check whether the course type is a video based course or
            exam prep. if "course" is null, it is exam prep.
             */
            uri = Uri.parse(examPrep.getDemo_url());
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(examPrep.getName());
            }
        } else {
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
                String price = "₹" + course.getPrice();
                tv_cost.setText(price);

                Float price_before_disc = Float.valueOf(course.getPrice_before_discount());
                Float price_after_disc = Float.valueOf(course.getPrice());
                Float discount = (price_before_disc - price_after_disc) * 100 / price_before_disc;
                tv_discount.setText(String.format("%.1f", discount) + "% Off including taxes");
                tv_striked_cost.setText("₹" + course.getPrice_before_discount());
                tv_striked_cost.setPaintFlags(tv_striked_cost.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            }
            /*
            Setting striked through text of price before discount
             */

            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(course.getName());
            }
        }


//        PlaylistItem playlistItem = new PlaylistItem.Builder()
//                .file(uri.toString())
//                .build();
//                PlaylistItem playlistItem = new PlaylistItem.Builder()
//                .file("http://yt-dash-mse-test.commondatastorage.googleapis.com/media/car-20120827-87.mp4")
//                .build();
//        playerView.load(playlistItem);
    }

    private void fetchUnit() {
        final String id = course.getId();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Videos .. ");
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://careeranna.com/api/getVideosWithNames.php?product_id=" + id;
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

                            expandableListView.setVisibility(GONE);

                            RequestQueue requestQueue1 = Volley.newRequestQueue(PurchaseCourseDetail.this);
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
                                                        String htmlDesc = descObject.getString("mock_test")
                                                                + descObject.getString("prev_paper");
                                                        htmlDesc = htmlDesc.replaceAll("<div", "<div style=\"margin-top:10px\"");
//                                                        htmlDesc = htmlDesc.replaceAll("\">", "\">○ ");
                                                        Log.d(TAG, "HtmlResponse: " + htmlDesc);

                                                        wv_productDescription.loadData(htmlDesc, "text/html; charset=utf-8", "UTF-8");
                                                        wv_productDescription.setVisibility(View.VISIBLE);

                                                    }
                                                } catch (JSONException e) {
                                                    Log.d(TAG, "HtmlResponseError: ");
                                                    e.printStackTrace();
                                                }
                                                progressDialog.dismiss();
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.d(TAG, "onErrorResponse_2nd try: ");
                                        }
                                    }
                            );

                            requestQueue1.add(stringRequest1);

                        } else {
                            ArrayList<String> course = new ArrayList<>();
                            try {
                                Log.d(TAG, "response = " + response);
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("content");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    course.add((String) jsonArray.get(i));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            /*
                            addCourseUnit call
                            pass the course
                             */
                            addCourseUnits(course);
                            progressDialog.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.networkResponse);
                Toast.makeText(PurchaseCourseDetail.this, "Error Occured", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
        requestQueue.add(stringRequest);
    }

    public void addCourseUnits(ArrayList<String> course_list) {
        Log.d(TAG, "addCourseUnits: " + course_list.size());
        Drawable check = getApplicationContext().getResources().getDrawable(R.drawable.ic_check_circle_black_24dp);
        Drawable unCheck = getApplicationContext().getResources().getDrawable(R.drawable.ic_check_circle_black1_24dp);
        ArrayList<Unit> mUnits = new ArrayList<>();

        Log.d(TAG, "units size = " + mUnits.size());
        for (String unitsname : course_list) {
            if (unitsname.length() > 0) {
                char c = unitsname.charAt(0);
                if (c != '$') {
                    Unit unit = new Unit(unitsname, check);
                    mUnits.add(unit);
                } else {
                    String array[] = unitsname.split(",url");
                    if(mUnits.size() == 0) {
                        if(course != null) {
                            Unit unit = new Unit(course.getName(), check);
                            mUnits.add(unit);
                        } else {
                            Unit unit = new Unit(examPrep.getName(), check);
                            mUnits.add(unit);
                        }
                    }
                    if (array.length == 1) {
                        mUnits.get(mUnits.size() - 1).topics.add(new Topic(array[0].substring(1), ""));
                    } else {
                        mUnits.get(mUnits.size() - 1).topics.add(new Topic(array[0].substring(1), array[1]));
                    }
                }


     /*       if (!mUnits.isEmpty()) {
                if (!mUnits.get(0).topics.isEmpty()) {
                    if (!mUnits.get(0).topics.get(0).getVideos().equals("")) {
                        playVideo(mUnits.get(0).topics.get(0).getVideos());
                    }
                }
            }
     */
                course_content_rv.setLayoutManager(new LinearLayoutManager(PurchaseCourseDetail.this, LinearLayoutManager.VERTICAL, false));

                CourseContentAdapter courseContentAdapter = new CourseContentAdapter(mUnits, this);
                course_content_rv.setAdapter(courseContentAdapter);

                listAdapter = new ExpandableListAdapterForNestedScroll(getApplicationContext(), mUnits, expandableListView);
                expandableListView.setAdapter(listAdapter);
            }
        }

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

            String courseString = course.getId() + "," + course.getPrice() + "," + course.getImageUrl() + "," + course.getName() + "," + course.getCategory_id() +","+course.getPrice() +","+course.getTotal_ratings()+","+course.getRatings();
            arrayList.add(courseString);
            Paper.book().write("cart1", new Gson().toJson(arrayList));
        } else {

            String courseString = course.getId() + "," + course.getPrice() + "," + course.getImageUrl() + "," + course.getName() + "," + course.getCategory_id() +","+course.getPrice() +","+course.getTotal_ratings()+","+course.getRatings();
            ArrayList<String> array = new ArrayList<>();
            array.add(courseString);
            Paper.book().write("cart1", new Gson().toJson(array));
        }

        Intent intent = new Intent(this, PaymentGateway.class);
        startActivity(intent);

    }

}

