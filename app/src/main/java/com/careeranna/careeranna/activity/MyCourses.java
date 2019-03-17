package com.careeranna.careeranna.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;

import java.util.Locale;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
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

import com.careeranna.careeranna.data.FreeVideos;
import com.careeranna.careeranna.InstructorsListActivity;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.adapter.ListViewAdapter;
import com.careeranna.careeranna.adapter.BannerViewPagerAdapter;
import com.careeranna.careeranna.data.Article;
import com.careeranna.careeranna.data.Banner;
import com.careeranna.careeranna.data.Category;
import com.careeranna.careeranna.data.Constants;
import com.careeranna.careeranna.data.Course;
import com.careeranna.careeranna.data.CourseWithLessData;
import com.careeranna.careeranna.data.ExamPrep;
import com.careeranna.careeranna.data.MenuList;
import com.careeranna.careeranna.data.UrlConstants;
import com.careeranna.careeranna.data.User;
import com.careeranna.careeranna.helper.CountDrawable;

import com.careeranna.careeranna.fragement.dashboard_fragements.WishListFragment;
import com.careeranna.careeranna.fragement.dashboard_fragements.CategoryFragment;
import com.careeranna.careeranna.fragement.dashboard_fragements.MyCoursesFragment;
import com.careeranna.careeranna.fragement.dashboard_fragements.ArticlesFragment;
import com.careeranna.careeranna.fragement.dashboard_fragements.CartFragment;
import com.careeranna.careeranna.fragement.dashboard_fragements.ExploreNew;

import com.careeranna.careeranna.fragement.ErrorOccurredFragment;
import com.careeranna.careeranna.fragement.NoInternetFragment;

import com.careeranna.careeranna.user.MyProfile_2;
import com.careeranna.careeranna.user.SignUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

import static android.view.View.GONE;

public class MyCourses extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, NoInternetFragment.OnFragemntClickListener, ErrorOccurredFragment.OnErrorFragementClickListener {

    public static final String TAG = "MyCoursesActivity";

    private DrawerLayout drawerLayout;

    private ActionBarDrawerToggle mToggle;

    WishListFragment wishListFragment;
    RelativeLayout relativeLayout;

    Menu menu;

    int fragment_id = 3;

    LinearLayout linearLayout;

    FirebaseAuth mAuth;

    String mUsername, profile_pic_url, mEmail;

    ArrayList<FreeVideos> freeVideosForExplore, trendingVideosForExplore;

    ArrayList<Course> coursesForExplore, freeCourseForExplore;

    ExploreNew exploreNew;
    MyCoursesFragment myCoursesFragment;
    NoInternetFragment noInternetFragment;
    ArticlesFragment myArticleFragment;
    CategoryFragment categoryFragment;
    ErrorOccurredFragment errorOccurredFragment;

    FragmentManager fragmentManager;

    ArrayList<Banner> mBanners;

    Button myCourses;

    int language;

    int page = 0;

    ViewPager viewPager;

    ProgressDialog progressDialog;

    BannerViewPagerAdapter bannerViewPagerAdapter;

    private int currentPage;
    private Handler handler;
    private int delay = 5000;
    Runnable runnable;

    User user;

    CartFragment cartFragment;
    ArrayList<Category> categories;
    ArrayList<Course> courses, freecourse;
    ArrayList<ExamPrep> examPreps;
    ArrayList<Article> mArticles;

    FrameLayout frameLayout;

    ArrayList<MenuList> your_array_list;

    private long backPressed;

    RequestQueue requestQueue;

    NavigationView navigationView;

    ArrayList<CourseWithLessData> names, urls, ids, category_ids;

    ListView listView;

    private int menuToChoose = R.menu.add_cart;


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        this.menu = menu;

        setCartItemCountOnMenu();

