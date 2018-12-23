package com.careeranna.careeranna.dummy_data;

public class ProfileCourses {
    private String name, imgLink;
    private int progress;

    public ProfileCourses(String name, int progress, String imgLink){
        this.name = name;
        this.progress = progress;
        this.imgLink = imgLink;
    }

    public String getName(){
        return name;
    }

    public int getProgress(){
        return progress;
    }

    public String getImgLink(){
        return imgLink;
    }

}
