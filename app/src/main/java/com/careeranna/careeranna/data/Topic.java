package com.careeranna.careeranna.data;

import android.graphics.drawable.Drawable;

public class Topic {

    private String Name;
    private String id;
    private Drawable icon;
    private String videos;
    private Boolean isWatched;
    private String duration;

    public Topic(String id, String name, String videos, String duration) {
        Name = name;
        this.id = id;
        this.videos = videos;
        this.duration = duration;
    }

    public Topic(String id, String name, String videos, Boolean isWatched, String duration) {
        Name = name;
        this.id = id;
        this.videos = videos;
        this.isWatched = isWatched;
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public Boolean getWatched() {
        return isWatched;
    }

    public void setWatched(Boolean watched) {
        isWatched = watched;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Topic(String name, String videos) {
        Name = name;
        this.icon = icon;
        this.videos = videos;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Topic(String name, Drawable icon, String videos, Boolean isWatched) {
        Name = name;
        this.icon = icon;
        this.videos = videos;
        this.isWatched = isWatched;
    }

    public boolean isWatched() {
        return isWatched;
    }

    public void setWatched(boolean watched) {
        isWatched = watched;
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

