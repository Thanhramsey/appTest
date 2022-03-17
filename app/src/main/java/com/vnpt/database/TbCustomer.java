package com.vnpt.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TbCustomer {
    /*CREATE TABLE "Customer" ("id" INTEGER PRIMARY KEY  NOT NULL ,
    "Name" TEXT DEFAULT (null) ,"Phone" TEXT DEFAULT (null) ,
    "Address" TEXT DEFAULT (null) ,"Longitude" DOUBLE DEFAULT (null) ,
    "Latitude" DOUBLE DEFAULT (null) , "CMND" TEXT, "Email" TEXT)*/
    public final static String CUSTOMER_TABLE = "Customer";

    public final static String CUSTOMER_IdUser = "id";
    public final static String CUSTOMER_Name = "Name";
    public final static String CUSTOMER_Phone = "Phone";
    public final static String CUSTOMER_Address = "Address";
    public final static String CUSTOMER_Longitude = "Longitude";
    public final static String CUSTOMER_Latitude = "Latitude";
    public final static String CUSTOMER_CMND = "CMND";
    public final static String CUSTOMER_Email = "Email";
    public final static String CUSTOMER_NameCus = "name_cus";
    protected static TbCustomer instance;

    public TbCustomer(Context context) {
        mcontext = context;
    }

    static Context mcontext;

    public static TbCustomer getInstance(Context context) {

        if (instance == null) {
            instance = new TbCustomer(context);
        }
        return instance;
    }

    public Cursor getallCustomeQuery(String query) {
        Cursor cursor = DatabaseHelp.getInstance(mcontext).selectQuery(query);
        if (cursor != null)
            cursor.moveToFirst();
        return cursor;
    }

    public String getCrurentDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }
}
