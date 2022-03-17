package com.vnpt.dto;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @Description: lop InvoiceDetails
 * @author:truonglt2
 * @since:Feb 7, 2014 4:13:08 PM
 * @version: 1.0
 * @since: 1.0
 *
 */
@SuppressWarnings("serial")
public class InvoiceDetails implements Serializable {
    private int id_customer;
    private String name;
    private String phone;
    private String address;
    private double longtitude;
    private double latitude;
    private String cmnd;
    private String email;
    private String ma_kh;
    private String ma_sms;
    private String name_unsigned;
    private String id_user;

    public ArrayList<ItemDebit> getArrData() {
        return arrData;
    }

    public void setArrData(ArrayList<ItemDebit> arrData) {
        this.arrData = arrData;
    }

    public int getId_customer() {
        return id_customer;
    }

    public void setId_customer(int id_customer) {
        this.id_customer = id_customer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getCmnd() {
        return cmnd;
    }

    public void setCmnd(String cmnd) {
        this.cmnd = cmnd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMa_kh() {
        return ma_kh;
    }

    public void setMa_kh(String ma_kh) {
        this.ma_kh = ma_kh;
    }

    public String getMa_sms() {
        return ma_sms;
    }

    public void setMa_sms(String ma_sms) {
        this.ma_sms = ma_sms;
    }

    public String getName_unsigned() {
        return name_unsigned;
    }

    public void setName_unsigned(String name_unsigned) {
        this.name_unsigned = name_unsigned;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    private ArrayList<ItemDebit> arrData;
    
}