package com.vnpt.dto;

import java.io.Serializable;

/**
 * @Description: lop book
 * @author:truonglt2
 * @since:Feb 7, 2014 4:13:08 PM
 * @version: 1.0
 * @since: 1.0
 */
@SuppressWarnings("serial")
public class HoaDon implements Serializable {
    private int idInvoice;
    private int idCustomer;
    private String nameCus;
    private String phone;
    private String address;

    private double longitude;
    private double latitude;
    private String CMND;
    private String email;

    private String numberInvoice;
    private String ngayThu;
    private String price;
    private String status;
    private String dueDate;
    private String tax;

    private String noSerial;

    public String getStateDebit() {
        return stateDebit;
    }

    public void setStateDebit(String stateDebit) {
        this.stateDebit = stateDebit;
    }

    private String stateDebit;

    public String getNoSerial() {
        return noSerial;
    }

    public void setNoSerial(String noSerial) {
        this.noSerial = noSerial;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }
    private String codeCustomer;
    private String codeTaxCustomer;
    private String smRepresent;

    public String getUnsignedNameCus() {
        return unsignedNameCus;
    }

    public void setUnsignedNameCus(String unsignedNameCus) {
        this.unsignedNameCus = unsignedNameCus;
    }

    public String getCodeCustomer() {
        return codeCustomer;
    }

    public void setCodeCustomer(String codeCustomer) {
        this.codeCustomer = codeCustomer;
    }

    public String getCodeTaxCustomer() {
        return codeTaxCustomer;
    }

    public void setCodeTaxCustomer(String codeTaxCustomer) {
        this.codeTaxCustomer = codeTaxCustomer;
    }

    public String getSmRepresent() {
        return smRepresent;
    }

    public void setSmRepresent(String smRepresent) {
        this.smRepresent = smRepresent;
    }

    private String unsignedNameCus;
    /* chuối phân cách nhau bởi "-"
    * default 0: Chưa chứng thực
    * 1: chứng thực chữ ký
    * 2: chứng thực record
    * 3: chứng thực hình ảnh
    * */
    private String certifiedCustomer;// default 0
    public String getPathCertified() {
        return pathCertified;
    }

    public void setPathCertified(String pathCertified) {
        this.pathCertified = pathCertified;
    }

    public String getCertifiedCustomer() {
        return certifiedCustomer;
    }

    public void setCertifiedCustomer(String certifiedCustomer) {
        this.certifiedCustomer = certifiedCustomer;
    }

    private String pathCertified;
    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public int getIdInvoice() {
        return idInvoice;
    }

    public void setIdInvoice(int idInvoice) {
        this.idInvoice = idInvoice;
    }

    public int getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(int idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getNameCus() {
        return nameCus;
    }

    public void setNameCus(String nameCus) {
        this.nameCus = nameCus;
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

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getCMND() {
        return CMND;
    }

    public void setCMND(String CMND) {
        this.CMND = CMND;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumberInvoice() {
        return numberInvoice;
    }

    public void setNumberInvoice(String numberInvoice) {
        this.numberInvoice = numberInvoice;
    }

    public String getNgayThu() {
        return ngayThu;
    }

    public void setNgayThu(String ngayThu) {
        this.ngayThu = ngayThu;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }





}