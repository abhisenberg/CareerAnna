package com.careeranna.careeranna.dummy_data;

public class ProfileCerti {

    private String certiName, certiDate, certiLink, certiImage;

    public ProfileCerti(String certiName, String certiDate, String certiLink, String certiImage) {
        this.certiName = certiName;
        this.certiDate = certiDate;
        this.certiLink = certiLink;
        this.certiImage = certiImage;
    }

    public String getCertiName() {
        return certiName;
    }

    public String getCertiDate() {
        return certiDate;
    }

    public String getCertiLink() {
        return certiLink;
    }

    public String getCertiImage() {
        return certiImage;
    }
}
