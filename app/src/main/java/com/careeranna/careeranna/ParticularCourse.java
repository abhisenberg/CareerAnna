package com.careeranna.careeranna;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.careeranna.careeranna.activity.MainActivity;
import com.careeranna.careeranna.activity.MyCourses;
import com.careeranna.careeranna.data.Topic;
import com.careeranna.careeranna.data.Unit;
import com.careeranna.careeranna.data.User;
import com.careeranna.careeranna.fragement.profile_fragements.CertificateFragment;
import com.careeranna.careeranna.fragement.profile_fragements.NotesFragment;
import com.careeranna.careeranna.fragement.profile_fragements.TestFragment;
import com.careeranna.careeranna.fragement.profile_fragements.TutorialFragment;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class ParticularCourse extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "ParticularCourse";

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;

    CircleImageView imageView;
    TextView userName, userEmail;
    FirebaseAuth mAuth;

    NavigationView navigationView;
    TutorialFragment tutorialFragment;
    NotesFragment notesFragment;
    TestFragment testFragment;
    CertificateFragment certificateFragment;

    User user;

    String material;

    String id, name, urls;

    ProgressDialog progressDialog;

    String mUsername, profile_pic_url, mEmail;

    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_particular_course);

        Log.d(TAG, "onCreate: ");

        navigationView = findViewById(R.id.nav_view1);
        drawerLayout = findViewById(R.id.drawelayout1);

        mAuth = FirebaseAuth.getInstance();

        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(mToggle);

        mToggle.syncState();

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Paper.init(this);

        id = getIntent().getStringExtra("course_ids");
        name = getIntent().getStringExtra("course_name");
        urls = getIntent().getStringExtra("course_image");

        String cache = Paper.book().read("user");
        if(cache != null && !cache.isEmpty()) {
            user =  new Gson().fromJson(cache, User.class);

            profile_pic_url = user.getUser_photo().replace("\\", "");
            mUsername = user.getUser_username();
            mEmail = user.getUser_email();
        }

        navigationView.setNavigationItemSelectedListener(this);

        setHeader();

        getSupportActionBar().setTitle("Tutorial");
        navigationView.setCheckedItem(R.id.tutorial);

        initializeFragement();

        fetchUnit();

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_content, tutorialFragment).commit();
    }


    private void initializeFragement() {

        tutorialFragment = new TutorialFragment();
        notesFragment = new NotesFragment();
        testFragment = new TestFragment();
        certificateFragment = new CertificateFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.reset_course, menu);
        return true;
    }

    private void setHeader() {

        View headerView = navigationView.getHeaderView(0);

        CircleImageView profile = headerView.findViewById(R.id.navImage);
        userName = headerView.findViewById(R.id.navUsername);
        userEmail = headerView.findViewById(R.id.navUseremail);

        Glide.with(this).load(profile_pic_url).into(profile);
        userName.setText(mUsername);
        userEmail.setText(mEmail);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.reset) {

            Toast.makeText(this, "Reset Your Course", Toast.LENGTH_SHORT).show();
        }


        if(mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();

        if(id == R.id.signout) {

            mAuth.signOut();
            LoginManager.getInstance().logOut();
            startActivity(new Intent(this, MainActivity.class));
            finish();

        } else if(id == R.id.tutorial) {

            fetchUnit();
            navigationView.setCheckedItem(R.id.tutorial);
            fragmentManager.beginTransaction().replace(R.id.main_content,tutorialFragment).commit();
            if(getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Tutorial");
            }
        } else if(id == R.id.notes) {

            fetchPdf();

            navigationView.setCheckedItem(R.id.notes);
            fragmentManager.beginTransaction().replace(R.id.main_content,notesFragment).commit();
            if(getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Notes");
            }
        } else if(id == R.id.test) {

            navigationView.setCheckedItem(R.id.test);
            fragmentManager.beginTransaction().replace(R.id.main_content,testFragment).commit();
            if(getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Test");
            }

        } else if(id == R.id.certificate) {

            navigationView.setCheckedItem(R.id.certificate);
            fragmentManager.beginTransaction().replace(R.id.main_content,certificateFragment).commit();
            if(getSupportActionBar() != null ) {
                getSupportActionBar().setTitle("Certificate");
            }
        } else if(id == R.id.goBackHome) {

            startActivity(new Intent(ParticularCourse.this, MyCourses.class));
            finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }

    private void fetchUnit() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Videos .. ");
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://careeranna.com/api/getCourseVideosOfUserApp.php?product_id="+id;
        Log.i("url", url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<Unit> mUnits = new ArrayList<>();
                        try {
                            if(!response.equals("No results")) {
                                Drawable check = getApplicationContext().getResources().getDrawable(R.drawable.ic_check_circle_black_24dp);
                                    JSONArray jsonArray = new JSONArray(response);
                                    for(int i=0;i<jsonArray.length();i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        if(jsonObject.has("main")) {
                                            mUnits.add(new Unit(jsonObject.getString("main"), check));
                                        }
                                        if(jsonObject.has("heading")) {
                                            if (mUnits.size() == 0) {
                                                Unit unit = new Unit(name, check);
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
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        tutorialFragment.addCourseUnits(mUnits, "", "");
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ParticularCourse.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
        requestQueue.add(stringRequest);
    }

    private void fetchPdf() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading_notes));
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "http://careeranna.com/api/getPdfWithHeading.php?id="+id;
        Log.i("url", url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(!response.equals("No results")) {
                            String status = "No pdf";
                            ArrayList<String[]> pdf_links_and_titles = new ArrayList<String[]>();

                            try {
                                Log.i("pdf", response);
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray pdfs_links_json = jsonObject.getJSONArray("pdfs");
                                JSONArray pdfs_title_json = jsonObject.getJSONArray("materials");

                                for(int i=0; i<pdfs_links_json.length(); i++){
                                    /*
                                    The first element of the array link_and_title is the link of the pdf and the second element
                                    is the title of the pdf.
                                     */
                                    String[] link_and_title = new String[2];
                                    link_and_title[0] = pdfs_links_json.getString(i);
                                    link_and_title[1] = pdfs_title_json.getString(i);
                                    pdf_links_and_titles.add(link_and_title);
                                }

                                if(!pdf_links_and_titles.isEmpty()){
                                    status = "Select your notes from here:";
                                }

                            } catch (JSONException e) {
                                Log.d(TAG, "onResponse: Cannot parse");
                                e.printStackTrace();
                                status = "No Pdf";
                            }
                            progressDialog.dismiss();

                            notesFragment.addPDFs(pdf_links_and_titles, status);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ParticularCourse.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
//        StringRequest stringRequest = new StringRequest(Request.Method.GET,
//                url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        String status = "No pdf";
//                        try {
//                            Log.i("pdf", response);
//                            JSONObject jsonObject = new JSONObject(response);
//                            material = jsonObject.getString("material_file");
//                            if(!material.equals("null")) {
//                                status = "Select your notes from here:";
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            material = "No Pdf";
//                            status = "No Pdf";
//                        }
//                        progressDialog.dismiss();
//                        ArrayList<String> pdfs = new ArrayList<>();
//                        String[] pdfs1 = material.split(",");
//                        for(String pdf : pdfs1) {
//                            pdfs.add(pdf);
//                        }
//                        notesFragment.addPdf(pdfs, status);
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(ParticularCourse.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
//                progressDialog.dismiss();
//            }
//        });
        requestQueue.add(stringRequest);
    }

    public void removeActionBar(){
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null && actionBar.isShowing()){
            actionBar.hide();
        }
    }

    public void showActionBar(){
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null && !actionBar.isShowing()){
            actionBar.show();
        }
    }

    @Override
    public void onBackPressed() {
        if(tutorialFragment.isPlayerFullscreen())
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        else
            super.onBackPressed();
    }

}

