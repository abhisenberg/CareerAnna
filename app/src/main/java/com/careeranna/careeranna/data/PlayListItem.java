package com.careeranna.careeranna.data;

public class PlayListItem {

    String name, videoId;

    public PlayListItem(String name, String videoId) {
        this.name = name;
        this.videoId = videoId;
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
