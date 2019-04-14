package com.careeranna.careeranna;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.careeranna.careeranna.data.Topic;
import com.careeranna.careeranna.data.Unit;
import com.careeranna.careeranna.data.User;
import com.careeranna.careeranna.fragement.NoInternetFragment;
import com.careeranna.careeranna.fragement.profile_fragements.NotesFragment;
import com.careeranna.careeranna.fragement.profile_fragements.TestFragment;
import com.careeranna.careeranna.fragement.profile_fragements.TutorialFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class Exams extends AppCompatActivity implements NoInternetFragment.OnFragemntClickListener {

    public static final String TAG = "Exams";

//    private DrawerLayout drawerLayout;
//    private ActionBarDrawerToggle mToggle;

    CircleImageView imageView;
    TextView userName, userEmail;
    FirebaseAuth mAuth;

//    NavigationView navigationView;
    TutorialFragment tutorialFragment;
    NotesFragment notesFragment;
    TestFragment testFragment;
    NoInternetFragment noInternetFragment;

    RelativeLayout relativeLayout;

    Boolean isMock = false;


    TabLayout tabLayout;

    private Context context;

    ViewPager viewPager;

    User user;

    String id, name, urls, type;

    int fragement_id;

    ProgressDialog progressDialog;

    String mUsername, profile_pic_url, mEmail;

    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exams);

        Log.d(TAG, "onCreate: ");

//        navigationView = findViewById(R.id.nav_view1);
//        drawerLayout = findViewById(R.id.drawelayout1);

        relativeLayout = findViewById(R.id.relative_loading);

        mAuth = FirebaseAuth.getInstance();

//        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

//        drawerLayout.addDrawerListener(mToggle);

        fragement_id = R.id.tutorial;

        initializeFragement();

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager_courses);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        viewPager.setOffscreenPageLimit(3);

        id = getIntent().getStringExtra("course_ids");
        name = getIntent().getStringExtra("course_name");
        urls = getIntent().getStringExtra("course_image");
        type = getIntent().getStringExtra("type");

        String cache = Paper.book().read("user");
        if (cache != null && !cache.isEmpty()) {
            user = new Gson().fromJson(cache, User.class);

            profile_pic_url = user.getUser_photo().replace("\\", "");
            mUsername = user.getUser_username();
            mEmail = user.getUser_email();
        }

        tabLayout.addTab(tabLayout.newTab().setText("Videos"));

        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(name);
        }

        if(!type.equals("free")) {
            tabLayout.addTab(tabLayout.newTab().setText("E-Books"));
            tabLayout.addTab(tabLayout.newTab().setText("Test"));
        }

        setViewPager();
//        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Paper.init(this);


//
//        navigationView.setNavigationItemSelectedListener(this);
//
//        setHeader();

//        navigationView.setCheckedItem(R.id.tutorial);


//        fragmentManager = getSupportFragmentManager();

    }


    public class PagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;

        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    if(isMock) {
                        return "E-Books";
                    }
                    return "Videos";
                case 1:
                    if(isMock) {
                        return "Test";
                    }
                    return "E-Books";
                case 2:
                    return "Test";
                default:
                    return  "";
            }
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    if(isMock) {
                        fetchPdf();
                        return notesFragment;
                    } else {
                        fetchUnit();
                        return tutorialFragment;
                    }

                case 1:
                    if(isMock) {
                        return testFragment;
                    } else {
                        fetchPdf();
                        return notesFragment;
                    }
                case 2:
                    return testFragment;

            }

            return noInternetFragment;
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }


    public TutorialFragment getTutorialFragment() {
        return tutorialFragment;
    }

    private void initializeFragement() {

        noInternetFragment = new NoInternetFragment();
        noInternetFragment.setOnFragementClicklistener(this);
        tutorialFragment = new TutorialFragment();
        notesFragment = new NotesFragment();
        testFragment = new TestFragment();
    }

//    private void setHeader() {
//
//        View headerView = navigationView.getHeaderView(0);
//
//        CircleImageView profile = headerView.findViewById(R.id.navImage);
//        userName = headerView.findViewById(R.id.navUsername);
//        userEmail = headerView.findViewById(R.id.navUseremail);
//
//        /*
//        If there is no image url provided, then write the initial letter of the username
//         */
//        TextView initialAlphabet = headerView.findViewById(R.id.nav_username_initial);
//        if (!profile_pic_url.isEmpty()) {
//            Glide.with(this).load(profile_pic_url).into(profile);
//            initialAlphabet.setVisibility(View.INVISIBLE);
//        } else {
//        }
//        userName.setText(mUsername);
//        userEmail.setText(mEmail);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

