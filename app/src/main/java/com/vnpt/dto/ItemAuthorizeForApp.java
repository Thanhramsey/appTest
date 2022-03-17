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
public class ItemAuthorizeForApp implements Serializable {
    private int idItem;
    private String nameAuthorize;
    public String getNameAuthorize() {
        return nameAuthorize;
    }

    public void setNameAuthorize(String nameAuthorize) {
        this.nameAuthorize = nameAuthorize;
    }

    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }



}