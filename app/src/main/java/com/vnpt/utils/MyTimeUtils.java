package com.vnpt.utils;

import android.text.TextUtils;

import com.vnpt.common.ConstantsApp;
import com.vnpt.models.ThoiGianValue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * Created by Trần Vũ Hiếu on 3/5/2017.
 */

public class MyTimeUtils {
    public static final String FULL_DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String MONTH_FORMAT = "MM/yyyy";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String TIME_SHORT_FORMAT = "HH:mm";
    public static final String YEAR_FORMAT = "yyyy";
    public static final String BIRTHDAY_FORMAT = "MMMdd yyyy";


    public static String formatDate(long date_millis, String format) {
        if (!isCurrentYear(date_millis)) {
            format = "yyyy " + format;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date_millis);
    }

    public static String formatDate(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    public static Date formatDate(String dateStr, String format) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.parse(dateStr);
    }

    private static boolean isCurrentYear(long date_millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date_millis);
        Calendar current = Calendar.getInstance();
        return calendar.get(Calendar.YEAR) == current.get(Calendar.YEAR);
    }

    public interface AlertDialogCallback {
        void alertDialogCallback(ThoiGianValue value);
    }

    public static String get_time(String p_time, int kieu_time, final AlertDialogCallback callback) {
        // kiểu thời gian
        String tu_ngay = "";
        String den_ngay = "";
        Calendar c = Calendar.getInstance();
        Date d = c.getTime();
        // 0 : ngày(DD/MM/YYYY); 1 : tuần (YYYY-TT) ; 2 : tháng (MM-YYYY); 3 : Quý ; 4 : Năm (YYYY); 5 : Từ ngày đến ngày (DD/MM/YYYY|DD/MM/YYYY)
        String s = "";
        switch (kieu_time) {
            case ConstantsApp.TimeType.DATE:
                tu_ngay = p_time;
                den_ngay = p_time;
                break;
            case ConstantsApp.TimeType.WEEK:
                try {
                    ArrayList<String> arrChil = new ArrayList<>(Arrays.asList(p_time.split("-")));
                    String nam = TextUtils.isEmpty(arrChil.get(0).trim()) ? "0" : arrChil.get(0).trim();
                    String tuan = TextUtils.isEmpty(arrChil.get(1).trim()) ? "0" : arrChil.get(1).trim();
                    int year = Integer.parseInt(nam);
                    int week = Integer.parseInt(tuan);

                    Calendar cld = Calendar.getInstance();

                    cld.setMinimalDaysInFirstWeek(4);
                    cld.set(Calendar.YEAR, year);
                    cld.set(Calendar.WEEK_OF_YEAR, week);
                    while (cld.get(Calendar.DAY_OF_WEEK) > cld.getFirstDayOfWeek() + 1) {
                        cld.add(Calendar.DATE, -1); // Substract 1 day until first day of week.
                    }
                    Date result = cld.getTime();
                    SimpleDateFormat dfs = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    String FromDates = dfs.format(result);

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dfs.parse(FromDates)); // parsed date and setting to calendar
                    calendar.add(Calendar.DATE, 6);
                    Date resultTo = calendar.getTime();
                    String ToDates = dfs.format(resultTo);
                    tu_ngay = FromDates;
                    den_ngay = ToDates;

                } catch (Exception e) {
                    tu_ngay = "";
                    den_ngay = "";
                }
                break;
            case ConstantsApp.TimeType.MONTH:
                try {
                    ArrayList<String> arrChil = new ArrayList<>(Arrays.asList(p_time.split("/")));
                    String thang = TextUtils.isEmpty(arrChil.get(0).trim()) ? "0" : arrChil.get(0).trim();
                    String nam = TextUtils.isEmpty(arrChil.get(1).trim()) ? "0" : arrChil.get(1).trim();
                    int year = Integer.parseInt(nam);
                    int month = Integer.parseInt(thang);
                    tu_ngay = "01/" + thang + "/" + nam;
                    SimpleDateFormat dfs = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

                    Calendar calen = Calendar.getInstance();
                    calen.set(Calendar.YEAR, year);
                    calen.set(Calendar.MONTH, month);
                    calen.set(Calendar.DATE, 1);
                    calen.add(Calendar.DATE, -1);
                    Date resultTo = calen.getTime();
                    String ToDates = dfs.format(resultTo);
                    den_ngay = ToDates;
                } catch (Exception e) {

                }
                break;
            case ConstantsApp.TimeType.QUARTER:
                try {
                    ArrayList<String> arrChil = new ArrayList<>(Arrays.asList(p_time.split("-")));
                    String nam = TextUtils.isEmpty(arrChil.get(0).trim()) ? "0" : arrChil.get(0).trim();
                    String quy = TextUtils.isEmpty(arrChil.get(1).trim()) ? "0" : arrChil.get(1).trim();
                    int year = Integer.parseInt(nam);
                    int quarter = Integer.parseInt(quy);
                    int month = 0;
                    if (quarter == 1) month = 0;
                    else if (quarter == 2)
                        month = 3;
                    else if (quarter == 3)
                        month = 6;
                    else if (quarter == 4)
                        month = 9;
                    else
                        month = 0;

                    Calendar cld = Calendar.getInstance();
                    cld.set(Calendar.YEAR, year);
                    cld.set(Calendar.MONTH, month);
                    cld.set(Calendar.DATE, 1);
                    Date result = cld.getTime();
                    SimpleDateFormat dfs = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    String FromDates = dfs.format(result);

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month + 3);
                    calendar.set(Calendar.DATE, 1);
                    calendar.add(Calendar.DATE, -1);
                    Date resultTo = calendar.getTime();
                    String ToDates = dfs.format(resultTo);
                    tu_ngay = FromDates;
                    den_ngay = ToDates;
                } catch (Exception e) {
                    tu_ngay = "";
                    den_ngay = "";
                }
                break;
            case ConstantsApp.TimeType.YEAR:
                tu_ngay = "01/01/" + p_time;
                den_ngay = "31/12/" + p_time;
                break;
            case ConstantsApp.TimeType.DATE_TO_DATE:
                try {
                    ArrayList<String> arrChil = new ArrayList<>(Arrays.asList(p_time.split((char) 12 + "")));
                    String tungay = TextUtils.isEmpty(arrChil.get(0).trim()) ? "0" : arrChil.get(0).trim();
                    String denngay = TextUtils.isEmpty(arrChil.get(1).trim()) ? "0" : arrChil.get(1).trim();
                    denngay = denngay.substring(1);
                    tu_ngay = tungay;
                    den_ngay = denngay;
                } catch (Exception e) {
                    tu_ngay = "";
                    den_ngay = "";
                }
                break;
        }
        s = tu_ngay + " - " + den_ngay;
        ThoiGianValue thoiGianValue = new ThoiGianValue();
        thoiGianValue.setTU_NGAY(tu_ngay);
        thoiGianValue.setDEN_NGAY(den_ngay);
        callback.alertDialogCallback(thoiGianValue);
        return s;
    }

    public static String get_time_touch(ThoiGianValue value, int kieu_time, int touch, final AlertDialogCallback callback) {
        // kiểu thời gian
        // 1: next, 0 : back
        String tu_ngay = value.getTU_NGAY();
        String den_ngay = value.getDEN_NGAY();
        Calendar c = Calendar.getInstance();
        Date d = c.getTime();
        // 0 : ngày(DD/MM/YYYY); 1 : tuần (YYYY-TT) ; 2 : tháng (MM-YYYY); 3 : Quý ; 4 : Năm (YYYY); 5 : Từ ngày đến ngày (DD/MM/YYYY|DD/MM/YYYY)
        String s = "";
        switch (kieu_time) {
            case ConstantsApp.TimeType.DATE:
                try {
                    String FromDate = value.getTU_NGAY();
                    Date result = new SimpleDateFormat("dd/MM/yyyy").parse(tu_ngay);
                    SimpleDateFormat dfs = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    FromDate = dfs.format(result);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dfs.parse(FromDate)); // parsed date and setting to calendar
                    if (touch == 0)
                        calendar.add(Calendar.DATE, -1);
                    else
                        calendar.add(Calendar.DATE, 1);
                    Date resultTo = calendar.getTime();
                    tu_ngay = dfs.format(resultTo);
                    den_ngay = dfs.format(resultTo);

                } catch (Exception e) {
                }
                break;
            case ConstantsApp.TimeType.WEEK:
                try {
                    String FromDate = value.getTU_NGAY();
                    String ToDate = value.getDEN_NGAY();
                    Date result = new SimpleDateFormat("dd/MM/yyyy").parse(FromDate);
                    Date resultT = new SimpleDateFormat("dd/MM/yyyy").parse(ToDate);
                    SimpleDateFormat dfs = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    FromDate = dfs.format(result);
                    ToDate = dfs.format(resultT);
                    // calendar.set(Calendar.WEEK_OF_YEAR, 7);
                    if (touch == 0) // back
                    {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(dfs.parse(FromDate));
                        calendar.add(Calendar.DATE, -7);
                        Date resultTo = calendar.getTime();

                        Calendar calen = Calendar.getInstance();
                        calen.setTime(dfs.parse(ToDate));
                        calen.add(Calendar.DATE, -7);
                        Date resultFrom = calen.getTime();

                        tu_ngay = dfs.format(resultTo);
                        den_ngay = dfs.format(resultFrom);
                    } else // next
                    {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(dfs.parse(ToDate));
                        calendar.add(Calendar.DATE, 7);
                        Date resultTo = calendar.getTime();

                        Calendar calen = Calendar.getInstance();
                        calen.setTime(dfs.parse(FromDate));
                        calen.add(Calendar.DATE, 7);
                        Date resultFrom = calen.getTime();

                        tu_ngay = dfs.format(resultFrom);
                        den_ngay = dfs.format(resultTo);
                    }
                } catch (Exception e) {
                    tu_ngay = "";
                    den_ngay = "";
                }
                break;
            case ConstantsApp.TimeType.MONTH:
                try {
                    SimpleDateFormat dfs = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    String monthValue = value.getDEN_NGAY();
                    ArrayList<String> arrChil = new ArrayList<>(Arrays.asList(monthValue.split("/")));
                    String thang = TextUtils.isEmpty(arrChil.get(1).trim()) ? "0" : arrChil.get(1).trim();
                    String nam = TextUtils.isEmpty(arrChil.get(2).trim()) ? "0" : arrChil.get(2).trim();
                    int year = Integer.parseInt(nam);
                    int month = Integer.parseInt(thang);
                    if (touch == 0) {
                        Calendar calen = Calendar.getInstance();
                        calen.set(Calendar.YEAR, year);
                        calen.set(Calendar.MONTH, month - 1);
                        calen.add(Calendar.MONTH, -1);
                        calen.set(Calendar.DATE, 1);
                        Date resultFrom = calen.getTime();
                        String FromDates = dfs.format(resultFrom);
                        tu_ngay = FromDates;

                        Calendar calenFom = Calendar.getInstance();
                        calenFom.set(Calendar.YEAR, year);
                        calenFom.set(Calendar.MONTH, month - 1);
                        calenFom.set(Calendar.DATE, 1);
                        calenFom.add(Calendar.DATE, -1);
                        Date resultTo = calenFom.getTime();
                        String ToDates = dfs.format(resultTo);
                        den_ngay = ToDates;
                    } else {
                        Calendar calen = Calendar.getInstance();
                        calen.set(Calendar.YEAR, year);
                        calen.set(Calendar.MONTH, month - 1);
                        calen.add(Calendar.MONTH, 1);
                        calen.set(Calendar.DATE, 1);
                        Date resultFrom = calen.getTime();
                        String FromDates = dfs.format(resultFrom);
                        tu_ngay = FromDates;

                        Calendar calenFom = Calendar.getInstance();
                        calenFom.set(Calendar.YEAR, year);
                        calenFom.set(Calendar.MONTH, month + 1);
                        calenFom.set(Calendar.DATE, 1);
                        calenFom.add(Calendar.DATE, -1);
                        Date resultTo = calenFom.getTime();
                        String ToDates = dfs.format(resultTo);
                        den_ngay = ToDates;
                    }
                } catch (Exception e) {

                }
                break;
            case ConstantsApp.TimeType.QUARTER:
                try {
                    if (touch == 0) {
                        SimpleDateFormat dfs = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        String monthValue = value.getTU_NGAY();
                        ArrayList<String> arrChil = new ArrayList<>(Arrays.asList(monthValue.split("/")));
                        String thang = TextUtils.isEmpty(arrChil.get(1).trim()) ? "0" : arrChil.get(1).trim();
                        String nam = TextUtils.isEmpty(arrChil.get(2).trim()) ? "0" : arrChil.get(2).trim();
                        int year = Integer.parseInt(nam);
                        int month = Integer.parseInt(thang) - 1;

                        Calendar calen = Calendar.getInstance();
                        calen.set(Calendar.YEAR, year);
                        calen.set(Calendar.MONTH, month);
                        calen.add(Calendar.MONTH, -3);
                        calen.set(Calendar.DATE, 1);
                        Date resultFrom = calen.getTime();
                        String FromDates = dfs.format(resultFrom);
                        tu_ngay = FromDates;

                        Calendar calenFom = Calendar.getInstance();
                        calenFom.set(Calendar.YEAR, year);
                        calenFom.set(Calendar.MONTH, month);
                        calenFom.set(Calendar.DATE, 1);
                        calenFom.add(Calendar.DATE, -1);
                        Date resultTo = calenFom.getTime();
                        String ToDates = dfs.format(resultTo);
                        den_ngay = ToDates;
                    } else {
                        SimpleDateFormat dfs = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        String monthValue = value.getDEN_NGAY();
                        ArrayList<String> arrChil = new ArrayList<>(Arrays.asList(monthValue.split("/")));
                        String thang = TextUtils.isEmpty(arrChil.get(1).trim()) ? "0" : arrChil.get(1).trim();
                        String nam = TextUtils.isEmpty(arrChil.get(2).trim()) ? "0" : arrChil.get(2).trim();
                        int year = Integer.parseInt(nam);
                        int month = Integer.parseInt(thang);

                        Calendar calen = Calendar.getInstance();
                        calen.set(Calendar.YEAR, year);
                        calen.set(Calendar.MONTH, month);
                        calen.add(Calendar.MONTH, 3);
                        calen.set(Calendar.DATE, 1);
                        calen.add(Calendar.DATE, -1);
                        Date resultFrom = calen.getTime();
                        String FromDates = dfs.format(resultFrom);
                        den_ngay = FromDates;

                        Calendar calenFom = Calendar.getInstance();
                        calenFom.set(Calendar.YEAR, year);
                        calenFom.set(Calendar.MONTH, month);
                        calenFom.set(Calendar.DATE, 1);
                        Date resultTo = calenFom.getTime();
                        String ToDates = dfs.format(resultTo);
                        tu_ngay = ToDates;
                    }
                } catch (Exception e) {
                    tu_ngay = "";
                    den_ngay = "";
                }
                break;
            case ConstantsApp.TimeType.YEAR:
                try {
                    SimpleDateFormat dfs = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    String monthValue = value.getDEN_NGAY();
                    ArrayList<String> arrChil = new ArrayList<>(Arrays.asList(monthValue.split("/")));
                    String nam = TextUtils.isEmpty(arrChil.get(2).trim()) ? "0" : arrChil.get(2).trim();
                    int year = Integer.parseInt(nam);
                    if (touch == 0) {
                        year = year - 1;
                    } else {
                        year = year + 1;
                    }
                    tu_ngay = "01/01/" + year;
                    den_ngay = "31/12/" + year;
                } catch (Exception e) {
                }

                break;
            case ConstantsApp.TimeType.DATE_TO_DATE:
                try {
                    if (touch == 0) {
                        SimpleDateFormat dfs = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        String monthValue = value.getTU_NGAY();
                        ArrayList<String> arrChil = new ArrayList<>(Arrays.asList(monthValue.split("/")));
                        String ngay = TextUtils.isEmpty(arrChil.get(0).trim()) ? "0" : arrChil.get(0).trim();
                        String thang = TextUtils.isEmpty(arrChil.get(1).trim()) ? "0" : arrChil.get(1).trim();
                        String nam = TextUtils.isEmpty(arrChil.get(2).trim()) ? "0" : arrChil.get(2).trim();
                        int year = Integer.parseInt(nam);
                        int month = Integer.parseInt(thang) - 1;
                        int day = Integer.parseInt(ngay);

                        Calendar calen = Calendar.getInstance();
                        calen.set(Calendar.YEAR, year);
                        calen.set(Calendar.MONTH, month);
                        calen.set(Calendar.DATE, day);
                        calen.add(Calendar.DATE, -1);
                        Date resultFrom = calen.getTime();
                        String FromDates = dfs.format(resultFrom);
                        tu_ngay = FromDates;
                    } else {
                        SimpleDateFormat dfs = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        String monthValue = value.getDEN_NGAY();
                        ArrayList<String> arrChil = new ArrayList<>(Arrays.asList(monthValue.split("/")));
                        String ngay = TextUtils.isEmpty(arrChil.get(0).trim()) ? "0" : arrChil.get(0).trim();
                        String thang = TextUtils.isEmpty(arrChil.get(1).trim()) ? "0" : arrChil.get(1).trim();
                        String nam = TextUtils.isEmpty(arrChil.get(2).trim()) ? "0" : arrChil.get(2).trim();
                        int year = Integer.parseInt(nam);
                        int month = Integer.parseInt(thang) - 1;
                        int day = Integer.parseInt(ngay);

                        Calendar calen = Calendar.getInstance();
                        calen.set(Calendar.YEAR, year);
                        calen.set(Calendar.MONTH, month);
                        calen.set(Calendar.DATE, day);
                        calen.add(Calendar.DATE, 1);
                        Date resultFrom = calen.getTime();
                        String FromDates = dfs.format(resultFrom);
                        den_ngay = FromDates;
                    }
                } catch (Exception e) {
                    tu_ngay = "";
                    den_ngay = "";
                }
                break;
        }
        s = tu_ngay + " - " + den_ngay;
        ThoiGianValue thoiGianValue = new ThoiGianValue();
        thoiGianValue.setTU_NGAY(tu_ngay);
        thoiGianValue.setDEN_NGAY(den_ngay);
        callback.alertDialogCallback(thoiGianValue);
        return s;
    }

}
