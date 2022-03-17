package com.vnpt.dto;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @Description: lop book
 * @author:truonglt2
 * @since:Feb 7, 2014 4:13:08 PM
 * @version: 1.0
 * @since: 1.0
 *
 */
@SuppressWarnings("serial")
public class ServiceDebit implements Serializable {
    private int id_services;
    private String name_service;
    private int quantity;
    private double price;
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getId_services() {
        return id_services;
    }

    public void setId_services(int id_services) {
        this.id_services = id_services;
    }

    public String getName_service() {
        return name_service;
    }

    public void setName_service(String name_service) {
        this.name_service = name_service;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }



}