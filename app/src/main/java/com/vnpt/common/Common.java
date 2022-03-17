package com.vnpt.common;

import android.os.Build;

import com.vnpt.dto.InvoiceUpdatePaymentedRequest;
import com.vnpt.dto.User;
import com.vnpt.imgloader.ImageLoader;

import java.util.ArrayList;


/**
 * @Description: define cac gia tri cuc bo
 * @author:truonglt2
 * @since:Feb 7, 2014 3:52:08 PM
 * @version: 1.0
 * @since: 1.0
 */
public class Common {
    //HANOI_METROPOLE_FO_001.xslt
    //HANOI_METROPOLE_INT_06.xslt
//    public static String nameFileXSLT = "HANOI_METROPOLE_FO_001.xslt";
    public static String KEY_FINAL_ADDRESS_SERVER = "final_address_server";
    //    public static String nameFileXSLT = "InvoiceVAT4.xslt";
    public static String nameFileXSLT = "InvoiceVAT4(1).xslt";
    public static String remoteUser = "java2novice";
    public static String password = "Simple4u!";
    public static String AUTHORIZE_STRING = "";
//    public static String AUTHORIZE_WS_Account = "congtrinhdothigliservice";
//    public static String DEFAULT_PATTERN_INVOICES = "01BLP0-001";
//    public static String DEFAULT_SERIAL_INVOICES = "HC-19E";
    public static String DEFAULT_PATTERN_INVOICES = "01BLP0-001";
    public static String DEFAULT_SERIAL_INVOICES = "TT-20E";
//    public static String DEFAULT_PATTERN_INVOICES = "01GTKT0/001";
//    public static String DEFAULT_SERIAL_INVOICES = "AA/18E";

    public static ArrayList<InvoiceUpdatePaymentedRequest> arrInvoiceUpdatePaymented = new ArrayList<>();
    public static String CURRENCY = " VNĐ";
    public static String idInvoiceUpdatedPaymented = ""; // phân cách nhau bởi dấu -
    public static int currentPageInvoice;
    public static final int PERMISSION_REQUEST_CODE = 1;

    public static final int STATUS_PAYMENTED = 1;
    public static final int STATUS_NOT_PAYMENT = 0;

    public static final int DATA_ERROR = 0;
    public static final int DATA_SUCCESS = 1;
    public static final int DATA_INVALIDATE = 2;

    public static final int VAL_PERMISTION_STAFF = 1;
    public static final int VAL_PERMISTION_CUSTOMER = 2;
    //
    public static final int DIALOG_SINGLE_FILTER_INVOICE = 1;
    public static final int DIALOG_SINGLE_SORT_INVOICE = 2;

    public static final int ID_USER_TEST = 1;
    public static final float DEFAULT_BIEN_DO = (float) 3.5;

    // OWNER USER INFOMATION
    // This is used to load image normal in application
    public static boolean checkLogined = false;
    public static ImageLoader imageLoad;
    public static User userInfo;
    public static final int PAGE_SIZE = 15;

    public static String PACKAGE_NAME = "com.vnpt.staffhddt";
    public static String PATH_IMAGE_SIGNED = "/img/signed/";
    public static String DATETIME_FORMAT_PATTERN_1 = "dd/MM/yyyy";


    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    // value key of cerfiticate
    public static final String KEY_LANGUAGE_DEFAULT = "key_language_default";
    public static final String KEY_FIRST_CONFIG = "key_first_config";
    public static final String KEY_PATH_IMAGE_SIGN_MANUAL_RECENTLY = "key_path_image_sign_manual_recently";
    public static final String KEY_PATH_IMAGE_SIGN_SERVER_RECENTLY = "key_path_image_sign_server_recently";
    public static final String KEY_PATH_DEFAULT_IMAGE_SIGN_MANUAL = "key_path_default_image_sign_manual";
    public static final String KEY_DEFAULT_PATTERN_INVOICES = "key_default_pattern_invoices";
    public static final String KEY_DEFAULT_SERIAL_INVOICES = "key_default_serial_invoices";
    public static final String KEY_TYPE_SYS_MOBILE = "key_type_sys_mobile";


