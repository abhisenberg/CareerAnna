package com.careeranna.careeranna.data;

public class Notification {
    public static final String NOTIF_LIST = "NotifyList";

    String title, description, image_url, date, redirectURl;


    public Notification(String title, String description, String image_url, String date, String redirectURl) {
        this.title = title;
        this.description = description;
        this.image_url = image_url;
        this.date = date;
        this.redirectURl = redirectURl;
    }

    public String getRedirectURl() {
        return redirectURl;
    }

    public void setRedirectURl(String redirectURl) {
        this.redirectURl = redirectURl;
    }

    public static String getNotifList() {
        return NOTIF_LIST;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
}
