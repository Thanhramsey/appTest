/*
 * Copyright (c) 2017. Author: truonglt@vnpt.vn
 */

package com.vnpt.dto;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @Description: lop Invoice
 * @author:truonglt2
 * @since:Feb 7, 2014 4:13:08 PM
 * @version: 1.0
 * @since: 1.0
 * <Invoice>
 * <Content Id=\"SigningData\">
 * <ArisingDate>19/05/2017 10:54:11</ArisingDate>
 * <InvoiceName>Hóa đơn giá trị gia tăng</InvoiceName>
 * <InvoicePattern>01GTKT0/001</InvoicePattern>
 * <SerialNo>AA/17E</SerialNo>
 * <InvoiceNo>5</InvoiceNo>
 * <Kind_of_Payment>TM</Kind_of_Payment>
 * <ComName>CÔNG TY TNHH THỬ NGHIỆM</ComName>
 * <ComTaxCode>8328897201</ComTaxCode>
 * <ComAddress>Số 70, Ngô Mây, Hòa Xuân, Đà Nẵng</ComAddress>
 * <ComPhone />
 * <ComBankNo />
 * <ComBankName />
 * <CusCode>TRUONGLT</CusCode>
 * <CusName>Lê Tấn Trương</CusName>
 * <CusTaxCode /><CusPhone />
 * <CusAddress>Đà Nẵng</CusAddress>
 * <CusBankName />
 * <CusBankNo />
 * <Total>10000000</Total>
 * <VAT_Amount>1000000</VAT_Amount>
 * <Amount>11000000</Amount>
 * <Amount_words>Mười một triệu đồng</Amount_words>
 * <Buyer />
 * <VAT_Rate>10</VAT_Rate>
 * <KindOfService />
 * <Products>
 * <Product>
 * <Code />
 * <Remark />
 * <Total>0</Total>
 * <ProdName>Đi chơi </ProdName>
 * <ProdUnit />
 * <ProdQuantity>0</ProdQuantity>
 * <ProdPrice>0</ProdPrice>
 * <Discount>0</Discount>
 * <DiscountAmount>0</DiscountAmount>
 * <VATRate>0</VATRate>
 * <VATAmount>0</VATAmount>
 * <Amount>10000000</Amount>
 * </Product>
 * </Products>
 * <Extra />
 * <SignDate>19/05/2017</SignDate>
 * </Content>
 * </Invoice>
 */
@SuppressWarnings("serial")
public class InvoiceHDDTDetails implements Serializable {
    private String ArisingDate;
    private String InvoiceName;
    private String InvoicePattern;
    private String SerialNo;
    private String InvoiceNo;
    private String Kind_of_Payment;
    private String ComName;
    private String ComTaxCode;
    private String ComAddress;
    private String ComPhone;
    private String ComBankNo;
    private String ComBankName;
    private String CusCode;
    private String CusName;
    private String CusPhone;
    private String CusAddress;
    private String CusTaxCode;
    private String CusBankName;
    private String CusBankNo;
    private String Amount_words;
    private String Buyer;
    private String KindOfService;
    private double Total;
    private double VAT_Amount;
    private double Amount;
    private double VAT_Rate;
    private double GrossValue;

    private String Extra1;
    private String Extra;
    private String SignDate;
    private String AmountInWords;
    private String CurrencyUnit;

    private ArrayList<ProductInvoiceDetails> arrayListProduct;
    //Phần mở rộng của khách sạn
    private String PaymentMethod;
    private String FolioNo;
    private String RoomNo;
    private String Arrival;
    private String Departure;
    private String QuestQuantity;
    private String Rate;
    private String EquivalentUSD;
    private double VatAmount30;
    private int PaymentStatus;
    private String Cashier;
    private String Publisher;
    private String CusCountry;
    private String ARNumber;
    private String CheckNo;
    private String TableNo;
    private boolean NoServiceCharge;
    private double ServicechargeAmount;

