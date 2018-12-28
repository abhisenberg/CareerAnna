package com.careeranna.careeranna.data;

import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;

import com.careeranna.careeranna.VideoWithComment;

import retrofit2.http.GET;

public class MenuList {

    String name;
    
    Drawable drawable;
    String color;
    String insideColor;
    int gravity;
    int visibility;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public MenuList(String name, Drawable drawable, String color, String insideColor, int gravity) {
        this.name = name;
        this.drawable = drawable;
        this.color = color;
        this.insideColor = insideColor;
        this.gravity = gravity;
    }

    public int getgravity() {
        return gravity;
    }

    public void setgravity(int gravity) {
        this.gravity = gravity;
    }

    public MenuList(String name, Drawable drawable, String color, String insideColor) {
        this.name = name;
        this.drawable = drawable;
        this.color = color;
        this.insideColor = insideColor;
    }

    public String getInsideColor() {
        return insideColor;
    }

    public void setInsideColor(String insideColor) {
        this.insideColor = insideColor;
    }

    public MenuList(String name) {
        this.name = name;
    }

    public MenuList(String name, Drawable drawable) {
        this.name = name;
        this.drawable = drawable;
        this.color = "#FFF5F3F3";
        this.insideColor = "#FF0780C9";
        this.gravity = Gravity.LEFT;
        this.visibility = View.VISIBLE;
    }

    public MenuList(String name, Drawable drawable, String color) {
        this.name = name;
        this.drawable = drawable;
        this.color = color;
    }

    public int getGravity() {
        return gravity;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public MenuList(String name, Drawable drawable, String color, String insideColor, int gravity, int visibility) {
        this.name = name;
        this.drawable = drawable;
        this.color = color;
        this.insideColor = insideColor;
        this.gravity = gravity;
        this.visibility = visibility;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }
}
