package com.careeranna.careeranna.data;

import java.io.Serializable;

public class Article implements Serializable {

    private String id, name, image_url, author, category, content, created_at;

    public Article(String id, String name, String image_url, String author, String category, String content, String created_at) {
        this.id = id;
        this.name = name;
        this.image_url = image_url;
        this.author = author;
        this.category = category;
        this.content = content;
        this.created_at = created_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
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

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
