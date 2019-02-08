package com.careeranna.careeranna.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.careeranna.careeranna.JW_Player_Files.KeepScreenOnHandler;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.adapter.CommentAdapter;
import com.careeranna.careeranna.adapter.PlaylistAdapter;
import com.careeranna.careeranna.data.Comment;
import com.careeranna.careeranna.data.FreeVideos;
import com.careeranna.careeranna.data.PlayListItem;
import com.careeranna.careeranna.data.User;
import com.careeranna.careeranna.user.SignUp;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.events.FullscreenEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.paytm.pgsdk.easypay.manager.PaytmAssist.getContext;

public class VideoWithComment extends AppCompatActivity implements VideoPlayerEvents.OnFullscreenListener, CommentAdapter.OnItemClickListener, PlaylistAdapter.OnItemClickListener {

    public static final String TAG = "VideoWithComment";
    RecyclerView recyclerView;

    SearchView searchView;

    FreeVideos freeVideos;

    RelativeLayout relativeLayout1, create_relative, add_relative;

    Snackbar snackbar;
    PlaylistAdapter playlistAdapter;

    ArrayList<String> playlist;

    private JWPlayerView playerView;

    CommentAdapter commentAdapter;

    Button addToPlayList, createPlayList;

    private boolean is_liked, is_dislike;

    ArrayList<Comment> comments;

    EditText comment_tv, play_list_item;

    String id = "";

    ProgressBar progressBar;

    TextView views, likes, unlikes;

    Button up, thumbdown;

    boolean liked_once, dislike_once;

    User user;

    RelativeLayout relativeLayout;

    String mUsername = "", profile_pic_url = "", mEmail = "";

    Button addComment, cancel;

    RecyclerView playlist_rv;

    CircleImageView image;

    RequestQueue requestQueue1;

    boolean isUpdating = false;

    ArrayList<PlayListItem> playlistItems, tempPlaylists;

    AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_with_comment);

        Paper.init(this);

        addToPlayList = findViewById(R.id.add_to_playlist);
        relativeLayout1 = findViewById(R.id.layout_comment);
        relativeLayout = findViewById(R.id.rel);
        comment_tv = findViewById(R.id.addComment);
        addComment = findViewById(R.id.reply);
        image = findViewById(R.id.image);
        cancel = findViewById(R.id.cancel);
        views = findViewById(R.id.views);
        up = findViewById(R.id.up);
        thumbdown = findViewById(R.id.thumbdown);
        unlikes = findViewById(R.id.dislikes);

        requestQueue1 = Volley.newRequestQueue(this);

        comments = new ArrayList<>();

        user = null;

        thumbdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isUpdating) {
                    if (!is_dislike) {
                        if (liked_once) {
                            changeupdatelikes("dislikeandupdatelike");
                            Drawable img = getResources().getDrawable(R.drawable.ic_thumbs_up_hand_symbol);
                            img.setBounds(0, 0, 30, 30);
                            up.setCompoundDrawables(img, null, null, null);
                            int likes = Integer.valueOf(up.getText().toString()) - 1;
                            up.setText(likes + "");
                            up.setTextColor(getResources().getColor(R.color.dark_gray));
                            is_liked = false;
                            liked_once = false;
                        } else {
                            changeupdatelikes("dislike");
                        }
                        Drawable img = getResources().getDrawable(R.drawable.ic_thumbs_down_silhouette_red);
                        img.setBounds(0, 0, 30, 30);
                        thumbdown.setCompoundDrawables(img, null, null, null);
                        int dislikes = Integer.valueOf(unlikes.getText().toString()) + 1;
                        unlikes.setText(dislikes + "");
                        unlikes.setTextColor(getResources().getColor(R.color.red));
                        is_dislike = true;
                        dislike_once = true;
                    } else {
                        changeupdatelikes("notdislike");
                        Drawable img = getResources().getDrawable(R.drawable.ic_thumbs_down_silhouette);
                        img.setBounds(0, 0, 30, 30);
                        thumbdown.setCompoundDrawables(img, null, null, null);
                        int dislikes = Integer.valueOf(unlikes.getText().toString()) - 1;
                        unlikes.setText(dislikes + "");
                        unlikes.setTextColor(getResources().getColor(R.color.dark_gray));
                        is_dislike = false;
                        dislike_once = false;
                    }
                }
            }
        });


        addToPlayList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(VideoWithComment.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.add_to_playlist_dialog);

                play_list_item = dialog.findViewById(R.id.playlist_item_name_et);

                Button add_playlist = dialog.findViewById(R.id.add_btn);

                searchView = dialog.findViewById(R.id.search);

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        searchView.clearFocus();
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        playlistItems.clear();
                        for(PlayListItem playListItem: tempPlaylists) {
                            if(playListItem.getName().toLowerCase().contains(newText.toLowerCase())) {
                                playlistItems.add(playListItem);
                            }
                        }
                        playlistAdapter.notifyDataSetChanged();
                        return true;
                    }
                });

                progressBar = dialog.findViewById(R.id.progress);

                playlistItems = new ArrayList<>();

                add_playlist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {

                        String url = "https://www.careeranna.com/api/addVideoIdToPlaylist.php";
                        RequestQueue requestQueue = Volley.newRequestQueue(VideoWithComment.this);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                                url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if (response.equals("Added To Playlist")) {
                                            play_list_item.setText("");
                                        } else if(response.equals("Created Playlist")){
                                            PlayListItem playListItem = new PlayListItem(play_list_item.getText().toString(), "0");
                                            tempPlaylists.add(playListItem);
                                            playlistAdapter.addPlayListItem(playListItem);
                                            play_list_item.setText("");
                                            Toast.makeText(VideoWithComment.this, response, Toast.LENGTH_SHORT).show();
                                        }
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                                        add_relative.setVisibility(View.INVISIBLE);
                                        create_relative.setVisibility(View.VISIBLE);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        add_relative.setVisibility(View.INVISIBLE);
                                        create_relative.setVisibility(View.VISIBLE);
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                                        Log.d("err", error.getMessage());
                                        play_list_item.setText("");
                                        Toast.makeText(VideoWithComment.this, getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                                    }
                                }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Log.d("user_id", user.getUser_id());
                                // Posting params to login url
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("user_id", user.getUser_id());
                                params.put("playlist_name", play_list_item.getText().toString());
                                params.put("video_id","0");
                                return params;
                            }

                        };
                        if(play_list_item.getText().toString().isEmpty()) {
                            Toast.makeText(VideoWithComment.this, "Please Enter PlayList Name", Toast.LENGTH_SHORT).show();
                            return;
                        } else if(playlist.contains(play_list_item.getText().toString())) {
                            play_list_item.setText("");
                            Toast.makeText(VideoWithComment.this, "PlayList Already Exists", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            requestQueue.add(stringRequest);
                        }
                    }
                });

                create_relative = dialog.findViewById(R.id.create_layout);
                add_relative = dialog.findViewById(R.id.add_layout);
                createPlayList = dialog.findViewById(R.id.create_btn);

                createPlayList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        add_relative.setVisibility(View.VISIBLE);
                        create_relative.setVisibility(View.INVISIBLE);
                    }
                });

                playlist = new ArrayList<>();
                fetchPlayList();

                playlist_rv = dialog.findViewById(R.id.playlist_rv);

                Button cancel = dialog.findViewById(R.id.cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isUpdating) {
                    if (!is_liked) {
                        if (dislike_once) {
                            changeupdatelikes("likedandchangedislike");
                            Drawable img = getResources().getDrawable(R.drawable.ic_thumbs_down_silhouette);
                            img.setBounds(0, 0, 30, 30);
                            thumbdown.setCompoundDrawables(img, null, null, null);
                            int dislikes = Integer.valueOf(unlikes.getText().toString()) - 1;
                            unlikes.setText(dislikes + "");
                            unlikes.setTextColor(getResources().getColor(R.color.dark_gray));
                            is_dislike = false;
                            dislike_once = false;
                        } else {
                            changeupdatelikes("liked");
                        }
                        Drawable img = getResources().getDrawable(R.drawable.ic_thumbs_up_hand_symbol_blue);
                        img.setBounds(0, 0, 30, 30);
                        up.setCompoundDrawables(img, null, null, null);
                        int likes = Integer.valueOf(up.getText().toString()) + 1;
                        up.setText(likes + "");
                        up.setTextColor(getResources().getColor(R.color.sign_in_button_color));
                        is_liked = true;
                        liked_once = true;
                    } else {
                        changeupdatelikes("notliked");
                        Drawable img = getResources().getDrawable(R.drawable.ic_thumbs_up_hand_symbol);
                        img.setBounds(0, 0, 30, 30);
                        up.setCompoundDrawables(img, null, null, null);
                        int likes = Integer.valueOf(up.getText().toString()) - 1;
                        up.setText(likes + "");
                        up.setTextColor(getResources().getColor(R.color.dark_gray));
                        is_liked = false;
                        liked_once = false;
                    }
                }
            }

        });

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

                if(getWindow() != null ) {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                }
            }
        });

        if(profile_pic_url.length() > 0) {
            Glide.with(this).load(profile_pic_url).into(image);
        }

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

        if(freeVideos != null) {

            String url = freeVideos.getVideo_url();
            if(getSupportActionBar() != null) {
                getSupportActionBar().setTitle(freeVideos.getTitle());
            }
            playVideo(url);

            id = freeVideos.getId();
            views.setText(freeVideos.getTotal_view() + " Views");
            up.setText(freeVideos.getLikes());
            unlikes.setText(freeVideos.getDislikes());
        }


        RequestQueue requestQueue = Volley.newRequestQueue(this);

        comments = new ArrayList<>();

        String url1 = "https://careeranna.com/api/comments.php?id="+id;
        Log.d("comment_url", url1);
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
                                        jsonObject.getString("videoId"),
                                        jsonObject.getString("comment_created")));
                            } else {
                                for (int j=0;j<comments.size();j++) {
                                    if(comments.get(j).getId().equals(jsonObject.getString("parent_id"))) {
                                        comments.get(j).getComments().add(new Comment(jsonObject.getString("comment_id"),
                                                jsonObject.getString("comment_name"),
                                                jsonObject.getString("comment_email"),
                                                jsonObject.getString("comment_body"),
                                                jsonObject.getString("parent_id"),
                                                jsonObject.getString("videoId"),
                                                jsonObject.getString("comment_created")));
                                        break;
                                    }
                                }
                            }
                         }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(VideoWithComment.this, "JSON Error ", Toast.LENGTH_SHORT).show();
                    }
                }

                commentAdapter = new CommentAdapter(comments, VideoWithComment.this, user);
                commentAdapter.setOnItemClicklistener(VideoWithComment.this);
                recyclerView.setAdapter(commentAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(VideoWithComment.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
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
                            comment_tv.getText().clear();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(VideoWithComment.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
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

    private void changeupdatelikes(String type) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                "https://www.careeranna.com/api/updatelikes.php?id=" + freeVideos.getId() + "&type=" + type,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("liked_response", response);
                        snackbar.dismiss();
                        if(response.equals("updated")) {
                            isUpdating = false;
                            snackbar = Snackbar.make(relativeLayout1, "Your Response is aved!", Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        } else {
                            isUpdating = false;
                            snackbar = Snackbar.make(relativeLayout1, "Something Went Wrong Your Response is not saved!", Snackbar.LENGTH_SHORT);      }
                        snackbar.show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        snackbar.dismiss();
                        isUpdating = false;
                        snackbar = Snackbar.make(relativeLayout1, "Something Went Wrong!", Snackbar.LENGTH_SHORT);
                        snackbar.show();

                    }
                });
        if(!isUpdating) {
            snackbar = Snackbar.make(relativeLayout1, "Please Wait Saving Response", Snackbar.LENGTH_SHORT);
            snackbar.show();
            requestQueue1.add(stringRequest);
        }
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
        playerView.play();
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
        Log.d(TAG, "onConfigurationChanged: ");
        boolean isLandscape = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE;

        Log.d(TAG, "orientation "+isLandscape);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            if(isLandscape){
                Log.d(TAG, "landscape 27+");
                playerView.setFullscreen(true, true);
            } else {
                Log.d(TAG, "potrait 27+");
                playerView.setFullscreen(false, true);
            }
        } else {
            Log.d(TAG, "27- "+isLandscape);
            playerView.setFullscreen(isLandscape, true);
        }

        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onFullscreen(FullscreenEvent fullscreenEvent) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            Log.d(TAG, "27+ onFullscreen: Went fullscreen "+fullscreenEvent.getFullscreen());
        } else {
            Log.d(TAG, "27- onFullscreen: Went fullscreen "+fullscreenEvent.getFullscreen());
        }

        if(fullscreenEvent.getFullscreen()){
            //If fullscreen, remove the action bar
            Log.d(TAG, "onFullscreen: trying to go fullscreen");

        } else {
            //If not fullscreen, show the action bar
            Log.d(TAG, "onFullscreen: not in fullscreen");
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
                Toast.makeText(VideoWithComment.this, "Comment Created Successfully!", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(VideoWithComment.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
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

    public void fetchPlayList() {

        progressBar.setVisibility(View.VISIBLE);

        tempPlaylists = new ArrayList<>();
        String url = "https://www.careeranna.com/api/getMyPlaylist.php?user_id=";
        if(user != null) {
            url = url + user.getUser_id();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(VideoWithComment.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response_from_playlist", response);
                        if(!response.equals("No Playlist Created")) {
                            try {
                                JSONArray playlistArray = new JSONArray(response);
                                JSONObject playlistItem;
                                for(int i=0;i<playlistArray.length();i++) {
                                    playlistItem = playlistArray.getJSONObject(i);
                                    playlistItems.add(new PlayListItem(playlistItem.getString("playlist_name"),
                                            playlistItem.getString("video_ids")));
                                    tempPlaylists.add(new PlayListItem(playlistItem.getString("playlist_name"),
                                            playlistItem.getString("video_ids")));

                                }
                                playlist_rv.setLayoutManager(new LinearLayoutManager(VideoWithComment.this));

                                playlistAdapter = new PlaylistAdapter(playlistItems, VideoWithComment.this, freeVideos.getId());
                                playlist_rv.setAdapter(playlistAdapter);
                                playlistAdapter.setOnItemClicklistener(VideoWithComment.this);
                                progressBar.setVisibility(View.GONE);
                            } catch (JSONException e) {
                                progressBar.setVisibility(View.GONE);
                                e.printStackTrace();
                                Toast.makeText(VideoWithComment.this, getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(VideoWithComment.this, getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(stringRequest);
    }

    @Override
    public void onItemClick1(final int position) {

        String url = "https://www.careeranna.com/api/addVideoIdToPlaylist.php";
        RequestQueue requestQueue = Volley.newRequestQueue(VideoWithComment.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("Added To Playlist")) {
                            Toast.makeText(VideoWithComment.this, response, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(VideoWithComment.this, response, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("err", error.getMessage());
                        Toast.makeText(VideoWithComment.this, getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Log.d("user_id", user.getUser_id());
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user.getUser_id());
                params.put("playlist_name", playlistItems.get(position).getName());
                params.put("video_id",freeVideos.getId());
                return params;
            }

        };
        requestQueue.add(stringRequest);

    }
}
