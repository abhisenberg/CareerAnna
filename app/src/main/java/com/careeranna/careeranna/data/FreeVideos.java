package com.careeranna.careeranna.data;

import java.io.Serializable;

public class FreeVideos implements Serializable {

    String id, video_url, thumbnail, total_view, tags, title;

    public FreeVideos(String id, String video_url, String thumbnail, String total_view, String tags, String title) {
        this.id = id;
        this.video_url = video_url;
        this.thumbnail = thumbnail;
        this.total_view = total_view;
        this.tags = tags;
        this.title = title;
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
