package com.vnpt.models;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.gson.Gson;
import com.vnpt.common.ActionEvent;
import com.vnpt.common.ActionEventConstant;
import com.vnpt.common.Common;
import com.vnpt.common.ConstantsApp;
import com.vnpt.common.ErrorConstants;
import com.vnpt.common.ModelEvent;
import com.vnpt.controller.UserController;
import com.vnpt.database.TbCustomer;
import com.vnpt.database.TbInvoice;
import com.vnpt.dto.BaseReponse;
import com.vnpt.dto.FeeInvoice;
import com.vnpt.dto.HoaDon;
import com.vnpt.dto.InvoiceCadmin;
import com.vnpt.dto.InvoiceCadminBL;
import com.vnpt.dto.InvoiceDetails;
import com.vnpt.dto.InvoiceHDDTDetails;
import com.vnpt.dto.InvoiceUpdatePaymentedRequest;
import com.vnpt.dto.Outlet;
import com.vnpt.dto.TableNo;
import com.vnpt.dto.Term;
import com.vnpt.utils.DateTimeUtil;
import com.vnpt.utils.Helper;
import com.vnpt.utils.StoreInvoiceSharePreferences;
import com.vnpt.utils.StoreSharePreferences;
import com.vnpt.utils.StringBienLai;
import com.vnpt.webservice.AppServices;
import com.vnpt.webservice.CadminServices;
import com.vnpt.webservice.InvoicesServices;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description: lop model nhan cac request tu controler va xu ly data de tra ve
 * cho controller
 * @author:truonglt2
 * @since:Feb 7, 2014 5:10:15 PM
 * @version: 1.0
 * @since: 1.0
 */
public class UserModel {
    protected static UserModel instance;

    protected UserModel() {
    }

    public static UserModel getInstance() {
        if (instance == null) {
            instance = new UserModel();
        }
        return instance;
    }

