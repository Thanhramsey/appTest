package com.vnpt.webservice;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.vnpt.common.Common;
import com.vnpt.common.ConstantsApp;
import com.vnpt.dto.FeeInvoice;
import com.vnpt.dto.InvoiceCadmin;
import com.vnpt.dto.InvoiceCadminBL;
import com.vnpt.dto.InvoiceHDDTDetails;
import com.vnpt.dto.Outlet;
import com.vnpt.dto.ProductInvoiceDetails;
import com.vnpt.dto.User;
import com.vnpt.utils.StoreSharePreferences;
import com.vnpt.webservice.SoapUntils.XMLParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.ksoap2.serialization.SoapObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class AppServices {
    Context mcontext;

    public AppServices(Context context) {
        this.mcontext = context;
    }

    public String login(String username, String password) {
        return SOAPWebservicePublish.getInstance(mcontext).getWS_ImportAndPublishInv(username, password, "pattern", "serial", 0, "strXmlInvData");
    }

    public int getTotalInvCurrentDate(Context context, String userName) {
        int count = 0;
        String strResult = SOAPWebservicePortal.getInstance(mcontext).getWS_listInvByCusFkeyVNP();

        if (strResult.equals("<Data></Data>")) {
            return count;
        }

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new ByteArrayInputStream(strResult.getBytes()));
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("Item");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    if (eElement.getElementsByTagName("cusname")
                            .item(0)
                            .getTextContent().equals(userName)
                            && eElement.getElementsByTagName("status")
                            .item(0)
                            .getTextContent().equals("1")) {
                        count++;
                    }
                }
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }

    public InvoiceCadminBL listInvoice(String strFromDate, String strToDate, String strPattern, String strSerial, int orgID, int pageSize, int pageIndex) {
        InvoiceCadminBL invoiceCadminBL = new InvoiceCadminBL();
        SoapObject objectResult = SOAPWebserviceExtRecipt.getInstance(mcontext).getWS_listInv(strFromDate, strToDate, strPattern, strSerial, orgID, pageSize, pageIndex);
        if (objectResult != null) {
            String object = (String) objectResult.getPropertyAsString(0);
            if (object.contains("ERR:")) {
                invoiceCadminBL.setStatusCode(0);
            } else {
                invoiceCadminBL.setStatusCode(1);
                ArrayList<InvoiceCadmin> arrData = new ArrayList<>();
                arrData = parseDataListInv(object);
                invoiceCadminBL.setArrData(arrData);
            }
            Object totalRecords = objectResult.getProperty(1);
            int total = Integer.valueOf(totalRecords.toString());
            invoiceCadminBL.setTotalRecords(total);
            return invoiceCadminBL;
        }
        return invoiceCadminBL;
    }

    public String listOrgInvoice(String userLogin) {
        SoapObject objectResult = SOAPWebserviceExtRecipt.getInstance(mcontext).getWS_getOrg(userLogin);
        if (objectResult != null) {
            //String object = (String) objectResult.getPropertyAsString(0);
            String object = objectResult.getProperty(0).toString();
            if (object.contains("ERR:")) {

            } else {

            }
        }
        return "";
    }

    public List<FeeInvoice> listFeeInvoice() {
        List<FeeInvoice> list = new ArrayList<FeeInvoice>();
        SoapObject objectResult = SOAPWebserviceExtRecipt.getInstance(mcontext).getWS_listFee();
        if (objectResult != null) {
            String object = (String) objectResult.getPropertyAsString(0);
            if (object.contains("ERR:")) {

            } else {
                list = parseDataListFee(object);
            }
        }
        return list;
    }

    public String importAndPublishInv(String username, String password, String strPattern, String strSerial, int convert, String strXmlInvData) {
        return SOAPWebservicePublish.getInstance(mcontext).getWS_ImportAndPublishInv(username, password, strPattern, strSerial, convert, strXmlInvData);
    }

    public String downloadInvNoPay(String invToken) {
        return SOAPWebservicePortal.getInstance(mcontext).getWS_downloadInvNoPay(invToken, ConstantsApp.AUTHORIZE_WS_Account, ConstantsApp.AUTHORIZE_WS_Password);
    }

    public String downloadInvFkeyNoPay(String fKey) {
        return SOAPWebservicePortal.getInstance(mcontext).getWS_downloadInvFkeyNoPay(fKey, ConstantsApp.AUTHORIZE_WS_Account, ConstantsApp.AUTHORIZE_WS_Password);
    }

    public String getInvViewFkeyNoPay(String fKey) {
        return SOAPWebservicePortal.getInstance(mcontext).getWS_getInvViewFkeyNoPay(fKey, ConstantsApp.AUTHORIZE_WS_Account, ConstantsApp.AUTHORIZE_WS_Password);
    }

    public String confirmPaymentFkey(String fKey) {
        return SOAPWebserviceBusiness.getInstance(mcontext).getWS_confirmPaymentFkey(fKey, ConstantsApp.AUTHORIZE_WS_Account, ConstantsApp.AUTHORIZE_WS_Password);
    }

    public String unConfirmPaymentFkey(String fKey) {
        return SOAPWebserviceBusiness.getInstance(mcontext).getWS_unConfirmPaymentFkey(fKey, ConstantsApp.AUTHORIZE_WS_Account, ConstantsApp.AUTHORIZE_WS_Password);
    }


    public void parseXMLGesCus(String strResult) {
        User user = new User();

        XMLParser xmlParser = new XMLParser();
        Document doc = xmlParser.getDomElement(strResult);
        NodeList nodes = doc.getElementsByTagName("Data");
        Element element = (Element) nodes.item(0);
        NodeList childNodes = element.getChildNodes();
        /** childNodes = 6
         * <![CDATA[<Data>
         <code>TRUONGLT</code>
         <name><![CDATA[Lê Tấn Trương]]]]>><![CDATA[</name>
         <address><![CDATA[Đà Nẵng]]]]>><![CDATA[</address>
         <phone></phone>
         <taxcode></taxcode>
         <email>lttruong@vnpt.vn</email>
         </Data>]]>
         * */
        try {
            Element elementCode = (Element) childNodes.item(0);
            String code = xmlParser.getCharacterDataFromElement(elementCode);
            user.setMa_kh(code);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            Element elementName = (Element) childNodes.item(1);
            String name = xmlParser.getCharacterDataFromElement(elementName);
            user.setName(name);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            Element elementAddress = (Element) childNodes.item(2);
            String address = xmlParser.getCharacterDataFromElement(elementAddress);
            user.setAddress(address);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            Element elementphone = (Element) childNodes.item(3);
            String phone = xmlParser.getCharacterDataFromElement(elementphone);
            user.setPhone(phone);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            Element elementtaxcode = (Element) childNodes.item(4);
            String taxcode = xmlParser.getCharacterDataFromElement(elementtaxcode);
            user.setTaxcode(taxcode);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            Element elementemail = (Element) childNodes.item(5);
            String email = xmlParser.getCharacterDataFromElement(elementemail);
            user.setEmail(email);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Common.userInfo = user;
    }

    public InvoiceHDDTDetails parseDetaisInvoice(String strResult) {
        InvoiceHDDTDetails invoiceHDDTDetails = new InvoiceHDDTDetails();
//		String xmlString;  // some XML String previously created
//		XmlToJson xmlToJson = new XmlToJson.Builder(strResult).build();
        String xmlRecords = "<data><terminal_id>1000099999</terminal_id><merchant_id>10004444</merchant_id><merchant_info>Mc Donald's - Abdoun</merchant_info></data>";
        Log.d("XML1-", strResult);
        Gson gson = new Gson();
        JSONObject jsonObj = null;
        try {
            jsonObj = XML.toJSONObject(strResult);
//			JSONObject jsInv = jsonObj.getJSONObject("Inv");
            JSONObject jsInvoice = jsonObj.getJSONObject("Invoice");
            JSONObject jsInvoiceContent = jsInvoice.getJSONObject("Content");
//			String keyInvoice = jsInvoice.getString("key");
            try {
                if (jsonObj != null) {
                    String test = jsInvoiceContent.toString();
                    Log.e("hsContent", test);
                    invoiceHDDTDetails = gson.fromJson(jsInvoiceContent.toString(), InvoiceHDDTDetails.class);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                JSONObject jsonObject = jsInvoiceContent.getJSONObject("Products");
                JSONArray jsArr = jsonObject.getJSONArray("Product");
                ArrayList<ProductInvoiceDetails> arrProductInvoiceDetails = new ArrayList<>();
                for (int i = 0; i < jsArr.length(); i++) {
                    ProductInvoiceDetails item = gson.fromJson(jsArr.get(i).toString(), ProductInvoiceDetails.class);
                    arrProductInvoiceDetails.add(item);
                }
                invoiceHDDTDetails.setArrayListProduct(arrProductInvoiceDetails);
            } catch (Exception ex) {
                ex.printStackTrace();
                JSONObject jsArr = jsInvoiceContent.getJSONObject("Products");
                JSONObject jsonObject = jsArr.getJSONObject("Product");
                ArrayList<ProductInvoiceDetails> arrProductInvoiceDetails = new ArrayList<>();
                ProductInvoiceDetails item = gson.fromJson(jsonObject.toString(), ProductInvoiceDetails.class);
                arrProductInvoiceDetails.add(item);
                invoiceHDDTDetails.setArrayListProduct(arrProductInvoiceDetails);

            }

        } catch (JSONException e) {
            Log.e("JSON exception", e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            Log.e("JSON exception", e.getMessage());
            e.printStackTrace();
        }
        Log.d("XML1-", jsonObj.toString());
        Log.d("invoiceHDDTDetails-", invoiceHDDTDetails.toString());
        return invoiceHDDTDetails;
    }

    public ArrayList<InvoiceCadmin> parseDataListInv(String strResult) {
        Log.d("XML1-", strResult);
        Gson gson = new Gson();
        JSONObject jsonObj = null;
        ArrayList<InvoiceCadmin> arrInvoiceCadmin = new ArrayList<>();
        if (strResult.equals("<Data></Data>")) {
            return arrInvoiceCadmin;
        }
        try {
            //JSONObject jsonInvoice = new JSONObject(strResult);
            jsonObj = XML.toJSONObject(strResult);
            JSONObject jsInvoice = jsonObj.getJSONObject("Data");
            try {
                JSONArray jsContent = jsInvoice.getJSONArray("Item");
                try {
                    for (int i = 0; i < jsContent.length(); i++) {
                        InvoiceCadmin item = gson.fromJson(jsContent.get(i).toString(), InvoiceCadmin.class);
                        if (item.getUnitCurrency() == null) {
                            item.setUnitCurrency("");
                        }
                        if (item.getCusAddress() == null) {
                            item.setCusAddress("");
                        }
                        arrInvoiceCadmin.add(item);
                    }
                    return arrInvoiceCadmin;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } catch (Exception ex)// nếu là lỗi 1 item
            {
                JSONObject jsContent = jsInvoice.getJSONObject("Item");
                try {
                    InvoiceCadmin item = gson.fromJson(jsContent.toString(), InvoiceCadmin.class);
                    if (item.getUnitCurrency() == null) {
                        item.setUnitCurrency("");
                    }
                    if (item.getCusAddress() == null) {
                        item.setCusAddress("");
                    }
                    arrInvoiceCadmin.add(item);
                    return arrInvoiceCadmin;
                } catch (Exception exx) {
                    exx.printStackTrace();
                }
            }
        } catch (JSONException e) {
            Log.e("JSON exception", e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Outlet> parseDataListOultet(String strResult) {
        Log.d("XML1-", strResult);
        Gson gson = new Gson();
        JSONObject jsonObj = null;
        try {
            jsonObj = XML.toJSONObject(strResult);
            JSONObject jsInvoice = jsonObj.getJSONObject("Data");
            JSONArray jsContent = jsInvoice.getJSONArray("Item");
            try {
                ArrayList<Outlet> arrInvoiceCadmin = new ArrayList<>();
                for (int i = 0; i < jsContent.length(); i++) {
                    Outlet item = gson.fromJson(jsContent.get(i).toString(), Outlet.class);
                    arrInvoiceCadmin.add(item);
                }
                return arrInvoiceCadmin;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (JSONException e) {
            Log.e("JSON exception", e.getMessage());
            try {
                jsonObj = XML.toJSONObject(strResult);
                JSONObject jsInvoice = jsonObj.getJSONObject("Data");
                JSONObject jsContent = jsInvoice.getJSONObject("Item");
                try {
                    ArrayList<Outlet> arrInvoiceCadmin = new ArrayList<>();
                    Outlet item = gson.fromJson(jsContent.toString(), Outlet.class);
                    arrInvoiceCadmin.add(item);
                    return arrInvoiceCadmin;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } catch (Exception ex) {

            }

            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String parseDataGetStringDownloadHTMLInv(String strResult) {
        String strHTML = "";
        XMLParser xmlParser = new XMLParser();
        Document doc = xmlParser.getDomElement(strResult);
        NodeList nodes = doc.getChildNodes();
        Element element = (Element) nodes.item(0);
        strHTML = xmlParser.getCharacterDataFromElement(element);
        return strHTML;
    }

    public ArrayList<FeeInvoice> parseDataListFee(String strResult) {
        Log.d("XML1-", strResult);
        Gson gson = new Gson();
        JSONObject jsonObj = null;
        ArrayList<FeeInvoice> arrInvoiceCadmin = new ArrayList<>();
        if (strResult.equals("<Data></Data>")) {
            return arrInvoiceCadmin;
        }
        try {
            //JSONObject jsonInvoice = new JSONObject(strResult);
            jsonObj = XML.toJSONObject(strResult);
            JSONObject jsInvoice = jsonObj.getJSONObject("Data");
            try {
                JSONArray jsContent = jsInvoice.getJSONArray("Item");
                try {
                    for (int i = 0; i < jsContent.length(); i++) {
                        FeeInvoice item = new FeeInvoice();
                        JSONObject object = jsContent.getJSONObject(i);
                        if (object.get("cusCode") != null) {
                            item.setName(object.getString("cusCode"));
                        }
                        if (object.get("Fkey") != null) {
                            item.setFee(object.getDouble("Fkey"));
                        }
                        if (object.get("publishDate") != null) {
                            item.setGroup(object.getInt("publishDate"));
                        }
                        if (object.get("serial") != null) {
                            item.setId(object.getInt("serial"));
                        }
                        arrInvoiceCadmin.add(item);
                    }
                    return arrInvoiceCadmin;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } catch (Exception ex)// nếu là lỗi 1 item
            {
                JSONObject object = jsInvoice.getJSONObject("Item");
                try {
                    FeeInvoice item = new FeeInvoice();
                    if (object.get("cusCode") != null) {
                        item.setName(object.getString("cusCode"));
                    }
                    if (object.get("Fkey") != null) {
                        item.setFee(object.getDouble("Fkey"));
                    }
                    if (object.get("publishDate") != null) {
                        item.setGroup(object.getInt("publishDate"));
                    }
                    if (object.get("serial") != null) {
                        item.setId(object.getInt("serial"));
                    }
                    arrInvoiceCadmin.add(item);
                    return arrInvoiceCadmin;
                } catch (Exception exx) {
                    exx.printStackTrace();
                }
            }
        } catch (JSONException e) {
            Log.e("JSON exception", e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
