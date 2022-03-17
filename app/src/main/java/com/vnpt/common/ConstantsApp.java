package com.vnpt.common;

/**
 * @Description: Define hang so trong chuong trinh
 * @author:truonglt2
 * @since:Feb 7, 2014 3:52:24 PM
 * @version: 1.0
 * @since: 1.0
 */
public interface ConstantsApp {

    String ACTION_BROADCAST = "bookstore.action";
    String HASHCODE_BROADCAST = "bookstore.hashCode";
    String URL_SERVER_SOAP = "https://ttvhttpleikuadmin.vnpt-invoice.com.vn";
//    String URL_SERVER_SOAP = "https://ttvhttpleikuadmindemo.vnpt-invoice.com.vn/";
    //	String URL_SERVER_SOAP = "https://blaihccongthaadmindemo.vnpt-invoice.com.vn";
//	String AUTHORIZE_WS_Account = "hccthaservice";
    String AUTHORIZE_WS_Account = "ttvhttpleikuservice";
//    String AUTHORIZE_WS_Account = "congtrinhdothigliservice";
    String AUTHORIZE_WS_Password = "123456aA@";


    String TYPE_GRID_VIEW_CUSTOM = "type grid view custom";

    int TYPE_GRID_VIEW_CUSTOM_TEXT_DEFAULT = 1;
    int TYPE_GRID_VIEW_CUSTOM_TEXT_CUSTOM_ONE_TEXT = 2;
    int TYPE_GRID_VIEW_CUSTOM_TEXT_CUSTOM_MORE_TEXT = 3;
    int TYPE_GRID_VIEW_CUSTOM_IMAGE = 4;
    int TYPE_GRID_VIEW_CUSTOM_IMAGE_AND_TEXT = 5;
    int TYPE_GRID_VIEW_CUSTOM_INVOICE_BL = 6;
    String ID_PROVINCE = "Mã tỉnh: ";
    int SIZE_IMAGE = 120;
    /* Value so sánh giá trị ngày*/
    final int DATE_AFTER = 1;
    final int DATE_BEFOR = -1;
    final int DATE_EQUAL = 0;
    //	/* Dinh nghia max length */
//	// min length ten dang nhap
//	public static final int NUM_MIN_LENGTH_LOGIN_NAME = 3;
//	// max length ten dang nhap
//	public static final int NUM_MAX_LENGTH_LOGIN_NAME = 20;
//	// min length mat khau
//	public static final int NUM_MIN_LENGTH_PASSWORD = 6;
//	// max length mat khau
//	public static final int NUM_MAX_LENGTH_PASSWORD = 16;
//	// max length nick
//	public static final int NUM_MAX_LENGTH_NICK = 20;
//	// max length ho ten
//	public static final int NUM_MAX_LENGTH_FULL_NAME = 20;
//	// max length den tu
//	public static final int NUM_MAX_LENGTH_ADDRESS = 40;
//	// max length gioi thieu
//	public static final int NUM_MAX_LENGTH_ABOUT = 120;
//	// max length so dien thoai
//	public static final int NUM_MAX_LENGTH_PHONE_NUMBER = 14;
//	// min length so dien thoai
//	public static final int NUM_MIN_LENGTH_PHONE_NUMBER = 7;
//	// max length noi dung chat
//	public static final int NUM_MAX_LENGTH_CHAT_CONTENT = 140;
//	// max length so dien thoai filter
//	public static final int NUM_MAX_LENGTH_PHONE_FILTER = 50;
//	// max length search text box
//	public static final int NUM_MAX_LENGTH_SEARCH_TEXT = 255;
//	// max length so tien
//	public static final int NUM_MAX_LENGTH_AMOUNT_NUMBER = 8;

    //kich thuoc cua hinh anh dinh kem
    int MAX_THUMB_NAIL_WIDTH = 70;
    int MAX_THUMB_NAIL_HEIGHT = 50;
    //kich thuoc anh toi da upload
    int MAX_IMAGE_WIDTH_HEIGHT = 740;
    int MAX_UPLOAD_IMAGE_WIDTH = 740;
    int MAX_UPLOAD_IMAGE_HEIGHT = 740;

    String TEMP_IMG = "image_tmp.jpg";//ten temp image

    final class TextLoai_ErrorOnServer {
        public static final String ERR_DATA_NOT_FOUND = "ORA-20002:";
        public static final String ERR_INVALID_DATA = "ORA-20003:";
        public static final String ERR_DATA_ALREADY_EXISTS = "ORA-20004:";
        public static final String ERR_INVALID_ACTION = "ORA-20005";
        public static final String ERR_GENERY = "ORA-06512";
    }

    final class TimeType {
        public static final int DATE = 0;
        public static final int WEEK = DATE + 1;
        public static final int MONTH = WEEK + 1;
        public static final int QUARTER = MONTH + 1;
        public static final int YEAR = QUARTER + 1;
        public static final int DATE_TO_DATE = YEAR + 1;
    }

    final class TypeSysInvMobile {
        public static final int INVOICE = 1;
        public static final int RECEIPT = 2;
    }

    final class StatusPayment {
        public static final int NOT_PAYMENT = 0;
        public static final int PAYMENTED = 1;
    }

    final class StatusProgress {
        public static final int SUCCESS = 1;
        public static final int FAILED = 0;
    }

}
