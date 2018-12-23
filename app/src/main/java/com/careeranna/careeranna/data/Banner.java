package com.careeranna.careeranna.data;

import com.google.gson.annotations.SerializedName;

public class Banner {

    private String id;
    private String title;
    private String image_url;

    public Banner(String id, String name, String image_url) {
        this.id = id;
        this.title = name;
        this.image_url = image_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getmName() {
        return title;
    }

    public void setmName(String mName) {
        this.title = mName;
    }

    public String getmLink() {
        return image_url;
    }

    public void setmLink(String mLink) {
        this.image_url = mLink;
    }
}
