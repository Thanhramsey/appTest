package com.vnpt.webservice;

import android.content.Context;
import android.util.Log;

import com.vnpt.common.Common;
import com.vnpt.utils.StoreSharePreferences;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class SOAPWebserviceBusiness {

    public static String URL_SERVER_SOAP = "https://congtrinhdothigliadmindemo.vnpt-invoice.com.vn";
    private static final String TAG = "BaseSOAPWebservice";
    private static final String NAMESPACE = "http://tempuri.org/";

    private static String URL_BUSINESSSERVICE = URL_SERVER_SOAP + "/businessservice.asmx?wsdl";

    private static final String SOAP_ACTION_CONFIRMPAYMENTFKEY = "http://tempuri.org/confirmPaymentFkey";
    private static final String METHOD_CONFIRMPAYMENTFKEY = "confirmPaymentFkey";

    private static final String SOAP_ACTION_UNCONFIRMPAYMENTFKEY = "http://tempuri.org/UnConfirmPaymentFkey";
    private static final String METHOD_UNCONFIRMPAYMENTFKEY = "UnConfirmPaymentFkey";


    private static SOAPWebserviceBusiness instance;

    public SOAPWebserviceBusiness(String server) {
        this.URL_SERVER_SOAP = server;
        this.URL_BUSINESSSERVICE = URL_SERVER_SOAP + "/businessservice.asmx?WSDL";
    }

    public static synchronized SOAPWebserviceBusiness getInstance(Context context) {
        if (instance == null) {
            String strServer = StoreSharePreferences.getInstance(context).loadStringSavedPreferences(Common.KEY_FINAL_ADDRESS_SERVER);
            instance = new SOAPWebserviceBusiness(strServer);
        }
        return instance;
    }


    public String getWS_confirmPaymentFkey(String fKey, String userService, String passService) {
        String ret = null;
        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_CONFIRMPAYMENTFKEY);
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
            pFkey.setName("lstFkey");
            pFkey.setValue(fKey);
            pFkey.setType(String.class);
            request.addProperty(pFkey);


            //String url = "https://congtrinhdothigliadmindemo.vnpt-invoice.com.vn/publishservice.asmx?WSDL"
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL_BUSINESSSERVICE, 30000);
            try {
                androidHttpTransport.call(SOAP_ACTION_CONFIRMPAYMENTFKEY, envelope);
                //SoapPrimitive  resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();
                SoapPrimitive resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();
//                SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                System.out.println("Response:" + resultsRequestSOAP.toString());
                ret = resultsRequestSOAP.toString();
            } catch (Exception e) {
                Log.e(TAG, "getWS_ImportAndPublishInv" + e);
                e.printStackTrace();
            }
        } catch (Exception ex) {
            Log.e(TAG, "getWS_ImportAndPublishInv");
            ex.printStackTrace();
        }
        return ret;
    }

    public String getWS_unConfirmPaymentFkey(String fKey, String userService, String passService) {
        String ret = null;
        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_CONFIRMPAYMENTFKEY);
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
            pFkey.setName("lstFkey");
            pFkey.setValue(fKey);
            pFkey.setType(String.class);
            request.addProperty(pFkey);


            //String url = "https://congtrinhdothigliadmindemo.vnpt-invoice.com.vn/publishservice.asmx?WSDL"
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL_BUSINESSSERVICE, 30000);
            try {
                androidHttpTransport.call(SOAP_ACTION_CONFIRMPAYMENTFKEY, envelope);
                //SoapPrimitive  resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();
                SoapPrimitive resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();
//                SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                System.out.println("Response:" + resultsRequestSOAP.toString());
                ret = resultsRequestSOAP.toString();
            } catch (Exception e) {
                Log.e(TAG, "getWS_ImportAndPublishInv" + e);
                e.printStackTrace();
            }
        } catch (Exception ex) {
            Log.e(TAG, "getWS_ImportAndPublishInv");
            ex.printStackTrace();
        }
        return ret;
    }
}
