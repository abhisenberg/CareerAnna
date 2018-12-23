package com.careeranna.careeranna;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.careeranna.careeranna.JW_Player_Files.KeepScreenOnHandler;
import com.careeranna.careeranna.adapter.CommentAdapter;
import com.careeranna.careeranna.data.Comment;
import com.careeranna.careeranna.data.FreeVideos;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.events.FullscreenEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VideoWithComment extends AppCompatActivity implements VideoPlayerEvents.OnFullscreenListener {

    RecyclerView recyclerView;

    FreeVideos freeVideos;

    private JWPlayerView playerView;

    CommentAdapter commentAdapter;

    ArrayList<Comment> comments;

    EditText comment_tv;

    Button addComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_with_comment);

        comments = new ArrayList<>();
        comments.add(new Comment());

        comment_tv = findViewById(R.id.addComment);

        addComment = findViewById(R.id.reply);

        final ArrayList<Comment> comments1 = new ArrayList<>();
        comments1.add(new Comment());
        comments1.add(new Comment());
        comments1.add(new Comment());

        comments.get(0).setComments(comments1);

        comments.add(new Comment());


        playerView = findViewById(R.id.videoView);
        new KeepScreenOnHandler(playerView, this.getWindow());

        freeVideos = (FreeVideos) getIntent().getSerializableExtra("course");

        recyclerView = findViewById(R.id.comments);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        commentAdapter = new CommentAdapter(comments, this);
        recyclerView.setAdapter(commentAdapter);

        String url = freeVideos.getVideo_url();

        getSupportActionBar().setTitle(freeVideos.getTitle());

        playVideo(url);

        String id = "38";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        comments = new ArrayList<>();

        String url1 = "https://careeranna.com/api/comments.php?id="+id;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("url_comment", response);
                if (!response.equals("0 results")) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.toString());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            if(jsonObject.getString("parent_id").equals("0")) {
                                comments.add(new Comment(jsonObject.getString("comment_id"),
                                        jsonObject.getString("comment_name"),
                                        jsonObject.getString("comment_email"),
                                        jsonObject.getString("comment_body"),
                                        jsonObject.getString("parent_id"),
                                        jsonObject.getString("comment_id")));
                            } else {
                                for (int j=0;j<comments.size();j++) {
                                    if(comments.get(j).getId().equals(jsonObject.getString("parent_id"))) {
                                        comments.get(j).getComments().add(new Comment(jsonObject.getString("comment_id"),
                                                jsonObject.getString("comment_name"),
                                                jsonObject.getString("comment_email"),
                                                jsonObject.getString("comment_body"),
                                                jsonObject.getString("parent_id"),
                                                jsonObject.getString("comment_id")));
                                        break;
                                    }
                                }
                            }
                         }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                commentAdapter = new CommentAdapter(comments, VideoWithComment.this);
                recyclerView.setAdapter(commentAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(stringRequest);

        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!comment_tv.getText().toString().isEmpty()) {
                    commentAdapter.addComment(new Comment());
                }
            }
        });

    }

    private void playVideo(String videoUrl){
        PlaylistItem playlistItem = new PlaylistItem.Builder()
                .file(videoUrl)
                .build();
        playerView.load(playlistItem);
    }


    @Override
    public void onDestroy() {
        playerView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        playerView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        playerView.onResume();
        super.onResume();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        playerView.setFullscreen(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE, true);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onFullscreen(FullscreenEvent fullscreenEvent) {
        if(fullscreenEvent.getFullscreen()){
            //If fullscreen, remove the action bar
        } else {
            //If not fullscreen, show the action bar
        }
    }

    public boolean isPlayerFullscreen(){
        return playerView.getFullscreen();
    }
}
