package com.careeranna.careeranna.data;

public class UserWatchedVideoOfCourse {

    private String id, time_taken;

    public UserWatchedVideoOfCourse(String id, String time_taken) {
        this.id = id;
        this.time_taken = time_taken;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime_taken() {
        return time_taken;
    }

    public void setTime_taken(String time_taken) {
        this.time_taken = time_taken;
    }
}
