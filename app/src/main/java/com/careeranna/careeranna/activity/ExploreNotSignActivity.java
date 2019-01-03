package com.careeranna.careeranna.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.careeranna.careeranna.fragement.explore_not_sign_in_fragements.InsideWithoutSignFragment;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.adapter.BannerViewPagerAdapter;
import com.careeranna.careeranna.data.Banner;
import com.careeranna.careeranna.data.UrlConstants;
import com.careeranna.careeranna.user.SignUp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ExploreNotSignActivity extends AppCompatActivity {

    LinearLayout linearLayout;                              // Linear Layout For Dots Of Banner

    InsideWithoutSignFragment insideWithoutSignFragment;    // Fragement Inside Explore to Show Details

    FragmentManager fragmentManager;                        // Fragement Manager To Change Fragements

    ArrayList<Banner> mBanners;                             // Arraylist Of Banner For Slider

    int page = 0;                                           // Position For The Current Banner Image

    ViewPager viewPager;                                    // ViewPager For Banner

    BannerViewPagerAdapter bannerViewPagerAdapter;  // Pager Adaper For Banner Slider

    private int currentPage;        // Current Image Position
    private Handler handler;        // Handler For The Images
    private int delay = 5000;       // Delay Of 5 Seconds For Image Changing
    Runnable runnable;              // Runnable For Changing Banner Images

    FrameLayout frameLayout;        // FrameLayout For Banner Images

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_not_sign);

        /**
         * Initializing Variables of layout
         */

        viewPager = findViewById(R.id.viewPager);
        linearLayout = findViewById(R.id.sliderDots);
        frameLayout = findViewById(R.id.pager);

        /**
         * Inializing Fragement Which will have all the free videos and courses
         */

        insideWithoutSignFragment = new InsideWithoutSignFragment();

        /**
         * Getting Fragement Manager Fron The Activity
         */

        fragmentManager = getSupportFragmentManager();

        /**
         * Replacing Main Container With Fragment Inside Explore
         */

        fragmentManager.beginTransaction().replace(R.id.main_content, insideWithoutSignFragment).commit();

        getBanner();

    }

    /**
     * Fetching Banner From The Api Every Time The App Loads
     */

    public void getBanner() {

        mBanners = new ArrayList<>();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                UrlConstants.FETCH_BANNER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("url_response", response.toString());
                            JSONArray bannerArray = new JSONArray(response);
                            for (int i = 0; i < bannerArray.length(); i++) {
                                JSONObject banner = bannerArray.getJSONObject(i);
                                mBanners.add(new Banner(banner.getString("banner_id"),
                                        banner.getString("banner_title"),
                                        "https://careeranna.com/uploads/banners/banner/" + banner.getString("banner_image_url")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        bannerViewPagerAdapter = new BannerViewPagerAdapter(getApplicationContext(), mBanners);
                        viewPager.setAdapter(bannerViewPagerAdapter);
                        // Initializing dots for swipping banner layout
                        viewPager.addOnPageChangeListener(bannerListener);
                        currentPage = 0;

                        makeRunnable();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        );

        requestQueue.add(stringRequest);
    }

    /**
     * Changing Dots From Non Active To Active Accoring To Position
     *
     * @param i
     */

    private void addDots(int i) {
        linearLayout.removeAllViews();
        TextView[] dots = new TextView[bannerViewPagerAdapter.getCount()];

        for (int x = 0; x < dots.length; x++) {
            dots[x] = new TextView(this);
            dots[x].setText(String.valueOf(Html.fromHtml("&#8226")));
            dots[x].setTextSize(40);
            dots[x].setTextColor(getResources().getColor(R.color.intro_dot_dark));

            linearLayout.addView(dots[x]);
        }

        dots[i].setTextColor(getResources().getColor(R.color.intro_dot_light));
    }

    /**
     * Runnable Which Will Change Image Every 5 Seconds
     */

    public void makeRunnable() {
        runnable = new Runnable() {
            public void run() {
                if (bannerViewPagerAdapter.getCount() == page) {
                    page = 0;
                } else {
                    page++;
                }
                viewPager.setCurrentItem(page, true);
                handler.postDelayed(this, delay);
            }
        };
    }

    /**
     * Listener For Banner Images Which Wil Change Dots State From Active To Non Active According
     * To Postion
     */

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

    /**
     * Inflating Menu Sign In Which Has Only One Item For Sign In
     *
     * @param menu
     * @return
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.signin_button, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Menu Item For go To Sign up Activity from Without Sign In Explore
     *
     * @param item
     * @return
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_signin:
                Intent signInActivity = new Intent(this, SignUp.class);
                startActivity(signInActivity);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Checking If User Is Connected To Wifi Or Mobile Internet
     *
     * @return
     */

    private boolean amIConnect() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

}