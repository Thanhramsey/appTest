package com.vnpt.webservice;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.vnpt.common.Common;
import com.vnpt.dto.InvoiceUpdatePaymentedRequest;
import com.vnpt.utils.Helper;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InvoicesServices extends BaseWebservice{


	public InvoicesServices()
	{
		Common.AUTHORIZE_STRING = Helper.getB64Auth();
		Log.e("co vo day  Common.AUTHORIZE_STRING", Common.AUTHORIZE_STRING + "");
	}
	public JSONObject requestListDataInvoice(String idStaff, String strSearch, String idky,String filterType, String page, String pageSize)
    {
		String wsUrl = URL_SERVER + INVOICE_API_LIST_INVOICE_BY_USER;
		String errorLog = "Error while requestListDataInvoice";
		Log.e("wsUrl", wsUrl);
		List<NameValuePair> listParams = new ArrayList<>(6);
		listParams.add(new BasicNameValuePair("idStaff", idStaff));
		listParams.add(new BasicNameValuePair("strSearch", strSearch));
		listParams.add(new BasicNameValuePair("idKy", idky));
		listParams.add(new BasicNameValuePair("filterType", filterType));
		listParams.add(new BasicNameValuePair("page", page));
		listParams.add(new BasicNameValuePair("pageSize", pageSize));
		return doGetJSONObjPOST(wsUrl, errorLog, listParams);
    }
	public JSONObject requestDetailInvoice(int idInvoice, int idCustomer)
	{
		String wsUrl = URL_SERVER + INVOICE_API_DETAIL_INVOICE_BY_USER;
		String errorLog = "Error while requestDetailInvoice";
		Log.e("wsUrl", wsUrl);
		List<NameValuePair> listParams = new ArrayList<>(2);
		listParams.add(new BasicNameValuePair("idInvoice", ""+idInvoice));
		listParams.add(new BasicNameValuePair("idCustomer", ""+idCustomer));
		return doGetJSONObjPOST(wsUrl, errorLog, listParams);
	}
	public JSONObject requestAllTermInvoice()
	{
		String wsUrl = URL_SERVER + INVOICE_API_GET_ALL_TERM;
		String errorLog = "Error while requestAllTermInvoice";
		Log.e("wsUrl", wsUrl);
		return doGetJSONObj(wsUrl, errorLog);
	}
	public JSONObject requestUpdatePaymentInvoice(ArrayList<InvoiceUpdatePaymentedRequest> arr)
	{
		String wsUrl = URL_SERVER + INVOICE_API_UPDATE_STATUS_PAYMENTED_INVOICE;
		String errorLog = "Error while requestDetailInvoice";
		Log.e("wsUrl", wsUrl);
		Gson gson = new GsonBuilder().create();
		JsonArray myCustomArray = gson.toJsonTree(arr).getAsJsonArray();
		String strArrJS = myCustomArray.toString();
		return doPostObjectPOST(wsUrl, errorLog, strArrJS);
	}
	public JSONObject updateStatusPaymentedToNotPaymentInvoice(ArrayList<InvoiceUpdatePaymentedRequest> arr)
	{
		String wsUrl = URL_SERVER + INVOICE_API_UPDATE_STATUS_RETURN_INVOICE;
		String errorLog = "Error while requestDetailInvoice";
		Log.e("wsUrl", wsUrl);
		Gson gson = new GsonBuilder().create();
		JsonArray myCustomArray = gson.toJsonTree(arr).getAsJsonArray();
		String strArrJS = myCustomArray.toString();
		return doPostObjectPOST(wsUrl, errorLog, strArrJS);
	}
}
