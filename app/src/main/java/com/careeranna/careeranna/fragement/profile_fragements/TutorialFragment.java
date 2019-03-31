package com.careeranna.careeranna.fragement.profile_fragements;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
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
import com.careeranna.careeranna.adapter.CourseVideoAdapter;
import com.careeranna.careeranna.adapter.ExpandableList_Adapter;
import com.careeranna.careeranna.data.Topic;
import com.careeranna.careeranna.data.Unit;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.events.BeforePlayEvent;
import com.longtailvideo.jwplayer.events.BufferChangeEvent;
import com.longtailvideo.jwplayer.events.BufferEvent;
import com.longtailvideo.jwplayer.events.FullscreenEvent;
import com.longtailvideo.jwplayer.events.LevelsEvent;
import com.longtailvideo.jwplayer.events.PlayEvent;
import com.longtailvideo.jwplayer.events.listeners.AdvertisingEvents;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static com.facebook.FacebookSdk.getApplicationContext;


public class TutorialFragment extends Fragment implements ExpandableList_Adapter.OnItemClickListener
        , VideoPlayerEvents.OnFullscreenListener {

    public static final String TAG = "TutorialFragment";

    //    VideoView videoView;
    private JWPlayerView playerView;

    CardView cardView;

    Context context;

    RecyclerView video_heading_rv;

    ExpandableListView listView;
    ExpandableList_Adapter listAdapter;
    ArrayList<Unit> mUnits;

    ImageView imageView;


    public TutorialFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tutorial, container, false);

        context = inflater.getContext();

        playerView = view.findViewById(R.id.videoView);
        video_heading_rv = view.findViewById(R.id.videos_rv);
        imageView = view.findViewById(R.id.image_view);
        if (getActivity() != null) {
            if (getActivity().getWindow() != null) {
                new KeepScreenOnHandler(playerView, getActivity().getWindow());
            }
        }
        /*
        Playing a sample video in the starting, this statement should be removed before
        finalizing the app.
        TODO: Play from the last moment where the user left.
         */


        cardView = view.findViewById(R.id.card);

        listView = view.findViewById(R.id.expandableunit);
        return view;
    }

    public void addCourseUnits(ArrayList<String> course, ArrayList<String> watched) {

        Drawable check = getApplicationContext().getResources().getDrawable(R.drawable.ic_check_circle_black_24dp);

        mUnits = new ArrayList<>();

        if (course.size() == 0 || course.get(0).equals("No results")) {
            playerView.setVisibility(GONE);
            cardView.setVisibility(View.VISIBLE);
        } else {


            String last_played_url = "";
            for (String unitsname : course) {
                if (unitsname.length() > 0) {
                    char c = unitsname.charAt(0);
                    if (c != '$') {
                        Unit unit = new Unit(unitsname, check);
                        mUnits.add(unit);
                    } else {
                        if(mUnits.size() == 0) {
                            Unit unit = new Unit("Introduction", check);
                            mUnits.add(unit);
                        }
                        String array[] = unitsname.split(",url");
                        if (array.length == 3) {
                            if(watched.size() > 0) {
                                if(watched.get(watched.size()-1).equals(array[2])) {
                                    last_played_url = array[1];
                                }
                            }
                            Topic topic = new Topic(array[0].substring(1), array[1]);
                            if (watched.contains(array[2])) {
                                topic.setWatched(true);
                            } else {
                                topic.setWatched(false);
                            }
                            mUnits.get(mUnits.size() - 1).topics.add(topic);
                        } else if (array.length == 2) {
                            Topic topic = new Topic(array[0].substring(1), array[1]);
                            if (watched.contains(array[2])) {
                                topic.setWatched(true);
                            } else {
                                topic.setWatched(false);
                            }
                            mUnits.get(mUnits.size() - 1).topics.add(topic);
                        } else {
                            Topic topic = new Topic(array[0].substring(1), "");
                            if (watched.contains(array[2])) {
                                topic.setWatched(true);
                            } else {
                                topic.setWatched(false);
                            }
                            mUnits.get(mUnits.size() - 1).topics.add(topic);
                        }
                    }
                }

                /*
                Play the first video of the course by default
                 */
                if(!last_played_url.equals("")) {
                    playVideo(last_played_url);
                } else {
                    if (!mUnits.isEmpty()) {
                        if (!mUnits.get(0).topics.isEmpty()) {
                            if (!mUnits.get(0).topics.get(0).getVideos().equals("")) {
                                playVideo(mUnits.get(0).topics.get(0).getVideos());
                            }
                        }
                    }

                }
                video_heading_rv.setLayoutManager(new LinearLayoutManager(context));
                video_heading_rv.setHasFixedSize(true);

                CourseVideoAdapter courseVideoAdapter = new CourseVideoAdapter(mUnits, context, this);
                video_heading_rv.setAdapter(courseVideoAdapter);

                listAdapter = new ExpandableList_Adapter(getApplicationContext(), mUnits, listView);
                listView.setAdapter(listAdapter);
                listAdapter.setOnItemClicklistener(this);
            }
        }
    }


    @Override
    public void onItemClick1(int parent, int child) {
        playVideo(mUnits.get(parent).topics.get(child).getVideos());
    }

    public void playVideo(String videoUrl) {
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
        playerView.pause();
        super.onPause();
    }

    @Override
    public void onResume() {
        playerView.play();
        super.onResume();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        playerView.setFullscreen(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE, true);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onFullscreen(FullscreenEvent fullscreenEvent) {
        if (fullscreenEvent.getFullscreen()) {
            //If fullscreen, remove the action bar
            if (getActivity() != null) {
                ((ParticularCourse) getActivity()).removeActionBar();
            }
        } else {
            //If not fullscreen, show the action bar
            if (getActivity() != null) {
                ((ParticularCourse) getActivity()).showActionBar();
            }
        }
    }

    public boolean isPlayerFullscreen() {
        return playerView.getFullscreen();
    }

}
