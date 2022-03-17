package com.vnpt.utils;

import com.vnpt.common.Common;
import com.vnpt.common.ConstantsApp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @Description: cac ham xu ly ve datetime
 * @author:truonglt2
 * @since:Feb 7, 2014 5:25:05 PM
 * @version: 1.0
 * @since: 1.0
 */
public class DateTimeUtil {

    private Calendar calendar;
    private int month;
    private int year;
    public DateTimeUtil() {
        // TODO Auto-generated constructor stub
        init();
    }

    private void init() {
        // TODO Auto-generated method stub
        calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);

    }
    public String minDateOfMonth()
    {
        calendar = Calendar.getInstance();
        return format(calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
    }

    public String maxDateOfMonth()
    {
        return format(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
    }
    private String format(int day) {

        SimpleDateFormat format = new SimpleDateFormat(Common.DATETIME_FORMAT_PATTERN_1);
        return format.format(date(gc(day)));
    }
    private GregorianCalendar gc(int day)
    {
        return new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), day);
    }
    private Date date(GregorianCalendar c)
    {
        return new java.util.Date(c.getTime().getTime());

    }
    /**
     * This method will return the current date time follow format: dd/mm/yyyy hh/mm/ss
     *
     * @return Return date time follow this format dd/mm/yyyy hh/mm/ss
     *
     * @author Truonglt2
     * @throws:
     */
    public static String getCurrentDateTime() {
        Calendar cal = Calendar.getInstance();

        // get current day in month
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        // get current month
        int month = cal.get(Calendar.MONTH) + 1;
        // get current year
        int year = cal.get(Calendar.YEAR);

        // get current hour 24h for a day
        int hour24 = cal.get(Calendar.HOUR);
        // get current minute
        int minute = cal.get(Calendar.MINUTE);
        // get current second
        int second = cal.get(Calendar.SECOND);

        String AM_PM;
        if (Calendar.AM == cal.get(Calendar.AM_PM)) {
            AM_PM = " AM";
        } else {
            AM_PM = " PM";
        }

        StringBuffer dateTimeString = new StringBuffer();
        dateTimeString.append(dayOfMonth);
        dateTimeString.append("/");
        dateTimeString.append(month);
        dateTimeString.append("/");
        dateTimeString.append(year);

        dateTimeString.append(" ");

        dateTimeString.append(hour24);
        dateTimeString.append(":");
        dateTimeString.append(minute);
        dateTimeString.append(":");
        dateTimeString.append(second);

        dateTimeString.append(AM_PM);

        return dateTimeString.toString();
    }

    /**
     * kiem tra thoi gian co hop le hay ko
     *
     * @author: truonglt2
     * @return: void
     * @throws:
     */
    public static boolean isTimeValidate() {
        Calendar c = Calendar.getInstance();
        int minu = c.get(Calendar.MINUTE);
        int hour = c.get(Calendar.HOUR);

        if ((hour >= 8 && hour <= 12) || (hour >= 13 && hour < 18)) {
            if (hour >= 13 && hour < 14) {
                return minu > 30;
            }
            if (hour >= 17 && hour < 18) {
                return minu < 30;
            }
            return true;
        }
        return false;
    }

    public static String getCurrentDate() {
        Date date = Calendar.getInstance().getTime();
        //
        // Display a date in day, month, year format
        //
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String today = formatter.format(date);
        System.out.println("Today : " + today);
        return today;
    }

    public static String getCurrentMonth(String strDatetime, String strFormat) {
        Date date = Calendar.getInstance().getTime();
        //
        // Display a date in day, month, year format
        //
        SimpleDateFormat formatter = new SimpleDateFormat(strFormat);
//        String today = formatter.format(date);
        Date dateCurrent = new Date();
        try {
            dateCurrent = formatter.parse(strDatetime);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        int month = dateCurrent.getMonth()+1;
        String strReturn = "";
        if (month < 10) {
            strReturn = "0" + month;
        } else {
            strReturn = "" + month;
        }
        System.out.println("StrReturn : " + strReturn);
        return strReturn;
    }

    public String formatDateWithDate(Date date, String format) {
        DateFormat formatter = new SimpleDateFormat(format);
        String strDate = formatter.format(date);
        System.out.println("Today : " + strDate);
        return strDate;
    }
    public static int compareDate(String strDate1, String strDate2)
    {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(Common.DATETIME_FORMAT_PATTERN_1);
            Date date1 = sdf.parse(strDate1);
            Date date2 = sdf.parse(strDate2);

            System.out.println("date1 : " + sdf.format(date1));
            System.out.println("date2 : " + sdf.format(date2));
            if (date1.compareTo(date2) > 0) {
                System.out.println("Date1 is after Date2");
                return ConstantsApp.DATE_AFTER;
            } else if (date1.compareTo(date2) < 0) {
                System.out.println("Date1 is before Date2");
                return ConstantsApp.DATE_BEFOR;
            } else if (date1.compareTo(date2) == 0) {
                System.out.println("Date1 is equal to Date2");
                return ConstantsApp.DATE_EQUAL;
            } else {
                System.out.println("How to get here?");
            }
        }catch (Exception ex){
            ex.printStackTrace();
            return 0;
        }
        return 0;
    }
}
