package com.careeranna.careeranna.data;

import java.io.Serializable;

public class PromoCode implements Serializable {

    String code_id, user_id, code_name, discount_amount, active_status, from_date, to_date, product_id, promocode_details, min_amount, for_all;

    public PromoCode(String code_id, String user_id, String code_name, String discount_amount, String active_status, String from_date, String to_date, String product_id, String promocode_details, String min_amount, String for_all) {
        this.code_id = code_id;
        this.user_id = user_id;
        this.code_name = code_name;
        this.discount_amount = discount_amount;
        this.active_status = active_status;
        this.from_date = from_date;
        this.to_date = to_date;
        this.product_id = product_id;
        this.promocode_details = promocode_details;
        this.min_amount = min_amount;
        this.for_all = for_all;
    }

    public String getCode_id() {
        return code_id;
    }

    public void setCode_id(String code_id) {
        this.code_id = code_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCode_name() {
        return code_name;
    }

    public void setCode_name(String code_name) {
        this.code_name = code_name;
    }

    public String getDiscount_amount() {
        return discount_amount;
    }

    public void setDiscount_amount(String discount_amount) {
        this.discount_amount = discount_amount;
    }

    public String getActive_status() {
        return active_status;
    }

    public void setActive_status(String active_status) {
        this.active_status = active_status;
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getPromocode_details() {
        return promocode_details;
    }

    public void setPromocode_details(String promocode_details) {
        this.promocode_details = promocode_details;
    }

    public String getMin_amount() {
        return min_amount;
    }

    public void setMin_amount(String min_amount) {
        this.min_amount = min_amount;
    }

    public String getFor_all() {
        return for_all;
    }

    public void setFor_all(String for_all) {
        this.for_all = for_all;
    }
}