    public String getArisingDate() {
        return ArisingDate;
    }

    public void setArisingDate(String arisingDate) {
        ArisingDate = arisingDate;
    }

    public double getVAT_Amount() {
        return VAT_Amount;
    }

    public void setVAT_Amount(double VAT_Amount) {
        this.VAT_Amount = VAT_Amount;
    }

    public double getVAT_Rate() {
        return VAT_Rate;
    }

    public void setVAT_Rate(double VAT_Rate) {
        this.VAT_Rate = VAT_Rate;
    }

    public double getGrossValue() {
        return GrossValue;
    }

    public void setGrossValue(double grossValue) {
        GrossValue = grossValue;
    }

    public String getExtra1() {
        return Extra1;
    }

    public void setExtra1(String extra1) {
        Extra1 = extra1;
    }

    public String getCurrencyUnit() {
        return CurrencyUnit;
    }

    public void setCurrencyUnit(String currencyUnit) {
        CurrencyUnit = currencyUnit;
    }

    public String getExtra() {
        return Extra;
    }

    public void setExtra(String extra) {
        Extra = extra;
    }

    public ArrayList<ProductInvoiceDetails> getArrayListProduct() {
        return arrayListProduct;
    }

    public void setArrayListProduct(ArrayList<ProductInvoiceDetails> arrayListProduct) {
        this.arrayListProduct = arrayListProduct;
    }

    public String getPaymentMethod() {
        return PaymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        PaymentMethod = paymentMethod;
    }

    public String getFolioNo() {
        return FolioNo;
    }

    public void setFolioNo(String folioNo) {
        FolioNo = folioNo;
    }

    public String getRoomNo() {
        return RoomNo;
    }

    public void setRoomNo(String roomNo) {
        RoomNo = roomNo;
    }

    public String getArrival() {
        return Arrival;
    }

    public void setArrival(String arrival) {
        Arrival = arrival;
    }

    public String getDeparture() {
        return Departure;
    }

    public void setDeparture(String departure) {
        Departure = departure;
    }

    public String getQuestQuantity() {
        return QuestQuantity;
    }


    public String getRate() {
        return Rate;
    }

    public void setRate(String rate) {
        Rate = rate;
    }

    public String getEquivalentUSD() {
        return EquivalentUSD;
    }

    public void setEquivalentUSD(String equivalentUSD) {
        EquivalentUSD = equivalentUSD;
    }

    public double getVatAmount30() {
        return VatAmount30;
    }

    public void setVatAmount30(double vatAmount30) {
        VatAmount30 = vatAmount30;
    }

    public int getPaymentStatus() {
        return PaymentStatus;
    }

    public void setPaymentStatus(int paymentStatus) {
        PaymentStatus = paymentStatus;
    }

    public String getCashier() {
        return Cashier;
    }

    public void setCashier(String cashier) {
        Cashier = cashier;
    }

    public String getPublisher() {
        return Publisher;
    }

    public void setPublisher(String publisher) {
        Publisher = publisher;
    }

    public String getCusCountry() {
        return CusCountry;
    }

    public void setCusCountry(String cusCountry) {
        CusCountry = cusCountry;
    }

    public String getARNumber() {
        return ARNumber;
    }

