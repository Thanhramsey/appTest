package com.vnpt.webservice;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import com.vnpt.common.Common;
import com.vnpt.common.ConstantsApp;
import com.vnpt.utils.StoreSharePreferences;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class SOAPWebserviceExtRecipt {
    public SOAPWebserviceExtRecipt(String server)
    {
       this.URL_SERVER_SOAP = server;
       this.URL_EXTRECIPT_SERVICE = URL_SERVER_SOAP+"/ExtRecipt.asmx?WSDL";
    }
    Context context;
    private static SOAPWebserviceExtRecipt instance;
    /**
     * Get current instance of class
     *
     * @return
     */
    public static synchronized SOAPWebserviceExtRecipt getInstance(Context context) {
        if (instance == null) {
            String strServer = StoreSharePreferences.getInstance(context).loadStringSavedPreferences(Common.KEY_FINAL_ADDRESS_SERVER);
            instance = new SOAPWebserviceExtRecipt(strServer);
        }
        return instance;
    }
    private static final String TAG = "SOAPWebserviceExtRecipt";
    private static final String NAMESPACE = "http://tempuri.org/";
    private String URL_SERVER_SOAP = "https://congtrinhdothigliadmindemo.vnpt-invoice.com.vn";
    private String URL_EXTRECIPT_SERVICE = URL_SERVER_SOAP+"/publishservice.asmx?WSDL";



    // các hàm ws xử lý
    private static final String SOAP_ACTION_LISTINV = "http://tempuri.org/listInv";
    private static final String METHOD_LISTINV = "listInv";

    private static final String SOAP_ACTION_GETORG = "http://tempuri.org/getOrg";
    private static final String METHOD_GETORG = "getOrg";

    private static final String SOAP_ACTION_LISTFEE = "http://tempuri.org/listFee";
    private static final String METHOD_LISTFEE = "listFee";


    public SoapObject getWS_listInv(String strFromDate, String strToDate, String strPattern, String strSerial,int orgID, int pageSize, int pageIndex) {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        String ret = null;
        SoapObject resultsRequestSOAP = null;
        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_LISTINV);
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
            fromDate.setValue(strFromDate);
            fromDate.setType(String.class);
            request.addProperty(fromDate);

            PropertyInfo toDate = new PropertyInfo();
            toDate.setName("toDate");
            toDate.setValue(strToDate);
            toDate.setType(String.class);
            request.addProperty(toDate);

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

            PropertyInfo porgID = new PropertyInfo();
            porgID.setName("orgID");
            porgID.setValue(orgID);
            porgID.setType(Integer.class);
            request.addProperty(porgID);

            PropertyInfo pPageSize = new PropertyInfo();
            pPageSize.setName("pageSize");
            pPageSize.setValue(pageSize);
            pPageSize.setType(Integer.class);
            request.addProperty(pPageSize);

            PropertyInfo pPageIndex = new PropertyInfo();
            pPageIndex.setName("pageIndex");
            pPageIndex.setValue(pageIndex);
            pPageIndex.setType(Integer.class);
            request.addProperty(pPageIndex);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL_EXTRECIPT_SERVICE, 30000);
            try {
                androidHttpTransport.call(SOAP_ACTION_LISTINV, envelope);
//                SoapPrimitive resultsRequestSOAPx = (SoapPrimitive) envelope.getResponse();
                resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                System.out.println("Response:"+resultsRequestSOAP.toString());
                return resultsRequestSOAP;

            } catch (Exception e) {
                Log.e(TAG, "getWS_ImportAndPublishInv"+e);
                e.printStackTrace();
            }
        } catch (Exception ex) {
            Log.e(TAG, "getWS_ImportAndPublishInv");
            ex.printStackTrace();
        }
        return resultsRequestSOAP;
    }

    public SoapObject getWS_getOrg(String userGet) {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        String ret = null;
        SoapObject resultsRequestSOAP = null;
        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_GETORG);
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

            PropertyInfo strUserGet = new PropertyInfo();
            strUserGet.setName("userGet");
            strUserGet.setValue(userGet);
            strUserGet.setType(String.class);
            request.addProperty(strUserGet);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL_EXTRECIPT_SERVICE, 30000);
            try {
                androidHttpTransport.call(SOAP_ACTION_GETORG, envelope);
                //SoapPrimitive  resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();
                resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                System.out.println("Response:"+resultsRequestSOAP.toString());
                return resultsRequestSOAP;

            } catch (Exception e) {
                Log.e(TAG, "getWS_getOrg"+e);
                e.printStackTrace();
            }
        } catch (Exception ex) {
            Log.e(TAG, "getWS_getOrg");
            ex.printStackTrace();
        }
        return resultsRequestSOAP;
    }
    public SoapObject getWS_listFee() {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        String ret = null;
        SoapObject resultsRequestSOAP = null;
        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_LISTFEE);
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


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL_EXTRECIPT_SERVICE, 30000);
            try {
                androidHttpTransport.call(SOAP_ACTION_LISTFEE, envelope);
//                SoapPrimitive resultsRequestSOAPx = (SoapPrimitive) envelope.getResponse();
                resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                System.out.println("Response:"+resultsRequestSOAP.toString());
                return resultsRequestSOAP;

            } catch (Exception e) {
                Log.e(TAG, "getWS_listFee"+e);
                e.printStackTrace();
            }
        } catch (Exception ex) {
            Log.e(TAG, "getWS_listFee");
            ex.printStackTrace();
        }
        return resultsRequestSOAP;
    }

}
