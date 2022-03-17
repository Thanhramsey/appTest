package com.vnpt.dto;

import java.io.Serializable;

/**
 * @Description: lop Invoice Cadmin
 * @author:truonglt2
 * @since:Feb 7, 2014 4:13:08 PM
 * @version: 1.0
 * @since: 1.0
 */

public class InvoiceCadmin_X implements Serializable {
    public String key;
    public String Pattern;
    public String Serial;
    public String No;
    public String CheckNo;
    public String TableNo;
    public String RoomNo;
    public int StatusInv;
    public double Amount;

    public int getStatus() {
        return StatusInv;
    }

    public void setStatus(int status) {
        StatusInv = status;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPattern() {
        return Pattern;
    }

    public void setPattern(String pattern) {
        Pattern = pattern;
    }

    public String getSerial() {
        return Serial;
    }

    public void setSerial(String serial) {
        Serial = serial;
    }

    public String getNo() {
        return No;
    }

    public void setNo(String no) {
        No = no;
    }

    public String getCheckNo() {
        return CheckNo;
    }

    public void setCheckNo(String checkNo) {
        CheckNo = checkNo;
    }

    public String getTableNo() {
        return TableNo;
    }

    public void setTableNo(String tableNo) {
        TableNo = tableNo;
    }

    public String getRoomNo() {
        return RoomNo;
    }

    public void setRoomNo(String roomNo) {
        RoomNo = roomNo;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    @Override
    public String toString() {
        return "InvoiceCadmin{" +
                "key='" + key + '\'' +
                ", Pattern='" + Pattern + '\'' +
                ", Serial='" + Serial + '\'' +
                ", No='" + No + '\'' +
                ", CheckNo='" + CheckNo + '\'' +
                ", TableNo='" + TableNo + '\'' +
                ", RoomNo='" + RoomNo + '\'' +
                ", Amount=" + Amount +
                '}';
    }
}