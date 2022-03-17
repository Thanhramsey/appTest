package com.vnpt.webservice;

import android.content.Context;
import android.util.Log;

import com.vnpt.common.Common;
import com.vnpt.common.ConstantsApp;
import com.vnpt.utils.StoreSharePreferences;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.Date;


public class SOAPWebservicePortal {

    public static String URL_SERVER_SOAP = "https://congtrinhdothigliadmindemo.vnpt-invoice.com.vn";
    private static final String TAG = "SOAPWebservicePortal";
    private static final String NAMESPACE = "http://tempuri.org/";

    private static String URL_PORTALSERVICE = URL_SERVER_SOAP+"/portalservice.asmx?wsdl";

    
    private static final String SOAP_ACTION_LISTINVBYCUS = "http://tempuri.org/listInvByCus";
    private static final String METHOD_LISTINVBYCUS = "listInvByCus";

    private static final String SOAP_ACTION_LISTINVBYCUSFKEYVNP = "http://tempuri.org/listInvByCusFkeyVNP";
    private static final String METHOD_LISTINVBYCUSFKEYVNP = "listInvByCusFkeyVNP";

    private static final String SOAP_ACTION_DOWNLOADINVNOPAY = "http://tempuri.org/downloadInvNoPay";
    private static final String METHOD_DOWNLOADINVNOPAY = "downloadInvNoPay";

    private static final String SOAP_ACTION_DOWNLOADINVFKEYNOPAY = "http://tempuri.org/downloadInvFkeyNoPay";
    private static final String METHOD_DOWNLOADINVFKEYNOPAY = "downloadInvFkeyNoPay";

    private static final String SOAP_ACTION_GETINVVIEWFKEYNOPAY = "http://tempuri.org/getInvViewFkeyNoPay";
    private static final String METHOD_GETINVVIEWFKEYNOPAY = "getInvViewFkeyNoPay";

    Context context;
    private static SOAPWebservicePortal instance;
    public SOAPWebservicePortal(String server)
    {
        this.URL_SERVER_SOAP = server;
        this.URL_PORTALSERVICE = URL_SERVER_SOAP+"/portalservice.asmx?WSDL";
    }
    public static synchronized SOAPWebservicePortal getInstance(Context context) {
        if (instance == null) {
            String strServer = StoreSharePreferences.getInstance(context).loadStringSavedPreferences(Common.KEY_FINAL_ADDRESS_SERVER);
            instance = new SOAPWebservicePortal(strServer);
        }
        return instance;
    }
    public String getWS_downloadInvNoPay(String invToken, String userService, String passService) {
        String ret = null;
        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_DOWNLOADINVNOPAY);
            // Set the property info for the to currency
            //Tham số default

            PropertyInfo userCName = new PropertyInfo();
            userCName.setName("userName");
            userCName.setValue(userService);
            userCName.setType(String.class);
            request.addProperty(userCName);

            PropertyInfo userCPass = new PropertyInfo();
            userCPass.setName("userPass");
            userCPass.setValue(passService);
            userCPass.setType(String.class);
            request.addProperty(userCPass);

            PropertyInfo pInvToken = new PropertyInfo();
            pInvToken.setName("invToken");
            pInvToken.setValue(invToken);
            pInvToken.setType(String.class);
            request.addProperty(pInvToken);


