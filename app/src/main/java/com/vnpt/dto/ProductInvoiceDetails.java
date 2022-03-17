/*
 * Copyright (c) 2017. Author: truonglt@vnpt.vn
 */

package com.vnpt.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ProductInvoiceDetails implements Serializable {
    private String Code;
    private String Remark;
    private String ProdName;
    private String ProdUnit;
    private double Total;
    private double ProdQuantity;


    private double ProdPrice;
    private double Discount;
    private double DiscountAmount;
    private double VATRate;
    private double VATAmount;
    private double Amount;
    private double FeeID;
    private double FeeRate;
    private String Extra1;
    private String Extra2;
    private String CurrencyUnit;
    public String getCurrencyUnit() {
        return CurrencyUnit;
    }

    public void setCurrencyUnit(String currencyUnit) {
        CurrencyUnit = currencyUnit;
    }




    public void setFeeID(double feeID) {
        FeeID = feeID;
    }

    public double getFeeRate() {
        return FeeRate;
    }

    public void setFeeRate(double feeRate) {
        FeeRate = feeRate;
    }

    public String getExtra1() {
        return Extra1;
    }

    public void setExtra1(String extra1) {
        Extra1 = extra1;
    }

    public String getExtra2() {
        return Extra2;
    }

    public void setExtra2(String extra2) {
        Extra2 = extra2;
    }

    public void setProdPrice(double prodPrice) {
        ProdPrice = prodPrice;
    }

    public double getProdPrice() {
        return ProdPrice;
    }

    public double getFeeID() {
        return FeeID;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getProdName() {
        return ProdName;
    }

    public void setProdName(String prodName) {
        ProdName = prodName;
    }

    public String getProdUnit() {
        return ProdUnit;
    }

    public void setProdUnit(String prodUnit) {
        ProdUnit = prodUnit;
    }

    public double getTotal() {
        return Total;
    }

    public void setTotal(double total) {
        Total = total;
    }

    public double getProdQuantity() {
        return ProdQuantity;
    }

    public void setProdQuantity(double prodQuantity) {
        ProdQuantity = prodQuantity;
    }

    public double getDiscount() {
        return Discount;
    }

    public void setDiscount(double discount) {
        Discount = discount;
    }

    public double getDiscountAmount() {
        return DiscountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        DiscountAmount = discountAmount;
    }

    public double getVATRate() {
        return VATRate;
    }

    public void setVATRate(double VATRate) {
        this.VATRate = VATRate;
    }

    public double getVATAmount() {
        return VATAmount;
    }

    public void setVATAmount(double VATAmount) {
        this.VATAmount = VATAmount;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }


}