    /**
     * nhan request data tu controller
     *
     * @param e
     * @author: truonglt2
     * @return: void
     * @throws:
     */
    public void requestData(ActionEvent e) {
        ModelEvent model = new ModelEvent();
        model.setActionEvent(e);
        Activity base = null;

        if (e.sender instanceof Activity) {
            base = (Activity) e.sender;
        } else if (e.sender instanceof Fragment) {
            base = ((Fragment) e.sender).getActivity();
        }

        switch (e.action) {
            // handle event get data list menu and content of book
            case ActionEventConstant.ACTION_GET_DATA_INVOICE_BY_LIST: {
                try {
                    Bundle bundle = (Bundle) e.viewData;
                    String mPattern = bundle.getString(Common.BUNDLE_KEY_PATTERN);
                    String mSerial = bundle.getString(Common.BUNDLE_KEY_SERIAL);
                    String fromDate = bundle.getString(Common.BUNDLE_KEY_FROMDATE, DateTimeUtil.getCurrentDate());
                    String toDate = bundle.getString(Common.BUNDLE_KEY_TODATE, DateTimeUtil.getCurrentDate());
                    int pageIndex = bundle.getInt(Common.BUNDLE_KEY_PAGEINDEX, 1);
                    int pageSize = bundle.getInt(Common.BUNDLE_KEY_PAGESIZE, Common.PAGE_SIZE);
                    int orgId = bundle.getInt(Common.BUNDLE_KEY_ORG_ID, 0);
                    ArrayList<InvoiceCadmin> arrData = new ArrayList<InvoiceCadmin>();
                    InvoiceCadminBL invoiceCadminBL = new InvoiceCadminBL();
                    AppServices service = new AppServices(base);
                    System.out.println("Đầu vào:" + mPattern + " + " + mSerial + " + " + fromDate + " + " + toDate + " + " + pageIndex + " + " + pageSize + " + " + orgId);
                    boolean isLoadFromServer = bundle.getBoolean(Common.BUNDLE_KEY_LOAD_FROM_SERVER, false);
                    InvoiceCadminBL result = new InvoiceCadminBL();
                    if (isLoadFromServer) {
                        result = service.listInvoice(fromDate, toDate, mPattern, mSerial, orgId, pageSize, pageIndex);
                    } else {
                        int offset = (pageIndex - 1) * pageSize + 1;
                        int fromIndex = offset;
                        int toIndex = offset + pageSize;
                        result = getDataFromLocal(base, fromIndex, toIndex);
                    }
                    if (result.getStatusCode() == 0) {
                        //ERR:1 : Không có quyền
                        model.setModelMessage("Lỗi xảy ra!");
                        model.setModelCode(ErrorConstants.ERROR_COMMON);
                        UserController.getInstance().handleErrorModelEvent(model);
                    } else //result.getStatusCode()==1
                    {
                        //NẾU KO CÓ LỖI, trả về XML định dạng như sau:
                        model.setModelData(result);
                        model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
                        UserController.getInstance().handleModelEvent(model);
                    }

                } catch (Exception ex) {
                    model.setModelMessage(ex.getMessage());
                    model.setModelCode(ErrorConstants.ERROR_COMMON);
                    UserController.getInstance().handleErrorModelEvent(model);
                }
                break;
            }
            case ActionEventConstant.ACTION_GET_DATA_INVOICE_BY_LIST_LOAD_MORE: {
                try {
                    Bundle bundle = (Bundle) e.viewData;
                    String mPattern = bundle.getString(Common.BUNDLE_KEY_PATTERN);
                    String mSerial = bundle.getString(Common.BUNDLE_KEY_SERIAL);
                    String fromDate = bundle.getString(Common.BUNDLE_KEY_FROMDATE, DateTimeUtil.getCurrentDate());
                    String toDate = bundle.getString(Common.BUNDLE_KEY_TODATE, DateTimeUtil.getCurrentDate());
                    int pageIndex = bundle.getInt(Common.BUNDLE_KEY_PAGEINDEX, 1);
                    int pageSize = bundle.getInt(Common.BUNDLE_KEY_PAGESIZE, Common.PAGE_SIZE);
                    int orgId = bundle.getInt(Common.BUNDLE_KEY_ORG_ID, 0);
                    ArrayList<InvoiceCadmin> arrData = new ArrayList<InvoiceCadmin>();
                    InvoiceCadminBL invoiceCadminBL = new InvoiceCadminBL();
                    AppServices service = new AppServices(base);
                    System.out.println("Đầu vào:" + mPattern + " + " + mSerial + " + " + fromDate + " + " + toDate + " + " + pageIndex + " + " + pageSize + " + " + orgId);
                    boolean isLoadFromServer = bundle.getBoolean(Common.BUNDLE_KEY_LOAD_FROM_SERVER, false);
                    InvoiceCadminBL result = new InvoiceCadminBL();
                    if (isLoadFromServer) {
                        result = service.listInvoice(fromDate, toDate, mPattern, mSerial, orgId, pageSize, pageIndex);
                    } else {
                        int offset = (pageIndex - 1) * pageSize + 1;
                        int fromIndex = offset;
                        int toIndex = offset + pageSize;
                        result = getDataFromLocal(base, fromIndex, toIndex);
                    }
                    if (result.getStatusCode() == 0) {
                        //ERR:1 : Không có quyền
                        model.setModelMessage("Lỗi xảy ra!");
                        model.setModelCode(ErrorConstants.ERROR_COMMON);
                        UserController.getInstance().handleErrorModelEvent(model);
                    } else //result.getStatusCode()==1
                    {
                        //NẾU KO CÓ LỖI, trả về XML định dạng như sau:
                        model.setModelData(result);
                        model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
                        UserController.getInstance().handleModelEvent(model);
                    }

                } catch (Exception ex) {
                    model.setModelMessage(ex.getMessage());
                    model.setModelCode(ErrorConstants.ERROR_COMMON);
                    UserController.getInstance().handleErrorModelEvent(model);
                }
                break;
            }
            case ActionEventConstant.ACTION_GET_DATA_DETAIL_INVOICE: {
                try {
                    Bundle bundle = (Bundle) e.viewData;
                    String tokenInvoice = bundle.getString(Common.BUNDLE_KEY_TOKEN_INVOICE);
                    AppServices service = new AppServices(base);
                    InvoiceHDDTDetails itemDetails = new InvoiceHDDTDetails();
//                    String strResult = service.downloadInvNoPay(tokenInvoice);
                    String strResult = service.downloadInvFkeyNoPay(tokenInvoice);
                    if (strResult.contains("ERR:") || strResult == null) {
                        //ERR:1 : Không có quyền
                        //ERR:2 : Đầu vào thiếu dữ liệu (y/c truyền đủ tham số)
                        //ERR:7 : Không tìm thấy công typ
                        model.setModelMessage("Lỗi xảy ra!");
                        model.setModelCode(ErrorConstants.ERROR_COMMON);
                        UserController.getInstance().handleErrorModelEvent(model);
                    } else {
                        itemDetails = service.parseDetaisInvoice(strResult);
                        model.setModelData(itemDetails);
                        model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
                        UserController.getInstance().handleModelEvent(model);
                    }

                } catch (Exception ex) {
                    model.setModelMessage(ex.getMessage());
                    model.setModelCode(ErrorConstants.ERROR_COMMON);
                    UserController.getInstance().handleErrorModelEvent(model);
                }
                break;
            }
            case ActionEventConstant.ACTION_SAVE_NEW_DATA_INVOICE: {
                try {
                    Bundle bundle = (Bundle) e.viewData;
                    AppServices service = new AppServices(base);
                    String cusName =  bundle.getString(Common.BUNDLE_KEY_CUS_NAME);
                    String cusAddress =  bundle.getString(Common.BUNDLE_KEY_CUS_ADD);
                    String cusPhone = bundle.getString(Common.BUNDLE_KEY_CUS_PHONE);
                    String cusCode = bundle.getString(Common.BUNDLE_KEY_ID_CUSTOMER);
                    double fee =  bundle.getDouble(Common.BUNDLE_KEY_FEE_VAL);
                    String feeString = String.valueOf(fee);
                    String amountInWords = StringBienLai.docSo(fee);
                    String feeName =  bundle.getString(Common.BUNDLE_KEY_FEE_NAME);
                    String kindOfPayment =  bundle.getString(Common.BUNDLE_KEY_ORG_ID);
                    String userLogin = StoreSharePreferences.getInstance(base).loadStringSavedPreferences(Common.KEY_USER_NAME);
                    String userPass = StoreSharePreferences.getInstance(base).loadStringSavedPreferences(Common.KEY_USER_PASS);
                    String mPattern =  StoreSharePreferences.getInstance(base).loadStringSavedPreferences(Common.KEY_DEFAULT_PATTERN_INVOICES);
                    String mSerial =  StoreSharePreferences.getInstance(base).loadStringSavedPreferences(Common.KEY_DEFAULT_SERIAL_INVOICES);
                    String xmlData = StringBienLai.getStringBienLai(null, cusCode, cusName, cusAddress, cusPhone, "", kindOfPayment, feeName, feeString, feeString, amountInWords, null, userLogin);
                    String result = service.importAndPublishInv(userLogin, userPass, mPattern, mSerial, 0, xmlData);
                    if (result.contains("ERR:") || result == null) {
                        //ERR:1 : Không có quyền
                        //ERR:2 : Đầu vào thiếu dữ liệu (y/c truyền đủ tham số)
                        //ERR:5 : Lỗi CKS
                        //ERR:7 : Không tìm thấy công ty
                        model.setModelMessage("Lỗi xảy ra!");
                        model.setModelCode(ErrorConstants.ERROR_COMMON);
                        UserController.getInstance().handleErrorModelEvent(model);
                    } else {
//                        itemDetails = service.parseDetaisInvoice(strResult);
//                        model.setModelData(itemDetails);
                        model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
                        UserController.getInstance().handleModelEvent(model);
                    }

                } catch (Exception ex) {
                    model.setModelMessage(ex.getMessage());
                    model.setModelCode(ErrorConstants.ERROR_COMMON);
                    UserController.getInstance().handleErrorModelEvent(model);
                }
                break;
            }
            case ActionEventConstant.ACTION_GET_DATA_ORG_INVOICE: {
                try {
                    Bundle bundle = (Bundle) e.viewData;
                    String userLogin = StoreSharePreferences.getInstance(base).loadStringSavedPreferences(Common.KEY_USER_NAME);
                    AppServices service = new AppServices(base);
                    String strResult = service.listOrgInvoice(userLogin);
                    if (strResult.contains("ERR:") || strResult == null) {
                        //ERR:1 : Không có quyền
                        //ERR:2 : Đầu vào thiếu dữ liệu (y/c truyền đủ tham số)
                        //ERR:7 : Không tìm thấy công typ
                        model.setModelMessage("Lỗi xảy ra!");
                        model.setModelCode(ErrorConstants.ERROR_COMMON);
                        UserController.getInstance().handleErrorModelEvent(model);
                    } else {
//                        itemDetails = service.parseDetaisInvoice(strResult);
//                        model.setModelData(itemDetails);
                        model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
                        UserController.getInstance().handleModelEvent(model);
                    }

                } catch (Exception ex) {
                    model.setModelMessage(ex.getMessage());
                    model.setModelCode(ErrorConstants.ERROR_COMMON);
                    UserController.getInstance().handleErrorModelEvent(model);
                }
                break;
            }
            case ActionEventConstant.ACTION_GET_DATA_FEE_INVOICE: {
                try {
                    Bundle bundle = (Bundle) e.viewData;
                    AppServices service = new AppServices(base);
                    List<FeeInvoice> result = service.listFeeInvoice();;
                    if (result == null || result.size() == 0) {
                        //ERR:1 : Không có quyền
                        //ERR:2 : Đầu vào thiếu dữ liệu (y/c truyền đủ tham số)
                        //ERR:7 : Không tìm thấy công typ
                        model.setModelMessage("Lỗi xảy ra!");
                        model.setModelCode(ErrorConstants.ERROR_COMMON);
                        UserController.getInstance().handleErrorModelEvent(model);
                    } else {
//                        itemDetails = service.parseDetaisInvoice(strResult);
                        model.setModelData(result);
                        model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
                        UserController.getInstance().handleModelEvent(model);
                    }

                } catch (Exception ex) {
                    model.setModelMessage(ex.getMessage());
                    model.setModelCode(ErrorConstants.ERROR_COMMON);
                    UserController.getInstance().handleErrorModelEvent(model);
                }
                break;
            }

            // handle event get data list menu and content of book
//            case ActionEventConstant.ACTION_GET_DATA_INVOICE_BY_LIST_SEARCH: {
//                try {
//                    Bundle bundle = (Bundle) e.viewData;
//                    String mPattern =  bundle.getString(Common.BUNDLE_KEY_PATTERN);
//                    String mSerial =  bundle.getString(Common.BUNDLE_KEY_SERIAL);
//                    String fromDate = bundle.getString(Common.BUNDLE_KEY_FROMDATE, DateTimeUtil.getCurrentDate());
//                    String toDate = bundle.getString(Common.BUNDLE_KEY_TODATE,DateTimeUtil.getCurrentDate());
//                    int pageIndex =  bundle.getInt(Common.BUNDLE_KEY_PAGEINDEX);
//                    int pageSize =  bundle.getInt(Common.BUNDLE_KEY_PAGESIZE);
//                    int orgId =  bundle.getInt(Common.BUNDLE_KEY_ORG_ID);
//                    List<InvoiceCadmin> arrData = new ArrayList<>();
//                    AppServices service = new AppServices(base);
//                    System.out.println("Đầu vào:"+mPattern+" + "+mSerial+" + "+fromDate+" + "+toDate+" + "+pageIndex+" + "+pageSize+" + "+orgId);
//                    String result = service.listInvoice(fromDate,toDate,mPattern,mSerial,orgId,pageSize, pageIndex);
//                    if(result.equals("ERR:1"))
//                    {
//                        //ERR:1 : Không có quyền
//                        model.setModelMessage("Lỗi xảy ra!");
//                        model.setModelCode(ErrorConstants.ERROR_COMMON);
//                        UserController.getInstance().handleErrorModelEvent(model);
//                    }
//                    else if (result.equals("ERR:2"))
//                    {
//                        model.setModelMessage("Lỗi xảy ra!");
//                        model.setModelCode(ErrorConstants.ERROR_COMMON);
//                        UserController.getInstance().handleErrorModelEvent(model);
//                        //ERR:2 : Đầu vào thiếu dữ liệu (y/c truyền đủ tham số)
//                    }
//                    else if(result.equals("ERR:7"))
//                    {
//                        model.setModelMessage("Lỗi xảy ra!");
//                        model.setModelCode(ErrorConstants.ERROR_COMMON);
//                        UserController.getInstance().handleErrorModelEvent(model);
//                        //ERR:7 : Không tìm thấy công typ
//                    }
//                    else
//                    {
//                        //NẾU KO CÓ LỖI, trả về XML định dạng như sau:
//                        try {
//                            arrData = service.parseDataListInv(result);
//                        }catch (Exception ex)
//                        {
//                            ex.printStackTrace();
//                        }
//                        model.setModelData(arrData);
//                        model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
//                        UserController.getInstance().handleModelEvent(model);
//                    }
//
//                } catch (Exception ex) {
//                    model.setModelMessage(ex.getMessage());
//                    model.setModelCode(ErrorConstants.ERROR_COMMON);
//                    UserController.getInstance().handleErrorModelEvent(model);
//                }
//                break;
//            }
            case ActionEventConstant.ACTION_UPDATE_SIGNED_INVOICE: {
                try {
                    Bundle bundle = (Bundle) e.viewData;
                    String mFkey = bundle.getString(Common.BUNDLE_KEY_FKEY_INVOICE);
                    String mCheckNo = bundle.getString(Common.BUNDLE_KEY_CHECKNO);
                    String mPattern = bundle.getString(Common.BUNDLE_KEY_PATTERN);
                    String mSerial = bundle.getString(Common.BUNDLE_KEY_SERIAL);
                    String mXmlInvData = bundle.getString(Common.BUNDLE_KEY_XMLINVDATA);
                    CadminServices service = new CadminServices(base);
                    String result = service.updateAndPublishInvByFkey(mFkey, mCheckNo, mXmlInvData, mPattern, mSerial);
                    if (result.equals("ERR:1")) {
                        //ERR:1 : Không có quyền
                        model.setModelMessage("Không có quyền!");
                        model.setModelCode(ErrorConstants.ERROR_COMMON);
                        UserController.getInstance().handleErrorModelEvent(model);
                    } else if (result.equals("ERR:2")) {
                        //ERR:1 : Không có quyền
                        model.setModelMessage("Thông tin đầu vào thiếu!");
                        model.setModelCode(ErrorConstants.ERROR_COMMON);
                        UserController.getInstance().handleErrorModelEvent(model);
                    } else if (result.equals("ERR:20")) {
                        //ERR:2 : Đầu vào thiếu dữ liệu (y/c truyền đủ tham số)
                        model.setModelMessage("Tham số Pattern và Serial không hợp lệ!");
                        model.setModelCode(ErrorConstants.ERROR_COMMON);
                        UserController.getInstance().handleErrorModelEvent(model);
                    } else if (result.equals("ERR:6")) {
                        //ERR:2 : Đầu vào thiếu dữ liệu (y/c truyền đủ tham số)
                        model.setModelMessage("Không tìm thấy hóa đơn!");
                        model.setModelCode(ErrorConstants.ERROR_COMMON);
                        UserController.getInstance().handleErrorModelEvent(model);
                    } else if (result.equals("ERR:5")) {
                        //ERR:7 : Không tìm thấy công typ
                        model.setModelMessage("Không cập nhật và phát hành đc HĐ. Lỗi chung!");
                        model.setModelCode(ErrorConstants.ERROR_COMMON);
                        UserController.getInstance().handleErrorModelEvent(model);
                    } else {
                        //NẾU KO CÓ LỖI, trả về XML định dạng như sau:
//                        String strHTML = "";
//                        try {
//                            strHTML = service.parseDataGetStringDownloadHTMLInv(result);
//                        }catch (Exception ex)
//                        {
//                            ex.printStackTrace();
//                        }
                        model.setModelData(result);
                        model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
                        UserController.getInstance().handleModelEvent(model);
                    }
                } catch (Exception ex) {
                    model.setModelMessage(ex.getMessage());
                    model.setModelCode(ErrorConstants.ERROR_COMMON);
                    UserController.getInstance().handleErrorModelEvent(model);
                }
                break;
            }

            case ActionEventConstant.ACTION_GET_DATA_INVOICE_BY_FKEY: {
                try {
                    Bundle bundle = (Bundle) e.viewData;
                    String fkeyInvoice = bundle.getString(Common.BUNDLE_KEY_FKEY_INVOICE);
                    AppServices service = new AppServices(base);
                    String result = service.getInvViewFkeyNoPay(fkeyInvoice);
                    if (result.equals("ERR:1")) {
                        //ERR:1 : Không có quyền
                        model.setModelMessage("Không có quyền!");
                        model.setModelCode(ErrorConstants.ERROR_COMMON);
                        UserController.getInstance().handleErrorModelEvent(model);
                    } else if (result.equals("ERR:2")) {
                        //ERR:2 : Đầu vào thiếu dữ liệu (y/c truyền đủ tham số)
                        model.setModelMessage("Thông tin đầu vào thiếu!");
                        model.setModelCode(ErrorConstants.ERROR_COMMON);
                        UserController.getInstance().handleErrorModelEvent(model);
                    } else if (result.equals("ERR:4")) {
                        //ERR:2 : Đầu vào thiếu dữ liệu (y/c truyền đủ tham số)
                        model.setModelMessage("Chưa có mẫu hóa đơn nào!");
                        model.setModelCode(ErrorConstants.ERROR_COMMON);
                        UserController.getInstance().handleErrorModelEvent(model);
                    } else if (result.equals("ERR:6")) {
                        //ERR:6 : Không tìm thấy hóa đơn
                        model.setModelMessage("Không tìm thấy hóa đơn!");
                        model.setModelCode(ErrorConstants.ERROR_COMMON);
                        UserController.getInstance().handleErrorModelEvent(model);
                    } else {
                        //NẾU KO CÓ LỖI, trả về XML định dạng như sau:
//                        String strHTML = "";
//                        try {
//                            strHTML = service.parseDataGetStringDownloadHTMLInv(result);
//                        }catch (Exception ex)
//                        {
//                            ex.printStackTrace();
//                        }
                        model.setModelData(result);
                        model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
                        UserController.getInstance().handleModelEvent(model);
                    }

                } catch (Exception ex) {
                    model.setModelMessage(ex.getMessage());
                    model.setModelCode(ErrorConstants.ERROR_COMMON);
                    UserController.getInstance().handleErrorModelEvent(model);
                }
                break;
            }
            case ActionEventConstant.ACTION_GET_INFOR_OUTLET: {
                try {
                    Bundle bundle = (Bundle) e.viewData;
                    CadminServices service = new CadminServices(base);
                    String result = service.getAllOutlet();
                    if (result.equals("ERR:1")) {
                        //ERR:1 : Không có quyền
                        model.setModelMessage("Không có quyền!");
                        model.setModelCode(ErrorConstants.ERROR_COMMON);
                        UserController.getInstance().handleErrorModelEvent(model);
                    } else if (result.equals("ERR:2")) {
                        //ERR:2 : Đầu vào thiếu dữ liệu (y/c truyền đủ tham số)
                        model.setModelMessage("Thông tin đầu vào thiếu!");
                        model.setModelCode(ErrorConstants.ERROR_COMMON);
                        UserController.getInstance().handleErrorModelEvent(model);
                    } else if (result.equals("ERR:4")) {
                        //ERR:2 : Đầu vào thiếu dữ liệu (y/c truyền đủ tham số)
                        model.setModelMessage("Chưa có mẫu hóa đơn nào!");
                        model.setModelCode(ErrorConstants.ERROR_COMMON);
                        UserController.getInstance().handleErrorModelEvent(model);
                    } else if (result.equals("ERR:7")) {
                        //ERR:7 : Không tìm thấy công typ
                        model.setModelMessage("Không tìm thấy hóa đơn!");
                        model.setModelCode(ErrorConstants.ERROR_COMMON);
                        UserController.getInstance().handleErrorModelEvent(model);
                    } else {
                        List<Outlet> arrOutlet = new ArrayList<>();
                        try {
                            arrOutlet = service.parseDataListOultet(result);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        model.setModelData(arrOutlet);
                        model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
                        UserController.getInstance().handleModelEvent(model);
                    }

                } catch (Exception ex) {
                    model.setModelMessage(ex.getMessage());
                    model.setModelCode(ErrorConstants.ERROR_COMMON);
                    UserController.getInstance().handleErrorModelEvent(model);
                }
                break;
            }

            case ActionEventConstant.ACTION_UPATE_STATUS_PAYMENT_INVOICE: {
                try {
                    Bundle bundle = (Bundle) e.viewData;
                    String fkeyInvoice = bundle.getString(Common.BUNDLE_KEY_FKEY_INVOICE);
                    int status = bundle.getInt(Common.KEY_STATUS_PAYMENT_INVOICE);
                    AppServices service = new AppServices(base);
                    String result = "";
                    //nếu HĐ ở trạng thái đã thanh toán thì chuyển sang trạng thái chưa thanh toán và ngược lại
                    if (status == ConstantsApp.StatusPayment.PAYMENTED) {
                        result = service.unConfirmPaymentFkey(fkeyInvoice);
                    } else {
                        result = service.confirmPaymentFkey(fkeyInvoice);
                    }
                    BaseReponse reponse = new BaseReponse();
                    if (result.equals("OK:")) {
                        reponse.setStatusCode(ConstantsApp.StatusProgress.SUCCESS);
                        model.setModelData(reponse);
                        model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
                        UserController.getInstance().handleModelEvent(model);
                    } else {
                        model.setModelMessage("Lỗi xảy ra!");
                        model.setModelCode(ErrorConstants.ERROR_COMMON);
                        UserController.getInstance().handleErrorModelEvent(model);
                    }
                } catch (Exception ex) {
                    model.setModelMessage(ex.getMessage());
                    model.setModelCode(ErrorConstants.ERROR_COMMON);
                    UserController.getInstance().handleErrorModelEvent(model);
                }
                break;
            }
            case ActionEventConstant.ACTION_FILTER_AND_SORT_INVOICE: {
                try {
                    /*public static final int FILTER_BY_NAME_CUSTOMER = 1;
    public static final int FILTER_BY_STATUS_PAYMENT = 2;
    public static final int FILTER_BY_RADIUS_20 = 3;
    public static final int FILTER_BY_RADIUS_50 = 4;
    public static final int FILTER_BY_TIME_PAYMENT = 5;*/
                    Bundle bundle = (Bundle) e.viewData;
                    int typeFilter = bundle.getInt(Common.BUNDLE_KEY_FILTER);
                    boolean typeSort = bundle.getBoolean(Common.BUNDLE_KEY_SORT);
                    String strSort = "";
                    if (typeSort) {
                        strSort = "DESC";
                    } else {
                        strSort = "ASC";
                    }
                    String query = "";
                    switch (typeFilter) {
                        case Common.FILTER_BY_NAME_CUSTOMER: {

                            query = "SELECT DISTINCT * FROM  Invoice, Customer WHERE invoice.idCustomer = Customer.id ORDER BY " + TbCustomer.CUSTOMER_TABLE + "." + TbCustomer.CUSTOMER_Name + " " + strSort;
                            break;
                        }
                        case Common.FILTER_BY_STATUS_PAYMENTED: {
                            query = "SELECT DISTINCT * FROM  Invoice, Customer WHERE invoice.idCustomer = Customer.id ORDER BY " + TbInvoice.INVOICE_TABLE + "." + TbInvoice.INVOICE_Status + " " + strSort;
                            break;
                        }
                        case Common.FILTER_BY_STATUS_NOT_PAYMENT: {
                            query = "SELECT DISTINCT * FROM  Invoice, Customer WHERE invoice.idCustomer = Customer.id";
                            break;
                        }
                        default:
                            query = "SELECT DISTINCT * FROM  Invoice, Customer WHERE invoice.idCustomer = Customer.id";
                            break;
                    }
                    Log.e("gia trị query:", "" + query);
                    ArrayList<HoaDon> arrInvoice = TbInvoice.getInstance(base).getAllCustomerQuery(query);
                    if (arrInvoice == null) {
                        model.setModelMessage("Lỗi xảy ra!");
                        model.setModelCode(ErrorConstants.ERROR_COMMON);
                        UserController.getInstance().handleErrorModelEvent(model);
                    } else {
                        model.setModelData(arrInvoice);
                        model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
                    }
                    UserController.getInstance().handleModelEvent(model);
                } catch (Exception ex) {
                    model.setModelMessage(ex.getMessage());
                    model.setModelCode(ErrorConstants.ERROR_COMMON);
                    UserController.getInstance().handleErrorModelEvent(model);
                }
                break;
            }
            case ActionEventConstant.ACTION_SEARCH_DATA_INVOICE: {
                try {
                    Bundle bundle = (Bundle) e.viewData;
                    int typeFilter = bundle.getInt(Common.BUNDLE_KEY_FILTER);
                    if (typeFilter == 0) {
                        typeFilter += 1;
                    }
                    boolean typeSort = bundle.getBoolean(Common.BUNDLE_KEY_SORT);
                    String strSearch = bundle.getString(Common.BUNDLE_KEY_SEARCH);
                    strSearch = Helper.deAccent(strSearch.toLowerCase());
                    Log.e("Noi dung search", "" + strSearch);
                    String strSort = "";
                    if (typeSort) {
                        strSort = "DESC";
                    } else {
                        strSort = "ASC";
                    }
                    String query = "";

                    switch (typeFilter) {
                        case Common.FILTER_BY_NAME_CUSTOMER: {

                            query = "SELECT DISTINCT * FROM Invoice, Customer WHERE invoice.idCustomer = Customer.id and " +
                                    TbCustomer.CUSTOMER_TABLE + "." + TbCustomer.CUSTOMER_NameCus + " like '%" + strSearch + "%'" + "  ORDER BY " + TbCustomer.CUSTOMER_TABLE + "." + TbCustomer.CUSTOMER_Name + ", " + TbCustomer.CUSTOMER_TABLE + "." + TbCustomer.CUSTOMER_IdUser + " " + strSort;
                            break;
                        }
                        case Common.FILTER_BY_STATUS_PAYMENTED: {
                            query = "SELECT DISTINCT * FROM Invoice, Customer WHERE invoice.idCustomer = Customer.id and " +
                                    TbCustomer.CUSTOMER_TABLE + "." + TbCustomer.CUSTOMER_NameCus + " like '%" + strSearch + "%'" + "  ORDER BY " + TbInvoice.INVOICE_TABLE + "." + TbInvoice.INVOICE_Status + ", " + TbCustomer.CUSTOMER_TABLE + "." + TbCustomer.CUSTOMER_IdUser + " " + strSort;
                            break;
                        }
                        case Common.FILTER_BY_STATUS_NOT_PAYMENT: {
                            query = "SELECT DISTINCT * FROM Invoice, Customer WHERE invoice.idCustomer = Customer.id and " +
                                    TbCustomer.CUSTOMER_TABLE + "." + TbCustomer.CUSTOMER_NameCus + " like '%" + strSearch + "%'" + "  ORDER BY " + TbCustomer.CUSTOMER_TABLE + "." + TbCustomer.CUSTOMER_Name + ", " + TbCustomer.CUSTOMER_TABLE + "." + TbCustomer.CUSTOMER_IdUser + " " + strSort;
                            break;
                        }
                        default:
                            query = "SELECT DISTINCT * FROM Invoice, Customer WHERE invoice.idCustomer = Customer.id";
                            break;
                    }
                    Log.e("gia trị query:", "" + query);
                    ArrayList<HoaDon> arrInvoice = TbInvoice.getInstance(base).getAllCustomerQuery(query);
                    if (arrInvoice == null) {
                        model.setModelMessage("Lỗi xảy ra!");
                        model.setModelCode(ErrorConstants.ERROR_COMMON);
                        UserController.getInstance().handleErrorModelEvent(model);
                    } else {
                        model.setModelData(arrInvoice);
                        model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
                    }
                    UserController.getInstance().handleModelEvent(model);
                } catch (Exception ex) {
                    model.setModelMessage(ex.getMessage());
                    model.setModelCode(ErrorConstants.ERROR_COMMON);
                    UserController.getInstance().handleErrorModelEvent(model);
                }
                break;
            }
            case ActionEventConstant.ACTION_SEARCH_DATA_WITH_ID_CUSTOMER_INVOICE: {
                try {
                    Bundle bundle = (Bundle) e.viewData;
                    String strIdCustomer = bundle.getString(Common.BUNDLE_KEY_SEARCH_WITH_ID_CUSTOMER);
                    String strSearch = bundle.getString(Common.BUNDLE_KEY_SEARCH);
//                    strSearch = Helper.deAccent(strSearch.toLowerCase());
                    Log.e("Noi dung search", "" + strSearch);
                    String strSort = "";
                    strSort = "DESC";
                    String query = "";
                    query = "SELECT DISTINCT * FROM Invoice, Customer WHERE invoice.idCustomer = Customer.id AND invoice.idCustomer=" + strIdCustomer + " AND " +
                            TbCustomer.CUSTOMER_TABLE + "." + TbCustomer.CUSTOMER_NameCus + " like '%" + strSearch + "%'" + "  ORDER BY " + TbCustomer.CUSTOMER_TABLE + "." + TbCustomer.CUSTOMER_Name + ", " + TbCustomer.CUSTOMER_TABLE + "." + TbCustomer.CUSTOMER_IdUser + " " + strSort;
                    Log.e("gia trị query:", "" + query);
                    ArrayList<HoaDon> arrInvoice = TbInvoice.getInstance(base).getAllCustomerQuery(query);
                    if (arrInvoice == null) {
                        model.setModelMessage("Lỗi xảy ra!");
                        model.setModelCode(ErrorConstants.ERROR_COMMON);
                        UserController.getInstance().handleErrorModelEvent(model);
                    } else {
                        model.setModelData(arrInvoice);
                        model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
                    }
                    UserController.getInstance().handleModelEvent(model);
                } catch (Exception ex) {
                    model.setModelMessage(ex.getMessage());
                    model.setModelCode(ErrorConstants.ERROR_COMMON);
                    UserController.getInstance().handleErrorModelEvent(model);
                }
                break;
            }

            case ActionEventConstant.ACTION_SEARCH_FULL_DATA_INVOICE: {
                try {
                    Bundle bundle = (Bundle) e.viewData;
                    int typeFilter = bundle.getInt(Common.BUNDLE_KEY_FILTER);
                    String strSearch = bundle.getString(Common.BUNDLE_KEY_SEARCH);
                    String strFrom = bundle.getString(Common.BUNDLE_KEY_SEARCH_FROM);
                    String strTo = bundle.getString(Common.BUNDLE_KEY_SEARCH_TO);
                    String strSort = "";
                    String query = "";
                    switch (typeFilter) {
                        case Common.FILTER_BY_NAME_CUSTOMER: {

                            query = "SELECT DISTINCT * FROM  Invoice, Customer WHERE invoice.idCustomer = Customer.id and " +
                                    TbInvoice.INVOICE_TABLE + "." + TbInvoice.INVOICE_Number + " like '%" + strSearch + "%'" + " AND " + TbCustomer.CUSTOMER_TABLE + "." + TbCustomer.CUSTOMER_NameCus + " like '%" + strSearch + "%'" + "  ORDER BY " + TbCustomer.CUSTOMER_TABLE + "." + TbCustomer.CUSTOMER_Name + ", " + TbCustomer.CUSTOMER_TABLE + "." + TbCustomer.CUSTOMER_IdUser + " " + strSort;
                            break;
                        }
                        case Common.FILTER_BY_STATUS_PAYMENTED: {
                            query = "SELECT DISTINCT * FROM  Invoice, Customer WHERE invoice.idCustomer = Customer.id and " +
                                    TbInvoice.INVOICE_TABLE + "." + TbInvoice.INVOICE_Number + " like '%" + strSearch + "%'" + " AND " + TbCustomer.CUSTOMER_TABLE + "." + TbCustomer.CUSTOMER_NameCus + " like '%" + strSearch + "%'" + "  ORDER BY " + TbInvoice.INVOICE_TABLE + "." + TbInvoice.INVOICE_Status + " " + strSort;
                            break;
                        }
                        case Common.FILTER_BY_STATUS_NOT_PAYMENT: {
                            query = "SELECT DISTINCT * FROM  Invoice, Customer WHERE invoice.idCustomer = Customer.id and " +
                                    TbInvoice.INVOICE_TABLE + "." + TbInvoice.INVOICE_Number + " like '%" + strSearch + "%'" + " AND " + TbCustomer.CUSTOMER_TABLE + "." + TbCustomer.CUSTOMER_NameCus + " like '%" + strSearch + "%'" + "  ORDER BY " + TbCustomer.CUSTOMER_TABLE + "." + TbCustomer.CUSTOMER_Name + ", " + TbCustomer.CUSTOMER_TABLE + "." + TbCustomer.CUSTOMER_IdUser + " " + strSort;
                            break;
                        }
                        default:
                            query = "SELECT DISTINCT * FROM  Invoice, Customer WHERE invoice.idCustomer = Customer.id";
                            break;
                    }
                    Log.e("gia trị query:", "" + query);
                    ArrayList<HoaDon> arrInvoice = TbInvoice.getInstance(base).getAllCustomerQuery(query);
                    if (arrInvoice == null) {
                        model.setModelMessage("Lỗi xảy ra!");
                        model.setModelCode(ErrorConstants.ERROR_COMMON);
                        UserController.getInstance().handleErrorModelEvent(model);
                    } else {
                        model.setModelData(arrInvoice);
                        model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
                    }
                    UserController.getInstance().handleModelEvent(model);
                } catch (Exception ex) {
                    model.setModelMessage(ex.getMessage());
                    model.setModelCode(ErrorConstants.ERROR_COMMON);
                    UserController.getInstance().handleErrorModelEvent(model);
                }
                break;
            }
            case ActionEventConstant.ACTION_GET_ALL_TERM: {
                try {
                    ArrayList<Term> arrData = new ArrayList<>();
                    InvoicesServices service = new InvoicesServices();
                    JSONObject jsObject = service.requestAllTermInvoice();
                    if (jsObject != null) {
                        JSONArray arrObj = jsObject.getJSONArray("data");
                        Gson gson = new Gson();
                        for (int i = 0; i < arrObj.length(); i++) {
                            Term term = gson.fromJson(arrObj.get(i).toString(), Term.class);
                            arrData.add(term);
                        }
                    }
                    model.setModelData(arrData);
                    model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
                    UserController.getInstance().handleModelEvent(model);
                } catch (Exception ex) {
                    model.setModelMessage(ex.getMessage());
                    model.setModelCode(ErrorConstants.ERROR_COMMON);
                    UserController.getInstance().handleErrorModelEvent(model);
                }
                break;
            }

            case ActionEventConstant.ACTION_UPATE_STATUS_PAYMENT_INVOICE_NEW: {
                try {
                    ArrayList<InvoiceUpdatePaymentedRequest> arrInvoiceUpdate = Common.arrInvoiceUpdatePaymented;
                    InvoicesServices service = new InvoicesServices();
                    JSONObject jsObject = service.requestUpdatePaymentInvoice(arrInvoiceUpdate);

                    Gson gson = new Gson();
                    BaseReponse item = null;
                    if (jsObject != null) {
                        item = gson.fromJson(jsObject.toString(), BaseReponse.class);
                    }
                    if (item.getStatusCode() == Common.DATA_SUCCESS) {
                        model.setModelData(item);
                        model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
                    } else {
                        model.setModelData(item);
                        model.setModelCode(ErrorConstants.ERROR_DATA);
                    }
                    UserController.getInstance().handleModelEvent(model);
                } catch (Exception ex) {
                    model.setModelMessage(ex.getMessage());
                    model.setModelCode(ErrorConstants.ERROR_COMMON);
                    UserController.getInstance().handleErrorModelEvent(model);
                }
                break;
            }
            case ActionEventConstant.ACTION_UPDATE_STATUS_RETURN_INVOICE: {
                try {
                    Bundle bundel = (Bundle) e.viewData;
                    ArrayList<InvoiceUpdatePaymentedRequest> arrInvoiceUpdate = bundel.getParcelableArrayList(Common.BUNDLE_KEY_ARR_ITEM_REQUEST_UPADTE_STATUS_PAYMENT);
                    InvoicesServices service = new InvoicesServices();
                    JSONObject jsObject = service.updateStatusPaymentedToNotPaymentInvoice(arrInvoiceUpdate);

                    Gson gson = new Gson();
                    BaseReponse item = null;
                    if (jsObject != null) {
                        item = gson.fromJson(jsObject.toString(), BaseReponse.class);
                    }
                    if (item.getStatusCode() == Common.DATA_SUCCESS) {
                        model.setModelData(item);
                        model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
                    } else {
                        model.setModelData(item);
                        model.setModelCode(ErrorConstants.ERROR_DATA);
                    }
                    UserController.getInstance().handleModelEvent(model);
                } catch (Exception ex) {
                    model.setModelMessage(ex.getMessage());
                    model.setModelCode(ErrorConstants.ERROR_COMMON);
                    UserController.getInstance().handleErrorModelEvent(model);
                }
                break;
            }

            default:
                break;
        }

    }

    /**
     * parse du lieu catagory
     *
     * @return
     * @author: truonglt2
     * @return: ArrayList<Catagory>
     * @throws:
     */
//    private ArrayList<Term> getArrCatagory(Cursor cursor) {
//        ArrayList<Term> arrayList = new ArrayList<Term>();
//        if (cursor != null) {
//            cursor.moveToFirst();
//            while (cursor.isAfterLast() == false) {
//                Term item = new Term();
//                String id = cursor.getString(0);
//                String title = cursor.getString(1);
//                arrayList.add(item);
//                cursor.moveToNext();
//            }
//            cursor.close();
//        } else {
//            return null;
//        }
//        return arrayList;
//    }
    private ArrayList<TableNo> initDataDemo() {
        ArrayList<TableNo> tableNoArrayList = new ArrayList<>();
        TableNo tableNo = new TableNo();
        tableNo.setIdTable(1);
        tableNo.setCheckNo("ZDTEARA01");
        tableNo.setIdInvoice(1);
        tableNo.setIdImageTable("Duong dan 1");
        tableNo.setNumber_Inv("1237017321");
        TableNo tableNo1 = new TableNo();
        tableNo1.setIdTable(2);
        tableNo1.setCheckNo("ZDTEARA02");
        tableNo1.setIdInvoice(2);
        tableNo1.setIdImageTable("Duong dan 2");
        tableNo1.setNumber_Inv("1237017322");
        tableNoArrayList.add(tableNo);
        tableNoArrayList.add(tableNo1);
        return tableNoArrayList;
    }

    public InvoiceCadminBL getDataFromLocal(Context context, int fromIndex, int toIndex) {
        Map<String, ?> allInvoices = StoreInvoiceSharePreferences.getInstance(context).loadAllInvoicesShareReferences();
        ArrayList<InvoiceCadmin> prefDataList = new ArrayList<>();
        InvoiceCadminBL result = new InvoiceCadminBL();
        Gson gson = new Gson();
        for (int i = fromIndex; i <= toIndex; i++) {
            allInvoices.get(i).toString();
            String vlInv = allInvoices.get(i).toString();
            InvoiceCadmin item = gson.fromJson(vlInv, InvoiceCadmin.class);
            prefDataList.add(item);
        }
        result.setStatusCode(1);
        result.setArrData(prefDataList);
        return result;
    }

    void getAllInvDataFromLocal(Context context) {
        Map<String, ?> allInvoices = StoreInvoiceSharePreferences.getInstance(context).loadAllInvoicesShareReferences();
        List prefDataList = new ArrayList<>();
        Gson gson = new Gson();
        for (Map.Entry entry : allInvoices.entrySet()) {
//            entry.getKey();
            InvoiceCadmin item = gson.fromJson(entry.getValue().toString(), InvoiceCadmin.class);
            prefDataList.add(item);
        }
    }
}
