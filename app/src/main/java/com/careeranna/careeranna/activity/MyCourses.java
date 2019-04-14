package com.careeranna.careeranna.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.adapter.ListViewAdapter;
import com.careeranna.careeranna.data.Article;
import com.careeranna.careeranna.data.Constants;
import com.careeranna.careeranna.data.Course;
import com.careeranna.careeranna.data.CourseWithLessData;
import com.careeranna.careeranna.data.FreeVideos;
import com.careeranna.careeranna.data.MenuList;
import com.careeranna.careeranna.data.MyPaidCourse;
import com.careeranna.careeranna.data.UrlConstants;
import com.careeranna.careeranna.data.User;
import com.careeranna.careeranna.fragement.ErrorOccurredFragment;
import com.careeranna.careeranna.fragement.NoInternetFragment;
import com.careeranna.careeranna.fragement.dashboard_fragements.ArticlesFragment;
import com.careeranna.careeranna.fragement.dashboard_fragements.CartFragment;
import com.careeranna.careeranna.fragement.dashboard_fragements.FreeCoursesFragment;
import com.careeranna.careeranna.fragement.dashboard_fragements.MyCoursesFragment;
import com.careeranna.careeranna.fragement.dashboard_fragements.PaidCoursesFragment;
import com.careeranna.careeranna.fragement.dashboard_fragements.WishListFragment;
import com.careeranna.careeranna.helper.CountDrawable;
import com.careeranna.careeranna.user.MyProfile_2;
import com.google.firebase.auth.FirebaseAuth;
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

    private static final String TAG = "MyCoursesActivity";

    private DrawerLayout drawerLayout;

    private ActionBarDrawerToggle mToggle;

    private WishListFragment wishListFragment;
    private RelativeLayout relativeLayout;

    private Menu menu;

    private int fragment_id = 2;

    private LinearLayout linearLayout;

    private FirebaseAuth mAuth;

    private String mUsername;
    private String profile_pic_url;
    private String mEmail;

    private ArrayList<FreeVideos> freeVideosForExplore;
    private ArrayList<FreeVideos> trendingVideosForExplore;

    private ArrayList<Course> coursesForExplore;
    private ArrayList<Course> freeCourseForExplore;

    private MyCoursesFragment myCoursesFragment;
    private NoInternetFragment noInternetFragment;
    private ArticlesFragment myArticleFragment;
    private ErrorOccurredFragment errorOccurredFragment;
    private FreeCoursesFragment freeCoursesFragment;
    private PaidCoursesFragment paidCoursesFragment;

    private FragmentManager fragmentManager;

    private ViewPager viewPager;

    private ProgressDialog progressDialog;

    private User user;

    private CartFragment cartFragment;
    private ArrayList<Article> mArticles;

    ArrayList<MyPaidCourse> purchasedPaidCourses;

    private FrameLayout frameLayout;

    private ArrayList<MenuList> your_array_list;

    private long backPressed;

    private RequestQueue requestQueue;

    private NavigationView navigationView;

    private ArrayList<CourseWithLessData> myCourses;

    private ListView listView;

    private int menuToChoose = R.menu.add_cart;


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        this.menu = menu;

        setCartItemCountOnMenu();

        return true;
    }

    private void setCartItemCountOnMenu() {

        String cartItemCount = Paper.book().read("cart1");

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

    private void setCount(Context context, String count) {

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
        cartFragment = new CartFragment();
        paidCoursesFragment = new PaidCoursesFragment();

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


        myCourses = new ArrayList<>();


        /*
        LAUNCH EXPLORE FRAGMENT BY DEFAULT
         */


        if (amIConnect()) {
            Bundle extras = getIntent().getExtras();
            Log.d(TAG, "Trying to get intent, " + (extras == null));
            if (extras != null && extras.getBoolean(Constants.OPEN_MY_COURSES_INTENT)) {
                openMyCoursesFragment();
            } else {
                if(getIntent().hasExtra("fragment_name")) {
                    if(getIntent().getStringExtra("fragment_name").equals("cart")) {
                        cartFragment = new CartFragment();
                        menuToChoose = R.menu.notification;
                        invalidateOptionsMenu();

                        frameLayout.setVisibility(GONE);
                        if (getSupportActionBar() != null) {
                            getSupportActionBar().setTitle(getResources().getString(R.string.my_cart));
                        }

                        relativeLayout.setVisibility(GONE);
                        fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.main_content, cartFragment).commit();
                    } else if(getIntent().getStringExtra("fragment_name").equals("my_course")) {
                        changeFragmentToMyDashboard();
                    }
                } else {
                   changeFragmentToMyDashboard();
                }
            }
        } else {
            frameLayout.setVisibility(GONE);
            fragmentManager.beginTransaction().replace(R.id.main_content, noInternetFragment).commit();
        }

        // Set Navigation View Information
        setNavigationView();

    }

    private void populateNavigation() {

        your_array_list = new ArrayList<>();
        your_array_list.add(new MenuList(getResources().getString(R.string.premium_courses), getApplicationContext().getResources().getDrawable(R.drawable.ic_scholarship)));
        your_array_list.add(new MenuList(getResources().getString(R.string.free_courses), getApplicationContext().getResources().getDrawable(R.drawable.ic_free)));
        your_array_list.add(new MenuList("My Dashboard", getApplicationContext().getResources().getDrawable(R.drawable.ic_noun_book)));
        your_array_list.add(new MenuList(getResources().getString(R.string.articles), getApplicationContext().getResources().getDrawable(R.drawable.ic_article_1)));
        your_array_list.add(new MenuList(getResources().getString(R.string.my_profile), getApplicationContext().getResources().getDrawable(R.drawable.ic_settings)));
        your_array_list.add(new MenuList(getResources().getString(R.string.signout), getApplicationContext().getResources().getDrawable(R.drawable.ic_logout_1), "#FFDA3C21", "#FFF5F3F3", Gravity.CENTER, View.INVISIBLE));

        ListViewAdapter adapter = new ListViewAdapter(this, your_array_list);

        listView.setAdapter(adapter);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("My Dashboard");
        }
    }

    private void changeFragmentWithNav() {
        switch (fragment_id) {
            case 0:
                frameLayout.setVisibility(GONE);
                paidCoursesFragment = new PaidCoursesFragment();

                fragmentManager.beginTransaction().replace(R.id.main_content, paidCoursesFragment).commit();
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(getResources().getString(R.string.premium_courses));
                }
                break;
            case 1:
                frameLayout.setVisibility(GONE);
                freeCoursesFragment = new FreeCoursesFragment();

                fragmentManager.beginTransaction().replace(R.id.main_content, freeCoursesFragment).commit();
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(getResources().getString(R.string.free_courses));
                }
                break;
            case 2:
                changeFragmentToMyDashboard();
                break;
            case 3:
                frameLayout.setVisibility(GONE);
                initArticle();
                fragmentManager.beginTransaction().replace(R.id.main_content, myArticleFragment).commit();
                if(getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(getResources().getString(R.string.articles));
                }
                break;
            case 4:
                startActivity(new Intent(MyCourses.this, MyProfile_2.class));
                break;
            case 5:
                mAuth = FirebaseAuth.getInstance();
                if (mAuth != null) {
                    mAuth.signOut();
                }
                Paper.delete("user");
                startActivity(new Intent(MyCourses.this, SignInActivity.class));
                finish();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(menuToChoose, menu);
        return true;
    }

    private void setNavigationView() {

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

        String cache = Paper.book().read("user");
        if (cache != null && !cache.isEmpty()) {
            User user = new Gson().fromJson(cache, User.class);

            profile_pic_url = user.getUser_photo().replace("//", "");
            mUsername = user.getUser_username();
            mEmail = user.getUser_email();
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
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        setCartItemCountOnMenu();

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openMyCoursesFragment() {
        myCourse();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_content, myCoursesFragment).commit();
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(getResources().getString(R.string.mycourses));
    }

    @Override
    public void onItemClick1() {
        changeFragmentWithNav();
    }

    public void changeInternet() {

        noInternetFragment = new NoInternetFragment();
        noInternetFragment.setOnFragementClicklistener(this);
        frameLayout.setVisibility(GONE);
        fragmentManager.beginTransaction().replace(R.id.main_content, noInternetFragment).commit();
    }

    private void showErrorFragment() {

        errorOccurredFragment = new ErrorOccurredFragment();
        errorOccurredFragment.setOnFragementClicklistener(this);
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
                            JSONObject spo = new JSONObject(response);
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


    private void changeFragmentToMyDashboard() {

        frameLayout.setVisibility(GONE);
        myCourse();
        fragmentManager.beginTransaction().replace(R.id.main_content, myCoursesFragment).commit();
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("My Dashboard");
        }
    }

    private void myCourse() {

        myCourses = new ArrayList<>();

        relativeLayout.setVisibility(View.VISIBLE);

        RequestQueue requestQueue = Volley.newRequestQueue(MyCourses.this);
        final String myCourseUrl = UrlConstants.USER_COURSES_URL + user.getUser_id();
        StringRequest stringRequest1 = new StringRequest(Request.Method.GET,
                myCourseUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray coursesArray1 = jsonObject.getJSONArray("paid");
                            for (int i = 0; i < coursesArray1.length(); i++) {
                                JSONObject Category = coursesArray1.getJSONObject(i);
                                myCourses.add(new CourseWithLessData(Category.getString("product_name"),
                                        Category.getString("product_id"),
                                        Category.getString("product_image"),
                                        Category.getInt("progress")+"",
                                        "paid"
                                ));
                            }
                            JSONArray coursesArray = jsonObject.getJSONArray("free");
                            for (int i = 0; i < coursesArray.length(); i++) {
                                JSONObject Category = coursesArray.getJSONObject(i);
                                myCourses.add(new CourseWithLessData(Category.getString("course_name"),
                                        Category.getString("course_id"),
                                        "https://www.careeranna.com/"+Category.getString("course_image"),
                                        Category.getInt("progress")+"",
                                        "free"
                                ));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        myCoursesFragment.add(myCourses);

                        relativeLayout.setVisibility(GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        relativeLayout.setVisibility(View.GONE);
                        showErrorFragment();
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

        if(amIConnect()) {
            requestQueue.add(stringRequest1);
        } else {
            changeInternet();
        }
    }

    private void initArticle() {

        mArticles = new ArrayList<>();

        relativeLayout.setVisibility(View.VISIBLE);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String img_url = "https://www.careeranna.com/articles/wp-content/uploads/";
        String url = "https://careeranna.com/api/articlewithimage.php";

        final String finalImg_url = img_url;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray ArticlesArray = new JSONArray(response);
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

                        relativeLayout.setVisibility(View.GONE);
                        myArticleFragment.setmArticles(mArticles);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        relativeLayout.setVisibility(View.GONE);
                        showErrorFragment();
                    }
                }
        );


        if(amIConnect()) {
            requestQueue.add(stringRequest);
        } else {
            changeInternet();
        }
    }

}

