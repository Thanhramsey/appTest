package com.vnpt.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.vnpt.dto.HoaDon;
import com.vnpt.utils.Helper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class TbInvoice {
    /*CREATE TABLE "Invoice" ("IdInvoice" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL
     , "Number" TEXT, "Datetime" TEXT, "IdCustomer" INTEGER, "Price" TEXT, "Status" TEXT,
      "DueDate" TEXT)*/
    public final static String INVOICE_TABLE = "Invoice";

    public final static String INVOICE_IdInvoice = "IdInvoice";
    public final static String INVOICE_Number = "Number";
    public final static String INVOICE_Datetime = "Datetime";
    public final static String INVOICE_IdCustomer = "IdCustomer";
    public final static String INVOICE_Price = "Price";
    public final static String INVOICE_Status = "Status";
    public final static String INVOICE_DueDate = "DueDate";
    public final static String INVOICE_CertifiedCustomer = "CertifiedCustomer";
    public final static String INVOICE_PathCertificate = "PathCertificed";

    protected static TbInvoice instance;

    public TbInvoice(Context context) {
        mcontext = context;
    }

    static Context mcontext;

    public static TbInvoice getInstance(Context context) {

        if (instance == null) {
            instance = new TbInvoice(context);
        }
        return instance;
    }

    /*
    *  private int idInvoice;
    private int idCustomer;
    private String Name;
    private String Phone;
    private String Address;
    private double Address;
    private String Longitude;
    private String Latitude;
    private String CMND;
    private String Email;

    private String NumberInvoice;
    private String NgayThu;
    private String Price;
    private String Status;
    private String DueDate;
    * */
    public ArrayList<HoaDon> getAllCustomerQuery(String query) {
        Cursor cursor = DatabaseHelp.getInstance(mcontext).selectQuery(query);
        ArrayList<HoaDon> arrHoaDon = new ArrayList<HoaDon>();
        if (cursor != null) {
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                HoaDon item = new HoaDon();
                int idInvoice = cursor.getInt(0);
                String numberInvoice= cursor.getString(1);
                String ngayThu= cursor.getString(2);
                String price= cursor.getString(4);
                String status= cursor.getString(5);
                String dueDate= cursor.getString(6);
                String pathCer= cursor.getString(7);
                String certifiedCustomer = cursor.getString(8);

                String tax = cursor.getString(9);
                String serial = cursor.getString(10);
                String stateDebit = cursor.getString(11);

                int idCustomer = cursor.getInt(12);
                String nameCus = cursor.getString(13);
                String phone= cursor.getString(14);
                String address= cursor.getString(15);
                double longitude= cursor.getDouble(16);
                double latitude= cursor.getDouble(17);
                String CMND = cursor.getString(18);
                String email = cursor.getString(19);
                if(CMND == null || CMND.equals(""))
                {
                    CMND = "";
                }
                if(email == null || email.equals(""))
                {
                    email = "";
                }

                String makh = cursor.getString(20);
                String codeTax = cursor.getString(21);
                String smdd = cursor.getString(22);
                String nameCusUnsigned = cursor.getString(23);

                item.setIdInvoice(idInvoice);
                item.setIdCustomer(idCustomer);
                item.setNameCus(nameCus);
                item.setPhone(phone);
                item.setAddress(address);
                item.setLongitude(longitude);
                item.setLatitude(latitude);

                item.setCMND(CMND);
                item.setEmail(email);
                item.setNumberInvoice(numberInvoice);
                item.setNgayThu(ngayThu);

                item.setPrice(price);
                item.setStatus(status);
                item.setDueDate(dueDate);
                item.setCertifiedCustomer(""+certifiedCustomer);
                item.setPathCertified(pathCer);
                item.setStateDebit(stateDebit);
                item.setTax(tax);
                item.setCodeTaxCustomer(codeTax);
                item.setCodeCustomer(makh);
                item.setSmRepresent(smdd);
                item.setUnsignedNameCus(nameCusUnsigned);
                arrHoaDon.add(item);
                cursor.moveToNext();
            }
            cursor.close();
        } else {
            return null;
        }
        return arrHoaDon;
    }
    public long updateStateCerfiticateInvoice(int idHoaDon, String strType, int status, String pathCerfiticate)
    {
        ContentValues values = new ContentValues();
        values.put(INVOICE_CertifiedCustomer, strType);
        values.put(INVOICE_Status, status);
        values.put(INVOICE_PathCertificate, pathCerfiticate);
        String strQuery = INVOICE_IdInvoice +"="+idHoaDon;
        if ((DatabaseHelp.getInstance(mcontext).UPDATE_TABLE_NEW(INVOICE_TABLE,values,strQuery)) == 1) {
            return 1;//ok
        } else {
            return 0;//false
        }
    }
    public long updateStatusPaymentInvoice(int idHoaDon, int status)
    {
        ContentValues values = new ContentValues();
        values.put(INVOICE_Status, status);
        String strQuery = INVOICE_IdInvoice +"="+idHoaDon;
        if ((DatabaseHelp.getInstance(mcontext).UPDATE_TABLE_NEW(INVOICE_TABLE,values,strQuery)) == 1) {
            return 1;//ok
        } else {
            return 0;//false
        }
    }

    public String getCrurentDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }
}
