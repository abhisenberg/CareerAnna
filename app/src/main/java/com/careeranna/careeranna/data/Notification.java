package com.careeranna.careeranna.data;

public class Notification {
    public static final String NOTIF_LIST = "NotifyList";

    String title, image_url, date, redirectURlId, type, upper_title;

    public String getUpper_title() {
        return upper_title;
    }

    public void setUpper_title(String upper_title) {
        this.upper_title = upper_title;
    }

    public Notification(String title, String image_url, String date, String redirectURlId, String type) {
        this.title = title;
        this.image_url = image_url;
        this.date = date;
        this.redirectURlId = redirectURlId;
        this.type = type;
    }

    public String getRedirectURlId() {
        return redirectURlId;
    }

    public void setRedirectURlId(String redirectURlId) {
        this.redirectURlId = redirectURlId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRedirectURl() {
        return redirectURlId;
    }

    public void setRedirectURl(String redirectURl) {
        this.redirectURlId = redirectURl;
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

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