    public void setARNumber(String ARNumber) {
        this.ARNumber = ARNumber;
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

    public boolean isNoServiceCharge() {
        return NoServiceCharge;
    }

    public void setNoServiceCharge(boolean noServiceCharge) {
        NoServiceCharge = noServiceCharge;
    }

    public double getServicechargeAmount() {
        return ServicechargeAmount;
    }

    public void setServicechargeAmount(double servicechargeAmount) {
        ServicechargeAmount = servicechargeAmount;
    }

    public double getServicechargeRate() {
        return ServicechargeRate;
    }

    public void setServicechargeRate(double servicechargeRate) {
        ServicechargeRate = servicechargeRate;
    }

    public void setQuestQuantity(String questQuantity) {
        QuestQuantity = questQuantity;
    }

    private double ServicechargeRate;


    //phần getset
    public String getAmountInWords() {
        return AmountInWords;
    }

    public void setAmountInWords(String amountInWords) {
        AmountInWords = amountInWords;
    }


    public String getCusPhone() {
        return CusPhone;
    }

    public void setCusPhone(String cusPhone) {
        CusPhone = cusPhone;
    }

    public String getCusAddress() {
        return CusAddress;
    }

    public void setCusAddress(String cusAddress) {
        CusAddress = cusAddress;
    }

    public String getCusTaxCode() {
        return CusTaxCode;
    }

    public void setCusTaxCode(String cusTaxCode) {
        CusTaxCode = cusTaxCode;
    }

//    public String getArisingDate() {
//        return ArisingDate;
//    }
//
//    public void setArisingDate(String arisingDate) {
//        ArisingDate = arisingDate;
//    }

    public String getInvoiceName() {
        return InvoiceName;
    }

    public void setInvoiceName(String invoiceName) {
        InvoiceName = invoiceName;
    }

    public String getInvoicePattern() {
        return InvoicePattern;
    }

    public void setInvoicePattern(String invoicePattern) {
        InvoicePattern = invoicePattern;
    }

    public String getSerialNo() {
        return SerialNo;
    }

    public void setSerialNo(String serialNo) {
        SerialNo = serialNo;
    }

    public String getInvoiceNo() {
        return InvoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        InvoiceNo = invoiceNo;
    }

    public String getKind_of_Payment() {
        return Kind_of_Payment;
    }

    public void setKind_of_Payment(String kind_of_Payment) {
        Kind_of_Payment = kind_of_Payment;
    }

    public String getComName() {
        return ComName;
    }

    public void setComName(String comName) {
        ComName = comName;
    }

    public String getComTaxCode() {
        return ComTaxCode;
    }

    public void setComTaxCode(String comTaxCode) {
        ComTaxCode = comTaxCode;
    }

    public String getComAddress() {
        return ComAddress;
    }

    public void setComAddress(String comAddress) {
        ComAddress = comAddress;
    }

    public String getComPhone() {
        return ComPhone;
    }

    public void setComPhone(String comPhone) {
        ComPhone = comPhone;
    }

    public String getComBankNo() {
        return ComBankNo;
    }

    public void setComBankNo(String comBankNo) {
        ComBankNo = comBankNo;
    }

    public String getComBankName() {
        return ComBankName;
    }

    public void setComBankName(String comBankName) {
        ComBankName = comBankName;
    }

    public String getCusCode() {
        return CusCode;
    }

    public void setCusCode(String cusCode) {
        CusCode = cusCode;
    }

    public String getCusName() {
        return CusName;
    }

    public void setCusName(String cusName) {
        CusName = cusName;
    }

    public String getCusBankName() {
        return CusBankName;
    }

    public void setCusBankName(String cusBankName) {
        CusBankName = cusBankName;
    }

    public String getCusBankNo() {
        return CusBankNo;
    }

    public void setCusBankNo(String cusBankNo) {
        CusBankNo = cusBankNo;
    }

    public String getAmount_words() {
        return Amount_words;
    }

    public void setAmount_words(String amount_words) {
        Amount_words = amount_words;
    }

    public String getBuyer() {
        return Buyer;
    }

    public void setBuyer(String buyer) {
        Buyer = buyer;
    }

    public String getKindOfService() {
        return KindOfService;
    }

    public void setKindOfService(String kindOfService) {
        KindOfService = kindOfService;
    }

    public double getTotal() {
        return Total;
    }

    public void setTotal(double total) {
        Total = total;
    }


    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }


    public String getSignDate() {
        return SignDate;
    }

    public void setSignDate(String signDate) {
        SignDate = signDate;
    }


}