package com.careeranna.careeranna.data;

public class Instructor {
    private String name, image_url, desc, email;

    public Instructor(String name, String image_url, String desc, String email) {
        this.name = name;
        this.image_url = image_url;
        this.desc = desc;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getDesc() {
        return desc;
    }
}
