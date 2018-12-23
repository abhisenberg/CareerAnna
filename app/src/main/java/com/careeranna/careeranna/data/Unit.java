package com.careeranna.careeranna.data;

import android.graphics.drawable.Drawable;

import com.careeranna.careeranna.R;

import java.io.Serializable;
import java.util.ArrayList;

public class Unit implements Serializable {

    public String Name;
    public Drawable icon;
    public ArrayList<Topic> topics = new ArrayList<>();

    public Unit(String name, Drawable icon) {
        Name = name;
        this.icon = icon;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return Name;
    }
}
