package com.vnpt.webservice;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.vnpt.common.Common;
import com.vnpt.common.ConstantsApp;
import com.vnpt.dto.InvoiceHDDTDetails;
import com.vnpt.dto.InvoiceHDDTTG;
import com.vnpt.dto.ProductInvoiceDetails;
import com.vnpt.dto.User;
import com.vnpt.utils.StoreSharePreferences;
import com.vnpt.webservice.SoapUntils.XMLParser;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MetropoleServices extends BaseWebservice{
	public static MetropoleServices instance;
	Context mcontext;

	public MetropoleServices(Context context)
	{
		this.mcontext = context;
//		Common.AUTHORIZE_STRING = Helper.getB64Auth();
	}
	public int login(String username, String password)
    {
		String URL_SERVER_ = StoreSharePreferences.getInstance(mcontext).loadStringServerSavedPreferences(Common.KEY_FINAL_ADDRESS_SERVER);
		String wsUrl = URL_SERVER_ + USER_API_LOGIN;
		String errorLog = "Error while login";
		Log.e("wsUrl", wsUrl);
		List<NameValuePair> listParams = new ArrayList<>(2);
		listParams.add(new BasicNameValuePair("Username", username));
		listParams.add(new BasicNameValuePair("Password", password));

		return doGetIntPOST_Metropole(wsUrl, errorLog, listParams);
    }
	public int updateSignedMobile(int idInvoice, String pathImgSignedServer, String pathImgSignedMobile)
	{
		String URL_SERVER_ = StoreSharePreferences.getInstance(mcontext).loadStringServerSavedPreferences(Common.KEY_FINAL_ADDRESS_SERVER);
		String wsUrl = URL_SERVER_ + USER_API_UPDATE_SIGNED_MOBILE;
		String errorLog = "Error while updateSignedMobile";
		Log.e("wsUrl", wsUrl);
		List<NameValuePair> listParams = new ArrayList<>(5);
		listParams.add(new BasicNameValuePair("IdInvoice", ""+idInvoice));
		listParams.add(new BasicNameValuePair("PathImgSignedServer", pathImgSignedServer));
		listParams.add(new BasicNameValuePair("PathImgSignedMobile", pathImgSignedMobile));
		listParams.add(new BasicNameValuePair("WSAccount", ConstantsApp.AUTHORIZE_WS_Account));
		listParams.add(new BasicNameValuePair("WSPassword", ConstantsApp.AUTHORIZE_WS_Password));
		return doGetIntPOST_Metropole(wsUrl, errorLog, listParams);
	}
	public JSONObject getInforUser(String username, String password)
	{
		String URL_SERVER_ = StoreSharePreferences.getInstance(mcontext).loadStringServerSavedPreferences(Common.KEY_FINAL_ADDRESS_SERVER);
		String wsUrl = URL_SERVER_ + USER_API_INFOR;
		String errorLog = "Error while login";
		List<NameValuePair> listParams = new ArrayList<>(2);
		listParams.add(new BasicNameValuePair("Username", username));
		listParams.add(new BasicNameValuePair("Password", password));
		return doGetJSONObjPOST(wsUrl, errorLog, listParams);
	}
	public JSONArray requestListDataInvoice(int statusInv, String fromDate, String toDate, String pageNbr, String pageSize)
	{
		String URL_SERVER_ = StoreSharePreferences.getInstance(mcontext).loadStringServerSavedPreferences(Common.KEY_FINAL_ADDRESS_SERVER);
		String wsUrl = URL_SERVER_ + INVOICE_API_LIST_INVOICE_BY_USER;
		String errorLog = "Error while requestListDataInvoice";

		wsUrl += "?statusInv="+ statusInv;
		wsUrl += "&fromDate="+ fromDate;
		wsUrl += "&toDate="+ toDate;
		wsUrl += "&pageNbr="+ pageNbr;
		wsUrl += "&pageSize="+ pageSize;
		Log.e("wsUrl", wsUrl);
		return doGet(wsUrl, errorLog);
	}
	public JSONArray requestListOutletName()
	{
		String URL_SERVER_ = StoreSharePreferences.getInstance(mcontext).loadStringServerSavedPreferences(Common.KEY_FINAL_ADDRESS_SERVER);
		String wsUrl = URL_SERVER_ + INVOICE_API_LIST_OUTLET_NAME;
		String errorLog = "Error while requestListDataInvoice";


		Log.e("wsUrl", wsUrl);
		return doGetMetro(wsUrl, errorLog);
	}

	public JSONArray requestListDataInvoiceCuscode(String outletName, String fromDate, String toDate)
	{
		String URL_SERVER_ = StoreSharePreferences.getInstance(mcontext).loadStringServerSavedPreferences(Common.KEY_FINAL_ADDRESS_SERVER);
		String wsUrl = URL_SERVER_ + INVOICE_API_LIST_INVOICE_BY_DATE;
		String errorLog = "Error while requestListDataInvoice";


		wsUrl += "?fromDate="+ fromDate;
		wsUrl += "&toDate="+ toDate;
		wsUrl += "&outletName="+ outletName;
		Log.e("wsUrl", wsUrl);
		return doGet(wsUrl, errorLog);
	}
	public JSONArray requestListDataInvoiceSignByDateCuscode(String outletName, String fromDate, String toDate)
	{
		String URL_SERVER_ = StoreSharePreferences.getInstance(mcontext).loadStringServerSavedPreferences(Common.KEY_FINAL_ADDRESS_SERVER);
		String wsUrl = URL_SERVER_ + INVOICE_API_LIST_INVOICE_SIGN_BY_DATE;
		String errorLog = "Error while requestListDataInvoice";
		wsUrl += "?fromDate="+ fromDate;
		wsUrl += "&toDate="+ toDate;
		wsUrl += "&outletName="+ outletName;
		Log.e("wsUrl", wsUrl);
		return doGet(wsUrl, errorLog);
	}
	public String requestUploadImage(String id_inv, String username, String filePath, String roomNo,String cusName)
	{
		String URL_SERVER_ = StoreSharePreferences.getInstance(mcontext).loadStringServerSavedPreferences(Common.KEY_FINAL_ADDRESS_SERVER);
		String urlSerrver = URL_SERVER_ + INVOICE_API_LIST_INVOICE_UPLOAD_FILE;
		String errorLog = "Error while requestUploadImage";
		try {
			File image = new File(filePath);  //get the actual file from the device
			MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			entity.addPart("id_inv", new StringBody(""+id_inv));
			entity.addPart("username", new StringBody(""+username));
			entity.addPart("pathImgSignedMobile", new StringBody(""+filePath));
			entity.addPart("roomNo", new StringBody(""+roomNo));
			entity.addPart("CusName", new StringBody(""+cusName));
			entity.addPart("file", new FileBody(image));
			return doUploadPOST(urlSerrver, errorLog,entity);
		}catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return "";
	}
	/*
        * This is the method responsible for image upload
        * We need the full image path and the name for the image in this method
        * */
	public ArrayList<InvoiceHDDTTG> parseDataListInvByCus(String strResult)
	{
		ArrayList<InvoiceHDDTTG> arrayList = new ArrayList<>();
		XMLParser xmlParser = new XMLParser();
		Document doc = xmlParser.getDomElement(strResult);
		NodeList nodes = doc.getElementsByTagName("Data");
		for (int i = 0; i < nodes.getLength() ; i++) {
			Element element = (Element) nodes.item(i);
			NodeList childNodes = element.getChildNodes();
			for (int j = 0; j <childNodes.getLength() ; j++) {
				InvoiceHDDTTG invoice = new InvoiceHDDTTG();
				Element elementCode = (Element) childNodes.item(j);
				Element elementChild;
				NodeList childelementCode = elementCode.getChildNodes();
				try {//<index>5</index>
					elementChild = (Element) childelementCode.item(0);//
					String index = xmlParser.getCharacterDataFromElement(elementChild);
					invoice.setIndex(Integer.parseInt(index));
				}catch (Exception ex)
				{
					ex.printStackTrace();
				}
				try {//<invToken>01GTKT0/001;AA/17E;5</invToken>
					elementChild = (Element) childelementCode.item(1);//
					String invToken = xmlParser.getCharacterDataFromElement(elementChild);
					invoice.setInvToken(invToken);
				}catch (Exception ex)
				{
					ex.printStackTrace();
				}
				try {//<fkey>164901GTKT0001AA17E5</fkey>
					elementChild = (Element) childelementCode.item(2);//
					String fkey = xmlParser.getCharacterDataFromElement(elementChild);
					invoice.setFkey(fkey);
				}catch (Exception ex)
				{
					ex.printStackTrace();
				}
				try {// <name>Hóa đơn giá trị gia tăng</name>
					elementChild = (Element) childelementCode.item(3);//
					String name = xmlParser.getCharacterDataFromElement(elementChild);
					invoice.setName(name);
				}catch (Exception ex)
				{
					ex.printStackTrace();
				}
				try {// <publishDate>5/19/2017 10:54:11 AM</publishDate>
					elementChild = (Element) childelementCode.item(4);//
					String publishDate = xmlParser.getCharacterDataFromElement(elementChild);
					invoice.setPublishDate(publishDate);
				}catch (Exception ex)
				{
					ex.printStackTrace();
				}
				try {//<signStatus>4</signStatus>
					elementChild = (Element) childelementCode.item(5);//
					String signStatus = xmlParser.getCharacterDataFromElement(elementChild);
					invoice.setSignStatus(signStatus);
				}catch (Exception ex)
				{
					ex.printStackTrace();
				}
				try {// <total>10000000</total>
					elementChild = (Element) childelementCode.item(6);//
					String total = xmlParser.getCharacterDataFromElement(elementChild);
					invoice.setTotal(Double.parseDouble(total));
				}catch (Exception ex)
				{
					ex.printStackTrace();
				}
				try {//<amount>11000000</amount>
					elementChild = (Element) childelementCode.item(7);//
					String amount = xmlParser.getCharacterDataFromElement(elementChild);
					invoice.setAmount(Double.parseDouble(amount));
				}catch (Exception ex)
				{
					ex.printStackTrace();
				}
				try {//<pattern>01GTKT0/001</pattern>
					elementChild = (Element) childelementCode.item(8);//
					String pattern = xmlParser.getCharacterDataFromElement(elementChild);
					invoice.setPattern(pattern);
				}catch (Exception ex)
				{
					ex.printStackTrace();
				}
				try {//<serial>AA/17E</serial>
					elementChild = (Element) childelementCode.item(9);//
					String serial = xmlParser.getCharacterDataFromElement(elementChild);
					invoice.setSerial(serial);
				}catch (Exception ex)
				{
					ex.printStackTrace();
				}
				try {//<invNum>0000005</invNum>
					elementChild = (Element) childelementCode.item(10);//
					String invNum = xmlParser.getCharacterDataFromElement(elementChild);
					invoice.setInvNum(invNum);
				}catch (Exception ex)
				{
					ex.printStackTrace();
				}
				try {//<status>1</status>
					elementChild = (Element) childelementCode.item(11);//
					String status = xmlParser.getCharacterDataFromElement(elementChild);
					invoice.setStatus(Integer.parseInt(status));
				}catch (Exception ex)
				{
					ex.printStackTrace();
				}
				try {//<cusname><![CDATA[Lê Tấn Trương]]></cusname>
					elementChild = (Element) childelementCode.item(12);//
					String cusname = xmlParser.getCharacterDataFromElement(elementChild);
					invoice.setCusname(cusname);
				}catch (Exception ex)
				{
					ex.printStackTrace();
				}
				try {//<payment>1</payment>
					elementChild = (Element) childelementCode.item(13);//
					String payment = xmlParser.getCharacterDataFromElement(elementChild);
					Log.e("--","--"+payment);
					invoice.setPayment(Integer.parseInt(payment));
					Log.e("--","-getPayment-"+invoice.getPayment());
				}catch (Exception ex)
				{
					ex.printStackTrace();
				}
				arrayList.add(invoice);
			}

		}
		return arrayList;
	}
	public String parseDataGetStringDownloadHTMLInv(String strResult)
	{
		String strHTML = "";
		XMLParser xmlParser = new XMLParser();
		Document doc = xmlParser.getDomElement(strResult);
		NodeList nodes = doc.getChildNodes();
		Element element = (Element) nodes.item(0);
		strHTML = xmlParser.getCharacterDataFromElement(element);
		return strHTML;
	}
	public void parseXMLGesCus(String strResult)
	{
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
		}catch (Exception ex)
		{
			ex.printStackTrace();
		}
		try {
			Element elementName = (Element) childNodes.item(1);
			String name = xmlParser.getCharacterDataFromElement(elementName);
			user.setName(name);
		}catch (Exception ex)
		{
			ex.printStackTrace();
		}
		try {
			Element elementAddress = (Element) childNodes.item(2);
			String address = xmlParser.getCharacterDataFromElement(elementAddress);
			user.setAddress(address);
		}catch (Exception ex)
		{
			ex.printStackTrace();
		}
		try {
			Element elementphone = (Element) childNodes.item(3);
			String phone = xmlParser.getCharacterDataFromElement(elementphone);
			user.setPhone(phone);
		}catch (Exception ex)
		{
			ex.printStackTrace();
		}
		try {
			Element elementtaxcode = (Element) childNodes.item(4);
			String taxcode = xmlParser.getCharacterDataFromElement(elementtaxcode);
			user.setTaxcode(taxcode);
		}catch (Exception ex)
		{
			ex.printStackTrace();
		}
		try {
			Element elementemail = (Element) childNodes.item(5);
			String email = xmlParser.getCharacterDataFromElement(elementemail);
			user.setEmail(email);
		}catch (Exception ex)
		{
			ex.printStackTrace();
		}
		Common.userInfo = user;
	}
	public InvoiceHDDTDetails parseDataXMLFromInvoiceTrG(String strResult) {
		InvoiceHDDTDetails invoiceHDDTDetails = new InvoiceHDDTDetails();
//		String xmlString;  // some XML String previously created
//		XmlToJson xmlToJson = new XmlToJson.Builder(strResult).build();
		String xmlRecords = "<data><terminal_id>1000099999</terminal_id><merchant_id>10004444</merchant_id><merchant_info>Mc Donald's - Abdoun</merchant_info></data>";



//		Log.d("XML1-", strResult);
		Gson gson = new Gson();
		JSONObject jsonObj = null;
		try {
			jsonObj = XML.toJSONObject(strResult);
			JSONObject jsInv = jsonObj.getJSONObject("Inv");
			JSONObject jsInvoice = jsInv.getJSONObject("Invoice");
			String keyInvoice = jsInv.getString("key");
			try {
				if (jsonObj != null) {
					String test = jsInvoice.toString();
//					Log.e("hsContent",test);
					invoiceHDDTDetails = gson.fromJson(jsInvoice.toString(), InvoiceHDDTDetails.class);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			try {
				JSONObject jsonObject = jsInvoice.getJSONObject("Products");
				JSONArray jsArr = jsonObject.getJSONArray("Product");
				ArrayList<ProductInvoiceDetails> arrProductInvoiceDetails = new ArrayList<>();
				for (int i = 0; i < jsArr.length(); i++) {
					ProductInvoiceDetails item = gson.fromJson(jsArr.get(i).toString(), ProductInvoiceDetails.class);
					arrProductInvoiceDetails.add(item);
				}
				invoiceHDDTDetails.setArrayListProduct(arrProductInvoiceDetails);
			} catch (Exception ex) {
				ex.printStackTrace();
				JSONObject jsArr = jsInvoice.getJSONObject("Products");
				JSONObject jsonObject = jsArr.getJSONObject("Product");
				ArrayList<ProductInvoiceDetails> arrProductInvoiceDetails = new ArrayList<>();
				ProductInvoiceDetails item = gson.fromJson(jsonObject.toString(), ProductInvoiceDetails.class);
				arrProductInvoiceDetails.add(item);
				invoiceHDDTDetails.setArrayListProduct(arrProductInvoiceDetails);

			}

		} catch (JSONException e) {
//			Log.e("JSON exception", e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
//			Log.e("JSON exception", e.getMessage());
			e.printStackTrace();
		}
//		Log.d("XML1-", jsonObj.toString());
		return invoiceHDDTDetails;
	}
	public InvoiceHDDTDetails parseDataGetDownloadHTMLInv(String strResult) {
		InvoiceHDDTDetails invoiceHDDTDetails = new InvoiceHDDTDetails();
//		String xmlString;  // some XML String previously created
//		XmlToJson xmlToJson = new XmlToJson.Builder(strResult).build();
		Log.d("XML1-", strResult);
		Gson gson = new Gson();
		JSONObject jsonObj = null;
		try {
			jsonObj = XML.toJSONObject(strResult);
			JSONObject jsInvoice = jsonObj.getJSONObject("Invoice");
			JSONObject jsContent = jsInvoice.getJSONObject("Content");
			try {
				if (jsonObj != null) {
					String test = jsContent.toString();
					Log.e("hsContent",test);
					invoiceHDDTDetails = gson.fromJson(jsContent.toString(), InvoiceHDDTDetails.class);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			try {
				JSONObject jsonObject = jsContent.getJSONObject("Products");
				JSONArray jsArr = jsonObject.getJSONArray("Product");
				ArrayList<ProductInvoiceDetails> arrProductInvoiceDetails = new ArrayList<>();
				for (int i = 0; i < jsArr.length(); i++) {
					ProductInvoiceDetails item = gson.fromJson(jsArr.get(i).toString(), ProductInvoiceDetails.class);
					arrProductInvoiceDetails.add(item);
				}
				invoiceHDDTDetails.setArrayListProduct(arrProductInvoiceDetails);
			} catch (Exception ex) {
				JSONObject jsArr = jsContent.getJSONObject("Products");
				JSONObject jsonObject = jsArr.getJSONObject("Product");
				ArrayList<ProductInvoiceDetails> arrProductInvoiceDetails = new ArrayList<>();
				ProductInvoiceDetails item = gson.fromJson(jsonObject.toString(), ProductInvoiceDetails.class);
				arrProductInvoiceDetails.add(item);
				invoiceHDDTDetails.setArrayListProduct(arrProductInvoiceDetails);
				ex.printStackTrace();
			}

		} catch (JSONException e) {
			Log.e("JSON exception", e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			Log.e("JSON exception", e.getMessage());
			e.printStackTrace();
		}
		Log.d("XML1-", jsonObj.toString());
		return invoiceHDDTDetails;
	}
}
