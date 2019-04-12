package com.careeranna.careeranna.data;

import java.io.Serializable;

public class MyPaidCourse implements Serializable {

    private String product_id;

    public MyPaidCourse(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }
}
