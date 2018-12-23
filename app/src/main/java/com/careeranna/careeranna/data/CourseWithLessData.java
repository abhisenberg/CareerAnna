package com.careeranna.careeranna.data;

public class CourseWithLessData {
    String course_name, course_ID, course_imageURL;

    public CourseWithLessData(String course_name, String course_ID, String course_imageURL) {
        this.course_name = course_name;
        this.course_ID = course_ID;
        this.course_imageURL = course_imageURL;
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
