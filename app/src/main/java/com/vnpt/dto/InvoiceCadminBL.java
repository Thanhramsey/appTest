package com.vnpt.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: lop Invoice Cadmin
 * @author:truonglt2
 * @since:Feb 7, 2014 4:13:08 PM
 * @version: 1.0
 * @since: 1.0
 */
@SuppressWarnings("serial")
public class InvoiceCadminBL extends BaseReponse implements Serializable {
    public int totalRecords;
    public ArrayList<InvoiceCadmin> arrData;
    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public ArrayList<InvoiceCadmin> getArrData() {
        return arrData;
    }

    public void setArrData(ArrayList<InvoiceCadmin> arrData) {
        this.arrData = arrData;
    }




}