package com.careeranna.careeranna.data;

import java.io.Serializable;

public class SubCategory implements Serializable {

    private String EXAM_NAME_ID, EXAM_NAME, CATEGORY_ID, ACTIVE_STATUS;

    public SubCategory(String EXAM_NAME_ID, String EXAM_NAME, String CATEGORY_ID, String ACTIVE_STATUS) {
        this.EXAM_NAME_ID = EXAM_NAME_ID;
        this.EXAM_NAME = EXAM_NAME;
        this.CATEGORY_ID = CATEGORY_ID;
        this.ACTIVE_STATUS = ACTIVE_STATUS;
    }

    public String getEXAM_NAME_ID() {
        return EXAM_NAME_ID;
    }

    public void setEXAM_NAME_ID(String EXAM_NAME_ID) {
        this.EXAM_NAME_ID = EXAM_NAME_ID;
    }

    public String getEXAM_NAME() {
        return EXAM_NAME;
    }

    public void setEXAM_NAME(String EXAM_NAME) {
        this.EXAM_NAME = EXAM_NAME;
    }

    public String getCATEGORY_ID() {
        return CATEGORY_ID;
    }

    public void setCATEGORY_ID(String CATEGORY_ID) {
        this.CATEGORY_ID = CATEGORY_ID;
    }

    public String getACTIVE_STATUS() {
        return ACTIVE_STATUS;
    }

    public void setACTIVE_STATUS(String ACTIVE_STATUS) {
        this.ACTIVE_STATUS = ACTIVE_STATUS;
    }
}
