package com.careeranna.careeranna.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.adapter.NotificationAdapter;
import com.careeranna.careeranna.data.Article;
import com.careeranna.careeranna.data.Constants;
import com.careeranna.careeranna.data.FreeVideos;
import com.careeranna.careeranna.data.Notification;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.paperdb.Paper;

public class NotificationActivity extends AppCompatActivity implements NotificationAdapter.OnNotifClickListener{

    public static final String TAG = "NotificationActivity";

    RecyclerView recyclerView;
    NotificationAdapter adapter;
    ArrayList<Notification> notificationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Paper.init(this);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle("My Notifications");
        }

        recyclerView = findViewById(R.id.rv_notification);
        notificationList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new NotificationAdapter(this);
        recyclerView.setAdapter(adapter);

        notificationList = getNotificationList();

        adapter.updateData(notificationList);
        adapter.setOnNotifClickListener(this);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int position) {
        /*
        TODO: Open the correct page according to the type and id given in the notification.
         */
        Notification currentNotif = notificationList.get(position);

        Log.d("noti_type", currentNotif.getType());

        switch (currentNotif.getType()){
            case Constants.NOTIF_TYPE_ARTICLE:
                openArticle(currentNotif.getId());
                break;

            case Constants.NOTIF_TYPE_COURSE:

                break;


            default: break;
        }
    }



    private void openArticle(String id) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(true);
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://www.careeranna.com/api/fetchParticularVideo.php?id="+id;


        Log.d("noti_url", url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        progressDialog.dismiss();
                        try {
                            JSONObject articleJSON = new JSONObject(response);
                            Article article = new Article(
                                 articleJSON.getString("ID"),
                                 articleJSON.getString("post_title"),
                                 "https://www.careeranna.com/articles/wp-content/uploads/" + articleJSON.getString("meta_value").replace("\\", ""),
                                 articleJSON.getString("display_name"),
                                 "",
                                 "",
                                 articleJSON.getString("post_date")
                            );

                            //Open this particular article
                            Intent intent = new Intent(NotificationActivity.this, ParticularArticleActivity.class);
                            intent.putExtra("article", article);
                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Log.d(TAG, "onErrorResponse: ");
                        Toast.makeText(NotificationActivity.this, getResources().getText(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(stringRequest);
    }

    public void populateDummyDate (){
        for(int i=0; i<5; i++){
            notificationList.add(new Notification(
                    "Notification title "+i,
                    "Notification description "+i,
                    "https://www.careeranna.com/uploads/thumbnail_images/CAT01.jpg",
                    "",
                    ""

            ));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }}

    private ArrayList<Notification> getNotificationList(){
        return Paper.book().read(Notification.NOTIF_LIST, new ArrayList<Notification>());
    }


}
