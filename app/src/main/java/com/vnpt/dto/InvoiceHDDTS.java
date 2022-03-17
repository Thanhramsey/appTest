package com.vnpt.dto;

import java.io.Serializable;

/**
 * @Description: lop Invoice
 * @author:truonglt2
 * @since:Feb 7, 2014 4:13:08 PM
 * @version: 1.0
 * @since: 1.0
 * {\"ROWNUM\":1,\"TotalRow\":13,\"Id\":555,\"Pattern\":\"01GTKT0/001\",\"Serial\":\"AA/16E\",\"InvNo\":82,\"InvToken\":\"01GTKT0/001;AA/16E;82\",\"KindOfService\":\"2017-06-06T00:00:00\",\"IsWatch\":false,\"Status\":1,\"PaymentStatus\":1,\"KindOfInv\":0,\"CusCode\":\"0306212587\",\"CusName\":\"Cong Ty Co Phan Dich Vu Chu Du Hai Bon\",\"CusTaxCode\":\"0306212587\",\"PaymentCode\":\"c19589dfbc3546b39dc23ac370d41a2c\",\"Amount\":10890000}
 * <Item>
<index>5</index>
<invToken>01GTKT0/001;AA/17E;5</invToken>
<fkey>164901GTKT0001AA17E5</fkey>
<name>Hóa đơn giá trị gia tăng</name>
<publishDate>5/19/2017 10:54:11 AM</publishDate>
<signStatus>4</signStatus>
<total>10000000</total>
<amount>11000000</amount>
<pattern>01GTKT0/001</pattern>
<serial>AA/17E</serial>
<invNum>0000005</invNum>
<status>1</status>
<cusname><![CDATA[Lê Tấn Trương]]></cusname>
<payment>1</payment>
</Item>
 */
@SuppressWarnings("serial")



public class InvoiceHDDTS implements Serializable {
    private int index;
    private int status;//Thẻ <status> chứa trạng thái hóa đơn: 1- hóa đơn đã phát hành, 3- hóa đơn bị thay thế, 4- hóa đơn bị điều chỉnh; 5- hóa đơn bị hủy
    private String cusCode;
    private String name;
    private String publishDate;
    private int signStatus;
    private String pattern;
    private String serial;
    private String invNum;
    private String cusname;
    private int payment;
    private int converted;
    private double amount;
    private int month;
    private String tokenInv;
//    private InvoiceHDDTDetails invoiceHDDTDetails;
//
//    public InvoiceHDDTDetails getInvoiceHDDTDetails() {
//        return invoiceHDDTDetails;
//    }

//    public void setInvoiceHDDTDetails(InvoiceHDDTDetails invoiceHDDTDetails) {
//        this.invoiceHDDTDetails = invoiceHDDTDetails;
//    }


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCusCode() {
        return cusCode;
    }

    public void setCusCode(String cusCode) {
        this.cusCode = cusCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public int getSignStatus() {
        return signStatus;
    }

    public void setSignStatus(int signStatus) {
        this.signStatus = signStatus;
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

    public String getInvNum() {
        return invNum;
    }

    public void setInvNum(String invNum) {
        this.invNum = invNum;
    }

    public String getCusname() {
        return cusname;
    }

    public void setCusname(String cusname) {
        this.cusname = cusname;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public int getConverted() {
        return converted;
    }

    public void setConverted(int converted) {
        this.converted = converted;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getTokenInv() {
        return this.getPattern()+";"+ this.getSerial()+";"+this.getInvNum();
    }

    public void setTokenInv(String tokenInv) {
        this.tokenInv = tokenInv;
    }

}