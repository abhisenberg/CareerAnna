package com.careeranna.careeranna.data;

import java.io.Serializable;

public class FreeVideos implements Serializable {

    String id, video_url, thumbnail, total_view, tags, title, type, duration, likes, dislikes;

    public FreeVideos(String id, String video_url, String thumbnail, String total_view, String tags, String title, String type, String duration, String likes, String dislikes) {
        this.id = id;
        this.video_url = video_url;
        this.thumbnail = thumbnail;
        this.total_view = total_view;
        this.tags = tags;
        this.title = title;
        this.type = type;
        this.duration = duration;
        this.likes = likes;
        this.dislikes = dislikes;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getDislikes() {
        return dislikes;
    }

    public void setDislikes(String dislikes) {
        this.dislikes = dislikes;
    }

    public FreeVideos(String id, String video_url, String thumbnail, String total_view, String tags, String title) {
        this.id = id;
        this.video_url = video_url;
        this.thumbnail = thumbnail;
        this.total_view = total_view;
        this.tags = tags;
        this.title = title;
        this.type = "Free";
        this.duration = "0";
        this.tags = "null";
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public FreeVideos(String id, String video_url, String thumbnail, String total_view, String tags, String title, String type, String duration) {
        this.id = id;
        this.video_url = video_url;
        this.thumbnail = thumbnail;
        this.total_view = total_view;
        this.tags = tags;
        this.title = title;
        this.type = type;
        this.duration = duration;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public FreeVideos() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTotal_view() {
        return total_view;
    }

    public void setTotal_view(String total_view) {
        this.total_view = total_view;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
