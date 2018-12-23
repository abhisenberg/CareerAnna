package com.careeranna.careeranna.data;

import android.graphics.drawable.Drawable;

import com.careeranna.careeranna.R;

public class Topic {

    private String Name;
    private Drawable icon;
    private String videos;

    public Topic(String name, String videos) {
        Name = name;
        this.icon = icon;
        this.videos = videos;
    }

    public String getVideos() {
        return videos;
    }

    public void setVideos(String videos) {
        this.videos = videos;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
}

