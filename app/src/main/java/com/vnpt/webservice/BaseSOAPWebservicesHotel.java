package com.vnpt.webservice;

import android.util.Log;

import com.vnpt.common.ConstantsApp;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public abstract class BaseSOAPWebservicesHotel {

    public static String URL_SERVER_SOAP = "https://metropolehanoiadmin.vnpt-invoice.com.vn";
    private static final String TAG = "BaseSOAPWebservice";
    private static final String NAMESPACE = "http://tempuri.org/";
    private static String URL = URL_SERVER_SOAP+"/hotelservice.asmx?wsdl";
    private static String URL3 =URL_SERVER_SOAP+"/x2invoiceservice.asmx?WSDL";
    private static final String METHOD_GETCUS = "getCus";
    private static final String SOAP_ACTION_HOTELLOGIN = "http://tempuri.org/HotelLogin";
    private static final String METHOD_HOTELLOGIN = "HotelLogin";


//    private static final String SOAP_ACTION_LISTINVOICE = "http://tempuri.org/ListInvoice";
//    private static final String METHOD_LISTINVOICE = "ListInvoice";
    private static final String SOAP_ACTION_LISTINVOICE = "http://tempuri.org/HotelListInvoice";
    private static final String METHOD_LISTINVOICE = "HotelListInvoice";

    private static final String SOAP_ACTION_GETINVOICEBYFKEY = "http://tempuri.org/GetInvoiceByFkey";
    private static final String METHOD_GETINVOICEBYFKEY = "GetInvoiceByFkey";

    private static final String SOAP_ACTION_UPDATEANDPUBLISHINVBYFKEY = "http://tempuri.org/UpdateAndPublishInvByFkey";
    private static final String METHOD_UPDATEANDPUBLISHINVBYFKEY = "UpdateAndPublishInvByFkey";

    private static final String SOAP_ACTION_GETALLOUTLET = "http://tempuri.org/GetAllOutlet";
    private static final String METHOD_GETALLOUTLET = "GetAllOutlet";

    public String getWS_GetAllOutlet(String strServer) {
        URL_SERVER_SOAP = strServer;
        URL3 =URL_SERVER_SOAP+"/x2invoiceservice.asmx?WSDL";
        URL = URL_SERVER_SOAP+"/hotelservice.asmx?wsdl";
        String ret = null;
        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_GETALLOUTLET);
            // Set the property info for the to currency
            //Tham số default
            PropertyInfo userCName = new PropertyInfo();
            userCName.setName("username");
            userCName.setValue(ConstantsApp.AUTHORIZE_WS_Account);
            userCName.setType(String.class);
            request.addProperty(userCName);
            PropertyInfo userCPass = new PropertyInfo();
            userCPass.setName("password");
            userCPass.setValue(ConstantsApp.AUTHORIZE_WS_Password);
            userCPass.setType(String.class);
            request.addProperty(userCPass);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL3, 30000);
            try {
                androidHttpTransport.call(SOAP_ACTION_GETALLOUTLET, envelope);
                //SoapPrimitive  resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();
                SoapPrimitive resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();
//            SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                System.out.println("Response::"+resultsRequestSOAP.toString());
                ret = resultsRequestSOAP.toString();
            } catch (Exception e) {
                System.out.println("Error:"+e);
            }
        } catch (Exception ex) {
            Log.e(TAG, "getWS_GetAllOutlet");
            ex.printStackTrace();
        }
        return ret;
    }
    public String getWS_HotelLogin(String strUserName, String strUserPass, String strServer) {
        String ret = null;
        URL_SERVER_SOAP = strServer;
        URL3 =URL_SERVER_SOAP+"/x2invoiceservice.asmx?WSDL";
        URL = URL_SERVER_SOAP+"/hotelservice.asmx?wsdl";
        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_HOTELLOGIN);
            // Set the property info for the to currency
            PropertyInfo userName = new PropertyInfo();
            userName.setName("Account");
            userName.setValue(strUserName);
            userName.setType(String.class);
            request.addProperty(userName);

            PropertyInfo userPass = new PropertyInfo();
            userPass.setName("ACpass");
            userPass.setValue(strUserPass);
            userPass.setType(String.class);
            request.addProperty(userPass);
            //Tham số default
            PropertyInfo userCName = new PropertyInfo();
            userCName.setName("username");
            userCName.setValue(ConstantsApp.AUTHORIZE_WS_Account);
            userCName.setType(String.class);
            request.addProperty(userCName);
            PropertyInfo userCPass = new PropertyInfo();
            userCPass.setName("password");
            userCPass.setValue(ConstantsApp.AUTHORIZE_WS_Password);
            userCPass.setType(String.class);
            request.addProperty(userCPass);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL, 30000);
            try {
                androidHttpTransport.call(SOAP_ACTION_HOTELLOGIN, envelope);
                //SoapPrimitive  resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();
                SoapPrimitive resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();
//            SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                System.out.println("Response::"+resultsRequestSOAP.toString());
                ret = resultsRequestSOAP.toString();
            } catch (Exception e) {
                System.out.println("Error:"+e);
            }
        } catch (Exception ex) {
            Log.e(TAG, "getWS_HotelLogin");
            ex.printStackTrace();
        }
        return ret;
    }
    public String getWS_ListInvoice(String strToken, String strPattern, String strSerial, String strStatus, String strOutlet, String fromDate, String toDate, int startIndex, int endIndex) {
        String ret = null;
        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_LISTINVOICE);
            // Set the property info for the to currency
            PropertyInfo pPattern = new PropertyInfo();
            pPattern.setName("pattern");
            pPattern.setValue(strPattern);
            pPattern.setType(String.class);
            request.addProperty(pPattern);

            PropertyInfo pSerial = new PropertyInfo();
            pSerial.setName("serial");
            pSerial.setValue(strSerial);
            pSerial.setType(String.class);
            request.addProperty(pSerial);

            PropertyInfo pStatus = new PropertyInfo();
            pStatus.setName("status");
            pStatus.setValue(strStatus);
            pStatus.setType(String.class);
            request.addProperty(pStatus);
            PropertyInfo pOutlet = new PropertyInfo();
            pOutlet.setName("outlet");
            pOutlet.setValue(strOutlet);
            pOutlet.setType(String.class);
            request.addProperty(pOutlet);

            PropertyInfo pToDate = new PropertyInfo();
            pToDate.setName("toDate");
            pToDate.setValue(toDate);
            pToDate.setType(String.class);
            request.addProperty(pToDate);

            PropertyInfo pFromDate = new PropertyInfo();
            pFromDate.setName("fromDate");
            pFromDate.setValue(fromDate);
            pFromDate.setType(String.class);
            request.addProperty(pFromDate);

            PropertyInfo pageIndex = new PropertyInfo();
            pageIndex.setName("pageIndex");
            pageIndex.setValue(startIndex);
            pFromDate.setType(Integer.class);
            request.addProperty(pageIndex);

            PropertyInfo pageSize = new PropertyInfo();
            pageSize.setName("pageSize");
            pageSize.setValue(endIndex);
            pageSize.setType(Integer.class);
            request.addProperty(pageSize);

            //Tham số default
            PropertyInfo mtoken = new PropertyInfo();
            mtoken.setName("token");
            mtoken.setValue(strToken);
            mtoken.setType(String.class);
            request.addProperty(mtoken);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            try {
                androidHttpTransport.call(SOAP_ACTION_LISTINVOICE, envelope);
                //SoapPrimitive  resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();
                SoapPrimitive resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();
//            SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                System.out.println("Response::"+resultsRequestSOAP.toString());
                ret = resultsRequestSOAP.toString();
            } catch (Exception e) {
                System.out.println("Error:"+e);
            }
        } catch (Exception ex) {
            Log.e(TAG, "getWS_ListInvoice");
            ex.printStackTrace();
        }
        return ret;
    }

    public String getWS_GetInvoiceByFkey(String strToken, String strFkey, String strCheckNo) {
        String ret = null;
        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_GETINVOICEBYFKEY);
            // Set the property info for the to currency
            PropertyInfo pFkey = new PropertyInfo();
            pFkey.setName("fkey");
            pFkey.setValue(strFkey);
            pFkey.setType(String.class);
            request.addProperty(pFkey);

            PropertyInfo pCheckNo = new PropertyInfo();
            pCheckNo.setName("CheckNo");
            pCheckNo.setValue(strCheckNo);
            pCheckNo.setType(String.class);
            request.addProperty(pCheckNo);

            //Tham số default
            //Tham số default
            PropertyInfo mtoken = new PropertyInfo();
            mtoken.setName("token");
            mtoken.setValue(strToken);
            mtoken.setType(String.class);
            request.addProperty(mtoken);
