package com.careeranna.careeranna.data;

public class PaidCourse {

    private String product_id,
            course_name,
            price,
            product_image,
            exam_id,
            discount,
            course_video_url,
            average_rating,
            total_rating,
            learners_count;

    public PaidCourse(String product_id, String course_name, String price, String product_image, String exam_id, String discount, String course_video_url, String average_rating, String total_rating, String learners_count) {
        this.product_id = product_id;
        this.course_name = course_name;
        this.price = price;
        this.product_image = product_image;
        this.exam_id = exam_id;
        this.discount = discount;
        this.course_video_url = course_video_url;
        this.average_rating = average_rating;
        this.total_rating = total_rating;
        this.learners_count = learners_count;
    }

    public String getLearners_count() {
        return learners_count;
    }

    public void setLearners_count(String learners_count) {
        this.learners_count = learners_count;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getExam_id() {
        return exam_id;
    }

    public void setExam_id(String exam_id) {
        this.exam_id = exam_id;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getCourse_video_url() {
        return course_video_url;
    }

    public void setCourse_video_url(String course_video_url) {
        this.course_video_url = course_video_url;
    }

    public String getAverage_rating() {
        return average_rating;
    }

    public void setAverage_rating(String average_rating) {
        this.average_rating = average_rating;
    }

    public String getTotal_rating() {
        return total_rating;
    }

    public void setTotal_rating(String total_rating) {
        this.total_rating = total_rating;
    }
}