    public static final String KEY_PATH_IMAGE_SING_CAMERA = "key_path_image_sing_camera";
    public static final String KEY_PATH_CERTIFICATE_USER = "key_path_certificate_user";
    public static final String KEY_PATH_CERTIFICATE_USER_MOBILE = "key_path_certificate_user";
    public static final String KEY_PATH_CERTIFICATE_USER_SERVER = "key_path_certificate_user";
    public static final String KEY_PATH_DEFAULT_XML = "key_path_default_xml";
    public static final String KEY_PATH_DEFAULT_XML_RECENTLY = "key_path_default_xml_recently";
    public static final String KEY_PATH_DEFAULT_HTML_INVOICE = "key_path_default_html_invoice";
    public static final String KEY_FROM_WHICH_SCREEN = "key_from_which_screen";//key de biet nguoi dung vao tu man hinh nao de load webview cho nhe
    // Key shared Refference
    public static final String KEY_LOGIN_USER_OUTLET = "key_login_user_outlet";
    public static final String KEY_IS_LOGIN_INFOR_USER = "is_login_infor_user";
    public static final String KEY_TOKEN_LOGIN = "key_token_login";
    public static final String KEY_USER_NAME = "key_user_name";
    public static final String KEY_USER_PASS = "key_user_pass";
    public static final String KEY_OUTLET_LOGIN = "key_outlet_login";
    public static final String KEY_USER_FULLNAME = "key_user_fullname";
    public static final String KEY_USER_LOGINLAST = "key_user_loginlast";
    public static final String KEY_DATA_ITEM_INVOICE = "key_data_item_invoice";
    public static final String KEY_DATA_ROOM_NO = "key_data_room_no";
    public static final String KEY_DATA_NAME_CUS = "key_data_name_cus";
    public static final String REF_KEY_TYPE_FILTER = "ref_key_type_filter";
    public static final String REF_KEY_TYPE_SORT = "ref_key_type_sort";
    public static final String REF_KEY_DATE_SYNC = "ref_key_date_sync";
    public static final String REF_KEY_DATE_SYNC_FROM = "ref_key_date_sync_from";
    public static final String REF_KEY_DATE_SYNC_TO = "ref_key_date_sync_to";
    public static final String REF_KEY_CHANGE_DATE = "ref_key_change_date";

    //request code of activity for result
    public static final int REQUEST_CODE_ACTIVITY_SIGNATURE_MANUAL = 1;
    public static final int REQUEST_CODE_ACTIVITY_SIGNATURE_RECORD = 2;
    public static final int REQUEST_CODE_ACTIVITY_SIGNATURE_CAMERA = 3;
    public static final int REQUEST_CODE_ACTIVITY_DETAILS_INVOICE = 4;
    public static final int REQUEST_CODE_ACTIVITY_DETAILS_FOR_RETURN_INVOICE = 5;

    public static final String STR_STATUS_PAYMENTED = "Đã nộp tiền";
    public static final String STR_STATUS_NOT_PAYMENT = "Chưa nộp tiền";
    public static final String KEY_TYPE_VIEW_CERFITICATE = "key_type_view_cerfiticate";
    // 3 phương thứ của chứng thực app
    public static final int TYPE_HAVE_NOT_CERFITICATE = 1;
    public static final int TYPE_VIEW_CERFITICATE_MANUAL = 2;
    public static final int TYPE_VIEW_CERFITICATE_RECORD = 3;
    public static final int TYPE_VIEW_CERFITICATE_CAMERA = 4;

    public static final int TYPE_VIEW_CERFITICATE_SMS = 5;
    public static final int TYPE_VIEW_CERFITICATE_EMAIL = 6;
    public static final int TYPE_VIEW_CERFITICATE_APP_USER = 7;
    public static final int TYPE_VIEW_CERFITICATE_PRINT_PAPER = 8;

    public static final String KEY_ID_INVOICE_CERTIFICATE = "key_id_invoice_certificate";
    public static final String KEY_TYPE_UPDATE_CERTIFICATE = "key_type_update_certificate";
    public static final String KEY_STATUS_PAYMENT_INVOICE = "key_status_payment_invoice";
    public static final String BUNDLE_ITEM_STATUS_PAYMENTED_INVOICE = "bundle_item_status_paymented_invoice";
    public static final String BUNDLE_KEY_BACKLIST = "bundle_key_backlist";
    // type bundle
     /*
    *  typeFilter:
    // 1 - theo tên khách hàng
    // 2 - theo status trả tiền
    // 3 - theo bán kính 20
    // 4 - theo bán kính 50
    // 5 - theo thời gian
    * */
    public static final String BUNDLE_KEY_STATUS_INVOICE = "bundle_key_status_invoice";
    public static final String BUNDLE_KEY_FROM_DATE_METROPOLE = "bundle_key_from_date_metropole";
    public static final String BUNDLE_KEY_TO_DATE_METROPOLE = "bundle_key_to_date_metropole";

