package com.careeranna.careeranna.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.careeranna.careeranna.JW_Player_Files.KeepScreenOnHandler;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.adapter.CommentAdapter;
import com.careeranna.careeranna.data.Comment;
import com.careeranna.careeranna.data.FreeVideos;
import com.careeranna.careeranna.data.User;
import com.careeranna.careeranna.user.SignUp;
import com.google.gson.Gson;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.events.FullscreenEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class VideoWithComment extends AppCompatActivity implements VideoPlayerEvents.OnFullscreenListener, CommentAdapter.OnItemClickListener {

    RecyclerView recyclerView;

    FreeVideos freeVideos;

    private JWPlayerView playerView;

    CommentAdapter commentAdapter;

    ArrayList<Comment> comments;

    EditText comment_tv;

    String id;

    User user;

    RelativeLayout relativeLayout;

    String mUsername, profile_pic_url, mEmail;

    Button addComment, cancel;

    CircleImageView image;

    AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_with_comment);

        comments = new ArrayList<>();

        relativeLayout = findViewById(R.id.rel);

        comment_tv = findViewById(R.id.addComment);

        addComment = findViewById(R.id.reply);

        Paper.init(this);


        image = findViewById(R.id.image);

        user = null;

        String cache = Paper.book().read("user");
        if(cache != null && !cache.isEmpty()) {
            user =  new Gson().fromJson(cache, User.class);

            profile_pic_url = user.getUser_photo().replace("\\", "");
            mUsername = user.getUser_username();
            mEmail = user.getUser_email();
        } else {
            image.setVisibility(View.INVISIBLE);
            relativeLayout.setVisibility(View.VISIBLE);
        }

        comment_tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(user == null) {
                    alertDialogForComment();
                } else if(!s.toString().isEmpty()) {
                    cancel.setVisibility(View.VISIBLE);
                    addComment.setVisibility(View.VISIBLE);
                } else {
                    cancel.setVisibility(View.INVISIBLE);
                    addComment.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

            }
        });

        Glide.with(this).load(profile_pic_url).into(image);

        cancel = findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment_tv.setText("");
            }
        });

        playerView = findViewById(R.id.videoView);
        new KeepScreenOnHandler(playerView, this.getWindow());

        freeVideos = (FreeVideos) getIntent().getSerializableExtra("videos");

        recyclerView = findViewById(R.id.comments);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        commentAdapter = new CommentAdapter(comments, this, user);
        commentAdapter.setOnItemClicklistener(this);
        recyclerView.setAdapter(commentAdapter);

        String url = freeVideos.getVideo_url();

        getSupportActionBar().setTitle(freeVideos.getTitle());

        playVideo(url);

        id = freeVideos.getId();

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

                commentAdapter = new CommentAdapter(comments, VideoWithComment.this, user);
                commentAdapter.setOnItemClicklistener(VideoWithComment.this);
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

                    StringRequest stringRequest = new StringRequest(Request.Method.POST,
                            "https://careeranna.com/api/addComment.php", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("url_response", "Register Response: " + response);
                            Toast.makeText(VideoWithComment.this, response.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(VideoWithComment.this, "Some Error Occured", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            // Posting params to login url
                            Map<String, String> params = new HashMap<>();
                            params.put("email", user.getUser_email());
                            params.put("name", user.getUser_username());
                            params.put("body", comment_tv.getText().toString());
                            params.put("parent_id", "0");
                            params.put("ne_id", id);
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(VideoWithComment.this);
                    requestQueue.add(stringRequest);

                    Comment comment = new Comment();
                    comment.setComment_body(comment_tv.getText().toString());
                    comment.setName(user.getUser_username());
                    commentAdapter.addComment(comment);

                }
            }
        });

    }

    private void alertDialogForComment() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Sign In");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setCancelable(false);

        builder.setMessage("Please Sign in to comment")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alert.dismiss();
                    }
                })
                .setPositiveButton("Sign In", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(
                                new Intent(
                                      VideoWithComment.this, SignUp.class
                                        )
                        );
                    }
                });

        alert = builder.create();
        alert.show();
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

    @Override
    public void onItemClick1(final int position, final String comment) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://careeranna.com/api/addComment.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("url_response", "Register Response: " + response);
                Toast.makeText(VideoWithComment.this, response.toString(), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(VideoWithComment.this, "Some Error Occured", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<>();
                params.put("email", user.getUser_email());
                params.put("name", user.getUser_username());
                params.put("body", comment);
                params.put("parent_id", comments.get(position).getId());
                params.put("ne_id", id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(VideoWithComment.this);
        requestQueue.add(stringRequest);

    }
}
