package com.careeranna.careeranna.data;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderedList implements Serializable {

    ArrayList<OrderedCourse> courses;

    public OrderedList(ArrayList<OrderedCourse> courses) {
        this.courses = courses;
    }

    public ArrayList<OrderedCourse> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<OrderedCourse> courses) {
        this.courses = courses;
    }
}
