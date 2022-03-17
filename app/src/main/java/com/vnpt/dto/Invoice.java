package com.vnpt.dto;

import java.io.Serializable;

/**
 * @Description: lop Invoice
 * @author:truonglt2
 * @since:Feb 7, 2014 4:13:08 PM
 * @version: 1.0
 * @since: 1.0
 */
@SuppressWarnings("serial")
public class Invoice implements Serializable {
    private int idInvoices;
    private String number_no;
    private int status;

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    private String update_time;
    private String serial_no;
    private int state_debit;
    private int id_item_invoice;
    private int id_staff;
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
    private int id_user;

    private double sumPrice;
    private double tax;
    private double totalPrice;
    private String nameTerm;
    private String nameCurrency;
    private String unitCurrency;

    public String getUnitCurrency() {
        return unitCurrency;
    }

    public void setUnitCurrency(String unitCurrency) {
        this.unitCurrency = unitCurrency;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getNameTerm() {
        return nameTerm;
    }

    public void setNameTerm(String nameTerm) {
        this.nameTerm = nameTerm;
    }

    public String getNameCurrency() {
        return nameCurrency;
    }

    public void setNameCurrency(String nameCurrency) {
        this.nameCurrency = nameCurrency;
    }

    public double getSumPrice() {
        return sumPrice;
    }

    public void setSumPrice(double sumPrice) {
        this.sumPrice = sumPrice;
    }


    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public int getIdInvoices() {
        return idInvoices;
    }

    public void setIdInvoices(int idInvoices) {
        this.idInvoices = idInvoices;
    }

    public String getNumber_no() {
        return number_no;
    }

    public void setNumber_no(String number_no) {
        this.number_no = number_no;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public String getSerial_no() {
        return serial_no;
    }

    public void setSerial_no(String serial_no) {
        this.serial_no = serial_no;
    }

    public int getState_debit() {
        return state_debit;
    }

    public void setState_debit(int state_debit) {
        this.state_debit = state_debit;
    }


    public int getId_item_invoice() {
        return id_item_invoice;
    }

    public void setId_item_invoice(int id_item_invoice) {
        this.id_item_invoice = id_item_invoice;
    }

    public int getId_staff() {
        return id_staff;
    }

    public void setId_staff(int id_staff) {
        this.id_staff = id_staff;
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


}