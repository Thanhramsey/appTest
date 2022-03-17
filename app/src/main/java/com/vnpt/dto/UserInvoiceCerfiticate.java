package com.vnpt.dto;

import java.io.Serializable;

/**
 * @Description: lop UserInvoiceCerfiticate
 * @author:truonglt2
 * @since:Feb 7, 2014 4:13:08 PM
 * @version: 1.0
 * @since: 1.0
 * id_user_invoice_cerfiticate": 1,
"id_invoice": 1,
"id_user": 1,
"path_cerfiticate": null,
"id_type_cerfiticate": 1
 */
@SuppressWarnings("serial")
public class UserInvoiceCerfiticate implements Serializable {
    private int id_user_invoice_cerfiticate;
    private int id_invoice;
    private int id_user;
    private String path_cerfiticate;
    private int id_type_cerfiticate;

    public int getId_type_cerfiticate() {
        return id_type_cerfiticate;
    }

    public void setId_type_cerfiticate(int id_type_cerfiticate) {
        this.id_type_cerfiticate = id_type_cerfiticate;
    }

    public int getId_user_invoice_cerfiticate() {
        return id_user_invoice_cerfiticate;
    }

    public void setId_user_invoice_cerfiticate(int id_user_invoice_cerfiticate) {
        this.id_user_invoice_cerfiticate = id_user_invoice_cerfiticate;
    }

    public int getId_invoice() {
        return id_invoice;
    }

    public void setId_invoice(int id_invoice) {
        this.id_invoice = id_invoice;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getPath_cerfiticate() {
        return path_cerfiticate;
    }

    public void setPath_cerfiticate(String path_cerfiticate) {
        this.path_cerfiticate = path_cerfiticate;
    }


}