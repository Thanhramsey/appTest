package com.vnpt.dto;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @Description: lop book
 * @author:truonglt2
 * @since:Feb 7, 2014 4:13:08 PM
 * @version: 1.0
 * @since: 1.0
 */
@SuppressWarnings("serial")
public class ItemDebit implements Serializable {
    private int idInvoices;
    private String number_no;
    private String status;

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    private String update_time;
    private String serial_no;
    private int state_debit;
    private double sumPrice;
    private double tax;
    private double totalPrice;
    private String unitItem;
    private String nameCurrency;
    private String unitCurrency;
    private String nameTerm;

    public ArrayList<UserInvoiceCerfiticate> getListUserInvoiceCerfiticates() {
        return listUserInvoiceCerfiticates;
    }

    public void setListUserInvoiceCerfiticates(ArrayList<UserInvoiceCerfiticate> listUserInvoiceCerfiticates) {
        this.listUserInvoiceCerfiticates = listUserInvoiceCerfiticates;
    }

    private ArrayList<UserInvoiceCerfiticate> listUserInvoiceCerfiticates;
    private ArrayList<ServiceDebit> itemInvoice;
    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }


    public ArrayList<ServiceDebit> getItemInvoice() {
        return itemInvoice;
    }

    public void setItemInvoice(ArrayList<ServiceDebit> itemInvoice) {
        this.itemInvoice = itemInvoice;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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

    public double getSumPrice() {
        return sumPrice;
    }

    public void setSumPrice(double sumPrice) {
        this.sumPrice = sumPrice;
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

    public String getUnitItem() {
        return unitItem;
    }

    public void setUnitItem(String unitItem) {
        this.unitItem = unitItem;
    }

    public String getNameCurrency() {
        return nameCurrency;
    }

    public void setNameCurrency(String nameCurrency) {
        this.nameCurrency = nameCurrency;
    }

    public String getUnitCurrency() {
        return unitCurrency;
    }

    public void setUnitCurrency(String unitCurrency) {
        this.unitCurrency = unitCurrency;
    }

    public String getNameTerm() {
        return nameTerm;
    }

    public void setNameTerm(String nameTerm) {
        this.nameTerm = nameTerm;
    }



}