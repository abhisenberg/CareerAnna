package com.careeranna.careeranna.fragement.profile_fragements;


import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.careeranna.careeranna.JW_Player_Files.KeepScreenOnHandler;
import com.careeranna.careeranna.ParticularCourse;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.adapter.CourseContentAdapter;
import com.careeranna.careeranna.adapter.ExpandableList_Adapter;
import com.careeranna.careeranna.data.Unit;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.events.AdPlayEvent;
import com.longtailvideo.jwplayer.events.BeforePlayEvent;
import com.longtailvideo.jwplayer.events.ErrorEvent;
import com.longtailvideo.jwplayer.events.FullscreenEvent;
import com.longtailvideo.jwplayer.events.PlayEvent;
import com.longtailvideo.jwplayer.events.PlaylistCompleteEvent;
import com.longtailvideo.jwplayer.events.PlaylistEvent;
import com.longtailvideo.jwplayer.events.ReadyEvent;
import com.longtailvideo.jwplayer.events.SetupErrorEvent;
import com.longtailvideo.jwplayer.events.listeners.AdvertisingEvents;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.playlists.MediaSource;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.http.Url;

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

    Button share;

    String type;

    public String shareUrl;

    public TutorialFragment() {
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tutorial, container, false);

        context = inflater.getContext();

        Paper.init(context);

        playerView = view.findViewById(R.id.videoView);
        video_heading_rv = view.findViewById(R.id.videos_rv);
        imageView = view.findViewById(R.id.image_view);
        share = view.findViewById(R.id.share);
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

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shareUrl != null) {

                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "https://www.careeranna.com/english/free/videos/mba/" + shareUrl.replaceAll(" ", "-"));
                    sendIntent.setType("text/plain");
                    inflater.getContext().startActivity(sendIntent);

                }
            }
        });

        return view;
    }

    public void addCourseUnits(ArrayList<Unit> mUnits, String last_played_url, String shareUrl) {

        Drawable check = getApplicationContext().getResources().getDrawable(R.drawable.ic_check_circle_black_24dp);

        if (mUnits.size() == 0) {
            playerView.setVisibility(GONE);
            cardView.setVisibility(View.VISIBLE);
            share.setVisibility(GONE);
        } else {

            this.mUnits = new ArrayList<>();
            this.mUnits.addAll(mUnits);
            video_heading_rv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

            CourseContentAdapter courseContentAdapter = new CourseContentAdapter(mUnits, context);
            video_heading_rv.setAdapter(courseContentAdapter);
            courseContentAdapter.addFragment(this);


                /*
                Play the first video of the course by default
                 */
            this.shareUrl = shareUrl;

            if (!last_played_url.equals("")) {
                playVideo(last_played_url, shareUrl);
            } else {
                if (!mUnits.isEmpty()) {
                    if (!mUnits.get(0).topics.isEmpty()) {
                        if (!mUnits.get(0).topics.get(0).getVideos().equals("")) {
                            this.shareUrl = mUnits.get(0).topics.get(0).getName();
                            playVideo(mUnits.get(0).topics.get(0).getVideos(), mUnits.get(0).topics.get(0).getName());
                        }
                    }
                }

            }

        }
    }

    public void addType(String type) {
        this.type = type;
        if (type.equals("paid")) {
            share.setVisibility(GONE);
        }
    }

    @Override
    public void onItemClick1(int parent, int child) {
        playVideo(mUnits.get(parent).topics.get(child).getVideos(), mUnits.get(parent).topics.get(child).getName());
    }

    public void playVideo(String videoUrl, String heading) {
        Log.d("url_from_video", videoUrl);
//
//        try {
//            videoUrl = URLEncoder.encode(videoUrl, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

        PlaylistItem playlistItem1 = new PlaylistItem.Builder()
                        .file(videoUrl)
                        .build();
                playerView.load(playlistItem1);
                playerView.play();

        playerView.addOnErrorListener(new VideoPlayerEvents.OnErrorListener() {
            @Override
            public void onError(ErrorEvent errorEvent) {
            }
        });

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
