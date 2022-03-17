package com.vnpt.dto;

public class TicketItem {
    private String invNum;
    private int status;
    private String cusname;

    public TicketItem(String invNum, int status, String cusname) {
        this.invNum = invNum;
        this.status = status;
        this.cusname = cusname;
    }

    public TicketItem() {
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

    public String getCusname() {
        return cusname;
    }

    public void setCusname(String cusname) {
        this.cusname = cusname;
    }
}
