package com.careeranna.careeranna.data;

public class Notification {
    public static final String NOTIF_LIST = "NotifList";

    String title, description, image_url, type, id;

    public Notification(String title, String description, String image_url, String type, String id) {
        this.title = title;
        this.description = description;
        this.image_url = image_url;
        this.type = type;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
