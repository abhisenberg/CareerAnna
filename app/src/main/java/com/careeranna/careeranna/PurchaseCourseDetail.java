package com.careeranna.careeranna;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.careeranna.careeranna.JW_Player_Files.KeepScreenOnHandler;
import com.careeranna.careeranna.adapter.ExpandableList_Adapter;
import com.careeranna.careeranna.data.Course;
import com.careeranna.careeranna.data.ExamPrep;
import com.careeranna.careeranna.data.OrderedCourse;
import com.careeranna.careeranna.data.OrderedList;
import com.careeranna.careeranna.data.Topic;
import com.careeranna.careeranna.data.Unit;
import com.careeranna.careeranna.data.User;
import com.careeranna.careeranna.dummy_data.CourseDummyData;
import com.careeranna.careeranna.user.SignUp;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.events.FullscreenEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;
import com.payu.india.CallBackHandler.OnetapCallback;
import com.payu.india.Model.PaymentParams;
import com.payu.india.Model.PayuConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.paperdb.Paper;

import static com.facebook.FacebookSdk.getApplicationContext;

public class PurchaseCourseDetail extends AppCompatActivity implements VideoPlayerEvents.OnFullscreenListener{

    public static final String TAG = "PurchaseCourses";

    private static final Pattern REMOVE_TAGS = Pattern.compile("<.+?>");

    private static final Pattern REMOVE_TAGS1 = Pattern.compile("&.+;");

    TextView tv_courseName, tv_instructor_name , tv_star_rating, tv_user_ratings, tv_cost, tv_striked_cost,
    tv_discount, tv_enrollments;

    RatingBar ratingBar;
//    TextView price,desc,name;
//
//    TextView description;

    FloatingActionButton addTocart;

    JWPlayerView playerView;

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

    ExpandableList_Adapter listAdapter;

    ExpandableListView expandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_course_detail_2);

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

        expandableListView = findViewById(R.id.purchaseCourse_expandableUnit);
        progressBar = findViewById(R.id.progress_bar_course);
//        price = findViewById(R.id.course_price);
        playerView =  findViewById(R.id.playerView);
