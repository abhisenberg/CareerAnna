package com.careeranna.careeranna.data;

import java.io.Serializable;

public class OrderedCourse implements Serializable {

    String course_id, price, image, name, category_id, old_price, total_rating, average_rating;

    public OrderedCourse(String course_id, String price, String image, String name, String category_id, String old_price, String total_rating, String average_rating) {
        this.course_id = course_id;
        this.price = price;
        this.image = image;
        this.name = name;
        this.category_id = category_id;
        this.old_price = old_price;
        this.total_rating = total_rating;
        this.average_rating = average_rating;
    }

    public String getTotal_rating() {
        return total_rating;
    }

    public void setTotal_rating(String total_rating) {
        this.total_rating = total_rating;
    }

    public String getAverage_rating() {
        return average_rating;
    }

    public void setAverage_rating(String average_rating) {
        this.average_rating = average_rating;
    }

    public OrderedCourse(String course_id, String name, String price, String image) {
        this.course_id = course_id;
        this.price = price;
        this.image = image;
        this.name = name;
    }

    public OrderedCourse(String course_id, String price, String image, String name, String category_id, String old_price) {
        this.course_id = course_id;
        this.price = price;
        this.image = image;
        this.name = name;
        this.category_id = category_id;
        this.old_price = old_price;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public OrderedCourse(String course_id, String price, String image, String name, String category_id) {
        this.course_id = course_id;
        this.price = price;
        this.image = image;
        this.name = name;
        this.category_id = category_id;
        this.old_price = "0";
    }

    public String getOld_price() {
        return old_price;
    }

    public void setOld_price(String old_price) {
        this.old_price = old_price;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

