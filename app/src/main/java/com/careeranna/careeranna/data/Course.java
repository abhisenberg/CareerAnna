package com.careeranna.careeranna.data;

import java.io.Serializable;

public class Course implements Serializable {

    private String id, name, imageUrl, category_id, price, desc, demo_url, type, price_before_discount, ratings, total_ratings, learners_count;

    public String getPrice_before_discount() {
        return price_before_discount;
    }

    public void setPrice_before_discount(String price_before_discount) {
        this.price_before_discount = price_before_discount;
    }

    public Course(String id, String name, String imageUrl, String category_id, String price, String desc, String demo_url, String type, String price_before_discount, String ratings, String total_ratings, String learners_count) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.category_id = category_id;
        this.price = price;
        this.desc = desc;
        this.demo_url = demo_url;
        this.type = type;
        this.price_before_discount = price_before_discount;
        this.ratings = ratings;
        this.total_ratings = total_ratings;
        this.learners_count = learners_count;
    }

    public String getLearners_count() {
        return learners_count;
    }

    public void setLearners_count(String learners_count) {
        this.learners_count = learners_count;
    }

    public Course(String id, String name, String imageUrl, String category_id, String price, String desc, String demo_url, String type, String price_before_discount, String ratings, String total_ratings) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.category_id = category_id;
        this.price = price;
        this.desc = desc;
        this.demo_url = demo_url;
        this.type = type;
        this.price_before_discount = price_before_discount;
        this.ratings = ratings;
        this.total_ratings = total_ratings;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public String getTotal_ratings() {
        return total_ratings;
    }

    public void setTotal_ratings(String total_ratings) {
        this.total_ratings = total_ratings;
    }

    public Course(String id, String name, String imageUrl, String category_id, String price, String desc, String demo_url, String type, String price_before_discount) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.category_id = category_id;
        this.price = price;
        this.desc = desc;
        this.demo_url = demo_url;
        this.type = type;
        this.price_before_discount = price_before_discount;
    }

    public Course(String id, String name, String imageUrl, String category_id, String price, String desc, String demo_url) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.category_id = category_id;
        this.price = price;
        this.desc = desc;
        this.demo_url = demo_url;
        this.type = "Paid";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDemo_url() {
        return demo_url;
    }

    public void setDemo_url(String demo_url) {
        this.demo_url = demo_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
