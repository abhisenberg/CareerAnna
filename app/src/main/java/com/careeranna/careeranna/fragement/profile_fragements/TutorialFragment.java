package com.careeranna.careeranna.fragement.profile_fragements;


import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.careeranna.careeranna.JW_Player_Files.KeepScreenOnHandler;
import com.careeranna.careeranna.ParticularCourse;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.adapter.ExpandableList_Adapter;
import com.careeranna.careeranna.data.Topic;
import com.careeranna.careeranna.data.Unit;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.events.FullscreenEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;


public class TutorialFragment extends Fragment implements ExpandableList_Adapter.OnItemClickListener
        , VideoPlayerEvents.OnFullscreenListener{

    //    VideoView videoView;
    private JWPlayerView playerView;

    String videoUrl;

    ExpandableListView listView;
    ExpandableList_Adapter listAdapter;
    ArrayList<Unit> mUnits;

    ProgressDialog progressDialog;

    public TutorialFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tutorial, container, false);

        playerView = view.findViewById(R.id.videoView);
        new KeepScreenOnHandler(playerView, getActivity().getWindow());
        /*
        Playing a sample video in the starting, this statement should be removed before
        finalizing the app.
        TODO: Play from the last moment where the user left.
         */

        String videoPath = "android.resource://com.careeranna.careeranna/"+R.raw.video;
        Uri uri = Uri.parse(videoPath);
        playVideo(uri.toString());

        listView = view.findViewById(R.id.expandableunit);
        return view;
    }

    public void addCourseUnits(ArrayList<String> course) {

        Drawable check = getApplicationContext().getResources().getDrawable(R.drawable.ic_check_circle_black_24dp);
        Drawable unCheck = getApplicationContext().getResources().getDrawable(R.drawable.ic_check_circle_black1_24dp);

        mUnits = new ArrayList<>();

        for (String unitsname : course) {
            char c = unitsname.charAt(0);
            if(c != '$') {
                Unit unit = new Unit(unitsname, check);
                mUnits.add(unit);
            } else {
                String array[] = unitsname.split(",url");
                if(array.length == 1) {
                    mUnits.get(mUnits.size()-1).topics.add(new Topic(array[0].substring(1), ""));
                } else {
                    mUnits.get(mUnits.size()-1).topics.add(new Topic(array[0].substring(1), array[1]));
                }
            }

            listAdapter = new ExpandableList_Adapter(getApplicationContext(), mUnits, listView);
            listView.setAdapter(listAdapter);
            listAdapter.setOnItemClicklistener(this);
        }
    }


    @Override
    public void onItemClick1(int position, int position2) {

        playVideo(mUnits.get(position).topics.get(position2).getVideos());

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
            ((ParticularCourse)getActivity()).removeActionBar();
        } else {
            //If not fullscreen, show the action bar
            ((ParticularCourse)getActivity()).showActionBar();
        }
    }

    public boolean isPlayerFullscreen(){
        return playerView.getFullscreen();
    }

}
