package com.careeranna.careeranna.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.careeranna.careeranna.MyCourses;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.adapter.ViewPagerAdapter;
import com.careeranna.careeranna.data.Banner;
import com.careeranna.careeranna.data.FreeVideos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ExploreNotSIActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = "ExploreNotSi";

    private LinearLayout freeVidLayout, trendingVidLayout, topCoursesLayout;
    private ViewPagerAdapter bannerAdapter;
    private LinearLayout dotsLayout;
    private ViewPager banner;

    ArrayList<Banner> mBanners;

    ProgressDialog progressDialog;

    ArrayList<FreeVideos> freeVideos;
    private int currentPage;
    private Runnable runnable;
    private Handler handler;

    private int delay = 5000;       //Change the delay of the banner scroll from here


    private String[] imageUrls = new String[] {
            "https://4.bp.blogspot.com/-qf3t5bKLvUE/WfwT-s2IHmI/AAAAAAAABJE/RTy60uoIDCoVYzaRd4GtxCeXrj1zAwVAQCLcBGAs/s1600/Machine-Learning.png",
            "https://cdn-images-1.medium.com/max/2000/1*SSutxOFoBUaUmgeNWAPeBA.jpeg",
            "https://www.digitalvidya.com/wp-content/uploads/2016/02/Master_Digital_marketng-1170x630.jpg"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_not_si);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(
                    Html.fromHtml("<font color='#FFFFFF'> Featured </font>"));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        topCoursesLayout = findViewById(R.id.ll_top);
        freeVidLayout = findViewById(R.id.ll_free_vid);
        trendingVidLayout = findViewById(R.id.ll_trending);
        banner = findViewById(R.id.banner);
        dotsLayout = findViewById(R.id.bannerDots);

        /*
        Setting the banners and it's adapter
         */
        initializeBanner();

        LayoutInflater layoutInflater = LayoutInflater.from(this);

        /*
        Setting the banner auto-scroll
         */
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if(bannerAdapter.getCount() == currentPage){
                    currentPage = 0;
                } else {
                    currentPage++;
                }
                banner.setCurrentItem(currentPage, true);
                handler.postDelayed(this, delay);
            }
        };

        /*
        Only for testing purpose, to check how the activity will look like when the actual video
        thumbnails will be shown.
        ********************************************************************************************
         */
        for(int i=0; i<10; i++){
            View view = layoutInflater.inflate(R.layout.video_thumbnail, topCoursesLayout, false);

            TextView title = view.findViewById(R.id.tv_vid_thumbnail_title);
            ImageView imageView = view.findViewById(R.id.iv_vid_thumbnail_img);

            title.setText("Video "+i);
            imageView.setImageResource(R.mipmap.ic_launcher);

            topCoursesLayout.addView(view);
        }

        initalizeVideos();

        for(int i=0; i<10; i++){
            View view = layoutInflater.inflate(R.layout.video_thumbnail,trendingVidLayout, false);

            TextView title = view.findViewById(R.id.tv_vid_thumbnail_title);
            ImageView imageView = view.findViewById(R.id.iv_vid_thumbnail_img);

            title.setText("Video "+i);
            imageView.setImageResource(R.mipmap.ic_launcher);

            trendingVidLayout.addView(view);
        }

        // *************ONLY FOR TESTING PURPOSE CODE, ENDS HERE***********************************
    }

    public void initializeBanner() {
        // Initializing ArrayList
        mBanners = new ArrayList<>();

        mBanners.add(new Banner("1", "Machine Learning", imageUrls[0]));
        mBanners.add(new Banner("2", "Python", imageUrls[1]));
        mBanners.add(new Banner("3", "Marketing", imageUrls[2]));

        // Setting Adapter for banner viewPage
        bannerAdapter = new ViewPagerAdapter(getApplicationContext(), mBanners);
        banner.setAdapter(bannerAdapter);
        banner.addOnPageChangeListener(bannerListener);
        currentPage = 0;
        addDots(0);
    }

    private void initalizeVideos() {

        freeVideos = new ArrayList<>();

        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Loading My Courses Please Wait ... ");
        progressDialog.show();

        progressDialog.setCancelable(false);

        final LayoutInflater layoutInflater = LayoutInflater.from(this);

        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        String url1 = "https://careeranna.com/api/getFreeVideos.php";
        Log.d("url_res", url1);
        StringRequest stringRequest1  = new StringRequest(Request.Method.GET, url1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("url_response", response.toString());
                            JSONArray VideosArray = new JSONArray(response);
                            for(int i=0;i<10;i++) {
                                JSONObject videos = VideosArray.getJSONObject(i);
                                freeVideos.add(new FreeVideos(
                                        videos.getString("id"),
                                        videos.getString("video_url").replace("\\",""),
                                        "https://www.careeranna.com/thumbnail/" +videos.getString("thumbnail"),
                                        videos.getString("tags"),
                                        videos.getString("totalViews"),""));

                                View view = layoutInflater.inflate(R.layout.video_thumbnail, freeVidLayout, false);

                                TextView title = view.findViewById(R.id.tv_vid_thumbnail_title);
                                ImageView imageView = view.findViewById(R.id.iv_vid_thumbnail_img);

                                if(!videos.getString("tags").equals("")&&!videos.getString("tags").equals("null")) {

                                    if(!videos.getString("thumbnail").equals("")) {
                                        title.setText(videos.getString("tags"));
                                        Glide.with(ExploreNotSIActivity.this).load("https://www.careeranna.com/thumbnail/" + videos.getString("thumbnail")).into(imageView);

                                        freeVidLayout.addView(view);
                                    }
                                }
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
                });

        requestQueue1.add(stringRequest1);

    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable, delay);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.signin_button, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.menu_signin:
                Intent signInActivity = new Intent(this, SignUp.class);
                startActivity(signInActivity);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {

    }

    /*
    To make the dots, change the dots attributes from here.
    */

    private void addDots(int i){
        dotsLayout.removeAllViews();
        TextView[] dots = new TextView[bannerAdapter.getCount()];

        for(int x=0; x<dots.length; x++){
            dots[x] = new TextView(this);
            dots[x].setText(String.valueOf(Html.fromHtml("&#8226")));
            dots[x].setTextSize(40);
            dots[x].setTextColor(getResources().getColor(R.color.intro_dot_dark));

            dotsLayout.addView(dots[x]);
        }

        dots[i].setTextColor(getResources().getColor(R.color.intro_dot_light));
    }

    ViewPager.OnPageChangeListener bannerListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            addDots(i);
            currentPage = i;
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };
}