//        description = findViewById(R.id.course_description);
        addTocart = findViewById(R.id.btn_cart);

        Intent intent = getIntent();

        course = (Course) intent.getSerializableExtra("Course");

        if(course == null) {
            /*
            DON'T FETCH UNITS HERE BECAUSE THIS IS EXAM_PREP COURSE
             */
            examPrep = (ExamPrep) intent.getSerializableExtra("Examp");
//            description.append("\n"+removeTags(examPrep.getDesc()).replace("-", "\n"));

        } else {
            Log.d(TAG, "Course id =  "+course.getId());
            fetchUnit();
//            description.append("\n"+removeTags(course.getDesc()).replace("-", "\n"));
        }

        playerView.addOnFullscreenListener(this);
        String cart = Paper.book().read("cart");

        ratingBar.setRating(5f);
        /*
        TODO: Change the color of the stars
         */
        if(cart != null && !cart.isEmpty()) {

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

            if(isAvailable) {

                builder = new android.app.AlertDialog.Builder(PurchaseCourseDetail.this);
                builder.setTitle("Already In The Cart");
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setCancelable(false);
                builder.setMessage("Please Remove In Your Cart Or Go To Cart ")
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
        }

        playerView.addOnFullscreenListener(this);
        new KeepScreenOnHandler(playerView, getWindow());

        setCourse();

        addTocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buyCourse();
            }
        });


        String cache = Paper.book().read("user");
        if(cache != null && !cache.isEmpty()) {
            user =  new Gson().fromJson(cache, User.class);
        }


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
        if(fullscreenEvent.getFullscreen()){
            //If fullscreen, remove action bar
        } else {
            //If not fullscreen, show action bar
        }
    }

    @Override
    protected void onDestroy() {
        playerView.onDestroy();
        super.onDestroy();
    }

    private void showActionBar() {
        Log.d(TAG, "showActionBar: ");
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.show();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG, "onConfigurationChanged "+(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE));

        playerView.setFullscreen(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE, true);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPause() {
        playerView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        playerView.onResume();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if(playerView.getFullscreen())
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        else
            super.onBackPressed();
    }

    private void buyCourse() {

        mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle("Are You Sure You Want To Buy This Course");
        mBuilder.setCancelable(false);
        if(course != null ) {
            mBuilder.setMessage("Course Name : " + course.getName() + " \n Price : " + course.getPrice());
        } else {
            mBuilder.setMessage("Course Name : " + examPrep.getName() + " \n Price : " + examPrep.getPrice());
        }
        mBuilder.setPositiveButton("Check Out", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                buyCourseInside();
            }
        });

        mBuilder.setNegativeButton("Add To Cart", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String cart = Paper.book().read("cart");;

                if(cart != null && !cart.isEmpty()) {

                    Log.i( "details",   cart);

                    Gson gson = new Gson();

                    Type type = new TypeToken<ArrayList<String>>() {}.getType();

                    ArrayList<String> arrayList = gson.fromJson(cart, type);

                    String courseString = course.getId() + "," + course.getPrice() + "," + course.getImageUrl() + "," + course.getName() + "," + course.getCategory_id();

                    arrayList.add(courseString);

                    Paper.book().write("cart", new Gson().toJson(arrayList));

                } else {

                    String courseString = course.getId()+","+course.getPrice()+","+course.getImageUrl()+","+course.getName()+","+course.getCategory_id();

                    ArrayList<String> array = new ArrayList<>();

                    array.add(courseString);

                    Paper.book().write("cart", new Gson().toJson(array));
                }

                startActivity(new Intent(PurchaseCourseDetail.this, MyCourses.class));
                finish();
            }
        });

        alertDialog = mBuilder.show();
    }

    private void buyCourseInside() {
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue requestQueue =Volley.newRequestQueue(PurchaseCourseDetail.this);
        String url = "https://careeranna.com/api/addProduct.php?";
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
                if(course != null ) {
                    params.put("product", course.getId());
                    params.put("category","15");
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

    private void setCourse() {
        Uri uri;
        if(course == null) {
            /*
            Check whether the course type is a video based course or
            exam prep. if "course" is null, it is exam prep.
             */
            uri = Uri.parse(examPrep.getDemo_url());
//            price.setText(examPrep.getPrice());
            getSupportActionBar().setTitle(examPrep.getName());
        } else {
            /*
            If "course" is not null, it is video based course.
             */
            uri = Uri.parse(course.getDemo_url());

            tv_courseName.setText(course.getName());
            tv_instructor_name.setText("By Careeranna");
            if(course.getPrice().equals("null"))
                tv_cost.setText("Free");
            else{
                String price = "₹"+course.getPrice();
                tv_cost.setText(price);
            }

//            price.setText("Free");
            getSupportActionBar().setTitle(course.getName());
        }

        PlaylistItem playlistItem = new PlaylistItem.Builder()
                .file(uri.toString())
                .build();
        playerView.load(playlistItem);
    }

    private void fetchUnit() {
//        String id = course.getId();
//
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Loading Videos .. ");
//        progressDialog.show();
//
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        String url = "https://careeranna.com/api/getVideosWithNames.php?product_id="+id;
////        String url = "https://careeranna.com/api/getVideosWithNames.php?product_id=228";
//        Log.i("urlInsidePurchaseCourse", url);
//        StringRequest stringRequest = new StringRequest(Request.Method.GET,
//                url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
                String response = "";
                       ArrayList<String> course = new ArrayList<>();
                        try {
                            Log.i("pdf", response);
//                            CourseDummyData cdd = new CourseDummyData();
//                            response = cdd.getDummyData();
                            response = CourseDummyData.dummyString;
                            Log.d(TAG, "response = "+response);
                            JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("content");
                                for(int i=0;i<jsonArray.length();i++) {
                                    course.add((String)jsonArray.get(i));
                                }
                                Log.d(TAG, "DummyUnits = "+course.size());
                        } catch (JSONException e) {
                            e.printStackTrace(); }
                        /*
                        addCourseUnit call
                        pass the course
                         */
                        addCourseUnits(course);
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d(TAG, "onErrorResponse: "+error.networkResponse);
//                Toast.makeText(PurchaseCourseDetail.this, "Error Occured", Toast.LENGTH_SHORT).show();
//                progressDialog.dismiss();
//            }
//        });
//        requestQueue.add(stringRequest);
    }

    public void addCourseUnits(ArrayList<String> course) {
        Log.d(TAG, "addCourseUnits: "+course.size());
        Drawable check = getApplicationContext().getResources().getDrawable(R.drawable.ic_check_circle_black_24dp);
        Drawable unCheck = getApplicationContext().getResources().getDrawable(R.drawable.ic_check_circle_black1_24dp);
        ArrayList<Unit> mUnits = new ArrayList<>();

        for (String unitsname : course) {
            char c = unitsname.charAt(0);
            if(!Character.isDigit(c)) {
                if(c != '$') {
                    Unit unit = new Unit(unitsname, check);
                    mUnits.add(unit);
                } else {
                    String array[] = unitsname.split(",");
                    mUnits.get(mUnits.size() -1).topics.add(new Topic(array[0].substring(1), array[1]));
                }
            }

            listAdapter = new ExpandableList_Adapter(getApplicationContext(), mUnits);
            listAdapter.setExpandableListView(expandableListView);
            expandableListView.setAdapter(listAdapter);
        }
        Log.d(TAG, "addCourseUnits: units size: "+mUnits.size());
    }
}

