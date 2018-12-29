package com.careeranna.careeranna.data;

import java.util.Random;

public class CourseWithLessData {
    String course_name, course_ID, course_imageURL, progress, category_id;

    public CourseWithLessData(String course_name, String course_ID, String course_imageURL) {
        this.course_name = course_name;
        this.course_ID = course_ID;
        this.course_imageURL = course_imageURL;
        this.progress = "0";
    }



    public CourseWithLessData(String course_name, String course_ID, String course_imageURL, String progress) {
        this.course_name = course_name;
        this.course_ID = course_ID;
        this.course_imageURL = course_imageURL;
        this.progress = progress;
    }

    public CourseWithLessData(String course_name, String course_ID, String course_imageURL, String progress, String category_id) {
        this.course_name = course_name;
        this.course_ID = course_ID;
        this.course_imageURL = course_imageURL;
        this.progress = progress;
        this.category_id = category_id;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getCourse_ID() {
        return course_ID;
    }

    public void setCourse_ID(String course_ID) {
        this.course_ID = course_ID;
    }

    public String getCourse_imageURL() {
        return course_imageURL;
    }

    public void setCourse_imageURL(String course_imageURL) {
        this.course_imageURL = course_imageURL;
    }
}
