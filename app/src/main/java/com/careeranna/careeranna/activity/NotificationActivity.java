package com.careeranna.careeranna.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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

        Notification currentNotif = notificationList.get(position);

        Intent intent =  new Intent(
                Intent.ACTION_VIEW,
                Uri.parse(currentNotif.getRedirectURl())
        );
        startActivity(intent);
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
