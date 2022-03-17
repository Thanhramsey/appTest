package com.vnpt.dto;

import java.util.ArrayList;

/**
 * Created by apple on 8/17/16.
 */

public class BaseReponse {
    private int statusCode;
    private String message;

    public ArrayList<Object> getData() {
        return data;
    }

    public void setData(ArrayList<Object> data) {
        this.data = data;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private ArrayList<Object> data;


}
