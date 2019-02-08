package com.careeranna.careeranna.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.adapter.PlaylistAdapter;
import com.careeranna.careeranna.adapter.PlaylistWithCounterAdapter;
import com.careeranna.careeranna.data.FreeVideos;
import com.careeranna.careeranna.data.PlayListItem;
import com.careeranna.careeranna.data.PlayListItemWithVideo;
import com.careeranna.careeranna.data.User;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.paperdb.Paper;

public class MyPlayList extends AppCompatActivity implements PlaylistWithCounterAdapter.OnItemClickListener{

    RecyclerView playLitsts;
    ArrayList<PlayListItemWithVideo> playListItems;

    ProgressDialog progressDialog;

    PlaylistWithCounterAdapter playlistWithCounterAdapter;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_play_list);

        Paper.init(this);

        playListItems = new ArrayList<>();

        playLitsts = findViewById(R.id.playlist_rv);

        String cache = Paper.book().read("user");
        if(cache != null && !cache.isEmpty()) {
            user =  new Gson().fromJson(cache, User.class);
        }

        fetchPlayList();
    }


    public void fetchPlayList() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        String url = "https://www.careeranna.com/api/fetchPlayListVideo.php?user_id=";
        if(user != null) {
            url = url + user.getUser_id();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(MyPlayList.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response_from_playlist", response);
                            try {
                                JSONArray playlistArray = new JSONArray(response);
                                JSONObject playlistItem;
                                for(int i=0;i<playlistArray.length();i++) {
                                    playlistItem = playlistArray.getJSONObject(i);
                                    PlayListItemWithVideo playListItemWithVideo = new PlayListItemWithVideo(playlistItem.getString("playlist_name"),
                                            playlistItem.getString("video_ids"));
                                    JSONArray videoArray = playlistItem.getJSONArray("playlist");
                                    ArrayList<FreeVideos> freeVideosArray = new ArrayList<>();
                                    for(int j=0;j<videoArray.length();j++) {
                                        JSONObject videos = videoArray.getJSONObject(j);
                                        freeVideosArray.add(new FreeVideos(
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
                                        freeVideosArray.get(j).setType("Trending");
                                    }
                                    playListItemWithVideo.setVideos(freeVideosArray);
                                    playListItems.add(playListItemWithVideo);
                                    }
                                playLitsts.setLayoutManager(new LinearLayoutManager(MyPlayList.this));

                                playlistWithCounterAdapter = new PlaylistWithCounterAdapter(playListItems, MyPlayList.this);
                                playlistWithCounterAdapter.setOnItemClicklistener(MyPlayList.this);
                                playLitsts.setAdapter(playlistWithCounterAdapter);
                                progressDialog.dismiss();
                            } catch (JSONException e) {
                                progressDialog.dismiss();
                                e.printStackTrace();
                                Toast.makeText(MyPlayList.this, "JSON Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(MyPlayList.this, getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(stringRequest);
    }

    @Override
    public void onItemClick1(int position) {
    }
}
