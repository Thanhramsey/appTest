package com.vnpt.dto;

import java.io.Serializable;
import java.util.Date;
/**
 * @Description: lop Invoice Cadmin
 * @author:truonglt2
 * @since:Feb 7, 2014 4:13:08 PM
 * @version: 1.0
 * @since: 1.0
 */
@SuppressWarnings("serial")
public class InvoiceCadmin implements Serializable {
    public String invNum;
    public String Fkey;
    public String pattern;
    public String serial;
    public String cusCode;
    public double amount;
    public double Total;
    public String cusAddress;
    public String cusname;
    public String publishDate;

    public String unitCurrency;
    public int PaymentStatus;
    public int getPaymentStatus() {
        return PaymentStatus;
    }

    public void setPaymentStatus(int paymentStatus) {
        PaymentStatus = paymentStatus;
    }

    public String getUnitCurrency() {
        return unitCurrency;
    }

    public void setUnitCurrency(String unitCurrency) {
        this.unitCurrency = unitCurrency;
        if(this.unitCurrency == null)
        {
            this.unitCurrency = "VNĐ";
        }
    }



    public String getInvNum() {
        return invNum;
    }

    public void setInvNum(String invNum) {
        this.invNum = invNum;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int status;

    public String getFkey() {
        return Fkey;
    }

    public void setFkey(String fkey) {
        Fkey = fkey;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getCusCode() {
        return cusCode;
    }

    public void setCusCode(String cusCode) {
        this.cusCode = cusCode;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCusAddress() {
        return cusAddress;
    }

    public void setCusAddress(String cusAddress) {
        this.cusAddress = cusAddress;
        if(this.cusAddress == null)
        {
            this.cusAddress = "";
        }
    }

    public String getCusname() {
        return cusname;
    }

    public void setCusname(String cusname) {
        this.cusname = cusname;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }


}