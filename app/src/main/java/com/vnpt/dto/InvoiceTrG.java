package com.vnpt.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: lop Invoice
 * @author:truonglt2
 * @since:Feb 7, 2014 4:13:08 PM
 * @version: 1.0
 * @since: 1.0
 */
@SuppressWarnings("serial")
/**
 *
 * ROWNUM
 integer
 None.

 TotalRow
 integer
 None.

 Id
 integer
 None.

 Pattern
 string
 None.

 Serial
 string
 None.

 InvNo
 integer
 None.

 InvToken
 string
 None.

 KindOfService
 date
 Data type: Date

 IsWatch
 boolean
 None.

 Status
 integer
 None.

 PaymentStatus
 integer
 None.

 KindOfInv
 integer
 None.

 CusCode
 string
 None.

 CusName
 string
 None.

 CusTaxCode
 string
 None.

 PaymentCode
 string
 None.

 Amount
 decimal number
 None.

 XML
 string
 None.

 TableNo
 string
 None.

 CheckNo
 *
 */

public class InvoiceTrG implements Serializable {
    public int ROWNUM;
    public int TotalRow;
    public int Id;
    public String Pattern;
    public String Serial;
    public String InvNo;
    public String InvToken;
    public Date KindOfService;
    public Date str_KindOfService;

    public boolean IsWatch;
    public int Status;
    public int PaymentStatus;
    public int KindOfInv;
    public String CusCode;
    public String CusName;
    public String CusTaxCode;
    public String PaymentCode;
    public int StatusCusSigned;
    public String PathImgCusSigned;
    public String PathImgSignedServer;
    public String PathImgSignedMobile;



    public int StatusPaymentedMobile;
    public double Amount;
    public String XML;
    public String TableNo;
    public String CheckNo;
    public InvoiceHDDTDetails invoiceXml;
    public InvoiceHDDTDetails getInvoiceXml() {
        return invoiceXml;
    }
    public int getStatusCusSigned() {
        return StatusCusSigned;
    }
    public String getPathImgCusSigned() {
        return PathImgCusSigned;
    }

    public void setPathImgCusSigned(String pathImgCusSigned) {
        PathImgCusSigned = pathImgCusSigned;
    }
    public void setStatusCusSigned(int statusCusSigned) {
        StatusCusSigned = statusCusSigned;
    }
    public void setInvoiceXml(InvoiceHDDTDetails invoiceXml) {
        this.invoiceXml = invoiceXml;
    }


    public int getROWNUM() {
        return ROWNUM;
    }

    public void setROWNUM(int ROWNUM) {
        this.ROWNUM = ROWNUM;
    }

    public int getTotalRow() {
        return TotalRow;
    }

    public void setTotalRow(int totalRow) {
        TotalRow = totalRow;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
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

    public String getInvNo() {
        return InvNo;
    }

    public void setInvNo(String invNo) {
        InvNo = invNo;
    }

    public String getInvToken() {
        return InvToken;
    }

    public void setInvToken(String invToken) {
        InvToken = invToken;
    }

    public Date getKindOfService() {
        return KindOfService;
    }

    public void setKindOfService(Date kindOfService) {
        KindOfService = kindOfService;
    }

    public Date getStr_KindOfService() {
        return str_KindOfService;
    }

    public void setStr_KindOfService(Date str_KindOfService) {
        this.str_KindOfService = str_KindOfService;
    }

    public boolean isWatch() {
        return IsWatch;
    }

    public void setWatch(boolean watch) {
        IsWatch = watch;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public int getPaymentStatus() {
        return PaymentStatus;
    }

    public void setPaymentStatus(int paymentStatus) {
        PaymentStatus = paymentStatus;
    }

    public int getKindOfInv() {
        return KindOfInv;
    }

    public void setKindOfInv(int kindOfInv) {
        KindOfInv = kindOfInv;
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

    public String getCusTaxCode() {
        return CusTaxCode;
    }

    public void setCusTaxCode(String cusTaxCode) {
        CusTaxCode = cusTaxCode;
    }

    public String getPaymentCode() {
        return PaymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        PaymentCode = paymentCode;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public String getXML() {
        return XML;
    }

    public void setXML(String XML) {
        this.XML = XML;
    }

    public String getTableNo() {
        return TableNo;
    }

    public void setTableNo(String tableNo) {
        TableNo = tableNo;
    }

    public String getCheckNo() {
        return CheckNo;
    }

    public void setCheckNo(String checkNo) {
        CheckNo = checkNo;
    }

    @Override
    public String toString() {
        return "InvoiceTrG{" +
                "ROWNUM=" + ROWNUM +
                ", TotalRow=" + TotalRow +
                ", Id=" + Id +
                ", Pattern='" + Pattern + '\'' +
                ", Serial='" + Serial + '\'' +
                ", InvNo='" + InvNo + '\'' +
                ", InvToken='" + InvToken + '\'' +
                ", KindOfService=" + KindOfService +
                ", str_KindOfService=" + str_KindOfService +
                ", IsWatch=" + IsWatch +
                ", Status=" + Status +
                ", PaymentStatus=" + PaymentStatus +
                ", KindOfInv=" + KindOfInv +
                ", CusCode='" + CusCode + '\'' +
                ", CusName='" + CusName + '\'' +
                ", CusTaxCode='" + CusTaxCode + '\'' +
                ", PaymentCode='" + PaymentCode + '\'' +
                ", Amount=" + Amount +
                ", XML='" + XML + '\'' +
                ", TableNo='" + TableNo + '\'' +
                ", invoiceXml=" + invoiceXml +
                ", CheckNo='" + CheckNo + '\'' +
                '}';
    }
    public String getPathImgSignedServer() {
        return PathImgSignedServer;
    }

    public void setPathImgSignedServer(String pathImgSignedServer) {
        PathImgSignedServer = pathImgSignedServer;
    }

    public String getPathImgSignedMobile() {
        return PathImgSignedMobile;
    }

    public void setPathImgSignedMobile(String pathImgSignedMobile) {
        PathImgSignedMobile = pathImgSignedMobile;
    }

    public int getStatusPaymentedMobile() {
        return StatusPaymentedMobile;
    }

    public void setStatusPaymentedMobile(int statusPaymentedMobile) {
        StatusPaymentedMobile = statusPaymentedMobile;
    }

}