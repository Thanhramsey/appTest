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
public class ItemServiceTelecom implements Serializable {
    private int idService;

    public ItemServiceTelecom(int idService, String nameService, double priceService) {
        this.idService = idService;
        this.nameService = nameService;
        this.priceService = priceService;
    }

    public String getNameService() {
        return nameService;
    }

    public void setNameService(String nameService) {
        this.nameService = nameService;
    }

    public int getIdService() {
        return idService;
    }

    public void setIdService(int idService) {
        this.idService = idService;
    }

    private String nameService;

    public double getPriceService() {
        return priceService;
    }

    public void setPriceService(double priceService) {
        this.priceService = priceService;
    }

    private double priceService;

}