//        if (mToggle.onOptionsItemSelected(item)) {
//            return true;
//        }

    }

    private void fetchUnit() {

        relativeLayout.setVisibility(View.VISIBLE);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://careeranna.com/api/getCourseVideosOfUserApp.php?product_id=" + id + "&user=" + user.getUser_id();
        Log.i("url", url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<Unit> mUnits = new ArrayList<>();
                        String last_played_id = "";
                        String shareUrl = "";
                        try {
                            if (!response.equals("No results")) {
//                                navigationView.setCheckedItem(R.id.tutorial);
                                Drawable check = getApplicationContext().getResources().getDrawable(R.drawable.ic_check_circle_black_24dp);
                                JSONObject obj = new JSONObject(response);
                                JSONArray watched = obj.getJSONArray("watched");

                                ArrayList<String> userWatched = new ArrayList<>();
                                for (int i = 0; i < watched.length(); i++) {
                                    JSONObject jsonObject = watched.getJSONObject(i);
                                    userWatched.add(jsonObject.getString("video_id"));
                                }
                                JSONArray videosArray = obj.getJSONArray("videos");
                                for (int i = 0; i < videosArray.length(); i++) {
                                    JSONObject jsonObject = videosArray.getJSONObject(i);
                                    if (jsonObject.has("main")) {
                                        mUnits.add(new Unit(jsonObject.getString("main"), check));
                                    }
                                    if (jsonObject.has("heading")) {
                                        if (mUnits.size() == 0) {
                                            Unit unit = new Unit(name, check);
                                            mUnits.add(unit);
                                        }
                                        if(userWatched.contains(jsonObject.getString("id"))) {
                                            mUnits.get(mUnits.size() - 1)
                                                    .topics.add(new Topic(
                                                    jsonObject.getString("id"),
                                                    jsonObject.getString("heading"),
                                                    jsonObject.getString("video_url"),
                                                    true,
                                                    jsonObject.getString("duration")

                                            ));
                                            last_played_id = jsonObject.getString("video_url");
                                            shareUrl = jsonObject.getString("heading");
                                        } else {
                                            mUnits.get(mUnits.size() - 1)
                                                    .topics.add(new Topic(
                                                    jsonObject.getString("id"),
                                                    jsonObject.getString("heading"),
                                                    jsonObject.getString("video_url"),
                                                    false,
                                                    jsonObject.getString("duration")

                                            ));
                                        }

                                    }
                                }
                                tutorialFragment.addCourseUnits(mUnits,last_played_id, shareUrl);
                                tutorialFragment.addType(type);
                                relativeLayout.setVisibility(View.GONE);
                            } else {

                                relativeLayout.setVisibility(View.GONE);
                                tabLayout.removeAllTabs();

                                isMock = true;
                                tabLayout.addTab(tabLayout.newTab().setText("E-Books"));
                                tabLayout.addTab(tabLayout.newTab().setText("Test"));

                                setViewPager();

//                                navigationView.setCheckedItem(R.id.notes);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Exams.this, "Error Occured", Toast.LENGTH_SHORT).show();
                if(progressDialog != null) {
                    progressDialog.dismiss();
                }
                relativeLayout.setVisibility(View.GONE);
            }
        });
        requestQueue.add(stringRequest);
    }

    private void fetchPdf() {

        relativeLayout.setVisibility(View.VISIBLE);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://www.careeranna.com/api/getPdfWithHeading.php?id=" + id;
        Log.i("url", url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!response.equals("No results")) {
                            String status = "No pdf";
                            ArrayList<String[]> pdf_links_and_titles = new ArrayList<>();

                            try {
                                Log.i("pdf", response);
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray pdfs_links_json = jsonObject.getJSONArray("pdfs");
                                JSONArray pdfs_title_json = jsonObject.getJSONArray("materials");

                                Log.d(TAG, "onResponse: " + pdfs_links_json);

                                for (int i = 0; i < pdfs_links_json.length(); i++) {
                                    /*
                                    The first element of the array link_and_title is the link of the pdf and the second element
                                    is the title of the pdf.
                                     */
                                    String[] link_and_title = new String[2];
                                    link_and_title[0] = pdfs_links_json.getString(i);
                                    link_and_title[1] = pdfs_title_json.getString(i);
                                    pdf_links_and_titles.add(link_and_title);
                                }

                                if (!pdf_links_and_titles.isEmpty()) {
                                    status = "Select your notes from here:";
                                }

                            } catch (JSONException e) {
                                Log.d(TAG, "onResponse: Cannot parse");
                                e.printStackTrace();
                                status = "No Pdf";
                            }

                            relativeLayout.setVisibility(View.GONE);

                            notesFragment.addPDFs(pdf_links_and_titles, status);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Exams.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                relativeLayout.setVisibility(View.GONE);
            }
        });
        requestQueue.add(stringRequest);
    }

    public void removeActionBar() {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null && actionBar.isShowing()) {
            actionBar.hide();
        }
    }

    public void showActionBar() {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null && !actionBar.isShowing()) {
            actionBar.show();
        }
    }

    @Override
    public void onBackPressed() {
        if (tutorialFragment.isPlayerFullscreen())
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        else
            super.onBackPressed();
    }


    private boolean amIConnect() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    @Override
    public void onItemClick1() {
        recreate();
    }

    public void setViewPager() {
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount()));

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setVisibility(View.VISIBLE);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}