        return true;
    }

    public void setCartItemCountOnMenu() {

        String cartItemCount = Paper.book().read("cart");

        if (cartItemCount != null && !cartItemCount.isEmpty()) {

            Log.i("cart", cartItemCount);
            Gson gson = new Gson();

            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();

            ArrayList<String> arrayList = gson.fromJson(cartItemCount, type);

            setCount(this, arrayList.size() + "");

        } else {
            setCount(this, "0");
        }
    }

    public void setCount(Context context, String count) {

        if (menuToChoose == R.menu.add_cart) {
            MenuItem menuItem = menu.findItem(R.id.add_to_cart);

            LayerDrawable icon = (LayerDrawable) menuItem.getIcon();

            CountDrawable badge;

            // Reuse drawable if possible
            Drawable reuse = icon.findDrawableByLayerId(R.id.ic_group_count);
            if (reuse != null && reuse instanceof CountDrawable) {
                badge = (CountDrawable) reuse;
            } else {
                badge = new CountDrawable(context);
            }

            badge.setCount(count);
            icon.mutate();
            icon.setDrawableByLayerId(R.id.ic_group_count, badge);

        }
    }

    private void setLocale(String lang) {

        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        onConfigurationChanged(conf);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        populateNavigation();

        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_courses);

        Log.d(TAG, "onCreate: ");

        // Initializing Paper.db for getting cache data of user
        Paper.init(this);

        // Checking App Version For playStore Apk And User Installed Apk
        checkAppUpdatesOnPlayStore();

        // Initializing Fragments For this Activity
        errorOccurredFragment = new ErrorOccurredFragment();
        noInternetFragment = new NoInternetFragment();
        myCoursesFragment = new MyCoursesFragment();
        myArticleFragment = new ArticlesFragment();
        wishListFragment = new WishListFragment();
        categoryFragment = new CategoryFragment();
        cartFragment = new CartFragment();
        exploreNew = new ExploreNew();

        // Adding Listener For Retry Of No Internet And Error Occurred
        noInternetFragment.setOnFragementClicklistener(this);
        errorOccurredFragment.setOnFragementClicklistener(this);

        //  Initialize Layout Variable
        drawerLayout = findViewById(R.id.drawelayout);
        navigationView = findViewById(R.id.nav_view);
        viewPager = findViewById(R.id.viewPager);
        linearLayout = findViewById(R.id.sliderDots);
        frameLayout = findViewById(R.id.pager);

        requestQueue = Volley.newRequestQueue(this);


        relativeLayout = findViewById(R.id.relative_loading);


        freeVideosForExplore = new ArrayList<>();
        trendingVideosForExplore = new ArrayList<>();

        coursesForExplore = new ArrayList<>();
        freeCourseForExplore = new ArrayList<>();


        fragmentManager = getSupportFragmentManager();

        try {
            language = Paper.book().read(Constants.LANGUAGE);
        } catch (Exception e) {
            language = 1;
            Paper.book().write(Constants.LANGUAGE, language);
        }

        listView = findViewById(R.id.list_menu_items);

        populateNavigation();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                fragment_id = position;

                if (!amIConnect()) {

                    frameLayout.setVisibility(GONE);
                    fragmentManager.beginTransaction().replace(R.id.main_content, noInternetFragment).commit();

                    setCartItemCountOnMenu();

                    drawerLayout.closeDrawer(GravityCompat.START);

                    return;
                }

                menuToChoose = R.menu.add_cart;
                invalidateOptionsMenu();

                changeFragmentWithNav();

                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        // Binding NavigationView To Toolbar
        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        String cache = Paper.book().read("user");
        if (cache != null && !cache.isEmpty()) {
            user = new Gson().fromJson(cache, User.class);

            profile_pic_url = user.getUser_photo().replace("\\", "");
            mUsername = user.getUser_username();
            mEmail = user.getUser_email();
        }


        names = new ArrayList<>();
        urls = new ArrayList<>();
        ids = new ArrayList<>();

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        if (task.getResult() != null) {

                            String token = task.getResult().getToken();
                            Log.d(TAG, "TOKEN IS " + token);
                        }
                    }
                });

        getSupportActionBar().setTitle(getResources().getString(R.string.explore));

        /*
        LAUNCH EXPLORE FRAGMENT BY DEFAULT
         */


        if (amIConnect()) {
            Bundle extras = getIntent().getExtras();
            Log.d(TAG, "Trying to get intent, " + (extras == null));
            if (extras != null && extras.getBoolean(Constants.OPEN_MY_COURSES_INTENT)) {
                openMyCoursesFragment();
            } else {
                fragmentManager.beginTransaction().replace(R.id.main_content, exploreNew).commit();
                initializeVideo();
            }
        } else {
            frameLayout.setVisibility(GONE);
            fragmentManager.beginTransaction().replace(R.id.main_content, noInternetFragment).commit();
        }

        // Set Navigation View Information
        setNavigationView();

        // Setting Banner Information
        getBanner();
        // Runnable For banner for changing in banner
        handler = new Handler();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(menuToChoose, menu);
        return true;
    }

    public void getBanner() {

        mBanners = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                UrlConstants.FETCH_BANNER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i(TAG, response.toString());
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
                        //               progressDialog.dismiss();
                    }
                }
        );

        requestQueue.add(stringRequest);
    }


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

    public void setNavigationView() {

        // Initialize Views for navigation
        TextView username, useremail;
        CircleImageView profile;

        LinearLayout headerView;

        headerView = findViewById(R.id.header_header);

        profile = headerView.findViewById(R.id.navImage);
        username = headerView.findViewById(R.id.navUsername);
        useremail = headerView.findViewById(R.id.navUseremail);

        /*
        If there is no image url provided, then write the initial letter of the username
         */
        TextView initialAlphabet = findViewById(R.id.nav_username_initial);
        if (!profile_pic_url.isEmpty()) {
            Glide.with(this).load(profile_pic_url).into(profile);
            initialAlphabet.setVisibility(View.INVISIBLE);
        } else {
            initialAlphabet.setVisibility(View.VISIBLE);
            initialAlphabet.setText(mUsername.substring(0, 1));
        }

        username.setText(mUsername);
        useremail.setText(mEmail);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.add_to_cart) {

            cartFragment = new CartFragment();
            menuToChoose = R.menu.notification;
            invalidateOptionsMenu();

            frameLayout.setVisibility(GONE);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(getResources().getString(R.string.my_cart));
            }

            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_content, cartFragment).commit();
        } else if (id == R.id.notification_bell_icon) {
            startActivity(new Intent(this, NotificationActivity.class));
        }

        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        handler.postDelayed(runnable, delay);
        String cache = Paper.book().read("user");
        if (cache != null && !cache.isEmpty()) {
            User user = new Gson().fromJson(cache, User.class);

            profile_pic_url = user.getUser_photo().replace("//", "");
            mUsername = user.getUser_username();
            mEmail = user.getUser_email();
        }

        try {
            language = Paper.book().read(Constants.LANGUAGE);
            Log.d("in_try", language + " ");
        } catch (NullPointerException e) {
            language = 1;
            Log.d("in_catch", language + " ");
            Paper.book().write(Constants.LANGUAGE, language);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }

    }

    @Override
    protected void onPause() {

        super.onPause();
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
        handler.removeCallbacks(runnable);
    }

