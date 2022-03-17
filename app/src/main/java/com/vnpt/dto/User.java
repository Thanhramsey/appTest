package com.vnpt.dto;

import com.vnpt.common.Common;

import java.io.Serializable;

/**
 * @author:truonglt2
 * @since:Jan 21, 2014 4:43:57 PM
 * @Description:
 */
public class User implements Serializable {
    private int idUser;
    private int idRoleUser;
    private String username;
    private String createDate;
    private String nameRole;
    private String name;
    private String phone;
    private String address;

    public String getTaxcode() {
        return taxcode;
    }

    public void setTaxcode(String taxcode) {
        this.taxcode = taxcode;
    }

    private String taxcode;
    public int getId_staff() {
        return id_staff;
    }

    public void setId_staff(int id_staff) {
        this.id_staff = id_staff;
    }

    private int id_staff;
    private int idCustomer;
    private double longtitude;
    private double latitude;
    private String cmnd;
    private String email;
    private String ma_kh;
    private String ma_sms;
    private String name_unsigned;
    private Company company;

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdRoleUser() {
        return idRoleUser;
    }

    public void setIdRoleUser(int idRoleUser) {
        this.idRoleUser = idRoleUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getNameRole() {
        return nameRole;
    }

    public void setNameRole(String nameRole) {
        this.nameRole = nameRole;
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

    public int getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(int idCustomer) {
        this.idCustomer = idCustomer;
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


}
