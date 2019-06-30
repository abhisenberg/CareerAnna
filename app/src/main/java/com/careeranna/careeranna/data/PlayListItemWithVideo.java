package com.careeranna.careeranna.data;

import java.util.ArrayList;

public class PlayListItemWithVideo {

    String name, videoId;
    ArrayList<FreeVideos> videos;

    public PlayListItemWithVideo(String name, String videoId) {
        this.name = name;
        this.videoId = videoId;
        this.videos = new ArrayList<>();
    }

    public ArrayList<FreeVideos> getVideos() {
        return videos;
    }

    public void setVideos(ArrayList<FreeVideos> videos) {
        this.videos = videos;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