            //String url = "https://congtrinhdothigliadmindemo.vnpt-invoice.com.vn/publishservice.asmx?WSDL"
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL_PORTALSERVICE, 30000);
            try {
                androidHttpTransport.call(SOAP_ACTION_DOWNLOADINVNOPAY, envelope);
                //SoapPrimitive  resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();
//                SoapPrimitive resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();
                SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                System.out.println("Response:"+resultsRequestSOAP.toString());
                ret = resultsRequestSOAP.toString();
            } catch (Exception e) {
                Log.e(TAG, "getWS_ImportAndPublishInv"+e);
                e.printStackTrace();
            }
        } catch (Exception ex) {
            Log.e(TAG, "getWS_ImportAndPublishInv");
            ex.printStackTrace();
        }
        return ret;
    }
    public String getWS_downloadInvFkeyNoPay(String fKey, String userService, String passService) {
        String ret = null;
        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_DOWNLOADINVFKEYNOPAY);
            // Set the property info for the to currency
            //Tham số default

            PropertyInfo userCName = new PropertyInfo();
            userCName.setName("userName");
            userCName.setValue(userService);
            userCName.setType(String.class);
            request.addProperty(userCName);

            PropertyInfo userCPass = new PropertyInfo();
            userCPass.setName("userPass");
            userCPass.setValue(passService);
            userCPass.setType(String.class);
            request.addProperty(userCPass);

            PropertyInfo pFkey = new PropertyInfo();
            pFkey.setName("fkey");
            pFkey.setValue(fKey);
            pFkey.setType(String.class);
            request.addProperty(pFkey);


            //String url = "https://congtrinhdothigliadmindemo.vnpt-invoice.com.vn/publishservice.asmx?WSDL"
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL_PORTALSERVICE, 30000);
            try {
                androidHttpTransport.call(SOAP_ACTION_DOWNLOADINVFKEYNOPAY, envelope);
                //SoapPrimitive  resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();
                SoapPrimitive resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();
//                SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                System.out.println("Response:"+resultsRequestSOAP.toString());
                ret = resultsRequestSOAP.toString();
            } catch (Exception e) {
                Log.e(TAG, "getWS_ImportAndPublishInv"+e);
                e.printStackTrace();
            }
        } catch (Exception ex) {
            Log.e(TAG, "getWS_ImportAndPublishInv");
            ex.printStackTrace();
        }
        return ret;
    }
    public String getWS_getInvViewFkeyNoPay(String fKey, String userService, String passService) {
        String ret = null;
        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_GETINVVIEWFKEYNOPAY);
            // Set the property info for the to currency
            //Tham số default

            PropertyInfo userCName = new PropertyInfo();
            userCName.setName("userName");
            userCName.setValue(userService);
            userCName.setType(String.class);
            request.addProperty(userCName);

            PropertyInfo userCPass = new PropertyInfo();
            userCPass.setName("userPass");
            userCPass.setValue(passService);
            userCPass.setType(String.class);
            request.addProperty(userCPass);

            PropertyInfo pFkey = new PropertyInfo();
            pFkey.setName("fkey");
            pFkey.setValue(fKey);
            pFkey.setType(String.class);
            request.addProperty(pFkey);


            //String url = "https://congtrinhdothigliadmindemo.vnpt-invoice.com.vn/publishservice.asmx?WSDL"
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL_PORTALSERVICE, 30000);
            try {
                androidHttpTransport.call(SOAP_ACTION_GETINVVIEWFKEYNOPAY, envelope);
                //SoapPrimitive  resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();
                SoapPrimitive resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();
//                SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                System.out.println("Response:"+resultsRequestSOAP.toString());
                ret = resultsRequestSOAP.toString();
            } catch (Exception e) {
                Log.e(TAG, "getWS_ImportAndPublishInv"+e);
                e.printStackTrace();
            }
        } catch (Exception ex) {
            Log.e(TAG, "getWS_ImportAndPublishInv");
            ex.printStackTrace();
        }
        return ret;
    }
    public String getWS_listInvByCus(String strServer, String strPattern, String strSerial, String cusCode) {
        URL_SERVER_SOAP = strServer;
        URL_PORTALSERVICE =URL_SERVER_SOAP+"/publishservice.asmx?wsdl";
        String ret = null;
        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_LISTINVBYCUS);
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

            PropertyInfo pCusCode = new PropertyInfo();
            pCusCode.setName("cusCode");
            pCusCode.setValue(cusCode);
            pCusCode.setType(String.class);
            request.addProperty(pCusCode);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL_PORTALSERVICE, 30000);
            try {
                androidHttpTransport.call(SOAP_ACTION_LISTINVBYCUS, envelope);
                //SoapPrimitive  resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();
                SoapPrimitive resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();
//            SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                System.out.println("Response::"+resultsRequestSOAP.toString());
                ret = resultsRequestSOAP.toString();
            } catch (Exception e) {
                System.out.println("Error:"+e);
            }
        } catch (Exception ex) {
            Log.e(TAG, "getWS_listInvByCus");
            ex.printStackTrace();
        }
        return ret;
    }
    public String getWS_listInvByCusFkeyVNP() {
        String ret = null;
        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_LISTINVBYCUSFKEYVNP);
            // Set the property info for the to currency
            //Tham số default
            PropertyInfo userCName = new PropertyInfo();
            userCName.setName("userName");
            userCName.setValue(ConstantsApp.AUTHORIZE_WS_Account);
            userCName.setType(String.class);
            request.addProperty(userCName);

            PropertyInfo userCPass = new PropertyInfo();
            userCPass.setName("userPass");
            userCPass.setValue(ConstantsApp.AUTHORIZE_WS_Password);
            userCPass.setType(String.class);
            request.addProperty(userCPass);

            PropertyInfo fromDate = new PropertyInfo();
            fromDate.setName("fromDate");
            String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            fromDate.setValue(date);
            fromDate.setType(String.class);
            request.addProperty(fromDate);

            PropertyInfo toDate = new PropertyInfo();
            toDate.setName("toDate");
            toDate.setValue(date);
            toDate.setType(String.class);
            request.addProperty(toDate);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL_PORTALSERVICE, 30000);
            try {
                androidHttpTransport.call(SOAP_ACTION_LISTINVBYCUSFKEYVNP, envelope);
                //SoapPrimitive  resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();
                SoapPrimitive resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();
//            SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                System.out.println("Response::"+resultsRequestSOAP.toString());
                ret = resultsRequestSOAP.toString();
            } catch (Exception e) {
                System.out.println("Error:"+e);
            }
        } catch (Exception ex) {
            Log.e(TAG, "getWS_listInvByCus");
            ex.printStackTrace();
        }
        return ret;
    }
}
