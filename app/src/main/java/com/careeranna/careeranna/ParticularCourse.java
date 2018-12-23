package com.careeranna.careeranna;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.careeranna.careeranna.data.User;
import com.careeranna.careeranna.fragement.profile_fragements.CertificateFragment;
import com.careeranna.careeranna.fragement.profile_fragements.NotesFragment;
import com.careeranna.careeranna.fragement.profile_fragements.TestFragment;
import com.careeranna.careeranna.fragement.profile_fragements.TutorialFragment;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
            getSupportActionBar().setTitle("Tutorial");

        } else if(id == R.id.notes) {

            fetchPdf();

            navigationView.setCheckedItem(R.id.notes);
            fragmentManager.beginTransaction().replace(R.id.main_content,notesFragment).commit();
            getSupportActionBar().setTitle("Notes");

        } else if(id == R.id.test) {

            navigationView.setCheckedItem(R.id.test);
            fragmentManager.beginTransaction().replace(R.id.main_content,testFragment).commit();
            getSupportActionBar().setTitle("Test");

        } else if(id == R.id.certificate) {

            navigationView.setCheckedItem(R.id.certificate);
            fragmentManager.beginTransaction().replace(R.id.main_content,certificateFragment).commit();
            getSupportActionBar().setTitle("Certificate");

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
        String url = "https://careeranna.com/api/getVideosWithNames.php?product_id="+id;
        Log.i("url", url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<String> course = new ArrayList<>();
                        try {
                            Log.i("pdf", response);
                            if(!response.equals("No results")) {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("content");
                                for(int i=0;i<jsonArray.length();i++) {
                                    course.add((String)jsonArray.get(i));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        tutorialFragment.addCourseUnits(course);
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ParticularCourse.this, "Error Occured", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
        requestQueue.add(stringRequest);
    }

    private void fetchPdf() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Materilal .. ");
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "http://careeranna.com/apicoursePdf.php?id="+id;
        Log.i("url", url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String status = "No pdf";
                        try {
                            Log.i("pdf", response);
                            JSONObject jsonObject = new JSONObject(response);
                            material = jsonObject.getString("material_file");
                            if(!material.equals("null")) {
                                status = "Select Pdf from Below !";
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            material = "No Pdf";
                            status = "No Pdf";
                        }
                        progressDialog.dismiss();
                        ArrayList<String> pdfs = new ArrayList<>();
                        String[] pdfs1 = material.split(",");
                        for(String pdf : pdfs1) {
                            pdfs.add(pdf);
                        }
                        notesFragment.addPdf(pdfs, status);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ParticularCourse.this, "Error Occured", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
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

