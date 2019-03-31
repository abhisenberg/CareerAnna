package com.careeranna.careeranna.data;

public class FreeCourse {

    private String name;
    private String course_id;
    private String image;
    private String total_rating;
    private String average_rating;
    private String learner_count;

    public FreeCourse(String name, String course_id, String image, String total_rating, String average_rating, String learners_count) {
        this.name = name;
        this.course_id = course_id;
        this.image = image;
        this.total_rating = total_rating;
        this.average_rating = average_rating;
        this.learner_count = learners_count;
    }

    public String getLearners_count() {
        return learner_count;
    }

    public void setLearners_count(String learners_count) {
        this.learner_count = learners_count;
    }

    public String getTotal_rating() {
        return total_rating;
    }

    public String getAverage_rating() {
        return average_rating;
    }

    public void setAverage_rating(String average_rating) {
        this.average_rating = average_rating;
    }

    public void setTotal_rating(String total_rating) {
        this.total_rating = total_rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
