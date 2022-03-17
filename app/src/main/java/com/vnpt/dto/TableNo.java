package com.vnpt.dto;

import java.io.Serializable;

/**
 * Created by apple on 8/17/16.
 */

public class TableNo implements Serializable{
    private int idTable;
    private String nameTable;
    private String checkNo;
    private int idInvoice;
    private String number_Inv;
    private int status;
    private String update_time;

    public String getIdImageTable() {
        return idImageTable;
    }

    public void setIdImageTable(String idImageTable) {
        this.idImageTable = idImageTable;
    }

    private String idImageTable;

    public int getIdTable() {
        return idTable;
    }

    public void setIdTable(int idTable) {
        this.idTable = idTable;
    }

    public String getNameTable() {
        return nameTable;
    }

    public void setNameTable(String nameTable) {
        this.nameTable = nameTable;
    }

    public String getCheckNo() {
        return checkNo;
    }

    public void setCheckNo(String checkNo) {
        this.checkNo = checkNo;
    }

    public int getIdInvoice() {
        return idInvoice;
    }

    public void setIdInvoice(int idInvoice) {
        this.idInvoice = idInvoice;
    }

    public String getNumber_Inv() {
        return number_Inv;
    }

    public void setNumber_Inv(String number_Inv) {
        this.number_Inv = number_Inv;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getSerial_Inv() {
        return serial_Inv;
    }

    public void setSerial_Inv(String serial_Inv) {
        this.serial_Inv = serial_Inv;
    }

    private String serial_Inv;

}
