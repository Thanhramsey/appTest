package com.vnpt.dto;

/**
 * Created by apple on 8/17/16.
 */

public class Company {
    private int idCompany;
    private String nameCompany;
    private String phone;
    private String address;
    private String logo;
    private String other;
    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public int getIdCompany() {
        return idCompany;
    }

    public void setIdCompany(int idCompany) {
        this.idCompany = idCompany;
    }

    public String getNameCompany() {
        return nameCompany;
    }

    public void setNameCompany(String nameCompany) {
        this.nameCompany = nameCompany;
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }


}