    public static final String BUNDLE_KEY_FILTER = "bundle_key_filter";
    public static final String BUNDLE_KEY_PAGE_LIST_INVOICE = "bundle_key_page_list_invoice";
    public static final String BUNDLE_KEY_PAGE_SIZE_LIST_INVOICE = "bundle_key_page_size_list_invoice";
    public static final String BUNDLE_KEY_ARR_ITEM_REQUEST_UPADTE_STATUS_PAYMENT = "bundle_key_arr_item_request_upadte_status_payment";

    public static final String BUNDLE_KEY_ID_INVOICE = "bundle_key_id_invoice";
    public static final String BUNDLE_KEY_ID_CUSTOMER = "bundle_key_id_customer";
    public static final String BUNDLE_KEY_TOKEN_INVOICE = "bundle_key_token_invoice";

    public static final int FILTER_BY_NAME_CUSTOMER = 1;
    public static final int FILTER_BY_CHECK_NO = 2;
    public static final int FILTER_BY_TABLE_NO = 3;
    public static final int FILTER_BY_STATUS_PAYMENTED = 4;
    public static final int FILTER_BY_STATUS_NOT_PAYMENT = 5;


    public static final String BUNDLE_KEY_SORT = "bundle_key_sort";
    public static final String BUNDLE_KEY_SEARCH = "bundle_key_search";
    public static final String BUNDLE_KEY_SEARCH_FROM = "bundle_key_search_from";
    public static final String BUNDLE_KEY_SEARCH_TO = "bundle_key_search_to";
    public static final String BUNDLE_KEY_SEARCH_WITH_ID_CUSTOMER = "bundle_key_search_with_id_customer";

    public static final String REFF_KEY_METHOD_AUTHORIZE_USER = "reff_key_method_authorize_user";

    public static final String REFF_KEY_DATA_INFOR_USER_LOGIN = "reff_key_data_infor_user_login";
    public static final String REFF_KEY_DATA_TERM = "reff_key_data_term";

    public static final String BUNDLE_KEY_FKEY_INVOICE = "bundle_key_fkey_invoice";
    public static final String BUNDLE_KEY_PATTERN = "bundle_key_pattern";
    public static final String BUNDLE_KEY_SERIAL = "bundle_key_serial";
    public static final String BUNDLE_KEY_STATUS = "bundle_key_status";
    public static final String BUNDLE_KEY_XMLINVDATA = "bundle_key_xmlinvdata";
    public static final String BUNDLE_KEY_FROMDATE = "bundle_key_fromdate";
    public static final String BUNDLE_KEY_TODATE = "bundle_key_todate";
    public static final String BUNDLE_KEY_CHECKNO = "bundle_key_checkno";
    public static final String BUNDLE_KEY_HTML_SIGNED_SUCCESS = "bundle_key_html_signed_success";
    public static final String BUNDLE_KEY_CUS_NAME = "bundle_key_cus_name";
    public static final String BUNDLE_KEY_ROOM_NO = "bundle_key_room_no";
    public static final String BUNDLE_KEY_BASE64_IMAGE = "bundle_key_base64_image";
    public static final String BUNDLE_KEY_LOAD_FROM_SERVER = "bundle_key_load_from_server";


    public static final String BUNDLE_KEY_PAGEINDEX = "bundle_key_pageindex";
    public static final String BUNDLE_KEY_PAGESIZE = "bundle_key_pagesize";
    public static final String BUNDLE_KEY_ORG_ID = "bundle_key_org_id";

    public static final String BUNDLE_KEY_FEE_VAL = "bundle_key_fee_val";
    public static final String BUNDLE_KEY_FEE_NAME = "bundle_key_fee_name";
    public static final String BUNDLE_KEY_CUS_ADD = "bundle_key_cus_add";
    public static final String BUNDLE_KEY_CUS_PHONE = "bundle_key_cus_phone";


    public static final String REFF_KEY_TYPE_DATA_DEMO = "reff_key_type_data_demo";
    public static final int VALUE_REFF_KEY_TYPE_DATA_DEMO_ENVIRONMENT = 4;

    public static final int PRINTER_WOOSIM = 1;
    public static final int PRINTER_ER58AI = 2;
    public static final int PRINTER_POS58 = 3;

}