//            PropertyInfo userName = new PropertyInfo();
//            userName.setName("Account");
//            userName.setValue(strUserName);
//            userName.setType(String.class);
//            request.addProperty(userName);
//
//            PropertyInfo userPass = new PropertyInfo();
//            userPass.setName("ACpass");
//            userPass.setValue(strUserPass);
//            userPass.setType(String.class);
//            request.addProperty(userPass);
//
//            PropertyInfo userCName = new PropertyInfo();
//            userCName.setName("username");
//            userCName.setValue(AUTHORIZE_WS_Account);
//            userCName.setType(String.class);
//            request.addProperty(userCName);
//            PropertyInfo userCPass = new PropertyInfo();
//            userCPass.setName("password");
//            userCPass.setValue(AUTHORIZE_WS_Password);
//            userCPass.setType(String.class);
//            request.addProperty(userCPass);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            try {
                androidHttpTransport.call(SOAP_ACTION_GETINVOICEBYFKEY, envelope);
                //SoapPrimitive  resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();
                SoapPrimitive resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();
//            SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                System.out.println("Response::"+resultsRequestSOAP.toString());
                ret = resultsRequestSOAP.toString();
            } catch (Exception e) {
                System.out.println("Error:"+e);
            }
        } catch (Exception ex) {
            Log.e(TAG, "getWS_ListInvoice");
            ex.printStackTrace();
        }
        return ret;
    }
    public String getWS_UpdateAndPublishInvByFkey(String strToken, String strFkey, String strCheckNo, String strXmlInvData,String strPattern, String strSerial) {
        String ret = null;
        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_UPDATEANDPUBLISHINVBYFKEY);
            // Set the property info for the to currency
            PropertyInfo pFkey = new PropertyInfo();
            pFkey.setName("strFkey");
            pFkey.setValue(strFkey);
            pFkey.setType(String.class);
            request.addProperty(pFkey);

            PropertyInfo pCheckNo = new PropertyInfo();
            pCheckNo.setName("strCheckNo");
            pCheckNo.setValue(strCheckNo);
            pCheckNo.setType(String.class);
            request.addProperty(pCheckNo);

            PropertyInfo pXmlInvData = new PropertyInfo();
            pXmlInvData.setName("xmlInvData");
            pXmlInvData.setValue(strXmlInvData);
            pXmlInvData.setType(String.class);
            request.addProperty(pXmlInvData);

            PropertyInfo pPattern = new PropertyInfo();
            pPattern.setName("pattern");
            pPattern.setValue(strPattern);
            pPattern.setType(String.class);
            request.addProperty(pPattern);

            PropertyInfo pSerial = new PropertyInfo();
            pSerial.setName("serial");
            pSerial.setValue(strSerial);
            pSerial.setType(String.class);
            request.addProperty(pSerial);

            //Tham số default
            PropertyInfo mtoken = new PropertyInfo();
            mtoken.setName("token");
            mtoken.setValue(strToken);
            mtoken.setType(String.class);
            request.addProperty(mtoken);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            try {
                androidHttpTransport.call(SOAP_ACTION_UPDATEANDPUBLISHINVBYFKEY, envelope);
                //SoapPrimitive  resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();
                SoapPrimitive resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();
//            SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                System.out.println("Response::"+resultsRequestSOAP.toString());
                ret = resultsRequestSOAP.toString();
            } catch (Exception e) {
                System.out.println("Error:"+e);
            }
        } catch (Exception ex) {
            Log.e(TAG, "getWS_ListInvoice");
            ex.printStackTrace();
        }
        return ret;
    }
}
