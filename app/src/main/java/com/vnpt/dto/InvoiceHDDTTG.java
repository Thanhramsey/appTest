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
public class InvoiceHDDTTG implements Serializable {
    // phần thuộc tính cho dữ liệu XML
    private int index;
//    private String invToken;
    private String fkey;
    private String name;
    private String publishDate;
    private String signStatus;
    private double total;
//    private double amount;
//    private String pattern;
//    private String serial;
    private String invNum;
//    private int status;
    private String cusname;
    private int payment;

    private String Pattern;
    private String Serial;
    private String InvToken;
    private int Status;//Thẻ <status> chứa trạng thái hóa đơn: 1- hóa đơn đã phát hành, 3- hóa đơn bị thay thế, 4- hóa đơn bị điều chỉnh; 5- hóa đơn bị hủy
    private double Amount;

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        this.Amount = amount;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getInvToken() {
        return InvToken;
    }

    public void setInvToken(String invToken) {
        this.InvToken = invToken;
    }

    public String getFkey() {
        return fkey;
    }

    public void setFkey(String fkey) {
        this.fkey = fkey;
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

    public String getSignStatus() {
        return signStatus;
    }

    public void setSignStatus(String signStatus) {
        this.signStatus = signStatus;
    }


    public String getPattern() {
        return Pattern;
    }

    public void setPattern(String pattern) {
        this.Pattern = pattern;
    }

    public String getSerial() {
        return Serial;
    }

    public void setSerial(String serial) {
        this.Serial = serial;
    }

    public String getInvNum() {
        return invNum;
    }

    public void setInvNum(String invNum) {
        this.invNum = invNum;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        this.Status = status;
    }

    public String getCusname() {
        return cusname;
    }

    public void setCusname(String cusname) {
        this.cusname = cusname;
    }

    // phần thuộc tính cho dữ liệu JSON
    //{\"ROWNUM\":1,\"TotalRow\":13,\"Id\":555,\"Pattern\":\"01GTKT0/001\",\"Serial\":\"AA/16E\",\"InvNo\":82,
    // \"InvToken\":\"01GTKT0/001;AA/16E;82\",\"KindOfService\":\"2017-06-06T00:00:00\",\"IsWatch\":false,
    // \"Status\":1,\"PaymentStatus\":1,\"KindOfInv\":0,\"CusCode\":\"0306212587\",\"CusName\":\"Cong Ty Co Phan Dich Vu Chu Du Hai Bon\",
    // \"CusTaxCode\":\"0306212587\",\"PaymentCode\":\"c19589dfbc3546b39dc23ac370d41a2c\",\"Amount\":10890000}
    private int ROWNUM;
    private int TotalRow;
    private int Id;

    /*private String Pattern;
    private String Serial;
    private String InvToken;
    private int Status;
    private double Amount;*/

    private String InvNo;
    private String KindOfService;
    private boolean IsWatch;
    private int PaymentStatus;
    private int KindOfInv;
    private String CusCode;
    private String CusName;
    private String CusTaxCode;
    private String PaymentCode;

    private int KindOfDataInvoice;// 1 - JSON 2 - XML
    public int getKindOfDataInvoice() {
        return KindOfDataInvoice;
    }

    public void setKindOfDataInvoice(int kindOfDataInvoice) {
        KindOfDataInvoice = kindOfDataInvoice;
    }



    public int getROWNUM() {
        return ROWNUM;
    }

    public void setROWNUM(int ROWNUM) {
        this.ROWNUM = ROWNUM;
    }

    public int getTotalRow() {
        return TotalRow;
    }

    public void setTotalRow(int totalRow) {
        TotalRow = totalRow;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getInvNo() {
        return InvNo;
    }

    public void setInvNo(String invNo) {
        InvNo = invNo;
    }

    public String getKindOfService() {
        return KindOfService;
    }

    public void setKindOfService(String kindOfService) {
        KindOfService = kindOfService;
    }

    public boolean isWatch() {
        return IsWatch;
    }

    public void setWatch(boolean watch) {
        IsWatch = watch;
    }

    public int getPaymentStatus() {
        return PaymentStatus;
    }

    public void setPaymentStatus(int paymentStatus) {
        PaymentStatus = paymentStatus;
    }

    public int getKindOfInv() {
        return KindOfInv;
    }

    public void setKindOfInv(int kindOfInv) {
        KindOfInv = kindOfInv;
    }

    public String getCusCode() {
        return CusCode;
    }

    public void setCusCode(String cusCode) {
        CusCode = cusCode;
    }

    public String getCusName() {
        return CusName;
    }

    public void setCusName(String cusName) {
        CusName = cusName;
    }

    public String getCusTaxCode() {
        return CusTaxCode;
    }

    public void setCusTaxCode(String cusTaxCode) {
        CusTaxCode = cusTaxCode;
    }

    public String getPaymentCode() {
        return PaymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        PaymentCode = paymentCode;
    }


}