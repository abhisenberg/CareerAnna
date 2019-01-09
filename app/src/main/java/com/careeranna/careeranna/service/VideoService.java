package com.careeranna.careeranna.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.careeranna.careeranna.R;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

public class VideoService extends Service implements View.OnClickListener{

    private WindowManager windowManager;
    PopupWindow popupWindow;
    private View appIcon;

    private int previousX, previousY;

    JWPlayerView playerView;

    private float mStartX, mStartY;

    private WindowManager.LayoutParams mIconViewParams;

    @NonNull
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initView();
        return START_STICKY;
    }

    public void initView() {
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        createIconView();
        showIcon();
    //    playVideo("https://s3.amazonaws.com/careeranna/Daily+News+Analysis+/DNA+28+Dec_tamil.mp4");
    }

    private void showIcon() {
        windowManager.addView(appIcon, mIconViewParams);
    }

    private void removeView() {
        windowManager.removeView(appIcon);
    }

    private void createIconView() {



        mIconViewParams = new WindowManager.LayoutParams();
        mIconViewParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mIconViewParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mIconViewParams.format = PixelFormat.TRANSLUCENT;
        mIconViewParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        mIconViewParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        appIcon = LayoutInflater.from(this).inflate(R.layout.view_icon, null);

        Button close = appIcon.findViewById(R.id.close);

        playerView = (JWPlayerView) appIcon.findViewById(R.id.playerView);

        close.setOnClickListener(this);

        appIcon.findViewById(R.id.relative).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = mIconViewParams.x;
                        initialY = mIconViewParams.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;

                    case MotionEvent.ACTION_UP:
                        //when the drag is ended switching the state of the widget
                    /*    collapsedView.setVisibility(View.GONE);
                        expandedView.setVisibility(View.VISIBLE);
                    */    return true;

                    case MotionEvent.ACTION_MOVE:
                        //this code is helping the widget to move around the screen with fingers
                        mIconViewParams.x = initialX + (int) (event.getRawX() - initialTouchX);
                        mIconViewParams.y = initialY + (int) (event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(appIcon, mIconViewParams);
                        return true;
                }
                return false;
            }
        });

    }


    private void playVideo(String videoUrl) {
        PlaylistItem playlistItem = new PlaylistItem.Builder()
                .file(videoUrl)
                .build();
        playerView.load(playlistItem);
    }

    public void showPopUp() {

        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.view_icon, null);

        popupWindow = new PopupWindow(view,
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        Button close = view.findViewById(R.id.close);

        close.setOnClickListener(this);

        close.setOnClickListener(

                new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    }
        });

        popupWindow.showAsDropDown(close, 50, -30);

        view.setOnTouchListener(new View.OnTouchListener() {

            int mxX, myY;
            int offSetX, offSetY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        mxX = (int) event.getX();
                        myY = (int) event.getY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        offSetX = (int) event.getRawX() - mxX;
                        offSetY = (int) event.getRawY() - myY;

                        popupWindow.update(offSetX, offSetY, -1, -1, true);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {

        stopSelf();
        removeView();
    }
/*
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                previousX = mIconViewParams.x;
                previousY = mIconViewParams.y;
                mStartX = event.getRawX();
                mStartY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                double deltaX = event.getRawX() - mStartX;
                double deltaY = event.getRawY() - mStartY;

                mIconViewParams.x = previousX + (int) deltaX;
                mIconViewParams.y = previousY + (int) deltaY;

                windowManager.updateViewLayout(appIcon, mIconViewParams);
                break;
        }

        return true;
    }*/
}