//    public void signOut(View view) {
//
//        mAuth = FirebaseAuth.getInstance();
//        if(mAuth != null) {
//            mAuth.signOut();
//            LoginManager.getInstance().logOut();
//        }
//        startActivity(new Intent(this, MainActivity.class));
//        finish();
//    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        setCartItemCountOnMenu();

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void myCourse() {

        names = new ArrayList<>();
        urls = new ArrayList<>();
        ids = new ArrayList<>();
        category_ids = new ArrayList<>();

        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage(getString(R.string.loading_just_a_sec));
        progressDialog.show();

        progressDialog.setCancelable(false);

        RequestQueue requestQueue1 = Volley.newRequestQueue(MyCourses.this);
        final String myCourseUrl = UrlConstants.USER_COURSES_URL + user.getUser_id();
        Log.d("url_res", myCourseUrl);
        StringRequest stringRequest1 = new StringRequest(Request.Method.GET,
                myCourseUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("url_response", response.toString());
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray coursesArray = jsonObject.getJSONArray("free");
                            for (int i = 0; i < coursesArray.length(); i++) {
                                JSONObject Category = coursesArray.getJSONObject(i);
                                names.add(new CourseWithLessData(Category.getString("course_name"),
                                        Category.getString("course_id"),
                                        "https://www.careeranna.com/"+Category.getString("course_image"),
                                        "0",
                                        "-"
                                ));
                            }
                            JSONArray coursesArray1 = jsonObject.getJSONArray("paid");
                            for (int i = 0; i < coursesArray1.length(); i++) {
                                JSONObject Category = coursesArray1.getJSONObject(i);
                                names.add(new CourseWithLessData(Category.getString("product_name"),
                                        Category.getString("product_id"),
                                        Category.getString("product_image"),
                                        "0",
                                        "-"
                                ));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        myCoursesFragment.add(names);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<>();
                params.put("user", user.getUser_id());
                return params;

            }
        };

        requestQueue1.add(stringRequest1);
    }

    public void initArticle() {

        try {
            language = Paper.book().read(Constants.LANGUAGE);
            Log.d("in_try", language + " ");
        } catch (NullPointerException e) {
            language = 1;
            Log.d("in_catch", language + " ");
            Paper.book().write(Constants.LANGUAGE, language);
        }

        mArticles = new ArrayList<>();

        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage(getString(R.string.loading_just_a_sec));
        progressDialog.show();

        progressDialog.setCancelable(false);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String img_url = "https://www.careeranna.com/articles/wp-content/uploads/";
        String url = "https://careeranna.com/api/articlewithimage.php";
        if (language == 2) {
            url = "https://www.careeranna.com/api/hindiarticleswithimage.php";
            img_url = "https://www.careeranna.com/hin/articles/wp-content/uploads/";
        }
        final String finalImg_url = img_url;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("url_response", response.toString());
                            JSONArray ArticlesArray = new JSONArray(response.toString());
                            for (int i = 0; i < ArticlesArray.length(); i++) {
                                JSONObject Articles = ArticlesArray.getJSONObject(i);
                                mArticles.add(new Article(Articles.getString("ID"),
                                        Articles.getString("post_title"),
                                        finalImg_url + Articles.getString("meta_value").replace("\\", ""),
                                        Articles.getString("display_name"),
                                        "CAT",
                                        Articles.getString("post_content"),
                                        Articles.getString("post_date")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        progressDialog.dismiss();
                        myArticleFragment.setmArticles(mArticles, language);
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


    }

    private boolean amIConnect() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    private void checkAppUpdatesOnPlayStore() {

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest str = new StringRequest(Request.Method.GET,
                UrlConstants.FETCH_APP_VERSION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("App", response);
                            JSONObject spo = new JSONObject(response);
                            Log.d("App Version", "App : " + Constants.VERSION_NAME + " Playstore : " + spo.getString("version_name"));
                            if (!spo.getString("version_name").equals(Constants.VERSION_NAME)) {
                                alertDialogForUpdate();
                            }
                        } catch (JSONException e) {
                            Log.e("error_code", e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(str);

    }


    private void alertDialogForUpdate() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Update Available");
        builder.setCancelable(true);

        builder.setMessage(getString(R.string.update_your_app))
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(
                                new Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse(UrlConstants.PLAYSTORE_URL)
                                )
                        );
                    }
                });


        AlertDialog alert = builder.create();
        alert.show();
    }


    private void openMyCoursesFragment() {
        myCourse();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_content, myCoursesFragment).commit();
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(getResources().getString(R.string.mycourses));
    }

    private void initializeVideo() {

        if (!exploreNew.isEmptyArrayList()) {

            trendingVideosForExplore = new ArrayList<>();
            freeVideosForExplore = new ArrayList<>();

            try {
                language = Paper.book().read(Constants.LANGUAGE);
                if (language == 2) {
                    setLocale("hi");
                } else if (language == 1) {
                    setLocale("en");
                }
                Log.d("in_try", language + " ");
            } catch (Exception e) {
                language = 1;
                Log.d("in_catch", language + " ");
                Paper.book().write(Constants.LANGUAGE, language);
            }


            relativeLayout.setVisibility(View.VISIBLE);

            RequestQueue requestQueue1 = Volley.newRequestQueue(this);
            String url1 = UrlConstants.FETCH_TRENDING_VIDEOS + String.valueOf(language);
            Log.d("url_res", url1);
            StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url1,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.i("url_response", response);
                                JSONArray VideosArray = new JSONArray(response);
                                for (int i = 0; i < VideosArray.length(); i++) {
                                    JSONObject videos = VideosArray.getJSONObject(i);
                                    trendingVideosForExplore.add(new FreeVideos(
                                            videos.getString("id"),
                                            videos.getString("video_url").replace("\\", ""),
                                            "https://www.careeranna.com/thumbnail/" + videos.getString("thumbnail"),
                                            videos.getString("totalViews"),
                                            videos.getString("tags"),
                                            videos.getString("heading"),
                                            "Free",
                                            videos.getString("duration"),
                                            videos.getString("likes"),
                                            videos.getString("dislikes")));
                                    trendingVideosForExplore.get(i).setType("Trending");
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            RequestQueue requestQueue1 = Volley.newRequestQueue(MyCourses.this);
                            String url1 = "https://careeranna.com/api/getFreeVideos.php?id=" + String.valueOf(language);
                            Log.d("url_res", url1);
                            StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url1,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                Log.i("url_response", response);
                                                JSONArray VideosArray = new JSONArray(response);
                                                for (int i = 0; i < VideosArray.length(); i++) {
                                                    JSONObject videos = VideosArray.getJSONObject(i);
                                                    freeVideosForExplore.add(new FreeVideos(
                                                            videos.getString("id"),
                                                            videos.getString("video_url").replace("\\", ""),
                                                            "https://www.careeranna.com/thumbnail/" + videos.getString("thumbnail"),
                                                            videos.getString("totalViews"),
                                                            videos.getString("tags"),
                                                            videos.getString("heading"),
                                                            "Trending",
                                                            videos.getString("duration"),
                                                            videos.getString("likes"),
                                                            videos.getString("dislikes")
                                                    ));
                                                    freeVideosForExplore.get(i).setType("Latest");
                                                }

                                            } catch (JSONException e) {
                                                Log.e(TAG, "onResponse: ", e.fillInStackTrace());
                                                e.printStackTrace();
                                            }

                                            addPaidCourse();
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                            showErrorFragment();
                                        }
                                    });

                            requestQueue1.add(stringRequest1);


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            showErrorFragment();
                        }
                    });
            requestQueue1.add(stringRequest1);
        } else {
            relativeLayout.setVisibility(GONE);
        }

    }


    private void addPaidCourse() {

        coursesForExplore = new ArrayList<>();

        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        String url1 = "https://careeranna.com/api/getAllCourse.php";
        Log.d("url_res", url1);
        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            Log.i("url_response", response.toString());
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray CategoryArray = jsonObject.getJSONArray("paid");
                            for (int i = 0; i < 40; i++) {
                                JSONObject Category = CategoryArray.getJSONObject(i);
                                coursesForExplore.add(new Course(Category.getString("product_id"),
                                        Category.getString("course_name"),
                                        "https://www.careeranna.com/" + Category.getString("product_image").replace("\\", ""),
                                        Category.getString("exam_id"),
                                        Category.getString("discount")
                                        , "",
                                        Category.getString("video_url").replace("\\", ""),
                                        "Paid",
                                        Category.getString("price")
                                ));
                                coursesForExplore.get(i).setType("Paid");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        addFreeForExplore();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        showErrorFragment();
                    }
                }
        );

        requestQueue1.add(stringRequest1);
    }

    private void populateNavigation() {

        your_array_list = new ArrayList<>();
        your_array_list.add(new MenuList(getResources().getString(R.string.MBA), getApplicationContext().getResources().getDrawable(R.drawable.ic_scholarship)));
        //your_array_list.add(new MenuList("UPSC", getApplicationContext().getResources().getDrawable(R.drawable.ic_hinduism)));
        your_array_list.add(new MenuList(getResources().getString(R.string.general_knowledge), getApplicationContext().getResources().getDrawable(R.drawable.ic_gears)));
        //your_array_list.add(new MenuList("Finance", getApplicationContext().getResources().getDrawable(R.drawable.ic_ascendant_bars_graphic)));
        //your_array_list.add(new MenuList("Marketing", getApplicationContext().getResources().getDrawable(R.drawable.ic_digital_marketing)));
        //your_array_list.add(new MenuList("Certificate Courses", getApplicationContext().getResources().getDrawable(R.drawable.ic_certi)));
        your_array_list.add(new MenuList(getResources().getString(R.string.mycourses), getApplicationContext().getResources().getDrawable(R.drawable.ic_study)));
        your_array_list.add(new MenuList(getResources().getString(R.string.explore), getApplicationContext().getResources().getDrawable(R.drawable.ic_book)));
        your_array_list.add(new MenuList(getResources().getString(R.string.articles), getApplicationContext().getResources().getDrawable(R.drawable.ic_article_1)));
        your_array_list.add(new MenuList(getResources().getString(R.string.our_mentors), getApplicationContext().getResources().getDrawable(R.drawable.ic_teacher_showing_curve_line_on_whiteboard)));
        your_array_list.add(new MenuList("My PlayLists", getApplicationContext().getResources().getDrawable(R.drawable.ic_playlist)));
        your_array_list.add(new MenuList(getResources().getString(R.string.WishList), getApplicationContext().getResources().getDrawable(R.drawable.ic_like)));
        your_array_list.add(new MenuList(getResources().getString(R.string.settings), getApplicationContext().getResources().getDrawable(R.drawable.ic_settings)));
        your_array_list.add(new MenuList(getResources().getString(R.string.signout), getApplicationContext().getResources().getDrawable(R.drawable.ic_logout_1), "#FFDA3C21", "#FFF5F3F3", Gravity.CENTER, View.INVISIBLE));

        ListViewAdapter adapter = new ListViewAdapter(this, your_array_list);
        //
        //// Attach the adapter to a ListView
        //
        //ListView listView = (ListView) findViewById(R.id.lvItems);
        //
        listView.setAdapter(adapter);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.explore));
        }
    }

    private void addFreeForExplore() {

        freeCourseForExplore = new ArrayList<>();

        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        String url1 = "https://careeranna.com/api/getFreeCourse.php?id=" + String.valueOf(language);
        Log.d("url_res", url1);
        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            Log.i("url_response", response.toString());
                            JSONArray CategoryArray = new JSONArray(response.toString());
                            for (int i = 0; i < CategoryArray.length(); i++) {
                                JSONObject Category = CategoryArray.getJSONObject(i);
                                freeCourseForExplore.add(new Course(Category.getString("course_id"),
                                        Category.getString("name"),
                                        "https://www.careeranna.com/" + Category.getString("image").replace("\\", ""),
                                        Category.getString("eid"),
                                        "0"
                                        , "meta_description", ""));
                                freeCourseForExplore.get(i).setType("Free");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        exploreNew.addFree(freeVideosForExplore, trendingVideosForExplore, coursesForExplore, freeCourseForExplore);
                        relativeLayout.setVisibility(GONE);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        showErrorFragment();

                    }
                }
        );

        requestQueue1.add(stringRequest1);
    }

    @Override
    public void onItemClick1() {
        changeFragmentWithNav();
    }

    private void changeFragmentWithNav() {
        switch (fragment_id) {
            case 0:
                frameLayout.setVisibility(GONE);
                categoryFragment = new CategoryFragment();
                categoryFragment.addSubCategory("1", "4", user.getUser_id());

                fragmentManager.beginTransaction().replace(R.id.main_content, categoryFragment).commit();
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(getResources().getString(R.string.MBA));
                }
                break;
            case 1:
                frameLayout.setVisibility(GONE);
                categoryFragment = new CategoryFragment();

                categoryFragment.addSubCategory("6", "6", user.getUser_id());

                fragmentManager.beginTransaction().replace(R.id.main_content, categoryFragment).commit();
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(getResources().getString(R.string.general_knowledge));
                }
                break;

            case 2:
                frameLayout.setVisibility(GONE);
                myCourse();
                fragmentManager.beginTransaction().replace(R.id.main_content, myCoursesFragment).commit();
                if(getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(getResources().getString(R.string.mycourses));
                }
                break;
            case 3:
                frameLayout.setVisibility(View.VISIBLE);
                fragmentManager.beginTransaction().replace(R.id.main_content, exploreNew).commit();
                initializeVideo();
                if(getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(getResources().getString(R.string.explore));
                }
                break;

            case 4:
                frameLayout.setVisibility(GONE);
                initArticle();
                fragmentManager.beginTransaction().replace(R.id.main_content, myArticleFragment).commit();
                if(getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(getResources().getString(R.string.articles));
                }
                break;
            case 5:
                startActivity(new Intent(MyCourses.this, InstructorsListActivity.class));
                break;
            case 6:
                startActivity(new Intent(MyCourses.this, MyPlayList.class));
                break;
            case 7:
                frameLayout.setVisibility(GONE);
                fragmentManager.beginTransaction().replace(R.id.main_content, wishListFragment).commit();
                if(getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(getResources().getString(R.string.WishList));
                }
                break;
            case 8:
                startActivity(new Intent(MyCourses.this, MyProfile_2.class));
                break;
            case 9:
                mAuth = FirebaseAuth.getInstance();
                if (mAuth != null) {
                    mAuth.signOut();
                }
                Paper.delete("user");
                startActivity(new Intent(MyCourses.this, SignUp.class));
                finish();
                break;
        }
    }

    public void changeInternet() {
        frameLayout.setVisibility(GONE);
        fragmentManager.beginTransaction().replace(R.id.main_content, noInternetFragment).commit();
    }

    public void showErrorFragment() {
        relativeLayout.setVisibility(GONE);
        frameLayout.setVisibility(GONE);
        fragmentManager.beginTransaction().replace(R.id.main_content, errorOccurredFragment).commit();
    }

    @Override
    public void onRefreshClick() {
        changeFragmentWithNav();
    }


    @Override
    public void onBackPressed() {

        if (backPressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(this, getString(R.string.click_again_to_exit), Toast.LENGTH_SHORT).show();
        }

        backPressed = System.currentTimeMillis();
    }
}

