package com.careeranna.careeranna.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
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

import com.careeranna.careeranna.BuildConfig;
import com.careeranna.careeranna.adapter.FreeCourseAdapter;
import com.careeranna.careeranna.adapter.TrendingVideosAdapter;
import com.careeranna.careeranna.data.FreeVideos;
import com.careeranna.careeranna.fragement.NoInternetFragement;
import com.careeranna.careeranna.fragement.dashboard_fragements.CartFragment;
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
import com.careeranna.careeranna.data.User;
import com.careeranna.careeranna.fragement.dashboard_fragements.CategoryFragment;
import com.careeranna.careeranna.fragement.dashboard_fragements.ExploreNew;
import com.careeranna.careeranna.helper.CountDrawable;
import com.careeranna.careeranna.fragement.dashboard_fragements.ArticlesFragment;
import com.careeranna.careeranna.fragement.dashboard_fragements.MyCoursesFragment;
import com.careeranna.careeranna.helper.InternetDialog;
import com.careeranna.careeranna.user.MyProfile_2;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class MyCourses extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, NoInternetFragement.OnFragemntClickListener {

    public static final String TAG = "MyCourses";

    private DrawerLayout drawerLayout;

    private ActionBarDrawerToggle mToggle;

    WhisListFragement whisListFragement;
    RelativeLayout relativeLayout;

    Menu menu;

    int fragement_id = 3;

    LinearLayout linearLayout;

    CircleImageView imageView;
    TextView username;

    FirebaseAuth mAuth;

    String mUsername, profile_pic_url, mEmail;

    ArrayList<FreeVideos> freeVideosForExplore, trendingvVideosForExplore;

    ArrayList<Course> coursesForExplore, freecourseForExplore;

    ExploreNew exploreNew;
    MyCoursesFragment myCoursesFragement;
    NoInternetFragement noInternetFragement;
    ArticlesFragment myArticleFragment;
    CategoryFragment categoryFragment;

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

    private long backPressed;

    NavigationView navigationView;

    ArrayList<CourseWithLessData> names, urls, ids, category_ids;

    ListView listView;

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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        this.menu = menu;

        String cart = Paper.book().read("cart");

        if (cart != null && !cart.isEmpty()) {

            Log.i("cart", cart);
            Gson gson = new Gson();

            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();

            ArrayList<String> arrayList = gson.fromJson(cart, type);

            setCount(this, arrayList.size() + "");

        } else {
            setCount(this, "0");
        }
        return true;
    }

    private int menuToChoose = R.menu.add_cart;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_courses);

        Log.d(TAG, "onCreate: ");

        //TODO: Uncomment this line when publishing updates to play store
        checkUpdates();

        //  Initialize Layout Variable
        drawerLayout = findViewById(R.id.drawelayout);
        navigationView = findViewById(R.id.nav_view);
        viewPager = findViewById(R.id.viewPager);
        linearLayout = findViewById(R.id.sliderDots);
        frameLayout = findViewById(R.id.pager);

        noInternetFragement = new NoInternetFragement();
        noInternetFragement.setOnFragementClicklistener(this);

        relativeLayout = findViewById(R.id.relative_loading);


        freeVideosForExplore = new ArrayList<>();
        trendingvVideosForExplore = new ArrayList<>();

        coursesForExplore = new ArrayList<>();
        freecourseForExplore = new ArrayList<>();

        Paper.init(this);

        fragmentManager = getSupportFragmentManager();

        try {
            language = Paper.book().read(Constants.LANGUAGE);
            Log.d("in_try", language +" ");
        } catch (Exception e){
            language = 1;
            Log.d("in_catch", language +" ");
            Paper.book().write(Constants.LANGUAGE, language);
        }


        listView = findViewById(R.id.list_menu_items);

        Drawable unCheck = getApplicationContext().getResources().getDrawable(R.drawable.ic_check_circle_black1_24dp);

        final ArrayList<MenuList> your_array_list = new ArrayList<MenuList>();
        your_array_list.add(new MenuList("MBA", getApplicationContext().getResources().getDrawable(R.drawable.ic_scholarship)));
        //your_array_list.add(new MenuList("UPSC", getApplicationContext().getResources().getDrawable(R.drawable.ic_hinduism)));
        your_array_list.add(new MenuList("General Knowledge", getApplicationContext().getResources().getDrawable(R.drawable.ic_gears)));
        //your_array_list.add(new MenuList("Finance", getApplicationContext().getResources().getDrawable(R.drawable.ic_ascendant_bars_graphic)));
        //your_array_list.add(new MenuList("Marketing", getApplicationContext().getResources().getDrawable(R.drawable.ic_digital_marketing)));
        //your_array_list.add(new MenuList("Certificate Courses", getApplicationContext().getResources().getDrawable(R.drawable.ic_certi)));
        your_array_list.add(new MenuList("My Courses", getApplicationContext().getResources().getDrawable(R.drawable.ic_study)));
        your_array_list.add(new MenuList("Explore", getApplicationContext().getResources().getDrawable(R.drawable.ic_book)));
        your_array_list.add(new MenuList("Articles", getApplicationContext().getResources().getDrawable(R.drawable.ic_article_1)));
        your_array_list.add(new MenuList("Our Mentors", getApplicationContext().getResources().getDrawable(R.drawable.ic_teacher_showing_curve_line_on_whiteboard)));
        your_array_list.add(new MenuList("Wish List", getApplicationContext().getResources().getDrawable(R.drawable.ic_like)));
        your_array_list.add(new MenuList("Settings", getApplicationContext().getResources().getDrawable(R.drawable.ic_settings)));
        your_array_list.add(new MenuList("Sign Out", getApplicationContext().getResources().getDrawable(R.drawable.ic_logout_1), "#FFDA3C21", "#FFF5F3F3", Gravity.CENTER, View.INVISIBLE));

        ListViewAdapter adapter = new ListViewAdapter(this, your_array_list);
        //
        //// Attach the adapter to a ListView
        //
        //ListView listView = (ListView) findViewById(R.id.lvItems);
        //
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (!amIConnect()) {
/*
                    InternetDialog internetDialog = new InternetDialog();
                    internetDialog.showDialog(MyCourses.this, "none");*/

                    fragement_id = position;

                    frameLayout.setVisibility(View.GONE);
                    fragmentManager.beginTransaction().replace(R.id.main_content, noInternetFragement).commit();

                    String cart = Paper.book().read("cart");

                    if (cart != null && !cart.isEmpty()) {
                        Gson gson = new Gson();

                        Type type = new TypeToken<ArrayList<String>>() {
                        }.getType();

                        ArrayList<String> arrayList = gson.fromJson(cart, type);

                        setCount(MyCourses.this, arrayList.size() + "");

                    } else {
                        setCount(MyCourses.this, "0");
                    }

                    drawerLayout.closeDrawer(GravityCompat.START);

                    return;
                }

                menuToChoose = R.menu.add_cart;
                invalidateOptionsMenu();

                switch (position) {
                    case 0:
                        frameLayout.setVisibility(View.GONE);
                        categoryFragment = new CategoryFragment();
                        categoryFragment.addSubCategory("1", "4", user.getUser_id());

                        fragmentManager.beginTransaction().replace(R.id.main_content, categoryFragment).commit();
                        getSupportActionBar().setTitle("MBA");
                        break;
                    /*case 1:
                        categoryFragment = new CategoryFragment();

                        categoryFragment.addSubCategory("2", user.getUser_id());

                        fragmentManager.beginTransaction().replace(R.id.main_content, categoryFragment).commit();
                        getSupportActionBar().setTitle("UPSC");
                    */
                    case 1:
                        frameLayout.setVisibility(View.GONE);
                        categoryFragment = new CategoryFragment();

                        categoryFragment.addSubCategory("6", "6", user.getUser_id());

                        fragmentManager.beginTransaction().replace(R.id.main_content, categoryFragment).commit();
                        getSupportActionBar().setTitle("General Knowledge");
                        break;

          /*          case 3:
                        categoryFragment = new CategoryFragment();
                        categoryFragment.addSubCategory("13", user.getUser_id());

                        fragmentManager.beginTransaction().replace(R.id.main_content, categoryFragment).commit();
                        getSupportActionBar().setTitle("Finance");
                        break;
          *//*          case 4:
                        categoryFragment = new CategoryFragment();
                        categoryFragment.addSubCategory("14", user.getUser_id());

                        fragmentManager.beginTransaction().replace(R.id.main_content, categoryFragment).commit();
                        navigationView.setCheckedItem(R.id.marketing);
                        getSupportActionBar().setTitle("Marketing");
                        break;

                    case 3:
                        categoryFragment = new CategoryFragment();
                        categoryFragment.addSubCategory("15", user.getUser_id());

                        fragmentManager.beginTransaction().replace(R.id.main_content, categoryFragment).commit();
                        getSupportActionBar().setTitle("Certificate Courses");
                        break;

                    case 4:
                        openMyCoursesFragment();
            */
                    case 2:
                        frameLayout.setVisibility(View.GONE);
                        myCourse();
                        fragmentManager.beginTransaction().replace(R.id.main_content, myCoursesFragement).commit();
                        getSupportActionBar().setTitle("My Courses");

                        break;
                    case 3:
                        frameLayout.setVisibility(View.VISIBLE);
                        fragmentManager.beginTransaction().replace(R.id.main_content, exploreNew).commit();
                        getSupportActionBar().setTitle("Explorer");
                        initializevideo();

                        break;

                    case 4:
                        frameLayout.setVisibility(View.GONE);
                        initArticle();
                        fragmentManager.beginTransaction().replace(R.id.main_content, myArticleFragment).commit();
                        getSupportActionBar().setTitle("Articles");
                        break;
                    case 5:
                        startActivity(new Intent(MyCourses.this, InstructorsListActivity.class));
                        break;
                    case 6:
                        frameLayout.setVisibility(View.GONE);
                        fragmentManager.beginTransaction().replace(R.id.main_content, whisListFragement).commit();
                        getSupportActionBar().setTitle("Wish List");
                        break;
                    case 7:
                        startActivity(new Intent(MyCourses.this, MyProfile_2.class));
                        break;
                    case 8:
                        mAuth = FirebaseAuth.getInstance();
                        if (mAuth != null) {
                            mAuth.signOut();
                            LoginManager.getInstance().logOut();
                        }
                        Paper.delete("user");
                        startActivity(new Intent(MyCourses.this, MainActivity.class));
                        finish();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        // Binding NavigationView To Toolbar
        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String cache = Paper.book().read("user");
        if (cache != null && !cache.isEmpty()) {
            user = new Gson().fromJson(cache, User.class);

            profile_pic_url = user.getUser_photo().replace("\\", "");
            mUsername = user.getUser_username();
            mEmail = user.getUser_email();
        }


        whisListFragement = new WhisListFragement();
        myCoursesFragement = new MyCoursesFragment();
        myArticleFragment = new ArticlesFragment();
        exploreNew = new ExploreNew();
        categoryFragment = new CategoryFragment();
        cartFragment = new CartFragment();

        names = new ArrayList<>();
        urls = new ArrayList<>();
        ids = new ArrayList<>();
/*

        RequestQueue requestQueue1 = Volley.newRequestQueue(MyCourses.this);
        final String url1 = "https://careeranna.com/api/getMyCourse.php?user="+user.getUser_id()+"&category=15";
        final String url1 = "http://careeranna.com/apigetMyCourse.php?user="+user.getUser_id()+"&category=15";
        Log.d("url_res", url1);
        StringRequest stringRequest1  = new StringRequest(Request.Method.GET, url1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("url_response", response.toString());
                            JSONArray CategoryArray = new JSONArray(response);
                            for(int i=0;i<10;i++) {
                                JSONObject Category = CategoryArray.getJSONObject(i);
                                ids.add(Category.getString("product_id"));
                                names.add(Category.getString("product_name"));
                                urls.add(Category.getString("product_image").replace("\\",""));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        myCoursesFragement.add(names, urls, ids);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                    }
                });

        requestQueue1.add(stringRequest1);
*/

        getSupportActionBar().setTitle("Explore");

        /*
        LAUNCH EXPLORE FRAGMENT BY DEFAULT
         */


        if(amIConnect()) {
        Bundle extras = getIntent().getExtras();
        Log.d(TAG, "Trying to get intent, " + (extras == null));
        if (extras != null && extras.getBoolean(Constants.OPEN_MY_COURSES_INTENT)) {
            openMyCoursesFragment();
        } else {
            fragmentManager.beginTransaction().replace(R.id.main_content, exploreNew).commit();
            initializevideo();
        }
        } else {
            frameLayout.setVisibility(View.GONE);
            fragmentManager.beginTransaction().replace(R.id.main_content, noInternetFragement).commit();
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

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://careeranna.com/api/banner.php";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
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

            frameLayout.setVisibility(View.GONE);
            getSupportActionBar().setTitle("My Cart");

            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_content, cartFragment).commit();
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
            Log.d("in_try", language +" ");
        } catch (NullPointerException e){
            language = 1;
            Log.d("in_catch", language +" ");
            Paper.book().write(Constants.LANGUAGE, language);
        }

    }

    @Override
    protected void onPause() {

        super.onPause();
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

        int id = menuItem.getItemId();

        String cart = Paper.book().read("cart");

        if (cart != null && !cart.isEmpty()) {
            Gson gson = new Gson();

            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();

            ArrayList<String> arrayList = gson.fromJson(cart, type);

            setCount(this, arrayList.size() + "");

        } else {
            setCount(this, "0");
        }

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
        final String url1 = "https://careeranna.com/api/getMyCourse.php?user=" + user.getUser_id();
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
                                names.add(new CourseWithLessData(Category.getString("product_name"),
                                        Category.getString("product_id"),
                                        Category.getString("product_image").replace("\\", ""),
                                        "0",
                                        Category.getString("category_id")
                                ));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        myCoursesFragement.add(names);
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

        mArticles = new ArrayList<>();

        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage(getString(R.string.loading_just_a_sec));
        progressDialog.show();

        progressDialog.setCancelable(false);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://careeranna.com/api/articlewithimage.php";
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
                                        "https://www.careeranna.com/articles/wp-content/uploads/" + Articles.getString("meta_value").replace("\\", ""),
                                        Articles.getString("display_name"),
                                        "CAT",
                                        Articles.getString("post_content"),
                                        Articles.getString("post_date")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        progressDialog.dismiss();
                        myArticleFragment.setmArticles(mArticles);
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

    public void initCategory() {

        categories = new ArrayList<>();

        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage(getString(R.string.loading_just_a_sec));
        progressDialog.show();

        progressDialog.setCancelable(false);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "http://careeranna.com/apicategory.php";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("url_response", response.toString());
                            JSONArray CategoryArray = new JSONArray(response.toString());
                            for (int i = 0; i < CategoryArray.length(); i++) {
                                JSONObject Category = CategoryArray.getJSONObject(i);
                                categories.add(new Category(Integer.toString(Category.getInt("CATEGORY_ID")),
                                        Category.getString("CATEGORY_NAME"),
                                        Category.getString("cimage").replace("\\", "")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        RequestQueue requestQueue1 = Volley.newRequestQueue(MyCourses.this);
                        String url1 = "http://careeranna.com/api/getCertficateCourse.php";
                        Log.d("url_res", url1);
                        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url1,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            Log.i("url_response", response.toString());
                                            JSONArray CategoryArray = new JSONArray(response.toString());
                                            for (int i = 0; i < 10; i++) {
                                                JSONObject Category = CategoryArray.getJSONObject(i);
                                                courses.add(new Course(Category.getString("product_id"),
                                                        Category.getString("course_name"),
                                                        "https://www.careeranna.com/" + Category.getString("product_image").replace("\\", ""),
                                                        "15",
                                                        Category.getString("discount")
                                                        , Category.getString("description"),
                                                        Category.getString("video_url").replace("\\", "")));
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        progressDialog.dismiss();
                                        addExam();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        progressDialog.dismiss();
                                        addExam();
                                    }
                                }
                        );

                        requestQueue1.add(stringRequest1);
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

    private void addExam() {

        courses = new ArrayList<>();

        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage(getString(R.string.loading_just_a_sec));
        progressDialog.show();

        progressDialog.setCancelable(false);

        RequestQueue requestQueue1 = Volley.newRequestQueue(MyCourses.this);
        String url1 = "https://careeranna.com/api/getAllCourse.php";
        Log.d("url_res", url1);
        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            courses = new ArrayList<>();
                            names = new ArrayList<>();
                            urls = new ArrayList<>();

                            Log.i("url_response", response.toString());
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray CategoryArray = jsonObject.optJSONArray("paid");
                            for (int i = 0; i < CategoryArray.length(); i++) {
                                JSONObject Category = CategoryArray.getJSONObject(i);
                                courses.add(new Course(Category.getString("product_id"),
                                        Category.getString("course_name"),
                                        "https://www.careeranna.com/" + Category.getString("product_image").replace("\\", ""),
                                        Category.getString("exam_id"),
                                        Category.getString("discount")
                                        , "",
                                        Category.getString("video_url").replace("\\", "")));
                                courses.get(i).setType("Paid");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        progressDialog.dismiss();

                        addFree();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                        addFree();
                    }
                }
        );

        requestQueue1.add(stringRequest1);
    }

    private void addFree() {

        freecourse = new ArrayList<>();

        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage(getString(R.string.loading_just_a_sec));
        progressDialog.show();

        progressDialog.setCancelable(false);

        RequestQueue requestQueue1 = Volley.newRequestQueue(MyCourses.this);
        String url1 = "https://careeranna.com/api/getFreeCourse.php";
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
                                freecourse.add(new Course(Category.getString("course_id"),
                                        Category.getString("name"),
                                        "https://www.careeranna.com/" + Category.getString("image").replace("\\", ""),
                                        Category.getString("eid"),
                                        Category.getString("discount")
                                        , "meta_description", ""));
                                freecourse.get(i).setType("Free");
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

    private boolean amIConnect() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    private void checkUpdates() {

        final String[] versionUpdate = {""};
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://careeranna.com/api/updateVersion.php";
        StringRequest str = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("App", response);
                            JSONObject spo = new JSONObject(response);
                            versionUpdate[0] = spo.getString("version_name");
                            String versionName = BuildConfig.VERSION_NAME;
                            if (!versionUpdate[0].equals(versionName)) {
                                alertDialogForUpdate();
                            }
                        } catch (JSONException e) {
                            Log.e("error_coce", e.getMessage());
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
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setCancelable(false);

        builder.setMessage(getString(R.string.update_your_app))
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(
                                new Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("https://play.google.com/store/apps/details?id=com.careeranna.careeranna"
                                        )
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
        fragmentManager.beginTransaction().replace(R.id.main_content, myCoursesFragement).commit();
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("My Courses");
    }

    private void initializevideo() {

        trendingvVideosForExplore = new ArrayList<>();
        freeVideosForExplore = new ArrayList<>();

        try {
            language = Paper.book().read(Constants.LANGUAGE);
            Log.d("in_try", language +" ");
        } catch (Exception e){
            language = 1;
            Log.d("in_catch", language +" ");
            Paper.book().write(Constants.LANGUAGE, language);
        }


        relativeLayout.setVisibility(View.VISIBLE);

            RequestQueue requestQueue1 = Volley.newRequestQueue(this);
            String url1 = "https://careeranna.com/api/getTrendingVideos.php?id="+String.valueOf(language);
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
                                    trendingvVideosForExplore.add(new FreeVideos(
                                            videos.getString("id"),
                                            videos.getString("video_url").replace("\\", ""),
                                            "https://www.careeranna.com/thumbnail/" + videos.getString("thumbnail"),
                                            videos.getString("totalViews"),
                                            videos.getString("tags"),
                                            videos.getString("heading"),
                                            "Free",
                                            videos.getString("duration")));
                                    trendingvVideosForExplore.get(i).setType("Trending");
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            RequestQueue requestQueue1 = Volley.newRequestQueue(MyCourses.this);
                            String url1 = "https://careeranna.com/api/getFreeVideos.php?id="+String.valueOf(language);
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
                                                            videos.getString("duration")
                                                    ));
                                                    freeVideosForExplore.get(i).setType("Latest");
                                                }

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            addPaidCourse();
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                            addPaidCourse();
                                        }
                                    });

                            requestQueue1.add(stringRequest1);


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            addPaidCourse();
                        }
                    });

            requestQueue1.add(stringRequest1);

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


                        addFreeForExplore();
                    }
                }
        );

        requestQueue1.add(stringRequest1);
    }

    private void addFreeForExplore() {

        freecourseForExplore = new ArrayList<>();

        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        String url1 = "https://careeranna.com/api/getFreeCourse.php";
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
                                freecourseForExplore.add(new Course(Category.getString("course_id"),
                                        Category.getString("name"),
                                        "https://www.careeranna.com/" + Category.getString("image").replace("\\", ""),
                                        Category.getString("eid"),
                                        "0"
                                        , "meta_description", ""));
                                freecourseForExplore.get(i).setType("Free");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        relativeLayout.setVisibility(View.GONE);

                        exploreNew.addFree(freeVideosForExplore, trendingvVideosForExplore, coursesForExplore, freecourseForExplore);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        exploreNew.addFree(freeVideosForExplore, trendingvVideosForExplore, coursesForExplore, freecourseForExplore);
                        relativeLayout.setVisibility(View.GONE);
                    }
                }
        );

        requestQueue1.add(stringRequest1);
    }

    @Override
    public void onItemClick1() {
        switch (fragement_id) {
            case 0:
                frameLayout.setVisibility(View.GONE);
                categoryFragment = new CategoryFragment();
                categoryFragment.addSubCategory("1", "4", user.getUser_id());

                fragmentManager.beginTransaction().replace(R.id.main_content, categoryFragment).commit();
                getSupportActionBar().setTitle("MBA");
                break;
                    /*case 1:
                        categoryFragment = new CategoryFragment();

                        categoryFragment.addSubCategory("2", user.getUser_id());

                        fragmentManager.beginTransaction().replace(R.id.main_content, categoryFragment).commit();
                        getSupportActionBar().setTitle("UPSC");
                    */
            case 1:
                frameLayout.setVisibility(View.GONE);
                categoryFragment = new CategoryFragment();

                categoryFragment.addSubCategory("6", "6", user.getUser_id());

                fragmentManager.beginTransaction().replace(R.id.main_content, categoryFragment).commit();
                getSupportActionBar().setTitle("General Knowledge");
                break;

          /*          case 3:
                        categoryFragment = new CategoryFragment();
                        categoryFragment.addSubCategory("13", user.getUser_id());

                        fragmentManager.beginTransaction().replace(R.id.main_content, categoryFragment).commit();
                        getSupportActionBar().setTitle("Finance");
                        break;
          *//*          case 4:
                        categoryFragment = new CategoryFragment();
                        categoryFragment.addSubCategory("14", user.getUser_id());

                        fragmentManager.beginTransaction().replace(R.id.main_content, categoryFragment).commit();
                        navigationView.setCheckedItem(R.id.marketing);
                        getSupportActionBar().setTitle("Marketing");
                        break;

                    case 3:
                        categoryFragment = new CategoryFragment();
                        categoryFragment.addSubCategory("15", user.getUser_id());

                        fragmentManager.beginTransaction().replace(R.id.main_content, categoryFragment).commit();
                        getSupportActionBar().setTitle("Certificate Courses");
                        break;

                    case 4:
                        openMyCoursesFragment();
            */
            case 2:
                frameLayout.setVisibility(View.GONE);
                myCourse();
                fragmentManager.beginTransaction().replace(R.id.main_content, myCoursesFragement).commit();
                getSupportActionBar().setTitle("My Courses");

                break;
            case 3:
                frameLayout.setVisibility(View.VISIBLE);
                fragmentManager.beginTransaction().replace(R.id.main_content, exploreNew).commit();
                getSupportActionBar().setTitle("Explorer");
                initializevideo();

                break;

            case 4:
                frameLayout.setVisibility(View.GONE);
                initArticle();
                fragmentManager.beginTransaction().replace(R.id.main_content, myArticleFragment).commit();
                getSupportActionBar().setTitle("Articles");
                break;
            case 5:
                startActivity(new Intent(MyCourses.this, InstructorsListActivity.class));
                break;
            case 6:
                frameLayout.setVisibility(View.GONE);
                fragmentManager.beginTransaction().replace(R.id.main_content, whisListFragement).commit();
                getSupportActionBar().setTitle("Wish List");
                break;
            case 7:
                startActivity(new Intent(MyCourses.this, MyProfile_2.class));
                break;
            case 8:
                mAuth = FirebaseAuth.getInstance();
                if (mAuth != null) {
                    mAuth.signOut();
                    LoginManager.getInstance().logOut();
                }
                Paper.delete("user");
                startActivity(new Intent(MyCourses.this, MainActivity.class));
                finish();
                break;
        }
    }

    public void changeInternet() {
        frameLayout.setVisibility(View.GONE);
        fragmentManager.beginTransaction().replace(R.id.main_content, noInternetFragement).commit();
    }
}

