package com.careeranna.careeranna.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.careeranna.careeranna.R;
import com.longtailvideo.jwplayer.events.FullscreenEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;

public class PurchaseCourseDetail2 extends AppCompatActivity  implements VideoPlayerEvents.OnFullscreenListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_course_detail_2);
    }

    @Override
    public void onFullscreen(FullscreenEvent fullscreenEvent) {

    }
}
