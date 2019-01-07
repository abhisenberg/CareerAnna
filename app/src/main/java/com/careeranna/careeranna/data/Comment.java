package com.careeranna.careeranna.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Comment {

    String id, name, email, comment_body, parent_id, ne_id, date;
    private ArrayList<Comment> comments;

    public Comment(String id, String name, String email, String comment_body, String parent_id, String ne_id, String date) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.comment_body = comment_body;
        this.parent_id = parent_id;
        this.ne_id = ne_id;
        this.date = date;
        this.comments = new ArrayList<>();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Comment(String id, String name, String email, String comment_body, String parent_id, String ne_id) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.comment_body = comment_body;
        this.parent_id = parent_id;
        this.ne_id = ne_id;
        this.comments = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getComment_body() {
        return comment_body;
    }

    public void setComment_body(String comment_body) {
        this.comment_body = comment_body;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getNe_id() {
        return ne_id;
    }

    public void setNe_id(String ne_id) {
        this.ne_id = ne_id;
    }

    public Comment() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(cal.getTime());

        this.comments = new ArrayList<>();
        this.date = strDate;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }
}
